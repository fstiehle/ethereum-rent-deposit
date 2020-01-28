package com.example.ECProject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller//=================
@RequestMapping("api/addproperty")
public class PropertyController {

    private final PropertyService service;


    @Autowired
    public PropertyController(PropertyService service) {
        this.service = service;
    }
/*
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String TestService() {
        return "Service is up and running";
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
*/
    @RequestMapping(value="/save")
    @ResponseBody
    public int addProperty(Property pro)//int <-->String
    {

        if(service.newProperty(pro))
        {
            return pro.gethashValue();
            //return "success";

        }
        else
             return 65;
            // return "/fail";
    }
    //=====================
    
    @RequestMapping("/hello")
    public String start(){
        return  "/visit";
    }
    
    //========================


}
