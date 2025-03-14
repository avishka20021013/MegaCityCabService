package com.example.megaCabService.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.example.megaCabService.models.ClientModel;

@Repository
public class ClientRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public boolean nicExists(String nic) {
        String sql = "SELECT COUNT(*) FROM clients WHERE nic = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nic);
        return count != null && count > 0;
    }
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM clients WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM clients WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }


    public ClientModel getClient(int clientID) {
        String sql = "SELECT * FROM clients WHERE clientID=?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, clientID);

        if (rows.next()) {
        	ClientModel client = new ClientModel();
            client.setClientID(rows.getInt("clientID"));
            client.setFirstName(rows.getString("firstName"));
            client.setLastName(rows.getString("lastName"));
            client.setAddress(rows.getString("address"));
            client.setNic(rows.getString("nic"));
            client.setEmail(rows.getString("email"));
            client.setUsername(rows.getString("username"));
            client.setPassword(rows.getString("password"));
            return client;
        }
        return null;
    }
    
    public List<ClientModel> getAllClients() {
        String sql = "SELECT * FROM clients";
        List<ClientModel> clients = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while (rows.next()) {
            ClientModel client = new ClientModel();
            client.setClientID(rows.getInt("clientID"));
            client.setFirstName(rows.getString("firstName"));
            client.setLastName(rows.getString("lastName"));
            client.setAddress(rows.getString("address"));
            client.setNic(rows.getString("nic"));
            client.setEmail(rows.getString("email"));
            client.setUsername(rows.getString("username"));
            client.setPassword(rows.getString("password"));
            clients.add(client);
        }
        return clients;
    }
    
    public boolean deleteClient(int clientID) {
        String sql = "DELETE FROM clients WHERE clientID = ?";
        int count = jdbcTemplate.update(sql, clientID);
        return count > 0;
    }
    
    public boolean updateClient(ClientModel client) {
        String sql = "UPDATE clients SET firstName=?, lastName=?, address=?, nic=?, email=?, username=?, password=? WHERE clientID=?";
        int count = jdbcTemplate.update(sql, 
            client.getFirstName(), client.getLastName(), client.getAddress(), client.getNic(), 
            client.getEmail(), client.getUsername(), client.getPassword(), client.getClientID());

        return count > 0;
    }
    
    public ClientModel getClientByUsername(String username) {
        String sql = "SELECT * FROM clients WHERE username = ?";
        
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            ClientModel client = new ClientModel();
            client.setClientID(rs.getInt("clientID"));
            client.setFirstName(rs.getString("firstName"));
            client.setLastName(rs.getString("lastName"));
            client.setAddress(rs.getString("address"));
            client.setNic(rs.getString("nic"));
            client.setEmail(rs.getString("email"));
            client.setUsername(rs.getString("username"));
            client.setPassword(rs.getString("password"));
            return client;
        }, username);
    }
    
    public int getClientCount() {
        String sql = "SELECT COUNT(*) FROM clients";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
