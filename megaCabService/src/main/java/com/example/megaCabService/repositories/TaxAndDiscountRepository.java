package com.example.megaCabService.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.megaCabService.models.TaxAndDiscountModel;

@Repository
public class TaxAndDiscountRepository {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public TaxAndDiscountModel getTaxAndDiscount(int id) {
	    String sql = "SELECT * FROM tax_and_discount WHERE id=?";
	    SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

	    if (rows.next()) {
	        TaxAndDiscountModel taxAndDiscount = new TaxAndDiscountModel();
	        taxAndDiscount.setId(rows.getInt("id"));
	        taxAndDiscount.setTaxPercentage(rows.getDouble("tax_percentage"));
	        taxAndDiscount.setDiscountPercentage(rows.getDouble("discount_percentage"));
	        return taxAndDiscount;
	    }
	    return null;
	}
	
	public void addDefaultTaxAndDiscount() {
	    String checkSql = "SELECT COUNT(*) FROM tax_and_discount WHERE id = 1";
	    int count = jdbcTemplate.queryForObject(checkSql, Integer.class);

	    if (count == 0) {
	        String insertSql = "INSERT INTO tax_and_discount (id, tax_percentage, discount_percentage) VALUES (1, 0.00, 0.00)";
	        jdbcTemplate.update(insertSql);
	    }
	}
    public boolean updateTaxAndDiscount(TaxAndDiscountModel taxAndDiscountModel) {
        String sql = "UPDATE tax_and_discount SET tax_percentage=?, discount_percentage=? WHERE id=?";
        int count = jdbcTemplate.update(sql, 
        	taxAndDiscountModel.getTaxPercentage(), taxAndDiscountModel.getDiscountPercentage(), taxAndDiscountModel.getId());

        return count > 0;
    }

}
