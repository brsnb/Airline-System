/**
 * ReadModelDataIntoState
 * 		Reads data into a given flight list either from
 * 		an entire file or a single string
 */

package org.airlinesystem.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.StringTokenizer;

import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.exceptions.AirlineSystemException;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.model.FlightList;
import static org.airlinesystem.model.Aircraft.AircraftSize;
import org.airlinesystem.helpers.FlightBuilder;
import org.airlinesystem.model.Flight;
import java.io.IOException;

public class ReadModelDataIntoState {
	
	private static final String DELIM = "|";
	private FullLogging readDataLog = FullLogging.getInstance();
	
	/**
	 * Read information from the input file then sends that information properly
	 * formatted to be added to the FlightList line by line
	 * NOTE: First line ignored (assumed to be outline of data input)
	 * 
	 * @param listOfFlights_
	 * 		FlightList type object that is the list that will have the flights
	 * 		added to it
	 * @param fileToRead_
	 * 		String that tells what file should be attempted to be opened and read
	 * @param modelProperties_
	 * 		Properties type object that will be passed to the FlightList so that
	 * 		it can be used by the flightBuilder
	 * @return
	 * 		N/A
	 */
	public void readFileInputIntoFlightList(FlightList listOfFlights_, 
			File fileToRead_, Properties modelProperties_, AirportGraph airportGraph_)
			throws AirlineSystemException {
		readDataLog.debugDebug("Reading input file");
		
		ReadGraphFromPSV _addEdgeToGraph = new ReadGraphFromPSV();
		FlightBuilder _flightBuilder = new FlightBuilder();
		int[] _maxSeatsPerSection = new int [4];
		int[] _seatsFilledPerSection = new int [4];
		BigDecimal[] _seatCostPerSection = new BigDecimal [4];
		
		try (InputStream _is = new FileInputStream(fileToRead_)) {
			InputStreamReader _sr = new InputStreamReader(_is);
			BufferedReader reader = new BufferedReader(_sr);
			reader.readLine();
			StringTokenizer tokenizer;
			String line = null;
			String _source;
			String _destination;
			double _distanceTravelled;
			Flight _flight;
			AircraftSize _aircraftSize;
			READER_LOOP:
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				if ("".equals(line)) {
					continue;
				}
				
				tokenizer = new StringTokenizer(line, DELIM);
				_source = tokenizer.nextToken();
				_destination = tokenizer.nextToken();
				_distanceTravelled = setDistanceTravelled(tokenizer.nextToken());
				_aircraftSize = AircraftSize.valueOf(tokenizer.nextToken().toUpperCase());
				
				for (int i = 0; i < 4; i++) {
					_maxSeatsPerSection[i] = setMaxSeatsPerSection(tokenizer.nextToken());
				}
				for (int i = 0; i < 4; i++) {
					_seatsFilledPerSection[i] = setSeatsFilledPerSection(tokenizer.nextToken());
					if (_seatsFilledPerSection[i] > _maxSeatsPerSection[i]) {
						readDataLog.debugDebug("Ignored invalid input in data");
						continue READER_LOOP;
					}
				}
				for (int i = 0; i < 4; i++) {
					_seatCostPerSection[i] = setSeatCostPerSection(tokenizer.nextToken());
				}
				_addEdgeToGraph.readEdgeIntoGraph(airportGraph_, _source, _destination, _distanceTravelled);
				_flight = _flightBuilder.flightDispatchService(_aircraftSize, _maxSeatsPerSection, 
						_seatsFilledPerSection, _seatCostPerSection, _source, _destination, _distanceTravelled, modelProperties_);
				listOfFlights_.addFlightToList(_flight, airportGraph_);
			}
			readDataLog.debugDebug("Successfully read file");
			reader.close();
		}
		catch (IOException e_) {
			throw new AirlineSystemException("IOException: could not read data");
		}
		catch (NullPointerException e_) {
			throw new AirlineSystemException("NullPointerException: data file error,"
					+ " could not find file- " + fileToRead_.getName());
		}
		catch (Exception e_) {
			throw new AirlineSystemException("Unexpected error occured:" + e_.getStackTrace());
		}
	}
	
	/**
	 * Read information from a String then formats it and sends it to a FlightList
	 * 
	 * @param listOfFlights_
	 * 		FlightList type object that is the list that will have the flights
	 * 		added to it
	 * @param flightInformation_
	 * 		String that contains the information needed for a flight
	 * @param modelProperties_
	 * 		Properties type object that will be passed to the FlightList so that
	 * 		it can be used by the flightBuilder
	 * @return
	 * 		N/A
	 * */
	public void readSingleFlightIntoFlightList(FlightList listOfFlights_, 
			String flightInformation_, Properties modelProperties_, AirportGraph airportGraph_) {
		
		flightInformation_ = flightInformation_.replaceAll("\\s", "");
		FlightBuilder _flightBuilder = new FlightBuilder();
		int[] _maxSeatsPerSection = new int [4];
		int[] _seatsFilledPerSection = new int [4];
		BigDecimal[] _seatCostPerSection = new BigDecimal [4];
		StringTokenizer tokenizer = new StringTokenizer(flightInformation_, DELIM);
		
		String _source = tokenizer.nextToken();
		String _destination = tokenizer.nextToken();
		double _distanceTravelled = setDistanceTravelled(tokenizer.nextToken());
		AircraftSize _aircraftSize = AircraftSize.valueOf(tokenizer.nextToken().toUpperCase());
		
		for (int i = 0; i < 4; i++) {
			_maxSeatsPerSection[i] = setMaxSeatsPerSection(tokenizer.nextToken());
		}
		for (int i = 0; i < 4; i++) {
			_seatsFilledPerSection[i] = setSeatsFilledPerSection(tokenizer.nextToken());
		}
		for (int i = 0; i < 4; i++) {
			_seatCostPerSection[i] = setSeatCostPerSection(tokenizer.nextToken());
		}
		Flight _flight = _flightBuilder.flightDispatchService(_aircraftSize, _maxSeatsPerSection,
				_seatsFilledPerSection, _seatCostPerSection, _source, _destination, _distanceTravelled, modelProperties_);
		listOfFlights_.addFlightToList(_flight, airportGraph_);
	}
	
	/**
	 * Finds the distance travelled by converting a passed String to a double
	 * 
	 * @param distanceTravelled_
	 * 		String to be converted into a double
	 * @return
	 * 		double that represented the distance travelled by a flight
	 * */
	public double setDistanceTravelled(String distanceTravelled_) {
		return Double.parseDouble(distanceTravelled_);
	}
	
	/**
	 * Finds the aircraftSize that a flight will use by taking the first
	 * character of a passed String and returning it
	 * 
	 * @param aircraftSize_
	 * 		String to be converted into a char
	 * @return
	 * 		character that represented the size of the aircraft to use for the
	 * 		flight (s, m, l)
	 *
	public char setAircraftSize(String aircraftSize_) {
		
		return aircraftSize_.charAt(0);
	}
    */

	/**
	 * Finds the max seats in a section by converting a passed String to an integer
	 * 
	 * @param maxSeatsInSection_
	 * 		String to be converted into an integer
	 * @return
	 * 		integer that represented the max number of seats in a section
	 * 		for the flight
	 */
	public int setMaxSeatsPerSection(String maxSeatsInSection_) {
		return Integer.parseInt(maxSeatsInSection_);
	}
	
	/**
	 * Finds the number of seats filled in a section by converting 
	 * a passed String to an integer
	 * 
	 * @param seatsFilledInSection_
	 * 		String to be converted into an integer
	 * @return
	 * 		integer that represented the number of seats filled
	 * 		in a section for the flight
	 */
	public int setSeatsFilledPerSection(String seatsFilledInSection_) {
		return Integer.parseInt(seatsFilledInSection_);
	}
	
	/**
	 * Finds the cost of an individual seat in a section by converting 
	 * a passed String to a BigDecimal
	 * 
	 * @param seatCostInSection_
	 * 		String to be converted into a BigDecimal
	 * @return
	 * 		BigDecimal type that represented the cost for a seat
	 * 		in a section for the flight
	 */
	public BigDecimal setSeatCostPerSection(String seatCostInSection_) {
		BigDecimal _decimalReturn = new BigDecimal(seatCostInSection_);
		return _decimalReturn;
	}
}
