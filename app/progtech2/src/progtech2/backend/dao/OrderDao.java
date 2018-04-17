/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.enums.OrderStatus;

/**
 * OrderDao osztály, A rendelésekkel kapcsolatos adatbázis műveletek végrehajtásáért felel.
 * 
 * @author <Andó Sándor Zsolt>
 */
public class OrderDao extends GenericDao<Order, Long> implements IOrderDao {

    public OrderDao(Connection con) {
        super(con, "order", "orderId");
    }

    @Override
    public void delete(Long key) {

        /**
         * sql lekérdezés
         *
         * \"USERNAME\".\"order\" => adatbázis.táblanév,
         * ? => paraméter
         */
        String sql = "DELETE FROM \"USERNAME\".\"order\" WHERE orderId = ?";
        
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);

            //paraméter beállítása
            statement.setLong(1, key);

            //.executeUpdate() => SQL Data Manipulation Language (DML) (Update, Insert, Delete) típusú lekérdezés végrehajtása.
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //statement és resultset lezárása
            close(statement, resultSet);
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"order\"";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);

            //.executeQuery => sql leérdezés végrehajtása, egy resultSet objektummal tér vissza
            resultSet = statement.executeQuery();
            
            //resultSet feldolgozása
            List<Order> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrder(resultSet));
            }
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    @Override
    public Order findById(Long key) {
        String sql = "SELECT * FROM \"USERNAME\".\"order\" WHERE orderId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setLong(1, key);

            resultSet = statement.executeQuery();
            List<Order> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrder(resultSet));
            }
            return result.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    /**
     * resultSet alapján egy új Order objektum létrehozása
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

    @Override
    public List<OrderLine> findOrderLinesByOrderId(long key) {
        String sql = "SELECT * FROM \"USERNAME\".\"orderLine\" WHERE orderId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setLong(1, key);

            resultSet = statement.executeQuery();
            List<OrderLine> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrderLine(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
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
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            
            //Statement.RETURN_GENERATED_KEYS => beállítjuk, hogy a generált kulcs visszakérhető legyen
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, entity.getRetailerName());
            statement.setDate(2, new java.sql.Date(entity.getOrderDate().getTime()));
            statement.setBigDecimal(3, entity.getOrderPrice());
            statement.setString(4, entity.getStatus().name());

            statement.executeUpdate();

            //generált kulcs lekérése
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setOrderId(generatedKeys.getLong(1));
                return entity;
            } else {
                throw new SQLException("failed order creation");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    @Override
    public void update(Order entity) {
        String sql = "UPDATE \"USERNAME\".\"order\" SET orderPrice=?, status=? WHERE orderId=?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setBigDecimal(1, entity.getOrderPrice());
            statement.setString(2, entity.getStatus().name());
            statement.setLong(3, entity.getOrderId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
    }

    @Override
    public List<Order> findOrdersWithUnderDeliveryOrWaitingForDeliveryStatus() {
        String sql = "SELECT * FROM \"USERNAME\".\"order\" WHERE status=? or status=?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, OrderStatus.UNDER_DELIVERY.name());
            statement.setString(2, OrderStatus.WAITING_FOR_DELIVERY.name());

            resultSet = statement.executeQuery();
            List<Order> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrder(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
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
            Logger.getLogger(RetailerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
