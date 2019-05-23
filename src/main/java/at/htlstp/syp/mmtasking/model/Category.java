/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @GeneratedValue(generator = "cat_seq")
    private Integer id;
    
    @Column(name = "cat_bez")
    private String name;
    
    public Category() {
        
    }
    
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        //return "Category{" + "id=" + id + ", name=" + name + '}';
        return name;
    }
    
    
}
