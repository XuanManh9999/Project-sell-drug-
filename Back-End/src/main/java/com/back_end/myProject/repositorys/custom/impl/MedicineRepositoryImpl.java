package com.back_end.myProject.repositorys.custom.impl;

import com.back_end.myProject.dto.MedicineDTO;
import com.back_end.myProject.repositorys.custom.IMedicineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicineRepositoryImpl implements IMedicineRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MedicineDTO> findByField(MedicineDTO medicineDTO) {
        StringBuilder sql = new StringBuilder("SELECT * FROM medicine INNER JOIN category ON medicine.id_category = category.id WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (medicineDTO.getName() != null) {
            sql.append(" AND medicine.name LIKE ?");
            parameters.add("%" + medicineDTO.getName() + "%");
        }
        if (medicineDTO.getQuantity() != null) {
            sql.append(" AND medicine.quantity <= ?");
            parameters.add(medicineDTO.getQuantity());
        }
        if (medicineDTO.getComposition() != null) {
            sql.append(" AND medicine.composition = ?");
            parameters.add(medicineDTO.getComposition());
        }
        if (medicineDTO.getDosage() != null) {
            sql.append(" AND medicine.dosage >= ?");
            parameters.add(medicineDTO.getDosage());
        }
        if (medicineDTO.getFormulation() != null) {
            sql.append(" AND medicine.formulation = ?");
            parameters.add(medicineDTO.getFormulation());
        }
        if (medicineDTO.getId_category() != null) {
            sql.append(" AND medicine.id_category = ?");
            parameters.add(medicineDTO.getId_category());
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        // Set parameters
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        // Assuming you need to map results manually as MedicineDTO
        List<Object[]> results = query.getResultList();
        List<MedicineDTO> medicineList = new ArrayList<>();
        for (Object[] result : results) {
            // Map result to MedicineDTO here
            // For example:
            MedicineDTO dto = new MedicineDTO();
            dto.setName((String) result[0]);
            // Set other fields
            medicineList.add(dto);
        }

        return medicineList;
    }
}
