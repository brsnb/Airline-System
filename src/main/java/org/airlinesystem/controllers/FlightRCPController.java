/**
 * FlightRCPController class
 *		Calculates the revenue, cost, and overall profit
 *		for a given flight
 */

package org.airlinesystem.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Properties;

import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.model.Flight;
import org.airlinesystem.model.FlightList;
import org.airlinesystem.exceptions.AirlineSystemException;
import org.jgrapht.graph.DefaultEdge;

public class FlightRCPController {
	private BigDecimal fuelCost;
	private FullLogging flightRCPControllerLog = FullLogging.getInstance();
	
	/**
	 * Default constructor
	 */
	public FlightRCPController() {
	}
	
	public FlightRCPController(Properties modelProperties_) {
		fuelCost = new BigDecimal(modelProperties_.getProperty("FUEL_COST"));
	}
	
	/**
	 * Calculate and set revenue class variable
	 * 
	 * @param flightToCalculate_
	 * 		flight with information to use for finding revenue
	 * @return
	 * 		BigDecimal type revenue to be used for profit calculations
	 */
	public BigDecimal findRevenue(Flight flightToCalculate_) {
		BigDecimal _revenue = new BigDecimal("0");
		int[] seatsFilledPerSection_ = flightToCalculate_.getSeatsFilledPerSection();
		BigDecimal[] seatCostPerSection_ = flightToCalculate_.getSeatCostPerSection();
		
		for (int i = 0; i < flightToCalculate_.getSeatsFilledPerSection().length; i++) {
			BigDecimal _numberOfPeopleInClass = new BigDecimal(seatsFilledPerSection_[i]);
			_revenue = _revenue.add(_numberOfPeopleInClass.multiply(seatCostPerSection_[i]));
		}
		return _revenue;
	}

	/**
	 * Calculate and set cost class variable
	 * 
	 * @param flightToCalculate_
	 * 		flight with information to use for finding cost
	 * @return
	 * 		BigDecimal type cost to be used for profit calculations
	 */
	public BigDecimal findCost(Flight flightToCalculate_) {
		BigDecimal _cost = new BigDecimal(flightToCalculate_.getDistanceTravelled());
		_cost = _cost.multiply(fuelCost);
		_cost = _cost.add(flightToCalculate_.getCoPilot().getCostPerFlight());
		_cost = _cost.add(flightToCalculate_.getPilot().getCostPerFlight());
		return _cost;
	}
	
	/**
	 * Calculate and set profit class variable
	 * 
	 * @param flightToCalculate_
	 * 		flight with information to use for finding profit
	 * @return
	 * 		BigDecimal type profit for the given flight
	 */
	public BigDecimal findProfit(Flight flightToCalculate_) {
		BigDecimal _profit = new BigDecimal("0");
		_profit = _profit.add(findRevenue(flightToCalculate_));
		_profit = _profit.subtract(findCost(flightToCalculate_));
		return _profit;
	}
	
	/**
	 * Find Revenue, Cost, and Profit for an array
	 * 
	 * @param flightToCalculate_
	 * 		flight with information to use for calculations
	 * @return
	 * 		BigDecimal type array holding revenue, cost, and profit
	 * 		to be stored in the flight
	 */
	public BigDecimal[] getRCPAsArray(Flight flightToCalculate_) {
		BigDecimal[] _flightRCPArray = new BigDecimal[3];
		_flightRCPArray[0] = findRevenue(flightToCalculate_);
		_flightRCPArray[1] = findCost(flightToCalculate_);
		_flightRCPArray[2] = _flightRCPArray[0].subtract(_flightRCPArray[1]);
		return _flightRCPArray;
	}
	
	/**
	 * Calculate Overall profit for an entire list of flights
	 * 
	 * @param listOfFlights_
	 * 		the FlightList that will be iterated through until there are
	 * 		no more new flights remaining
	 * @return
	 * 		BigDecimal type _totalProfit that is the result of all flight profits
	 * 		may be positive or negative depending on flights input
	 */
	public BigDecimal[] findTotalRCPOfFlightList (FlightList listOfFlights_) {
		Iterator<Flight> _flightListItr = listOfFlights_.iterator();
		BigDecimal _totalProfit = new BigDecimal("0");
		BigDecimal _totalCost = new BigDecimal("0");
		BigDecimal _totalRevenue = new BigDecimal("0");
		Flight _currentFlight;
		NumberFormat _numberFormatter = NumberFormat.getInstance();
		
		while (_flightListItr.hasNext()) {
			_currentFlight = _flightListItr.next();
			_totalCost = _totalCost.add(_currentFlight.getCost());
			_totalRevenue = _totalRevenue.add(_currentFlight.getRevenue());
			_totalProfit = _totalProfit.add(_currentFlight.getProfit());
			flightRCPControllerLog.resultsInfo("Individual flight profit = $" + 
			_numberFormatter.format(_currentFlight.getProfit()));
		}
		BigDecimal[] _totalFlightRCPArray = {_totalRevenue, _totalCost, _totalProfit};
		return _totalFlightRCPArray;
	}

	/**
	 *  Finds the average profit over all the flights from a source to a destination airport.
	 *  
	 * 	@param listOfFlights_
	 * 		   the FlightList that will be iterated through until there are
	 * 		   no more new flights remaining
	 *  
	 *  @param airportGraph_
	 *  	   the AirportGraph corresponding to the current model used to access
	 *  	   the graphs unique edges
	 *  
	 *  @param source_
	 *  	   string representing the source airport
	 *  
	 *  @param destination_
	 *  	   string representing the destination airport
	 *  
	 *  @return
	 *  	   the BigDecimal value of the average profit for the specific edge requested
	 */
	
	public BigDecimal findAverageProfitPerEdge(FlightList listOfFlights_, AirportGraph airportGraph_,
			String source_, String destination_) throws AirlineSystemException {

		DefaultEdge _testEdge = airportGraph_.getGraphOfAirports().getEdge(source_, destination_);
		BigDecimal _totalProfit = new BigDecimal(0);
		int _counter = 0;
	
		if(!listOfFlights_.getFlightMap().containsKey(_testEdge)) {
			throw new AirlineSystemException("Edge is not contained in the map");
		}
		
		for(Flight _f : listOfFlights_.getFlightMap().get(_testEdge)) {
			_totalProfit = _totalProfit.add(_f.getProfit());
			_counter++;
		}
		
		BigDecimal _averageProfit = _totalProfit.divide(new BigDecimal(_counter), 2, RoundingMode.FLOOR);
		
		return _averageProfit;
	}
}
