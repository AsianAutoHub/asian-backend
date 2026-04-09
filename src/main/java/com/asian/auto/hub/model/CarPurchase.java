package com.asian.auto.hub.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.asian.auto.hub.enums.FuelType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "car_purchase")
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class CarPurchase {
	
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

	@Column(name = "number_plate")
  private String numberPlate;
	
	@Column(name = "car_model")
  private String model;
	
	@Column(name = "purchase_amount")
  private Double purchaseAmount;
	
	@Column(name = "purchase_date")
  private LocalDate purchaseDate;
	
	@Column(name = "purchase_from")
  private String purchaseFrom;
	
	@Column(name = "saled_amount")
  private Double saledAmount;
	
	@Column(name = "saled_date")
  private LocalDate saledDate;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@Column(name = "colour")
	private String colour;
	
	@Column(name = "fuel_type")
  @Enumerated(EnumType.STRING)
  private FuelType fuelType;
  
  @Column(name = "kmps")
  private Double kmps;
  
  @Column(name = "mfg_year")
  private Integer manufacturedYear;

  @ManyToOne
  @JoinColumn(name = "purchased_by")
  private User purchasedBy;

  @OneToMany(mappedBy = "carPurchase", cascade = CascadeType.ALL)
  private List<CarExpense> expenses;
  
	@Column(name = "created_by")
  private String created_by;
  
  @Column(name = "updated_by")
  private String updated_by;
  
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

  @Column(name = "created_on")
  private LocalDateTime createdOn;

}
