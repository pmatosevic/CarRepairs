package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.*;
import org.infiniteam.autoservice.repository.AutoServiceRepository;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    @GetMapping("/register/user")
    public String registrationFormUser(Model model){
        return "user/registration";
    }

    @PostMapping("/register/user")
    @Transactional
    public ResponseEntity<String> userRegistration(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userRepository.getByUsername(username).isPresent()) {
            return new ResponseEntity<>("Username already registered.", HttpStatus.BAD_REQUEST);
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
