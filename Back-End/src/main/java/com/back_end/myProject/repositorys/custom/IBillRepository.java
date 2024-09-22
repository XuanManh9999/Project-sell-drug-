package com.back_end.myProject.repositorys.custom;

import com.back_end.myProject.dto.BillDTO;
import com.back_end.myProject.dto.DetailBillDTO;

import java.util.List;

public interface IBillRepository {
    List<BillDTO> searchBill(BillDTO searchBill);
    BillDTO detailBill(Long id) ;
}
