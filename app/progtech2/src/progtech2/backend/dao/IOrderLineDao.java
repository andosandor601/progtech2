/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import progtech2.backend.entities.Product;

/**
 * Rendeléssorhoz tartozó Dao Interfész
 *
 * @author Andó Sándor Zsolt
 */
public interface IOrderLineDao {

    /**
     * Az adott kulcsú rendeléssorhoz tartozó termék megkeresése
     *
     * @param key
     * @return
     */
    Product findProductByOrderLineId(long key);

}
