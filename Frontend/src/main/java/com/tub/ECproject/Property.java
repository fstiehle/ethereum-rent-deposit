package com.tub.ECproject;


import java.sql.Date;
import java.util.Objects;

public class Property {

    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String street;
    private int plz;
    private int houseNumber;
    private String city;
    private String land;
    private String country;
    private int hashValue;
    private String ownerPublicKey;
    private String tenantPublicKey;
    private int depositAmount;
    private Date startDate;
    private Date endDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getHashValue() {
        return hashValue;
    }

    public void setHashValue(int hashValue) {
        this.hashValue = hashValue;
    }

    public String getOwnerPublicKey() {
        return ownerPublicKey;
    }

    public void setOwnerPublicKey(String ownerPublicKey) {
        this.ownerPublicKey = ownerPublicKey;
    }

    public String getTenantPublicKey() {
        return tenantPublicKey;
    }

    public void setTenantPublicKey(String tenantPublicKey) {
        this.tenantPublicKey = tenantPublicKey;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Property{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", plz=" + plz +
                ", houseNumber=" + houseNumber +
                ", city='" + city + '\'' +
                ", land='" + land + '\'' +
                ", country='" + country + '\'' +
                ", hashValue=" + hashValue +
                ", ownerPublicKey='" + ownerPublicKey + '\'' +
                ", tanentPublicKey='" + tenantPublicKey + '\'' +
                ", depositAmount=" + depositAmount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}