/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.model;

/**
 *
 * @author 20150223
 */
public enum TaskPriority {
    
    HIGH("Red"), MEDIUM("Orange"), LOW("Green");
    
    private final String color;
    
    private TaskPriority(String color) {
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
}
