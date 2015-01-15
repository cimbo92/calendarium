/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity with ID of Events
 * @author Alessandro
 */
@Entity
public class IDEvent implements Serializable {

     /*
     *******************************************************************
     * FIELDS
     *******************************************************************
     */

    private static final long serialVersionUID = 1L;

    @Id
   private Long id;
    @OneToOne(mappedBy = "idEvent")
    private Event event;

     /*
     *******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */

     public IDEvent(Long id)
    {
        this.id=id;
    }

    public IDEvent(String id)
    {
        this.id=Long.parseLong(id);
    }

        public IDEvent(){
    }

     /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */



    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IDEvent)) {
            return false;
        }
        IDEvent other = (IDEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.iDEvent[ id=" + id + " ]";
    }

}
