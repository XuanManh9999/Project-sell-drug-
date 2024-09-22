package com.back_end.myProject.repositorys;

import com.back_end.myProject.entities.Medicine;
import com.back_end.myProject.repositorys.custom.IMedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long>, IMedicineRepository {
    Optional<Medicine> findByName(String name);
    Page<Medicine> findAllByQuantityGreaterThan(double quantity, Pageable pageable);
}
