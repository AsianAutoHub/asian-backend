package com.asian.auto.hub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

import com.asian.auto.hub.enums.FuelType;

import lombok.Data;

@Data
public class CarPurchaseDto {
    
    
    @NotBlank(message = "Number plate is required")
    private String numberPlate;

    @NotBlank(message = "Car model is required")
    private String model;

    @NotNull(message = "Purchase amount is required")
    @Positive(message = "Purchase amount must be positive")
    private Double purchaseAmount;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotBlank(message = "Purchase from is required")
    private String purchaseFrom;

    private Double saledAmount;
    private LocalDate saledDate;

    @NotNull(message = "Purchased by user ID is required")
    private Long purchasedById;
    
    @NotBlank(message = "Colour is required")
    private String colour;
    
    @NotNull(message = "fuel type is required")
    private FuelType fuelType;
    
    @NotNull(message = "kmps is required")
    @Positive(message = "kmps must be positive")
    private Double kmps;
    
    @NotNull(message = "mfgYear is required")
    @Positive(message = "mfgYear must be positive")
    private Integer manufacturedYear;
}
