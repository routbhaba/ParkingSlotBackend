package com.interland.service;

import com.interland.dto.ParkingSlotDto;
import com.interland.dto.ServiceResponse;
import com.interland.dto.Users;
import com.interland.exception.DataNotFoundException;
import com.interland.exception.UserAlreadyExistException;
import com.interland.exception.UserNotFoundException;


public interface ParkingSlotService {

	ServiceResponse bookNewParkingSlot( ParkingSlotDto parkingSlotDto) throws UserAlreadyExistException;

	ServiceResponse updateBppkData( ParkingSlotDto parkingSlotDto) throws DataNotFoundException;

	ServiceResponse updateStatus(ParkingSlotDto parkingSlotDto) throws DataNotFoundException;

	ServiceResponse cancelStatus(ParkingSlotDto parkingSlotDto) throws DataNotFoundException;

	ServiceResponse getDataById(String bookId,String vehicleNo,String PhoneNo) throws DataNotFoundException;

	ServiceResponse parkingSlotSpecifications(String searchParam, String iDisplayStart, String iDisplayLength);
	
	ServiceResponse getAccessToken( Users user) throws UserNotFoundException;


}
