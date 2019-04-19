package org.airlinesystem.model;

import java.math.BigDecimal;

public class AircraftSection {
	private String classSectionName;
	private int maxNumOfSeats;
	private int filledSeats;
	private BigDecimal costOfSeat;

	public AircraftSection(int filledSeats_, String classSectionName_,
			BigDecimal costOfSeat_) {
		filledSeats = filledSeats_;
		classSectionName = classSectionName_;
		costOfSeat = costOfSeat_;
	}

	public String getClassSectionName() {
		return classSectionName;
	}

	public void setClassSectionName(String classSectionName_) {
		classSectionName = classSectionName_;
	}

	public int getMaxNumOfSeats() {
		return maxNumOfSeats;
	}

	public void setMaxNumOfSeats(int maxNumOfSeats_) {
		maxNumOfSeats = maxNumOfSeats_;
	}

	public int getFilledSeats() {
		return filledSeats;
	}

	public void setFilledSeats(int filledSeats_) {
		filledSeats = filledSeats_;
	}

	public BigDecimal getCostOfSeat() {
		return costOfSeat;
	}

	public void setCostOfSeat(BigDecimal costOfSeat_) {
		costOfSeat = costOfSeat_;
	}
}
