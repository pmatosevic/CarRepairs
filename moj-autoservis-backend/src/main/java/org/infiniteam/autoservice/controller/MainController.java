package org.infiniteam.autoservice.controller;

import org.apache.coyote.Response;
import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.infiniteam.autoservice.service.AutoServiceService;
import org.infiniteam.autoservice.service.UserService;
import org.infiniteam.autoservice.service.VehicleOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Map;

@Controller
public class MainController {

    private static final Map<Class<?>, String> REDIRECTS = Map.of(
            VehicleOwner.class, "/user",
            ServiceEmployee.class, "/autoservice",
            Administrator.class, "/admin"
    );

    @Autowired
    private UserService userService;

    @Autowired
    private AutoServiceService autoServiceService;

    @Autowired
    private VehicleOwnerService vehicleOwnerService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            CurrentUser user = (CurrentUser) token.getPrincipal();
            return "redirect:" + REDIRECTS.get(user.getAppUser().getClass());
        }
        return "home/home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        return "home/login";
    }

    @GetMapping("/register/user")
    public String registrationFormUser(Model model){
        return "home/userRegistration";
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> userRegistration(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Korisničko ime već postoji.");
        }

        VehicleOwner newUser = new VehicleOwner();
        newUser.setUsername(username);
        newUser.setEmail(request.getParameter("email"));
        newUser.setOib(request.getParameter("oib"));
        newUser.setFirstName(request.getParameter("name"));
        newUser.setLastName(request.getParameter("surname"));
        newUser.setPasswordHash(password);

        try {
            vehicleOwnerService.create(newUser);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/register/autoservice")
    public String registrationFormAutoService(Model model){
        return "home/autoserviceRegistration";
    }

    @PostMapping("/register/autoservice")
    public ResponseEntity<?> autoServiceRegistration(HttpServletRequest request, Model model) {
        if (autoServiceService.existsByOib(request.getParameter("oib"))) {
            return ResponseEntity.badRequest().body("Autoservis s tim OIB-om već postoji");
        }
        if (userService.existsByUsername(request.getParameter("username"))) {
            return ResponseEntity.badRequest().body("Korisničko ime već postoji.");
        }

        AutoService newAutoService = new AutoService();
        newAutoService.setShopName(request.getParameter("shopname"));
        newAutoService.setAddress(request.getParameter("address"));
        newAutoService.setOib(request.getParameter("oib"));

        ServiceEmployee owner = new ServiceEmployee();
        owner.setEmail(request.getParameter("email"));
        owner.setUsername(request.getParameter("username"));
        owner.setFirstName(request.getParameter("name"));
        owner.setLastName(request.getParameter("surname"));
        owner.setPasswordHash(request.getParameter("password"));

        try {
            autoServiceService.createServiceWithOwner(newAutoService, owner);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("");
        }
    }

    @GetMapping("/settings")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SERVICE_EMPLOYEE', 'ROLE_ADMIN')")
    public String settings(Model model){
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        model.addAttribute("user", appUser);
        return "changeSettings";
    }

    @PostMapping("/settings")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SERVICE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<?> changeUserSettings(@RequestParam String firstName, @RequestParam String lastName,
                                     @RequestParam String email) {
        AppUser user = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userService.modify(user);
        return ResponseEntity.ok("");
    }

    @GetMapping("/changePassword")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SERVICE_EMPLOYEE', 'ROLE_ADMIN')")
    public String changePasswordPage(Model model) {
        return "changePassword";
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SERVICE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        AppUser user = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        try {
            userService.changePassword(user, oldPassword, newPassword);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
