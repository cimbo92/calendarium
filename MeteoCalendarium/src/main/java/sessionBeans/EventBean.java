/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import HelpClasses.OverlappingException;
import entities.Event;
import entities.MainCondition;
import entities.Preference;
import entities.User;
import entities.UserEvent;
import entities.iDEvent;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import managerBeans.EventManagerInterface;
import managerBeans.IDEventManagerInterface;
import managerBeans.MailSenderManagerInterface;
import managerBeans.OwmClientInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author home
 */
@Named
@RequestScoped
public class EventBean implements Serializable {


    @EJB
    private PreferenceManagerInterface pm;    
    @EJB
    private EventManagerInterface em;
    @EJB
    private UserEventManagerInterface uem;
    @EJB
    private UserManagerInterface um;
    @EJB
    private IDEventManagerInterface idm;
    @EJB
    private MailSenderManagerInterface mailSender;
    @EJB
    private OwmClientInterface forecast;

    
    
    
    private List<String> selectedPref = new ArrayList<>();
    private List<Preference> preferences = new ArrayList<>();
    private List<String> invitated = new ArrayList<>();
    private Event event;

    
    private Date startDate = new Date();
    private Date endDate = new Date();
    private String outdoor;
    private UserEvent userEvent;


    
    public String getOutdoor() {
        return outdoor;
    }

    public void setOutdoor(String outdoor) {
        this.outdoor = outdoor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public EventBean(){}
    
    
    public Event getEvent(){
         if (event == null) {
            event = new Event();      
        }

        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    
    public void addEvent() throws OverlappingException {
        
            event.convertStartDate(startDate);
            event.convertEndDate(endDate);
            boolean inout;
            if(outdoor.equalsIgnoreCase("indoor"))
            {
                inout=false;
            } else {
                inout=true;
            }
            event.setOutdoor(inout);
            long id;
            id=idm.findMax();
            iDEvent idEv = new iDEvent();
            idEv.setId(id);
            event.setIdEvent(idEv);
            event.setCreator(um.getLoggedUser());
            em.addEvent(event);
       
    }
    
    public void create(){  
        FacesContext context = FacesContext.getCurrentInstance();
        
        try{
            this.addEvent();
            this.save();
            this.addUserEvent();
             context.addMessage(null, new FacesMessage("Successful","Event Created") );
       }catch(OverlappingException e)
        {
                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", e.getMessage()));
        }
    }

    public void addUserEvent(){
        
        userEvent=new UserEvent(event, um.getLoggedUser(), true);
        uem.addUserEvent(userEvent);
        for(int i=0;i<invitated.size();i++){
            userEvent=new UserEvent(event, um.findByMail(invitated.get(i)) , false);
               uem.addUserEvent(userEvent);
               mailSender.sendMail(invitated.get(i),"Invitation",userEvent.getEvent().toString());
        }
    }
      

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }
    
    
    public List<String> getSelectedPref() {
        return selectedPref;
    }

    public void setSelectedPref(List<String> selectedPref) {
        this.selectedPref = selectedPref;
    }

    public void save()
    { 
        for(int i=0;i<selectedPref.size();i++)
        {
            preferences.add(new Preference(event,selectedPref.get(i)));
            pm.addPreference(preferences.get(i));
        }
    }
    
    public List<String> listPref ()
    {
        return MainCondition.getListPref();
    }
    
    public List<String> listUser()
    {
        return um.getListUsers();
    }
    
    public List<String> getInvitated() {
        return invitated;
    }

    public void setInvitated(List<String> invitated) {
        this.invitated = invitated;
    }

    
     public void onEventSelect(SelectEvent selectEvent) {
          DefaultScheduleEvent selectedEvent = (DefaultScheduleEvent) selectEvent.getObject(); 
          event=em.loadSpecificEvent(selectedEvent.getDescription());
         List<String> preferenzeEvento = pm.getPreferenceOfEvent(event);
              
        for (String preferenza : preferenzeEvento) {
            this.selectedPref.add(preferenza);
        }
         List<String> invitedUsers = uem.invitedUsersOfEvent(event);
        for (String invitedUser : invitedUsers) {
            this.invitated.add(invitedUser);
        }
    
     }
     
       public void onDateSelect(SelectEvent selectEvent) {
           
     DefaultScheduleEvent selectedEvent = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    
     this.setStartDate(selectedEvent.getStartDate());
     this.setEndDate(selectedEvent.getStartDate());
       }
    
     
     
}
