package com.teknikality.DvlaMicroservices.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teknikality.DvlaMicroservices.entities.Vehicle;



public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
	 Vehicle  findByRegistrationNumber(String registrationNumber);
	 Long deleteByRegistrationNumber(String registrationNumber);

}

