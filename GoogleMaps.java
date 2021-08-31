
import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.lang.Object.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert.AlertType;
 import javafx.geometry.Pos;

/**
 * This class creates the google map view
 * @author xia yanjing, html file comes from https://developers.google.com/maps/documentation/javascript/markers
 * @version 1.0
 */
public class GoogleMaps {

    private WebEngine webengine;
    private WebView webview;
    private double latitude;
    private double longitude;
    private Pane mapContainer;
    /**
     * Get passed the latitude and latitude when the property is clicked
     *
     * @param latitude latitude of the property
     * @param longitude longitude of the property
     */
    public GoogleMaps(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     * Create the webview panel
     *
     * @return scroll the scrollPane that contains the google map
     */
    public ScrollPane getGoogleWebView() {
        try {
            webview = new WebView();
            webview.setVisible(true);
            webengine = webview.getEngine();
            webengine.setJavaScriptEnabled(true);
            File file = new File("googlesMaps.html");
            webengine.load(file.toURI().toURL().toString());
        } catch (Exception ex) {
            System.err.print("error " + ex.getMessage());
            ex.printStackTrace();
        }
        Button btn = new Button();
        btn.setText("View Property on Map");
        btn.setOnAction(e->createMapWithRightLocation(e));
        mapContainer = new StackPane();
        mapContainer.getChildren().add(btn);
        mapContainer.getChildren().add(webview);
        btn.toFront();
        ScrollPane scroll=new ScrollPane();
        scroll.setContent(mapContainer);
        return scroll;
    }

    /**
     * Implement the map with marker at the given position
     *
     * @param e Un parametro
     */
    public void createMapWithRightLocation(ActionEvent e) {
        if(latitude!=0.0 && longitude!=0.0)
        {
            webview.toFront();
            if (webengine != null) 
            {
                webengine.executeScript(
                    " const uluru = { lat:"+latitude+", lng:"+longitude+"};"+
                    "const map = new google.maps.Map(document.getElementById('mapView'), {"+
                    " zoom: 10,"+
                    "center: uluru,        });"+
                    "const marker = new google.maps.Marker({"+
                    "position: uluru,          map,        });      ");
    
            }
        }
        else
        {
            Label notAvailable=new Label("Map view not available");
            notAvailable.setAlignment(Pos.CENTER);
            mapContainer.getChildren().add(notAvailable);
            notAvailable.toFront();

        }
    }
  
}