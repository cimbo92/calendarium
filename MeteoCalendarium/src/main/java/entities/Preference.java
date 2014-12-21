/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alessandro
 */
public enum Preference {
    
    Sun("Snow"), Cloud("Cloud"), Rain("Rain"), Snow("Snow");
    private String abbreviation;

    
    private Preference(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * restituisce il tipo di preferenza a partire dalla stringa passata per
     * parametro il parametro di fatto corrisponde all'abbreviazione del tipo
     * 
     * @param abbreviation
     * @return
     */
    public static Preference fromAbbreviation(String abbreviation) {
        if (abbreviation != null) {
            for (Preference tipo : Preference.values()) {
                if (abbreviation.equalsIgnoreCase(tipo.abbreviation)) {
                    return tipo;
                }
            }
        }
        return null;
    }
    
    public List<String> getListPref()
    {
        List pref = new ArrayList<String>();
        pref.add("Sun");
        pref.add("Cloud");
        pref.add("Rain");
        pref.add("Snow");
        return pref;
    }
    
}
