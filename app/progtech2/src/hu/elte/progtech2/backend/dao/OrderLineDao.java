/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.dao;

import hu.elte.progtech2.backend.entities.OrderLine;
import hu.elte.progtech2.backend.entities.Product;

/**
 * Rendeléssorhoz tartozó Dao Interfész
 *
 * @author Andó Sándor Zsolt
 */
public interface OrderLineDao extends CRUDDao<OrderLine, Long>{

    /**
     * Az adott kulcsú rendeléssorhoz tartozó termék megkeresése
     *
     * @param key
     * @return
     */
    Product findProductByOrderLineId(long key);

}
