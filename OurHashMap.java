/**
 * OurHashMap is a custom implementation of a 
 * hash map. It provides its own hashing and 
 * compression functions. Entries in the map
 * are String, Integer pairs.
 * 
 * @author Brandon Otto
 *
 */

public class OurHashMap 
{

	protected static class MapEntry
	{
		private String key; 	//key
		private Integer value; 	//value
		
		/**
		 * Constructor for a map entry.
		 * 
		 * @param key The entry's key.
		 * @param value The entry's value.
		 * 
		 */
		
		public MapEntry(String key, Integer value)
		{
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Return the key value of an entry.
		 * 
		 * @return The entry's key.
		 * 
		 */
		
		public String getKey()
		{
			return key;
		}
		
		/**
		 * Return the value of an entry.
		 * 
		 * @return The entry's value.
		 * 
		 */
		
		public Integer getValue()
		{
			return value;
		}
		
		/**
		 * Set the key of an entry.
		 * 
		 * @param key The key to set.
		 * 
		 */
		
		protected void setKey(String key)
		{
			this.key = key;
		}
		
		/**
		 * Set the value of an entry and return the old value.
		 * 
		 * @param value The new value to store.
		 * 
		 */
		
		protected Integer setValue(Integer value)
		{
			Integer old = this.value;
			this.value = value;
			return old;
		}
		
		protected boolean equals(MapEntry B)
		{
			return this.key == B.getKey();
		}
		
		public String toString()
		{
			String str = key + " appears " + value + " times.";
		
			return str;
		}

		
	} //----------End of nested MapEntry class.----------
	
	//Storage for the map of entries.
	final int CAPACITY = 550681;
	private MapEntry[] table = new MapEntry[CAPACITY];
	
	//Variables for hashing.
	final int shiftLeft = 5;
	final int shiftRight = 32 - shiftLeft;
	
	/**
	 * Hashing function.
	 * 
	 * @param key The key to hash.
	 * @return The calculated value of the hash.
	 * 
	 */
	
	public int hashCode(String key) 
		{
	      int hash = 0;
	      
	      //Shift left and bitwise or shift right.
	      for (int i = 0; i < key.length(); i++) 
	      {
	         hash = ( (hash << shiftLeft) | (hash >>> shiftRight) );
	         hash += (int) key.charAt(i);
	      }
	      return hash;
	   }
	
	/**
	 * Compression function for array storage.
	 * 
	 * @param hash The original hash value.
	 * @return The index of the array to store the entry.
	 * 
	 */
	
	public int compressHash(int hash) 
	{
		//MAD compression.
		int index = ((7 * hash + 691) % 1000003) % CAPACITY; 
	    return Math.abs( index ); 								//don't want a negative index
	 }
		
	/**
	 * Quadratic probing function to handle index 
	 * collisions.
	 * 
	 * @param hashIndex The index with a collision.
	 * @return The new hashIndex after probing.
	 * 
	 */
	
	public int quadraticProbe(int hashIndex, int probeAttempt)
	{
		//Quadratic probe using probe attempt squared mod capacity.
		int probeHashIndex = (hashIndex + (probeAttempt * probeAttempt)) % CAPACITY; 
		return probeHashIndex;
	}
	
	//Variables for tracking collisions.
	int indexCollisions;
	int indexCollisionsResolved;
	int hashCollisions;
	
	
	/**
	 * Constructs an empty map. Sets collision 
	 * counts to 0.
	 * 
	 */
		
	public OurHashMap()
	{
		indexCollisions = 0;
		indexCollisionsResolved = 0;
		hashCollisions = 0;	
	}
		
	/**
	 * Access the value of a mapping given the key.
	 * 
	 * @param key The key of the desired mapping.
	 * @return the value mapped to the provided key or
	 * null if the mapping doesn't contain the key.
	 * 
	 */
		
	public Integer get(String key)
	{
		int keyHash = hashCode(key);
		int hashIndex = compressHash(keyHash);
		
		if(table[hashIndex] == null)
		{
			return null;									//empty index.
		} else if(table[hashIndex].getKey().equals(key))
			{
				return table[hashIndex].getValue(); 		//key found, return value.
			} else
				{
					indexCollisions++;
					//Probe for a secondary index.
					
					int attemptProbe = 1;
					boolean done = false;
					while(!done)
					{
						int probeIndex = quadraticProbe(hashIndex, attemptProbe);
						hashIndex = probeIndex;
						
						if(table[hashIndex] == null)
						{
							//Empty index found, collision resolved.
							indexCollisionsResolved++;
							return null;									//empty index.
						} else if(table[hashIndex].getKey().equals(key))
							{
								//Matching key found, collision resolved.
								indexCollisionsResolved++;
								return table[hashIndex].getValue(); 		//key found, return value.
							} else
								{
									attemptProbe++;								//key not found, keep probing.
								}
					
					}

				}
		return -1;
	}
		
	/**
	 * Maps the provided value to the provided key.
	 * Replaces the previous value if any.
	 * 
	 * @param key The key for the mapping.
	 * @param value The value for the mapping.
	 * @return The value if successfully mapped.
	 * 
	 */
		
	public Integer put(String key, Integer value)
	{
		// Create the entry.
		//Hash and compress the key for storage.
		MapEntry entry = new MapEntry(key, value);
		
		int hash = hashCode(key);
		int hashIndex = compressHash(hash);
		
		//Check the index, if empty add the new entry.
		if(table[hashIndex] == null)
		{
			table[hashIndex] = entry;

			return entry.getValue();
		} else if (table[hashIndex].getKey().equals(key))
		{
			//If the key's match, update the value.
			
			table[hashIndex].setValue(value);
			return entry.getValue();
		} else 
			{
				//Probe for an empty index.
				int attemptProbe = 1;
				while(table[hashIndex] != null)
				{
					int probeIndex = quadraticProbe(hashIndex, attemptProbe);
					attemptProbe++;
					hashIndex = probeIndex;
				}
				table[hashIndex] = entry;
				return entry.getValue(); 		
			}
	}	
	
	/**
	 * Utility method to print all the entries 
	 * in the table.
	 */
	
	public void printEntries()
	{
		for(MapEntry entries: table)
		{
			if(entries != null)
				{
					System.out.println(entries);
				}
		}
	}
	
	/**
	 * Utility method to print the key with the most
	 * occurences.
	 */
	
	public void printMaxKey()
	{
		int maxCount = 0;
		String maxWord = "no word";
		
		for(MapEntry entries: table)
		{
			if(entries != null)
				{
					if(entries.getValue() > maxCount)
					{
						maxWord = entries.getKey();
						maxCount = entries.getValue();
					}
				}
		}
		
		System.out.println("The most frequent word used is " + maxWord);
		System.out.println(maxWord + " appears " + maxCount + " times.\n");
	}
	
	/**
	 * Calculates the number of entries in the map.
	 * @return The number of entries stored in the map.
	 * 
	 */
	
	public int getSize()
	{
		int tableSize = 0;
		
		for(MapEntry entries: table)
		{
			if(entries != null)
				{
					tableSize++;
				}
		}
		
		return tableSize;
	}
}
