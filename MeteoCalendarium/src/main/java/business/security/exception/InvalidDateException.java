/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.exception;

/**
 * exception in case of invalid date submission
 *
 * @author Alessandro De Angelis
 */
public class InvalidDateException extends Exception {

    public InvalidDateException() {
        super("Are you Serious!? , Start Date after End Date");
    }
}
