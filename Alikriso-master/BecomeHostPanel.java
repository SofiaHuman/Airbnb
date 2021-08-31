import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URISyntaxException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;


/**
 * This builds "Become host panel" of the airbnb project
 *
 * @Xiaocheng Liang
 * @version 6th April 2021
 */
public class BecomeHostPanel extends Application
{
    // We keep track of the count, and label displaying the count:
    @FXML
    private Button submitButton; 

    @FXML
    private TextField nameBox; 

    @FXML
    private ComboBox boroughBox;
    
    @FXML
    private TextField longitudeBox;    

    @FXML
    private TextField latitudeBox; 
    
    @FXML
    private TextField propertyNameBox;  
    
    @FXML
    private ComboBox roomTypeBox; 

    @FXML
    private TextField numberOfNightBox;

    @FXML
    private TextField availabilityBox;

    @FXML
    private TextField priceBox;

    private ArrayList<String> listOfBoroughName;
    
    private ArrayList<String> listOfRoomType;
    
    private StatsLoader statsloader;
  
    private boolean successfully_update;
    
    private ArrayList<String> ListOfAlert;
    
    /**
     * Initiate the JavaFX application, load data from the statsLoader.
     */
    @FXML
    private void initialize() {
        statsloader = new StatsLoader();
        statsloader.load();
        listOfBoroughName = statsloader.getBoroughName();
        for(String boroughName: listOfBoroughName)
        {
            boroughBox.getItems().add(boroughName);       
        }
        
        listOfRoomType = new ArrayList<String>();
        listOfRoomType.add("Private room");
        listOfRoomType.add("Entire home/apt");
        listOfRoomType.add("Shared room");
        for(String roomType: listOfRoomType)
        {
            roomTypeBox.getItems().add(roomType);       
        }
        
        ListOfAlert = new ArrayList<String>();
        
    }

    /**
     * To start the javaFX application and load URL
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) throws Exception 
    {
        URL url = getClass().getResource("becomeHostPanel.fxml");
        Pane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
    }

    /**
     * Act when submit button is clicked
     * @param event The mouse click event
     */
    @FXML
    private void submitButtonClick(MouseEvent event)
    {
        // Counts number of button clicks and shows the result on a label
        successfully_update = true;
        
        ListOfAlert.clear();
        
        String name = nameBox.getText();
        checkIfEmpty(name, "Your name");
        
        String propertyName = propertyNameBox.getText();
        checkIfEmpty(propertyName, "Name/short description of the property");
        
        String latitude = latitudeBox.getText();
        // if not empty check if the string can be converted to a double
        if(!checkIfEmpty(latitude, "latitude"))
        {
            checkStringToDouble(latitude, "latitude");
        }
        
        String longitude = longitudeBox.getText();
        if(!checkIfEmpty(longitude, "longitude"))
        {
            checkStringToDouble(longitude, "longitude");
        }
        
        String MinimumNumberOfNight = numberOfNightBox.getText();
        if(!checkIfEmpty(MinimumNumberOfNight, "Minimum number of night"))
        {
            checkStringToInt(MinimumNumberOfNight, "minNight");  
        }

        String availability = availabilityBox.getText();
        if(!checkIfEmpty(availability, "Availability over 365 days"))
        {
            checkStringToInt(availability, "availability"); 
        }

        String priceForOneNight = priceBox.getText();
        if(!checkIfEmpty(priceForOneNight, "Price for one night"))
        {
            checkStringToInt(priceForOneNight, "price");
        }
      
        String boroughName = (String)boroughBox.getValue();
        checkStringInList(listOfBoroughName, boroughName);
        
        String roomType = (String)roomTypeBox.getValue();
        checkStringInList(listOfRoomType, roomType);
        
        // property id is the  largest previous id +1, so there will not be duplicate
        int propertyID = statsloader.getNewPropertyID();
        int hostID = statsloader.getNewHostID();
        int hostIDCount = statsloader.hostListingsCount(hostID);
        
        printEmptyAlert();
        
        // if all input are ok
        if(successfully_update)

        {
            ArrayList<String> dataList = new ArrayList<>();
            dataList.add(Integer.toString(propertyID)); //id
            dataList.add(propertyName);//name of property
            dataList.add(Integer.toString(hostID)); // hostid
            dataList.add(name); // host name
            dataList.add(boroughName); // borough name
            dataList.add(latitude); // latitude
            dataList.add(longitude); // longitude
            // *
            dataList.add(roomType); //room type
            dataList.add(priceForOneNight); // price
            dataList.add(MinimumNumberOfNight); //min number of nights
            dataList.add("0"); //number of reviews
            dataList.add(""); // last review time
            dataList.add(""); // reviews per month
            dataList.add(Integer.toString(hostIDCount)); // calculated host listings count
            dataList.add(availability); // availability
            //dataList.add(location); // additional box for storing location
            
            
            updateData(dataList);
            
            Alert alert = new Alert(AlertType.INFORMATION, "[Successfully submitted] We have posted your property!", ButtonType.OK);
            alert.showAndWait();            
        }
    }
    
    /**
     * Check the convertion from string to double is successful
     * @param stringToConvert The string that needs to be converted to double
     * @typeData The type of data
     */
    private void checkStringToDouble(String stringToConvert, String typeOfData)
    {
        double doubleValue = 0.0;
        String informTitle = "";
        String additionalInfo = "";
        try
        {
            doubleValue = Double.parseDouble(stringToConvert);
            boolean additionalAlert = false;
            //doubleValue>=0.28||doubleValue <=-0.5
            if(typeOfData.equals("longitude") && ((Double.compare(-0.5, doubleValue) > 0) || (Double.compare(doubleValue, 0.28) > 0)))
            {
                informTitle = "longitude value";
                additionalInfo = "over -0.5 and below 0.25 for a location in London";
                additionalAlert = true;
            }
            
            // data between 51.7 and 51.2
            else if(typeOfData.equals("latitude")  && ((Double.compare(51.2, doubleValue) > 0) || (Double.compare(doubleValue, 51.7) > 0)))
            {
                informTitle = "latitude value";
                additionalInfo = "over 51.2 and below 51.7 for a location in London";
                additionalAlert = true;
            }
            
            if(additionalAlert)
            {
                Alert alert = new Alert(AlertType.WARNING, "[" + informTitle + "] should be a double " + additionalInfo, ButtonType.OK);
                alert.showAndWait();
                successfully_update = false;
            }
        }
        catch (NumberFormatException ex) 
        {
            if(typeOfData.equals("longitude"))
            {
                informTitle = "longitude value";
            }
            
            else if(typeOfData.equals("latitude"))
            {
                informTitle = "latitude value";
            }
            else
            {
                informTitle = "?";
            }
            Alert alert = new Alert(AlertType.WARNING, "[" + informTitle + "] should be a double", ButtonType.OK);
            alert.showAndWait();
            successfully_update = false;
            //handle exception here
            // if (alert.getResult() == ButtonType.YES) {
            // //do stuff
            // 
        }
    }
    
    /**
     * Check the convertion from string to integer is successful
     * @param stringToConvert The string that needs to be converted to integer
     * @typeData The type of data
     */
    private void checkStringToInt(String stringToConvert, String typeOfData)
    {
        int intValue = 0;
        String informTitle = "";
        String additionalInfo = "";
        try
        {
            intValue = Integer.parseInt(stringToConvert);
            boolean additionalAlert = false;
            //additional alert for price
            if(typeOfData.equals("price") && (intValue<=0))
            {
                informTitle = "Price for one night";
                additionalInfo = "over 0";
                additionalAlert = true;
            }
            
            else if(typeOfData.equals("availability") && (intValue<=0 || intValue>365))
            {
                informTitle = "Availability over 365 days";
                additionalInfo = "over 0 and below/equals 365";
                additionalAlert = true;
            }
            
            else if(typeOfData.equals("minNight") && (intValue<=0))
            {
                informTitle = "minimum number of night";
                additionalInfo = "over 0";
                additionalAlert = true;
            }
            
            if(additionalAlert)
            {
                Alert alert = new Alert(AlertType.WARNING, "[" + informTitle + "] should be an integer " + additionalInfo, ButtonType.OK);
                alert.showAndWait();
                successfully_update = false;
            }
        }
        catch (NumberFormatException ex) 
        {
            //handle exception here
            if(typeOfData.equals("price"))
            {
                informTitle = "Price for one night";
            }
            else if(typeOfData.equals("availability"))
            {
                informTitle = "Availability over 365 days";
            }
            
            else if(typeOfData.equals("minNight"))
            {
                informTitle = "minimum number of night";
            }
            else
            {
                informTitle = "?";
            }
            Alert alert = new Alert(AlertType.WARNING, "[" + informTitle + "] should be an integer", ButtonType.OK);
            alert.showAndWait();
            successfully_update = false;
        }
    }
    
    /**
     * Check if a string is empty, if it is empty add it into the alert list.
     * @param stringToCheck The string that needs to be checked
     * @param alertInfo The title of the alert that should be given if the string is empty
     */
    private boolean checkIfEmpty(String stringToCheck, String alertInfo)
    {
        if(stringToCheck.isEmpty())
        {
            ListOfAlert.add(alertInfo);
            successfully_update = false;
        }
        return stringToCheck.isEmpty();
    }
    
    /**
     * Show the merged alert that shows all fields that are empty
     */
    private void printEmptyAlert()
    {
        if(ListOfAlert.size() > 0)
        {
            String missingData = "";
            for(String alert: ListOfAlert)
            {
                missingData  = missingData + "[" + alert + "]" + "\n";
            }
            Alert alert = new Alert(AlertType.WARNING,  missingData + " should not be empty", ButtonType.OK);
            alert.showAndWait();
            successfully_update = false;
        }
    }
    
    /**
     * Check if the string is in the list
     * @param listToCheck The list where the string should be included
     * @param stringtoCheck The string that should be checked
     */
    private void checkStringInList(ArrayList<String> listToCheck, String stringToCheck)
    {
        boolean selected = true; 
        if(stringToCheck == null)
        {
            selected = false;
        }

        if(!selected)
        {
            String informText = "";
            if(listToCheck == listOfBoroughName)
            {
                informText = "Area/borough name";
            }
            else if(listToCheck == listOfRoomType)
            {
                informText = "Room type";
            }
            ListOfAlert.add(informText);
        }
    }
    
    /**
     * Update the data of the csv file
     * @param inputData The ArrayList of data that should be put into the file
     */
    private void updateData(ArrayList<String> inputData)
    {
        UserDataExport writeInFile=new UserDataExport();
        writeInFile.connectToUserFile("airbnb-london.csv");
        for(String ele: inputData)
        {
            writeInFile.uploadData(ele);
        }
        writeInFile.setNewLine();
    }

}
