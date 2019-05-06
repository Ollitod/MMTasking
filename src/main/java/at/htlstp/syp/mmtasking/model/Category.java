/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class Category implements Comparable<Category> {
    
    private final String name;
    
    private static final Set<Category> CATEGORIES = new TreeSet<>();
    
    public Category(String name) {
        this.name = name;
        CATEGORIES.add(this);
    }

    public String getName() {
        return name;
    }

    public static Set<Category> getCategories() {
        return CATEGORIES;
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }
}
