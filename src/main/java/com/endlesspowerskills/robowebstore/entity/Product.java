package com.endlesspowerskills.robowebstore.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.lang.annotation.Documented;

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

    @NotNull(message = "{product.name.notNull}")
    @Size(min = 1, max=256,message = "{product.name.notNull}")
    private String name;

    @NotNull
    @Digits(integer = 8, fraction = 2, message = "{product.price.priceValidation}")
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
