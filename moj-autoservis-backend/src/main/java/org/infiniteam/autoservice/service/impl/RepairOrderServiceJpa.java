package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.RepairOrderRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RepairOrderServiceJpa implements RepairOrderService {

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Override
    public List<RepairOrder> findAll() {
        return repairOrderRepository.findAll();
    }

    @Override
    public RepairOrder fetch(long repairOrderId) {
        return findById(repairOrderId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public RepairOrder create(AutoService autoService, Vehicle vehicle, RepairOrderType type) {
        RepairOrder repairOrder = (type == RepairOrderType.REGULAR_REPAIR_ORDER) ?
                new RegularRepairOrder() : new RepairingRepairOrder();

        repairOrder.setAutoService(autoService);
        repairOrder.setVehicle(vehicle);
        repairOrder.setCreationTime(LocalDateTime.now());

        return repairOrderRepository.save(repairOrder);
    }

    @Override
    public Optional<RepairOrder> findById(long repairOrderId) {
        return repairOrderRepository.findById(repairOrderId);
    }

    @Override
    public List<RepairOrder> findAllByVehicle(Vehicle vehicle) {
        return repairOrderRepository.findAllByVehicle(vehicle);
    }

    @Override
    public boolean existsByVehicleAndServiceJobStatus(Vehicle vehicle, ServiceJobStatus status) {
        return repairOrderRepository.existsByVehicleAndServiceJobStatus(vehicle, status);
    }

}
