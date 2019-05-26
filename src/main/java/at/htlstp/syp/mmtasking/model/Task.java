/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlstp.syp.mmtasking.model;

import at.htlstp.syp.mmtasking.db.LocalDateTimeAttributeConverter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {

    @Id
    @Column(name = "task_id")
    private Integer id;

    @Column(name = "task_title")
    private String title;

    @Column(name = "task_beginning")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime beginning;

    @Column(name = "task_end")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime end;
    
    @ManyToOne
    @JoinColumn(name = "task_fahrt_id", nullable = false)
    private Fahrt fahrt;

    @Column(name = "task_category")
    private String category;

    @Column(name = "task_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "task_note")
    private String note;

    @Column(name = "task_deletable")
    private boolean deletable;

    @Column(name = "task_finalized")
    private boolean finalized;
    
    @Column(name = "task_time")
    private long time;

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Task() {

    }

    public Task(String title, LocalDateTime beginning, LocalDateTime end, Fahrt fahrt, String category, TaskPriority priority, String note, boolean deletable, boolean finalized) {
        this.title = title;
        this.beginning = beginning;
        this.end = end;
        this.fahrt = fahrt;
        this.category = category;
        this.priority = priority;
        this.note = note;
        this.deletable = deletable;
        this.finalized = finalized;
        this.time = ChronoUnit.MINUTES.between(beginning, end);
    }

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

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public void setBeginning(LocalDateTime beginning) {
        this.beginning = beginning;
        updateTime();
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
        updateTime();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void finalizeTask() {
        this.finalized = true;
    }
    
    public long getTime() {
        return time;
    }
    
    private void updateTime() {
        this.time = ChronoUnit.MINUTES.between(beginning, end);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Task other = (Task) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s von %s bis %s; Priorität: %s - %s", title, beginning.format(dtf), end.format(dtf), priority, (finalized ? "finalisiert" : "offen"));
    }
}
