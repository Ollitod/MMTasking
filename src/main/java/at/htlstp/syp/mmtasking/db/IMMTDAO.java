/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.db;

import at.htlstp.syp.mmtasking.model.Appointment;
import at.htlstp.syp.mmtasking.model.Task;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author 20150202
 */
public interface IMMTDAO {

  /**
   * Liefert alle Tasks aus der DB als typsichere Liste
   * aufsteigend nach ihren IDs sortiert
   * @return Liste aller Tasks, leere Liste wenn keine Tasks existieren
   * @throws MMTDBException bei einem DB Fehler
   */
  List<Task> getAllTasks() throws MMTDBException;

    /**
   * Liefert Tasks die in dem übergebenen Ort statt finden aus der DB als typsichere Liste
   * aufsteigend nach ihren IDs sortiert
   * @param ort der Ort nach dem gefiltert werden soll
   * @return Liste der gefilterten Tasks, leere Liste wenn keine Tasks existieren
   * @throws MMTDBException bei einem DB Fehler
   */
  List<Task> getTasksByLocation(String location) throws MMTDBException;
  
  
      /**
   * Liefert Tasks die in einem bestimmten Zeitraum statt finden aus der DB als typsichere Liste
   * aufsteigend nach ihren Datum sortiert sortiert
   * @param start Der beginn Zeitpunkt für die Filterung
   * @param end Der end Zeitpunkt für die Filterung
   * @return Liste der gefilterten Tasks, leere Liste wenn keine Tasks existieren
   * @throws MMTDBException bei einem DB Fehler
   */
  List<Task> getTasksByPeriod (LocalDateTime start, LocalDateTime end) throws MMTDBException;
  
  
      /**
   * Liefert Tasks die aus der übergebenen Kategorie sind aus der DB als typsichere Liste
   * aufsteigend nach ihren IDs sortiert
   * @param category die Category in der die Tasks enthalten sein sollen
   * @return Liste der gefilterten Tasks, leere Liste wenn keine Tasks existieren
   * @throws MMTDBException bei einem DB Fehler
   */
  List<Task> getTasksByCategory(String category) throws MMTDBException;
  
  /**
   * Speichert einen neuen Task in die DB.
   * Wenn der übergebene Task bereits eine ID ungleich
   * null besitzt, wird eine IllegalArgumentException geworfen
   * @param t ein neuer Task
   * @return true bei Erfolg, false bei Misserfolg
   * @throws MMTDBException bei einem DB Fehler
   * @throws IllegalArgumentException vgl. oben
   */
  boolean insertTask(Task t) throws MMTDBException;

   /**
   * Passt den Task von der DB an den tatsächlichen Task an
   * @param t ein neuer Task
   * @return true bei Erfolg, false bei Misserfolg
   * @throws MMTDBException bei einem DB Fehler
   */
  boolean updateTask(Task t) throws MMTDBException;
  
     /**
   * Löscht den übergebenen Task von der DB
   * @param t ein neuer Task
   * @return true bei Erfolg, false bei Misserfolg
   * @throws MMTDBException bei einem DB Fehler
   */
  boolean deleteTask(Task t) throws MMTDBException;
  
    /**
   * Liefert alle Appointment aus der DB als typsichere Liste
   * aufsteigend nach ihren IDs sortiert
   * @return Liste aller Appointment, leere Liste wenn keine Appointment existieren
   * @throws MMTDBException bei einem DB Fehler
   */
  List<Appointment> getAllAppointments() throws MMTDBException;


  /**
   * Speichert einen neuen Appointment in die DB.
   * Wenn der übergebene Task bereits eine ID ungleich
   * null besitzt, wird eine IllegalArgumentException geworfen
   * @param t ein neuer Appointment
   * @return true bei Erfolg, false bei Misserfolg
   * @throws MMTDBException bei einem DB Fehler
   * @throws IllegalArgumentException vgl. oben
   */
  boolean insertAppointment(Appointment t) throws MMTDBException;
  
  
   /**
   * Passt den Appointment von der DB an den tatsächlichen Task an
   * @param t ein neuer Task
   * @return true bei Erfolg, false bei Misserfolg
   * @throws MMTDBException bei einem DB Fehler
   */
  boolean updateAppointment(Appointment t) throws MMTDBException;
  
      /**
   * Löscht das übergebene Appointment von der DB
   * @param t ein neuer Appointment
   * @return true bei Erfolg, false bei Misserfolg
   * @throws MMTDBException bei einem DB Fehler
   */
  boolean deleteAppointment(Appointment t) throws MMTDBException;
  
  
  
}
