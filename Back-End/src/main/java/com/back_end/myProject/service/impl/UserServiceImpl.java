package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.dto.request.AuthenticationRequest;
import com.back_end.myProject.dto.request.IntrospeactRequest;
import com.back_end.myProject.dto.response.AuthenticationResponse;
import com.back_end.myProject.dto.response.IntrospeactResponse;
import com.back_end.myProject.entities.User;
import com.back_end.myProject.repositorys.UserRepository;
import com.back_end.myProject.service.UserService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Value("${jwt.signer.key}")
    private String SIGNER_KEY;
    @Autowired
    private  ModelMapper mapper;

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean loginUser(String email, String password) {
        User user =  userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    @Override
    public boolean registerUser(String email, String fullName, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            // email da duoc su dung
            return false;
        }
        // create User
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullname(fullName);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);// 10 la do manh cua thuat toan
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setStatus(1);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true; // Trả về true nếu xóa thành công
        }
        return false; // Trả về false nếu người dùng không tồn tại
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        // Kiểm tra xem người dùng có tồn tại không
        if (userDTO.getId() == null || !userRepository.existsById(userDTO.getId())) {
            return false; // Trả về false nếu người dùng không tồn tại
        }
        // Tìm người dùng hiện tại từ cơ sở dữ liệu
        User existingUser = userRepository.findById(userDTO.getId()).orElse(null);

        if (existingUser == null) {
            return false; // Trả về false nếu không tìm thấy người dùng
        }

        // Cập nhật các thuộc tính của người dùng
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullname(userDTO.getFullname());
        existingUser.setAge(userDTO.getAge());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setPassword(userDTO.getPassword()); // Lưu ý: Nên mã hóa mật khẩu trước khi lưu

        // Lưu người dùng đã cập nhật vào cơ sở dữ liệu
        userRepository.save(existingUser);

        return true; // Trả về true nếu cập nhật thành công
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> listUserDTO = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = mapper.map(user, UserDTO.class);
            listUserDTO.add(userDTO);
        }
        return listUserDTO;
    }

    @Override
    public UserDTO getUserByName(String fullname) {
        // tim trong db
        Optional<User> user = userRepository.findByFullname(fullname);
        return user.map(value -> mapper.map(value, UserDTO.class)).orElse(null);
    }

    @Override
    public UserDTO findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> mapper.map(value, UserDTO.class)).orElse(null);
    }

    @Override
    public boolean createUser(UserDTO userDTO) {
        // find email

        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if (user.isPresent()) {
            return false;
        }
        User newUser = mapper.map(userDTO, User.class);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(user -> mapper.map(user, UserDTO.class));
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (!authenticated) {
                authenticationResponse.setAuthenticated(authenticated);
            } else {
                authenticationResponse.setAuthenticated(authenticated);
                var token = generateToken(request.getEmail());
                authenticationResponse.setAccessToken(token);
            }


        }
        return authenticationResponse;

    }

    @Override
    public IntrospeactResponse introspeact(IntrospeactRequest request) throws JOSEException, ParseException {
        IntrospeactResponse introspeactResponse = new IntrospeactResponse();
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);


        // kiem tra token het han hay chua
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified =  signedJWT.verify(verifier);
        introspeactResponse.setValid(verified && expityTime.after(new Date()));
        return  introspeactResponse;
    }

    private String generateToken(String email) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // header chua thuat toan
        // data trong body goi la ClaimsSet
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(email)
                .issuer("devteria.com").issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customClain", "customClain")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can't not create token");
            throw new RuntimeException(e);
        }
    }
}
