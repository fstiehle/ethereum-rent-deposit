package com.example.BackendApi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(value ="api/addproperty" , method = RequestMethod.POST)
@CrossOrigin(origins =  "*" ,allowCredentials = "true" , maxAge = 3600)
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
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public String[] addProperty(@RequestBody Property pro)
    {
        String [] result= new String[2];
        for (String s : result = service.newProperty(pro)) {

        }
        ;
        if(result[0]=="0")
        {
            result[0]="There is a problem with the DB";
        }
        else
            if(result[1]=="0")
            {
                result[1]="There is a problem with the Block chain";
            }
        return result;
    }
}