package org.airlinesystem.controllers;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.math.BigDecimal;
import java.util.Properties;

import org.airlinesystem.controllers.FlightRCPController;
import org.airlinesystem.controllers.RuntimePropertyController;
import org.airlinesystem.helpers.FlightBuilder;
import org.airlinesystem.model.Flight;
import org.airlinesystem.model.FlightList;
import static org.airlinesystem.model.Aircraft.AircraftSize;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.helpers.ReadGraphFromPSV;
import org.airlinesystem.exceptions.*;

public class FlightRCPControllerTest {
	
	private final static int[] MAX_SEATS = {10, 10, 10, 10};
	private final static int[] SEATS_FILLED = {10, 10, 10, 10};
	private final static BigDecimal[] SEAT_COST =  {new BigDecimal(10), 
			new BigDecimal(15), new BigDecimal(20), new BigDecimal(25)};

	private static RuntimePropertyController propManager;
	private static Properties testProps;
	private static FlightBuilder fd;
	private static Flight testFlight;
	private static FlightRCPController testRcp;

	private AirportGraph airportGraph = new AirportGraph();
	private FlightList testFlightList = new FlightList();
	
	@BeforeClass
	public static void initialize() {
		propManager = new RuntimePropertyController();
		try {
			testProps = propManager.loadDefaultProperties();
		} catch(AirlineSystemException _e) {
			System.exit(0);
		}
		fd = new FlightBuilder();
		testFlight = fd.flightDispatchService(AircraftSize.L, MAX_SEATS, SEATS_FILLED, 
				SEAT_COST, "1", "2", 100, testProps);
		
		testRcp = new FlightRCPController(testProps);
	}
	
	@Before
	public void initializeFlightListAndGraph() {
		ReadGraphFromPSV loadGraph = new ReadGraphFromPSV();
		loadGraph.readEdgeIntoGraph(airportGraph, "1", "2", 100);
		testFlightList.addFlightToList(testFlight, airportGraph);
		testFlightList.addFlightToList(testFlight, airportGraph);
	}

	@After
	public void clearFlightListAndGraphAfterTest() {
		testFlightList.clear();
		airportGraph.clearGraph();
	}
	
	@Test
	public void testFindRevenue() {
		BigDecimal revenueTest = new BigDecimal(0);
		BigDecimal expectedRevenue = new BigDecimal(700);
		
		revenueTest = testRcp.findRevenue(testFlight);
		
		assertEquals("The value of revenue does not match", expectedRevenue, revenueTest);
	}

	@Test
	public void testFindCost() {
		BigDecimal costTest = new BigDecimal(0);
		BigDecimal expectedCost = new BigDecimal(3100);
		
		costTest = testRcp.findCost(testFlight);
		
		assertEquals("The value of cost does not match", expectedCost.doubleValue(),
				costTest.doubleValue(), .01);
	}

	@Test
	public void testFindProfit() {
		BigDecimal profitTest = new BigDecimal(0);
		BigDecimal expectedProfit = new BigDecimal(-2400);
		
		profitTest = testRcp.findProfit(testFlight);
		
		assertEquals("The value of profit does not match", expectedProfit.doubleValue(), 
				profitTest.doubleValue(), .01);
	}
	
	@Test
	public void testFindTotalRCPOfFlightList() {
		BigDecimal[] totalRCP;
		BigDecimal expectedRevenue = new BigDecimal(1400);
		BigDecimal expectedCost = new BigDecimal(6200);
		BigDecimal expectedProfit = new BigDecimal(-4800);
		
		totalRCP = testRcp.findTotalRCPOfFlightList(testFlightList);
		
		assertEquals("The value of revenue does not match", expectedRevenue, totalRCP[0]);
		assertEquals("The value of cost does not match", expectedCost.doubleValue(),
				totalRCP[1].doubleValue(), .01);
		assertEquals("The value of profit does not match", expectedProfit.doubleValue(), 
				totalRCP[2].doubleValue(), .01);
	}
	
	@Test
	public void testFindAverageRCPPerEdge() {
		BigDecimal averageProfit = new BigDecimal(0);
		BigDecimal expectedAverageProfit = new BigDecimal(-2400);

		try {
			averageProfit = testRcp.findAverageProfitPerEdge(testFlightList, airportGraph, "1", "2");
		} catch (AirlineSystemException _e) {
			_e.printStackTrace();
		}
		
		assertEquals("The value of average profit does not match", expectedAverageProfit.doubleValue(), 
				averageProfit.doubleValue(), .01);
	}
	
	@Test (expected = AirlineSystemException.class)
	public void testNullPointerException() throws AirlineSystemException {
		testRcp.findAverageProfitPerEdge(testFlightList, airportGraph, "NULL", "NULL2");
	}
}
