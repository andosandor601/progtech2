/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.util.List;

/**
 * Generikus Dao Interfész, CRUD műveletek
 *
 * @author Andó Sándor Zsolt
 */
public interface IGenericDao<E, K> {

    /**
     * Egy kulcsértékkel megadott entitás törlése
     *
     * @param key
     */
    void delete(K key);

    /**
     * Egy entitás plédányainak visszaadása a megadott osztály szerint
     *
     * @param entityClass
     * @return
     */
    List<E> findAll();

    /**
     * Egy entitás megkeresése kulcs alapján
     *
     * @param key
     * @return
     */
    E findById(K key);

    /**
     * Egy új entitás elmentése
     *
     * @param entity
     */
    E save(E entity);

    /**
     * Egy entitás módosítása
     *
     * @param entity
     */
    void update(E entity);

}
