package org.airlinesystem.controllers;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.io.File;

import org.airlinesystem.controllers.RuntimePropertyController;
import org.airlinesystem.exceptions.AirlineSystemException;

public class RuntimePropertyControllerTest {

	private RuntimePropertyController propController;
	private Properties testProperties;
	
	@Before
	public void objectsInit() {
		propController = new RuntimePropertyController();
		testProperties = null;
	}
	@Test
	public void testLoadDefaultProperties() {
		testProperties = propController.loadRuntimeProperties(new File("default.properties"));
		
		// Check a few of the default file properties to assure they are loaded correctly
		assertEquals("15", testProperties.getProperty("FUEL_COST"));
		assertEquals("100", testProperties.getProperty("NUMBER_OF_FLIGHTS"));
		assertEquals("150|100|100|50", testProperties.getProperty("LARGE_PLANE_SEAT_MAX_PER_SECTION"));
	}
	
	@Test
	public void testCreateRuntimeProperiesWithValidFile() {
		File _file = new File("src/test/resources/loadTest.properties");
		
		testProperties = propController.loadRuntimeProperties(_file);

		// Check a few of the default file properties to assure they are loaded correctly
		assertEquals("15", testProperties.getProperty("FUEL_COST"));
		assertEquals("100", testProperties.getProperty("NUMBER_OF_FLIGHTS"));
		assertEquals("150|100|100|50", testProperties.getProperty("LARGE_PLANE_SEAT_MAX_PER_SECTION"));
		
	}

	@Test
	public void testCreateRuntimePropertiesWithInvalidFile() {
		// Check that it throws the exceptions
		try {
			testProperties = propController.createRuntimeProperties(new File("this is not a file"));
			fail("createRuntimeProperties did not throw exception properly");
		} catch(AirlineSystemException _e) {
		}
	}

	@Test
	public void testLoadRuntimeProperties() {
		File _file = new File("src/test/resources/loadTest.properties");
		
		testProperties = propController.loadRuntimeProperties(_file);

		// Check a few of the default file properties to assure they are loaded correctly
		assertEquals("15", testProperties.getProperty("FUEL_COST"));
		assertEquals("100", testProperties.getProperty("NUMBER_OF_FLIGHTS"));
		assertEquals("150|100|100|50", testProperties.getProperty("LARGE_PLANE_SEAT_MAX_PER_SECTION"));
	}
}
