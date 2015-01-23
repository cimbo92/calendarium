/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.OverlappingException;
import entity.Event;
import entity.MainCondition;
import entity.Place;
import entity.Preference;
import entity.Users;
import entity.UserEvent;
import entity.IDEvent;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import control.EventManager;
import control.IDEventManager;
import control.PreferenceManager;
import control.UserEventManager;
import control.UserManager;
import java.io.IOException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * class for importing file xml
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class ImportBean {

    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    private EventManager em;
    @EJB
    private UserManager um;
    @EJB
    private IDEventManager idm;
    @EJB
    private PreferenceManager pm;
    @EJB
    private UserEventManager uem;

    public ImportBean() {
    }

    /**
     * function that reads the content of a xml file and charges the events in
     * the database
     */
    public void imports() {
        FacesContext context = FacesContext.getCurrentInstance();

        File file;
        Event event = new Event();

        String os = System.getProperty("os.name");
        String slash = "\\";
        if (os.equalsIgnoreCase("Mac OS X")) {
            slash = "/";
        }

        System.out.println("afet");
        String DefaultFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        file = new File(DefaultFolder + slash + "calendar.xml");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList events = doc.getElementsByTagName("event");
            if (events.getLength() != 0) {
                removeEvents();
            }
            for (int i = 0; i < events.getLength(); i++) {
                Element element = (Element) events.item(i);
                NodeList titles = element.getElementsByTagName("title");
                event.setTitle(titles.item(0).getTextContent());

                NodeList descriptions = element.getElementsByTagName("description");
                event.setDescription(descriptions.item(0).getTextContent());

                NodeList startDates = element.getElementsByTagName("startDate");

                //next line gets error
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss");

                Date parsedTimeStamp = dateFormat.parse(startDates.item(0).getTextContent());

                Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());

                event.setStartDate(timestamp);
                NodeList endDates = element.getElementsByTagName("endDate");

                parsedTimeStamp = dateFormat.parse(endDates.item(0).getTextContent());

                timestamp = new Timestamp(parsedTimeStamp.getTime());
                event.setEndDate(timestamp);

                NodeList outdoors = element.getElementsByTagName("outdoor");
                if (outdoors.item(0).getTextContent().equalsIgnoreCase("true")) {
                    event.setOutdoor(true);
                } else {
                    event.setOutdoor(false);
                }

                NodeList publics = element.getElementsByTagName("publicEvent");
                if (publics.item(0).getTextContent().equalsIgnoreCase("true")) {
                    event.setPublicEvent(true);
                } else {
                    event.setPublicEvent(false);
                }

                NodeList places = element.getElementsByTagName("place");
                Place place = new Place();
                place.setCity(places.item(0).getTextContent());
                event.setPlace(place);

                NodeList users = element.getElementsByTagName("creator");
                Users user = new Users();

                user.setEmail(users.item(0).getTextContent());

                event.setCreator(user);

                long id;
                id = idm.findFirstFreeID();
                IDEvent idEv = new IDEvent();
                idEv.setId(id);
                event.setIdEvent(idEv);
                try {
                    em.addEvent(event, event.getCreator());
                    NodeList preferences = element.getElementsByTagName("preference");
                    Preference pref;
                    MainCondition main;
                    for (int k = 0; k < preferences.getLength(); k++) {
                        pref = new Preference();
                        pref.setEvent(event);
                        main = new MainCondition();
                        main.setCondition(preferences.item(k).getTextContent());
                        pref.setMain(main);
                        pm.addPreference(pref);
                    }
                    NodeList invites = element.getElementsByTagName("invitated");
                    UserEvent userEvent;
                    userEvent = new UserEvent(event, um.getLoggedUser(), true);
                    uem.addUserEvent(userEvent);
                    for (int j = 0; j < invites.getLength(); j++) {

                        user = um.findByMail(invites.item(j).getTextContent());
                        if (user != null) {
                            userEvent = new UserEvent(event, user, false);
                            uem.addUserEvent(userEvent);
                        }

                    }
                } catch (OverlappingException e) {
                    System.out.println("Overlapping adding event");
                    context.addMessage(null, new FacesMessage("Error", "Overlapping"));
                }

            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | ParseException e) {
            System.out.println("File not opened");

        }

    }

    /**
     * this function removes all the event of a user
     */
    public void removeEvents() {
        em.removeAllEvent(um.getLoggedUser());
    }
}
