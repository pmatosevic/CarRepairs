package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.RepairOrderRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Secured("ROLE_SERVICE_EMPLOYEE")
public class AutoServiceController {

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Secured("ROLE_SERVICE_EMPLOYEE")
    @GetMapping("/autoservice")
    public String autoserviceHome(Model model) {
        return "autoservice/home";
    }

    @GetMapping("/autoservice/waiting")
    public String waitingRepairOrders(Model model) {
        AutoService autoService = getUserAutoService();
        List<RepairOrder> repairOrders = repairOrderRepository.findAllByServiceJobStatusAndAutoService(
                ServiceJobStatus.ACCEPTANCE_WAITING, autoService);
        model.addAttribute("repairOrders", repairOrders);
        return "autoservice/ro_list";
    }

    @GetMapping("/autoservice/repairOrder/{id}")
    public String showRepairOrder(Model model, @RequestParam Long id) {
        return null;
    }

    @GetMapping("/autoservice/closed")
    public String closedRepairOrders(Model model) {
        return null;
    }

    @GetMapping("/autoservice/employees")
    public String autoserviceEmployees(Model model) {
        return null;
    }

    @PostMapping("/autoservice/employees")
    public String addEmployee(@RequestBody Object dto) {
        return null;
    }

    @DeleteMapping("/autoservice/employees/{:id}")
    public HttpStatus deleteEmployee(@RequestParam Long id) {
        return null;
    }

    @PutMapping("/autoservice/ro/{id}/status")
    public HttpStatus updateStatus(@RequestParam Long id) {
        return null;
    }

    private AutoService getUserAutoService() {
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        ServiceEmployee employee = (ServiceEmployee) appUser;
        return employee.getAutoService();
    }
}
