package com.example.megaCabService.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.megaCabService.models.ClientModel;

@Repository
public class LoginAndRegistrationRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    
    public String getPasswordByUsername(String username) {
        String sql = "SELECT password FROM clients WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }
    

    public ClientModel clientRegistration(ClientModel client) {

        String sql = "INSERT INTO clients (firstName, lastName, address, nic, email, username, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int count = jdbcTemplate.update(sql, 
            client.getFirstName(), client.getLastName(), client.getAddress(), client.getNic(), client.getEmail(), client.getUsername(), client.getPassword());

        if (count > 0) {
        	String clientIdSql = "SELECT LAST_INSERT_ID()";
            Integer clientId = jdbcTemplate.queryForObject(clientIdSql, Integer.class);
            client.setClientID(clientId);
            return client;
        }
        return null; 
    }
}
