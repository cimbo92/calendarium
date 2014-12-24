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
public class Date {
    
    private int year;
    
    private int month;
    
    private int day;
    
    private int hour;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
    
    public boolean equals(Date first, Date second){
        return (first.getHour() == second.getHour()) && (first.getYear() == second.getYear()) && (first.getMonth() == second.getMonth()) && (first.getDay()==second.getDay()); 
    }
    
    public boolean sameDay(Date first, Date second){
        return (first.getYear() == second.getYear()) && (first.getMonth() == second.getMonth()) && (first.getDay()==second.getDay()); 
    }
    
}
