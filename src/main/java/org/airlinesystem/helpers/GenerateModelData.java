/**
 * GenerateModelData class
 * 
 *  Generates the data for the airport system based on the configuration file and 
 *  airport graph provided. This is intended to simulate the data from a basic
 *  POS system.
 */

package org.airlinesystem.helpers;

import java.util.Random;
import java.util.Properties;
import java.util.ArrayList;

import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.model.FlightList;
import static org.airlinesystem.model.Aircraft.AircraftSize;
import org.jgrapht.graph.*;

public class GenerateModelData {

	private Random rand = new Random();
	private FullLogging generateDataLog = FullLogging.getInstance();

	/**
	 *  Chooses a random edge from the graph to act as the path for a flight.
	 *  
	 *  @param edgeList_ The list of sorted edges returned by the sort method
	 *  @param preferredAirplaneSize_ AirplaneSize enum value indicating the
	 *  	   size of the airplane to create more of
	 *  		
	 *  @return The randomly selected edge
	 */
	public DefaultEdge getRandomEdge(ArrayList<DefaultEdge> edgeList_, AircraftSize preferredAirplaneSize_) {
		double _weight = 0.5;
		int _selection = 0;	
		
		switch(preferredAirplaneSize_) {
			case S:
				_weight = 0.25;
				break;
			case M:
				_weight = 0.5;
				break;
			case L:
				_weight = 0.75;
				break;
			default:
				return edgeList_.get(rand.nextInt(edgeList_.size()));
		}

		// Generate an index based on a binomial distribution weighted by the distance
		for(int _i = 0; _i < edgeList_.size() - 1; _i++) {
			if(rand.nextDouble() < _weight) {
				_selection++;
			}
		}
		
		// Saving non-binomial distribution
		//int _selection = (int)(edgeList_.size() * Math.pow(rand.nextDouble(), _weight));

		return edgeList_.get(_selection);
	}

	/**
	 *  Generate the amount of seats filled in each section on an airplane
	 *  for a certain flight. This depends on the flight size and the information
	 *  is loaded from the properties file.
	 *  
	 *  @param modelProperties_ The properties file that describes the current model being 
	 *  						tested
	 *  @param airplaneSize_    The size of the airplane to generate the seat list for
	 *  @return a pipe separated String of the form 
	 *  						 "econ basic seats filled | econ plus seats filled | business
	 *  						 seats filled | first class seats filled"
	 */
	public String generateRandomSeatsFilled(Properties modelProperties_, AircraftSize airplaneSize_) {
		String genString_ = null;
		String[] _maxSeats;

		try {
			int _econBasic;
			int _econPlus;
			int _business;
			int _first;
		
			switch(airplaneSize_) {
				case S:
					_maxSeats = modelProperties_.getProperty("SMALL_PLANE_SEAT_MAX_PER_SECTION").split("\\|");
					

					if(!_maxSeats[0].equals("0")) {
						_econBasic = rand.nextInt(Integer.parseInt(_maxSeats[0]));
					} else {
						_econBasic = 0;
					}
					if(!_maxSeats[1].equals("0")) {
						_econPlus = rand.nextInt(Integer.parseInt(_maxSeats[1]));
					} else {
						_econPlus = 0;
					}
					if(!_maxSeats[2].equals("0")) {
						_business = rand.nextInt(Integer.parseInt(_maxSeats[2]));	
					} else {
						_business = 0;
					}
					if(!_maxSeats[3].equals("0")) {
						_first = rand.nextInt(Integer.parseInt(_maxSeats[3]));
					} else {
						_first = 0;
					}
	
					genString_ = String.format("%d|%d|%d|%d", _econBasic, _econPlus, _business, _first);
					break;
	
				case M:
					_maxSeats = modelProperties_.getProperty("MEDIUM_PLANE_SEAT_MAX_PER_SECTION").split("\\|");
	
					if(!_maxSeats[0].equals("0")) {
						_econBasic = rand.nextInt(Integer.parseInt(_maxSeats[0]));
					} else {
						_econBasic = 0;
					}
					if(!_maxSeats[1].equals("0")) {
						_econPlus = rand.nextInt(Integer.parseInt(_maxSeats[1]));
					} else {
						_econPlus = 0;
					}
					if(!_maxSeats[2].equals("0")) {
						_business = rand.nextInt(Integer.parseInt(_maxSeats[2]));	
					} else {
						_business = 0;
					}
					if(!_maxSeats[3].equals("0")) {
						_first = rand.nextInt(Integer.parseInt(_maxSeats[3]));
					} else {
						_first = 0;
					}
	
					genString_ = String.format("%d|%d|%d|%d", _econBasic, _econPlus, _business, _first);
					break;
	
				case L:
					_maxSeats = modelProperties_.getProperty("LARGE_PLANE_SEAT_MAX_PER_SECTION").split("\\|");
	
					if(!_maxSeats[0].equals("0")) {
						_econBasic = rand.nextInt(Integer.parseInt(_maxSeats[0]));
					} else {
						_econBasic = 0;
					}
					if(!_maxSeats[1].equals("0")) {
						_econPlus = rand.nextInt(Integer.parseInt(_maxSeats[1]));
					} else {
						_econPlus = 0;
					}
					if(!_maxSeats[2].equals("0")) {
						_business = rand.nextInt(Integer.parseInt(_maxSeats[2]));	
					} else {
						_business = 0;
					}
					if(!_maxSeats[3].equals("0")) {
						_first = rand.nextInt(Integer.parseInt(_maxSeats[3]));
					} else {
						_first = 0;
					}
	
					genString_ = String.format("%d|%d|%d|%d", _econBasic, _econPlus, _business, _first);
					break;
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return genString_;
	}

	/**
	 *  This ties all of the random information and information from the properties file
	 *  into a single string to be parsed into a single Flight.
	 *  
	 *  @param modelProperties_ The properties file that describes the current model being 
	 *  						tested.
	 *  @param airportGraph_	The AirportGraph of the current model
	 *  @return The pipe separated string value representation of the Flight. 
	 */
	public String generateRandomFlight(Properties modelProperties_, AirportGraph airportGraph_, 
			ArrayList<DefaultEdge> sortedEdges_) {

		AircraftSize _aircraftSize;
		String _maxSeatsPerSection;
		String _seatPricePerSection;
		
		DefaultEdge _randomEdge = getRandomEdge(sortedEdges_, 
				AircraftSize.valueOf(modelProperties_.getProperty("PREFERRED_AIRCRAFT_SIZE").toUpperCase()));
		String _source = airportGraph_.getGraphOfAirports().getEdgeSource(_randomEdge);
		String _dest = airportGraph_.getGraphOfAirports().getEdgeTarget(_randomEdge);
	
		double _distance = airportGraph_.getGraphOfAirports().getEdgeWeight(_randomEdge);
	
		if(_distance < Double.parseDouble(modelProperties_.getProperty("SMALL_PLANE_MAX_RANGE"))) {
			_aircraftSize = AircraftSize.S;
			_maxSeatsPerSection = modelProperties_.getProperty("SMALL_PLANE_SEAT_MAX_PER_SECTION");
			_seatPricePerSection = modelProperties_.getProperty("SMALL_PLANE_SEAT_PRICE");

		} else if (_distance < Double.parseDouble(modelProperties_.getProperty("MEDIUM_PLANE_MAX_RANGE"))) {
			_aircraftSize = AircraftSize.M;
			_maxSeatsPerSection = modelProperties_.getProperty("MEDIUM_PLANE_SEAT_MAX_PER_SECTION");
			_seatPricePerSection = modelProperties_.getProperty("MEDIUM_PLANE_SEAT_PRICE");
		} else {
			_aircraftSize = AircraftSize.L;
			_maxSeatsPerSection = modelProperties_.getProperty("LARGE_PLANE_SEAT_MAX_PER_SECTION");
			_seatPricePerSection = modelProperties_.getProperty("LARGE_PLANE_SEAT_PRICE");
		}

		String _seatsFilledPerSection = generateRandomSeatsFilled(modelProperties_, _aircraftSize);
				
		String _flight = String.format("%s|%s|%f|%s|%s|%s|%s", _source, _dest, 
				_distance, _aircraftSize.toString(), _maxSeatsPerSection, 
				_seatsFilledPerSection, _seatPricePerSection);
		
		generateDataLog.debugDebug("Model gen string output: " + _flight);		
		
		return _flight;
	}

	/**
	 *  Runs through the total number of flights to be created by the current model and
	 *  reads them into the state.
	 *  
	 *  @param modelProperties_ The properties file that describes the current model being 
	 *  						tested
	 *  @param airportGraph_	The AirportGraph of the current model
	 *  @param listOfFlights_   The FlightList that represents all of the flights created 
	 *  						for this run of the model
	 *  @param flightInput_		The object that takes the psv Flight data and parses it into 
	 *  						the current state
	 *  @return N/A
	 */
	public void generateCurrentStateModel(Properties modelProperties_, AirportGraph airportGraph_,
			FlightList listOfFlights_, ReadModelDataIntoState flightInput_) {

		int _flightsNeeded = Integer.parseInt(modelProperties_.getProperty("NUMBER_OF_FLIGHTS"));
		String _flightData;
		ArrayList<DefaultEdge> _sortedEdges;
		
		_sortedEdges = airportGraph_.getSortedListOfEdges();

		for (int i = 0; i < _flightsNeeded; i++)
		{
			_flightData = generateRandomFlight(modelProperties_, airportGraph_, _sortedEdges);
			flightInput_.readSingleFlightIntoFlightList(listOfFlights_, _flightData, modelProperties_, airportGraph_);
		}
	}	
}