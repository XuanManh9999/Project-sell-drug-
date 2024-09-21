package com.back_end.myProject.repositorys;

import com.back_end.myProject.entities.Bill;
import com.back_end.myProject.repositorys.custom.IBillRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long>, IBillRepository {
    Optional<Bill> findByName(String name);
}
