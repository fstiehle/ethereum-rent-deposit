package com.example.BackendApi;

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

    public Property(String firstName, String lastName, Date birthDate, String email, String street, int plz,
                    int houseNumber, String city, String land, String country, String ownerPublicKey,
                    String tenantPublicKey, int depositAmount, Date startDate, Date endDate) {
        this.firstName = firstName;

        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.street = street;
        this.plz = plz;
        this.houseNumber = houseNumber;
        this.city = city;
        this.land = land;
        this.country = country;
        this.hashValue= this.hashCode();
        this.ownerPublicKey = ownerPublicKey;
        this.tenantPublicKey = tenantPublicKey;
        this.depositAmount = depositAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return plz == property.plz &&
                houseNumber == property.houseNumber &&
                Objects.equals(street, property.street) &&
                Objects.equals(city, property.city) &&
                Objects.equals(land, property.land) &&
                Objects.equals(country, property.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, plz, houseNumber, city, land, country);
    }

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

}
