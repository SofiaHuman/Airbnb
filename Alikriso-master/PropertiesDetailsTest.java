

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * The test class PropertiesDetailsTest.
 *
 * @author  Lisha Zhu
 * @version 2021.04.07
 */
public class PropertiesDetailsTest
{
    private AirbnbDataLoader dataLoader;
    private ArrayList<AirbnbListing> testListings;
    private ArrayList<AirbnbListing> sortedListings;
    
    /**
     * Default constructor for test class PropertiesDetailsTest
     */
    public PropertiesDetailsTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        dataLoader = new AirbnbDataLoader();
        testListings = dataLoader.load("test_data.csv");
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        dataLoader = null;
        testListings = null;
    }
    
    /**
     * Test whether sort by price in ascending order works properly.
     */
    @Test
    public void testSortByPriceAscending()
    {
        int expected = 23;
        
        SortingOptions PriceSorter = new PriceSorter("Price");
        PriceSorter.sort(testListings, "Ascending"); 
        
        int actual = testListings.get(0).getPrice();        
        assertEquals(expected, actual);
    }
    
    /**
     * Test whether sort by price in descending order works properly.
     */
    @Test
    public void testSortByPriceDescending()
    {
        int expected = 50;
        
        SortingOptions PriceSorter = new PriceSorter("Price");
        PriceSorter.sort(testListings, "Descending"); 
        
        int actual = testListings.get(0).getPrice();
        assertEquals(expected, actual);
    }
    
    /**
     * Test whether sort by number of reviews in ascending order works properly.
     */
    @Test
    public void testSortByReviewsAscending()
    {
        int expected = 0;
        
        SortingOptions ReviewSorter = new ReviewSorter("Reviews");
        ReviewSorter.sort(testListings, "Ascending"); 
        
        int actual= testListings.get(0).getNumberOfReviews();       
        assertEquals(expected, actual);
    }
    
    /**
     * Test whether sort by number of reviews in descending order works properly.
     */
    @Test
    public void testSortByReviewsDescending()
    {
        int expected = 15;
        
        SortingOptions ReviewSorter = new ReviewSorter("Reviews");
        ReviewSorter.sort(testListings, "Descending"); 
        
        int actual = testListings.get(0).getNumberOfReviews();
        
        assertEquals(expected, actual);
    }
    
    /**
     * Test whether sort by host name alphabetically in ascending order works properly.
     */
    @Test
    public void testSortByHostAscending()
    {
        AirbnbListing expectedListing = testListings.get(1);
        
        SortingOptions HostNameSorter = new HostNameSorter("Host Name");
        HostNameSorter.sort(testListings, "Ascending"); 
        
        AirbnbListing actualListing = testListings.get(0);
        
        assertEquals(expectedListing, actualListing);
    }
    
    /**
     * Test whether sort by host name alphabetically in descending order works properly.
     */
    @Test
    public void testSortByHostDescending()
    {
        AirbnbListing expectedListing = testListings.get(2);
        
        SortingOptions HostNameSorter = new HostNameSorter("Host Name");
        HostNameSorter.sort(testListings, "Descending"); 
        
        AirbnbListing actualListing = testListings.get(0);
        
        assertEquals(expectedListing, actualListing);
    }
}
