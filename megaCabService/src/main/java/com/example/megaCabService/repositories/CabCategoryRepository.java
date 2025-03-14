package com.example.megaCabService.repositories;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.megaCabService.models.CategoryModel;

@Repository
public class CabCategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean categoryExists(String category) {
        String sql = "SELECT COUNT(*) FROM categories WHERE category = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, category);
        return count != null && count > 0;
    }

    public CategoryModel getCategory(int categoryID) {
        String sql = "SELECT * FROM categories WHERE categoryID = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, categoryID);

        if (rows.next()) {
            CategoryModel category = new CategoryModel();
            category.setCategoryID(rows.getInt("categoryID"));
            category.setCategory(rows.getString("category"));
            category.setPrice(rows.getBigDecimal("price"));
            category.setDriverCost(rows.getBigDecimal("driverCost"));
            return category;
        }
        return null;
    }

    public CategoryModel getCategoryByName(String category) {
        String sql = "SELECT * FROM categories WHERE category = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, category);

        if (rows.next()) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setCategoryID(rows.getInt("categoryID"));
            categoryModel.setCategory(rows.getString("category"));
            categoryModel.setPrice(rows.getBigDecimal("price"));
            categoryModel.setDriverCost(rows.getBigDecimal("driverCost"));
            return categoryModel;
        }
        return null;
    }

    public CategoryModel addCategory(CategoryModel categoryModel) {
        String sql = "INSERT INTO categories (category, price, driverCost) VALUES (?, ?, ?)";

        int count = jdbcTemplate.update(sql, 
            categoryModel.getCategory(), 
            categoryModel.getPrice(),
            categoryModel.getDriverCost()
        );

        if (count > 0) {
            int id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            return getCategory(id);
        }
        return null;
    }
    
    public boolean deleteCabCategory(int categoryID) {
        String sql = "DELETE FROM categories WHERE categoryID = ?";
        int count = jdbcTemplate.update(sql, categoryID);
        return count > 0;
    }
    
    public boolean updateCabCategory(CategoryModel categoryModel) {
        String sql = "UPDATE categories SET category=?, price=?, driverCost=? WHERE categoryID=?";
        int count = jdbcTemplate.update(sql, 
            categoryModel.getCategory(), categoryModel.getPrice(), categoryModel.getDriverCost(), categoryModel.getCategoryID());

        return count > 0;
    }

    public List<CategoryModel> getAllCategories() {
        String sql = "SELECT * FROM categories";
        List<CategoryModel> categories = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while (rows.next()) {
            CategoryModel category = new CategoryModel();
            category.setCategoryID(rows.getInt("categoryID"));
            category.setCategory(rows.getString("category"));
            category.setPrice(rows.getBigDecimal("price"));
            category.setDriverCost(rows.getBigDecimal("driverCost"));
            categories.add(category);
        }
        return categories;
    }
}
