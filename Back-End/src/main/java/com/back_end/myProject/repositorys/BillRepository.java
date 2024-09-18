package com.back_end.myProject.repositorys;

import com.back_end.myProject.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Bill findByName(String name);
}
