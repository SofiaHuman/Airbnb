import java.util.TreeMap;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import java.util.Collections;

/**
 * Implement sorting
 * 
 * @author Xia yanjing, Zhu Lisha
 * @version 07/04/2021
 */
public abstract class SortingOptions
{
    private String name;

    /**
     * set the name of sort option
     */
    public SortingOptions(String name)
    {
        this.name=name;
    }

    /**
     * return the name of sort option
     *
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Apply the sort option to the property list
     * @param listing Unsorted list
     * @param sortOrder The order in which sorting will be, increasing/decreasing
     */
    abstract public void sort( ArrayList<AirbnbListing> listing, String sortOrder);
    
}
