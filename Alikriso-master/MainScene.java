import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.input.ContextMenuEvent;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
/**
 * Main scene of the Airbnb.
 * @author Lisha Zhu, Yanjing Xia, Xiaocheng Liang, Jiajunhe Luo
 * @version 1.0
 */
public class MainScene extends Application
{   
    // price range in drop-down boxes for users to select.
    ObservableList<Integer> priceFromList = FXCollections.observableArrayList(0,50,100,200,500,800,1000,1500,2000,5000);
    ObservableList<Integer> priceToList = FXCollections.observableArrayList(50,100,200,500,800,1000,1500,2000,5000,7000);

    private MapPanel mapPanel=new MapPanel();

    private Pane welcomePane;

    private Pane mapPane;

    private Pane statsPane;

    private Pane becomeHostPane; 
    // current panel presented in main scene
    private Pane currentPane;

    // minimum price the user can accept
    public static int priceFrom;
    // maximum price user can accept
    public static int priceTo;

    // drop-down boxes to select price
    @FXML
    private  ChoiceBox priceFromBox;

    @FXML
    private  ChoiceBox priceToBox;

    // buttons to switch between panels
    @FXML
    private Button forwardButton;

    @FXML
    private Button backwardButton;

    @FXML
    private BorderPane root;

    private  AirbnbDataLoader airbnbLoader ;
    // original listings from csv file
    private  ArrayList<AirbnbListing> listings;
    // listings filter by price range
    public static  ArrayList<AirbnbListing> filteredListings = new ArrayList<>();
    /**
     * Initialize the main scene.
     */           
    @FXML
    private void initialize() throws Exception
    {       
        // load the original data
        loadCsvFile();
        // connect to gmail API
        SendEmail.connectToGmailServer();

        // add the price range into choice boxes
        priceFromBox.setItems(priceFromList);
        priceToBox.setItems(priceToList);

        // load welcome panel
        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("welcome panel.fxml"));
        welcomePane = welcomeLoader.load();

        // display welcome pane first
        root.setCenter(welcomePane);
        currentPane = welcomePane;

        //load map panel
        FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("map panel.fxml"));
        mapPane = mapLoader.load();
        mapPanel = mapLoader.getController();

        // load stats panel
        FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("stats_panel.fxml"));
        statsPane = statsLoader.load();

        //load becomeHost panel
        FXMLLoader becomeHostLoader = new FXMLLoader(getClass().getResource("becomeHostPanel.fxml"));
        becomeHostPane = becomeHostLoader.load();
        
        // set default price range
        priceFromBox.setValue(0);
        priceToBox.setValue(7000);
    }

    /**
     * Set priceFrom to the user's selected price.
     * @param event click on priceFrom box
     */
    @FXML
    private void setPriceFrom(ActionEvent event)
    {
        int priceFrom1 = (int)priceFromBox.getValue();
        if(priceFrom1 > priceTo){ // invalid price range
            showInvalidPriceRange();
            priceFromBox.setValue(priceFrom);
        }else{
            priceFrom = priceFrom1;
            act();
        }
    }

    /**
     * Set priceTo to the user's selected price.
     * @param event click on priceTo box
     */
    @FXML
    private void setPriceTo(ActionEvent event)
    {
        int priceTo1 = (int)priceToBox.getValue();
        if(priceFrom > priceTo1){ // invalid price range
            showInvalidPriceRange();  
            priceToBox.setValue(priceTo);
        }else{
            priceTo = priceTo1;
            act();
        }
    }

    /**
     * React to valid price range.
     */
    public void act()
    {
        filterListings();
        mapPanel.generateMapDensity();
    }
    
    /**
     * Filter the listings by price range.
     */
    public void filterListings()
    {
        filteredListings.clear();
        listings.stream()
        .filter(l -> l.getPrice() >= priceFrom && l.getPrice() <= priceTo)
        .forEach(l -> filteredListings.add(l));
    }  

    /**
     * If the price range is invalid, show the waring window.
     */
    private void showInvalidPriceRange()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Price Range Error");
        alert.setHeaderText(null);
        alert.setContentText("The 'from' price cannot be greater than the 'to' price.");
        alert.showAndWait();
    }

    /**
     * Action taken when forward button is pressed.
     * @param event click on forward button.
     */
    @FXML
    private void forwardPressed(ActionEvent event)
    {
        if(currentPane == welcomePane){ 
            root.setCenter(mapPane);
            currentPane = mapPane;
        }
        else if(currentPane == mapPane){
            root.setCenter(statsPane);
            currentPane = statsPane;
        }
        else if(currentPane == statsPane){
            root.setCenter(becomeHostPane);
            currentPane = becomeHostPane;
        }
        else if(currentPane == becomeHostPane){
            loadCsvFile();
            filterListings();

            root.setCenter(welcomePane);
            currentPane = welcomePane;
        }
    }
    
    /**
     * Load the airbnb-london csv file.
     */
    private void loadCsvFile()
    {
        airbnbLoader=new AirbnbDataLoader();
        listings=new ArrayList<>();
        listings = airbnbLoader.load("airbnb-london.csv");
    }

    /**
     * Action taken when backward button is pressed.
     * @param evnet click on backward button.
     */
    @FXML
    private void backwardPressed(ActionEvent event)
    {
        if(currentPane == welcomePane){           
            root.setCenter(becomeHostPane); 
            currentPane = becomeHostPane;
        } 
        else if(currentPane == mapPane){           
            root.setCenter(welcomePane); 
            currentPane = welcomePane;
        }        
        else if(currentPane == statsPane){
            root.setCenter(mapPane);
            currentPane = mapPane;
        }       
        else if(currentPane == becomeHostPane){
            loadCsvFile();
            filterListings();

            root.setCenter(statsPane);
            currentPane = statsPane;
        }
    }

    /**
     * Main method to start the gui.
     * @param args
     */
    public static void main(String[] args)
    {
        Application.launch(args);    
    }

    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        URL url = getClass().getResource("main scene.fxml");
        Pane root = FXMLLoader.load(url);

        Scene scene = new Scene(root);
        stage.setTitle("Airbnb");
        stage.setScene(scene);

        stage.show();
    }
}