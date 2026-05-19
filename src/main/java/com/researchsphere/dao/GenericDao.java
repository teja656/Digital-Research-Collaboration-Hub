package com.researchsphere.dao;

import com.researchsphere.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * Base DAO with common Hibernate CRUD helpers.
 */
public abstract class GenericDao<T> {

    private final Class<T> entityClass;

    protected GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected T execute(Function<Session, T> work) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            T result = work.apply(session);
            tx.commit();
            return result;
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }

    protected List<T> executeList(Function<Session, List<T>> work) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            List<T> result = work.apply(session);
            tx.commit();
            return result;
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }

    protected void executeVoid(Function<Session, Void> work) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            work.apply(session);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }

    protected long executeLong(Function<Session, Long> work) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Long result = work.apply(session);
            tx.commit();
            return result == null ? 0L : result;
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }

    public T findById(Serializable id) {
        return execute(session -> session.get(entityClass, id));
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return executeList(session -> session.createQuery("from " + entityClass.getSimpleName() + " order by id desc").list());
    }

    public void save(T entity) {
        executeVoid(session -> {
            session.saveOrUpdate(entity);
            return null;
        });
    }

    public void delete(T entity) {
        executeVoid(session -> {
            session.delete(session.contains(entity) ? entity : session.merge(entity));
            return null;
        });
    }
}
