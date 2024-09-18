package com.back_end.myProject.service;

import com.back_end.myProject.dto.MedicineDTO;
import com.back_end.myProject.entities.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMedicine {
    boolean createMedicine(MedicineDTO medicineDTO);
    Page<MedicineDTO> getAllMedicines(Pageable pageable);
    MedicineDTO getMedicineById(Long id);
    boolean updateMedicine(MedicineDTO medicineDTO);
    boolean deleteMedicine(Long id);
}
