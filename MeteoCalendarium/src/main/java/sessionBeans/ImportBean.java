/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.Place;
import entities.User;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import managerBeans.EventManagerInterface;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class ImportBean {
    
    @EJB
    private EventManagerInterface em;
    public ImportBean(){}
    
     public void imports() {
    
        //removeEvent();
        File file;
        Event event = new Event();
        
       
        System.out.println("Import begin");
        
            file = new File("C:\\Users\\alessandro\\Documents\\NetBeansProjects\\MeteoCalendarium\\MeteoCalendarium\\src\\main\\import.xml");
          
        Timestamp help;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    org.w3c.dom.Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList events = doc.getElementsByTagName("event");
            System.out.println("Path: " +doc.getDocumentURI());
            System.out.println("Events dim: " + events.getLength());
            for (int i = 0; i < events.getLength(); i++) {
                Element element = (Element) events.item(i);
                NodeList titles = element.getElementsByTagName("title");
                event.setTitle(titles.item(0).getTextContent());
                System.out.println(event.getTitle());
                NodeList descriptions = element.getElementsByTagName("description");
                event.setDescription(descriptions.item(0).getTextContent());
                System.out.println(event.getDescription());
                NodeList startDates = element.getElementsByTagName("startDate");
                System.out.println(startDates.getLength());
                System.out.println(startDates.item(0).getTextContent());
                //next line gets error
                SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss");

    Date parsedTimeStamp = dateFormat.parse(startDates.item(0).getTextContent());

    Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
                
                
                
                System.out.println("Errore post help");
                event.setStartDate(timestamp);
                System.out.println(event.getStartDate().toString());
                NodeList endDates = element.getElementsByTagName("endDate");
                

    parsedTimeStamp = dateFormat.parse(endDates.item(0).getTextContent());

     timestamp = new Timestamp(parsedTimeStamp.getTime());
                event.setEndDate(timestamp);  
                System.out.println(event.getEndDate().toString());
                NodeList outdoors = element.getElementsByTagName("outdoor");
                if(outdoors.item(0).getTextContent().equalsIgnoreCase("1"))
                    event.setOutdoor(true);
                else
                    event.setOutdoor(false);
                System.out.println(event.isOutdoor() +"out");
                NodeList publics = element.getElementsByTagName("publicEvent");
                if(publics.item(0).getTextContent().equalsIgnoreCase("1"))
                    event.setPublicEvent(true);
                else
                    event.setPublicEvent(false);
                System.out.println(event.isPublicEvent()+" public");
                
                NodeList places = element.getElementsByTagName("place");
                Place place = new Place();
                place.setCity(places.item(0).getTextContent());
                event.setPlace(place);
                System.out.println(event.getPlace().getCity());
                System.out.println("pre creator");
                NodeList users = element.getElementsByTagName("creator");
                User user = new User();
                System.out.println(users.item(0).getTextContent());
                user.setEmail(users.item(0).getTextContent());
                System.out.println("error post set email user");
                event.setCreator(user);
                System.out.println("Within imports");
                em.addEvent(event);
                
                
            }
        } catch (Exception e) {
            System.out.println("Errore");
        }
        
    }
    }

