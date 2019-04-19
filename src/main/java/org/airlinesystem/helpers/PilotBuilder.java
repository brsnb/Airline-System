/**
 * PilotBuilder class
 *		Creates a new pilot object given specific requirements
 */

package org.airlinesystem.helpers;

import java.math.BigDecimal;
import java.util.Properties;

import org.airlinesystem.model.AircraftPilot;
import static org.airlinesystem.model.AircraftPilot.AircraftPilotSeniority;
import static org.airlinesystem.model.Aircraft.AircraftSize;

public class PilotBuilder {

    private BigDecimal _juniorCost;
    private BigDecimal _midlevelCost;
    private BigDecimal _seniorCost;

    
    public PilotBuilder(Properties modelProperties_) {
    	_juniorCost = new BigDecimal(modelProperties_.getProperty("JUNIOR_PILOT_PAY"));
    	_midlevelCost = new BigDecimal(modelProperties_.getProperty("MIDLEVEL_PILOT_PAY"));
    	_seniorCost = new BigDecimal(modelProperties_.getProperty("SENIOR_PILOT_PAY"));
    }
    
    /**
     * Create new pilot based off size of plane they will be flying
     * 
     * @param aircraftSize_
     * 		AircraftSize enum value that represents the size of plane that will be
     * 		passed in and used to determine the pilot assigned
     * @return
     * 		a new pilot to be assigned to the flight
     */
    public AircraftPilot assignPilotToAircraft(AircraftSize aircraftSize_) {
        AircraftPilot returnPilot;
        switch (aircraftSize_) {
            case L:
                returnPilot = new AircraftPilot();
                returnPilot.setSeniority(AircraftPilotSeniority.SENIOR);
                returnPilot.setCostPerFlight(_seniorCost);
                return returnPilot;
            case M:
                returnPilot = new AircraftPilot();
                returnPilot.setSeniority(AircraftPilotSeniority.MIDLEVEL);
                returnPilot.setCostPerFlight(_midlevelCost);
                return returnPilot;
            case S:
            default:
                returnPilot = new AircraftPilot();
                returnPilot.setSeniority(AircraftPilotSeniority.JUNIOR);
                returnPilot.setCostPerFlight(_juniorCost);
                return returnPilot;
        }
    }
}
