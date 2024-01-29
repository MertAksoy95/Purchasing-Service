package com.emlakjet.purchasing.dto;

import lombok.Data;

@Data
public class BillDto {

    private String firstName;
    private String lastName;
    private String email;
    private double amount;
    private String productName;
    private String billNo;

}
