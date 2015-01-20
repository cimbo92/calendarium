/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Entity that rapresents a Place
 *
 * @author home
 */
@Entity
public class Place implements Serializable {

    /*
     *******************************************************************
     * FIELDS
     *******************************************************************
     */
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull(message = "May not be empty31")
    private String city;

    /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city.toLowerCase();
    }

    public Place() {

    }

    public Place(String city) {
        this.city = city;
    }
}
