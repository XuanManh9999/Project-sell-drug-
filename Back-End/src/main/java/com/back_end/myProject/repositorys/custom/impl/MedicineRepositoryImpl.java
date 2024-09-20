package com.back_end.myProject.repositorys.custom.impl;

import com.back_end.myProject.dto.MedicineDTO;
import com.back_end.myProject.entities.Medicine;
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
        StringBuilder sql = new StringBuilder("SELECT medicine.id, medicine.name, medicine.quantity, medicine.composition, medicine.dosage, medicine.formulation, medicine.id_category, category.name as name_category, medicine.url_image  FROM medicine INNER JOIN category ON medicine.id_category = category.id where 1 = 1");

        // Thêm các điều kiện vào câu truy vấn nếu có
        if (medicineDTO.getName() != null && !medicineDTO.getName().isEmpty()) {
            sql.append(" AND medicine.name LIKE '%").append(medicineDTO.getName()).append("%'");
        }

        if (medicineDTO.getQuantity() != null) {
            sql.append(" AND medicine.quantity <= ").append(medicineDTO.getQuantity());
        }

        if (medicineDTO.getComposition() != null && !medicineDTO.getComposition().isEmpty()) {
            sql.append(" AND medicine.composition LIKE '%").append(medicineDTO.getComposition()).append("%'");
        }

        if (medicineDTO.getDosage() != null && !medicineDTO.getComposition().isEmpty()) {
            sql.append(" AND medicine.dosage >= ").append(medicineDTO.getDosage());
        }

        if (medicineDTO.getFormulation() != null && !medicineDTO.getFormulation().isEmpty()) {
            sql.append(" AND medicine.formulation = '").append(medicineDTO.getFormulation()).append("'");
        }

        if (medicineDTO.getId_category() != null) {
            sql.append(" AND medicine.id_category = ").append(medicineDTO.getId_category());
        }

        // Khởi tạo danh sách để lưu trữ kết quả
        List<MedicineDTO> medicineDTOList = new ArrayList<>();

        // Thực hiện truy vấn
        Query query = entityManager.createNativeQuery(sql.toString());

        // Lặp qua các kết quả và chuyển đổi chúng thành DTO
        List<Object[]> results = query.getResultList();
        for (Object[] row : results) {
            // Giả sử các cột được trả về theo thứ tự như sau: id, name, quantity, composition, dosage, formulation, id_category
            Long id = ((Number) row[0]).longValue(); // id
            String name = (String) row[1]; // name
            Double quantity = ((Number) row[2]).doubleValue(); // quantity
            String composition = (String) row[3]; // composition
            String dosage = ((String) row[4]); // dosage
            String formulation = (String) row[5]; // formulation
            Long idCategory = ((Number) row[6]).longValue(); // id_category
            String name_category = (String) row[7];
            String url_image = (String) row[8];
            MedicineDTO dto = new MedicineDTO();
            dto.setId(id);
            dto.setName(name);
            dto.setQuantity(quantity);
            dto.setComposition(composition);
            dto.setDosage(dosage);
            dto.setFormulation(formulation);
            dto.setId_category(idCategory);
            dto.setName_category(name_category);
            dto.setUrl_image(url_image);
            medicineDTOList.add(dto);
        }
        return medicineDTOList;
    }

}
