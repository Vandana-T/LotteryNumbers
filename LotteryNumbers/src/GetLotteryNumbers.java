import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * @author vandana
 *
 */
public class GetLotteryNumbers {

	
	/**
	 * Executes different combinations of number strings to see if lottery numbers can be generated for them. 
	 * @param args Ignored since no terminal input is needed. 
	 */
	public static void main(String[] args) {
	
		// The below numbers are test cases to cover different scenarios
		// If zero cannot be combined with the previous digit, then its an invalid sequence. 
		String[] numbers = new String [] {  "1", "42", "100848", "4938532894754", "1234567", "472844278465445","4930532890754", "49305300000", "1234067", "12345450"}; 
		PrintLotteryNumbers(numbers); 
		
		String[] emptyNumbers =  new String[] { }; 
		PrintLotteryNumbers(emptyNumbers);
		
		// Test null and empty array
		String[] nullNumbers =  new String[] { null, "" }; 
		PrintLotteryNumbers(nullNumbers);
		
		// test array with invalid values. 
		// 4938532896954 is invalid because of the presence of 969 sequence. 
		String[] invalidNumbers =  new String[] { "-12638", "HGHA72837", "01234567", "0123456", "4938532896954"};
		PrintLotteryNumbers(invalidNumbers);
		
	}

	
	/**
	 * Prints the lottery numbers for each valid number in the array. Returns an empty list for an invalid number.
	 * @param numbers List of numbers for which the lottery numbers need to be generated. 
	 */
	public static void PrintLotteryNumbers(String[] numbers)
	{
		System.out.println("Input : " + String.join(",", numbers));
		
		HashMap<String, List<Integer>> result  =  GetLotteryNumbers(numbers);
		
		Set<String> keySet =  result.keySet(); 
		
		for(String key : keySet)
		{
			List<Integer> values  = result.get(key);
			System.out.println("Key " + key + " Value [" + Join(values) + "]");
		}
		
		System.out.println("================================");
	}
	
	/**
	 * @param numbers: Get Lottery numbers for each number in the string array
	 * @return A Map which returns the valid lottery numbers for each string. 
	 */
	public  static HashMap<String, List<Integer>> GetLotteryNumbers(String[] numbers)
	{
		HashMap<String, List<Integer>> lotteryNumbers =  new HashMap<String, List<Integer>>(); 

		for (int index = 0; index< numbers.length; index++) 
		{
			if (numbers[index] != null && numbers[index].length() >= 7 &&  numbers[index].length() <= 14)
			{
				ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>(); 
	
				// get the unique numbers between 1 and 59 by parsing the number
				if (GetUniqueNumbersFromString(numbers[index], 0, result) 
						&& result.size() > 0 && result.get(0).size() == 7) 
				{
					lotteryNumbers.put(numbers[index], result.get(0)); 
				}
	
				// Otherwise valid numbers could not be fetched.  Continue checking the next number. 
			}
		}

		return lotteryNumbers; 

	}

	
	/**
	 * @param number: string number being considered
	 * @param index:  index of the char to be parsed
	 * @param lotteryList: List of lottery number combinations found
	 * @return true if the operation succeeded, false otherwise
	 */
	private static boolean GetUniqueNumbersFromString(String number, int index, ArrayList<ArrayList<Integer>> lotteryList)
	{
		Integer value;

		if ( (value = ValidateAndGetDigit(number, index)) >= 0) 
		{
			if(value == 0) 
			{
				if (!AddZeroToTheList(lotteryList)) 
				{
					// If the addition of zero fails, then this number is invalid
					return false; 
				}
			}
			else 
			{
				AddNonZeroValueToTheList(lotteryList, value);
			}
		}
		else  
		{ 
			return false; 	
		}

		// Do an iterative call for the next character in the string
		if (index < number.length() - 1)
		{
			return GetUniqueNumbersFromString(number, index+1, lotteryList); 
		}
		
		return true; 
	}

	
	/**
	 * @param number:  The original number string
	 * @param index: Index of the character to be considered
	 * @return Integer value for the character, for an invalid character returns -1
	 */
	private static Integer ValidateAndGetDigit(String number, int index) {
	
		Integer value = -1;
		
		if (number.charAt(index) >= '0' && number.charAt(index) <= '9')
		{
			value = number.charAt(index) - '0'; 
		}
		
		return value;
	}


	/**
	 * Adds zero the already found lottery combinations
	 * @param lotteryList: Lottery number combinations found
	 * @return true if the operation succeeded, false otherwise
	 */
	private static boolean AddZeroToTheList(ArrayList<ArrayList<Integer>> lotteryList)
	{
		boolean zeroAdded = false; 
		Iterator<ArrayList<Integer>> iter = lotteryList.iterator();
		ArrayList<ArrayList<Integer>> toRemove = new ArrayList<ArrayList<Integer>>();
		
		// Go through each lottery list to see if zero can be added.
		while (iter.hasNext()) 
		{
			ArrayList<Integer> currentLotteryNums = iter.next(); 
			
			// Zero can be added only if it can be appended to the previous number. For the specified range, only 10 to 50 are valid numbers.
			int lastValue = currentLotteryNums.get(currentLotteryNums.size()-1); 
			
			if (lastValue >=1 && lastValue <= 5) 
			{
				// Replace the last number with the new value
				currentLotteryNums.remove(currentLotteryNums.size()-1); 
				int newValue = lastValue * 10; 
				currentLotteryNums.add(newValue); 
				zeroAdded = true; 
			}
			else 
			{ 
				toRemove.add(currentLotteryNums);
			}
		}

		lotteryList.removeAll(toRemove);
		return zeroAdded; 
	}

	
	/**
	 * Adds the value to the list of numbers generated for lottery
	 * @param lotteryList:  List of lottery numbers possible. 
	 * @param value:  Integer to be inserted into the list of lottery numbers. 
	 */
	private static void AddNonZeroValueToTheList(ArrayList<ArrayList<Integer>> lotteryList, int value) 
	{
		if (lotteryList.size() == 0) 
		{
			// If this is the first number, then just add it to the list. 
			ArrayList<Integer> newList = new ArrayList<Integer>(7); 
			newList.add(value);
			lotteryList.add(newList);
		}
		else 
		{ 
			// Add the Integer to the possible combinations. 
			Iterator<ArrayList<Integer>> iter = lotteryList.iterator();
			ArrayList<ArrayList<Integer>> toRemove = new ArrayList<ArrayList<Integer>>(); 
			ArrayList<ArrayList<Integer>> toAdd = new ArrayList<ArrayList<Integer>>(); 
			
			while (iter.hasNext()) 
			{
				ArrayList<Integer> currentLotteryNums = iter.next(); 
				
				int lastValue =  currentLotteryNums.get(currentLotteryNums.size() - 1); 
				
				// Check if the current value can be appended to the previous number in the combinations found.
				int newValue  = lastValue * 10 + value; 
				
				if (newValue >= 10 && newValue <= 59) 
				{
					// If new value is valid, then add the new value into a new list. Add the new list into the combinations found.
					// e.g. lotteryList = [ 4 5 ], value = 9, new combination added here is [4, 59]
					// if value was instead [4 6], value = 9, new combination cannot be added because 69 is invalid
					ArrayList<Integer> newList = new ArrayList<Integer>(currentLotteryNums); 
					newList.remove(currentLotteryNums.size()-1); 
					newList.add(newValue);
					
					// Since only 7 unique numbers are valid, don't add to the list if the newList created has a size > 7
					if (newList.size() <= 7)
					{
						toAdd.add(newList); 
					}
				}
	
				// Add the digit independently to the list. 
				// e.g. [4 5], value =6, new combination found is [4 5 6]
				currentLotteryNums.add(value);
				
				// If the size of the list is greater than 7, remove it from the possible combinations
				if(currentLotteryNums.size() > 7)
				{
					toRemove.add(currentLotteryNums);
				}
			}
			
			lotteryList.removeAll(toRemove);
			lotteryList.addAll(toAdd);
		}
	}
	
	
	/**
	 * Join values with a comma and return a string
	 * @param values to be concatenated
	 * @return string which contains the concatenated vales separated by comma.
	 */
	private static String Join(List<Integer> values) {
		String valueStr = ""; 
		
		for(int index = 0; index < values.size(); index++)
		{
			valueStr += values.get(index); 
			
			if(index != values.size() -1)
			{
				valueStr += ", ";
			}
		}
		
		return valueStr; 
	}

}
