package io.github.teamfractal.util;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import io.github.teamfractal.entity.enums.ResourceType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;


// JavaDocs added by Josh Neil
/**
 * Test case for {@link StringUtil}
 */

@RunWith(Enclosed.class)
public class StringUtilTest {
	
	// Moved into its own class by Josh Neil
	/**
	 * Tests {@link StringUtil#Capitalise(String)} and ensures that it returns the correct values for a variety
	 * of different input values
	 * @author jcn509
	 *
	 */
	public static class testCapitalise{
		/**
		 * Tests {@link StringUtil#Capitalise(String)} and ensures that it returns the correct values for a variety
		 * of different input values
		 * @author jcn509
		 *
		 */
		@Test
		public void capitaliseShouldBeDoneProperly(){
			assertEquals("Java", StringUtil.Capitalise("java"));
			assertEquals("JAVA", StringUtil.Capitalise("jAVA"));
			assertEquals("J", StringUtil.Capitalise("j"));
		}
	}
	
	// Added by Josh Neil
	/**
	 * Tests {@link StringUtil#stringToResource(String)} ensures that the correct ResourceType is returned
	 * for valid input strings
	 * @author jcn509
	 *
	 */
	@RunWith(Parameterized.class)
	public static class StringUtilTestStringToResource{
		private String inputString;
		private ResourceType expectedResource;
		private boolean exceptionShouldBeThrown;
		
		/**
		 * Sets up the values to be used in each test
		 * @param inputString The string that is input to {@link StringUtil#stringToResource(String)}
		 * @param expectedResource The result that should be thrown (if exceptionShouldBeThrown is false)
		 * @param exceptionShouldBeThrown Whether or not the given input should cause an exception to be thrown
		 */
		public StringUtilTestStringToResource(String inputString, ResourceType expectedResource, boolean exceptionShouldBeThrown){
			this.inputString = inputString;
			this.expectedResource = expectedResource;
			this.exceptionShouldBeThrown = exceptionShouldBeThrown;	
		}
		
		/**
		 * Defines the values to be used in each test
		 */
		@Parameterized.Parameters
		public static Collection StringToResourceValues(){
			 return Arrays.asList(new Object[][] {
		         {"ore",ResourceType.ORE,false},
		         {"orE",ResourceType.ORE,false},
		         {"oRe",ResourceType.ORE,false},
		         {"oRE",ResourceType.ORE,false},
		         {"Ore",ResourceType.ORE,false},
		         {"OrE",ResourceType.ORE,false},
		         {"ORe",ResourceType.ORE,false},
		         {"ORE",ResourceType.ORE,false},
		         {"  ORE",ResourceType.ORE,false},
		         
		         {"energy",ResourceType.ENERGY,false},
		         {"  energy   ",ResourceType.ENERGY,false},
		         {"energY",ResourceType.ENERGY,false},
		         {"eneRgy",ResourceType.ENERGY,false},
		         {"Energy",ResourceType.ENERGY,false},
		         {"ENergy",ResourceType.ENERGY,false},
		         {"enERgy",ResourceType.ENERGY,false},
		         {"eNerGy",ResourceType.ENERGY,false},
		         {"enErgy",ResourceType.ENERGY,false},
		         
		         {"food",ResourceType.FOOD,false},
		         {"FOOD",ResourceType.FOOD,false},
		         {"FoOd",ResourceType.FOOD,false},
		         {"Food",ResourceType.FOOD,false},
		         {"fOOd",ResourceType.FOOD,false},
		         {"fOOd  ",ResourceType.FOOD,false},
		         
		         {"orres",ResourceType.FOOD,true},
		         {"fod",ResourceType.FOOD,true},
		         {"enrgy",ResourceType.FOOD,true},
		         {"roboticon",ResourceType.FOOD,true},
		         {"customisation",ResourceType.FOOD,true},
		         {"",ResourceType.FOOD,true},
		         {"    ",ResourceType.FOOD,true},
		      });
		}
		
		/**
		 * Tests {@link StringUtil#stringToResource(String)} and if it is supposed to throw an exception ensures
		 * that it does and if it is not ensures that it does not
		 */
		@Test
		public void testExceptionThrown(){
			if(exceptionShouldBeThrown){
				try{
					StringUtil.stringToResource(inputString);
					fail(); // Fail if no exception is thrown!
				}
				catch(Exception e){
					return;
				}
			}
			else{
				try{
					StringUtil.stringToResource(inputString);
					return;
				}
				catch(Exception e){
					fail(); // Fail if exception is thrown!
				}
			}
		}
		
		/**
		 * Tests {@link StringUtil#stringToResource(String)} and ensures that it returns the correct ResouceType
		 * for a given input string
		 */
		@Test
		public void testCorrectReturnValue(){
			if(!exceptionShouldBeThrown){
				assertEquals(expectedResource, StringUtil.stringToResource(inputString));
			}
		}
	}
	
	
	
}
