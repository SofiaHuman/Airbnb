import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static java.util.Comparator.comparing;
import java.util.Comparator;
/**
 * Aggiungi qui una descrizione della classe HostNameSorter
 * @author Yanjing Xia, Lisha Zhu
 * @version 07/04/2021
 */
public class HostNameSorter extends SortingOptions
{
    /**
     * pass the name to superclass
     */
    public HostNameSorter(String name)
    {
        super(name);
    }

    /**
     * Apply sort by host name
     *
     * @param listing Unsorted list
     * @param sortOrder decreasing/increasing
     */
      public void sort(ArrayList<AirbnbListing> currentListings, String order)
    {
        switch (order) {
            case "Ascending":
                currentListings.sort(comparing(AirbnbListing::getHost_name));
                break;
            case "Descending":
                currentListings.sort(comparing(AirbnbListing::getHost_name).reversed());
                break;
        }
    }
}
