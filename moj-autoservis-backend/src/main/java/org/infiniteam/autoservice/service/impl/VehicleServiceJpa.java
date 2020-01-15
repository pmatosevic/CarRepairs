package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.repository.VehicleRepository;
import org.infiniteam.autoservice.service.*;
import org.infiniteam.autoservice.service.huo.HuoConnector;
import org.infiniteam.autoservice.service.huo.HuoServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceJpa implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private HuoConnector huoConnector;

    @Override
    public Vehicle fetch(long vehicleId) {
        return findById(vehicleId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<Vehicle> findById(long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    @Override
    public List<Vehicle> findAllByOwner(VehicleOwner owner) {
        return vehicleRepository.findAllByOwner(owner);
    }

    @Override
    public boolean existsByLicencePlateAndOwner(String licencePlate, VehicleOwner owner) {
        return vehicleRepository.existsByLicencePlateAndOwner(licencePlate, owner);
    }

    @Override
    public Vehicle create(String licencePlate, VehicleOwner owner) {
        if (existsByLicencePlateAndOwner(licencePlate, owner)) {
            throw new AlreadyExistsException("Vozilo već postoji!");
        }

        VehicleData vehicleData;
        try {
            vehicleData = huoConnector.fetchVehicleData(licencePlate);
        } catch (HuoServiceException e) {
            throw new IllegalArgumentException("Nije moguće dohvatiti vozilo iz HUO registra.");
        }

        return vehicleRepository.save(new Vehicle(vehicleData, owner));
    }


}
