package org.infiniteam.autoservice.controller;

import org.infiniteam.autoservice.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private UserDetailsServiceImpl manager;

    @GetMapping("/")
    public String index(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> appLogin(String username, String password, HttpServletRequest request) {
        try {
            UserDetails userDetails = manager.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
    }

}
