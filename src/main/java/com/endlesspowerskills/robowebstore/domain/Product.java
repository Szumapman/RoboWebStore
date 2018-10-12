package com.endlesspowerskills.robowebstore.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
    private String image;

}
