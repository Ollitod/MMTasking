/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Franz Kuben, 4BHIF
 */
public class Setup {
    
    private static String databaseURL;

    public static void runSetup() {
        try {
            String s = System.getenv("APPDATA");
            if (s == null) {
                System.exit(1);
            }

            Path appdata = Paths.get(s, "/MMTasking");
            if (!Files.exists(appdata)) {
                Files.createDirectory(appdata);
            }

            Path data = appdata.resolve("data");
            if (!Files.exists(data)) {
                Files.createDirectory(data);
            }

            Path database = data.resolve("MMTasking.accdb");
            databaseURL = database.toString();
            if (!Files.exists(database)) {
                Path p = null;
                try {
                    final URI uri = Setup.class.getResource("/MMTasking.accdb").toURI();
                    p = Paths.get(uri);
                } catch (URISyntaxException use) {
                    System.err.println(use);
                }

                try {
                    Files.copy(p, database);
                } catch (IOException ioe) {
                    System.err.println(ioe);
                }
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }

        // ----------------------
//        String s = System.getenv("APPDATA");
//        File root = new File(s + "\\Fahrzeugverwaltung");
//        if (!root.exists()) {
//            root.mkdir();
//        }
//
//        File data = new File(root, "/data");
//        if (!data.exists()) {
//            data.mkdir();
//        }
//
//        File database = new File(data, "database.accdb");
//        if (!database.exists()) {
//            Path p = null;
//            try {
//                //p = Paths.get(Setup.class.getResource("/database.accdb").toURI());
//                final URI uri = Setup.class.getResource("/database.accdb").toURI();
//                Map<String, String> env = new HashMap<>();
//                env.put("create", "true");
//                /*  try {
//                    FileSystem zipfs = FileSystems.newFileSystem(uri, env);
//                } catch (IOException ex) {
//                    Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
//                } */
//                p = Paths.get(uri);
//            } catch (URISyntaxException ex) {
//                Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            try {
//                Files.copy(p, database.toPath());
//            } catch (IOException ex) {
//                Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//
//        File images = new File(root, "/images");
//        if (!images.exists()) {
//            images.mkdir();
//        }
//
//        File logs = new File(root, "/logs");
//        if (!logs.exists()) {
//            logs.mkdir();
//        }
    }
    
    public static String getDatabaseURL() {
        if (databaseURL == null) {
            throw new IllegalStateException("No database was found!");
        }
        return databaseURL;
    }

}
