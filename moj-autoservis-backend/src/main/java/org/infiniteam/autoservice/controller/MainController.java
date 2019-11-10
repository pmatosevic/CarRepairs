package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.Administrator;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
public class MainController {

    private static final Map<Class<?>, String> redirects = Map.of(
            VehicleOwner.class, "/user",
            ServiceEmployee.class, "/autoservice",
            Administrator.class, "/admin"
    );

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:" + redirects.get(principal.getClass());
        }
        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

}
