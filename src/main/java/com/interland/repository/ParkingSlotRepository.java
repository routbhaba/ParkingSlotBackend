package com.interland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.interland.entity.ParkingSlot;
import com.interland.entity.ParkingSlotPk;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, ParkingSlotPk>,JpaSpecificationExecutor<ParkingSlot> {

}
