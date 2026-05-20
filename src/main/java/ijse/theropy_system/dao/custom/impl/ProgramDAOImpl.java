package ijse.theropy_system.dao.custom.impl;

import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.entity.TheropyPrograms;
import ijse.theropy_system.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ProgramDAOImpl implements ProgramDAO {

    @Override
    public boolean save(TheropyPrograms entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to save program: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(TheropyPrograms entity) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to update program: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            TheropyPrograms program = session.get(TheropyPrograms.class, id);
            if (program != null) {
                session.remove(program);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            throw new Exception("Failed to delete program: " + e.getMessage(), e);
        }
    }

    @Override
    public TheropyPrograms findById(String id) throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(TheropyPrograms.class, id);
        }
    }

    @Override
    public List<TheropyPrograms> findAll() throws Exception {
        try (Session session = HibernateUtil.getSession()) {
            Query<TheropyPrograms> query = session.createQuery("FROM TheropyPrograms", TheropyPrograms.class);
            return query.list();
        }
    }
}
