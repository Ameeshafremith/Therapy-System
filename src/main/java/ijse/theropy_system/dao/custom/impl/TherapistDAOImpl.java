package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.TherapistDAO;
import ijse.theropy_system.entity.Therapist;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TherapistDAOImpl implements TherapistDAO {

    @Override
    public boolean save(Therapist entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save therapist: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Therapist entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to update therapist: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Therapist therapist = session.get(Therapist.class, id);
            if (therapist != null) {
                session.remove(therapist);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            throw new Exception("Failed to delete therapist: " + e.getMessage(), e);
        }
    }

    @Override
    public Therapist findById(String id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Therapist.class, id);
        }
    }

    @Override
    public List<Therapist> findAll() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Therapist> query = session.createQuery("FROM Therapist", Therapist.class);
            return query.list();
        }
    }

    @Override
    public List<Therapist> findByAvailability(String availability) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Therapist> query = session.createQuery(
                    "FROM Therapist WHERE availability = :availability", Therapist.class);
            query.setParameter("availability", availability);
            return query.list();
        }
    }

    @Override
    public List<Therapist> findByProgramId(String programId) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT t FROM Therapist t JOIN t.programs p WHERE p.id = :programId";
            Query<Therapist> query = session.createQuery(hql, Therapist.class);
            query.setParameter("programId", programId);
            return query.list();
        }
    }
}
