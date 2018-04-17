/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public abstract class GenericDao<E, K> implements IGenericDao<E, K> {

    protected final String tableName;
    protected final String keyName;
    protected Connection con;

    protected GenericDao(Connection con, String tableName, String keyName) {
        this.con = con;
        this.tableName = tableName;
        this.keyName = keyName;
    }

    @Override
    public abstract void delete(K key);

    @Override
    public abstract List<E> findAll();

    @Override
    public abstract E findById(K key);

    @Override
    public abstract E save(E entity);

    @Override
    public abstract void update(E entity);

    protected void setCon(Connection con) {
        this.con = con;
    }

}
