package com.back_end.myProject.entities;

import com.back_end.myProject.dto.DetailBillDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "detailbill")
@Getter
@Setter
public class DetailBill  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status;


    @ManyToOne
    @JoinColumn(name = "id_medicine")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bill")
    @JsonBackReference // Ngăn chặn chu kỳ trong JSON serialization
    private Bill bill;


}
