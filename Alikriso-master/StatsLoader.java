import java.util.ArrayList;
import com.opencsv.CSVReader;
import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Formatter;
import javafx.scene.layout.Pane;
import javafx.util.Pair;


/**
 * load stats that useful for statsPanel and calculate data from the stats
 *
 * @XiaochengLiang
 * @version 6th April 2021
 */
public class StatsLoader
{
    ArrayList<Integer> listOfNumberOfReviews = new ArrayList<>();
    ArrayList<String> listOfNameOfNeighbourhood = new ArrayList<>();
    ArrayList<Integer> listOfNumberOfAvailability = new ArrayList<>(); 
    ArrayList<String> listOfIdOfRoomType= new ArrayList<>();
    ArrayList<Integer> listOfIdOfEntireHome = new ArrayList<>();
    ArrayList<Integer> listOfPropertyExpense = new ArrayList<>();
    //lists for additional stats table
    ArrayList<String>listOfNameOfboroughName = new ArrayList<>();
    ArrayList<Float>ListOfCrimeRate = new ArrayList<>();
    ArrayList<Float>ListOfResidentIncome = new ArrayList<>();
    ArrayList<Float>ListOfPM2_5Value = new ArrayList<>();
    ArrayList<Float>ListOfCovid19InfectionRate = new ArrayList<>();
    // lists for becomeHost Panel
    ArrayList<Integer>listOfPropertyID = new ArrayList<>();
    ArrayList<Integer>listOfHostID = new ArrayList<>();
    /**
     * load data from airbnb-london.csv file and sdditional_stats.csv
     */
    public ArrayList<Integer> load() {        
        try{
            URL url = getClass().getResource("airbnb-london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {   
                String neighbourhood = line[4];
                listOfNameOfNeighbourhood.add(neighbourhood);
                
                int numberOfReviews = convertInt(line[10]);               
                listOfNumberOfReviews.add(numberOfReviews);                
                                
                int availability365 = convertInt(line[14]);
                listOfNumberOfAvailability.add(availability365); 
                
                String room_type = line[7];
                listOfIdOfRoomType.add(room_type);
                
                if(room_type.equals("Entire home/apt"))
                {
                    String id = line[0];
                    listOfIdOfEntireHome.add(Integer.parseInt(id));
                }
                
                int price = convertInt(line[8]);
                int minimumNights = convertInt(line[9]);
                int expense = price * minimumNights;
                listOfPropertyExpense.add(expense);
                
                //for becomeHost panel
                int propertyID = convertInt(line[0]);
                listOfPropertyID.add(propertyID);
                
                int HostID = convertInt(line[2]);
                listOfHostID.add(HostID);
            }
            
            // load data from additional_stats.csv
            URL additionalUrl = getClass().getResource("additional_stats.csv");
            CSVReader additionalReader = new CSVReader(new FileReader(new File(additionalUrl.toURI()).getAbsolutePath()));
            String [] additionalLine;
            //skip the first row (column headers)
            additionalReader.readNext();
            while ((additionalLine = additionalReader.readNext()) != null) {   
                String boroughName = additionalLine[0];
                listOfNameOfboroughName.add(boroughName);
                
                float crime_rate = Float.parseFloat(additionalLine[1]);
                ListOfCrimeRate.add(crime_rate);
                
                float resident_income = Float.parseFloat(additionalLine[2]);
                ListOfResidentIncome.add(resident_income);
                
                float PM2_5 = Float.parseFloat(additionalLine[3]);
                ListOfPM2_5Value.add(PM2_5);
                
                float infectionRate = Float.parseFloat(additionalLine[4]);
                ListOfCovid19InfectionRate.add(infectionRate);
            }
            } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong when generate stats loader");
            e.printStackTrace();
        }
        calculateAverage(listOfNumberOfReviews);
        return listOfNumberOfReviews;
    }
    
    /**
     * get the number of properties in the airbnb-london.csv" file
     * @ return The string value that shows the number of properties in the file
     */
    public String getNumberOfProperties()
    {
        return(Integer.toString(listOfNumberOfAvailability.size()));
    }
    
    /**
     * Calculate the average review number in the airbnb-london.csv" file
     * @return The string the shows the average review number
     */
    public String getAverageReviewNumber()
    {
        return(calculateAverage(listOfNumberOfReviews));         
    }
    
    /**
     * Get the sorted data of the properties availability in the format "pair" 
     * (borough name, number of borough available)
     * @rturn ArrayList<pair> that show the borough name and number of borough available
     */
    public ArrayList<Pair> getPropertiesSorted(){
        return(sortByBorough(listOfNumberOfAvailability, true, null, null));
    }
    
    /**
     * Calculate average of given ArrayList
     * @param values The arraylist that needs to calculate average
     * @return  The string value that shows average value of the given ArrayList
     */
    private String calculateAverage(ArrayList<Integer> values){
        double total = 0;
        for(Integer value: values)
        {
            total += value;
        }
        double average = total/(values.size());
        //convert to 2 decimal
        Formatter formatter = new Formatter();
        formatter.format("%.2f", average);
        String result = formatter.toString();
        return result;
    }    
    
    /**
     * Get the number of reviews data sorted by borough
     * @return The Arraylist that shows the reviews data sorted by borough
     */
    public ArrayList<Pair> updateReviewsStats()
    {
        return (sortByBorough(listOfNumberOfReviews, false, null, null));
    }
    
    /**
     * Get the number of entire room in airbnb-london.csv
     * @return The string that shows the number of entire room
     */
    public String getNumberOfEntireRoom()
    {
        return(Integer.toString(listOfIdOfEntireHome.size()));         
    }
    
    /**
     * Get the ArrayList that show the number of entire room sorted by boroughs
     * @return The ArrayList that shows the number of entire room sorted by boroughs
     */
    public ArrayList<Pair> NumberOfEntireRoomsChart()
    {
        return (sortByBorough(listOfIdOfEntireHome, true, listOfIdOfRoomType, "Entire home/apt"));
    }
    
    /**
     * Get the righestBorough by comparing the sum of property expenses in each borough
     * @return the name of the richest borough
     */
    public String boroughExpenseData()
    {
        ArrayList<Pair> data = sortByBorough(listOfPropertyExpense, false,null, null);
        int max = 0;
        String richestBorough = null;
        for(Pair value: data)
        {
            int cur = (int)value.getValue();
            if(cur > max)
            {
                max = cur;
                richestBorough = (String)value.getKey();
            }
        }
        String result = Integer.toString(max);
        return richestBorough;
    }
    
    /**
     * Get the borough expense sorted by boroughs
     * @return The ArrayList that shwos the borough expense sorted by boroughs
     */
    public ArrayList<Pair> boroughExpenseChart()
    {
        return (sortByBorough(listOfPropertyExpense, false, null, null));
    }
    
    /**
     * Get the borough crime rate sorted by borough
     * @return The ArrayLit that shows the crime rate sorted by borough
     */
    public ArrayList crimeRateByBorough()
    {
        return(getPairData(ListOfCrimeRate));
    }
    
    /**
     * Get the name of the borough with the highest crime rate
     * @return The name of the borough with the highest crime rate
     */
    public String highestCrimeRateBorough()
    {
        return getBoroughWithHighestValue(ListOfCrimeRate);
    }
    
    /**
     * Get the resident income sorted by borough
     * @return The ArrayList that shows the resident income sorted by borough
     */
    public ArrayList getResidentIncome()
    {
        return(getPairData(ListOfResidentIncome));
    }
    
    /**
     * Get the average resident incom
     * @return The string that shows the average resident incom
     */
    public String getAverageResidentIncome()
    {
        Double total = 0.0;
        for(int i = 0; i < ListOfResidentIncome.size(); i++)
        {
            double number = ListOfResidentIncome.get(i);
            total += number;
        }
        double average = total/(ListOfResidentIncome.size());
        String average2Decimal = String.format("%.2f", average);
        //String averageString = Double.toString(average);
        return average2Decimal;
    }
    
    /**
     * Get the PM2.5 value sorted by borough
     * @return The ArrayList that shows the PM2.5 value sorted by borough
     */
    public ArrayList getPM2_5Data()
    {
        return(getPairData(ListOfPM2_5Value));
    }
    
    /**
     * Get the name of borough that has the highest PM2.5 value
     * @return The name of borough that has the highest PM2.5 value
     */
    public String boroughWithHighestPM2_5()
    {
        return getBoroughWithHighestValue(ListOfPM2_5Value);
    }
    
    /**
     * Get the covid-19 infection data sorted by borough
     * @return The ArrayList that shows the covid-19 infection data sorted by borough
     */
    public ArrayList getCovidInfectionData()
    {
        return(getPairData(ListOfCovid19InfectionRate));
    }
    
    /**
     * Get the borough with the highest covid-19 infection rate
     * @return The name of the borough whcih has the highest infection rate
     */
    public String boroughWithHighestCovidInfectionRate()
    {
        return getBoroughWithHighestValue(ListOfCovid19InfectionRate);
    }
    
    /**
     * Get the list of borough name 
     * @return The ArrayList that shows the borough name
     */
    public ArrayList<String> getBoroughName()
    {
        return listOfNameOfboroughName;
    }
    
    /**
     * Convert the String value into int
     * @return The integer that converted from the param intString
     * @param intString The string that needs to be converted to integer
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }
    
    /**
     * Get the Arraylist of pair from given data list
     * @param dataList the ArrayList that needed to be sorted by borough
     * @return The ArrayList of pair sorted from given data list
     */
    public ArrayList getPairData(ArrayList<Float> dataList)
    {
        ArrayList<Pair> data = new ArrayList<Pair>();
        for(int i = 0; i < dataList.size(); i++)
        {
            String name = listOfNameOfboroughName.get(i);
            Float number = dataList.get(i);
            Pair<String, Float> dataPair= new Pair(name, number);
            data.add(dataPair);
        }
        return data;
    }
    
    /**
     * Get the borough name that has the highest value
     * @param dataList The ArrayList that used to find the highest value
     * @return The name of the borough that has the highest value
     */
    public String getBoroughWithHighestValue(ArrayList<Float> dataList)
    {
        int indexOfLargestData = 0;
        float largestData = 0;
        for(int i = 0; i < dataList.size(); i++)
        {
            Float number = dataList.get(i);
            if(number> largestData)
            {
                indexOfLargestData = i;
                largestData = number;
            }
        }
        return listOfNameOfboroughName.get(indexOfLargestData);
    }

    // when count is true, only count the total number of properties(do not add up the specific value)
    // when count is false, get the data from each row.
    /**
     * Sort the given arraylist by borough
     * @param dataList The ArrayList that needed to be sorted
     * @param count If true, return the number of properties that meet the condition, 
     * if false, retrieve the data from the file and return the sum value of the 
     * data.
     * @param additionCond The Arraylist that has additional condition that needs to be fulfilled
     * @param additionCondKey The String that the additionCond needs be the same for the property 
     * to be considered.
     * @return The ArrayList that shows the sorted data by borough
     */
    public ArrayList sortByBorough(ArrayList<Integer>dataList, boolean count, ArrayList<String>additionCond, String additionCondKey)
    {
        ArrayList<Pair> data = new ArrayList<Pair>();
        String name = listOfNameOfNeighbourhood.get(0);
        int sum = 0;
        for(int i = 0; i < (listOfNameOfNeighbourhood.size()); i++)
        {
            //the sum data for one borough            
            if(listOfNameOfNeighbourhood.get(i).equals(name))
            {
                if(additionCondKey == null || additionCondKey.equals(additionCond.get(i)) )
                {
                    if(!count)
                    {
                        sum += dataList.get(i); 
                    }
                    else
                    {
                        sum ++;
                    }
                }
            }
            else
            {
                Pair<String, Integer> pair = new Pair(name, sum);
                boolean repeatedName = false;
                for(int s = 0; s<data.size(); s++)
                {
                    String curName = (String)data.get(s).getKey();
                    if(curName.equals(name))
                    {
                        int finValue = sum + (int)data.get(s).getValue();
                        Pair<String, Integer> newPair = new Pair(curName, finValue);
                        data.set(s, newPair);   
                        repeatedName = true;
                    }
                }
                if(!repeatedName)
                {
                    data.add(pair);
                    
                }
                
                name = listOfNameOfNeighbourhood.get(i);
                //sum = dataList.get(i);
                if(!count)
                {
                    sum = dataList.get(i);
                }
                else
                {
                    sum = 1;
                }
            }
        }  
        return data;            
    }
    
    //generate a new id for the become host panel
    /**
     * Generate a new property id
     * @return The new property id
     */
    public int getNewPropertyID()
    {
        int largestID = 0;
        for(int id: listOfPropertyID)
        {
            if(id > largestID)
            {
                largestID = id;
            }
        }
        int newID = largestID + 1;
        listOfPropertyID.add(newID);
        return newID;
    }
    
    /**
     * Generate new host id
     * @return The new host id
     */
    public int getNewHostID()
    {
        int largestID = 0;
        for(int id: listOfHostID)
        {
            if(id > largestID)
            {
                largestID = id;
            }
        }
        int newID = largestID + 1;
        listOfHostID.add(newID);
        return newID;
    }
    
    /**
     * Count the number of times that the given host id appears in the csv file
     * @param id ID that needs to be count number of appearance.
     * @return The number of times that the id appears in the csv file
     */
    public int hostListingsCount(int id)
    {
        int count = 0;
        for(int idNumber:listOfHostID)
        {
            if(idNumber == id)
            {
                count +=1;
            }
        }
        return count;
    }
    
}
