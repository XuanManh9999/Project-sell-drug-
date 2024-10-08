package com.back_end.myProject.dto;
import com.back_end.myProject.entities.DetailBill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicineDTO {
    private Long id;
    private String name;
    private Double quantity;
    private String composition;
    private String dosage;
    private Double price;
    private String formulation;
    private String url_image;
    private String usage_instructions;
    private Integer status;
    private String name_category;
    private Long id_category;
    private CategoryDTO categoryDTO;
    private List<DetailBill> detailBills;


}
