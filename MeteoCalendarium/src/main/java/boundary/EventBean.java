/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package boundary;

import HelpClasses.EventCreation;
import HelpClasses.InvalidDateException;
import HelpClasses.OverlappingException;
import entity.Event;
import entity.IDEvent;
import entity.MainCondition;
import entity.Preference;
import entity.UserEvent;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import control.EventManagerInterface;
import control.IDEventManagerInterface;
import control.MailSenderManagerInterface;
import control.PreferenceManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Alessandro De Angelis
 */
@SessionScoped
@Named
public class EventBean implements Serializable {

    /*
    * ******************************************************************
    * EJB MANAGERS
    * ******************************************************************
    */
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

    FacesContext c;

    RequestContext r;

    /*
    *******************************************************************
    * FIELDS
    *******************************************************************
    */
    /**
     * list of preferences selected by creator user
     */
    private List<String> selectedPreferences = new ArrayList<>();
    /**
     * list of preferences to save in database
     */
    private List<Preference> toSavePreferences = new ArrayList<>();

    /**
     * List of users selected by creator user
     */
    private List<String> selectedUsers = new ArrayList<>();
    private UserEvent userEvent;
    private EventCreation beanEvent = new EventCreation();

    //booleans used to enable/disable buttons in dialog
    private boolean isOwnEvent;
    private boolean creating;
    private boolean required=true;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    //Utility Date to convert timestamps
    private Date startDate = new Date();
    private Date endDate = new Date();

    private EventCreation tempEvent;


    //context used for messges
    /*
    *******************************************************************
    PUBLIC FUNCTIONS
    *******************************************************************
    */
    /**
     * Function that create An Event ( and all consequent invitations and
     * preferences ) in database using fields inserted by user
     */
    public void create() {
           FacesContext context = FacesContext.getCurrentInstance();
           if(context == null){
               context = c;
           }
        try {
            this.addEvent();
            this.savePreferences();
            this.addUserEvent();
            context.addMessage(null, new FacesMessage("Successful", "Event Created"));
        } catch (OverlappingException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", e.getMessage()));
        } catch (InvalidDateException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", "Are you Serious!? , Date Not Correct"));
        }
        beanEvent = new EventCreation();

    }

    public void unload(){
        this.required=false;
    }

    public void reCreate() throws OverlappingException, InvalidDateException {
        FacesContext context = FacesContext.getCurrentInstance();

        this.addEvent();
        this.savePreferences();
        this.addUserEvent();
        context.addMessage(null, new FacesMessage("Successful", "Event Recreated"));

        beanEvent = new EventCreation();
    }

    public void createFromModify() throws OverlappingException, InvalidDateException {
        FacesContext context = FacesContext.getCurrentInstance();
        if(context == null){
            context = c;
        }

        this.addEvent();
        this.savePreferences();
        this.addUserEvent();
        context.addMessage(null, new FacesMessage("Successful", "Event Modifed"));

        beanEvent = new EventCreation();
    }

    /**
     * Function that modify ( and all consequent invitations and preferences )
     * the event selected in database using fields modified by user
     */
    public void modify() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(context == null){
            context = c;
        }
        try {
            this.updateEvent();
        } catch (OverlappingException | InvalidDateException e) {
            beanEvent = tempEvent;
            this.setStartDate(tempEvent.getStartDate());
            this.setEndDate(tempEvent.getEndDate());
            try {
                this.reCreate();
            } catch (OverlappingException ex) {
                Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidDateException ex) {
                Logger.getLogger(EventBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", e.getMessage()));
        }
        RequestContext request = RequestContext.getCurrentInstance();
        if(request == null){
            request = r;
        }
        request.update("formcentral:schedule");
        this.resetBean();

    }


    /**
     * Function that cancel ( and all consequent invitations and preferences )
     * the event selected in database using fields modified by user
     */
    public void cancel() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(context == null){
            context = c;
        }
        Event event = new Event();
        event.loadEvent(beanEvent);
        em.removeEvent(event);
        RequestContext request = RequestContext.getCurrentInstance();
        if(request == null){
            request = r;
        }
        request.update("formcentral:schedule");
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Event delete completed"));
    }


    /**
     * After accepting an invitation , The function revert decision of the user
     * for the selected event
     *
     * @return String for redirecting page
     */
    public String decline() {
        Event event = new Event();
        event.loadEvent(beanEvent);

        UserEvent ue = uem.getUserEventofUser(event, um.getLoggedUser());
        uem.modifyUserEvent(ue, false, false);
        return "calendar?faces-redirect=true";
    }

    /**
     * Function that called by the warning dialog change the Event date in case
     * of warning for weather condition
     *
     * @param event the @Event to change
     * @param pref @Event preferences
     * @param userEvent @Event invitations
     * @throws OverlappingException
     * @throws InvalidDateException
     */
    public void modifyFromWarning(Event event, List<String> pref, List<String> userEvent) throws OverlappingException, InvalidDateException {
        em.removeEventByID(event);
        this.resetBean();
        this.selectedUsers = userEvent;
        this.selectedPreferences = pref;
        this.beanEvent.loadEvent(event);
        this.startDate=beanEvent.getStartDate();
        this.endDate=beanEvent.getStartDate();
        this.addEvent();
        this.savePreferences();
        this.addUserEvent();
        this.resetBean();
    }

    /**
     * Function called by PrimeFace's schedule in caso of selection of event
     * that load informations about the selected @Event
     *
     * @param selectEvent
     */
    public void onEventSelect(SelectEvent selectEvent) {

        this.resetBean();
        DefaultScheduleEvent selectedEvent = (DefaultScheduleEvent) selectEvent.getObject();
        Event event = new Event();
        event = em.loadSpecificEvent(selectedEvent.getDescription());
        beanEvent.loadEvent(event);
        this.isOwnEvent = em.isCreator(event, um.getLoggedUser());
        this.creating=false;
        beanEvent.getIdEvent().setId(Long.parseLong(selectedEvent.getDescription()));
        this.startDate = selectedEvent.getStartDate();
        this.endDate = selectedEvent.getEndDate();
        this.loadInvitations();
        this.loadPreferences();
    }

    /**
     * Function called by PrimeFace's schedule in caso of selection of a day
     * that the day Date
     *
     * @param selectEvent
     */
    public void onDateSelect(SelectEvent selectEvent) {

        DefaultScheduleEvent selectedEvent = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        this.resetBean();
        creating = true;
        isOwnEvent = true;
        Date correction;
        correction = new Date(selectedEvent.getStartDate().getTime() + (60 * 60000));
        this.setStartDate(correction);
        this.setEndDate(correction);
    }

    /**
     * main Conditions to load in Event Dialog
     *
     * @return List of Main Condition
     */
    public List<String> listPref() {
        return MainCondition.getListPref();
    }

    /**
     * Username of user to load in Event Dialog
     *
     * @return List of Users
     */
    public List<String> listUser() {
        return um.getListUsers();
    }

    /*
    * ******************************************************************
    PRIVATE UTILITY FUNCTIONS
    *******************************************************************
    */
    public void resetBean() {
        beanEvent = new EventCreation();
        selectedPreferences = new ArrayList<>();
        selectedUsers = new ArrayList<>();
        toSavePreferences = new ArrayList<>();
        tempEvent = new EventCreation();
        required=true;
    }

    private void loadPreferences() {
        Event event = new Event();
        event.loadEvent(beanEvent);
        List<String> preferenzeEvento = pm.getPreferenceOfEvent(event);
        for (String preferenza : preferenzeEvento) {
            this.selectedPreferences.add(preferenza);
        }
    }

    private void loadInvitations() {
        Event event = new Event();
        event.loadEvent(beanEvent);
        List<String> invitedUsers = uem.invitedUsersOfEvent(event);
        for (String invitedUser : invitedUsers) {
            this.selectedUsers.add(invitedUser);
        }
    }

    private boolean controlDate() {
        Timestamp now = new Timestamp(new java.util.Date().getTime());
        this.beanEvent.convertStartDate(startDate);
        this.beanEvent.convertEndDate(endDate);
        boolean correct;
        //date must be after today and start must be before end date
        if (beanEvent.getStartDate().after(beanEvent.getEndDate()) || beanEvent.getStartDate().before(now)) {
            correct = false;
        } else {
            correct= true;
        }
        return correct;
    }



    private void addEvent() throws OverlappingException, InvalidDateException {

        if (this.controlDate()) {
            IDEvent idEv = new IDEvent(idm.findFirstFreeID());
            beanEvent.setIdEvent(idEv);
            beanEvent.setCreator(um.getLoggedUser());
            Event event = new Event();

                event.loadEvent(beanEvent);

            em.addEvent(event, um.getLoggedUser());
        } else {
            throw new InvalidDateException();
        }
    }

    private void savePreferences() {
        for (int i = 0; i < selectedPreferences.size(); i++) {
            Event event = new Event();
            event.loadEvent(beanEvent);
            toSavePreferences.add(new Preference(event, selectedPreferences.get(i)));
            pm.addPreference(toSavePreferences.get(i));
        }
        if (selectedPreferences.isEmpty()) {
            for (int i = 0; i < this.listPref().size(); i++) {
                Event event = new Event();
                event.loadEvent(beanEvent);
                pm.addPreference(new Preference(event, listPref().get(i)));
            }
        }
        toSavePreferences = new ArrayList<>();
    }

    public boolean isCanDecline() {
        return !this.isOwnEvent & !this.creating;
    }

    private void updateEvent() throws OverlappingException, InvalidDateException {
        Event oldEvent = em.loadSpecificEvent(beanEvent.getIdEvent().getId().toString());
        tempEvent.loadEvent(oldEvent);
        Event event = new Event();
        event.loadEvent(beanEvent);
        em.removeEvent(event);
        this.createFromModify();
    }

    private void addUserEvent() {
        Event event = new Event();
        event.loadEvent(beanEvent);
        userEvent = new UserEvent(event, um.getLoggedUser(), true);
        uem.addUserEvent(userEvent);
        for (String invitated1 : selectedUsers) {
            userEvent = new UserEvent(event, um.findByMail(invitated1), false);
            uem.addUserEvent(userEvent);
            mailSender.sendMail(invitated1, "Invitation", userEvent.getEvent().toString());
        }
    }

    /*
    * ******************************************************************
    * GETTERS AND SETTERS
    *******************************************************************
    */
    public boolean isCanSelectPreferences() {
        return this.beanEvent.isOutdoor() & this.isOwnEvent;
    }

    public boolean isIsOwnEvent() {
        return isOwnEvent;
    }

    public boolean canEliminate() {
        return this.isOwnEvent & !this.creating;
    }

    public void setIsOwnEvent(boolean isOwnEvent) {
        this.isOwnEvent = isOwnEvent;
    }

    public boolean isCanEliminate() {
        return this.isOwnEvent & !this.creating;
    }

    public boolean isCreating() {
        return creating;
    }

    public void setCreating(boolean creating) {
        this.creating = creating;
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

    public EventCreation getBeanEvent() {
        if (beanEvent == null) {
            beanEvent = new EventCreation();
        }
        return beanEvent;
    }

    public void setBeanEvent(EventCreation beanEvent) {
        this.beanEvent = beanEvent;
    }

    public List<String> getToInviteUsers() {
        return selectedUsers;
    }

    public void setToInviteUsers(List<String> toInviteUsers) {
        this.selectedUsers = toInviteUsers;
    }

    public List<Preference> getToSavePreferences() {
        return toSavePreferences;
    }

    public void setToSavePreferences(List<Preference> toSavePreferences) {
        this.toSavePreferences = toSavePreferences;
    }

    public List<String> getSelectedPreferences() {
        return selectedPreferences;
    }

    public void setSelectedPreferences(List<String> selectedPreferences) {
        this.selectedPreferences = selectedPreferences;
    }

    public PreferenceManagerInterface getPm() {
        return pm;
    }

    public void setPm(PreferenceManagerInterface pm) {
        this.pm = pm;
    }

    public EventManagerInterface getEm() {
        return em;
    }

    public void setEm(EventManagerInterface em) {
        this.em = em;
    }

    public UserEventManagerInterface getUem() {
        return uem;
    }

    public void setUem(UserEventManagerInterface uem) {
        this.uem = uem;
    }

    public UserManagerInterface getUm() {
        return um;
    }

    public void setUm(UserManagerInterface um) {
        this.um = um;
    }

    public IDEventManagerInterface getIdm() {
        return idm;
    }

    public void setIdm(IDEventManagerInterface idm) {
        this.idm = idm;
    }

    public MailSenderManagerInterface getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSenderManagerInterface mailSender) {
        this.mailSender = mailSender;
    }

    public List<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public UserEvent getUserEvent() {
        return userEvent;
    }

    public void setUserEvent(UserEvent userEvent) {
        this.userEvent = userEvent;
    }

    public EventCreation getTempEvent() {
        return tempEvent;
    }

    public void setTempEvent(EventCreation tempEvent) {
        this.tempEvent = tempEvent;
    }

    public FacesContext getContext() {
        return c;
    }

    public void setContext(FacesContext context) {
        this.c = context;
    }



}
