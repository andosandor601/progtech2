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
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Product;
import progtech2.backend.entities.Retailer;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderLineDao extends GenericDao<OrderLine, Long> implements IOrderLineDao {

    public OrderLineDao(Connection con) {
        super(con, "orderline", "orderLineId");
    }
    
    @Override
    public void delete(Long key) {
        String sql = "DELETE FROM \"USERNAME\".\"orderLine\" WHERE orderLineId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setLong(1, key);

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            close(statement, resultSet);
        }
    }

    @Override
    public List<OrderLine> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"orderLine\"";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);

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

    @Override
    public OrderLine findById(Long key) {
        String sql = "SELECT * FROM \"USERNAME\".\"orderLine\" WHERE orderLineId = ?";
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
            return result.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    private OrderLine setOrderLine(ResultSet resultSet) throws SQLException {
        //orderlineid, orderid, price, productname, quantity
        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(resultSet.getLong("orderLineId"));
        orderLine.setOrderId(resultSet.getLong("orderId"));
        orderLine.setPrice(resultSet.getBigDecimal("price"));
        orderLine.setProduct(resultSet.getString("productName"));
        orderLine.setQuantity(resultSet.getInt("quantity"));
        return orderLine;
    }

    @Override
    public OrderLine save(OrderLine entity) {
        String sql = "INSERT INTO \"USERNAME\".\"orderLine\" (orderId, price, productName, quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, entity.getOrderId());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setString(3, entity.getProduct());
            statement.setInt(4, entity.getQuantity());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setOrderLineId(generatedKeys.getLong(1));
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
    public void update(OrderLine entity) {
        String sql = "UPDATE \"USERNAME\".\"orderLine\" SET quantity=? WHERE orderLineId=?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, entity.getQuantity());
            statement.setLong(2, entity.getOrderLineId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
    }

    @Override
    public Product findProductByOrderLineId(long key) {
        String sql = "SELECT * FROM \"USERNAME\".\"product\" WHERE productName = (SELECT productName FROM \"USERNAME\".\"orderLine\" WHERE orderLineId=?)";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setLong(1, key);

            resultSet = statement.executeQuery();
            List<Product> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setProduct(resultSet));
            }
            return result.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    private Product setProduct(ResultSet resultSet) throws SQLException {
        //price productname stock
        Product product = new Product();
        product.setProductName(resultSet.getString("productName"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setStock(resultSet.getInt("stock"));
        return product;
    }

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
