package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.Vehicle;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Secured("ROLE_USER")
public class VehicleOwnerController {

    @GetMapping("/user")
    public String carOwnerHome(Model model) {
        return "user/home";
    }

    @PostMapping("/user/vehicles")
    public Vehicle addVehicle(Vehicle vehicleDto) {
        return null;
    }

}
