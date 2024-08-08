package com.example.flowers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ItemDTO implements Serializable {
    private String code;
    private String description;
    private double price;
    private int qty;

}
