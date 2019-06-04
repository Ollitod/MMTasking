/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.db;

import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.Category;
import at.htlstp.syp.mmtasking.model.Fahrt;
import at.htlstp.syp.mmtasking.model.Location;
import at.htlstp.syp.mmtasking.model.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author 20150202
 */
public class MMTDAO implements IMMTDAO {

    private static MMTDAO instance;

    private MMTDAO() {

    }

    public static MMTDAO getInstance() {
        if (instance == null) {
            instance = new MMTDAO();
        }
        return instance;
    }

    @Override
    public List<Task> getAllTasks() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Task> jQuery = em.createQuery("select t from Task t", Task.class);
        return jQuery.getResultList();
    }

    public List<Location> getAllLocations() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Location> jQuery = em.createQuery("select l from Location l ", Location.class);
        return jQuery.getResultList();
    }

    @Override
    public List<Task> getTasksByLocation(String location) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Task> jQuery = em.createQuery("select t from Task t where t.location = :location", Task.class);
        jQuery.setParameter("location", location);
        return jQuery.getResultList();
    }

    @Override
    public List<Task> getTasksBetween(LocalDateTime start, LocalDateTime end) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Task> jQuery = em.createQuery("select t from Task t where t.beginning between :start and :end or t.end between :start and :end", Task.class);
        jQuery.setParameter("start", start);
        jQuery.setParameter("end", end);
        return jQuery.getResultList();
    }

    @Override
    public List<Task> getTasksByCategory(String category) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Task> jQuery = em.createQuery("select t from Task t where t.category = :category", Task.class);
        jQuery.setParameter("category", category);
        return jQuery.getResultList();
    }

    @Override
    public boolean insertTask(Task t) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean updateTask(Task t) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Task dbT = null;
        if (t.getId() != null) {
            dbT = em.find(Task.class, t.getId());
        }
        try {
            if (dbT == null) {
                try {
                    tx.begin();
                    em.persist(t);
                    tx.commit();
                    return true;
                } catch (Exception ex) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw new MMTDBException(ex.getMessage());
                }
            } else {
                dbT.setBeginning(t.getBeginning());
                dbT.setCategory(t.getCategory());
                dbT.setEnd(t.getEnd());
                dbT.setNote(t.getNote());
                dbT.setPriority(t.getPriority());
                dbT.setTitle(t.getTitle());
                return true;
            }
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteTask(Task t) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new MMTDBException(ex.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> getAllAppointments() throws MMTDBException {
        List<Appointment> appointments = new ArrayList<>();

        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Appointment> jQuery = em.createQuery("select a from Appointment", Appointment.class
        );
        appointments = jQuery.getResultList();
        return appointments;
    }

    @Override
    public boolean insertAppointment(Appointment t) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean updateAppointment(Appointment appt) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Appointment dbT = null;
        if (appt.getId() != null) {
            dbT = em.find(Appointment.class, appt.getId());
        }
        try {
            if (dbT == null) {
                try {
                    tx.begin();
                    em.persist(appt);
                    tx.commit();
                    return true;
                } catch (Exception ex) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw new MMTDBException(ex.getMessage());
                }
            } else {
                dbT.setDate(appt.getDate());
                dbT.setLocation(appt.getLocation());
                dbT.setNote(appt.getNote());
                dbT.setTitle(appt.getTitle());
                return true;
            }
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteAppointment(Appointment t) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new MMTDBException(ex.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Category> getCategoriesforAnalyse() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Category> jQuery = em.createQuery("select c from Category c", Category.class);
        return jQuery.getResultList();
    }

    public List<Location> getLocationsforAnalyse() {
        List<Location> locations = new ArrayList<>();

        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Location> jQuery = em.createQuery("select l from Location l", Location.class);
        return jQuery.getResultList();
    }

    public List<Category> getAllCategories() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            TypedQuery<Category> jQuery = em.createQuery("select c from Category c", Category.class);
            return jQuery.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Category findCategoryByName(String bezeichnung) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            TypedQuery<Category> jQuery = em.createQuery("select c from Category c where c.catBez = :bez", Category.class);
            jQuery.setParameter("bez", bezeichnung);
            return jQuery.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public Fahrt getFahrtNach(Location location) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            TypedQuery<Fahrt> jQuery = em.createQuery("select f from Fahrt f where f.von = :von and f.nach = :nach", Fahrt.class);
            Location von = this.findLocationByName("Irnfritz");
            jQuery.setParameter("von", von);
            jQuery.setParameter("nach", location);
            return jQuery.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public Location findLocationByName(String name) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            TypedQuery<Location> jQuery = em.createQuery("select l from Location l where l.name = :name", Location.class);
            jQuery.setParameter("name", name);
            return jQuery.getSingleResult();
        } finally {
            em.close();
        }
    }
    
//    @Override
    public List<Task> getTasksByPeriod(LocalDateTime start, LocalDateTime end) throws MMTDBException {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        TypedQuery<Task> jQuery = em.createQuery("select t from Task t where t.beginning between :start and :end or t.end between :start and :end", Task.class);
        jQuery.setParameter("start", start);
        jQuery.setParameter("end", end);
        return jQuery.getResultList();
    }
}
