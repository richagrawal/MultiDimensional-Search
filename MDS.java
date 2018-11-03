/** Starter code for LP3
 *  @author
 */

// Change to your net id
package LongProject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

// If you want to create additional classes, place them in this file as subclasses of MDS

public class MDS {
    // Add fields of MDS here
	TreeMap<Long, ItemDetails> item;
	HashMap<Long, HashSet<Long>> descLookup;
	int result;

    // Constructors
    public MDS() {
    	item = new TreeMap<Long, ItemDetails>();
    	descLookup = new HashMap<Long, HashSet<Long>>();
    	result = 0;
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
    */
    public int insert(long id, Money price, java.util.List<Long> list) {
    	
    	int insRetVal = 0;
    	ItemDetails detail = new ItemDetails(price, list);
		
    	Iterator<Long> it = list.iterator();
    	HashSet<Long> idSet = new HashSet<Long>();
    	
    	
    	if(item.containsKey(id)){
    		HashSet<Long> descDel = item.get(id).desc;
    		Iterator<Long> itDel = descDel.iterator();
    		if(list.size()>0){
	    		while(itDel.hasNext()){
	    			Long delId = itDel.next();
	    			if(descLookup.containsKey(delId)){
	    				idSet = descLookup.get(delId);
	    				if(idSet.size()==1){
	    					descLookup.remove(delId);
	    				}
	    				else{
	    					idSet.remove(id);
	    				}
	    			}
	    		}
    		}
    		else{
    			detail.desc = descDel;
    		}
    		item.put(id, detail);
    	}
    	else{
    		item.put(id, detail);
    		insRetVal = 1;
    	}
    	
    	while(it.hasNext()){
    		Long descId = it.next();
    		idSet = new HashSet<Long>();
    		if(descLookup.containsKey(descId)){
    			idSet = descLookup.get(descId);
    			
    		}
    		idSet.add(id);
			descLookup.put(descId, idSet);
    	}
    	
    	return insRetVal;
    }

    // b. Find(id): return price of item with given id (or 0, if not found).
    public Money find(long id) {
    	
    	if(item.containsKey(id)){
    		ItemDetails det = item.get(id);
    		return(det.price);
    	}
    	return new Money();
    }

    /* 
       c. Delete(id): delete item from storage.  Returns the sum of the
       long ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */
    public long delete(long id) {
    	
    	if(item.containsKey(id)){
    		long sum = 0;
    		ItemDetails det = item.get(id);
    		Iterator<Long> it = det.desc.iterator();
    		while(it.hasNext()){
    			Long descId = it.next();
    			HashSet<Long> idSet = descLookup.get(descId);
    			idSet.remove(id);
    			descLookup.put(descId, idSet);
    			sum = sum + descId;
    		}
    		item.remove(id);
    		return sum;
    	}
    	return 0;
    }

    /* 
       d. FindMinPrice(n): given a long int, find items whose description
       contains that number (exact match with one of the long ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */
    public Money findMinPrice(long n) {
    	
    	if(descLookup.containsKey(n)){
    		
    		Iterator<Long> it = descLookup.get(n).iterator();
    		Long id = 0l;
    		Money minPrice = new Money();
    		Money p = new Money();
    		ItemDetails detail;
    		if(it.hasNext()){
    			id = it.next();
    			detail = item.get(id);
    			minPrice = detail.price;
    		}
    		while(it.hasNext()){
    			id = it.next();
    			detail = item.get(id);
    			p = detail.price;
    			if(p.compareTo(minPrice)==-1){
    				minPrice = p;
    			}
    		}
    		return minPrice;
    	}
    	
    	return new Money();
    }

    /* 
       e. FindMaxPrice(n): given a long int, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */
    public Money findMaxPrice(long n) {
    	
    	if(descLookup.containsKey(n)){
    		
    		Iterator<Long> it = descLookup.get(n).iterator();
    		Long id = 0l;
    		Money maxPrice = new Money();
    		Money p = new Money();
    		ItemDetails detail;
    		while(it.hasNext()){
    			id = it.next();
    			detail = item.get(id);
    			p = detail.price;
    			if(p.compareTo(maxPrice)==1){
    				maxPrice = p;
    			}
    		}
    		return maxPrice;
    	}
    	
    	return new Money();
    }

    /* 
       f. FindPriceRange(n,low,high): given a long int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    public int findPriceRange(long n, Money low, Money high) {
    	if(descLookup.containsKey(n)){
    		HashSet<Long> idSet = descLookup.get(n);
    		Iterator<Long> it = idSet.iterator();
    		Long id = 0l;
    		Money p = new Money();
    		int count = 0;
    		if(it.hasNext()){
    			id = it.next();
    			p = item.get(id).price;
    			if((p.compareTo(low)>-1)&&(p.compareTo(high)<1)){
    				count++;
    			}
    		}
    		return count;
    	}
    	return 0;
    }

    /* 
       g. PriceHike(l,h,r): increase the price of every product, whose id is
       in the range [l,h] by r%.  Discard any fractional pennies in the new
       prices of items.  Returns the sum of the net increases of the prices.
    */
    public Money priceHike(long l, long h, double rate) {


//		SortedMap<Long, ItemDetails> itemRange = item.subMap(l, h);
//		Set<Long> map = itemRange.keySet();
//		Iterator<Long> it = map.iterator();
//    	Long id = 0l, carry = 0l;
//    	Money p;
//
//    	while(it.hasNext()){
//    		id = it.next();
//    		p = item.get(id).price;
//    		if(p.c>0){
//
//    		}
//    	}
//
//    	return new Money();
		Money preSum = new Money();
		Money postSum = new Money();
		Money newPrice = new Money();
		for(long i=l;i<=h;i++){
			if(this.item.containsKey(i)){
				ItemDetails idObj = this.item.get(i);
//				System.out.println("Old price for id "+i+ " is "+idObj.price);
				preSum = this.addPrices(preSum,idObj.price);
//				System.out.println("after adding prev sum "+preSum);
				newPrice = this.incrementPrice(idObj.price,rate);
//				System.out.println("new price after increment for id "+i+ " is "+newPrice);
				postSum = this.addPrices(postSum,newPrice);
//				System.out.println("after adding post sum "+postSum);
			}
		}
		return getDifference(postSum,preSum);


    }

	private Money getDifference(Money postSum, Money preSum) {
    	double m1 = Double.parseDouble(postSum.toString());
    	double m2 = Double.parseDouble(preSum.toString());
//    	System.out.println("the increment is "+(m1-m2));
    	Money m = new Money(String.valueOf(m1-m2));
    	System.out.print(m);
    	return m;
	}


	public Money incrementPrice(Money m1, double rate){
    	Long d = m1.d;
    	int c = m1.c;
		double dRate = d+(d*rate/(double)100);
		Money dolInc = new Money(String.valueOf(dRate));

		c = c+(int)(c*rate/(double)100);;
		Money CenInc = new Money(c/100,c%100);
		return addPrices(dolInc,CenInc);
	}


    public Money addPrices(Money m1,Money m2){
    	long carryOver = 0L;
    	Money sumPrices = new Money();
    	sumPrices.c = m1.c+m2.c;
    	if(sumPrices.c > 100L){
    		carryOver = sumPrices.c/100;
    		sumPrices.c = sumPrices.c%100;
		}else{
    		carryOver = 0;
		}
		sumPrices.d = m1.d + m2.d + carryOver;
    	return sumPrices;
	}

    /*
      h. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    public long removeNames(long id, java.util.List<Long> list) {
		HashSet<Long> idDesc = new HashSet<>();
		Long sum = 0l;
		ItemDetails det;
		if(item.containsKey(id)){
			det = item.get(id);
			idDesc = det.desc;
			Iterator<Long> it = list.iterator();
			while(it.hasNext()){
				Long listElement = it.next();
				if(idDesc.contains(listElement)){
					sum+=listElement;
					//deleting corresponding ids from descLookup map
					HashSet<Long> lkupIds = descLookup.get(listElement);
					if(lkupIds.size()==1){
						descLookup.remove(listElement);
					}
					else{
						lkupIds.remove(id);
						descLookup.put(listElement, lkupIds);
					}
					//deleting the description element from item map
					idDesc.remove(listElement);
				}
			}
			det.desc = idDesc;
			item.put(id, det);
		}
		return sum;
    }
    
    // Do not modify the Money class in a way that breaks LP3Driver.java
    public static class Money implements Comparable<Money> { 
		long d;  int c;
		public Money() { d = 0; c = 0; }
		public Money(long d, int c) { this.d = d; this.c = c; }
		public Money(String s) {
		    String[] part = s.split("\\.");
		    int len = part.length;
		    if(len < 1) { d = 0; c = 0; }
		    else if(part.length == 1) { d = Long.parseLong(s);  c = 0; }
		    else {
		    	d = Long.parseLong(part[0]);
		    	if(part[1].length() >= 2){
					c = Integer.parseInt(part[1].substring(0,2));
				}else{
					c = Integer.parseInt(part[1])*10;
				}

		    }
		}
		public long dollars() { return d; }
		public int cents() { return c; }
		public int compareTo(Money other) { // Complete this, if needed
			if(this.d>other.d || (this.d==other.d && this.c>other.c)){
				return 1;
			}
			else if(this.d<other.d || (this.d==other.d && this.c<other.c)){
				return -1;
			}
			else{
				return 0;
			}
		}
		public String toString() {
			if(c > 9){
				return d + "." + c;
			}else{
				return d + ".0" + c;
			}
		}
    }
    
    public static class ItemDetails{
    	
    	Money price;
    	HashSet<Long> desc = new HashSet<Long>();
    	
    	public ItemDetails(Money p, java.util.List<Long> list) {
			// TODO Auto-generated constructor stub
    		this.price = p;
    		ListIterator<Long> li = list.listIterator();
    		while(li.hasNext()){
    			this.desc.add(li.next());
    		}
		}
    }

}