package com.example.assignment1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private String id;
    private String itemId;
    private String cusId;
    private String qtyOnHand;
    private String description;
    private String cusName;
    private String unitPrice;
    private String qty;
    private String total;
}
