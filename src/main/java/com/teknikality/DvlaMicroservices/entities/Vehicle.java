package com.teknikality.DvlaMicroservices.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="vehicle")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Details about Vehicle")
public class Vehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@NotFound(action = NotFoundAction.IGNORE)
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "id value is auto generated")
	private	int id;
	
	
	@Column(name="registrationNumber", unique=true)
	@ApiModelProperty(notes = "registration Value is Unique")
	private	String registrationNumber;
	
	@Column(name="make")
	@ApiModelProperty(notes = "make describes manufacturer is auto generated")
	private	String make;
	
	@Column(name="fuelType")
	@ApiModelProperty(notes = "which type of fuel vehicle consume")
	private	String fuelType;
	
	@Column(name="colour")
	@ApiModelProperty(notes = "colore of vehicle")
	private String colour;
	
	@Column(name="engineCapacity")
	@ApiModelProperty(notes = "EngineCapacity in terms of Litters")
	private	int engineCapacity;
	
	@Column(name="taxStatus")
	@ApiModelProperty(notes = "shows tax status of vehicle")
	private	String taxStatus;
	
	@Column(name = "date")
	@ApiModelProperty(notes = "date of retriving vehicle data")
	private LocalDate localDate = LocalDate.now(ZoneId.of("GMT+02:30"));
	
	

	
	
}
