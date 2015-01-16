
package sessionBeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Alessandro De Angelis
 */
@RequestScoped
@Named
/**
 * Bean for Images
 */
public class ImageBean {

    /**
     * get url of demoImage from repository
     * @return
     */
    public String getUDemoImage()
    {
        return "https://calendarium.googlecode.com/git/MeteoCalendarium/src/main/resources/images/logo.jpg";
    }

    public String getUDemoImageBig()
    {
        return "https://calendarium.googlecode.com/git/MeteoCalendarium/src/main/resources/images/logoBig.png";
    }


}
