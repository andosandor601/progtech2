/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Product;
import progtech2.backend.entities.Retailer;
import progtech2.backend.enums.OrderStatus;
import progtech2.backend.service.IService;
import progtech2.backend.service.Service;
import progtech2.backend.service.exceptions.ServiceException;
import progtech2.frontend.validator.Validator;
import progtech2.frontend.windows.DashboardWindow;
import progtech2.frontend.windows.OrderLineWindow;
import progtech2.frontend.windows.OrderStatusWindow;
import progtech2.frontend.windows.OrderWindow;
import progtech2.frontend.windows.ProductWindow;
import progtech2.frontend.windows.RetailerWindow;

/**
 * A felhasználói interfészeket fogja össze, továbbítja a felhasználói kéréseket
 * a Service-nek
 *
 * @author <Andó Sándor Zsolt>
 */
public final class GuiManager {

    private static DashboardWindow screen;
    private static final IService service = new Service();

    private GuiManager() {
    }

    public static void start() {
        screen = new DashboardWindow();
        screen.pack();
        screen.setVisible(true);
    }

    public static List<Product> listAllProducts() {
        return service.listProducts();
    }

    public static List<Retailer> listAllRetailers() {
        return service.listRetailers();
    }

    public static List<Order> listAllOrders() {
        return service.listOrders();
    }

    public static List<OrderLine> listOrderLines(String orderId) {
        if (Validator.validateLong(orderId, screen)) {
            return service.listOrderLines(Long.parseLong(orderId));
        }
        return null;
    }

    public static void addNewProduct(String name, String price, String stock) {
        if (Validator.validateProduct(name, price, stock, screen)) {
            service.addProduct(name, new BigDecimal(price), Integer.parseInt(stock));
        }
        screen.doListProducts();
    }

    public static void addNewRetailer(String name, String address, String creditLine, String phone) {
        if (Validator.validateRetailer(name, address, creditLine, phone, screen)) {
            service.addRetailer(name, address, new BigDecimal(creditLine), phone);
        }
        screen.doListRetailers();
    }

    public static void addNewOrder(List<OrderLine> orderLines, Object selectedItem) {
        if (Validator.validateOrder((String) selectedItem, orderLines, screen)) {
            try {
                service.addOrder((String) selectedItem, orderLines);
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(screen, ex);
            }
        }
        screen.doListOrders();
    }

    public static void deleteOrder(String orderId) {
        if (Validator.validateLong(orderId, screen)) {
            service.deleteOrder(Long.parseLong(orderId));
        }
    }

    public static void deleteOrderLine(long orderLineId) {
        service.deleteOrderLine(orderLineId);
    }

    public static void deleteProduct(String ID) {
        try {
            service.deleteProduct(ID);
            screen.doListProducts();
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(screen, ex);
        }
    }

    public static BigDecimal getProductPrice(String productName) {
        Product product = service.findProduct(productName);
        return product.getPrice();
    }

    public static void modifyOrder(String orderId, Object selectedItem) {
        if (Validator.validateLong(orderId, screen)) {
            service.modifyOrderStatus(Long.parseLong(orderId), (OrderStatus) selectedItem);
        }
        screen.doListOrders();
    }

    public static void modifyProduct(String ID, BigDecimal price, int quantity) {
        service.modifyProduct(ID, price, quantity);
        screen.doListProducts();
    }

    public static void modifyRetailer(String ID, String address, BigDecimal creditLine, String phone) {
        service.modifyRetailer(ID, address, creditLine, phone);
        screen.doListRetailers();
    }

    public static void filterStatusOfOrder() {
        screen.addContent(service.listNotDeliveredOrders());
    }

    public static void filterRetailer(String retailerName) {
        screen.addContent(service.listOrdersByRetailer(retailerName));
    }

    public static void showOrderWindow() {
        OrderWindow actionWindow = new OrderWindow();
        actionWindow.pack();
        actionWindow.setVisible(true);
    }

    public static void showOrderStatusWindow() {
        OrderStatusWindow actionWindow = new OrderStatusWindow(screen);
        actionWindow.pack();
        actionWindow.setVisible(true);
    }

    public static void showOrderLineWindow(Object orderLineId, Object orderId) {
        if (Validator.validateLong((String) orderId, screen) && Validator.validateLong((String) orderLineId, screen)) {
            OrderLineWindow actionWindow = new OrderLineWindow(screen, Long.parseLong((String) orderLineId), Long.parseLong((String) orderId));
            actionWindow.pack();
            actionWindow.setVisible(true);
        }
    }

    public static void showProductWindow(Object id, Object price, Object stock) {
        ProductWindow actionWindow = new ProductWindow(screen, (String) id, new BigDecimal((String) price), Integer.parseInt((String) stock));
        actionWindow.pack();
        actionWindow.setVisible(true);
    }

    public static void showRetailerWindow(Object id, Object address, Object creditLine, Object phone) {
        RetailerWindow actionWindow = new RetailerWindow(screen, (String) id, (String) address, new BigDecimal((String) creditLine), (String) phone);
        actionWindow.pack();
        actionWindow.setVisible(true);
    }
}
