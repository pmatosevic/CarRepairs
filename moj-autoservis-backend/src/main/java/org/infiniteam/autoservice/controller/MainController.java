package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.CurrentUser;
import org.infiniteam.autoservice.model.Administrator;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            CurrentUser user = (CurrentUser) token.getPrincipal();
            return "redirect:" + redirects.get(user.getUser().getClass());
        }
        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    @Secured("ROLE_USER")
    @GetMapping("/user")
    public String carOwnerHome(Model model) {
        return "user/home";
    }

    @Secured("ROLE_SERVICE_EMPLOYEE")
    @GetMapping("/autoservice")
    public String autoserviceHome(Model model) {
        return "autoservice/home";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminHome(Model model) {
        return "admin/home";
    }

}
