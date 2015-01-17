/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelpClasses;

/**
 *
 * @author Alessandro
 */
public class RegistrationException extends Exception{
public RegistrationException(){
        super("Username Already Present");
    }
}
