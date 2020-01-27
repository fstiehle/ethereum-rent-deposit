package com.example.ECProject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/addproperty")
public class PropertyController {

    private final PropertyService service;


    @Autowired
    public PropertyController(PropertyService service) {
        this.service = service;
    }

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
    public int addProperty(@RequestBody Property pro)
    {
        if(service.newProperty(pro))
        {
            return pro.getHashValue();
        }
        else
        return 65;
    }
}
