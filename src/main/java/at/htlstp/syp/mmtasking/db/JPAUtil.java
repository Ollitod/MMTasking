/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class JPAUtil {

    private static final EntityManagerFactory EMF;

    static {
        try {
            EMF = Persistence.createEntityManagerFactory("at.htlstp.syp.MMTasking");
        } catch (Exception ex) {
            System.err.println("EntityManagerFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEMF() {
        return EMF;
    }

    public static void close() {
        EMF.close();
    }
}
