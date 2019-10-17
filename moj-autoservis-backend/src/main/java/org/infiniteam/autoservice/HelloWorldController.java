package org.infiniteam.autoservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    /**
     * Returns hello world
     * @return
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

}
