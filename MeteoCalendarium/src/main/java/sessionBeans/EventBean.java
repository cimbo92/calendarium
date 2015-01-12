/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import HelpClasses.InvalidDateException;
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
import javax.ejb.SessionBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import managerBeans.EventManagerInterface;
import managerBeans.IDEventManagerInterface;
import managerBeans.MailSenderManagerInterface;
import managerBeans.OwmClientInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
/**
 *
 * @author home
 */
@SessionScoped
@Named
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
    private long savedId;
    private boolean creating;
    private boolean canEliminate;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private boolean canSelectPreferences;

    public boolean isCanSelectPreferences() {
        return this.event.isOutdoor()&this.isOwnEvent;
    }

    public void setCanSelectPreferences(boolean canSelectPreferences) {
        this.canSelectPreferences = canSelectPreferences;
    }
    
    private UserEvent userEvent;


       private boolean isOwnEvent;

    public boolean isIsOwnEvent() {
        return isOwnEvent;
    }

    public void setIsOwnEvent(boolean isOwnEvent) {
        this.isOwnEvent = isOwnEvent;
    }
    
        public long getSavedId() {
        return savedId;
    }

    public void setSavedId(long savedId) {
        this.savedId = savedId;
    }
    
    public boolean isCanEliminate() {
        return this.isOwnEvent&!this.creating;
    }

    public void setCanEliminate(boolean canEliminate) {
        this.canEliminate = canEliminate;
    }
           
    
    public boolean isCreating() {
        return creating;
    }

    public void setCreating(boolean creating) {
        this.creating = creating;
    }
    
    public boolean canEliminate()
    {
        return this.isOwnEvent&!this.creating;
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
    
    
    public void addEvent() throws OverlappingException, InvalidDateException{
         FacesContext context = FacesContext.getCurrentInstance();
       
            event.convertStartDate(startDate);
            event.convertEndDate(endDate);
            if(event.getStartDate().before(event.getEndDate()) || event.getStartDate().equals(event.getEndDate()) )
            {
                
            long id;
            id=idm.findMax();
            iDEvent idEv = new iDEvent();
            idEv.setId(id);
            event.setIdEvent(idEv);
            event.setCreator(um.getLoggedUser());
            em.addEvent(event,um.getLoggedUser());
       
            }
            else
            {
            throw new InvalidDateException();
            }
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
        catch(InvalidDateException e)
        {
           context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!","Are you Serious!? , Start Date after End Date"));
        }
        
         event=new Event();
    }
    
    
    public void modify(){
     FacesContext context = FacesContext.getCurrentInstance();
        try{
            this.updateEvent(); 
       }catch(OverlappingException e)
        {
                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", e.getMessage()));
        }   
       RequestContext request=  RequestContext.getCurrentInstance();
      request.update("formcentral:schedule");
         event=new Event();
    }

    public void cancel(){
        FacesContext context = FacesContext.getCurrentInstance();
     
         em.removeEvent(event);
          RequestContext request=  RequestContext.getCurrentInstance();
          
      request.update("formcentral:schedule");
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!","Event delete completed"));
    }
    

    public void updateEvent() throws OverlappingException{
        em.removeEvent(event);
        this.create();
        }
    public void addUserEvent(){
        
        userEvent=new UserEvent(event, um.getLoggedUser(), true);
        uem.addUserEvent(userEvent);
        for (String invitated1 : invitated) {
            userEvent = new UserEvent(event, um.findByMail(invitated1), false);
            uem.addUserEvent(userEvent);
            mailSender.sendMail(invitated1, "Invitation", userEvent.getEvent().toString());
        }
    }

    public void modifyFromWarning(Event event, List<String> pref, List<String> userEvent) throws OverlappingException
    {
        this.event=event;
        this.invitated = userEvent;
        this.selectedPref=pref;
        preferences = new ArrayList<>();
        em.addEvent(event,um.getLoggedUser());
        this.save();
        this.addUserEvent();
        this.event = new Event();
        invitated = new ArrayList<>();
        selectedPref  = new ArrayList<>();
        preferences = new ArrayList<>();
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
        if(selectedPref.isEmpty())
        {
         for(int i=0;i<this.listPref().size();i++)
        {
           
             pm.addPreference(new Preference(event,listPref().get(i)));
        }  
        }
        
        preferences = new ArrayList<>();
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
           creating= false;
          this.isOwnEvent = selectedEvent.getStyleClass().equals("emp1");
         event.getIdEvent().setId(Long.parseLong(selectedEvent.getDescription()));
           creating= false;
          this.startDate=selectedEvent.getStartDate();
                  this.endDate=selectedEvent.getEndDate();
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
         event = new Event();
         selectedPref = new ArrayList<>();
        invitated = new ArrayList<>();
        preferences= new ArrayList<>();
        creating= true;
        isOwnEvent=true;
     Date correction;
     correction = new Date(selectedEvent.getStartDate().getTime() + (60 * 60000));
     this.setStartDate(correction);
     this.setEndDate(correction);
       }
    
     
     
}