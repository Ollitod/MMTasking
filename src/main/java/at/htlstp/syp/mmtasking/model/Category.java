/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import at.htlstp.syp.mmtasking.controller.MainAppController;
import at.htlstp.syp.mmtasking.db.MMTDAO;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
@Entity
@Table(name = "category")
public class Category implements Serializable, Comparable<Category> {
    
    @Id
    @Column(name = "cat_id")
    private Integer id;
    
    @Column(name = "cat_bez")
    private String name;
    
    
    public Category() {   
    }
    
    public Category(String name) {
        // Abfrage auf letze ID
        this.name = name;
    }
    
    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public static Category getCategory(String category) {
        return MMTDAO.getInstance().getAllCategories()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(category))
                .findFirst()
                .get();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.name);
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
        final Category other = (Category) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
