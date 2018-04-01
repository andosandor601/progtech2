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

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class ProductDao extends GenericDao<Product, String> {

    public ProductDao(Connection con) {
        super(con, "prodect", "productName");
    }
    
    @Override
    public void delete(String key) {
        String sql = "DELETE FROM \"USERNAME\".\"product\" WHERE productName = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, key);

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            close(statement, resultSet);
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"product\"";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);

            resultSet = statement.executeQuery();
            List<Product> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setProduct(resultSet));
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
    public Product findById(String key) {
        String sql = "SELECT * FROM \"USERNAME\".\"product\" WHERE productName = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, key);

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

    @Override
    public Product save(Product entity) {
        String sql = "INSERT INTO \"USERNAME\".\"product\" (productName, price, stock) VALUES (?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, entity.getProductName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setInt(3, entity.getStock());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }

        return null;
    }

    @Override
    public void update(Product entity) {
        String sql = "UPDATE \"USERNAME\".\"product\" SET price=?, stock=? WHERE productName=?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setBigDecimal(1, entity.getPrice());
            statement.setInt(2, entity.getStock());
            statement.setString(3, entity.getProductName());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
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
