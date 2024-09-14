package com.back_end.myProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "detailbill")
@Getter
@Setter
public class DetailBill {
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

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;

}
