/**
 * AirlineSimulationBuilder class
 *		Runs a full simulation with calculations
 */

package org.airlinesystem.helpers;

import org.airlinesystem.controllers.FlightRCPController;
import org.airlinesystem.controllers.RuntimePropertyController;
import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.graphdb.impl.AirportGraph;
import org.airlinesystem.model.AirlineSimulation;
import org.airlinesystem.model.FlightList;
import org.airlinesystem.exceptions.AirlineSystemException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Properties;
import java.io.File;

public class AirlineSimulationBuilder {

	private FullLogging simulationBuilderLog = FullLogging.getInstance();
	
	/**
	 * Attempts to process a graph file given a name and graph to process it into
	 * by call on the class for reading graphs
	 * 
	 * @param graphOfAirports_
	 * 		AirportGraph type object that will have any new vertices and edges added to the graph
	 * @param graphFileName_
	 * 		String of the file to attempt to open and read for the graph data
	 * @return
	 * 		N/A
	 */
	public void processGraph(AirportGraph graphOfAirports_, File graphFile_) throws AirlineSystemException {
		ReadGraphFromPSV _graphInput = new ReadGraphFromPSV();
		try {
			_graphInput.readFileInputIntoGraph(graphOfAirports_, graphFile_);
			simulationBuilderLog.debugDebug("Graph successfully read");
		}
		catch(AirlineSystemException e_) {
			throw new AirlineSystemException(e_);
		}
	}
	
	/**
	 * Generates data to fill a flightList with the number of flights taken from properties
	 * 
	 * @param modelProperties_
	 * 		Properties used as guidelines for the amount and variety of data to generate
	 * @param graphOfAirports_
	 * 		AirportGraph used to select connected airports for flights
	 * @param listOfFlights_
	 * 		FlightList that will be filled with generated flights 
	 * @return
	 * 		N/A
	 */
	public void generateData(Properties modelProperties_, AirportGraph graphOfAirports_,
			FlightList listOfFlights_) throws AirlineSystemException{

		ReadModelDataIntoState _flightInput = new ReadModelDataIntoState();
		GenerateModelData _dataCreator = new GenerateModelData();

		try {
			_dataCreator.generateCurrentStateModel(modelProperties_, graphOfAirports_,
					listOfFlights_, _flightInput);
			simulationBuilderLog.debugDebug("Generated data");
		}
		catch (Exception e_) {
			throw new AirlineSystemException("Error, cannot generate data.\n", e_);
		}
	}
	
	/**
	 * Attempts to find the total revenue, cost, and profit of an entire flight list
	 * 
	 * @param listOfFlights_
	 * 		FlightList containing all the flights to be included in calculations
	 * @param modelProperties_
	 * 		Properties object that holds property file information
	 * @return
	 * 		BigDecimal array holding calculated total revenue, cost and profit of flights
	 */
	public BigDecimal[] findTotalRCP(FlightList listOfFlights_, Properties modelProperties_) throws AirlineSystemException {

		FlightRCPController _flightProfitManager = new FlightRCPController(modelProperties_);
		BigDecimal[] _totalRCP;
		
		try {		
			_totalRCP = _flightProfitManager.findTotalRCPOfFlightList(listOfFlights_);
			return _totalRCP;
		}
		catch (Exception e_) {
			throw new AirlineSystemException("Error calculating total RCP", e_);
		}
	
	}
	
	/**
	 * Attempts to simulate by processing graph and properties then generating data
	 * and finding results from that data
	 * 
	 * @param propertiesFileName_
	 * 		String of the file to attempt to open and read for the properties data
	 * @param graphFileName_
	 * 		String of the file to attempt to open and read for the graph data
	 * @param simulation_
	 * 		AirlineSimulation object of the simulation itself, holds the other objects used
	 * @return
	 * 		N/A
	 */
	public void runSimulation(File propertiesFile_, File graphFile_,  
			AirlineSimulation simulation_) {

		RuntimePropertyController _propertyController = new RuntimePropertyController();
		Properties _modelProperties = _propertyController.loadRuntimeProperties(propertiesFile_);
		simulation_.setSimulationProperties(_modelProperties);

		simulationBuilderLog.menuInfo("Calculating flight results...\n");
		simulationBuilderLog.debugDebug("runSimulation");
				
		try {
			processGraph(simulation_.getGraphOfAirports(), graphFile_);
		}
		catch (Exception e_) {
			simulationBuilderLog.menuError(e_.getMessage());
			StackTraceElement l = e_.getStackTrace()[0];
			simulationBuilderLog.debugError(e_.getMessage() + "\n" + l.getClassName() 
					+ "/" + l.getMethodName() + ":" + l.getLineNumber());
		}

		try {
			generateData(_modelProperties, simulation_.getGraphOfAirports(), 
					simulation_.getListOfFlights());
			BigDecimal[] arrayOfRCP;
			arrayOfRCP = findTotalRCP(simulation_.getListOfFlights(), _modelProperties);
			simulation_.setTotalRevenue(arrayOfRCP[0]);
			simulation_.setTotalCost(arrayOfRCP[1]);
			simulation_.setTotalProfit(arrayOfRCP[2]);

			NumberFormat _numberFormatter = NumberFormat.getInstance();
			simulationBuilderLog.resultsInfo("Total Profit = $" + 
			_numberFormatter.format(arrayOfRCP[2]));
			simulationBuilderLog.menuInfo("Flights successfully created\n");
		}
		catch (Exception e_) {
			simulationBuilderLog.menuError(e_.getMessage());
			StackTraceElement l = e_.getStackTrace()[0];
			simulationBuilderLog.debugError(e_.getMessage() + "\n" + l.getClassName() 
					+ "/" + l.getMethodName() + ":" + l.getLineNumber());
		}
	}
	
	/**
	 * Attempts to find results by using a data file already filled with flight
	 * information to find results
	 * 
	 * @param propertiesFileName_
	 * 		String of the file to attempt to open and read for the properties data
	 * @param dataFileName_
	 * 		String of the file to attempt to open and read for the flight data
	 * @param simulation_
	 * 		AirlineSimulation object holding other objects to be used
	 * @return
	 * 		N/A
	 */
	public void runFromDataFile(File propertiesFile_, File dataFile_, AirlineSimulation simulation_) 
		throws AirlineSystemException {

		simulationBuilderLog.menuInfo("Reading data file...\n");

		ReadModelDataIntoState _readData = new ReadModelDataIntoState();
		RuntimePropertyController _propertyController = new RuntimePropertyController();
		Properties _modelProperties = _propertyController.loadRuntimeProperties(propertiesFile_);

		BigDecimal[] _arrayOfRCP;

		try {
			_readData.readFileInputIntoFlightList(simulation_.getListOfFlights(), dataFile_,
					_modelProperties, simulation_.getGraphOfAirports());
			_arrayOfRCP = findTotalRCP(simulation_.getListOfFlights(), _modelProperties);
			simulation_.setTotalRevenue(_arrayOfRCP[0]);
			simulation_.setTotalCost(_arrayOfRCP[1]);
			simulation_.setTotalProfit(_arrayOfRCP[2]);
			simulationBuilderLog.menuInfo("Flights successfully created\n");
		}
		catch (AirlineSystemException e_) {
			simulationBuilderLog.menuError(e_.getMessage());
			StackTraceElement l = e_.getStackTrace()[0];
			simulationBuilderLog.debugError(e_.getMessage() + "\n" + l.getClassName() 
					+ "/" + l.getMethodName() + ":" + l.getLineNumber());
			throw new AirlineSystemException(e_);
		}
	}
}
