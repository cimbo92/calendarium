/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */
public class Place implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    @Id 
    @NotNull(message = "May not be empty")
    private String postalCode;
    @NotNull(message = "May not be empty")
    private String city;
    
    
}
