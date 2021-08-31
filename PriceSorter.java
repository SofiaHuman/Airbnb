import java.util.HashMap;
import javafx.scene.layout.Pane;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static java.util.Comparator.comparing;
import java.util.Comparator;
/**
 * Sort the property list by Price 
 * 
 * @author Yanjing Xia, Lisha Zhu
 * @version 07/04/2021
 */
public class PriceSorter extends SortingOptions
{
    /**
     * pass the name to superclass
     */
    public PriceSorter(String name)
    {
        super(name);
    }

    /**
     * Apply sort by price
     *
     * @param listing Unsorted list
     * @param sortOrder decreasing/increasing
     */
     public void sort(ArrayList<AirbnbListing> currentListings, String order)
    {
        switch(order){
            case "Ascending":
                currentListings.sort(comparing(AirbnbListing::getPrice));
                break;
            case "Descending":
                currentListings.sort(comparing(AirbnbListing::getPrice).reversed());
                break;
        }
    }
    
          
}
