import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.scene.control.*;

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

/**
 * Implements a pop out window that displays at the location of the host
 * in the map and its full description
 * @author xia yanjing
 * @version 18/03/2021
 */
public class PropertiesFullDetails {
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    public  void display(AirbnbListing currentProperty)
    {
        Stage stage=new Stage();
        //make sure only one window pops up
        stage.initModality(Modality.APPLICATION_MODAL);
        // show all information about the property
        Label propertyInformation=new Label("Property Information");
        propertyInformation.setId("title");
        Label propertyName = new Label("Description: " + currentProperty.getName());
        Label PropertyId = new Label("Property ID: " + currentProperty.getId());       
        Label priceProperty=new Label("Property price: " + currentProperty.getPrice());
        Label minNights = new Label("Minimum night: " + currentProperty.getMinimumNights());
        Label neighbourhood = new Label("Neighbourhood: " + currentProperty.getNeighbourhood());
        Label roomType = new Label("Room type: " + currentProperty.getRoom_type());
        Label latitude =new Label("Latitude: "+currentProperty.getLatitude());
        Label longitude =new Label("Longitude: "+currentProperty.getLongitude());
        Label availability365 = new Label("Availability 365: " + currentProperty.getAvailability365());
        // show all information about property reviews
        Label reviewInformation=new Label("Information about the reviewes");
        reviewInformation.setId("title");
        Label numberReviews=new Label("Number of reviews: " + currentProperty.getNumberOfReviews());
        Label lastReview = new Label("Last Review: " + currentProperty.getLastReview());
        Label reviewsPerMonth = new Label("Reviews Per Month: " + currentProperty.getReviewsPerMonth());
        //show property host information
        Label hostInformation=new Label("Information about hosts");
        hostInformation.setId("title");
        Label hostId = new Label("Host ID: " + currentProperty.getHost_id());
        Label calculatedHostListingsCount = new Label("Calculated Host Listings Count: " + currentProperty.getCalculatedHostListingsCount());
        
        Button buy=new Button("Book Now");
        buy.setOnAction(e->openBookingWindow(e,currentProperty));
        
        Pane detailsbox=new VBox();
        detailsbox.setId("fullDetailsBox");
        detailsbox.getChildren().addAll(propertyInformation,propertyName,PropertyId,priceProperty,minNights,neighbourhood,
                                    roomType,latitude,longitude,availability365,reviewInformation,numberReviews,
                                    lastReview,reviewsPerMonth,hostInformation,hostId,calculatedHostListingsCount,buy);
        Pane detailsContainer=new AnchorPane();
        detailsContainer.getChildren().add(detailsbox);
        AnchorPane.setBottomAnchor(detailsbox, 0.0);
        AnchorPane.setLeftAnchor(detailsbox, 0.0);
        AnchorPane.setRightAnchor(detailsbox, 0.0);
        AnchorPane.setTopAnchor(detailsbox, 0.0);

        // create the left panel of the splitPane in which contain the map
        ScrollPane mapContainer=new ScrollPane();
        GoogleMaps googleMap=new GoogleMaps(currentProperty.getLatitude(),currentProperty.getLongitude() );
        mapContainer=googleMap.getGoogleWebView();

        SplitPane splitPane=new SplitPane();
        splitPane.getItems().addAll(mapContainer,detailsContainer);
        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setContent(splitPane);
        scrollPane.setMaxSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        VBox.setVgrow(scrollPane,Priority.ALWAYS);

        // the root
        Pane root=new VBox();
        root.getChildren().add(scrollPane);
        root.setMinSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        root.setMaxSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        root.setPrefSize(500, 500);

        Scene scene= new Scene(root);
        scene.getStylesheets().add("propertyDetail.css");

        stage.setTitle("Property Full Description");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }  
    
    /**
     * open the booking panel when the property has enought available nights
     *
     * @param currentProperty The property
     */
    private void openBookingWindow(ActionEvent event,AirbnbListing currentProperty)
    {
        BookingPanel bookingPanel;
        if(currentProperty.getAvailability365()>=currentProperty.getMinimumNights())
             bookingPanel=new BookingPanel(currentProperty);
        else
            alertPropertyNotAvailable();
    }
    
    /**
     * Pop out window to warn the client that the chosen property is not available for booking
     *
     */
    private void alertPropertyNotAvailable()
    {
        Alert alert=new Alert(AlertType.WARNING);
        alert.setTitle("Booking alert");
        alert.setHeaderText(null);
        alert.setContentText("Property not available for booking ");
        alert.showAndWait();
    }
}