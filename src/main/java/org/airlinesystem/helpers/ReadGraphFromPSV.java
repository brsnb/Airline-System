/**
 * ReadGraphFromPSV Class
 * 		Reads a file input line by line with | as delimiters
 * 		this information is then converted to needed data types
 * 		and adds the vertices (if not already present) and 
 * 		edge (if allowed)
 */

package org.airlinesystem.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.model.Airport;
import org.airlinesystem.exceptions.AirlineSystemException;
import org.airlinesystem.exceptions.IllegalGraphAdditionException;

public class ReadGraphFromPSV {

	private static final String DELIM = "|";
	private FullLogging readGraphLog = FullLogging.getInstance();

	/**
	 * Reads a given file where each line contains two vertices (airports) that have an edge
	 * between them and a weight (distance) between them
	 * 
	 * @param graphOfAirports_
	 * 		AirportGraph type object that will have any new vertices and edges added to the graph
	 * @param graphFileName_
	 * 		String of the file to attempt to open and read for the graph data
	 * @return
	 * 		N/A
	 */
	public void readFileInputIntoGraph(AirportGraph graphOfAirports_, File graphFile_) 
			throws AirlineSystemException, NullPointerException {
		readGraphLog.debugDebug("Reading graph input file");
		
		int _counter = 1;
		try (FileInputStream _is = new FileInputStream(graphFile_)) {
			InputStreamReader _sr = new InputStreamReader(_is);
			BufferedReader reader = new BufferedReader(_sr);
			reader.readLine();
			StringTokenizer tokenizer;
			String line = null;
			Airport _sourceAirport;
			Airport _destinationAirport;
			String _source;
			String _destination;
			double _distanceTravelled;
			while ((line = reader.readLine()) != null) {
				_counter++;
				if ("".equals(line)) {
					continue;
				}
				tokenizer = new StringTokenizer(line, DELIM);
				_source = tokenizer.nextToken().toUpperCase();
				_destination = tokenizer.nextToken().toUpperCase();
				_distanceTravelled = setDistanceTravelled(tokenizer.nextToken());
				_sourceAirport = new Airport(_source);
				_destinationAirport = new Airport(_destination);
				graphOfAirports_.addAirport(_sourceAirport);
				graphOfAirports_.addAirport(_destinationAirport);
				try {
					graphOfAirports_.createEdge(_source, _destination, _distanceTravelled);
				} catch(IllegalGraphAdditionException _e) {
					readGraphLog.debugDebug("Ignored at line " + Integer.toString(_counter) + " in file");
				}
			}
			readGraphLog.debugDebug("Successfully read graph file");
			reader.close();
		}
		catch (IOException e_) {
			throw new AirlineSystemException("could not read graph.\n");
		}
		catch (NullPointerException e_) {
			throw new AirlineSystemException("NullPointerException: Graph file error,"
					+ " could not find file on path- " + graphFile_.getAbsolutePath() + e_.getStackTrace());
		}

	}
	
	/**
	 * Reads a single 'edge' into the graph where each edge is two vertices (airports) with a weight
	 * 
	 * @param graphOfAirports_
	 * 		AirportGraph type object that will have any new edge added to the graph
	 * @param source_
	 * 		String of the first vertex
	 * @param destination_
	 * 		String of second vertex
	 * @param distanceTravelled_
	 * 		Double of the weight
	 * @return
	 * 		N/A
	 */
	public void readEdgeIntoGraph(AirportGraph graphOfAirports_, String source_,
			String destination_, double distanceTravelled_) {

		Airport _sourceAirport = new Airport(source_);
		Airport _destinationAirport = new Airport(destination_);
		
		graphOfAirports_.addAirport(_sourceAirport);
		graphOfAirports_.addAirport(_destinationAirport);
		try {
			graphOfAirports_.createEdge(source_, destination_, distanceTravelled_);
		} catch(IllegalGraphAdditionException _e) {
			readGraphLog.debugDebug("Ignored edge input");
		}
	}
	
	/**
	 * Finds the distance travelled by converting a passed String to a double
	 * 
	 * @param distanceTravelled_
	 * 		String to be converted into a double
	 * @return
	 * 		double that represented the distance travelled by a flight
	 */
	public double setDistanceTravelled(String distanceTravelled_) {
		return Double.parseDouble(distanceTravelled_);
	}
}
