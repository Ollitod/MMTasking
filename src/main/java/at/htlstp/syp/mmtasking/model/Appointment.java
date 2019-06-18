/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {

    @Id
    @Column(name = "app_id")
    private Integer id;
    
    @Column(name = "app_title")
    private String title;
    
    @JoinColumn(name = "app_loc_id", referencedColumnName = "loc_id")
    private Location location;
    
    @Column(name = "app_date")
    private LocalDateTime date;
    
    @Column(name = "app_note")
    private String note;

    public Appointment() {
    }

    public Appointment(String title, Location location, LocalDateTime date, String note) {
        this.title = title;
//        this.location = location;
        this.date = date;
        this.note = note;
    }
    
    

//    @Override
//    public String toString() {
//        return "Appointment{" + "title=" + title + ", location=" + location + ", date=" + date + ", note=" + note + '}';
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    
}
