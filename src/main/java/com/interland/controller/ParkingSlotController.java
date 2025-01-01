package com.interland.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interland.dto.ParkingSlotDto;
import com.interland.dto.ServiceResponse;
import com.interland.dto.Users;
import com.interland.exception.DataNotFoundException;
import com.interland.exception.UserAlreadyExistException;
import com.interland.exception.UserNotFoundException;
import com.interland.service.ParkingSlotService;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@CrossOrigin(origins = "http://localhost:7700/")
@RestController
@RequestMapping("parking")
public class ParkingSlotController {
	
	private final ParkingSlotService parkingSlotService;

	public ParkingSlotController(ParkingSlotService parkingSlotService) {
		super();
		this.parkingSlotService = parkingSlotService;
	}

@PostMapping("/add")
public ResponseEntity<ServiceResponse> bookParkingSlot(@Valid @RequestBody ParkingSlotDto parkingSlotDto) throws UserAlreadyExistException {    
    return new ResponseEntity<>(parkingSlotService.bookNewParkingSlot(parkingSlotDto),HttpStatus.OK);
}
@PutMapping("/update")
public ResponseEntity<ServiceResponse> updateParkingSlot(@Valid @RequestBody ParkingSlotDto parkingSlotDto) throws DataNotFoundException {
    return new ResponseEntity<>(parkingSlotService.updateBppkData(parkingSlotDto),HttpStatus.OK);
}
@PutMapping("/status")
public ResponseEntity<ServiceResponse> updateParkingSlotStatus(@RequestBody ParkingSlotDto parkingSlotDto) throws DataNotFoundException {    
    return new ResponseEntity<>(parkingSlotService.updateStatus(parkingSlotDto),HttpStatus.OK);
}
@PutMapping("/cancel")
public ResponseEntity<ServiceResponse> putMethodName(@RequestBody ParkingSlotDto parkingSlotDto) throws DataNotFoundException {
    return new ResponseEntity<>(parkingSlotService.cancelStatus(parkingSlotDto),HttpStatus.OK);
}
@GetMapping("/get/{bookId}/{vehicleNo}/{phoneNo}")
public ResponseEntity<ServiceResponse> getBookDataById(@PathVariable String bookId,@PathVariable String vehicleNo,@PathVariable String phoneNo ) throws DataNotFoundException {
    return new ResponseEntity<>(parkingSlotService.getDataById(bookId,vehicleNo,phoneNo),HttpStatus.OK);
}

@GetMapping("/parkingSearch")
public ResponseEntity<ServiceResponse> searchByPageGroup(@RequestParam("searchParam") String searchParam,
		@RequestParam("iDisplayStart") String iDisplayStart, @RequestParam("iDisplayLength") String iDisplayLength)
		throws Exception {
	return new ResponseEntity<>(parkingSlotService.parkingSlotSpecifications(searchParam,iDisplayStart,iDisplayLength), new HttpHeaders(), HttpStatus.OK);
}
@PostMapping("/getaccesstoken")
public ResponseEntity<ServiceResponse> getAccessToken(@Valid @RequestBody Users user) throws UserNotFoundException{
	return new ResponseEntity<>(parkingSlotService.getAccessToken(user),HttpStatus.OK);
}
	

}
