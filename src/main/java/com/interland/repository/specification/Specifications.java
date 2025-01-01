package com.interland.repository.specification;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.util.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.interland.entity.ParkingSlot;

@Slf4j
@SuppressWarnings("unused")
public class Specifications {

	

	@SuppressWarnings("serial")
	public static Specification<ParkingSlot> getStreamBySearchSpec(String searchParam) {
		return new Specification<ParkingSlot>() {
 
			@Override
			public Predicate toPredicate(Root<ParkingSlot> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate finalPredicate = null;
				JSONParser parser = new JSONParser();
				JSONObject searchObject;
			
				
				try {
					if (StringUtils.hasLength(searchParam)) {
						searchObject = (JSONObject) parser.parse(searchParam);

						String vehicleType = (String) searchObject.get("vehicleType");
						String customerName=(String) searchObject.get("customerName");
						String dateOfParking=(String) searchObject.get("dateOfParking");
						String vehicleNo=(String) searchObject.get("vehicleNo");
						String status=(String) searchObject.get("status");
						String startDate=(String) searchObject.get("startDate");
						String endDate=(String) searchObject.get("endDate");
						
						

						if (StringUtils.hasLength(vehicleType)) {
							
							Predicate vehicleTypePredicate = criteriaBuilder.like(root.get("vehicleType"),"%"+vehicleType+"%");
							if (finalPredicate != null) {
								finalPredicate = criteriaBuilder.and(finalPredicate, vehicleTypePredicate);
							} else {
								finalPredicate = criteriaBuilder.and(vehicleTypePredicate);
							}

						}
						if (StringUtils.hasLength(customerName)) {
							
							Predicate customerNamePredicate = criteriaBuilder.like(root.get("customerName"),"%"+customerName+"%");
							if (finalPredicate != null) {
								finalPredicate = criteriaBuilder.and(finalPredicate, customerNamePredicate);
							} else {
								finalPredicate = criteriaBuilder.and(customerNamePredicate);
							}
						}
						
						if (StringUtils.hasLength(startDate) && StringUtils.hasLength(endDate)) {
							LocalDate startDat=LocalDate.parse(startDate);
							LocalDate endDat=LocalDate.parse(endDate);
						
							Predicate datePredicate = criteriaBuilder.between(root.get("dateOfParking"),startDat,endDat);
							if (finalPredicate != null) {
								finalPredicate = criteriaBuilder.and(finalPredicate,datePredicate);
							} else {
								finalPredicate = criteriaBuilder.and(datePredicate);
							}}
						
						if (StringUtils.hasLength(dateOfParking)) {
							LocalDate parkDate=LocalDate.parse(dateOfParking);
						
							Predicate datePredicate = criteriaBuilder.equal(root.get("dateOfParking"),parkDate);
							if (finalPredicate != null) {
								finalPredicate = criteriaBuilder.and(finalPredicate,datePredicate);
							} else {
								finalPredicate = criteriaBuilder.and(datePredicate);
							}}
	 
						if (StringUtils.hasLength(status)) {
						
							Predicate statusPredicate = criteriaBuilder.equal(root.get("status"),status);
							if (finalPredicate != null) {
								finalPredicate = criteriaBuilder.and(finalPredicate, statusPredicate);
							} else {
								finalPredicate = criteriaBuilder.and(statusPredicate);
							}
						}
						if(StringUtils.hasLength(vehicleNo)) {
							Predicate vehicleNoPredicate = criteriaBuilder.equal(root.get("parkingSlotPk").get("vehicleNo"),vehicleNo);
							if (finalPredicate != null) {
								finalPredicate = criteriaBuilder.and(finalPredicate, vehicleNoPredicate);
							} else {
								finalPredicate = criteriaBuilder.and(vehicleNoPredicate);
						}

						}    
					}
						
						} catch (ParseException e) {
					log.error(e.getMessage());
				}
				return finalPredicate;
			}
		};

	}

}
