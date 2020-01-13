package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.infiniteam.autoservice.service.AutoServiceService;
import org.infiniteam.autoservice.service.VehicleOwnerService;
import org.infiniteam.autoservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Secured("ROLE_ADMIN")
public class AdministratorController {

    @Autowired
    private VehicleOwnerService vehicleOwnerService;

    @Autowired
    private AutoServiceService autoServiceService;

    @GetMapping("/admin")
    public String adminHome() {
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users")
    public String appUsers(Model model) {
        List<VehicleOwner> users = vehicleOwnerService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/admin/users/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        VehicleOwner vehicleOwner = vehicleOwnerService.fetch(id);
        vehicleOwnerService.delete(vehicleOwner);
        return ResponseEntity.ok("");
    }

    @GetMapping("/admin/autoservices")
    public String autoservicesList(Model model) {
        List<AutoService> autoServices = autoServiceService.findAll();
        model.addAttribute("autoServices", autoServices);
        return "admin/autoServices";
    }

    @PostMapping("/admin/autoservices/{id}/delete")
    public ResponseEntity<?> deleteAutoService(@PathVariable long id) {
        AutoService autoService = autoServiceService.fetch(id);
        autoServiceService.softDelete(autoService);
        return ResponseEntity.ok("");
    }

}
