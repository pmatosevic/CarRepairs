package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.repository.VehicleRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.infiniteam.autoservice.service.HuoService;
import org.infiniteam.autoservice.service.HuoServiceException;
import org.infiniteam.autoservice.service.VehicleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@Controller
@Secured("ROLE_USER")
public class VehicleOwnerController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private HuoService huoService;

    @GetMapping("/user")
    public String carOwnerHome(Model model) {
        VehicleOwner user = getCurrentUser();
        model.addAttribute("vehicles", user.getVehicles());
        return "user/home";
    }

    @PostMapping("/user/vehicles")
    @Transactional
    public ResponseEntity addVehicle(@RequestBody String licencePlate) {
        VehicleOwner user = getCurrentUser();
        if (vehicleRepository.existsByLicencePlateAndOwner(licencePlate, user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vozilo već postoji!");
        }

        VehicleData vehicleData;
        try {
            vehicleData = huoService.fetchVehicleData(licencePlate);
        } catch (HuoServiceException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Vozilo nije registrirano u bazi osiguranih vozila!");
        }

        Vehicle vehicle = new Vehicle(vehicleData, user);
        vehicleRepository.saveAndFlush(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle.getVehicleId());
    }

    private VehicleOwner getCurrentUser() {
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        return (VehicleOwner) appUser;
    }

}
