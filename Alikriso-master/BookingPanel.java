import lang.stride.*;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import java.util.LinkedHashMap;
import java.util.Iterator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;
import javax.mail.SendFailedException;
import javafx.geometry.Pos;
/**
 * This is the panel in which client's insert their data and confirm booking
 * @author xiayanjing 
 * @version 04/04/2021
 */
public class BookingPanel
{
    private AirbnbListing currentProperty;
    private int nightsBooked;
    private ArrayList<Label> labelList;
    private LinkedHashMap<String,TextField> textFields;
    private Stage stage =  new  Stage();
    private UserDataExport writeInFile=new UserDataExport();

    /**
     * Initialize the booking panel
     * @param currentProperty the property the client has chosen
     */
    public BookingPanel(AirbnbListing currentProperty)
    {
        this.currentProperty = currentProperty;
        nightsBooked = currentProperty.getMinimumNights();
        initializeBookingPanel();
    }

    /**
     * Initialize the booking panel
     */
    public void initializeBookingPanel()
    {
        implementLabels();
        implementTextFields();
        
        Button addNightButton =  new  Button("+");
        addNightButton.setOnMouseClicked(e -> addNights(e));
        Button removeNightButton =  new  Button("-");
        removeNightButton.setOnMouseClicked(e -> removeNights(e));
        Pane nightNumberSelectBox =  new  HBox();
        nightNumberSelectBox.getChildren().addAll(removeNightButton, textFields.get("numberOfNights"), addNightButton);
        nightNumberSelectBox.setId("nightNumberSelectBox");
        
        GridPane formContainer =  new  GridPane();
        Iterator it = textFields.values().iterator();
        int j = 0;
        //add items to the grid pane
        while (j < textFields.size()) {
            if (labelList.get(j).getText() == "Number of Nights") 
            {
                formContainer.addRow(j, labelList.get(j), nightNumberSelectBox);
                it.next();
            }
            else 
                formContainer.addRow(j, labelList.get(j), (TextField)it.next());
            j++;
        }
        
        // set gri pane layout
        formContainer.setMaxSize(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE);
        formContainer.setMinSize(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE);
        formContainer.setId("formGrid");
        //set grid pane column constraints
        ColumnConstraints labelColumn =  new  ColumnConstraints();
        labelColumn.setFillWidth(true);
        labelColumn.setHgrow(Priority.SOMETIMES);
        labelColumn.setPrefWidth(133);
        labelColumn.setMaxWidth(GridPane.USE_PREF_SIZE);
        labelColumn.setMinWidth(GridPane.USE_PREF_SIZE);
        labelColumn.setPercentWidth(-1);
        ColumnConstraints textColumn =  new  ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.SOMETIMES);
        textColumn.setPrefWidth(400);
        textColumn.setMaxWidth(GridPane.USE_COMPUTED_SIZE);
        textColumn.setMinWidth(GridPane.USE_PREF_SIZE);
        textColumn.setPercentWidth(-1);
        //set grid pane row constraints
        RowConstraints gridRow =  new  RowConstraints();
        gridRow.setFillHeight(true);
        gridRow.setVgrow(Priority.SOMETIMES);
        gridRow.setPrefHeight(30);
        gridRow.setMaxHeight(GridPane.USE_COMPUTED_SIZE);
        gridRow.setMinHeight(GridPane.USE_PREF_SIZE);
        gridRow.setPercentHeight(-1);
        //add constraints to the grid
        formContainer.getColumnConstraints().add(0, labelColumn);
        formContainer.getColumnConstraints().add(1, textColumn);
        int i = 0;
        while (i < textFields.size()) {
            formContainer.getRowConstraints().add(i, gridRow);
            i = i + 1;
        }
        
        //add grid pane to scroll pane
        ScrollPane formBox =  new  ScrollPane();
        formBox.setContent(formContainer);
        formBox.fitToHeightProperty();
        formBox.fitToWidthProperty();
        VBox.setVgrow(formBox, Priority.ALWAYS);
        
        //set the grip where i put the confirm button
        Button confirmButton =  new  Button("Confirm Booking");
        confirmButton.setOnMouseClicked(e -> uploadData(e));
        Pane confirmContainer =  new  GridPane();
        confirmContainer.getChildren().add(confirmButton);
        
        //add both grid pane to root
        Pane root =  new  VBox();
        root.getChildren().addAll(formBox, confirmContainer);
        root.setId("bookingRoot");
        root.setPrefSize(600, 400);
        
        Scene scene =  new  Scene(root);
        scene.getStylesheets().add("propertyDetail.css");
        stage.setTitle("Booking");
        stage.setScene(scene);
        stage.show();
    }

    
    /**
     * Create the text fields when clients insert their data
     *
     */
    private void implementTextFields()
    {
        textFields =  new  LinkedHashMap <  > ();
        
        TextField nightsText =  new  TextField();
        nightsText.setAlignment(Pos.CENTER);
        nightsText.setPrefWidth(60);
        nightsText.setText("" + nightsBooked);
        nightsText.setEditable(false);
        nightsText.setOnKeyTyped(e -> alertTypingDisabled(e));
        TextField nameText =  new  TextField();
        TextField surnameText =  new  TextField();
        TextField emailText =  new  TextField();
        TextField totalAmountText =  new  TextField();
        totalAmountText.setEditable(false);
        totalAmountText.setText("" + nightsBooked * currentProperty.getPrice());
        textFields.put("name", nameText);
        textFields.put("surname", surnameText);
        textFields.put("email", emailText);
        textFields.put("numberOfNights", nightsText);
        textFields.put("totalAmount", totalAmountText);
    }

    /**
     * Create labels 
     */
    private void implementLabels()
    {
        labelList =  new  ArrayList <  > ();
        Label name =  new  Label("Name");
        Label surname =  new  Label("Surname");
        Label email =  new  Label("Email");
        Label nightsN =  new  Label("Number of Nights");
        Label totalAmount =  new  Label("Total amount");
        labelList.add(name);
        labelList.add(surname);
        labelList.add(email);
        labelList.add(nightsN);
        labelList.add(totalAmount);
    }

    /**
     * Pop out window that remind the client to fill all the text field in 
     * order to finish the booking
     *
     */
    public void alertInsertData()
    {
        Alert alert =  new  Alert(AlertType.WARNING);
        alert.setTitle("Booking alert");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all the camp ");
        alert.showAndWait();
    }

    /**
     * Upload client's data into csv file
     */
    public void uploadData(MouseEvent event)
    {
        if ( ! isAllCampFiled()) 
            alertInsertData();
        else {
                try {
                        writeInFile.connectToUserFile("userDataList.csv");

                        SendEmail.send(textFields, currentProperty);
                        Iterator iterate = textFields.values().iterator();
                        while (iterate.hasNext()) {
                            writeInFile.uploadData(((TextField)iterate.next()).getText());
                        }
                        uploadPropertyInformation();
                        writeInFile.setNewLine();
                        showSuccessBooking();
                        stage.close();
                    }catch (java.lang.Exception e) {
                        alertInvalidEmail();
                    }
        }
    }

    /**
     * Check if all the text fiels are filled
     *
     * @return true if allthe camp are filled,otherwise false
     */
    private boolean isAllCampFiled()
    {
        boolean allCampFiled = true;
        Iterator it = textFields.values().iterator();
        while (allCampFiled && it.hasNext()) 
        {
            TextField text = (TextField)it.next();
            if (text.getText().isBlank()) 
                allCampFiled = false;
            
        }
        return allCampFiled;
    }
    
    /**
     * Pop out window to alert the cient when an invalid emaill is inserted
     */
    private void alertInvalidEmail()
    {
        Alert alert =  new  Alert(AlertType.WARNING);
        alert.setTitle("Invalid email");
        alert.setHeaderText(null);
        alert.setContentText("Please insert a valid email ");
        alert.showAndWait();
    }

    /**
     * Upload informatioin about the booked property
     */
    public void uploadPropertyInformation()
    {
        writeInFile.uploadData(currentProperty.getName());
        writeInFile.uploadData(currentProperty.getNeighbourhood());
    }

    /**
     * Pop out window to inform the client that the booking ended successfully
     */
    public void showSuccessBooking()
    {
        Alert alert =  new  Alert(AlertType.INFORMATION);
        alert.setTitle("Booking alert");
        alert.setHeaderText(null);
        alert.setContentText("Booking was successful, a summary is sent in your email ");
        alert.showAndWait();
    }

    /**
     * When clicked adds number of night in the booking
     */
    public void addNights(MouseEvent event)
    {
        if (checkAvailability()) {
            nightsBooked = nightsBooked + 1;
            textFields.get("numberOfNights").setText("" + nightsBooked);
            updateTotalAmount();
        }
    }

    /**
     * Check if the property is available for booking
     * @return true if available nights is greater or equal than chosen nights, 
     * otherwise false
     */
    private boolean checkAvailability()
    {
        if (nightsBooked < currentProperty.getAvailability365()) {
            return true;
        }
        else {
            alertExceedPropertyAvailability();
            return false;
        }
    }

    /**
     * Pop out window to warn the client that number of night 
     * he/she wants to book exceed the available amount of night
     */
    private void alertExceedPropertyAvailability()
    {
        Alert alert =  new  Alert(AlertType.WARNING);
        alert.setTitle("Booking alert");
        alert.setHeaderText(null);
        alert.setContentText("The property is only available for maximum of " + currentProperty.getAvailability365() + " nights");
        alert.showAndWait();
    }

    /**
     * When clicked decrease the number of nights
     */
    public void removeNights(MouseEvent event)
    {
        if (nightsBooked > currentProperty.getMinimumNights()) {
            nightsBooked = nightsBooked - 1;
            textFields.get("numberOfNights").setText("" + nightsBooked);
            updateTotalAmount();
        }
        else {
            alertMinimumNight();
        }
    }

    /**
     * Update the total amount to be paid
     */
    private void updateTotalAmount()
    {
        textFields.get("totalAmount").setText("" + nightsBooked * currentProperty.getPrice());
    }

    /**
     * Pop out window to inform the client that he/she can only choose number of nights throught buttons
     */
    private void alertTypingDisabled(KeyEvent event)
    {
        Alert alert =  new  Alert(AlertType.INFORMATION);
        alert.setTitle("Booking alert");
        alert.setHeaderText(null);
        alert.setContentText("Choose nights number only with '+' and '-' buttons ");
        alert.showAndWait();
    }

    /**
     * Pop out window to warn the client that he/she cannot book the property for less than certain amount
     */
    private void alertMinimumNight()
    {
        Alert alert =  new  Alert(AlertType.WARNING);
        alert.setTitle("Booking alert");
        alert.setHeaderText(null);
        alert.setContentText("You can't book the property for less than " + currentProperty.getMinimumNights() + " nights");
        alert.showAndWait();
    }
}
