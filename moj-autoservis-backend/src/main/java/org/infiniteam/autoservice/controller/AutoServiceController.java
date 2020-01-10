package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.*;
import org.infiniteam.autoservice.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@Controller
@Secured("ROLE_SERVICE_EMPLOYEE")
public class AutoServiceController {

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private VehiclePartRepository vehiclePartRepository;

    @Autowired
    private ServiceLaborRepository serviceLaborRepository;

    @Autowired
    private ServiceEmployeeRepository serviceEmployeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/autoservice")
    public String autoserviceHome(Model model) {
        return "redirect:/autoservice/repairOrders/waiting";
    }

    @GetMapping("/autoservice/repairOrders/waiting")
    public String waitingRepairOrders(Model model) {
        AutoService autoService = getUserAutoService();
        List<RepairOrder> repairOrders = repairOrderRepository.findAllByServiceJobStatusAndAutoService(
                ServiceJobStatus.ACCEPTANCE_WAITING, autoService);
        model.addAttribute("repairOrders", repairOrders);
        return "autoservice/newOrders";
    }

    @GetMapping("/autoservice/repairOrders/opened")
    public String currentRepairOrders(Model model) {
        AutoService autoService = getUserAutoService();
        List<RepairOrder> repairOrders = repairOrderRepository.findAllByServiceJobStatusAndAutoService(
                ServiceJobStatus.IN_PROGRESS, autoService);
        model.addAttribute("repairOrders", repairOrders);
        return "autoservice/currentOrders";
    }

    @GetMapping("/autoservice/repairOrders/{id}")
    public String showRepairOrder(Model model, @PathVariable Long id) {
        Optional<RepairOrder> repairOrderOptional = repairOrderRepository.findById(id);
        if (repairOrderOptional.isEmpty()) throw new ResourceNotFoundException();

        RepairOrder repairOrder = repairOrderOptional.get();
        if (repairOrder instanceof RegularRepairOrder) {
            return "autoservice/editRegularRo";
        } else if (repairOrder instanceof RepairingRepairOrder) {
            return "autoservice/editRepairingRo";
        } else {
            throw new RuntimeException("Not implemented.");
        }
    }

    @GetMapping("/autoservice/priceList")
    @Secured("ROLE_SERVICE_ADMIN")
    public String showPriceList(Model model) {
        List<VehiclePart> parts = vehiclePartRepository.findAll();
        List<ServiceLabor> labors = serviceLaborRepository.findAll();
        model.addAttribute("parts", parts);
        model.addAttribute("labors", labors);
        return "autoservice/priceList";
    }

    @GetMapping("/autoservice/priceList/parts/{id}")
    @Secured("ROLE_SERVICE_ADMIN")
    public String vehiclePartModal(@PathVariable Long id, Model model) {
        VehiclePart part = id == 0 ? new VehiclePart() : vehiclePartRepository.findById(id).get();
        model.addAttribute("part", part);
        return "/autoservice/vehiclePartModal :: content";
    }

    @PostMapping("/autoservice/priceList/parts/{id}/delete")
    @Secured("ROLE_SERVICE_ADMIN")
    public String vehiclePartRemove(@PathVariable Long id) {
        VehiclePart part = vehiclePartRepository.findById(id).get();
        checkAutoServiceAccess(part.getAutoService());
        vehiclePartRepository.delete(part);
        return "redirect:/autoservice/priceList";
    }

    @PostMapping("/autoservice/priceList/parts")
    @Secured("ROLE_SERVICE_ADMIN")
    public String updateVehiclePart(@RequestParam String partName, @RequestParam int estimatedDuration,
                                    @RequestParam double price, @RequestParam Long id) {
        VehiclePart part;
        if (id != null && id != 0) {
            part = vehiclePartRepository.findById(id).get();
            checkAutoServiceAccess(part.getAutoService());
        } else {
            part = new VehiclePart();
            part.setAutoService(getUserAutoService());
        }
        part.setEstimatedDurationInKm(estimatedDuration);
        part.setPrice(price);
        part.setPartName(partName);
        vehiclePartRepository.save(part);
        return "redirect:/autoservice/priceList";
    }

    @GetMapping("/autoservice/priceList/labors/{id}")
    @Secured("ROLE_SERVICE_ADMIN")
    public String serviceLaborModal(@PathVariable Long id, Model model) {
        ServiceLabor labor = id == 0 ? new ServiceLabor() : serviceLaborRepository.findById(id).get();
        model.addAttribute("labor", labor);
        return "/autoservice/serviceLaborModal :: content";
    }

    @PostMapping("/autoservice/priceList/labors")
    @Secured("ROLE_SERVICE_ADMIN")
    public String updateServiceLabor(@RequestParam String serviceName,
                                     @RequestParam double price, @RequestParam Long id) {
        ServiceLabor serviceLabor;
        if (id != null && id != 0) {
            serviceLabor = serviceLaborRepository.findById(id).get();
            checkAutoServiceAccess(serviceLabor.getAutoService());
        } else {
            serviceLabor = new ServiceLabor();
            serviceLabor.setAutoService(getUserAutoService());
        }
        serviceLabor.setServiceName(serviceName);
        serviceLabor.setPrice(price);
        serviceLaborRepository.save(serviceLabor);
        return "redirect:/autoservice/priceList";
    }

    @PostMapping("/autoservice/priceList/labors/{id}/delete")
    @Secured("ROLE_SERVICE_ADMIN")
    public String serviceLaborRemove(@PathVariable Long id) {
        ServiceLabor labor = serviceLaborRepository.findById(id).get();
        checkAutoServiceAccess(labor.getAutoService());
        serviceLaborRepository.delete(labor);
        return "redirect:/autoservice/priceList";
    }

    @GetMapping("/autoservice/employees")
    @Secured("ROLE_SERVICE_ADMIN")
    public String autoserviceEmployees(Model model) {
        List<ServiceEmployee> employees = serviceEmployeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "autoservice/employees";
    }

    @GetMapping("/autoservice/details")
    @Secured("ROLE_SERVICE_ADMIN")
    public String autoserviceDetails(Model model){
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        ServiceEmployee user2 = (ServiceEmployee) appUser;
        AutoService autoSerivce = user2.getAutoService();
        model.addAttribute("service", autoSerivce);
        return "autoservice/details";
    }

    @PostMapping("/autoservice/details")
    @Secured("ROLE_SERVICE_ADMIN")
    public String changeServiceSettings(@RequestParam String shopName, @RequestParam String adress){
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        ServiceEmployee user2 = (ServiceEmployee) appUser;
        AutoService autoService = user2.getAutoService();
        autoService.setShopName(shopName);
        autoService.setAddress(adress);
        return "redirect:/details";
    }

    @PostMapping("/autoservice/employees")
    @Secured("ROLE_SERVICE_ADMIN")
    public ResponseEntity<?> addEmployee(@RequestParam String username, @RequestParam String password,
                                         @RequestParam String firstName, @RequestParam String lastName) {
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Korisničko ime je zauzeto");
        }
        if (username.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            return ResponseEntity.badRequest().body("Nisu popunjena sva polja");
        }

        ServiceEmployee employee = new ServiceEmployee();
        employee.setUsername(username);
        employee.setPasswordHash(passwordEncoder.encode(password));
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAutoService(getUserAutoService());
        serviceEmployeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PostMapping("/autoservice/employees/delete")
    @Secured("ROLE_SERVICE_ADMIN")
    public String removeEmployee(@RequestParam String username) {
        Optional<ServiceEmployee> employeeOptional = serviceEmployeeRepository.findByUsername(username);
        if (employeeOptional.isEmpty()) throw new ResourceNotFoundException();

        ServiceEmployee employee = employeeOptional.get();
        serviceEmployeeRepository.delete(employee);
        return "redirect:/autoservice/employees";
    }

    @GetMapping("/autoservice/closed")
    public String closedRepairOrders(Model model) {
        return null;
    }

    @DeleteMapping("/autoservice/employees/{:id}")
    public HttpStatus deleteEmployee(@RequestParam Long id) {
        return null;
    }

    @PostMapping("/autoservice/repairOrders/{id}/status")
    @Transactional
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        RepairOrder repairOrder = repairOrderRepository.findById(id).get();
        checkRepairOrderAccess(repairOrder);

        switch (status) {
            case "ACCEPT":
                repairOrder.setServiceJobStatus(ServiceJobStatus.IN_PROGRESS);
                return ResponseEntity.ok("");
            case "REJECT":
                repairOrder.setServiceJobStatus(ServiceJobStatus.REJECTED);
                return ResponseEntity.ok("");
            default:
                return ResponseEntity.badRequest().body("");
        }
    }

    private void checkRepairOrderAccess(RepairOrder repairOrder) {
        if (!repairOrder.getAutoService().getAutoServiceId().equals(getUserAutoService().getAutoServiceId())) {
            throw new AccessDeniedException("Forbidden");
        }
    }

    private void checkAutoServiceAccess(AutoService autoService) {
        if (!getUserAutoService().getAutoServiceId().equals(autoService.getAutoServiceId())) {
            throw new AccessDeniedException("Forbidden");
        }
    }

    private AutoService getUserAutoService() {
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        ServiceEmployee employee = (ServiceEmployee) appUser;
        return employee.getAutoService();
    }
}
