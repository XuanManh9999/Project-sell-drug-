package com.back_end.myProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;


    @Column (columnDefinition = "int default 1", name = "status")
    private Integer status;

    @OneToMany(mappedBy = "category")
    private List<Medicine> medicines;

}
