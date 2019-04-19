package org.airlinesystem.helpers;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;
import java.math.BigDecimal;
import java.io.File;

import org.airlinesystem.controllers.RuntimePropertyController;
import org.airlinesystem.helpers.PilotBuilder;
import org.airlinesystem.model.AircraftPilot;
import static org.airlinesystem.model.AircraftPilot.AircraftPilotSeniority;
import static org.airlinesystem.model.Aircraft.AircraftSize;

public class PilotBuilderTest {

	private static RuntimePropertyController propManager;
	private static Properties testProps;
	private static PilotBuilder testPilotBuilder;
	
	@BeforeClass
	public static void initialize() {
		propManager = new RuntimePropertyController();
		testProps = propManager.loadRuntimeProperties(new File("default.properties"));
		testPilotBuilder = new PilotBuilder(testProps);
	}
	
	@Test
	public void testAssignPilotToAircraft() {	
		char _aircraftSize = 'L';
		AircraftPilot testPilot = testPilotBuilder.assignPilotToAircraft(AircraftSize.valueOf(String.valueOf(_aircraftSize)));
		assertEquals("Pilot for large aircraft should be senior level",
				AircraftPilotSeniority.SENIOR, testPilot.getSeniority());
		assertEquals("Pilot for large aircraft should have senior pay", 
				new BigDecimal (testProps.getProperty("SENIOR_PILOT_PAY")), testPilot.getCostPerFlight());
		
		_aircraftSize = 'M';
		testPilot = testPilotBuilder.assignPilotToAircraft(AircraftSize.valueOf(String.valueOf(_aircraftSize)));
		assertEquals("Pilot for medium aircraft should be mid level",
				AircraftPilotSeniority.MIDLEVEL, testPilot.getSeniority());
		assertEquals("Pilot for medium aircraft should have mid level pay",
				new BigDecimal (testProps.getProperty("MIDLEVEL_PILOT_PAY")), testPilot.getCostPerFlight());
		
		_aircraftSize = 'S';
		testPilot = testPilotBuilder.assignPilotToAircraft(AircraftSize.valueOf(String.valueOf(_aircraftSize)));
		assertEquals("Pilot for small aircraft should be junior level",
				AircraftPilotSeniority.JUNIOR, testPilot.getSeniority());
		assertEquals("Pilot for small aircraft should have junior pay",
				new BigDecimal(testProps.getProperty("JUNIOR_PILOT_PAY")), testPilot.getCostPerFlight());
	}
}
