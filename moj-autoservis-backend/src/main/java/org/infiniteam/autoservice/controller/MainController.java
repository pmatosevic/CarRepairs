package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private static final Map<Class<?>, String> redirects = Map.of(
            VehicleOwner.class, "/user",
            ServiceEmployee.class, "/autoservice",
            Administrator.class, "/admin"
    );

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AutoServiceRepository autoServiceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            CurrentUser user = (CurrentUser) token.getPrincipal();
            return "redirect:" + redirects.get(user.getAppUser().getClass());
        }
        return "home/home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "home/login";
    }

    @GetMapping("/register/user")
    public String registrationFormUser(Model model){
        return "home/userRegistration";
    }

    @PostMapping("/register/user")
    @Transactional
    public ResponseEntity<String> userRegistration(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userRepository.getByUsername(username).isPresent()) {
            return new ResponseEntity<>("Korisničko ime već postoji.", HttpStatus.BAD_REQUEST);
        }

        VehicleOwner newUser = new VehicleOwner();
        newUser.setUsername(username);
        newUser.setEmail(request.getParameter("email"));
        newUser.setOib(request.getParameter("oib"));
        newUser.setFirstName(request.getParameter("name"));
        newUser.setLastName(request.getParameter("surname"));
        newUser.setPasswordHash(passwordEncoder.encode(password));
        userRepository.saveAndFlush(newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/register/autoservice")
    public String registrationFormAutoService(Model model){
        return "home/autoserviceRegistration";
    }

    @PostMapping("/register/autoservice")
    @Transactional
    public ResponseEntity<String> autoServiceRegistration(HttpServletRequest request, Model model) {
        if (autoServiceRepository.getByOib(request.getParameter("oib")).isPresent()) {
            return new ResponseEntity<>("Autoservis s tim OIB-om već postoji", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.getByUsername(request.getParameter("username")).isPresent()) {
            return new ResponseEntity<>("Korisničko ime već postoji.", HttpStatus.BAD_REQUEST);
        }

        AutoService newAutoService = new AutoService();
        newAutoService.setShopName(request.getParameter("shopname"));
        newAutoService.setAddress(request.getParameter("address"));
        newAutoService.setOib(request.getParameter("oib"));
        autoServiceRepository.save(newAutoService);

        ServiceEmployee owner = new ServiceEmployee();
        owner.setEmployeeType(ServiceEmployeeType.SERVICE_ADMINISTRATOR);
        owner.setEmail(request.getParameter("email"));
        owner.setUsername(request.getParameter("username"));
        owner.setFirstName(request.getParameter("name"));
        owner.setLastName(request.getParameter("surname"));
        owner.setPasswordHash(passwordEncoder.encode(request.getParameter("password")));
        userRepository.save(owner);
        newAutoService.addEmployee(owner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/settings")
    public String settings(Model model){
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        model.addAttribute("user", appUser);
        return "changeSettings";
    }

    @PostMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String changeUserSettings(@RequestParam String firstName, @RequestParam String lastName,
                                     @RequestParam String email) {
        AppUser appUser = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAppUser();
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setEmail(email);
        return "redirect:/settings";
    }

    @GetMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(Model model) {
        return "changePassword";
    }
}
