package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.PatientDAO;
import ijse.theropy_system.entity.Patient;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save patient: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Patient entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to update patient: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Patient patient = session.get(Patient.class, id);
            if (patient != null) {
                session.remove(patient);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            throw new Exception("Failed to delete patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient findById(String id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Patient.class, id);
        }
    }

    @Override
    public List<Patient> findAll() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Patient> query = session.createQuery("FROM Patient", Patient.class);
            return query.list();
        }
    }

    @Override
    public List<Patient> searchByName(String name) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<Patient> query = session.createQuery(
                    "FROM Patient WHERE name LIKE :name", Patient.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        }
    }

    @Override
    public List<Patient> findPatientsEnrolledInAllPrograms() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            // HQL Join Query: Patients enrolled in ALL therapy programs
            // Uses relational division: find patients where the count of their
            // enrolled programs equals the total number of programs
            String hql = """
                SELECT p FROM Patient p
                WHERE (SELECT COUNT(DISTINCT prog) FROM p.programs prog) =
                      (SELECT COUNT(prog2) FROM TheropyPrograms prog2)
                """;
            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        }
    }

    @Override
    public List<Patient> findPatientsWithPrograms() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            // JPQL: Retrieve patients along with their enrolled therapy programs
            String hql = "SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.programs";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        }
    }
}
