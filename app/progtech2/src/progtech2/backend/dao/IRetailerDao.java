/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.util.List;
import progtech2.backend.entities.Order;

/**
 * Kereskedőhöz tartozó Dao Interfész
 *
 * @author Andó Sándor Zsolt
 */
public interface IRetailerDao {

    /**
     * A megadott kulcsú kereskedő rendeléseinek listázása
     *
     * @param key
     * @return
     */
    List<Order> findOrdersByRetailerId(String key);

    /**
     * Az összes olyan rendelés kilistázása, amit még nem kezdtek el
     * kiszállítani
     *
     * @return
     */
    List<Order> findOrdersWithUnderDeliveryOrWaitingForDeliveryStatus();

}
