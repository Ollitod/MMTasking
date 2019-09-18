/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.db;

import at.htlstp.syp.mmtasking.Setup;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class JPAUtil {

    private static final EntityManagerFactory EMF;

    static {
        Map<String, String> properties = new HashMap<>();

        properties.put("javax.persistence.jdbc.url", "jdbc:ucanaccess://" + Setup.getDatabaseURL());
        properties.put("javax.persistence.jdbc.user", "");
        properties.put("javax.persistence.jdbc.password", "");
        properties.put("javax.persistence.jdbc.driver", "net.ucanaccess.jdbc.UcanaccessDriver");
        
        try {
            EMF = Persistence.createEntityManagerFactory("at.htlstp.syp.MMTasking", properties);
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
