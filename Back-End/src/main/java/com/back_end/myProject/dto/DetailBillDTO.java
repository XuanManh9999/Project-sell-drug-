package com.back_end.myProject.dto;

import com.back_end.myProject.entities.Bill;
import com.back_end.myProject.entities.Medicine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailBillDTO {
    private Long id;
    private Double quantity;
    private Integer status;
    private Long id_medicine;
    private Long id_bill;
    private MedicineDTO medicineDTO;

}
