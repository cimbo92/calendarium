/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelpClasses;

/**
 *
 * @author home
 */
public class OverlappingException extends Exception{
       
    public OverlappingException(){
        super("Operation Impossible, Found Overlapping");
    }
}
