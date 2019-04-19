package org.airlinesystem.helpers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.BeforeClass;
import java.io.File;
import java.io.IOException;

import org.airlinesystem.graphdb.impl.AirportGraph;

public class ReadGraphFromPSVTest {

	private static AirportGraph airportGraph;
	private static ReadGraphFromPSV readGraphTester;
	private static File graphFile;
	private static ExpectedException exception;

	@BeforeClass
	public static void initialize() {
		airportGraph = new AirportGraph();
		readGraphTester = new ReadGraphFromPSV();
		graphFile = new File("badGraphFile");
		exception = ExpectedException.none();
	}
	
	@Test 
	public void testIOException() throws IOException, Exception {
		exception.expect(IOException.class);
		readGraphTester.readFileInputIntoGraph(airportGraph, graphFile);
	}
	
	@Test
	public void testReadFileInputIntoGraph() {
		graphFile = new File("src/test/resources/loadTest-graph");
		try {
			readGraphTester.readFileInputIntoGraph(airportGraph, graphFile);
		}
		catch (Exception e_) {
			fail(e_.getMessage());
		}
	}
}
