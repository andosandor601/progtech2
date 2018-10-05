/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.dao;

import java.util.List;
import hu.elte.progtech2.backend.entities.Order;
import hu.elte.progtech2.backend.entities.Retailer;

/**
 * Kereskedőhöz tartozó Dao Interfész
 *
 * @author Andó Sándor Zsolt
 */
public interface RetailerDao extends CRUDDao<Retailer, String>{

    /**
     * A megadott kulcsú kereskedő rendeléseinek listázása
     *
     * @param key
     * @return
     */
    List<Order> findOrdersByRetailerId(String key);

}
