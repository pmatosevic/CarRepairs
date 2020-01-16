package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.*;
import org.infiniteam.autoservice.security.CurrentUser;
import org.infiniteam.autoservice.service.*;
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
import java.util.List;
import java.util.Optional;

@Controller
@Secured("ROLE_SERVICE_EMPLOYEE")
public class AutoServiceController {

    @Autowired
    private RepairOrderService repairOrderService;

    @Autowired
    private VehiclePartService vehiclePartService;

    @Autowired
    private ServiceLaborService serviceLaborService;

    @Autowired
    private ServiceEmployeeService serviceEmployeeService;

    @Autowired
    private AutoServiceService autoServiceService;


    @GetMapping("/autoservice")
    public String autoserviceHome(Model model) {
        return "redirect:/autoservice/repairOrders/waiting";
    }

    @GetMapping("/autoservice/repairOrders/waiting")
    public String waitingRepairOrders(Model model) {
        AutoService autoService = getUserAutoService();
        List<RepairOrder> repairOrders = repairOrderService.findAllByServiceJobStatusAndAutoService(
                ServiceJobStatus.ACCEPTANCE_WAITING, autoService);

        model.addAttribute("repairOrders", repairOrders);
        return "autoservice/newOrders";
    }

    @GetMapping("/autoservice/repairOrders/opened")
    public String currentRepairOrders(Model model) {
        AutoService autoService = getUserAutoService();
        List<RepairOrder> repairOrders = repairOrderService.findAllByServiceJobStatusAndAutoService(
                ServiceJobStatus.IN_PROGRESS, autoService);

        model.addAttribute("repairOrders", repairOrders);
        return "autoservice/currentOrders";
    }

    @GetMapping("/autoservice/repairOrders/{id}")
    public String showRepairOrder(Model model, @PathVariable Long id) {
        RepairOrder repairOrder = repairOrderService.fetch(id);
        model.addAttribute("order", repairOrder);

        if (repairOrder instanceof RegularRepairOrder) {
            return "autoservice/editRegularRo";
        } else if (repairOrder instanceof RepairingRepairOrder) {
            model.addAttribute("parts", vehiclePartService.findAllByAutoService(getUserAutoService()));
            model.addAttribute("labors", serviceLaborService.findAllByAutoService(getUserAutoService()));
            return "autoservice/editRepairingRo";
        } else {
            throw new RuntimeException("Not implemented.");
        }
    }

    @PostMapping("/autoservice/repairOrders/{id}/saveAndClose")
    public String saveAndCloseRegularRo(@PathVariable Long id,
                                        @RequestParam int kilometers, @RequestParam boolean recommended) {
        RegularRepairOrder repairOrder = repairOrderService.fetchRegularRepairOrder(id);
        checkRepairOrderAccess(repairOrder);

        repairOrderService.updateRegularRepairOrder(repairOrder, kilometers, recommended);
        repairOrderService.setFinished(repairOrder);

        return "redirect:/autoservice/repairOrders/opened";
    }

    @PostMapping("/autoservice/repairOrders/{id}/close")
    public String closeRepairingRo(@PathVariable Long id) {
        RepairingRepairOrder repairOrder = repairOrderService.fetchRepairingRepairOrder(id);
        checkRepairOrderAccess(repairOrder);

        repairOrderService.setFinished(repairOrder);
        return "redirect:/autoservice/repairOrders/opened";
    }

    @PostMapping("/autoservice/repairOrders/{id}/addPart")
    public ResponseEntity<?> addVehiclePartToRepairOrder(@PathVariable Long id, @RequestParam Long partId) {
        RepairingRepairOrder repairOrder = repairOrderService.fetchRepairingRepairOrder(id);
        VehiclePart part = vehiclePartService.fetch(partId);
        checkRepairOrderAccess(repairOrder);
        checkAutoServiceAccess(part.getAutoService());

        repairOrderService.addItemToOrder(repairOrder, part);

        return ResponseEntity.ok("");
    }

    @PostMapping("/autoservice/repairOrders/{id}/addLabor")
    public ResponseEntity<?> addServiceLaborToRepairOrder(@PathVariable Long id, @RequestParam Long laborId) {
        RepairingRepairOrder repairOrder = repairOrderService.fetchRepairingRepairOrder(id);
        ServiceLabor labor = serviceLaborService.fetch(laborId);
        checkRepairOrderAccess(repairOrder);
        checkAutoServiceAccess(labor.getAutoService());

        repairOrderService.addItemToOrder(repairOrder, labor);

        return ResponseEntity.ok("");
    }

    @PostMapping("/autoservice/repairOrders/{id}/removeItem")
    @Transactional
    public String removeItem(@PathVariable Long id, @RequestParam Long itemId) {
        RepairingRepairOrder ro = repairOrderService.fetchRepairingRepairOrder(id);
        checkRepairOrderAccess(ro);
        repairOrderService.removeItemFromOrder(ro, itemId);

        return "redirect:/autoservice/repairOrders/" + id;
    }









    @GetMapping("/autoservice/priceList")
    @Secured("ROLE_SERVICE_ADMIN")
    public String showPriceList(Model model) {
        List<VehiclePart> parts = vehiclePartService.findAllByAutoService(getUserAutoService());
        List<ServiceLabor> labors = serviceLaborService.findAllByAutoService(getUserAutoService());

        model.addAttribute("parts", parts);
        model.addAttribute("labors", labors);
        return "autoservice/priceList";
    }

    @GetMapping("/autoservice/priceList/parts/{id}")
    @Secured("ROLE_SERVICE_ADMIN")
    public String vehiclePartModal(@PathVariable Long id, Model model) {
        VehiclePart part = id == 0 ? new VehiclePart() : vehiclePartService.fetch(id);
        model.addAttribute("part", part);
        return "/autoservice/vehiclePartModal :: content";
    }

    @PostMapping("/autoservice/priceList/parts/{id}/delete")
    @Secured("ROLE_SERVICE_ADMIN")
    public String vehiclePartRemove(@PathVariable Long id) {
        VehiclePart part = vehiclePartService.fetch(id);
        checkAutoServiceAccess(part.getAutoService());

        vehiclePartService.delete(part);

        return "redirect:/autoservice/priceList";
    }

    @PostMapping("/autoservice/priceList/parts")
    @Secured("ROLE_SERVICE_ADMIN")
    public String updateVehiclePart(@RequestParam String partName, @RequestParam int estimatedDuration,
                                    @RequestParam double price, @RequestParam Long id) {
        VehiclePart part;
        if (id != null && id != 0) {
            part = vehiclePartService.fetch(id);
            checkAutoServiceAccess(part.getAutoService());
        } else {
            part = new VehiclePart();
            part.setAutoService(getUserAutoService());
        }
        part.setEstimatedDurationInKm(estimatedDuration);
        part.setPrice(price);
        part.setPartName(partName);
        vehiclePartService.addOrModify(part);
        return "redirect:/autoservice/priceList";
    }

    @GetMapping("/autoservice/priceList/labors/{id}")
    @Secured("ROLE_SERVICE_ADMIN")
    public String serviceLaborModal(@PathVariable Long id, Model model) {
        ServiceLabor labor = id == 0 ? new ServiceLabor() : serviceLaborService.fetch(id);
        model.addAttribute("labor", labor);
        return "/autoservice/serviceLaborModal :: content";
    }

    @PostMapping("/autoservice/priceList/labors")
    @Secured("ROLE_SERVICE_ADMIN")
    public String updateServiceLabor(@RequestParam String serviceName,
                                     @RequestParam double price, @RequestParam Long id) {
        ServiceLabor serviceLabor;
        if (id != null && id != 0) {
            serviceLabor = serviceLaborService.fetch(id);
            checkAutoServiceAccess(serviceLabor.getAutoService());
        } else {
            serviceLabor = new ServiceLabor();
            serviceLabor.setAutoService(getUserAutoService());
        }
        serviceLabor.setServiceName(serviceName);
        serviceLabor.setPrice(price);
        serviceLaborService.addOrModify(serviceLabor);
        return "redirect:/autoservice/priceList";
    }

    @PostMapping("/autoservice/priceList/labors/{id}/delete")
    @Secured("ROLE_SERVICE_ADMIN")
    public String serviceLaborRemove(@PathVariable Long id) {
        ServiceLabor labor = serviceLaborService.fetch(id);
        checkAutoServiceAccess(labor.getAutoService());
        serviceLaborService.delete(labor);
        return "redirect:/autoservice/priceList";
    }








    @GetMapping("/autoservice/employees")
    @Secured("ROLE_SERVICE_ADMIN")
    public String autoserviceEmployees(Model model) {
        List<ServiceEmployee> employees = serviceEmployeeService.findAllByAutoService(getUserAutoService());
        model.addAttribute("employees", employees);
        return "autoservice/employees";
    }

    @GetMapping("/autoservice/details")
    @Secured("ROLE_SERVICE_ADMIN")
    public String autoserviceDetails(Model model){
        ServiceEmployee serviceEmployee  = (ServiceEmployee) ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        AutoService autoSerivce = serviceEmployee.getAutoService();
        model.addAttribute("service", autoSerivce);
        return "autoservice/details";
    }

    @PostMapping("/autoservice/details")
    @Secured("ROLE_SERVICE_ADMIN")
    public String changeServiceSettings(@RequestParam String shopName, @RequestParam String address,
                                        @RequestParam double regularPrice,
                                        @RequestParam double latitude, @RequestParam double longitude) {
        ServiceEmployee serviceEmployee  = (ServiceEmployee) ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        AutoService autoService = serviceEmployee.getAutoService();
        autoService.setShopName(shopName);
        autoService.setAddress(address);
        autoService.setRegularServicePrice(regularPrice);
        autoService.setLatitude(latitude);
        autoService.setLongitude(longitude);
        autoServiceService.update(autoService);
        return "redirect:/autoservice/details";
    }

    @PostMapping("/autoservice/employees")
    @Secured("ROLE_SERVICE_ADMIN")
    public ResponseEntity<?> addEmployee(@RequestParam String username, @RequestParam String password,
                                         @RequestParam String firstName, @RequestParam String lastName) {
        if (username.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            return ResponseEntity.badRequest().body("Nisu popunjena sva polja");
        }

        ServiceEmployee employee = new ServiceEmployee();
        employee.setUsername(username);
        employee.setPasswordHash(password);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAutoService(getUserAutoService());

        try {
            serviceEmployeeService.add(employee);
        } catch (UsernameExistsException e) {
            return ResponseEntity.badRequest().body("Korisničko ime je zauzeto");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PostMapping("/autoservice/employees/delete")
    @Secured("ROLE_SERVICE_ADMIN")
    public String removeEmployee(@RequestParam String username) {
        Optional<ServiceEmployee> employeeOptional = serviceEmployeeService.findByUsername(username);
        if (employeeOptional.isEmpty()) throw new ResourceNotFoundException();

        ServiceEmployee employee = employeeOptional.get();
        serviceEmployeeService.delete(employee);
        return "redirect:/autoservice/employees";
    }






    @GetMapping("/autoservice/closed")
    public String closedRepairOrders(Model model) {
        //TODO: implement
        return null;
    }

    @PostMapping("/autoservice/repairOrders/{id}/status")
    @Transactional
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        RepairOrder repairOrder = repairOrderService.findById(id).get();
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
