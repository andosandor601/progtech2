/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.service;

import java.math.BigDecimal;
import java.util.List;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.Product;
import progtech2.backend.entities.Retailer;
import progtech2.backend.enums.OrderStatus;

/**
 * Az alkalmazás szerviz interfésze
 *
 * @author <Andó Sándor Zsolt>
 */
public interface IService {

    /**
     * Új rendelés elmentése
     *
     * @param retailerName
     * @param orderLines
     */
    void addOrder(String retailerName, List<OrderLine> orderLines);

    /**
     * Új rendeléssor elmentése
     *
     * @param productName
     * @param quantity
     * @return
     */
    OrderLine addOrderLine(String productName, int quantity);

    /**
     * Új termék elmentése
     *
     * @param productName
     * @param price
     * @param stock
     */
    void addProduct(String productName, BigDecimal price, int stock);

    /**
     * A megadott azonosítójú rendelés törlése a megadott kiskereskedőtől
     *
     * @param retailerName
     * @param orderId
     */
    void deleteOrder(String retailerName, long orderId);

    /**
     * A megadott azonosítójú rendeléssor törlése, a megadott azonosítójú
     * rendelésből
     *
     * @param orderId
     * @param orderLineId
     */
    void deleteOrderLine(long orderId, long orderLineId);

    /**
     * A megadott azonosítójú termék törlése
     *
     * @param productName
     */
    void deleteProduct(String productName);

    /**
     * Azon termékek listázáse, amik még nincsenek kiszállítás alatt, vagy nem
     * kerültek kiszállításra
     *
     * @return
     */
    List<Order> listNotDeliveredOrders();

    /**
     * Az adott azonosítójú rendeléshez tartozó rendeléssorok listázása
     *
     * @param orderId
     * @return
     */
    List<OrderLine> listOrderLines(long orderId);

    /**
     * Rendelések listázása
     *
     * @return
     */
    List<Order> listOrders();

    /**
     * Rendelések listázása megadott kereskedő neve alapján
     *
     * @param retailerName
     * @return
     */
    List<Order> listOrdersByRetailer(String retailerName);

    /**
     * Termékek listázása
     *
     * @return
     */
    List<Product> listProducts();

    /**
     * Kiskereskedők listázása
     *
     * @return
     */
    List<Retailer> listRetailers();

    /**
     * A megadott rendelés státuszának módosítása
     *
     * @param orderId
     * @param status
     */
    void modifyOrderStatus(long orderId, OrderStatus status);

    /**
     * A megadott termék árának módosítása
     *
     * @param productName
     * @param newPrice
     */
    void modifyProductPrice(String productName, BigDecimal newPrice);

    /**
     * A megadott termék, raktáron tárolt mennyiségének a módosítása
     *
     * @param productName
     * @param newQuantity
     */
    void modifyProductQuantity(String productName, int newQuantity);

}
