/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking;

import at.htlstp.syp.mmtasking.db.JPAUtil;
import at.htlstp.syp.mmtasking.db.MMTDBException;
import java.io.IOException;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class Main {

    public static void main(String[] args) throws IOException, MMTDBException {
//        Files.lines(Paths.get("src/main/resources/tasks.csv"))
//                .skip(1)
//                .map(line -> MMTUtil.createTaskFromCSV(line))
//                .forEach(System.out::println);
//        IMMTDAO dao = new MMTDAO();
//        dao.connect();
//        System.out.println(dao.isConnected());
        JPAUtil.getEMF().createEntityManager();
        JPAUtil.close();
    }
}
