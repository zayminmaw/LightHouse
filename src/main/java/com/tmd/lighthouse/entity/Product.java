package com.tmd.lighthouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @Column
    private String genericName;

    @Column
    private String preparation;

    @Column
    private String distributor;

    @Column
    private String manufacturer;

    @Column
    private String country;

    @Column
    private String picture;

    @Column
    private long unit;

    @Column
    private Double price;
}
