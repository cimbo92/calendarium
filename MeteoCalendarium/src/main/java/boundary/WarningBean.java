/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.InvalidDateException;
import HelpClasses.OverlappingException;
import entity.Event;
import entity.IDEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import control.BadWeatherNotificationManagerInterface;
import control.EventManagerInterface;
import control.PreferenceManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class WarningBean {

    @EJB
    private BadWeatherNotificationManagerInterface bwnm;
    @EJB
    UserManagerInterface um;
    @EJB
    EventManagerInterface em;
    @EJB
    PreferenceManagerInterface pm;
    @EJB
    UserEventManagerInterface uem;

    private List<Event> warnings;
    private List<Timestamp> solutions;
    private boolean enableModify=false;



    public boolean isEnableModify() {
        return enableModify;
    }

    public void setEnableModify(boolean enableModify) {
        this.enableModify = enableModify;
    }

    public void loadWarnings(){
  warnings = new ArrayList<>();
        warnings = bwnm.findWarnings(um.getLoggedUser());
        if(!warnings.isEmpty())
        {
            searchSolution();
            enableModify = true;
        }
        else
        {
            enableModify = false;
            Event noWarning = new Event();
            noWarning.setTitle("No Warnings");
            noWarning.setIdEvent(new IDEvent("-1"));
            warnings.add(noWarning);
        }
}



     @PostConstruct
    public void init() {
        warnings = new ArrayList<>();
        warnings = bwnm.findWarnings(um.getLoggedUser());
        if(!warnings.isEmpty())
        {
            searchSolution();
            enableModify = true;
        }
        else
        {
            enableModify = false;
            Event noWarning = new Event();
            noWarning.setTitle("No Warnings");
            noWarning.setIdEvent(new IDEvent("-1"));
            warnings.add(noWarning);
        }
    }

    public void searchSolution()
    {
        solutions = bwnm.findSolution(warnings);
    }

    public List<Timestamp> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Timestamp> solutions) {
        this.solutions = solutions;
    }

    public List<Event> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Event> warnings) {
        this.warnings = warnings;
    }

    public String modifyOk(Event event, EventBean eb) throws OverlappingException, InvalidDateException
    {
        List<String> preferenceEvent = new ArrayList<>();
        preferenceEvent= pm.getPreferenceOfEvent(event);
        List<String> userEvent = new ArrayList<>();
        userEvent = uem.invitedUsersOfEvent(event);

        em.removeEvent(event);
        long diff = event.getEndDate().getTime()-event.getStartDate().getTime();
        System.out.println("Evento modificato: " + event.getTitle() + "diff: " + diff);
        Timestamp help;
        boolean ok=false;
        for(int i=0;i<warnings.size()&&!ok ;i++)
        {
            if(Objects.equals(warnings.get(i).getIdEvent().getId(), event.getIdEvent().getId()))
                if(solutions.get(i)!=null)
                {
                     event.setStartDate(solutions.get(i));
                     help=new Timestamp(0);
                     help.setTime(solutions.get(i).getTime()+diff);
                     event.setEndDate(help);
                     eb.modifyFromWarning(event, preferenceEvent, userEvent);
                     ok=true;
                     warnings.remove(i);
                }
        }
              return "calendar?faces-redirect=true";
    }

    public String getDate(Long id)
    {
        if(id!=-1)
        {
            for(int i=0;i<warnings.size();i++)
            {
                if(Objects.equals(warnings.get(i).getIdEvent().getId(), id))
                    if(solutions.get(i)!=null)
                        return solutions.get(i).toString();
            }
            return "no possible postpone in the next 10 days";
        }
        return "";
    }
}
