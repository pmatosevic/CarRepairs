package org.infiniteam.autoservice.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VehicleOwnerController {

    @Secured("ROLE_USER")
    @GetMapping("/user")
    public String carOwnerHome(Model model) {
        return "user/home";
    }

    // Not implemented
    public String addVehicle(Model model) {
        return null;
    }

    public String removeVehicle(Model model) {
        return null;
    }

    public String vehicleStatistics(Model model) {
        return null;
    }

    public String autoServicesList(Model model) {
        return null;
    }

    public String openRepairOrder(Model model) {
        return null;
    }

    public String viewRepairOrder(Model model) {
        return null;
    }

}
