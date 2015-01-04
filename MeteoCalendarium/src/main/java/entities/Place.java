/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */
@Entity
public class Place implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
//    @NotNull(message = "May not be empty")
//    private String postalCode;
    @Id
    @NotNull(message = "May not be empty")
    private String city;
   
//    public String getPostalCode() {
//        return postalCode;
//    }
//
//    public void setPostalCode(String postalCode) {
//        this.postalCode = postalCode;
//    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city.toLowerCase();
    }
    
}
