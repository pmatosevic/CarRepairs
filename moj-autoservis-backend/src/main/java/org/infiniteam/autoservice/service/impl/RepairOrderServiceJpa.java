package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.controller.BadRequestException;
import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.RepairOrderRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.infiniteam.autoservice.model.RepairOrderType.REGULAR_REPAIR_ORDER;

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
    public RegularRepairOrder fetchRegularRepairOrder(long repairOrderId) {
        RepairOrder ro = fetch(repairOrderId);
        if (!(ro instanceof RegularRepairOrder)) throw new EntityNotFoundException();
        return (RegularRepairOrder) ro;
    }

    @Override
    public RepairingRepairOrder fetchRepairingRepairOrder(long repairOrderId) {
        RepairOrder ro = fetch(repairOrderId);
        if (!(ro instanceof RepairingRepairOrder)) throw new EntityNotFoundException();
        return (RepairingRepairOrder) ro;
    }

    @Override
    public RepairOrder create(AutoService autoService, Vehicle vehicle, RepairOrderType type) {
        RepairOrder repairOrder = (type == REGULAR_REPAIR_ORDER) ?
                new RegularRepairOrder() : new RepairingRepairOrder();

        repairOrder.setAutoService(autoService);
        repairOrder.setVehicle(vehicle);
        repairOrder.setCreationTime(LocalDateTime.now());
        repairOrder.setPrice(type == REGULAR_REPAIR_ORDER ? autoService.getRegularServicePrice() : 0.0);

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

    @Override
    public List<RepairOrder> findAllByServiceJobStatusAndAutoService(ServiceJobStatus status, AutoService autoService) {
        return repairOrderRepository.findAllByServiceJobStatusAndAutoService(status, autoService);
    }

    @Override
    public void setFinished(RepairOrder repairOrder) {
        Assert.isTrue(repairOrder.getServiceJobStatus() == ServiceJobStatus.IN_PROGRESS, "RO has to be in progress.");
        repairOrder.setServiceJobStatus(ServiceJobStatus.FINISHED);
        repairOrder.setFinishTime(LocalDateTime.now());
        repairOrderRepository.saveAndFlush(repairOrder);
    }

    @Override
    public void updateRegularRepairOrder(RegularRepairOrder ro, int kilometers, boolean repairRecommended) {
        Assert.isTrue(kilometers >= 0, "Kilometers must be non-negative.");
        ro.setKilometers(kilometers);
        ro.setRepairRecommended(repairRecommended);
        repairOrderRepository.saveAndFlush(ro);
    }

    @Override
    @Transactional
    public void removeItemFromOrder(RepairingRepairOrder ro, long itemId) {
        Assert.isTrue(ro.getServiceJobStatus() == ServiceJobStatus.IN_PROGRESS, "RO should be in progress.");

        RepairOrderItem item = null;
        for (RepairOrderItem item1 : ro.getItems()) {
            if (item1.getItemId().equals(itemId)) item = item1;
        }
        if (item == null) throw new EntityNotFoundException("Item not found.");
        ro.removeItem(item);
        ro.setPrice(ro.getPrice() - item.getPrice());

        repairOrderRepository.flush();
    }

    @Override
    @Transactional
    public void addItemToOrder(RepairingRepairOrder ro, Product product) {
        Assert.hasText(product.getName(), "Name should not be empty.");
        Assert.isTrue(product.getPrice() >= 0, "Price should be non negative.");
        Assert.isTrue(ro.getServiceJobStatus() == ServiceJobStatus.IN_PROGRESS, "RO should be in progress.");

        RepairOrderItem item = new RepairOrderItem();
        item.setName(product.getName());
        item.setPrice(product.getPrice());
        ro.addItem(item);
        ro.setPrice(ro.getPrice() + item.getPrice());

        repairOrderRepository.flush();
    }


}
