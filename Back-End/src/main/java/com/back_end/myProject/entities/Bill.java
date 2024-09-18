package com.back_end.myProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "bill")
@Getter
@Setter
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status;
    @Column(name = "customer_phone")
    private String customer_phone;

    @Column(name = "customer_name")
    private String customerName;

    @OneToMany(mappedBy = "bill")
    private List<DetailBill> listDetailBill;
}
