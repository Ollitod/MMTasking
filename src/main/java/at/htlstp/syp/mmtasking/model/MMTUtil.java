/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class MMTUtil {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private MMTUtil() {
        
    }
    
    public static Task createTaskFromCSV(String line) {
        String[] token = line.split(";");
        
        if (token.length != 8) {
            // exception
        }
        
        String title = token[0];
        LocalDateTime beginning = LocalDateTime.parse(token[1], FORMATTER);
        LocalDateTime end = LocalDateTime.parse(token[2], FORMATTER);
        Location location = new Location(token[3]);
        String category = token[4];
        TaskPriority priority = TaskPriority.valueOf(token[5]);
        String note = token[6];
        boolean deletable = Boolean.parseBoolean(token[7]);
        boolean finalized = Boolean.parseBoolean(token[8]);
        
        return new Task(title, beginning, end, location, category, priority, note, deletable, finalized);
    }
    
    public static Appointment createAppointmentFromCSV(String line) {
        String[] token = line.split(";");
        
        if (token.length != 4) {
            // exception
        }
        
        String title = token[0];
        Location location = new Location(token[1]);
        LocalDateTime date = LocalDateTime.parse(token[2], FORMATTER);
        String note = token[3];
        
        return new Appointment(title, location, date, note);
    }

    public static DateTimeFormatter getDTF() {
        return FORMATTER;
    }
    
    
}
