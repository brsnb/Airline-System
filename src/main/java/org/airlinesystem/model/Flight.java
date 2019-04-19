package org.airlinesystem.model;

import java.math.BigDecimal;

import static org.airlinesystem.model.Aircraft.AircraftSize;

public class Flight {
	private AircraftSize aircraftSize;
	private AircraftPilot pilot;
	private AircraftPilot coPilot;
	private Aircraft aircraftAssigned;
	private int[] seatsFilledPerSection;
	private BigDecimal[] seatCostPerSection;
	private double distanceTravelled;
	private Airport source;
	private Airport destination;
	private BigDecimal cost;
	private BigDecimal revenue;
	private BigDecimal profit;
	
	public Flight(AircraftSize aircraftSize_, int[] maxSeatsPerSection_, 
			int[] seatsFilledPerSection_, BigDecimal[] seatCostPerSection_, 
			Airport source_, Airport destination_, double distanceTravelled_,
			AircraftPilot pilot_, AircraftPilot coPilot_, Aircraft aircraftAssigned_) {

		aircraftSize = aircraftSize_;
		seatsFilledPerSection = seatsFilledPerSection_;
		seatCostPerSection = seatCostPerSection_;
		distanceTravelled = distanceTravelled_;
		source = source_;
		destination = destination_;
		pilot = pilot_;
		coPilot = coPilot_;
		aircraftAssigned = aircraftAssigned_;
		cost = new BigDecimal("0");
		revenue = new BigDecimal("0");
		profit = new BigDecimal("0");
	}
	
	public AircraftSize getAircraftSize() {
		return aircraftSize;
	}
	
	public AircraftPilot getPilot() {
		return pilot;
	}
	
	public AircraftPilot getCoPilot() {
		return coPilot;
	}
	
	public Aircraft getAircraftAssigned() {
		return aircraftAssigned;
	}

	public BigDecimal[] getSeatCostPerSection() {
		return seatCostPerSection;
	}
	
	public int[] getSeatsFilledPerSection() {
		return seatsFilledPerSection;
	}

	public double getDistanceTravelled() {
		return distanceTravelled;
	}

	public Airport getSource() {
		return source;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setCost(BigDecimal cost_) {
		cost = cost_;
	}

	public void setRevenue(BigDecimal revenue_) {
		revenue = revenue_;
	}

	public void setProfit(BigDecimal profit_) {
		profit = profit_;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public BigDecimal getProfit() {
		return profit;
	}	
}
