package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.PaymentDAO;
import ijse.theropy_system.entity.Payment;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save payment: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Payment entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to update payment: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Payment payment = session.get(Payment.class, id);
            if (payment != null) {
                session.remove(payment);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            throw new Exception("Failed to delete payment: " + e.getMessage(), e);
        }
    }

    @Override
    public Payment findById(Integer id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Payment.class, id);
        }
    }

    @Override
    public List<Payment> findAll() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Payment> query = session.createQuery("FROM Payment", Payment.class);
            return query.list();
        }
    }

    @Override
    public List<Payment> findByPatientId(String patientId) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Payment p WHERE p.patient.id = :patientId";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("patientId", patientId);
            return query.list();
        }
    }

    @Override
    public List<Payment> findByDateRange(LocalDate from, LocalDate to) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Payment p WHERE p.date BETWEEN :from AND :to";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            return query.list();
        }
    }

    @Override
    public List<Payment> findByStatus(String status) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Payment p WHERE p.status = :status";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("status", Payment.PaymentStatus.valueOf(status));
            return query.list();
        }
    }

    @Override
    public String generateInvoiceNumber() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM Payment p", Long.class);
            Long count = query.uniqueResult();
            return "INV-" + String.format("%06d", (count != null ? count : 0) + 1);
        }
    }
}
