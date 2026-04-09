package com.asian.auto.hub.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.asian.auto.hub.enums.FuelType;

import lombok.Data;

@Data
public class CarPurchaseResponseDto {
	 private Long id;
   private String numberPlate;
   private String model;
   private Double purchaseAmount;
   private LocalDate purchaseDate;
   private String purchaseFrom;
   private Double saledAmount;
   private LocalDate saledDate;
   private String purchasedByFirstname;   
   private String purchasedByEmail;
   private String colour;
   private FuelType fuelType;
   private Double kmps;
   private Integer manufacturedYear;
   private String createdBy;
   private String updatedBy;
   private LocalDateTime createdOn;
   private LocalDateTime updatedOn;
}
