package com.teknikality.DvlaMicroservices.services;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.teknikality.DvlaMicroservices.entities.Vehicle;
import com.teknikality.DvlaMicroservices.repo.VehicleRepository;



@Service
public class VehicleService {

	
	@Autowired
	private VehicleRepository repository;
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Vehicle> getVehicles(){
		
		return repository.findAll();
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
///////////////////////// call from database against registrationNumber///////////////////////////////////////////////
	public Vehicle getVehicle(String registrationNumber) throws IOException, InterruptedException{
		
		Vehicle vehicle = new Vehicle();
		try {
		 vehicle = repository.findByRegistrationNumber(registrationNumber);
		}catch(Exception e) {
			System.out.println(e);
		}
		finally {
		
			logger.info("Vehicle colore " + vehicle.getColour()); 
			logger.info("Vehicle EngineCapacity " + vehicle.getEngineCapacity()); 
			logger.info("Vehicle FuleType " + vehicle.getFuelType()); 
		}
		return vehicle;
		
	}
	
/////////////////////// call from external api against registrationNumber and save the data into database//////////////////
	
	@Transactional
	public Vehicle consumeApi(String registrationNumber) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://driver-vehicle-licensing.api.gov.uk/vehicle-enquiry/v1/vehicles"))
                .header("Content-Type", "application/json")
                .header("x-api-key", "lnJTBRkwbm4Fxf5SWwCAi9l7OPV9pDTB7OvGpt6H")
                .POST(HttpRequest.BodyPublishers.ofString("{\"registrationNumber\" :\""+registrationNumber+"\""+"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        logger.info(response.body());
        ObjectMapper objectMapper = new ObjectMapper();

      Vehicle  vehicle=objectMapper.readValue(response.body(), Vehicle.class);
      if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().equals(null) || vehicle.getRegistrationNumber().isEmpty()) {
			
		}else {
       repository.save(vehicle);
		}
       return vehicle;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/*
	 * Call from external api against registrationNumber and data is not saving in
	 * database, this is using for update vehicle data, here is only getting data
	 * from external api, in update method compares the dates of data and decides
	 * updated the data or not in database
	 */
	
	@Transactional
	public Vehicle getDatafromApi(String registrationNumber) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://driver-vehicle-licensing.api.gov.uk/vehicle-enquiry/v1/vehicles"))
                .header("Content-Type", "application/json")
                .header("x-api-key", "lnJTBRkwbm4Fxf5SWwCAi9l7OPV9pDTB7OvGpt6H")
                .POST(HttpRequest.BodyPublishers.ofString("{\"registrationNumber\" :\""+registrationNumber+"\""+"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        logger.info(response.body());
        ObjectMapper objectMapper = new ObjectMapper();
 
        return objectMapper.readValue(response.body(), Vehicle.class);
		/*
		 * Vehicle vehicle=objectMapper.readValue(response.body(), Vehicle.class);
		 * 
		 * return vehicle;
		 */
        
	}
/////////////////////////////////Delete method,//////////////////////////////////////////////////////////////////////
	
	public String  deleteVehicle(String registrationNumber){
		logger.info("in delete method");
		Vehicle vehicle = repository.findByRegistrationNumber(registrationNumber);
		int a= vehicle.getId();
		logger.info("id is :" +a);
	    repository.deleteById(a);
	    logger.info("vehicle is deleted ");
		return "Vehicle is deleted";
	}
/////////////////////////////////////////////////////Update Vehicle ////////////////////////////////////////////////////	
	@Transactional  
	public Vehicle  updateVehicle(String registrationNumber, Vehicle vehicle1) throws IOException, InterruptedException{
		logger.info("in update method");
		
		// call from external api against registrationNumber 
		 
		 Vehicle  vehicle = getDatafromApi(registrationNumber);
		 logger.info("Vehicle Color " + vehicle1.getColour());
 		 vehicle1.setMake(vehicle.getMake());
		 vehicle1.setFuelType(vehicle.getFuelType());
		 vehicle1.setColour(vehicle.getColour());
		 logger.info("Vehicle color :" + vehicle.getColour());
		 vehicle1.setEngineCapacity(vehicle.getEngineCapacity());
		 vehicle1.setTaxStatus(vehicle.getTaxStatus());
		 
		 vehicle1.setLocalDate(vehicle.getLocalDate());
		 logger.info("Calling Repository.save");
		
//		 data is  saving in database
		 repository.save(vehicle1);
		 logger.info("Vehicle is saved");
//
		 logger.info("vehicle is updated ");
		vehicle = getVehicle(registrationNumber);
		return vehicle;
	}
	
	
	
	/*
	 * String path = "C:\\Users\\shams.ulhasan\\Desktop\\Reports";
	 * 
	 * 
	 * public String expoertReport(String formate) throws JRException, IOException,
	 * InterruptedException {
	 * 
	 * List<Vehicle> vehicles = getVehicles();
	 * 
	 * // load file and compile it
	 * 
	 * File file = ResourceUtils.getFile("classpath:vehicle.jrxml"); JasperReport
	 * jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
	 * 
	 * // now we get Vehicles and map to the report
	 * 
	 * 
	 * JRBeanCollectionDataSource dataSource = new
	 * JRBeanCollectionDataSource(vehicles);
	 * 
	 * // now using dataSource, we can fill the report and design the report
	 * 
	 * Map<String, Object> parameters = new HashMap<>(); parameters.put("CreatedBy",
	 * "Teknikality");
	 * 
	 * JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
	 * parameters, dataSource); if(formate.equalsIgnoreCase("html")){
	 * JasperExportManager.exportReportToHtmlFile(jasperPrint,
	 * path+"\\employees.html"); } if(formate.equalsIgnoreCase("pdf")){
	 * JasperExportManager.exportReportToPdfFile(jasperPrint,
	 * path+"\\employees.pdf"); } return "report is generated in path : " +path; }
	 */
	
	

}
