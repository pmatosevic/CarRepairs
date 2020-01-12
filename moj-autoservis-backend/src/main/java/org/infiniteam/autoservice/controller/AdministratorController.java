package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.model.AppUser;
import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.repository.UserRepository;
import org.infiniteam.autoservice.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdministratorController {

    @Autowired
    private UserRepository userRepository;

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminHome(Model model) {
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users")
    @Secured("ROLE_ADMIN")
    public String appUsers(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/admin/users")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> addEmployee(@RequestParam String username, @RequestParam String password,
                                         @RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

}
