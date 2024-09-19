package com.back_end.myProject.repositorys.custom;

import com.back_end.myProject.dto.MedicineDTO;

import java.util.List;

public interface IMedicineRepository {
    List<MedicineDTO> findByField(MedicineDTO medicineDTO);
}
