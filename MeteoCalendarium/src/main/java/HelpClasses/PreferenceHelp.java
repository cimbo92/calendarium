/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelpClasses;

import entities.MainCondition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro
 */
public class PreferenceHelp {
    
    private List<MainCondition> selectedPreference= new ArrayList<>();
    

    public List<MainCondition> getSelectedPreference() {
        return selectedPreference;
    }

    public void setSelectedPreference(List<MainCondition> selectedPreference) {
        this.selectedPreference = selectedPreference;
    }
    
    
}
