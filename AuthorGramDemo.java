import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * AuthorGramDemo demonstrates the author grams
 * program. It uses a JCF map class and a custom
 * map class to catalog the word counts in a text.
 * 
 * @author Brandon Otto
 *
 */

public class AuthorGramDemo 
{

	public static void main (String[] args)
	{
		//Create a scanner for OliverTwist.
		Scanner oliverTwist = openFile("OliverTwist.txt");
		
		//Count all of the words in the Oliver Twist story.
		//First via standard hash map.
		Map<String, Integer> dickensStandard = new HashMap<String, Integer>();
		
		while(oliverTwist.hasNext())
	    {
			//Get the next word and the count.
	       	String word = oliverTwist.next().toLowerCase();
	       	Integer count = dickensStandard.get(word);
	       	
	       	if(count == null)
	       	{
	       		//If a new word, add with count 1.
	       		dickensStandard.put(word, 1);
	       	} else 
	       	{
	       		//Existing word, increment the count.
	       		dickensStandard.put(word, count + 1);
	       	}
	    }	
		
		//Close the file.
		 oliverTwist.close();
		 
		//Create a new scanner for OliverTwist.
		Scanner oliverTwistCustom = openFile("OliverTwist.txt");
		
		//Now use a custom hash map.
		OurHashMap dickensCustom = new OurHashMap();
			
	    while(oliverTwistCustom.hasNext())
	    {
	       	String word = oliverTwistCustom.next().toLowerCase();
	       	Integer count = dickensCustom.get(word);
	       	if(count == null)
	       	{
	       		//If a new word, add with count 1.
	       		dickensCustom.put(word, 1);
	       	}  else
	       	{
	    		//Existing word, increment the count.
	       		dickensCustom.put(word, count + 1);
	       	}
	    }	
		
	    //Close the file.
	    oliverTwistCustom.close();
	    
	    
	    //Analyze the results.
	    
	    System.out.println("Using the standard hash map.\n");
	    
	    //Variables for most common used word.
	    int maxCount = 0;
		String maxWord = "no word";
	    
	    //Get a set containing the keys in the standard map.
	    Set<String> standardKeys = dickensStandard.keySet();
	    
	    //Iterate through the keys looking for the most common used word.
	    for(String keys : standardKeys)
	    {
	    	if(dickensStandard.get(keys) > maxCount)
			{
				maxWord = keys;
				maxCount = dickensStandard.get(keys);
			}
	    }
	    
	    //Print the most common word.
	    System.out.println("The most frequent word used is " + maxWord);
		System.out.println(maxWord + " appears " + maxCount + " times.\n");
	    
	    System.out.println("Using the custom hash map.\n");
	    dickensCustom.printMaxKey();
	    
	    System.out.println("The standard map counted " + dickensStandard.size() + " mappings.\n");
	    System.out.println("The custom map counted " + dickensCustom.getSize() + " mappings.\n" );	
	    
	    //Find the top ten words.
	    System.out.println("Using the standard hash map.\n");
	    
	    System.out.println("The top ten words longer than 4 letters are:");
	    String[] standardTopTen = TopTen(standardKeys, dickensStandard, 4);
	    
	    for(String word : standardTopTen)
	    {
	    	System.out.println(word + " Count: " + dickensStandard.get(word));
	    }
	    
	    
	}
	
	/**
	 * Utility method to open a file.
	 * @param filename The file to open.
	 * @return A reference to the scanner for the file.
	 * 
	 */
	
	public static Scanner openFile(String filename)
	{
		Scanner scan;
		
		//Attempt to open the file.
		try
		{
			scan = new Scanner(new File(filename)).useDelimiter("[^a-zA-Z]+");
		} catch(FileNotFoundException e)
		{
			scan = null;
		}
		
		return scan;
	}

	/**
	 * Utility method to find the top ten words in the 
	 * standard hash map.
	 * @param keyList A list of the keys in the map.
	 * @param map A reference to the map.
	 * @return A String array of the top ten most common words.
	 * 
	 */
	
	public static String[] TopTen(Set<String> keyList, Map<String, Integer> map, int wordLength)
	{
		String[] list = new String[10];
		int numEntries = 0;
		
		for(String keys : keyList)
		{
			if(keys.length() > wordLength)
			{
				if(numEntries < list.length || map.get(keys) > map.get(list[numEntries - 1]) )
				{
					if(numEntries < list.length)
					{
						numEntries++;
					}
					
					int j = numEntries - 1;
					while(j > 0 && map.get(list[j-1]) < map.get(keys))
					{
						list[j] = list[j-1];
						j--;
					}
					list[j] = keys;
				}
			}
		}
			
		return list;
	}
	
}

