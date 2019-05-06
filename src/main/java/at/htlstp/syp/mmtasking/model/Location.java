/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
@Embeddable
public class Location implements Serializable {

    @Column(name = "loc_name")
    private String name;

    public Location() {
    }
    
    public Location(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" + "name=" + name + '}';
    }
    
    
}
