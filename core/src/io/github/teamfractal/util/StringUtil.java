package io.github.teamfractal.util;

import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidResourceTypeException;

/**
 * Some string extension classes
 */
public class StringUtil {
	/**
	 * Capitalise the string by make first letter capital.
	 * @param str     The input string.
	 * @return        The capitalised string.
	 */
	public static String Capitalise (String str) {
		if (str.length() <= 1) {
			return str.toUpperCase();
		}

		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	
	// Added by Josh Neil (used in the ResourceMarkerActors class when players select a resource from a drop down
	// menu)
	/**
	 * Takes a string as input and returns the corresponding resource type
	 * <p>
	 * Note is only to be used for resources that can be produced by roboticons.
	 * </p>
	 * @param resourceString A string such as "food" or "energy"
	 * @return The corresponding resource type
	 */
	public static ResourceType stringToResource(String resourceString){
		if(resourceString.toLowerCase().contains("food")){
			return ResourceType.FOOD;
		}
		else if(resourceString.toLowerCase().contains("energy")){
			return ResourceType.ENERGY;
		}
		else if(resourceString.toLowerCase().contains("ore")){
			return ResourceType.ORE;
		}
		else{ // Shouldn't use this method for other kinds of resource
			throw new InvalidResourceTypeException();
		}
	}
}
