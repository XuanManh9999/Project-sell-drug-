package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.CategoryDTO;
import com.back_end.myProject.dto.MedicineDTO;
import com.back_end.myProject.entities.Category;
import com.back_end.myProject.entities.Medicine;
import com.back_end.myProject.repositorys.CategoryRepository;
import com.back_end.myProject.repositorys.MedicineRepository;
import com.back_end.myProject.service.IMedicine;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class MedicineServiceImpl implements IMedicine {
    private final MedicineRepository medicineRepository;
    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    public MedicineServiceImpl(MedicineRepository medicineRepository, CategoryRepository categoryRepository) {
        this.medicineRepository = medicineRepository;
        this.modelMapper = new ModelMapper();
        this.categoryRepository = categoryRepository;
    }
    @Override
    public boolean createMedicine(MedicineDTO medicineDTO) {
        Medicine medicine = medicineRepository.findByName(medicineDTO.getName()).orElse(null);
        if (medicine != null) {
            return false;
        }
        // Nếu thuốc chưa tồn tại, khởi tạo đối tượng Medicine mới
        medicine = new Medicine();
        medicine.setName(medicineDTO.getName());
        medicine.setQuantity(medicineDTO.getQuantity());
        medicine.setStatus(1);
        medicine.setDosage(medicineDTO.getDosage());
        if ( medicineDTO.getId_category() != null) {
            Optional<Category> category = categoryRepository.findById((Long) medicineDTO.getId_category());
            category.ifPresent(medicine::setCategory);
        }
        medicine.setFormulation(medicineDTO.getFormulation());
        medicine.setComposition(medicineDTO.getComposition());
        medicine.setPrice(medicineDTO.getPrice());
        medicine.setUrl_image(medicineDTO.getUrl_image());
        medicine.setUsage_instructions(medicineDTO.getUsage_instructions());
        medicineRepository.save(medicine);
        return true;
    }

    @Override
    public Page<MedicineDTO> getAllMedicines(Pageable pageable) {
        // Sử dụng phương thức tìm kiếm thuốc có số lượng > 0
        Page<Medicine> medicines = medicineRepository.findAllByQuantityGreaterThan(0, pageable);

        return medicines.map(medicine -> {
            MedicineDTO medicineDTO = modelMapper.map(medicine, MedicineDTO.class);

            // Kiểm tra và map Category nếu tồn tại
            if (medicine.getCategory() != null) {
                CategoryDTO categoryDTO = modelMapper.map(medicine.getCategory(), CategoryDTO.class);
                medicineDTO.setCategoryDTO(categoryDTO);
            }
            return medicineDTO;
        });
    }



    @Override
    public MedicineDTO getMedicineById(Long id) {
        Optional<Medicine> medicine = medicineRepository.findById(id);
        if (medicine.isPresent()) {
            MedicineDTO medicineDTO = new MedicineDTO();
            medicineDTO = modelMapper.map(medicine.get(), MedicineDTO.class);
            if (medicine.get().getCategory() != null) {
                CategoryDTO categoryDTO = modelMapper.map(medicine.get().getCategory(), CategoryDTO.class);
                medicineDTO.setCategoryDTO(categoryDTO);
            }
            return medicineDTO;
        }
        return null;
    }

    @Override
    public boolean updateMedicine(MedicineDTO medicineDTO) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineDTO.getId());
        if (optionalMedicine.isPresent()) {
            Medicine medicine = optionalMedicine.get();
            medicine.setName(medicineDTO.getName());
            medicine.setQuantity(medicineDTO.getQuantity());
            medicine.setDosage(medicineDTO.getDosage());
            medicine.setComposition(medicineDTO.getComposition());
            medicine.setFormulation(medicineDTO.getFormulation());
            medicine.setPrice(medicineDTO.getPrice());
            medicine.setUrl_image(medicineDTO.getUrl_image());
            medicine.setUsage_instructions(medicineDTO.getUsage_instructions());
            if (medicineDTO.getId_category() != null) {
                Optional<Category> category = categoryRepository.findById((Long) medicineDTO.getId_category());
                category.ifPresent(medicine::setCategory);
            }else {
                medicine.setCategory(null);
            }
            medicineRepository.save(medicine);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMedicine(Long id) {
        Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
        if (optionalMedicine.isPresent()) {
            medicineRepository.delete(optionalMedicine.get());
            return true;
        }
        return false;
    }

    @Override
    public List<MedicineDTO> findByField(MedicineDTO medicineDTO) {
        return medicineRepository.findByField(medicineDTO);
    }

}
