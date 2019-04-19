package org.airlinesystem.graphdb.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.model.Airport;
import org.airlinesystem.exceptions.IllegalGraphAdditionException;
import org.jgrapht.graph.DefaultEdge;

public class AirportGraphTest {
	
	private AirportGraph graphOfAirports;
	
	@Before
	public void ObjectsInit() {
		graphOfAirports = new AirportGraph();
		graphOfAirports.addAirport(new Airport("A"));
		graphOfAirports.addAirport(new Airport("B"));
		graphOfAirports.addAirport(new Airport("D"));
		graphOfAirports.addAirport(new Airport("E"));
	}
	@Test
	public void testEdgeFunctions() {
		try {
			graphOfAirports.createEdge("A", "B", 7);
		} catch(IllegalGraphAdditionException _e) {
			fail("Should have created edge between A and B");
		}
		try {
			graphOfAirports.createEdge("A", "B", 300);
			fail("Should not create edge from A to B again");
		} catch(IllegalGraphAdditionException _e) {
		}
		try { 
			graphOfAirports.createEdge("B", "A", 450);
			fail("Should not create edge from B to A again");
		} catch(IllegalGraphAdditionException _e) {
		}
		try { 
			graphOfAirports.createEdge("B", "D", -20);
			fail("Should not create edge from B to D with negative distance");
		} catch(IllegalGraphAdditionException _e) {
		}
		try { 
			graphOfAirports.createEdge("B", "D", 500);
		} catch(IllegalGraphAdditionException _e) {
			fail("Should create edge from B to D");
		}
		try {
			graphOfAirports.createEdge("B", "C", 250);
			fail("Should not create edge from B to C when C doesn't exist");
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("E", "A", 0);
			fail("Should not create edge from E to A with distance of 0");
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("A", "A", 340);
			fail("Should not create edge from A to A");
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("D", "A", 237);
		} catch(IllegalGraphAdditionException _e) {
			fail("Should create edge from D to A");
		}
		
		assertTrue("Should be an edge from A to B", graphOfAirports.areAirportsConnected("A", "B"));
		assertTrue("Should be an edge from B to A", graphOfAirports.areAirportsConnected("B", "A"));
		assertFalse("Should not be an edge from A to A", graphOfAirports.areAirportsConnected("A", "A"));
		
		assertEquals("Check distance from A to B is 7.0", 7.0, 
				graphOfAirports.getDistance("A", "B"), 0.01);
		assertEquals("Check distance from A to D is 237.0", 237.0,
				graphOfAirports.getDistance("A", "D"), 0.01);
		assertEquals("Check distance from D to A is 237.0", 237.0,
				graphOfAirports.getDistance("D", "A"), 0.01);
		assertEquals("Check distance from A to E is 0.0", 0.0,
				graphOfAirports.getDistance("A", "E"), 0.01);
		
		graphOfAirports.removeEdge("A", "B");
		
		assertFalse("Should not be an edge from A to B", 
				graphOfAirports.areAirportsConnected("A", "B"));
		assertFalse("Should not be an edge from B to A", 
				graphOfAirports.areAirportsConnected("B", "A"));
		assertEquals("Check distance between A and B is 0.0", 0.0,
				graphOfAirports.getDistance("A", "B"), 0.01);
	}
	
	@Test
	public void testAirportFunctions() {
		graphOfAirports.removeAirport("D");
		assertFalse("D should no longer be in the graph", graphOfAirports.isAirportInGraph("D"));
		assertFalse("D and A should not be connected", graphOfAirports.areAirportsConnected("D", "A"));
		assertFalse("A and D should not be connected", graphOfAirports.areAirportsConnected("A", "D"));
		graphOfAirports.addAirport(new Airport("D"));
		assertTrue("D should be in the graph", graphOfAirports.isAirportInGraph("D"));
		assertFalse("D and A should not be connected", graphOfAirports.areAirportsConnected("D", "A"));
		assertFalse("A and D should not be connected", graphOfAirports.areAirportsConnected("A", "D"));

		assertTrue("Airport A is in graph",graphOfAirports.isAirportInGraph("A"));
		assertFalse("Airport C is not in graph",graphOfAirports.isAirportInGraph("C"));
		graphOfAirports.addAirport(new Airport("C"));
		assertTrue("Airport C is now in graph after adding it",graphOfAirports.isAirportInGraph("C"));
		
		assertFalse("Airport A should not be connected to itself", 
				graphOfAirports.areAirportsConnected("A", "A"));
		assertEquals("Hashmap should return airport with correct name field", "A", 
				graphOfAirports.getAirport("A").getName());
	}
	
	@Test
	public void testGetSortedListOfEdges() {
		try {
			graphOfAirports.createEdge("A", "B", 5);
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("A", "D", 3);
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("B", "D", 4);
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("A", "E", 1);
		} catch(IllegalGraphAdditionException _e) {
		}
		try {
			graphOfAirports.createEdge("D", "E", 2);
		} catch(IllegalGraphAdditionException _e) {
		}
		
		ArrayList<DefaultEdge>	_sortedEdges;
		
		_sortedEdges = graphOfAirports.getSortedListOfEdges();
		
		assertTrue("Index 0 less than index 1",
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(0)) <=
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(1)));
		assertTrue("Index 1 less than index 2",
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(1)) <=
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(2)));
		assertTrue("Index 2 less than index 3",
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(2)) <=
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(3)));
		assertTrue("Index 3 less than index 4",
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(3)) <=
				graphOfAirports.getGraphOfAirports().getEdgeWeight(_sortedEdges.get(4)));
	}
}
