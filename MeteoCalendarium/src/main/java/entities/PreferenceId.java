/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Objects;



/**
 *
 * @author Alessandro
 */
public class PreferenceId {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.event;
        hash = 19 * hash + Objects.hashCode(this.main);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PreferenceId other = (PreferenceId) obj;
        if (this.event != other.event) {
            return false;
        }
        if (!Objects.equals(this.main, other.main)) {
            return false;
        }
        return true;
    }
int event;       
String main;
}
