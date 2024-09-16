package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.entities.User;
import com.back_end.myProject.repositorys.UserRepository;
import com.back_end.myProject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
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
            return user.getPassword().equals(password);
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
        newUser.setPassword(password);
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
}
