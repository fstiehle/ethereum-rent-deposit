package com.example.ECProject;

import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository("PropertyDao")
public class PropertyDao {

    private static Connection connection;

    public PropertyDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/ectest","admin","admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int InsertProperty(Property property) {
        int result=0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("INSERT INTO properties (firstname,lastname,birthdate,email,street,plz,housenum,city,land,country,hashvalue) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1,property.getFirstName());
            statement.setString(2,property.getLastName());
            statement.setDate(3, property.getBirthDate());
            statement.setString(4,property.getEmail());
            statement.setString(5,property.getStreet());
            statement.setInt(6,property.getPlz());
            statement.setInt(7,property.getHouseNumber());
            statement.setString(8,property.getCity());
            statement.setString(9,property.getLastName());
            statement.setString(10,property.getCountry());
            statement.setInt(11,property.getHashValue());
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
