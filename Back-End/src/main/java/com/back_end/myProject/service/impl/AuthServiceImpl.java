package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.dto.response.AuthLoginResponse;
import com.back_end.myProject.dto.response.AuthenticationResponse;
import com.back_end.myProject.entities.User;
import com.back_end.myProject.repositorys.UserRepository;
import com.back_end.myProject.service.IAuth;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuth {
    @Value("${jwt.signer.key}")
    private String SIGNER_KEY;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    public final UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(digestBase64("SHA", password));
        userRepository.save(user);
        return true;
    }

    private String digestBase64(String algorithm, String... text) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance(algorithm);

            StringBuilder sb = new StringBuilder();

            for (String t : text) {
                if (sb.length() > 0) {
                    sb.append(":"); // Thay thế cho StringPool.COLON
                }
                sb.append(t);
            }

            String s = sb.toString();

            messageDigest.update(s.getBytes("UTF-8")); // Thay thế Digester.ENCODING bằng UTF-8
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("Error occurred during hashing", e);
            throw new RuntimeException(e);
        }

        byte[] bytes = messageDigest.digest();
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public AuthLoginResponse login(String email, String password) {
        AuthLoginResponse authLoginResponse = new AuthLoginResponse();

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.get();

        String hashedPassword = digestBase64("SHA", password);
        if (!user.getPassword().equals(hashedPassword)) {
            authLoginResponse.setAuthenticated(false);
        }else {
            authLoginResponse.setAuthenticated(true);
            var token = generateToken(user.getEmail(), user.getId());
            authLoginResponse.setToken(token);
        }

        return authLoginResponse;
    }

    @Override
    public UserDTO authenticateUser(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Lấy thời gian hết hạn từ token
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Xác thực token
        var verified = signedJWT.verify(verifier);

        Boolean isUserAuthenticated = verified && expiryTime.after(new Date());

        if (isUserAuthenticated) {
            Long userId = signedJWT.getJWTClaimsSet().getLongClaim("id");
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                UserDTO userDTO = modelMapper.map(userOptional.get(), UserDTO.class);
                return userDTO;
            } else {
                throw new RuntimeException("User không tồn tại.");
            }
        } else {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn.");
        }
    }


    private String generateToken(String email, Long id) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // Dữ liệu trong body gọi là ClaimsSet
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("nguyen-xuan-manh")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(1, ChronoUnit.HOURS).toEpochMilli())) // thời gian hết hạn
                .claim("id", id)
                .claim("customClain", "customClain")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
