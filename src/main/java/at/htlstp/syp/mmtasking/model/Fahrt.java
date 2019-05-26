/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author 20150202
 */

@Entity
@Table(name = "fahrt")
public class Fahrt implements Serializable {

    @Id
    @Column(name = "fahrt_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fahrt_loc_von_id", nullable = false)
    private Location von;

    @ManyToOne
    @JoinColumn(name = "fahrt_loc_nach_id", nullable = false)
    private Location nach;

    @Column(name = "fahrt_zeit")
    private Integer fahrzeit;

    public Fahrt() {
        
    }
    
    public Fahrt(Location von, Location bis, Integer fahrzeit) {
        this.von = von;
        this.nach = bis;
        this.fahrzeit = fahrzeit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getVon() {
        return von;
    }

    public void setVon(Location von) {
        this.von = von;
    }

    public Location getNach() {
        return nach;
    }

    public void setNach(Location nach) {
        this.nach = nach;
    }

    public Integer getFahrzeit() {
        return fahrzeit;
    }

    public void setFahrzeit(Integer fahrzeit) {
        this.fahrzeit = fahrzeit;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fahrt other = (Fahrt) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fahrt von " + von.getName() + " nach " + nach.getName();
    }
}
