package com.example.BackendApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final PropertyDao dao;

    @Autowired
    public PropertyService(@Qualifier("PropertyDao") PropertyDao dao) {
        this.dao = dao;
    }

    public String [] newProperty(Property property)
    {
        String [] result= new String[2];
        for (String s : result = dao.InsertProperty(property)) {

        }
        ;
        return result;
    }
}
