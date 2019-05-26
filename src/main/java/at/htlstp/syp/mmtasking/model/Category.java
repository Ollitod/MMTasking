/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import at.htlstp.syp.mmtasking.db.MMTDAO;
import java.io.Serializable;
import javax.persistence.Basic;
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
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "cat_id")
    private Short catId;
    
    @Column(name = "cat_bez")
    private String catBez;

    public Category() {
    }

    public Category(Short catId) {
        this.catId = catId;
    }
    
    public Category(String bezeichnung) {
        this.catId = 6;
        this.catBez = bezeichnung;
    }

    public Short getCatId() {
        return catId;
    }

    public void setCatId(Short catId) {
        this.catId = catId;
    }

    public String getCatBez() {
        return catBez;
    }

    public void setCatBez(String catBez) {
        this.catBez = catBez;
    }
    
    public static Category getCategory(String category) {
        MMTDAO dao = MMTDAO.getInstance();
        return MMTDAO.getInstance().findCategoryByName(category);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catId != null ? catId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.catId == null && other.catId != null) || (this.catId != null && !this.catId.equals(other.catId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return catBez;
    }
}