package org.infiniteam.autoservice.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminHome(Model model) {
        return "admin/home";
    }

}
