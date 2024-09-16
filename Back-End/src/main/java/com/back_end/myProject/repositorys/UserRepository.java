package com.back_end.myProject.repositorys;

import com.back_end.myProject.entities.User;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullname(String fullname);
    // Them phuong thuc phan trang
    Page<User> findAll(Pageable pageable); // Thêm phương thức phân trang
}
