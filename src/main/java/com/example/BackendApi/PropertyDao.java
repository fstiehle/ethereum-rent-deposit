package com.example.BackendApi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository("PropertyDao")
public class PropertyDao {

    private static Connection connection;
    private static Oracle oracle;

    public PropertyDao() {

        Dotenv dotenv = Dotenv.load();
        oracle = new Oracle();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection= DriverManager.getConnection("jdbc:mysql://propertiesdb.cwpjk6ezwd65.us-east-1.rds.amazonaws.com:3306/ECProject", dotenv.get("DB_USER"),dotenv.get("DB_PASS"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String [] InsertProperty(Property property) {

        String [] res= new String[2];
        res[0]="0";
        res[1]="0";
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
            statement.setString(9,property.getLand());
            statement.setString(10,property.getCountry());
            statement.setInt(11,property.getHashValue());
            statement.execute();
            res[0]= String.valueOf(property.hashCode());
            res[1]=oracle.createRentContract(property);

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
        return res;
    }




}
