package com.example.megaCabService.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.megaCabService.models.CabModel;

@Repository
public class CabsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public CabModel getCab(int cabID) {
        String sql = "SELECT * FROM cabs WHERE cabID=?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, cabID);

        if (rows.next()) {
            CabModel cab = new CabModel();
            cab.setCabID(rows.getInt("cabID"));
            cab.setCabNumber(rows.getString("cabNumber"));
            cab.setModel(rows.getString("model"));
            cab.setCategory(rows.getString("category"));
            cab.setSeats(rows.getInt("seats"));
            cab.setDriverName(rows.getString("ownerName"));
            cab.setDriverContact(rows.getString("ownerContact"));
            cab.setImageUrl(rows.getString("imageUrl"));

            return cab;
        }
        return null;
    }
    
    public boolean cabNumberExists(String cabNumber) {
        String sql = "SELECT COUNT(*) FROM cabs WHERE cabNumber = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cabNumber);
        return count != null && count > 0;
    }

    public CabModel addCab(CabModel cab) {
        if (cabNumberExists(cab.getCabNumber())) {
            return null;
        }

        String sql = "INSERT INTO cabs (cabNumber, model, category, seats, ownerName, ownerContact, imageUrl) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int count = jdbcTemplate.update(sql, 
            cab.getCabNumber(), cab.getModel(), cab.getCategory(), 
            cab.getSeats(), cab.getDriverName(), cab.getDriverContact(), cab.getImageUrl());

        if (count > 0) {
            int id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            return getCab(id);
        }
        return null; 
    }

    
    public boolean deleteCab(int cabID) {
        String sql = "DELETE FROM cabs WHERE cabID = ?";
        int count = jdbcTemplate.update(sql, cabID);
        return count > 0;
    }
    
    public boolean updateCab(CabModel cab) {
        String sql = "UPDATE cabs SET cabNumber=?, model=?, category=?, seats=?, ownerName=?, ownerContact=?, imageUrl=? WHERE cabID=?";
        int count = jdbcTemplate.update(sql, 
            cab.getCabNumber(), cab.getModel(), cab.getCategory(), cab.getSeats(), 
            cab.getDriverName(), cab.getDriverContact(), cab.getImageUrl(), cab.getCabID());

        return count > 0;
    }


    
    public List<CabModel> getAllCabs() {
        String sql = "SELECT * FROM cabs";
        List<CabModel> cabs = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while (rows.next()) {
            CabModel cab = new CabModel();
            cab.setCabID(rows.getInt("cabID"));
            cab.setCabNumber(rows.getString("cabNumber"));
            cab.setModel(rows.getString("model"));
            cab.setCategory(rows.getString("category"));
            cab.setSeats(rows.getInt("seats"));
            cab.setDriverName(rows.getString("ownerName"));
            cab.setDriverContact(rows.getString("ownerContact"));
            cab.setImageUrl(rows.getString("imageUrl"));
            cabs.add(cab);
        }
        return cabs;
    }
    
    public List<String> getAllCabCategories() {
        String sql = "SELECT category FROM categories";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    public List<CabModel> getCabsByCategory(String category) {
        String sql = "SELECT * FROM cabs WHERE category = ?";
        List<CabModel> cabs = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, category);

        while (rows.next()) {
            CabModel cab = new CabModel();
            cab.setCabID(rows.getInt("cabID"));
            cab.setCabNumber(rows.getString("cabNumber"));
            cab.setModel(rows.getString("model"));
            cab.setCategory(rows.getString("category"));
            cab.setSeats(rows.getInt("seats"));
            cab.setDriverName(rows.getString("ownerName"));
            cab.setDriverContact(rows.getString("ownerContact"));
            cab.setImageUrl(rows.getString("imageUrl"));

            cabs.add(cab);
        }
        return cabs;
    }
    public int getCabCount() {
        String sql = "SELECT COUNT(*) FROM cabs";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
