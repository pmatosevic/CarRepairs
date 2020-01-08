package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.dto.OpenRepairOrderDto;
import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.repository.RepairOrderRepository;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.repository.VehicleRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.infiniteam.autoservice.service.HuoService;
import org.infiniteam.autoservice.service.HuoServiceException;
import org.infiniteam.autoservice.service.VehicleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@Secured("ROLE_USER")
public class VehicleOwnerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private AutoServiceRepository autoServiceRepository;

    @Autowired
    private HuoService huoService;

    @GetMapping("/user")
    @Transactional
    public String carOwnerHome(Model model) {
        VehicleOwner user = getCurrentUser();
        model.addAttribute("vehicles", vehicleRepository.findAllByOwner(user));
        return "user/vehicleList";
    }

    @GetMapping("/user/vehicles/{id}")
    @Transactional
    public String carDetails(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        checkVehicleRights(vehicle);

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("roDisabled", !roCanBeOpened(vehicle));
        model.addAttribute("repairOrders", repairOrderRepository.findAllByVehicle(vehicle));
        model.addAttribute("autoServices", autoServiceRepository.findAll());

        return "user/vehicle";
    }

    @GetMapping("/user/vehicles/{id}/ro/{roId}")
    @Transactional
    public String repairOrderDetails(@PathVariable Long id, @PathVariable Long roId, Model model) {
        RepairOrder ro = repairOrderRepository.findById(roId).get();
        checkVehicleRights(ro.getVehicle());

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

    @PostMapping("/user/vehicles/{id}/delete")
    @Transactional
    public String deleteVehicle(@PathVariable Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).get();
        checkVehicleRights(vehicle);
        if (roCanBeOpened(vehicle)) {
            vehicle.setOwner(null);
        }
        return "redirect:/user";
    }

    @PostMapping("/user/vehicles/{id}/ro")
    @Transactional
    public ResponseEntity<?> openRepairOrder(@PathVariable Long id, @RequestBody OpenRepairOrderDto openRepairOrderDto) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        Optional<AutoService> as = autoServiceRepository.findById(openRepairOrderDto.getAutoServiceId());
        if (vehicleOptional.isEmpty() || as.isEmpty()) return ResponseEntity.badRequest().body("Bad request.");

        Vehicle vehicle = vehicleOptional.get();
        checkVehicleRights(vehicle);
        if (!roCanBeOpened(vehicle)) {
            return ResponseEntity.badRequest().body("Vozilo je već na servisu.");
        }

        RepairOrder repairOrder = (openRepairOrderDto.getRepairOrderType() == RepairOrderType.REGULAR_REPAIR_ORDER) ?
                new RegularRepairOrder() : new RepairingRepairOrder();
        repairOrder.setAutoService(as.get());
        repairOrder.setVehicle(vehicle);
        repairOrder.setCreationTime(LocalDateTime.now());
        repairOrderRepository.saveAndFlush(repairOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    private boolean roCanBeOpened(Vehicle vehicle) {
        return !repairOrderRepository.existsByVehicleAndServiceJobStatus(vehicle, ServiceJobStatus.IN_PROGRESS)
                && !repairOrderRepository.existsByVehicleAndServiceJobStatus(vehicle, ServiceJobStatus.ACCEPTANCE_WAITING);
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
