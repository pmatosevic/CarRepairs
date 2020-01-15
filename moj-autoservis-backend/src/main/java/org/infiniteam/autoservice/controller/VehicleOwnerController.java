package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.security.CurrentUser;
import org.infiniteam.autoservice.service.*;
import org.infiniteam.autoservice.service.huo.HuoConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
@Secured("ROLE_USER")
public class VehicleOwnerController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RepairOrderService repairOrderService;

    @Autowired
    private AutoServiceService autoServiceService;

    @GetMapping("/user")
    @Transactional
    public String carOwnerHome(Model model) {
        VehicleOwner user = getCurrentUser();
        model.addAttribute("vehicles", vehicleService.findAllByOwner(user));
        return "user/vehicleList";
    }

    @GetMapping("/user/vehicles/{id}")
    @Transactional
    public String carDetails(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.fetch(id);
        checkVehicleRights(vehicle);

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("roDisabled", !roCanBeOpened(vehicle));
        model.addAttribute("repairOrders", repairOrderService.findAllByVehicle(vehicle));
        model.addAttribute("autoServices", autoServiceService.findAllActive());

        return "user/vehicle";
    }

    @GetMapping("/user/vehicles/{id}/ro/{roId}")
    @Transactional
    public String repairOrderDetails(@PathVariable Long id, @PathVariable Long roId, Model model) {
        RepairOrder ro = repairOrderService.fetch(roId);
        checkVehicleRights(ro.getVehicle());

        model.addAttribute("ro", ro);

        if (ro instanceof RegularRepairOrder) {
            return "user/regularRoModal :: content";
        } else if (ro instanceof RepairingRepairOrder) {
            return "user/repairingRoModal :: content";
        } else {
            throw new RuntimeException("Broken application.");
        }
    }

    @PostMapping("/user/vehicles")
    @Transactional
    public ResponseEntity<?> addVehicle(@RequestBody String licencePlate) {
        VehicleOwner user = getCurrentUser();
        try {
            Vehicle vehicle = vehicleService.create(licencePlate, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicle.getVehicleId());
        } catch (AlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Vozilo je već dodano.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Vozilo nije pronađeno u HUO registru.");
        }
    }

    @PostMapping("/user/vehicles/{id}/delete")
    @Transactional
    public String deleteVehicle(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.fetch(id);
        checkVehicleRights(vehicle);
        if (roCanBeOpened(vehicle)) {
            vehicle.setOwner(null);
        }
        return "redirect:/user";
    }

    @PostMapping("/user/vehicles/{id}/ro")
    @Transactional
    public ResponseEntity<?> openRepairOrder(@PathVariable Long id, @RequestParam long autoServiceId,
                                             @RequestParam RepairOrderType repairOrderType) {
        Vehicle vehicle = vehicleService.fetch(id);
        AutoService autoservice = autoServiceService.fetch(autoServiceId);

        checkVehicleRights(vehicle);
        if (!roCanBeOpened(vehicle)) {
            return ResponseEntity.badRequest().body("Vozilo je već na servisu.");
        }

        repairOrderService.create(autoservice, vehicle, repairOrderType);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    private boolean roCanBeOpened(Vehicle vehicle) {
        return !repairOrderService.existsByVehicleAndServiceJobStatus(vehicle, ServiceJobStatus.IN_PROGRESS)
                && !repairOrderService.existsByVehicleAndServiceJobStatus(vehicle, ServiceJobStatus.ACCEPTANCE_WAITING);
    }

    private void checkVehicleRights(Vehicle vehicle) {
        if (!vehicle.getOwner().getUserId().equals(getCurrentUser().getUserId())) {
            throw new AccessDeniedException("Forbidden");
        }
    }

    private VehicleOwner getCurrentUser() {
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        return (VehicleOwner) appUser;
    }

}
