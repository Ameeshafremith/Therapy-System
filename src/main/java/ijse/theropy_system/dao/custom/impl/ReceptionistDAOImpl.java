package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.ReceptionistDAO;
import ijse.theropy_system.entity.Receptionist;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ReceptionistDAOImpl implements ReceptionistDAO {

    @Override
    public Receptionist findByEmail(String email) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Receptionist> query = session.createQuery(
                    "FROM Receptionist WHERE email = :email", Receptionist.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }

    @Override
    public boolean save(Receptionist receptionist) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(receptionist);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save receptionist: " + e.getMessage(), e);
        }
    }
}
