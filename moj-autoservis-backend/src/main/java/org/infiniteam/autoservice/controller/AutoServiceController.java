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

}
