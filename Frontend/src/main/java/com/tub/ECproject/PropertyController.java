package com.tub.ECproject;



import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api")
public class PropertyController {


    @RequestMapping("/hello")
    public String start(){
        return  "visit";
    }

}
