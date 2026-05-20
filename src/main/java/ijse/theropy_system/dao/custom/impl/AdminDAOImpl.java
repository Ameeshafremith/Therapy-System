package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.AdminDAO;
import ijse.theropy_system.entity.Admin;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin findByUsername(String username) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Admin> query = session.createQuery(
                    "FROM Admin WHERE username = :username", Admin.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }

    @Override
    public boolean save(Admin admin) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(admin);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save admin: " + e.getMessage(), e);
        }
    }
}
