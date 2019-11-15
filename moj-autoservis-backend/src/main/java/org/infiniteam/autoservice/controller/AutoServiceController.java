package org.infiniteam.autoservice.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutoServiceController {

    @Secured("ROLE_SERVICE_EMPLOYEE")
    @GetMapping("/autoservice")
    public String autoserviceHome(Model model) {
        return "autoservice/home";
    }

    // Not implemented:
    public String manageEmployees(Model model) {
        return null;
    }

    public String editAutoService(Model model) {
        return null;
    }

    public String availableRepairOrdersList(Model model) {
        return null;
    }

    public String acceptRepairOrder(Model model) {
        return null;
    }

    public String editRepairOrder(Model model) {
        return null;
    }

    public String addItemToOrder(Model model) {
        return null;
    }

    public String closeRepairOrder(Model model) {
        return null;
    }

    public String manageServiceLabors(Model model) {
        return null;
    }

    public String manageParts(Model model) {
        return null;
    }

    public String viewStatistics(Model model) {
        return null;
    }

}
