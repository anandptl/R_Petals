package as.r_petals.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MianController {

    @GetMapping
    public String IndexPage(){
        return "Welcome to R_Petals";
    }


}
