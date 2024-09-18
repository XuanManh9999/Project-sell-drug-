package com.back_end.myProject.service;

import com.back_end.myProject.dto.BillDTO;
import com.back_end.myProject.entities.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBill {
    boolean creayeBill(BillDTO billDTO);
    boolean updateBill(BillDTO billDTO);
    boolean deleteBill(Long id);
    Page<Bill> getAllBills(Pageable pageable);
}
