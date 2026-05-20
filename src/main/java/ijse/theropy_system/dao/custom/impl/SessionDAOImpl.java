package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.SessionDAO;
import ijse.theropy_system.entity.TherapySessions;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SessionDAOImpl implements SessionDAO {

    @Override
    public boolean save(TherapySessions entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save session: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(TherapySessions entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to update session: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            TherapySessions sessionEntity = session.get(TherapySessions.class, id);
            if (sessionEntity != null) {
                session.remove(sessionEntity);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            throw new Exception("Failed to delete session: " + e.getMessage(), e);
        }
    }

    @Override
    public TherapySessions findById(Integer id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(TherapySessions.class, id);
        }
    }

    @Override
    public List<TherapySessions> findAll() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<TherapySessions> query = session.createQuery("FROM TherapySessions", TherapySessions.class);
            return query.list();
        }
    }

    @Override
    public List<TherapySessions> findByDate(LocalDate date) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<TherapySessions> query = session.createQuery(
                    "FROM TherapySessions WHERE date = :date", TherapySessions.class);
            query.setParameter("date", date);
            return query.list();
        }
    }

    @Override
    public List<TherapySessions> findByPatientId(String patientId) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM TherapySessions s WHERE s.patient.id = :patientId";
            Query<TherapySessions> query = session.createQuery(hql, TherapySessions.class);
            query.setParameter("patientId", patientId);
            return query.list();
        }
    }

    @Override
    public List<TherapySessions> findByTherapistId(String therapistId) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM TherapySessions s WHERE s.therapist.id = :therapistId";
            Query<TherapySessions> query = session.createQuery(hql, TherapySessions.class);
            query.setParameter("therapistId", therapistId);
            return query.list();
        }
    }

    @Override
    public boolean hasSchedulingConflict(String therapistId, LocalDate date, LocalTime time) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT COUNT(s) FROM TherapySessions s " +
                    "WHERE s.therapist.id = :therapistId AND s.date = :date " +
                    "AND s.time = :time AND s.status != :cancelledStatus";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("therapistId", therapistId);
            query.setParameter("date", date);
            query.setParameter("time", time);
            query.setParameter("cancelledStatus", TherapySessions.SessionStatus.CANCELLED);
            return query.uniqueResult() > 0;
        }
    }
}
