package com.example.BackendApi;

import org.junit.jupiter.api.Test;

import java.sql.Date;

public class OracleTest {

    @Test
    void oracleTest() {
        Oracle oracle = new Oracle();
        oracle.createRentContract(new Property(
                "1",
                "2",
                new Date(12),
                "s",
                "s",
                2,
                3,
                "s",
                "3",
                "2",
                "1",
                "2",
                2,
                new Date(12),
                new Date(13)));
    }
}
