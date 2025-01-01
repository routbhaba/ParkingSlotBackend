package com.interland.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.interland.dto.ParkingSlotDto;
import com.interland.dto.ServiceResponse;
import com.interland.dto.Users;
import com.interland.entity.ParkingSlot;
import com.interland.entity.ParkingSlotPk;
import com.interland.exception.DataNotFoundException;
import com.interland.exception.UserAlreadyExistException;
import com.interland.exception.UserNotFoundException;
import com.interland.repository.ParkingSlotRepository;
import com.interland.repository.UserRepository;
import com.interland.repository.specification.Specifications;
import com.interland.util.Constants;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unchecked")
@Slf4j
@Service
public class ParkingSlotServiceImplements implements ParkingSlotService {
	  @Value("${service.url}") 
	  private String url;
	  
	  @Value("${service.client.id}") 
	  private String id;
	  
	  @Value("${service.client.secret}") 
	  private String secret;
	  
	  @Value("${service.client.password}") 
	  private String password; 
	  
	private final ParkingSlotRepository parkingSlotRepository;
	private final MessageSource messageSource;
	private final UserRepository userRepo;
	private final RestTemplate restTemplate;

	
	public ParkingSlotServiceImplements(ParkingSlotRepository parkingSlotRepository, MessageSource messageSource,
			UserRepository userRepo, RestTemplate restTemplate) {
		super();
		this.parkingSlotRepository = parkingSlotRepository;
		this.messageSource = messageSource;
		this.userRepo = userRepo;
		this.restTemplate = restTemplate;
	}

	@Override
	public ServiceResponse bookNewParkingSlot(@Valid ParkingSlotDto parkingSlotDto) throws UserAlreadyExistException {

		ParkingSlotPk parkingPk = buildPk(parkingSlotDto);
		if (parkingSlotRepository.findById(parkingPk).isPresent()) {
			throw new UserAlreadyExistException();
		}
		ParkingSlot parkingSlot = ParkingSlot.builder().parkingSlotPk(parkingPk)
				.customerName(parkingSlotDto.getCustomerName()).vehicleType(parkingSlotDto.getVehicleType())
				.dateOfParking(parkingSlotDto.getDateOfParking()).days(parkingSlotDto.getDays())
				.price(calculateTotalPrice(parkingSlotDto.getDays())).createdDate(LocalDateTime.now())
				.status(Constants.PENDING).build();
		try {
			parkingSlotRepository.save(parkingSlot);
			return new ServiceResponse(Constants.SUCCESS,
					messageSource.getMessage("success.details.m1", null, LocaleContextHolder.getLocale()), List.of());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ServiceResponse(Constants.FAILED,
				messageSource.getMessage("failed.details.m9", null, LocaleContextHolder.getLocale()), List.of());
	}

	@Override
	public ServiceResponse updateBppkData(ParkingSlotDto parkingSlotDto) throws DataNotFoundException {

		ParkingSlotPk parkingPk = buildPk(parkingSlotDto);
		try {
			ParkingSlot parkingSlot = getParkingSlotData(parkingPk);
			if (statusCheck(parkingSlot.getStatus())) {
				return new ServiceResponse(Constants.FAILED,
						messageSource.getMessage("failed.details.m7", null, LocaleContextHolder.getLocale()),
						List.of());
			}
			parkingSlot = ParkingSlot.builder().parkingSlotPk(parkingPk).customerName(parkingSlotDto.getCustomerName())
					.vehicleType(parkingSlotDto.getVehicleType()).dateOfParking(parkingSlotDto.getDateOfParking())
					.days(parkingSlotDto.getDays()).price(calculateTotalPrice(parkingSlotDto.getDays()))
					.createdDate(parkingSlot.getCreatedDate()).status(parkingSlot.getStatus()).build();
			parkingSlotRepository.save(parkingSlot);
			return new ServiceResponse(Constants.SUCCESS,
					messageSource.getMessage("success.details.m2", null, LocaleContextHolder.getLocale()), List.of());

		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ServiceResponse(Constants.FAILED,
				messageSource.getMessage("failed.details.m7", null, LocaleContextHolder.getLocale()), List.of());
	}

	@Override
	public ServiceResponse updateStatus(ParkingSlotDto parkingSlotDto) throws DataNotFoundException {
		ParkingSlotPk parkingPk = buildPk(parkingSlotDto);
		try {
			ParkingSlot parkingSlot = getParkingSlotData(parkingPk);
			String status = statusChange(parkingSlot.getStatus());
			if (status.equals("")) {
				return new ServiceResponse(Constants.FAILED,
						messageSource.getMessage("failed.details.m5", null, LocaleContextHolder.getLocale()),
						List.of());
			}
			parkingSlot = ParkingSlot.builder().parkingSlotPk(parkingPk).customerName(parkingSlot.getCustomerName())
					.vehicleType(parkingSlot.getVehicleType()).dateOfParking(parkingSlot.getDateOfParking())
					.days(parkingSlot.getDays()).price(parkingSlot.getPrice()).createdDate(parkingSlot.getCreatedDate())
					.status(status).build();
			parkingSlotRepository.save(parkingSlot);
			return new ServiceResponse(Constants.SUCCESS,
					messageSource.getMessage("success.details.m3", null, LocaleContextHolder.getLocale()), List.of());
		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ServiceResponse(Constants.FAILED,
				messageSource.getMessage("failed.details.m5", null, LocaleContextHolder.getLocale()), List.of());
	}

	@Override
	public ServiceResponse cancelStatus(ParkingSlotDto parkingSlotDto) throws DataNotFoundException {
		ParkingSlotPk parkingPk = buildPk(parkingSlotDto);
		try {
			ParkingSlot parkingSlot = getParkingSlotData(parkingPk);
			if (statusCheck(parkingSlot.getStatus())) {
				return new ServiceResponse(Constants.FAILED,
						messageSource.getMessage("failed.details.m5", null, LocaleContextHolder.getLocale()),
						List.of());
			}
			parkingSlot = ParkingSlot.builder().parkingSlotPk(parkingPk).customerName(parkingSlot.getCustomerName())
					.vehicleType(parkingSlot.getVehicleType()).dateOfParking(parkingSlot.getDateOfParking())
					.days(parkingSlot.getDays()).price(parkingSlot.getPrice()).createdDate(parkingSlot.getCreatedDate())
					.status(Constants.CANCEL).build();
			parkingSlotRepository.save(parkingSlot);
			return new ServiceResponse(Constants.SUCCESS,
					messageSource.getMessage("success.details.m3", null, LocaleContextHolder.getLocale()), List.of());
		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ServiceResponse(Constants.FAILED,
				messageSource.getMessage("failed.details.m5", null, LocaleContextHolder.getLocale()), List.of());
	}

	@Override
	public ServiceResponse getDataById(String bookId, String vehicleNo, String phoneNo) throws DataNotFoundException {
		ParkingSlotPk parkingPk = ParkingSlotPk.builder().bookId(bookId).vehicleNo(vehicleNo).phoneNo(phoneNo).build();
		try {
			ParkingSlot parkingSlot = getParkingSlotData(parkingPk);
			ParkingSlotDto sendDto = convertToDto(parkingSlot);
			JSONObject dtoObject = new JSONObject();
			dtoObject.put("Data", sendDto);
			return new ServiceResponse(Constants.SUCCESS,
					messageSource.getMessage("success.details.m4", null, LocaleContextHolder.getLocale()),
					List.of(dtoObject));
		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ServiceResponse(Constants.FAILED,
				messageSource.getMessage("failed.details.m8", null, LocaleContextHolder.getLocale()), List.of());
	}

	@Override
	public ServiceResponse parkingSlotSpecifications(String searchParam, String displayStart, String displayLength) {
		JSONObject result = new JSONObject();
		try {
			int start = Integer.parseInt(displayStart);
			int pageSize = Integer.parseInt(displayLength);
			Pageable pageable = PageRequest.of(start, pageSize);
			Page<ParkingSlot> dataList = parkingSlotRepository
					.findAll(Specifications.getStreamBySearchSpec(searchParam), pageable);
			long count = parkingSlotRepository.count(Specifications.getStreamBySearchSpec(searchParam));
			JSONArray array = new JSONArray();
			for (ParkingSlot parkingSlot : dataList) {
				array.add(convertToDto(parkingSlot));
			}
			result.put(Constants.AA_DATA, array);
			result.put(Constants.TOTAL_DISPLAY_RECORD, count);
			result.put(Constants.TOTAL_RECORD, count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			result.put(Constants.AA_DATA, new JSONArray());
			result.put(Constants.TOTAL_DISPLAY_RECORD, 0);
			result.put(Constants.TOTAL_RECORD, 0);
		}
		return new ServiceResponse(Constants.SUCCESS,
				messageSource.getMessage("success.details.m1", null, LocaleContextHolder.getLocale()), List.of(result));
	}
	@Override
	public ServiceResponse getAccessToken(Users user) throws UserNotFoundException {
		try {
			if (userRepo.findById(user.getUserName()).isEmpty()) {
				throw new UserNotFoundException();
			}
			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("grant_type",password);
			formData.add("client_id",id );
			formData.add("username", user.getUserName());
			formData.add("password", user.getPassword());
			formData.add("client_secret",secret);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			JSONObject accessToken=new JSONObject();
			accessToken.put("Data", restTemplate.exchange(url, HttpMethod.POST,  new HttpEntity<>(formData, headers), Object.class));
			return new ServiceResponse(Constants.SUCCESS,messageSource.getMessage("success.details.m1", null, LocaleContextHolder.getLocale()),List.of(accessToken)) ;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	private ParkingSlotDto convertToDto(ParkingSlot parkingSlot) {
		return ParkingSlotDto.builder().bookId(parkingSlot.getParkingSlotPk().getBookId())
				.vehicleNo(parkingSlot.getParkingSlotPk().getVehicleNo())
				.phoneNo(parkingSlot.getParkingSlotPk().getPhoneNo()).customerName(parkingSlot.getCustomerName())
				.vehicleType(parkingSlot.getVehicleType()).dateOfParking(parkingSlot.getDateOfParking())
				.days(parkingSlot.getDays()).price(parkingSlot.getPrice())
				.status(parkingSlot.getStatus()).build();
	}

	private boolean statusCheck(String status) {
		return switch (status) {
		case Constants.DEPARTED -> true;
		case Constants.CANCEL -> true;
		case Constants.PARKED -> true;
		default -> false;
		};
	}

	private ParkingSlot getParkingSlotData(ParkingSlotPk parkingPk) throws DataNotFoundException {
		return parkingSlotRepository.findById(parkingPk).orElseThrow(DataNotFoundException::new);
	}

	private double calculateTotalPrice(int days) {
		return 100.00 * days;
	}

	public String statusChange(String status) {
		return switch (status) {
		case Constants.PENDING -> Constants.BOOKED;
		case Constants.BOOKED -> Constants.PARKED;
		case Constants.PARKED -> Constants.DEPARTED;
		default -> "";
		};

	}

	private ParkingSlotPk buildPk(ParkingSlotDto parkingSlotDto) {
		return ParkingSlotPk.builder().bookId(parkingSlotDto.getBookId()).vehicleNo(parkingSlotDto.getVehicleNo())
				.phoneNo(parkingSlotDto.getPhoneNo()).build();
	}
}
