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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import progtech2.backend.entities.Product;

/**
 * ProductDao osztály. A temékekkel kapcsolatos adatbázis műveletek
 * végrehajtásáért felel.
 *
 * @author <Andó Sándor Zsolt>
 */
public class ProductDao implements IProductDao{

    private Connection con;

    public ProductDao(Connection con) {
        this.con = con;
    }

    @Override
    public void delete(String key) {

        /**
         * sql lekérdezés
         *
         * \"USERNAME\".\"product\" => adatbázis.táblanév, ? => paraméter
         */
        String sql = "DELETE FROM \"USERNAME\".\"product\" WHERE productName = ?";
        //try-with-resources try-catch-finally helyett lásd effective java item 9
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            //paraméter beállítása
            statement.setString(1, key);

            //.executeUpdate() => SQL Data Manipulation Language (DML) (Update, Insert, Delete) típusú lekérdezés végrehajtása.
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"product\"";
        //try-with-resources try-catch-finally helyett lásd effective java item 9
        //.executeQuery => sql leérdezés végrehajtása, egy resultSet objektummal tér vissza
        try (PreparedStatement statement = con.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();) {

            //resultSet feldolgozása
            List<Product> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setProduct(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Product findById(String key) {
        String sql = "SELECT * FROM \"USERNAME\".\"product\" WHERE productName = ?";
        try (PreparedStatement statement = createPreparedStatement(con, sql, key);
                ResultSet resultSet = statement.executeQuery();) {

            List<Product> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setProduct(resultSet));
            }
            return result.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * resultSet alapján egy új Product objektum létrehozása
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Product setProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setProductName(resultSet.getString("productName"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setStock(resultSet.getInt("stock"));
        return product;
    }

    @Override
    public Product save(Product entity) {
        String sql = "INSERT INTO \"USERNAME\".\"product\" (productName, price, stock) VALUES (?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, entity.getProductName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setInt(3, entity.getStock());
            statement.executeUpdate();
            return entity;
            
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public void update(Product entity) {
        String sql = "UPDATE \"USERNAME\".\"product\" SET price=?, stock=? WHERE productName=?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            
            statement.setBigDecimal(1, entity.getPrice());
            statement.setInt(2, entity.getStock());
            statement.setString(3, entity.getProductName());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
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
