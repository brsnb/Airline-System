/**
 * AirlineSimulation class
 *		Model class representing the simulation's state
 */

package org.airlinesystem.model;

import java.math.BigDecimal;
import java.util.Properties;

import org.airlinesystem.graphdb.impl.AirportGraph;

public class AirlineSimulation {


	private FlightList listOfFlights = new FlightList();
	private AirportGraph graphOfAirports = new AirportGraph();
	private Properties simulationProperties;
	private BigDecimal totalCost;
	private BigDecimal totalRevenue;
	private BigDecimal totalProfit;


	public AirlineSimulation() {}

	public FlightList getListOfFlights() {
		return listOfFlights;
	}
	public void setListOfFlights(FlightList listOfFlights_) {
		listOfFlights = listOfFlights_;
	}
	public AirportGraph getGraphOfAirports() {
		return graphOfAirports;
	}
	public void setGraphOfAirports(AirportGraph graphOfAirports_) {
		graphOfAirports = graphOfAirports_;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost_) {
		totalCost = totalCost_;
	}
	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(BigDecimal totalRevenue_) {
		totalRevenue = totalRevenue_;
	}
	public BigDecimal getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(BigDecimal totalProfit_) {
		totalProfit = totalProfit_;
	}
	public Properties getSimulationProperties() {
		return simulationProperties;
	}

	public void setSimulationProperties(Properties simulationProperties_) {
		simulationProperties = simulationProperties_;
	}
}
