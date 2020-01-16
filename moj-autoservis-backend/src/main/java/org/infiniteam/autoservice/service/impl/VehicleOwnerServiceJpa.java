package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.repository.VehicleOwnerRepository;
import org.infiniteam.autoservice.repository.VehicleRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.Utility;
import org.infiniteam.autoservice.service.VehicleOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class VehicleOwnerServiceJpa implements VehicleOwnerService {

    @Autowired
    private VehicleOwnerRepository vehicleOwnerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public VehicleOwner fetch(long userId) {
        return vehicleOwnerRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<VehicleOwner> findAll() {
        return vehicleOwnerRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(VehicleOwner vehicleOwner) {
        List<Vehicle> vehicles = vehicleRepository.findAllByOwner(vehicleOwner);
        for (Vehicle vehicle : vehicles) {
            vehicle.setOwner(null);
        }

        vehicleOwnerRepository.delete(vehicleOwner);
    }

    @Override
    public VehicleOwner create(VehicleOwner vehicleOwner) {
        Utility.validateExists(vehicleOwner, userRepository);
        Utility.checkUsername(vehicleOwner.getUsername());
        Utility.checkPassword(vehicleOwner.getPasswordHash());
        Utility.validateModifiableData(vehicleOwner);
        Utility.checkOib(vehicleOwner.getOib());

        vehicleOwner.setPasswordHash(passwordEncoder.encode(vehicleOwner.getPasswordHash()));
        return vehicleOwnerRepository.saveAndFlush(vehicleOwner);
    }

}
