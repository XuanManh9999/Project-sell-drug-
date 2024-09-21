package com.back_end.myProject.repositorys.custom;

import com.back_end.myProject.dto.BillDTO;

import java.util.List;

public interface IBillRepository {
    List<BillDTO> searchBill(BillDTO searchBill);
}
