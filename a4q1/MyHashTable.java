package a4q1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class MyHashTable<K,V> implements Iterable<MyHashTable<K,V>.HashEntry>{
	/*
	 *   Number of entries in the HashTable. 
	 */
	private int entryCount = 0;
	
	/*
	 * Number of buckets. The constructor sets this variable to its initial value,
	 * which eventually can get changed by invoking the rehash() method.
	 */
	private int numBuckets;
	
	/**
	 * Threshold load factor for rehashing.
	 */
	private final double MAX_LOAD_FACTOR=0.75;
	
	/**
	 *  buckets to store the key-value pairs.   Traditionally an array is used for the buckets
	 *  and a linked allEntries is use for the entries within each bucket.   
	 *  
	 *  We use an ArrayallEntries rather than array, since the former is simpler to use in Java.   
	 */
	 
	ArrayList<LinkedList<HashEntry>>  buckets;
	
	/* 
	 * Constructor.
	 * 
	 * numBuckets is the initial number of buckets used by this hash table
	 */
	 
	MyHashTable(int numBuckets) {
		
		//  ADD YOUR CODE HERE
		
		this.numBuckets = numBuckets;
		//initialize buckets array
		this.buckets = new ArrayList<LinkedList<HashEntry>>(this.numBuckets);
		//initialize linked lists in each bucket
		for (int i = 0; i < this.numBuckets; i++){
			this.buckets.add(new LinkedList<HashEntry>());
		}
	}
	
	/**
	 * Given a key, return the bucket position for the key. 
	 */
	private int hashFunction(K key) {
		
		return  Math.abs( key.hashCode() ) % numBuckets ;
	}
	
	/**
	 * Checking if the hash table is empty.  
	 */
	public boolean isEmpty()
	{
		if (entryCount == 0)
			return true;
		else
			return(false);
	}
	
	/**
	 *   return the number of entries in the hash table.
	 */
	public int size()
	{
		return(entryCount);
	}
	
	/**
	 * Adds a key-value pair to the hash table. If the load factor goes above the 
	 * MAX_LOAD_FACTOR, then call the rehash() method after inserting. 
	 * 
	 *  If there was a previous value for the given key in this hashtable, then return it.
	 *  Otherwise return null.   
	 */
	
	public  V  put(K key, V value) {
		
		//  ADD YOUR CODE HERE
		
		int hashValue = hashFunction(key);
		HashEntry hashEntry = new HashEntry(key, value);
		
		if (buckets.get(hashValue).contains(hashEntry)){ //collision
			V cool = get(key);
			buckets.get(hashValue).add(hashEntry);
			entryCount++;
			return cool;
		}
		
		buckets.get(hashValue).add(hashEntry); 
		entryCount++;
		//buckets is arraylist
		//have to get() buckets in order to access linked list within
		
		//check load factor
		//load factor = #entries/#buckets
		double loadFactor = entryCount/numBuckets;
		if(loadFactor > MAX_LOAD_FACTOR){
			rehash();
		}
		
		return null;   //  remove this stub
	}
	
	/**
	 * Retrieves a value associated with some given key in the hash table.
	   Returns null if the key could not be found in the hash table)
	 */
	public V get(K key) {
		
		//  ADD YOUR CODE HERE
		
		int hashValue = hashFunction(key);
		
		//if key exists in hash table
		if (buckets.get(hashValue).isEmpty() == false){
			LinkedList<HashEntry> linkedlist = new LinkedList<HashEntry>();
			linkedlist = buckets.get(hashValue);
			for (int i = 0; i < linkedlist.size(); i++){//look at each element/entry in linked list
				if(linkedlist.get(i).getKey().equals(key)){
					return linkedlist.get(i).getValue();
				}
			}
			
		}
		
		//if key does not exist

			return null;
	
	}
	
	/**
	 * Removes a key-value pair from the hash table.
	 * Return value associated with the provided key. If the key is not found, return null.
	 */
	public V remove(K key) {

		//  ADD YOUR CODE HERE
		int hashValue = hashFunction(key);
		//remove if key found
		if (buckets.get(hashValue).isEmpty() == false){//is not empty //same as in get()
			LinkedList<HashEntry> linkedlist = new LinkedList<HashEntry>();
			linkedlist = buckets.get(hashValue);
			for(int i = 0; i < linkedlist.size(); i++){
				if(linkedlist.get(i).getKey().equals(key)){
					V bitchy = linkedlist.get(i).getValue();
					linkedlist.remove(i);
					return bitchy;
				}
			}	
		}
		return(null);
	}
	
	/*
	 *  This method is used for testing rehash().  Normally one would not provide such a method. 
	 */
	public int getNumBuckets(){
		return numBuckets;
	}

	/*
	 * Returns an iterator for the hash table. 
	 */
	
	//@Override
	public MyHashTable<K, V>.HashIterator	iterator(){
		return new HashIterator();
	}
	
	/**
	 * Removes all the entries from the hash table, but keeps the number of buckets intact.
	 */
	public void clear()
	{
		for (int ct = 0; ct < buckets.size(); ct++){
			buckets.get(ct).clear();
		}
		entryCount=0;		
	}
	
	/**
	 *   Create a new hash table that has twice the number of buckets.
	 */
	
	public void rehash()
	{
		//   ADD YOUR CODE HERE
		
		int newNumBuckets = numBuckets*2; //double the size
		//^ should I call it newNumBuckets?
		//create a new table with double dimensions
		MyHashTable<K,V> newTable = new MyHashTable<K,V>(newNumBuckets);

		//for all linked lists (buckets) pointing to old arraylist, 
		//make them point to new hash table
		//this. = old hash table
		for (int i = 0; i < this.numBuckets; i++){
			for(int j = 0; j < this.buckets.get(i).size(); j++){ //this...size() = size of linked list
				K key = this.buckets.get(i).get(j).getKey();
				V value = this.buckets.get(i).get(j).getValue();
				//numBuckets = newNumBuckets;
				newTable.put(key, value);
			}
		}
		this.buckets = newTable.buckets;
		this.numBuckets = newNumBuckets;
		
		//numBuckets = newNumBuckets;
	}
	
	
	/*
	 * Checks if the hash table contains the given key.
	 * Return true if the hash table has the specified key, and false otherwise.
	 */
	public boolean containsKey(K key)
	{
		return (get(key) != null);
	}
		
	/*
	 * return an ArrayList of the keys in the hashtable
	 */
	
	public ArrayList<K>  keys()
	{
		
		ArrayList<K>  listKeys = new ArrayList<K>();
		
		//   ADD YOUR CODE HERE

		//go through each ArrayList index, go into linked list, add all keys from each entry
		for (int i = 0; i < numBuckets; i++){ //i = hashValue //go through ArrayList indices 
			//listKeys.add(i, buckets.get(hashFunction(key)).get(i).getKey());
			LinkedList<HashEntry> linkedList = new LinkedList<HashEntry>();
			linkedList = buckets.get(i); //linked list for each arraylist index
			for(int j = 0; j < linkedList.size(); j++){ //go through elements in linked list
				listKeys.add(j, buckets.get(i).get(j).getKey());
			}
		}
		
		return listKeys;

	}
	
	/*
	 * return an ArrayList of the values in the hashtable
	 */
	public ArrayList <V> values()
	{
		ArrayList<V>  listValues = new ArrayList<V>();
		
		//   ADD YOUR CODE HERE
		
		for (int i = 0; i < numBuckets; i++){ //i = hashValue //go through ArrayList indices 
			LinkedList<HashEntry> linkedList = new LinkedList<HashEntry>();
			linkedList = buckets.get(i); //linked list for each arraylist index
			for(int j = 0; j < linkedList.size(); j++){ //go through elements in linked list
				listValues.add(j, buckets.get(i).get(j).getValue());
			}
		}
		
		return listValues;
	}

	@Override
	public String toString() {
		/*
		 * Implemented method. You do not need to modify.
		 */
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buckets.size(); i++) {
			sb.append("Bucket ");
			sb.append(i);
			sb.append(" has ");
			sb.append(buckets.get(i).size());
			sb.append(" entries.\n");
		}
		sb.append("There are ");
		sb.append(entryCount);
		sb.append(" entries in the hash table altogether.");
		return sb.toString();
	}
	
	/*
	 *    Inner class:   Iterator for the Hash Table.
	 */
	public class HashIterator implements  Iterator<HashEntry>
	{
		LinkedList<HashEntry>  allEntries;
		
		/**
		 * Constructor:   make a linkedlist 'allEntries' of all the entries in the hash table
		 */
		public  HashIterator()
		{
			
			//  ADD YOUR CODE HERE
			
			LinkedList<HashEntry> allEntries = new LinkedList<HashEntry>();
			
//			//for each bucket, look at the linked list.
//			//for each liked list, add the entries
//			for (int i = 0; i < numBuckets; i++){
//				
//				
//				//this.buckets.add(this.allEntries);
//				
//				allEntries.add(i, MyHashTable.HashEntry()); //K,V pair = entry though
				
			}
		
		
		//  Override
		@Override
		public boolean hasNext()
		{
			return !allEntries.isEmpty();
		}
			
		//  Override
		@Override
		public HashEntry next()
		{	
			return allEntries.removeFirst();
		}

		@Override
		public void remove() {

		// not implemented,  but must be declared because it is in the Iterator interface
			
		}		
	}
	
	//  helper method
	
	private HashEntry  getEntry(K key){
		int index = hashFunction(key);
 		LinkedList<HashEntry> bucketList = buckets.get(index);
 		for (HashEntry node : bucketList ){
 			if (node.getKey() == key)
 				return node; 
 		}
 		return null;	//else 	
	}

	class HashEntry {
		
		private K key;
		private V value;
		
		/*
		 * Constructor.
		 */
		HashEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/*
		 * Returns this hash entry's key.   Assume entry is not null.
		 * @return This hash entry's key
		 */
		K getKey() {
			return(key);
		}
		
		/**
		 * Returns this hash entry's value.  Assume entry is not null.
		 */
		V getValue() {
			return(value);
		}
		
		/**
		 * Sets this hash entry's value.
		 */
		void setValue(V value) {
			this.value =  value;		
		}		
	}


}
