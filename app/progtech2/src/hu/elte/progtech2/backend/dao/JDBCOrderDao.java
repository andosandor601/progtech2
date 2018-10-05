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
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import hu.elte.progtech2.backend.entities.Order;
import hu.elte.progtech2.backend.entities.OrderLine;
import hu.elte.progtech2.backend.enums.OrderStatus;
import java.time.LocalDateTime;

/**
 * JDBCOrderDao osztály, A rendelésekkel kapcsolatos adatbázis műveletek
 végrehajtásáért felel.
 *
 * @author <Andó Sándor Zsolt>
 */
public class JDBCOrderDao implements OrderDao {

    private Connection con;

    public JDBCOrderDao(Connection con) {
        this.con = con;
    }

    @Override
    public void delete(Long key) {

        /**
         * sql lekérdezés
         *
         * \"USERNAME\".\"order\" => adatbázis.táblanév, ? => paraméter
         */
        String sql = "DELETE FROM \"USERNAME\".\"order\" WHERE orderId = ?";

        //try-with-resources try-catch-finally helyett lásd effective java item 9
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            //paraméter beállítása
            statement.setLong(1, key);

            //.executeUpdate() => SQL Data Manipulation Language (DML) (Update, Insert, Delete) típusú lekérdezés végrehajtása.
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"order\"";

        //try-with-resources try-catch-finally helyett lásd effective java item 9
        //.executeQuery => sql leérdezés végrehajtása, egy resultSet objektummal tér vissza
        try (PreparedStatement statement = con.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();) {
            //resultSet feldolgozása
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

    @Override
    public Order findById(Long key) {
        String sql = "SELECT * FROM \"USERNAME\".\"order\" WHERE orderId = ?";
        try (PreparedStatement statement = createPreparedStatement(con, sql, key);
                ResultSet resultSet = statement.executeQuery();) {

            List<Order> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrder(resultSet));
            }
            return result.get(0);
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
        order.setOrderDate(resultSet.getDate("orderDate").toLocalDate());
        order.setOrderPrice(resultSet.getBigDecimal("orderPrice"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setRetailerName(resultSet.getString("retailerName"));
        return order;
    }

    @Override
    public List<OrderLine> findOrderLinesByOrderId(long key) {
        String sql = "SELECT * FROM \"USERNAME\".\"orderLine\" WHERE orderId = ?";
        try (PreparedStatement statement = createPreparedStatement(con, sql, key);
                ResultSet resultSet = statement.executeQuery();) {

            List<OrderLine> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrderLine(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * resultSet alapján egy új OrderLine objektum létrehozása
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private OrderLine setOrderLine(ResultSet resultSet) throws SQLException {
        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(resultSet.getLong("orderLineId"));
        orderLine.setOrderId(resultSet.getLong("orderId"));
        orderLine.setPrice(resultSet.getBigDecimal("price"));
        orderLine.setProduct(resultSet.getString("productName"));
        orderLine.setQuantity(resultSet.getInt("quantity"));
        return orderLine;
    }

    @Override
    public Order save(Order entity) {
        String sql = "INSERT INTO \"USERNAME\".\"order\" (retailerName, orderDate, orderPrice, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = createPreparedStatementForSave(con, sql, entity);
                ResultSet generatedKeys = statement.getGeneratedKeys();) {

            if (generatedKeys.next()) {
                entity.setOrderId(generatedKeys.getLong(1));
                return entity;
            } else {
                throw new SQLException("failed order creation");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void update(Order entity) {
        String sql = "UPDATE \"USERNAME\".\"order\" SET orderPrice=?, status=? WHERE orderId=?";
        try (PreparedStatement statement = createPreparedStatementForUpdate(con, sql, entity);) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Order> findOrdersWithUnderDeliveryOrWaitingForDeliveryStatus() {
        String sql = "SELECT * FROM \"USERNAME\".\"order\" WHERE status=? or status=?";
        try (PreparedStatement statement = createPreparedStatementForStatusFilter(con, sql);
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

    private PreparedStatement createPreparedStatement(Connection con, String sql, Long key) throws SQLException {
        PreparedStatement statement = con.prepareStatement(sql);

        statement.setLong(1, key);

        return statement;
    }

    private PreparedStatement createPreparedStatementForSave(Connection con, String sql, Order entity) throws SQLException {
        //Statement.RETURN_GENERATED_KEYS => beállítjuk, hogy a generált kulcs visszakérhető legyen
        PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, entity.getRetailerName());
        statement.setDate(2, java.sql.Date.valueOf((entity.getOrderDate())));
        statement.setBigDecimal(3, entity.getOrderPrice());
        statement.setString(4, entity.getStatus().name());

        statement.executeUpdate();

        return statement;
    }

    private PreparedStatement createPreparedStatementForUpdate(Connection con, String sql, Order entity) throws SQLException {
        PreparedStatement statement = con.prepareStatement(sql);

        statement.setBigDecimal(1, entity.getOrderPrice());
        statement.setString(2, entity.getStatus().name());
        statement.setLong(3, entity.getOrderId());

        return statement;
    }

    private PreparedStatement createPreparedStatementForStatusFilter(Connection con, String sql) throws SQLException {
        PreparedStatement statement = con.prepareStatement(sql);

        statement.setString(1, OrderStatus.UNDER_DELIVERY.name());
        statement.setString(2, OrderStatus.WAITING_FOR_DELIVERY.name());

        return statement;
    }

    @Override
    public void setCon(Connection con) {
        this.con = con;
    }

}
