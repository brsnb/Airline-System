package org.airlinesystem.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.airlinesystem.graphdb.impl.AirportGraph;
import org.jgrapht.graph.DefaultEdge;

public class FlightList extends ArrayList<Flight> {

	private HashMap<DefaultEdge, ArrayList<Flight>> mapEdgeToFlights;
	private static final long serialVersionUID = 4575157870451051348L;

	public FlightList() {
		mapEdgeToFlights = new HashMap<DefaultEdge, ArrayList<Flight>>();
	}


	public void addFlightToList(Flight flight_, AirportGraph graph_) {
		add(flight_);
		mapFlight(graph_, flight_, flight_.getSource().getName(), flight_.getDestination().getName());
	}
	
	public void mapFlight(AirportGraph airportGraph_, Flight addedFlight_, 
			String source_, String destination_) {
		
		DefaultEdge _testEdge = airportGraph_.getGraphOfAirports().getEdge(source_, destination_);

		if(mapEdgeToFlights.containsKey(_testEdge)) {
			mapEdgeToFlights.get(_testEdge).add(addedFlight_);

		} else {
			FlightList _list = new FlightList();
			_list.add(addedFlight_);
			mapEdgeToFlights.put(_testEdge, _list);
		}
	}
	
	public HashMap<DefaultEdge, ArrayList<Flight>> getFlightMap() {
		return mapEdgeToFlights;
	}
	
	@Override
	public void clear() {
		mapEdgeToFlights.clear(); 
		super.clear();
	}
	
}