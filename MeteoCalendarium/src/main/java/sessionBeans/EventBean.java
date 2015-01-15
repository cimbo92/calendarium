/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import HelpClasses.InvalidDateException;
import HelpClasses.OverlappingException;
import entities.Event;
import entities.IDEvent;
import entities.MainCondition;
import entities.Preference;
import entities.UserEvent;
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
import managerBeans.EventManagerInterface;
import managerBeans.IDEventManagerInterface;
import managerBeans.MailSenderManagerInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;
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
    private Event beanEvent;

    //booleans used to enable/disable buttons in dialog
    private boolean isOwnEvent;
    private boolean creating;

    //Utility Date to convert timestamps
    private Date startDate = new Date();
    private Date endDate = new Date();

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
        beanEvent = new Event();
    }

    /**
     * Function that modify ( and all consequent invitations and preferences )
     * the event selected in database using fields modified by user
     */
    public void modify() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            this.updateEvent();
        } catch (OverlappingException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", e.getMessage()));
        }
        RequestContext request = RequestContext.getCurrentInstance();
        request.update("formcentral:schedule");
        this.resetBean();
    }

    /**
     * Function that cancel ( and all consequent invitations and preferences )
     * the event selected in database using fields modified by user
     */
    public void cancel() {
        FacesContext context = FacesContext.getCurrentInstance();

        em.removeEvent(beanEvent);
        RequestContext request = RequestContext.getCurrentInstance();
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
        UserEvent ue = uem.getUserEventofUser(beanEvent, um.getLoggedUser());
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
        this.resetBean();
        this.beanEvent = event;
        this.selectedUsers = userEvent;
        this.selectedPreferences = pref;
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
        beanEvent = em.loadSpecificEvent(selectedEvent.getDescription());
        this.isOwnEvent = em.isCreator(beanEvent, um.getLoggedUser());
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
    private void resetBean() {
        beanEvent = new Event();
        selectedPreferences = new ArrayList<>();
        selectedUsers = new ArrayList<>();
        toSavePreferences = new ArrayList<>();
    }

    private void loadPreferences() {
        List<String> preferenzeEvento = pm.getPreferenceOfEvent(beanEvent);
        for (String preferenza : preferenzeEvento) {
            this.selectedPreferences.add(preferenza);
        }
    }

    private void loadInvitations() {
        List<String> invitedUsers = uem.invitedUsersOfEvent(beanEvent);
        for (String invitedUser : invitedUsers) {
            this.selectedUsers.add(invitedUser);
        }
    }

    private boolean controlDate() {
        beanEvent.convertStartDate(startDate);
        beanEvent.convertEndDate(endDate);
        Timestamp now = new Timestamp(new java.util.Date().getTime());

        //date must be after today and start must be before end date
        if (beanEvent.getStartDate().before(beanEvent.getEndDate()) || beanEvent.getStartDate().equals(beanEvent.getEndDate()) && beanEvent.getStartDate().after(now)) {
            return true;
        } else {
            return false;
        }

    }

    private void addEvent() throws OverlappingException, InvalidDateException {

        if (this.controlDate()) {
            IDEvent idEv = new IDEvent(idm.findFirstFreeID());
            beanEvent.setIdEvent(idEv);
            beanEvent.setCreator(um.getLoggedUser());
            em.addEvent(beanEvent, um.getLoggedUser());
        } else {
            throw new InvalidDateException();
        }
    }

    private void savePreferences() {
        for (int i = 0; i < selectedPreferences.size(); i++) {
            toSavePreferences.add(new Preference(beanEvent, selectedPreferences.get(i)));
            pm.addPreference(toSavePreferences.get(i));
        }
        if (selectedPreferences.isEmpty()) {
            for (int i = 0; i < this.listPref().size(); i++) {

                pm.addPreference(new Preference(beanEvent, listPref().get(i)));
            }
        }
        toSavePreferences = new ArrayList<>();
    }

    public boolean isCanDecline() {
        return !this.isOwnEvent & !this.creating;
    }

    private void updateEvent() throws OverlappingException {
        em.removeEvent(beanEvent);
        this.create();
    }

    private void addUserEvent() {

        userEvent = new UserEvent(beanEvent, um.getLoggedUser(), true);
        uem.addUserEvent(userEvent);
        for (String invitated1 : selectedUsers) {
            userEvent = new UserEvent(beanEvent, um.findByMail(invitated1), false);
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

    public Event getBeanEvent() {
        if (beanEvent == null) {
            beanEvent = new Event();
        }
        return beanEvent;
    }

    public void setBeanEvent(Event beanEvent) {
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

}
