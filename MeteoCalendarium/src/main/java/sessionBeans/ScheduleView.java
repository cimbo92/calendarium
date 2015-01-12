package sessionBeans;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alessandro
 */
import entities.Event;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import managerBeans.EventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ViewScoped
@Stateful
@Named("scheduleView")
public class ScheduleView implements Serializable {

    private ScheduleModel eventModel;

    @EJB
    private EventManagerInterface em;

    @EJB
    UserManagerInterface um;
 
    
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        loadCalendar();
    }

    public void loadCalendar() {
        List<Event> tempCalendar = em.loadCalendar(um.getLoggedUser());
        eventModel = new DefaultScheduleModel();
        for (Event tempCalendar1 : tempCalendar) {
            DefaultScheduleEvent temp;
            if (tempCalendar1.getCreator().getEmail().equals(um.getLoggedUser().getEmail())) {
                temp = new DefaultScheduleEvent(tempCalendar1.getTitle(), tempCalendar1.getStartDate(), tempCalendar1.getEndDate(), "emp1");
            } else {
                temp = new DefaultScheduleEvent(tempCalendar1.getTitle(), tempCalendar1.getStartDate(), tempCalendar1.getEndDate(), "emp2");
            }
            temp.setDescription(tempCalendar1.getIdEvent().getId().toString());
            
            boolean alreadyIn=false;
            for(int i=0; i<eventModel.getEventCount();i++)
            {
                if(eventModel.getEvents().get(i).getDescription().equals(temp.getDescription()))
                {
                    alreadyIn=true;
                }
           
            }
             if (!alreadyIn) {
                eventModel.addEvent(temp);
            } else {
                eventModel.updateEvent(temp);
            }
        }
        
        
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

}
