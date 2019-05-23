/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
@Entity
@Table(name = "location")
public class Location implements Serializable {

    
    @Id
    @Column(name = "loc_id")
    private Integer id;
    
    @Column(name = "loc_name")
    private String name;

    public Location() {
    }
    
    public Location(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        //return "Location{" + "name=" + name + '}';
        return name;
    }
    
    
}
