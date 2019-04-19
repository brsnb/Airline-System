package org.airlinesystem.model;

import java.util.ArrayList;
import java.math.BigDecimal;

import static org.airlinesystem.model.Aircraft.AircraftSize;

public class AircraftSectionList extends ArrayList<AircraftSection> {

    private static final long serialVersionUID = 5534735606050325939L;
    
    /**
	 *  Section class options
	 *  <li>{@link #FIRST}/<li>
	 * <li>{@link #BUSINESS}/<li>
	 * <li>{@link #ECON_PLUS}/<li>
	 * <li>{@link #ECON_BASIC}/<li>
	 */
	private enum sectionClassTypes {
		/**
		 * First Class
		 */
		FIRST,
		
		/**
		 * Business Class
		 */
		BUSINESS,
		
		/**
		 * Economy Plus Class
		 */
		ECON_PLUS,
		
		/**
		 * Economy Basic Class
		 */
		ECON_BASIC;
	}

	public AircraftSectionList(AircraftSize aircraftSize_, 
			int[] seatsFilledPerSection_, BigDecimal[] costOfSeat_) {
		createSections(aircraftSize_, seatsFilledPerSection_, costOfSeat_);
	}
	
	public void createSections(AircraftSize aircraftSize_, 
			int[] seatsFilledPerSection_, BigDecimal[] costOfSeat_) {
		String _classSectionName;
		
		switch(aircraftSize_) {
		case S:
			//create 1 section with proper name
			_classSectionName = sectionClassTypes.ECON_BASIC.toString();
			add(new AircraftSection(seatsFilledPerSection_[0], _classSectionName, costOfSeat_[0]));
			break;
		case M:
			//create 3 sections with proper names
			_classSectionName = sectionClassTypes.ECON_BASIC.toString();
			add(new AircraftSection(seatsFilledPerSection_[0], _classSectionName, costOfSeat_[0]));
			_classSectionName = sectionClassTypes.BUSINESS.toString();
			add(new AircraftSection(seatsFilledPerSection_[2], _classSectionName, costOfSeat_[1]));
			_classSectionName = sectionClassTypes.FIRST.toString();
			add(new AircraftSection(seatsFilledPerSection_[3], _classSectionName, costOfSeat_[3]));
			break;
		case L:
		default:
			//create 4 sections with proper names
			_classSectionName = sectionClassTypes.ECON_BASIC.toString();
			add(new AircraftSection(seatsFilledPerSection_[0], _classSectionName, costOfSeat_[0]));
			_classSectionName = sectionClassTypes.ECON_PLUS.toString();
			add(new AircraftSection(seatsFilledPerSection_[1], _classSectionName, costOfSeat_[1]));
			_classSectionName = sectionClassTypes.BUSINESS.toString();
			add(new AircraftSection(seatsFilledPerSection_[2], _classSectionName, costOfSeat_[2]));
			_classSectionName = sectionClassTypes.FIRST.toString();
			add(new AircraftSection(seatsFilledPerSection_[3], _classSectionName, costOfSeat_[3]));
			break;
		}	
	}
}
