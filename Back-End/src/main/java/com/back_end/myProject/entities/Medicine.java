package com.back_end.myProject.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "medicine")
@Getter
@Setter
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Double quantity;


    @Column(name = "composition")
    private String composition;
    @Column(name = "dosage")
    private String dosage;
    @Column(name = "formulation")
    private String formulation;
    @Column(columnDefinition = "TEXT", name = "url_image")
    private String url_image;
    @Column(columnDefinition = "TEXT", name = "usage_instructions")
    private String usage_instructions;

    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private Category category;

    @OneToMany(mappedBy = "medicine")
    private List<DetailBill> detailBillList;

}
