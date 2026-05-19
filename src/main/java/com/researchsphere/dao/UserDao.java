package com.researchsphere.dao;

import com.researchsphere.entity.User;
import org.hibernate.Session;

import java.util.List;

public class UserDao extends GenericDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findByEmail(String email) {
        return execute(session -> session.createQuery(
                "from User u where lower(u.email) = lower(:email)", User.class)
                .setParameter("email", email)
                .uniqueResult());
    }

    public List<User> findByRole(String role) {
        return executeList(session -> session.createQuery(
                "from User u where u.role = :role order by u.fullName", User.class)
                .setParameter("role", role)
                .list());
    }

    public long countAll() {
        return executeLong(session -> session.createQuery("select count(u) from User u", Long.class).uniqueResult());
    }
}
