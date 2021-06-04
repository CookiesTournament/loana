package org.cookiesturnier.loana.webservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiHandler {

    @GetMapping("/")
    public String root() {
        return "Hello World!";
    }

}
