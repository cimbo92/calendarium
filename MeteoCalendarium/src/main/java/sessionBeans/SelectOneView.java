/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.bean.ManagedProperty;
import managerBeans.EventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.model.DefaultScheduleModel;
 
@RequestScoped
@Named
public class SelectOneView implements Serializable {   
     @EJB
    private UserManagerInterface um;
    
    private String option;   

    public String getOption() {
        return option;
    }
 
    @Schedule(second = "/5", minute = "*", hour = "*", persistent = false)
    public void prova()
    {
        System.out.println(option);
    }
    
    
    public void setOption(String option) {
        this.option = option;
    }
 
private List<String> users =new ArrayList<>();

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @PostConstruct
    public void init() {
        users =  um.getListUsers();  
    }
    
    public void print()
    {
        System.out.println("prova"+option);
    }
    
}
