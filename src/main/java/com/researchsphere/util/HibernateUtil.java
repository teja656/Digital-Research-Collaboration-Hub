package com.researchsphere.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Lazy Hibernate SessionFactory — connects on first use, not at server startup.
 */
public final class HibernateUtil {

    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new Configuration().configure().buildSessionFactory();
                    } catch (Exception ex) {
                        throw new IllegalStateException(
                                "Database connection failed. Start MySQL and run START.bat again: "
                                        + ex.getMessage(), ex);
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
