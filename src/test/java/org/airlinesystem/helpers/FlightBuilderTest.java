package org.airlinesystem.helpers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;

import java.math.BigDecimal;
import java.util.Properties;
import java.io.File;

import org.airlinesystem.controllers.RuntimePropertyController;
import org.airlinesystem.helpers.FlightBuilder;
import org.airlinesystem.model.Flight;
import static org.airlinesystem.model.AircraftPilot.AircraftPilotSeniority;
import static org.airlinesystem.model.Aircraft.AircraftSize;

public class FlightBuilderTest {

	private static FlightBuilder testDispatcher;
	private static RuntimePropertyController propManager;
	private static Properties testProps;
	
	@BeforeClass
	public static void initialize() {
		testDispatcher = new FlightBuilder();
		propManager = new RuntimePropertyController();
		testProps = propManager.loadRuntimeProperties(new File("default.properties"));
	}
	
	@Test
	public void testFlightDispatchService() {
		AircraftSize _aircraftSize = AircraftSize.L;
		int[] _maxSeats = new int[] {150, 100, 100, 50};
		int[] _seatsFilled = new int[] {73, 42, 10, 5};
		BigDecimal[] _seatCost = {new BigDecimal("250"), new BigDecimal("350"), 
				new BigDecimal("450"), new BigDecimal("650")};
		String _source = "1";
		String _dest = "2";
		double _distanceTravelled = 2500;
		
		Flight testDispatch = testDispatcher.flightDispatchService(_aircraftSize, _maxSeats, 
				_seatsFilled, _seatCost, _source, _dest, _distanceTravelled, testProps);

		assertEquals("Plane size should be large", AircraftSize.L, testDispatch.getAircraftSize());
		assertEquals("Max number of seats should be 400", 400, 
				testDispatch.getAircraftAssigned().getMaxAircraftSeats());
		assertEquals("Plane seats filled should be the same as entered", _seatsFilled, 
				testDispatch.getSeatsFilledPerSection());
		assertEquals("Plane source should be 1", _source, testDispatch.getSource().getName());
		assertEquals("Plane destination should be 2", _dest, testDispatch.getDestination().getName());
		assertEquals("Plane travel distance should be 2500", _distanceTravelled, 
				testDispatch.getDistanceTravelled(), .1);
		assertEquals("Pilot should be senior level", AircraftPilotSeniority.SENIOR, testDispatch.getPilot().getSeniority());
		assertEquals("CoPilot should be senior level", AircraftPilotSeniority.SENIOR, testDispatch.getCoPilot().getSeniority());
		for(int i = 0; i < _seatCost.length; i ++) {
			assertEquals("Seat cost for this section should be " + _seatCost[i], 
					_seatCost[i], testDispatch.getSeatCostPerSection()[i]);
		}
		
		_aircraftSize = AircraftSize.M;
		_maxSeats = new int[] {100, 0, 70, 30};
		_seatsFilled = new int[] {27, 0, 35, 3};
		BigDecimal[] _seatCostMedium = {new BigDecimal("150"), new BigDecimal("0"), 
				new BigDecimal("250"), new BigDecimal("400")};
		_source = "2";
		_dest = "3";
		_distanceTravelled = 1237;
		
		testDispatch = testDispatcher.flightDispatchService(_aircraftSize, _maxSeats, 
				_seatsFilled, _seatCostMedium, _source, _dest, _distanceTravelled, testProps);
		assertEquals("Plane size should be medium", AircraftSize.M, testDispatch.getAircraftSize());
		assertEquals("Max number of seats should be 200", 200, 
				testDispatch.getAircraftAssigned().getMaxAircraftSeats());
		assertEquals("Plane seats filled should be the same as entered", _seatsFilled, 
				testDispatch.getSeatsFilledPerSection());
		assertEquals("Plane source should be 2", _source, testDispatch.getSource().getName());
		assertEquals("Plane destination should be 3", _dest, testDispatch.getDestination().getName());
		assertEquals("Plane travel distance should be 1237", _distanceTravelled, 
				testDispatch.getDistanceTravelled(), .1);
		assertEquals("Pilot should be middle level", AircraftPilotSeniority.MIDLEVEL, testDispatch.getPilot().getSeniority());
		assertEquals("CoPilot should be middle level", AircraftPilotSeniority.MIDLEVEL, testDispatch.getCoPilot().getSeniority());
		for(int i = 0; i < _seatCost.length; i ++) {
			assertEquals("Seat cost for this section should be " + _seatCostMedium[i], _seatCostMedium[i], testDispatch.getSeatCostPerSection()[i]);
		}
		
		_aircraftSize = AircraftSize.S;
		_maxSeats = new int[] {50, 0, 0, 0};
		_seatsFilled = new int[] {33, 0, 0, 0};
		BigDecimal[] _seatCostSmall = {new BigDecimal("100"), new BigDecimal("0"), 
				new BigDecimal("0"), new BigDecimal("0")};
		_source = "3";
		_dest = "1";
		_distanceTravelled = 232;
		
		testDispatch = testDispatcher.flightDispatchService(_aircraftSize, _maxSeats, 
				_seatsFilled, _seatCostSmall, _source, _dest, _distanceTravelled, testProps);
		assertEquals("Plane size should be small", AircraftSize.S, testDispatch.getAircraftSize());
		assertEquals("Max number of seats should be 50", 50, 
				testDispatch.getAircraftAssigned().getMaxAircraftSeats());
		assertEquals("Plane seats filled should be the same as entered", _seatsFilled, 
				testDispatch.getSeatsFilledPerSection());
		assertEquals("Plane source should be 3", _source, testDispatch.getSource().getName());
		assertEquals("Plane destination should be 1", _dest, testDispatch.getDestination().getName());
		assertEquals("Plane travel distance should be 232", _distanceTravelled, 
				testDispatch.getDistanceTravelled(), .1);
		assertEquals("Pilot should be bottom level", AircraftPilotSeniority.JUNIOR, testDispatch.getPilot().getSeniority());
		assertEquals("CoPilot should be bottom level", AircraftPilotSeniority.JUNIOR, testDispatch.getCoPilot().getSeniority());
		for(int i = 0; i < _seatCost.length; i ++) {
			assertEquals("Seat cost for this section should be " + _seatCostSmall[i], 
					_seatCostSmall[i], testDispatch.getSeatCostPerSection()[i]);
		}
	}
}
