package boundary;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alessandro
 */
import entity.Event;
import entity.Forecast;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import control.BadWeatherNotificationManager;
import control.EventManager;
import control.ForecastManager;
import control.UserManager;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 * Event Manager on View
 *
 * @author Alessandro De Angelis
 */
@ViewScoped
@Stateful
@Named("scheduleView")
public class ScheduleView implements Serializable {

    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    EventManager em;
    @EJB
    UserManager um;
    @EJB
    BadWeatherNotificationManager bwm;
    @EJB
    ForecastManager fm;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */
    /**
     * Manager of PrimeFaces schedule
     */
    private ScheduleModel eventModel;

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    /**
     * initializer
     */
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        loadCalendar();
    }

    /**
     * load Events on Schedule
     */
    public void loadCalendar() {

        List<Event> Calendar = em.loadCalendar(um.getLoggedUser());
        eventModel = new DefaultScheduleModel();
        for (Event tempCalendar : Calendar) {
            //Assing a banner and save id in ScheduleEvent data
            DefaultScheduleEvent temp;
            String banner;
            banner = this.getBanner(tempCalendar);
            temp = new DefaultScheduleEvent(tempCalendar.getTitle(), tempCalendar.getStartDate(), tempCalendar.getEndDate(), banner);
            temp.setDescription(tempCalendar.getIdEvent().getId().toString());

            boolean alreadyIn = false;
            for (int i = 0; i < eventModel.getEventCount(); i++) {
                if (eventModel.getEvents().get(i).getDescription().equals(temp.getDescription())) {
                    alreadyIn = true;
                }

            }
            if (!alreadyIn) {
                eventModel.addEvent(temp);
            } else {
                eventModel.updateEvent(temp);
            }
        }
    }


    /*
     * ******************************************************************
     * PRIVATE FUNCTIONS
     *******************************************************************
     */
    /**
     * Return Name of Proper Banner for Event
     *
     * @param event
     * @param user
     * @return
     */
    private String getBanner(Event event) {
        String returnBanner = "";

        List<Forecast> forecastEvent = fm.getForecastOfEvent(event);
        if (forecastEvent.isEmpty() || em.isIndoor(event)) {
            return "NoForecast";
        } else {

        }
        if (forecastEvent.size() > 1) {
            returnBanner = returnBanner + "Variable";
        } else {
            returnBanner = returnBanner + forecastEvent.get(0).getMainCondition().getCondition();
        }
        if (bwm.isWarned(event)) {
            returnBanner = returnBanner + "Warned";
        } else {
            returnBanner = returnBanner + "NotWarned";
        }

        return returnBanner;
    }

    /*
     * ******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

}
