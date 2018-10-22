package com.endlesspowerskills.robowebstore.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Product implements Serializable {

    // -- constants
    @Getter(AccessLevel.NONE)
    private static final long serialVersionUID = -5689755290409597380L;

    // -- fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private double price;
    private String description;
    private String manufacturer;
    private String category;
    private int unitsInStock;

    @ToString.Exclude
    private String image;

    @ToString.Exclude
    private byte[] manual;
    @ToString.Exclude
    private String fileType;
    @ToString.Exclude
    private String fileName;

}
