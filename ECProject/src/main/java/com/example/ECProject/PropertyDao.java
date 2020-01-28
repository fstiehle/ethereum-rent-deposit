package com.example.ECProject;

import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository("PropertyDao")
public class PropertyDao {

    private static Connection connection;

    public PropertyDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3307/ec?characterEncoding=utf-8&useSSL=false&serverTimezone=Hongkong","root","admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int InsertProperty(Property property) {
        int result=0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("INSERT INTO properties (firstName,lastName,birthDate,email,street,plz,housenum,city,land,country,hashValue) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, property.getfirstName());
            statement.setString(2, property.getlastName());
            statement.setDate(3, (Date) property.getbirthDate());
            statement.setString(4, property.getEmail());
            statement.setString(5, property.getStreet());
            statement.setInt(6, property.getPlz());
            statement.setInt(7, property.gethouseNumber());
            statement.setString(8, property.getCity());
            statement.setString(9, property.getlastName());
            statement.setString(10, property.getCountry());
            statement.setInt(11, property.gethashValue());
            statement.execute();
            result=1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
