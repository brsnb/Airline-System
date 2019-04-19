/**
 * FlightBuilder class
 *		Creates a new flight using given data by adding 
 *		pilots, aircraft, and airports that will be stored
 *		in the flight itself
 */

package org.airlinesystem.helpers;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import org.airlinesystem.controllers.FlightRCPController;
import org.airlinesystem.model.Aircraft;
import org.airlinesystem.model.AircraftPilot;
import static org.airlinesystem.model.Aircraft.AircraftSize;
import org.airlinesystem.model.Airport;
import org.airlinesystem.model.Flight;

import java.util.Properties;

public class FlightBuilder {
	
    /**
     * Create new flight based off requirements passed in
     * 
     * @param aircraftSize_
     * 		enum that represents the size of plane that will be
     * 		passed in and used to determine the pilot assigned
     * @param maxSeatsPerSection_
     * 		integer array used to determine total number of seats on
     * 		the flight for each section
     * @param seatsFilledPerSection_
     * 		integer array used to determine total number of passengers
     * 		and the number of filled seats in each section for the flight
     * @param seatCostPerSection_
     * 		BigDecimal type array that determines the cost of a single seat
     * 		in each section
     * @param source_
     * 		String that represents the first node on the graph for the flight
     * @param destination_
     * 		String that represents the second node on the graph for the flight
     * @param distanceTravelled_
     * 		Double that represents that edge weight from the graph for the flight
     * @return
     * 		A new flight that has all variables set
     */
	public Flight flightDispatchService(AircraftSize aircraftSize_, int[] maxSeatsPerSection_, 
			int[] seatsFilledPerSection_, BigDecimal[] seatCostPerSection_, 
			String source_, String destination_, double distanceTravelled_,
			Properties modelProperties_) {
		
		PilotBuilder assignPilots = new PilotBuilder(modelProperties_);

		AircraftPilot _pilot = assignPilots.assignPilotToAircraft(aircraftSize_);
		AircraftPilot _coPilot = assignPilots.assignPilotToAircraft(aircraftSize_);	
		Aircraft _aircraftAssigned = new Aircraft(aircraftSize_, getTotalNumOfPassengers(seatsFilledPerSection_),
				seatsFilledPerSection_, seatCostPerSection_, getMaxAircraftSeats(maxSeatsPerSection_));
		Airport source = new Airport(source_);
		Airport destination = new Airport(destination_);
		
		Flight _newFlightFromData = new Flight(aircraftSize_, maxSeatsPerSection_, 
				seatsFilledPerSection_, seatCostPerSection_, source, destination, 
				distanceTravelled_, _pilot, _coPilot, _aircraftAssigned);
		setFlightRCPData(_newFlightFromData, modelProperties_);
		
		return _newFlightFromData;
	}
	
	/**
	 * Sets a given flight's revenue, cost, and profit data
	 * 
	 * @param flightToSet_
	 * 		the flight object that will be set
	 * @return
	 * 		N/A
	 */
	public void setFlightRCPData(Flight flightToSet_, Properties modelProperties_) {
		FlightRCPController _revenueCostProfitFinder = new FlightRCPController(modelProperties_);
		BigDecimal[] _flightRCPArray = _revenueCostProfitFinder.getRCPAsArray(flightToSet_);
		flightToSet_.setRevenue(_flightRCPArray[0]);
		flightToSet_.setCost(_flightRCPArray[1]);
		flightToSet_.setProfit(_flightRCPArray[2]);
	}
	
	/**
	 * Finds the number of passengers that are actually on a flight
	 * 
	 * @param seatsFilledPerSection_
	 * 		Integer array that is summed to find passenger number
	 * @return
	 * 		total number of passengers as an integer
	 */
	public int getTotalNumOfPassengers(int[] seatsFilledPerSection_) {
		return IntStream.of(seatsFilledPerSection_).sum();
	}
	
	/**
	 * Find the number of seats on a flight
	 * 
	 * @param maxSeatsPerSection_
	 * 		Integer array that is summed to find number of seats
	 * @return
	 * 		total number of seats as an integer
	 */
	public int getMaxAircraftSeats(int[] maxSeatsPerSection_) {
		return IntStream.of(maxSeatsPerSection_).sum();
	}
}
