package com.back_end.myProject.repositorys;

import com.back_end.myProject.entities.DetailBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DetailBillRepository extends JpaRepository<DetailBill, Long> {
    @Query("SELECT SUM(d.quantity * d.medicine.price) FROM DetailBill d")
    Double calculateTotalRevenue();

    @Query("SELECT SUM(d.quantity) FROM DetailBill d")
    Long countSoldProducts();
}
