package org.airlinesystem.helpers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;

import java.util.Properties;
import java.io.File;

import org.airlinesystem.controllers.RuntimePropertyController;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.helpers.GenerateModelData;
import org.airlinesystem.helpers.ReadModelDataIntoState;
import org.airlinesystem.model.FlightList;
import static org.airlinesystem.model.Aircraft.AircraftSize;

public class GenerateModelDataTest {

	private static AirportGraph airportGraph;
	private static GenerateModelData gen;
	private static Properties props;

	@BeforeClass
	public static void initialize() {

		props = new RuntimePropertyController().loadRuntimeProperties(new File("default.properties"));
		gen = new GenerateModelData();
		airportGraph = new AirportGraph();
		
		ReadModelDataIntoState _in = new ReadModelDataIntoState();

		try {
			_in.readFileInputIntoFlightList(new FlightList(), new File("src/test/resources/test-model-data"), 
					props, airportGraph);
		} catch(Exception e) {
		}

	}
	
	@Test
	public void testGenerateRandomSeatsFilled() {
		String[] _seatsFilled = gen.generateRandomSeatsFilled(props, AircraftSize.M).split("\\|");
		String[] _maxSeats = props.getProperty("MEDIUM_PLANE_SEAT_MAX_PER_SECTION").split("\\|");
		
		assertTrue("The seats filled for econ basic is between zero and max.", 
				0 < Integer.parseInt(_seatsFilled[0]) && Integer.parseInt(_seatsFilled[0]) < Integer.parseInt(_maxSeats[0]));
		
		assertTrue("The seats filled for first class is between zero and max.", 
				0 < Integer.parseInt(_seatsFilled[3]) && Integer.parseInt(_seatsFilled[3]) < Integer.parseInt(_maxSeats[3]));
		
	}

}
