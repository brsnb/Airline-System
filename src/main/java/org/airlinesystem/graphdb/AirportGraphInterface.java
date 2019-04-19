/**
 * AirportGraphInterface
 * 		Setup for graph interface to be implemented
 */

package org.airlinesystem.graphdb;

import java.util.ArrayList;
import java.util.HashMap;

import org.airlinesystem.model.Airport;
import org.airlinesystem.exceptions.IllegalGraphAdditionException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public interface AirportGraphInterface {

	/**
	 * Add a new airport by creating a new vertex on the graph
	 * based on the airport object input
	 * 
	 * @param airport_
	 * 		Airport object to be added to the graph and mapped with
	 * 		its name
	 * @return
	 * 		N/A
	 */
	void addAirport(Airport airport_);

	/**
	 * Add a new edge (flight) between vertices (airports) if they are not
	 * connected, not the same airport, and the distance is positive
	 * 
	 * @param source_
	 * 		String that represents name of first airport
	 * @param destination_
	 * 		String that represents name of second airport
	 * @param distance_
	 * 		double that represents distance between airports (weight)
	 * @return
	 * 		n/a
	 */
	void createEdge(String source_, String destination_, double distance_) throws IllegalGraphAdditionException;
	
	/**
	 * Finds the distance between two airports based on their names
	 * 
	 *  @param source_
	 *  	String of the first airport name
	 *  @param destination_
	 *  	String of the second airport name
	 *  @return
	 *  	double of the weight (distance) between two airports \
	 *  	if they are connected, otherwise 0
	 */
	double getDistance(String source_, String destination_);

	/**
	 * Remove a connection between two airports
	 * only if there exists a connection
	 * 
	 * @param source_
	 * 		String of the first airport name
	 * @param destination_
	 * 		String of the second airport name
	 * @return
	 * 		N/A
	 */
	void removeEdge(String source_, String destination_);

	/**
	 * Remove an airport from the graph and all connections
	 * only if it exists
	 * 
	 * @param aiport_
	 * 		String of the name of an airport to remove
	 * @return
	 * 		N/A
	 */
	void removeAirport(String airport_);

	/**
	 * Find if an airport is present in the graph
	 * 
	 * @param airport_
	 * 		String of the name of the airport to search for
	 * @return
	 * 		true if aiprot found, false otherwise
	 */
	boolean isAirportInGraph(String airport_);
	
	/**
	 * Find if two airports are directly connected
	 * 
	 * @param source_
	 * 		String of the name of the first airport
	 * @param destination_
	 * 		String of the name of the second airport
	 * @return
	 * 		true, if airports are connected, false otherwise
	 */
	boolean areAirportsConnected(String source_, String destination_);
	
	/**
	 * Returns airport object from hash map based on name
	 * 
	 * @param airportName_
	 * 		name of the airport wanted
	 * @return
	 * 		Airport object that corresponds to the name given
	 */
	Airport getAirport(String airportName_);

	
	/**
	 * 	Sorts edges in ascending order
	 * 
	 * @return
	 * 		ArrayList<DefaultEdge> that is all edges sorted in ascending order
	 */
	ArrayList<DefaultEdge> getSortedListOfEdges();
	
	/**
	 * Prints the current graph of the airports by iterating
	 * through the set of vertices and each of their edges
	 * 
	 * @return
	 * 		N/A
	 */
	void printGraph();

	/**
	 * Completely empties the graph and any airport mappings
	 * 
	 * @return
	 * 		N/A
	 */
	void clearGraph();

	Graph<String, DefaultEdge> getGraphOfAirports();

	HashMap<String, Airport> getMapAirportToName();
	
}
