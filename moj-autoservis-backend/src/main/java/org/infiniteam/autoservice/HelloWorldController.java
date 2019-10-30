package org.infiniteam.autoservice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "world");

        return "hello";
    }

}
