/*****************************************************************************
 * FILE: Airport.java
 * DSCRPT:
 ****************************************************************************/

package org.airlinesystem.model;

public class Airport {
	
    private String name;
    
    public Airport(String name_) {
    	name = name_;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name_) {
        name = name_;
    }

    @Override
    public String toString() {
        return "Airport [name=" + name + "]";
    }
}
