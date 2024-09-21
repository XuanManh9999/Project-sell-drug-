package com.back_end.myProject.repositorys.custom.impl;

import com.back_end.myProject.dto.BillDTO;
import com.back_end.myProject.repositorys.custom.IBillRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BillRepositoryImpl implements IBillRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BillDTO> searchBill(BillDTO searchBill) {
        StringBuilder sql = new StringBuilder("SELECT \n" +
                "    bill.id, \n" +
                "    bill.name, \n" +
                "    bill.customer_name, \n" +
                "    bill.customer_phone, \n" +
                "    COUNT(detailbill.id) AS detail_count\n" +
                "FROM bill\n" +
                "LEFT JOIN detailbill ON bill.id = detailbill.id_bill where 1 = 1\n");
        String name = searchBill.getName();
        String customer_phone = searchBill.getCustomer_phone();
        String customer_name = searchBill.getCustomer_name();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND bill.name like'%").append(name.trim()).append("%'");
        }
        if (customer_phone != null && !customer_phone.isEmpty()) {
            sql.append(" AND bill.customer_phone = '").append(customer_phone.trim()).append("'");
        }
        if (customer_name != null && !customer_name.isEmpty()) {
            sql.append(" AND bill.customer_name like '%").append(customer_name.trim()).append("%'");
        }

        sql.append(" GROUP BY bill.id, bill.name, bill.customer_name, bill.customer_phone ");
        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object []> list = query.getResultList();
        List<BillDTO> billDTOList = new ArrayList<>();
        for (Object[] obj : list) {
            BillDTO billDTO = new BillDTO();
            billDTO.setId((Long) obj[0]);
            billDTO.setName((String) obj[1]);
            billDTO.setCustomer_name((String) obj[2]);
            billDTO.setCustomer_phone((String) obj[3]);
            billDTO.setDetailBill((Long) obj[4]);
            billDTOList.add(billDTO);
        }
        return billDTOList;
    }
}
