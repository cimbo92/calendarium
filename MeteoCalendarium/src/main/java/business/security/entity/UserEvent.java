package business.security.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Entity for invitations
 *
 * @author alessandro
 */
@Entity
public class UserEvent implements Serializable {

    /*
     *******************************************************************
     * FIELDS
     *******************************************************************
     */
    @Id
    @NotNull
    @GeneratedValue(generator = "increment")
    private int idUserEvent;

    @ManyToOne(targetEntity = Event.class, optional = false, fetch = FetchType.EAGER)
    private Event event;

    @ManyToOne(targetEntity = Users.class, optional = false, fetch = FetchType.EAGER)
    private Users user;

    private boolean creator;

    private boolean accepted;

    private boolean viewed;

    /*
     *******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    public UserEvent() {
    }

    public UserEvent(Event event, Users user, boolean creator) {
        this.event = event;
        this.user = user;
        this.creator = creator;
    }

    /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
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

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public int getIdUserEvent() {
        return idUserEvent;
    }

    public void setIdUserEvent(int idUserEvent) {
        this.idUserEvent = idUserEvent;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
