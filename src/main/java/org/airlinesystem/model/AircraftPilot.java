package org.airlinesystem.model;

import java.math.BigDecimal;



public class AircraftPilot {
	
	/**
	 * Possible ranks of pilots
	 * <li>{@link #SENIOR}/<li>
	 * <li>{@link #MIDLEVEL}/<li>
	 * <li>{@link #JUNIOR}/<li>
	 */
	public static enum AircraftPilotSeniority {
		/**
		 * Senior ranking pilot
		 */
		SENIOR,
		
		/**
		 * Middle ranking pilot
		 */
		MIDLEVEL,
		
		/**
		 * Junior ranking pilot
		 */
		JUNIOR
	};

    private BigDecimal costPerFlight;

    private AircraftPilotSeniority seniority;

    public AircraftPilot() {}

    public AircraftPilotSeniority getSeniority() {
        return seniority;
    }

    public void setSeniority(AircraftPilotSeniority seniority_) {
        seniority = seniority_;
    }

    public final void setCostPerFlight(BigDecimal costPerFlight_) {
        costPerFlight = costPerFlight_;
    }

    public BigDecimal getCostPerFlight() {
        return costPerFlight;
    }
}
