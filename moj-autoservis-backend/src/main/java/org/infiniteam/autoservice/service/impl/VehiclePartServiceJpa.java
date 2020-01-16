package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.VehiclePart;
import org.infiniteam.autoservice.repository.VehiclePartRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.VehiclePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class VehiclePartServiceJpa implements VehiclePartService {

    @Autowired
    private VehiclePartRepository vehiclePartRepository;

    @Override
    public VehiclePart fetch(long vehiclePartId) {
        return vehiclePartRepository.findById(vehiclePartId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<VehiclePart> findAllByAutoService(AutoService autoService) {
        return vehiclePartRepository.findAllByAutoService(autoService);
    }

    @Override
    public VehiclePart addOrModify(VehiclePart vehiclePart) {
        Assert.hasText(vehiclePart.getPartName(), "Part name shuld not be blank.");
        Assert.isTrue(vehiclePart.getPrice() >= 0, "Price should be non negative");
        Assert.isTrue(vehiclePart.getEstimatedDurationInKm() >= 0, "Kilometers should be non negative");

        return vehiclePartRepository.save(vehiclePart);
    }

    @Override
    public void delete(VehiclePart part) {
        vehiclePartRepository.delete(part);
    }

}
