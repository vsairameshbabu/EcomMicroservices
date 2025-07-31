package com.app.ecom.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
