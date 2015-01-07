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
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import managerBeans.EventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
 
@ViewScoped
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
     
    public void loadCalendar(){
       List<Event> tempCalendar = em.loadCalendar(um.getLoggedUser());
        for (Event tempCalendar1 : tempCalendar) {
           DefaultScheduleEvent temp = new DefaultScheduleEvent(tempCalendar1.getTitle(), tempCalendar1.getStartDate(), tempCalendar1.getEndDate());
           temp.setDescription(tempCalendar1.getIdEvent().getId().toString());
           if(!eventModel.getEvents().contains(temp)) 
           {
               eventModel.addEvent(temp);
           }
        }       
    }	
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
      
    
    public void onEventSelect(SelectEvent selectEvent) {
      
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
     DefaultScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
   
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

