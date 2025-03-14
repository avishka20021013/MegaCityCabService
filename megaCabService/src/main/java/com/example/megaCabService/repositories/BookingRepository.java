package com.example.megaCabService.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.megaCabService.models.BookingModel;
import com.example.megaCabService.models.CabModel;

@Repository
public class BookingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public BookingModel getBooking(int bookingID) {
        String sql = "SELECT * FROM bookings WHERE bookingID=?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, bookingID);

        if (rows.next()) {
            BookingModel booking = new BookingModel();
            booking.setBookingID(rows.getInt("bookingID"));
            booking.setClientID(rows.getInt("clientID"));
            booking.setCabID(rows.getInt("cabID"));
            booking.setDriverStatus(rows.getString("driverStatus"));
            booking.setBookedDates(rows.getString("bookedDates"));
            booking.setBookingDate(rows.getString("bookingDate")); 
            booking.setBillAmount(rows.getDouble("billAmount"));
            return booking;
        }
        return null;
    }
    
    public List<BookingModel> getAllBookings() {
        String sql = "SELECT * FROM bookings";
        List<BookingModel> bookings = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while (rows.next()) {
        	BookingModel booking = new BookingModel();
            booking.setBookingID(rows.getInt("bookingID"));
            booking.setClientID(rows.getInt("clientID"));
            booking.setCabID(rows.getInt("cabID"));
            booking.setDriverStatus(rows.getString("driverStatus"));
            booking.setBookedDates(rows.getString("bookedDates"));
            booking.setBookingDate(rows.getString("bookingDate")); 
            booking.setBillAmount(rows.getDouble("billAmount"));
            bookings.add(booking);
        }
        return bookings;
    }
    
    @SuppressWarnings("deprecation")
    public List<CabModel> getAvailableCabsByDates(List<String> selectedDates, String category) {
        String sql = "SELECT * FROM cabs WHERE category = ? AND cabID NOT IN (" +
                     "SELECT DISTINCT cabID FROM bookings WHERE " +
                     "bookedDates REGEXP ?)";

        String regexPattern = "(" + String.join("|", selectedDates) + ")";

        return jdbcTemplate.query(sql, new Object[]{category, regexPattern}, (rs, rowNum) -> {
            CabModel cab = new CabModel();
            cab.setCabID(rs.getInt("cabID"));
            cab.setCabNumber(rs.getString("cabNumber"));
            cab.setModel(rs.getString("model"));
            cab.setCategory(rs.getString("category"));
            cab.setSeats(rs.getInt("seats"));
            cab.setDriverName(rs.getString("ownerName"));
            cab.setDriverContact(rs.getString("ownerContact"));
            cab.setImageUrl(rs.getString("imageUrl"));
            return cab;
        });
    }


    public BookingModel addBooking(BookingModel booking) {
        String sql = "INSERT INTO bookings (clientID, cabID, driverStatus, bookedDates, bookingDate, billAmount) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        int count = jdbcTemplate.update(sql, 
            booking.getClientID(), booking.getCabID(), booking.getDriverStatus(), 
            booking.getBookedDates(), booking.getBookingDate(), booking.getBillAmount());

        if (count > 0) {
            int id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            return getBooking(id);
        }
        return null; 
    }
    
    @SuppressWarnings("deprecation")
	public List<BookingModel> getBookingsByClientID(int clientID) {
        String sql = "SELECT * FROM bookings WHERE clientID = ?";
        return jdbcTemplate.query(sql, new Object[]{clientID}, (rs, rowNum) -> {
            BookingModel booking = new BookingModel();
            booking.setBookingID(rs.getInt("bookingID"));
            booking.setClientID(rs.getInt("clientID"));
            booking.setCabID(rs.getInt("cabID"));
            booking.setDriverStatus(rs.getString("driverStatus"));
            booking.setBookedDates(rs.getString("bookedDates"));
            booking.setBookingDate(rs.getString("bookingDate"));
            booking.setBillAmount(rs.getDouble("billAmount"));
            return booking;
        });
    }

    public boolean deleteBooking(int bookingID) {
        String sql = "DELETE FROM bookings WHERE bookingID = ?";
        int count = jdbcTemplate.update(sql, bookingID);
        return count > 0;
    }
    
    public int getBookingCount() {
        String sql = "SELECT COUNT(*) FROM bookings";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
