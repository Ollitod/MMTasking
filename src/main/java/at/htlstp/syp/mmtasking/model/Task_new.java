/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.htlstp.syp.mmtasking.model;

import java.time.LocalDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Oliver Tod / 4BHIF
 */
public class Task_new {

    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper(this, "id");

    public int getId() {
        return id.get();
    }

    public ReadOnlyIntegerProperty idProperty() {
        return id;
    }
    
    private final StringProperty title = new SimpleStringProperty(this, "title");

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }
    
    private final ObjectProperty<LocalDateTime> beginning = new SimpleObjectProperty<>(this, "beginning");

    public LocalDateTime getBeginning() {
        return beginning.get();
    }

    public void setBeginning(LocalDateTime value) {
        beginning.set(value);
    }

    public ObjectProperty beginningProperty() {
        return beginning;
    }

    private final ObjectProperty<LocalDateTime> end = new SimpleObjectProperty<>(this, "end");

    public LocalDateTime getEnd() {
        return end.get();
    }

    public void setEnd(LocalDateTime value) {
        end.set(value);
    }

    public ObjectProperty endProperty() {
        return end;
    }
    
    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(this, "location");

    public Location getLocation() {
        return location.get();
    }

    public void setLocation(Location value) {
        location.set(value);
    }

    public ObjectProperty locationProperty() {
        return location;
    }

    private final StringProperty category = new SimpleStringProperty(this, "category");

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String value) {
        category.set(value);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    private final ObjectProperty<TaskPriority> priority = new SimpleObjectProperty<>(this, "priority");

    public TaskPriority getPriority() {
        return priority.get();
    }

    public void setPriority(TaskPriority value) {
        priority.set(value);
    }

    public ObjectProperty priorityProperty() {
        return priority;
    }

    private final StringProperty note = new SimpleStringProperty(this, "note");

    public String getNote() {
        return note.get();
    }

    public void setNote(String value) {
        note.set(value);
    }

    public StringProperty noteProperty() {
        return note;
    }

    private final BooleanProperty deletable = new SimpleBooleanProperty(this, "deletable");

    public boolean isDeletable() {
        return deletable.get();
    }

    public void setDeletable(boolean value) {
        deletable.set(value);
    }

    public BooleanProperty deletableProperty() {
        return deletable;
    }
    
    private final BooleanProperty finalized = new SimpleBooleanProperty(this, "finalized");

    public boolean isFinalized() {
        return finalized.get();
    }

    public void setFinalized(boolean value) {
        finalized.set(value);
    }

    public BooleanProperty finalizedProperty() {
        return finalized;
    }
}
