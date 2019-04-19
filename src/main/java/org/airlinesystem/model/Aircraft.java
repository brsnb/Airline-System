package org.airlinesystem.model;

import java.math.BigDecimal;

public class Aircraft {

	/**
	 * Possible sizes of aircraft
	 * <li>{@link #S}/<li>
	 * <li>{@link #M}/<li>
	 * <li>{@link #L}/<li>
	 */
	public static enum AircraftSize {
		/**
		 * Small plane
		 */
		S,
		
		/**
		 * Medium plane
		 */
		M,
		
		/**
		 * Large plane
		 */
		L;
	}
	private AircraftSize aircraftSize;
	private AircraftSectionList sectionList;
	private int maxAircraftSeats;
	private int totalNumOfPassengers;

		
	public Aircraft(AircraftSize aircraftSize_, int totalNumOfPassengers_, int[] seatsFilledPerSection_, 
			BigDecimal[] costOfSeat_, int maxAircraftSeats_) {
		aircraftSize = aircraftSize_;
		sectionList = new AircraftSectionList(aircraftSize_, seatsFilledPerSection_, costOfSeat_);
		maxAircraftSeats = maxAircraftSeats_;
		totalNumOfPassengers = totalNumOfPassengers_;
	}

	public AircraftSize getAircraftSize() {
		return aircraftSize;
	}

	public void setAircraftSize(AircraftSize aircraftSize) {
		this.aircraftSize = aircraftSize;
	}

	public AircraftSectionList getSectionList() {
		return sectionList;
	}

	public int getMaxAircraftSeats() {
		return maxAircraftSeats;
	}
	
	public void setMaxAircraftSeats(int maxAircraftSeats_) {
		maxAircraftSeats = maxAircraftSeats_;
	}
	
	public void setTotalNumOfPassengers(int totalNumOfPassengers_) {
		totalNumOfPassengers = totalNumOfPassengers_;
	}
	
	public int getTotalNumOfPassengers() {
		return totalNumOfPassengers;
	}
}
