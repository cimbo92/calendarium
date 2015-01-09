/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.Place;
import java.io.File;
import java.sql.Timestamp;
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
        
       
        
        
            file = new File(getPathRisorsa("evento.xml"));
          
        Timestamp help;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(file);
            NodeList events = doc.getElementsByTagName("event");
            for (int i = 0; i < events.getLength(); i++) {
                Element element = (Element) events.item(i);
                NodeList titles = element.getElementsByTagName("title");
                event.setTitle(titles.item(0).getTextContent());
                NodeList descriptions = element.getElementsByTagName("description");
                event.setDescription(descriptions.item(0).getTextContent());
                NodeList startDates = element.getElementsByTagName("startDate");
                help = Timestamp.valueOf(startDates.item(0).getTextContent());
                event.setStartDate(help);
                NodeList endDates = element.getElementsByTagName("endDate");
                help = Timestamp.valueOf(endDates.item(0).getTextContent());
                event.setEndDate(help);  
                NodeList outdoors = element.getElementsByTagName("outdoor");
                if(outdoors.item(0).getTextContent().equalsIgnoreCase("1"))
                    event.setOutdoor(true);
                else
                    event.setOutdoor(false);
                NodeList publics = element.getElementsByTagName("publicEvent");
                if(publics.item(0).getTextContent().equalsIgnoreCase("1"))
                    event.setPublicEvent(true);
                else
                    event.setPublicEvent(false);
                
                Place place = new Place();
                place.setCity("ciao");
                event.setPlace(place);
                
                em.addEvent(event);
                
                
            }
        } catch (Exception e) {
            System.out.println("Errore");
        }
        
    }
    
     
     public static String getPathRisorsa(String nomeRisorsa) {
        File risorsa = new File("src/main/" + nomeRisorsa);
        return risorsa.getPath();
    }
}
