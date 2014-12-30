/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alessandro
 */
@Entity
public class UserEvent {
    
        @Id
        @NotNull
        @GeneratedValue(generator="increment")
        private int idUserEvent;
    
        @ManyToOne(targetEntity = Event.class, optional = false , cascade={CascadeType.MERGE},fetch=FetchType.LAZY)
        private Event event;
        
        @ManyToOne(targetEntity = User.class, optional = false , cascade={CascadeType.MERGE},fetch=FetchType.LAZY)
        private User user;
        
        public boolean creator;
        
        public boolean accepted;
        
        public boolean view;

    public boolean isCreator() {
        return creator;
    }

    public void setCreator(boolean creator) {
        this.creator = creator;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }
        
        public UserEvent() {};
        
        public UserEvent(Event event, User user, boolean creator){
            this.event=event;
            this.user=user;
            this.creator=creator;
        }
    
}
