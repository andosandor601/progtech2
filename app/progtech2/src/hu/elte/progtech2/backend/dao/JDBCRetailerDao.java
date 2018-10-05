/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.elte.progtech2.backend.entities.Order;
import hu.elte.progtech2.backend.entities.Retailer;
import hu.elte.progtech2.backend.enums.OrderStatus;

/**
 * JDBCRetailerDao osztály. A kereskedőkkel kapcsolatos adatbázis műveletek
 * végrehajtásáért felel.
 *
 * @author <Andó Sándor Zsolt>
 */
public class JDBCRetailerDao implements RetailerDao {

    private Connection con;

    public JDBCRetailerDao(Connection con) {
        this.con = con;
    }

    @Override
    public void delete(String key) {
        /**
         * sql lekérdezés
         *
         * \"USERNAME\".\"retailer\" => adatbázis.táblanév, ? => paraméter
         */
        String sql = "DELETE FROM \"USERNAME\".\"retailer\" WHERE name = ?";
        //try-with-resources try-catch-finally helyett lásd effective java item 9
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            //paraméter beállítása
            statement.setString(1, key);

            //.executeUpdate() => SQL Data Manipulation Language (DML) (Update, Insert, Delete) típusú lekérdezés végrehajtása.
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Retailer> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"retailer\"";
        //try-with-resources try-catch-finally helyett lásd effective java item 9
        //.executeQuery => sql leérdezés végrehajtása, egy resultSet objektummal tér vissza
        try (PreparedStatement statement = con.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();) {

            //resultSet feldolgozása
            List<Retailer> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setRetailer(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Retailer findById(String key) {
        String sql = "SELECT * FROM \"USERNAME\".\"retailer\" WHERE name = ?";
        try (PreparedStatement statement = createPreparedStatement(con, sql, key);
                ResultSet resultSet = statement.executeQuery();) {

            List<Retailer> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setRetailer(resultSet));
            }
            return result.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * resultSet alapján egy új Retailer objektum létrehozása
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Retailer setRetailer(ResultSet resultSet) throws SQLException {
        //address, creditline, name, phone
        Retailer retailer = new Retailer();
        retailer.setName(resultSet.getString("name"));
        retailer.setAddress(resultSet.getString("address"));
        retailer.setCreditLine(resultSet.getBigDecimal("creditLine"));
        retailer.setPhone(resultSet.getString("phone"));
        return retailer;
    }

    @Override
    public Retailer save(Retailer entity) {
        String sql = "INSERT INTO \"USERNAME\".\"retailer\" (name, address, creditLine, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getAddress());
            statement.setBigDecimal(3, entity.getCreditLine());
            statement.setString(4, entity.getPhone());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void update(Retailer entity) {
        String sql = "UPDATE \"USERNAME\".\"retailer\" SET address=?, creditLine=?, phone=? WHERE name=?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            
            statement.setString(1, entity.getAddress());
            statement.setBigDecimal(2, entity.getCreditLine());
            statement.setString(3, entity.getPhone());
            statement.setString(4, entity.getName());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Order> findOrdersByRetailerId(String key) {
        String sql = "SELECT * FROM \"USERNAME\".\"order\" WHERE retailerName = ?";
        try (PreparedStatement statement = createPreparedStatement(con, sql, key);
                ResultSet resultSet = statement.executeQuery();) {
            
            List<Order> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrder(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * resultSet alapján egy új Order objektum létrehozása
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Order setOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong("orderId"));
        order.setOrderDate(resultSet.getDate("orderDate"));
        order.setOrderPrice(resultSet.getBigDecimal("orderPrice"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setRetailerName(resultSet.getString("retailerName"));
        return order;
    }

    /**
     * statement, és resultSet lezárása
     *
     * @param statement
     * @param resultSet
     */
    private void close(PreparedStatement statement, ResultSet resultSet) {
        try {
            if (!(statement == null || statement.isClosed())) {
                statement.close();
            }
            if (!(resultSet == null || resultSet.isClosed())) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCRetailerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PreparedStatement createPreparedStatement(Connection con, String sql, String key) throws SQLException {
        PreparedStatement statement = con.prepareStatement(sql);

        statement.setString(1, key);

        return statement;
    }
    
    @Override
    public void setCon(Connection con) {
        this.con = con;
    }

}
