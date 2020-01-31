package com.tub.ECproject;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Controller//=================
@RequestMapping("/api")
public class PropertyController {

    @RequestMapping("/hello")
    public String start(){
        return  "visit";
    }

    /*
    //following just for Restful api test

    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public String addProperty(@RequestBody Property pro)
    {
        //String[] list=new String[]{"firstName","lastName","birthDate","email","street","plz","houseNumber",
       // "city","land","country","ownerPublicKey","tanentPublicKey","depositAmount","startDate","endDate"};
        System.out.println("receive JSON already ");
        if(!"".equals(pro.getFirstName())&&!"".equals(pro.getLastName())&&!"".equals(pro.getBirthDate())&&!"".equals(pro.getEmail())
                &&!"".equals(pro.getStreet())&&!"0".equals(String.valueOf(pro.getPlz()))&&!"0".equals(String.valueOf(pro.getHouseNumber()))&&!"".equals(pro.getCity())
                &&!"".equals(pro.getLand())&&!"".equals(pro.getCountry()) &&!"".equals(pro.getOwnerPublicKey())&&!"".equals(pro.getTenantPublicKey())
                &&!"0".equals(String.valueOf(pro.getDepositAmount()))&&!"".equals(pro.getStartDate())&&!"".equals(pro.getEndDate())
        ){

            System.out.println(pro);

            return "200";
        } else{
            return "fail";
        }
    }
   */





}
