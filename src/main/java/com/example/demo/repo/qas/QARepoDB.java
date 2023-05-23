package com.example.demo.repo.qas;

import com.example.demo.model.Programmer;
import com.example.demo.model.QA;
import com.example.demo.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class QARepoDB implements QARepo {
    private static SessionFactory sessionFactory;

    private static HibernateUtils hibernateUtils;

    public QARepoDB() {}

    public static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public List<QA> getAll() {
        SessionFactory ses = HibernateUtils.getSession();
        try (Session session = ses.openSession()) {
            Transaction transact = null;
            try {
                transact = session.beginTransaction();
                List<QA> qas = session.createQuery("FROM QA ", QA.class).list();
                System.out.println(qas.size() + " qas");
                transact.commit();
                return qas;
            } catch (Exception e) {
                System.err.println("Error when getting all the qas " + e);
                if (transact != null) {
                    transact.rollback();
                }
            }
        }
        return null;
    }

    @Override
    public boolean delete(QA entity) throws IOException {
        return false;
    }

    @Override
    public QA update(QA entity) throws IOException {
        return null;
    }

    @Override
    public QA save(QA entity) throws IOException {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public QA findByUsernamePwd(String username, String password) {
        SessionFactory ses = HibernateUtils.getSession();
        try (Session session = ses.openSession()) {
            Transaction transact = null;
            try {
                transact = session.beginTransaction();
                Query<QA> query = session.createQuery("FROM QA WHERE username=:username AND password=:password");
                query.setParameter("username", username);
                query.setParameter("password", password);
                QA qa = query.getSingleResult();
                System.out.println("Found QA " + qa.toString());
                transact.commit();
                return qa;
            } catch (Exception e) {
                System.err.println("Error when getting the QA " + e);
                if (transact != null) {
                    transact.rollback();
                }
            }
        }
        return null;
    }
}
