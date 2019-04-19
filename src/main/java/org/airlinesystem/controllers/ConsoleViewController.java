/**
 * ConsoleViewController class
 *		Controllers user display and input
 *		options available
 */

package org.airlinesystem.controllers;

import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import org.airlinesystem.controllers.logging.FullLogging;
import org.airlinesystem.helpers.AirlineSimulationBuilder;
import org.airlinesystem.model.AirlineSimulation;
import org.airlinesystem.model.AirlineSystemFileConstants;
import org.airlinesystem.view.*;
import org.airlinesystem.view.ConsoleView.MenuOption;
import org.airlinesystem.exceptions.*;

public class ConsoleViewController {

	private FullLogging viewControllerLog = FullLogging.getInstance();
	private boolean hasSimBeenRun = false;
	private ConsoleView consoleOut = new ConsoleView();
	private File propertiesFile;
	private File graphFile;
	private File dataFile; 
	private Scanner input = new Scanner(System.in);

	/**
	 * Main menu controller for user, handles all main menu input
	 * 
	 * @param fileNameList_
	 * 		String array that contains list of file names to be used
	 * @param simulation_
	 * 		AirlineSimulation object holding information for simulator to use
	 * @param simulator_
	 * 		AirlineSimulationBuilder object used to run simulation
	 * @return
	 * 		N/A
	 */
	public void menuController(String[] fileNameList_,
			AirlineSimulation simulation_, AirlineSimulationBuilder simulator_) {

		Path _pathToFile = Paths.get(fileNameList_[0]);
		propertiesFile = _pathToFile.toFile();
		_pathToFile = Paths.get(fileNameList_[1]);
		graphFile = _pathToFile.toFile();
		_pathToFile = Paths.get(fileNameList_[2]);
		dataFile = _pathToFile.toFile();
		
		MenuOption _selection;
		hasSimBeenRun = false;
		
		do {
			_selection = MenuOption.valueOf(String.valueOf(consoleOut.showMainMenu(input)));
			
			switch(_selection) {
				case INPUT_CUSTOM_FILES:
					getCustomFilePathsFromUser();
					break;
				case RUN_SIMULATION:
					runSimulation(simulation_, simulator_);
					break;
				case SHOW_RESULTS:
					showResults(simulation_);
					break;
				case FIND_AVG_PROFIT_BETWEEN_AIRPORTS:
					findAverageProfitForAirports(simulation_);
					break;
				case READ_FROM_DATA_FILE:
					readFromDataFile(simulation_, simulator_);
					break;
				case DISPLAY_GRAPH:
					displayGraph(simulation_);
					break;
				case QUIT_PROGRAM:
					break;		
			}
		} while(!_selection.equals(MenuOption.QUIT_PROGRAM));
		input.close();
	}
	
	/**
	 * Call prompt for user to input their own file paths.
	 * Check returned paths and if empty use defaults, 
	 * otherwise use user's path
	 * 
	 * @return
	 * 		N/A
	 */
	public void getCustomFilePathsFromUser() {
		
		Path _pathToFile;
		String[] fileNameList_ = consoleOut.promptUserForFileNames(input);
		
		if(fileNameList_[0] != null && !(fileNameList_[0].isEmpty())) {
			_pathToFile = Paths.get(fileNameList_[0]);
			propertiesFile = _pathToFile.toFile();
		} else {
			propertiesFile = new File(System.getProperty("user.dir") + 
					AirlineSystemFileConstants.AIRLINESYSTEM_DEFAULT_PROPERTIES);
		}					
		if(fileNameList_[1] != null && !(fileNameList_[1].isEmpty())) {
			_pathToFile = Paths.get(fileNameList_[1]);
			graphFile = _pathToFile.toFile();
		} else {
			graphFile = new File(System.getProperty("user.dir") + 
					AirlineSystemFileConstants.AIRLINESYSTEM_DEFAULT_GRAPH);
		}
		
		if(fileNameList_[2] != null && !(fileNameList_[2].isEmpty())) {
			_pathToFile = Paths.get(fileNameList_[2]);
			dataFile = _pathToFile.toFile();
		}
		else {
			dataFile = new File(System.getProperty("user.dir") + 
					AirlineSystemFileConstants.AIRLINESYSTEM_DEFAULT_DATA);
		}
		hasSimBeenRun = false;
	}
	
	/**
	 * Clear out any existing flights and graph, then
	 * run the simulator using properties and graph file
	 * for a simulation
	 * 
	 * @param simulation_
	 * 		AirlineSimulation object containing data to be used by simulator
	 * @param simulator_
	 * 		AirlineSimulationBuilder object used to run simulation
	 * @return
	 * 		N/A
	 */
	public void runSimulation(AirlineSimulation simulation_, AirlineSimulationBuilder simulator_) {
		
		simulation_.getListOfFlights().clear();
		simulation_.getGraphOfAirports().clearGraph();
		simulator_.runSimulation(propertiesFile, graphFile, simulation_);
		if(simulation_.getListOfFlights().size() != 0) {
			hasSimBeenRun = true;
		}
	}
	
	/**
	 * Display results only after a simulation has been run correctly
	 * 
	 * @param simulation_
	 * 		AirlineSimulation object containing data to be displayed
	 * @return
	 * 		N/A
	 */
	public void showResults(AirlineSimulation simulation_) {
		
		if(hasSimBeenRun) {
			consoleOut.resultsView(simulation_.getTotalProfit(), simulation_.getTotalCost(), 
					simulation_.getTotalRevenue(), simulation_.getListOfFlights().size(), 
					simulation_.getTotalProfit().divide(new BigDecimal(simulation_.getListOfFlights().size()),
							2, RoundingMode.FLOOR));
			}
			else {
				viewControllerLog.menuError("No simulation run, unable to show results\n");
			}
	}
	
	/**
	 * Call prompt for user to input airports and then find the average 
	 * profit between two airports and display it
	 * 
	 * @param simulation_
	 * 		AirlineSimulation object containing data to be used
	 * @return
	 * 		N/A
	 */
	public void findAverageProfitForAirports(AirlineSimulation simulation_) {
		
		if(hasSimBeenRun) {
			FlightRCPController _flightRCPManager = new FlightRCPController();
			String[] _airportNames = consoleOut.findAverageBetweenAirports(input);
			if(simulation_.getGraphOfAirports().areAirportsConnected(_airportNames[0], _airportNames[1])) {
				try {
					BigDecimal _averageProfit = _flightRCPManager.findAverageProfitPerEdge(simulation_.getListOfFlights(),
							simulation_.getGraphOfAirports(), _airportNames[0], _airportNames[1]);
					consoleOut.displayAverageBetweenAirports(_averageProfit);
				} catch(AirlineSystemException _e) {
					viewControllerLog.menuError("There are no flights between the two airports\n");
				}
			}
			else if(!(simulation_.getGraphOfAirports().getGraphOfAirports().containsVertex(_airportNames[0]))
					|| !(simulation_.getGraphOfAirports().getGraphOfAirports().containsVertex(_airportNames[1]))) {
				viewControllerLog.menuError("Airport input not present in graph, cannot find average\n");
			}
			else {
				viewControllerLog.menuError("Airports are not connected, cannot find average\n");
			}
		} 
		else {
			viewControllerLog.menuError("No simulation run, unable to find profit\n");
		}
	}
	
	/**
	 * Read from data file for information then use it to run a full simulation
	 * 
	 * @param simulation_
	 * 		AirlineSimulation object to be filled with data and used by simulator
	 * @param simulator_
	 * 		AirlineSimulationBuilder object used to run simulation
	 * @return
	 * 		N/A
	 */
	public void readFromDataFile(AirlineSimulation simulation_, AirlineSimulationBuilder simulator_) {
		
		simulation_.getListOfFlights().clear();
		simulation_.getGraphOfAirports().clearGraph();
		try {
			simulator_.runFromDataFile(propertiesFile, dataFile, simulation_);
			hasSimBeenRun = true;
		}
		catch (Exception e_) {
			hasSimBeenRun = false;
			viewControllerLog.menuError("\nError reading data, cannot run simulation\n");
		}
	}
	
	/**
	 * Display graph of the simulation if it has been run.
	 * Shows list of all airports and their connections.
	 * 
	 * @param simulation_
	 * 		AirlineSimulation object containing graph to be displayed
	 * @return
	 * 		N/A
	 */
	public void displayGraph(AirlineSimulation simulation_) {
		
		if(hasSimBeenRun) {
			simulation_.getGraphOfAirports().printGraph();
		}
		else {
			viewControllerLog.menuError("No simulation run, unable to display graph\n");
		}
	}
}
