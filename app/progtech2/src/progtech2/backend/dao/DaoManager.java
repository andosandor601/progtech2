/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Product;
import progtech2.backend.entities.Retailer;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class DaoManager {

    private static final String URL = "jdbc:derby:progtech2";
    private static final String USER = "username";
    private static final String PASSWORD = "password";

    private Connection con;

    private ProductDao pDao;
    private RetailerDao rDao;
    private OrderDao oDao;
    private OrderLineDao olDao;

    public DaoManager() {
        this.oDao = new OrderDao(con);
        this.rDao = new RetailerDao(con);
        this.olDao = new OrderLineDao(con);
        this.pDao = new ProductDao(con);
    }

    public Retailer findRetailer(String retailerName) {
        open();
        rDao.setCon(con);
        Retailer retailer = rDao.findById(retailerName);
        close();
        return retailer;
    }

    public Order saveOrder(Order order, List<OrderLine> orderLines) {
        open();
        oDao.setCon(con);
        Order savedOrder = oDao.save(order);
        close();
        for (OrderLine orderLine : orderLines) {
            orderLine.setOrderId(savedOrder.getOrderId());
            saveOrderLine(orderLine);
        }
        return savedOrder;
    }

    public Product findProduct(String productName) {
        open();
        pDao.setCon(con);
        Product product = pDao.findById(productName);
        close();
        return product;
    }

    public OrderLine saveOrderLine(OrderLine orderLine) {
        open();
        olDao.setCon(con);
        OrderLine savedOrderLine = olDao.save(orderLine);
        close();
        return savedOrderLine;
    }

    public void saveProduct(Product product) {
        open();
        pDao.setCon(con);
        pDao.save(product);
        close();
    }
    
     public void saveRetailer(Retailer retailer) {
        open();
        rDao.setCon(con);
        rDao.save(retailer);
        close();
    }

    public void deleteOrder(long orderId) {
        open();
        oDao.setCon(con);
        oDao.delete(orderId);
        close();
    }

    public List<OrderLine> findOrderLinesByOrderId(long orderId) {
        open();
        oDao.setCon(con);
        List<OrderLine> orderLines = oDao.findOrderLinesByOrderId(orderId);
        close();
        return orderLines;
    }

    public OrderLine findOrderLine(long orderLineId) {
        open();
        olDao.setCon(con);
        OrderLine orderLine = olDao.findById(orderLineId);
        close();
        return orderLine;
    }

    public void deleteOrderLine(long orderLineId) {
        open();
        olDao.setCon(con);
        olDao.delete(orderLineId);
        close();
    }

    public void deleteProduct(String productName) {
        open();
        pDao.setCon(con);
        pDao.delete(productName);
        close();
    }

    public List<Order> listNotDeliveredOrders() {
        open();
        oDao.setCon(con);
        List<Order> orders = oDao.findOrdersWithUnderDeliveryOrWaitingForDeliveryStatus();
        close();
        return orders;
    }

    public List<OrderLine> listOrderLines(long orderId) {
        open();
        oDao.setCon(con);
        List<OrderLine> orderLines = oDao.findOrderLinesByOrderId(orderId);
        close();
        return orderLines;
    }

    public List<Order> listOrders() {
        open();
        oDao.setCon(con);
        List<Order> orders = oDao.findAll();
        close();
        return orders;
    }

    public List<Order> listOrdersByRetailer(String retailerName) {
        open();
        rDao.setCon(con);
        List<Order> orders = rDao.findOrdersByRetailerId(retailerName);
        close();
        return orders;
    }

    public List<Product> listProducts() {
        open();
        pDao.setCon(con);
        List<Product> products = pDao.findAll();
        close();
        return products;
    }

    public List<Retailer> listRetailers() {
        open();
        rDao.setCon(con);
        List<Retailer> retailers = rDao.findAll();
        close();
        return retailers;
    }

    public Order findOrder(long orderId) {
        open();
        oDao.setCon(con);
        Order order = oDao.findById(orderId);
        close();
        return order;
    }

    public void updateOrder(Order order) {
        open();
        oDao.setCon(con);
        oDao.update(order);
        close();
    }

    public void updateProduct(Product product) {
        open();
        pDao.setCon(con);
        pDao.update(product);
        close();
    }

    public void updateRetailer(Retailer retailer) {
        open();
        rDao.setCon(con);
        rDao.update(retailer);
        close();
    }

    private void open() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void close() {
        try {
            if ((con != null) && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
