package com.back_end.myProject.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillDTO {
    private Long id;
    private String name;
    private Integer status;
    private String customer_phone;
    private String customer_name;
    private Integer quantity_product;
    private Object detailBill;
}
