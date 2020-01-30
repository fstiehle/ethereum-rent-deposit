package com.example.RESTapi;

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

    public boolean newProperty(Property property)
    {
        if(dao.InsertProperty(property)==1)
        {
            return true;
        }
        else
            return false;
    }
}
