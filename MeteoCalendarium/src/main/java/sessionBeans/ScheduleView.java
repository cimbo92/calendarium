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
import entities.Forecast;
import entities.User;
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
import managerBeans.BadWeatherNotificationManagerInterface;
import managerBeans.EventManagerInterface;
import managerBeans.ForecastManagerInterface;
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
    private UserManagerInterface um;
 
    @EJB 
         private   BadWeatherNotificationManagerInterface bwm;
    @EJB 
        private    ForecastManagerInterface fm;
    
    
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
            String banner;
            banner = this.getBanner(tempCalendar1, um.getLoggedUser());
            temp = new DefaultScheduleEvent(tempCalendar1.getTitle(), tempCalendar1.getStartDate(), tempCalendar1.getEndDate(),banner); 
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

    public String getBanner(Event event,User user){
        String returnBanner = "";
        
        List<Forecast> forecastEvent=  fm.getForecastOfEvent(event);
        if(forecastEvent.isEmpty())
        {
            return "NoForecast";
        }else{
            
        }
        if(forecastEvent.size()>1)
        {
        returnBanner =returnBanner+"Variable";
        }
        else{
        returnBanner =returnBanner+forecastEvent.get(0).getMainCondition().getCondition();
        }
        if(bwm.isWarned(event)){
            returnBanner = returnBanner + "Warned";
        }else
        {
             returnBanner = returnBanner +"NotWarned" ;
        }
        
        return returnBanner;
    }
    
}
