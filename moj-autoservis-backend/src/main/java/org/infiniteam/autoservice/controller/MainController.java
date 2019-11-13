package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private UserRepository userRepository;
    private AutoServiceRepository autoServiceRepository;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            CurrentUser user = (CurrentUser) token.getPrincipal();
            return "redirect:" + redirects.get(user.getAppUser().getClass());
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

    @GetMapping("/register/user")
    public String registrationFormUser(Model model){
        return "user/registration";
    }

    @PostMapping("/register/user")
    public String userRegistration(HttpServletRequest request, Model model) {
        AppUser newUser = new VehicleOwner();
        newUser.setUsername(request.getParameter("username"));
        newUser.setEmail(request.getParameter("email"));
        newUser.setPasswordHash(request.getParameter("password"));
        userRepository.save(newUser);
        return "redirect:/login";
    }

    @GetMapping("/register/autoservice")
    public String registrationFormAutoService(Model model){
        return "autoservice/registration";
    }

    @PostMapping("/register/autoservice")
    public String autoServiceRegistration(HttpServletRequest request, Model model) {

        AutoService newAutoService = new AutoService();
        newAutoService.setShopName(request.getParameter("shopname"));
        newAutoService.setAddress(request.getParameter("address"));
        newAutoService.setOib(request.getParameter("oib"));
        autoServiceRepository.save(newAutoService);
        return "redirect:/register/user";
    }
}
