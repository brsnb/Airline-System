package org.airlinesystem.helpers;

import static org.junit.Assert.*;
import org.airlinesystem.model.AirlineSimulation;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;

public class AirlineSimulationBuilderTest {

	private static AirlineSimulationBuilder testSimulation;
	private static File graphFile;
	private static File propertiesFile;
	
	@BeforeClass
	public static void initialize() {
		testSimulation = new AirlineSimulationBuilder();
		graphFile = new File("src/test/resources/loadTest-graph");
		propertiesFile = new File("src/test/resources/default.properties");
	}
	
	@Test
	public void testFullSimulation() {
		AirlineSimulation _sim = new AirlineSimulation();
		testSimulation.runSimulation(propertiesFile, graphFile, _sim);
	}
	
	@Test
	public void testDataSimulation() {
		AirlineSimulation _sim = new AirlineSimulation();
		try {
			testSimulation.runFromDataFile(propertiesFile, 
					new File("src/test/resources/loadTest-data"), _sim);
		}
		catch (Exception e_) {
			fail("Should read data file for information");
		}
	}
}
