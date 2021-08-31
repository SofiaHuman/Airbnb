import java.util.List;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.ArrayList;

/**
 * Implement a pop out window that shows the list of hosts 
 * and some general information
 * Each host has a box where there are its general information
 * @author xia yanjing, Lisha Zhu
 * @version 07/04/2021
*/
public class PropertiesDetails
{
    private SortingOptions currentSortingOption;
    private String currentSortOrder;
    private String currentNeighbourhood;
    
    private PropertiesFullDetails fullDetailsPanel;
    private ArrayList<AirbnbListing> currentListings;
    private List<SortingOptions> sortings;
    private AirbnbDataLoader airbnbLoader = new AirbnbDataLoader();
    
    private ScrollPane scrollPane;
    private Pane box;
    private MenuButton menuButton;
    private Stage stage;
        
    private Label hostName;
    private Label numberReviwes ;
    private Label priceProperty ;
    private Label maxNight ;
    
    /**    
     * Displays properties of the selected borough.
     * @param button the button associated with the borough.
     */
    public PropertiesDetails()
    {
        createSorting();
        
        // create new stage
        stage = new Stage();
        // make sure only one window pops up
        stage.initModality(Modality.APPLICATION_MODAL);
        
        Pane root = new VBox();
        // create properties' boxes
        root.setMinSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        root.setMaxSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        root.setPrefSize(500, 500);
        
        fullDetailsPanel = new PropertiesFullDetails();
        
        box = new VBox();
        
        scrollPane = new ScrollPane();
        scrollPane.setContent(box);
        
        createSortMenu();
        
        box.setId("box");
        box.setBackground(Background.EMPTY);
        root.getChildren().addAll(menuButton,scrollPane);
        scrollPane.setMinSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        scrollPane.setMaxSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);

        Scene scene = new Scene(root);
        VBox.setVgrow(scrollPane,Priority.ALWAYS);
        scene.getStylesheets().add("propertyDetail.css");
        stage.setScene(scene);
        
        stage.sizeToScene();
    }
    
    /**
     * Filter the listings by selected borough.
     */
    public void filterListing(Button button)
    {               
        currentListings=new ArrayList<>();
        MainScene.filteredListings.stream()
        .filter(l-> (l.getNeighbourhood().substring(0,4).toUpperCase().equals(button.getId())))
        .forEach(l -> currentListings.add(l));
    }
    
    /**
     * Create all sorting options.
     */
    private void createSorting()
    {
        sortings = new ArrayList<>();
        sortings.add(new PriceSorter("Price"));
        sortings.add(new ReviewSorter("Reviews"));
        sortings.add(new HostNameSorter("Host Name"));
    }
    
    /**
     * Check if there are properies that satisfy the price range in a chosen neighbourhood
     */
    private void checkPropertiesWithinPriceRange()
    {
        if(currentListings.size()!= 0){
            stage.setTitle(currentListings.get(0).getNeighbourhood());
            stage.show();  
        }
        else{
            showEmptyListingMessage();
        }
    }
    
    /**
     * Display the sorted listiings.
     */
    public void display(Button button)
    {
        currentSortingOption = null;
        currentSortOrder = null;
   
        filterListing(button);
        displayList();
        checkPropertiesWithinPriceRange();        
    }
    
    /**
     * Create the layout of sort menu.
     */
    private void createSortMenu()
    {
        menuButton = new MenuButton("Sort By");
        menuButton.setBorder(Border.EMPTY);
        menuButton.setBackground(Background.EMPTY);
        VBox.setMargin(menuButton, new Insets(20, 0, 0, 0));
        for(SortingOptions sorts : sortings) {
            Menu sortOption = new Menu(sorts.getName());
            MenuItem ascending  = new MenuItem("Ascending");
            MenuItem descending  = new MenuItem("Descending");
            sortOption.getItems().addAll(ascending,descending);
            ascending.setOnAction(e -> applySort(sorts,ascending.getText()));
            descending.setOnAction(e -> applySort(sorts,descending.getText()));
            menuButton.getItems().add(sortOption);
        }
    }
    
    /**
     * Display the list of properties within price range of a chosen neighbourhood
     */
    private void displayList()
    {
        box.getChildren().clear();
        currentListings.forEach(p -> createSpecificBox(p));
    }
    
    /**
     * Create the layout of single property of the list
     * @param currentProperty The property
     */
    private void createSpecificBox(AirbnbListing currentProperty)
    {
        Label hostName = new Label("Host Name: " + currentProperty.getHost_name());
        Label numberReviwes = new Label("Number of reviews: " + currentProperty.getNumberOfReviews());
        Label priceProperty = new Label("Property price: " + currentProperty.getPrice());
        Label maxNight = new Label("Maximum night: " + currentProperty.getMinimumNights());

        Pane specificBox = new VBox();
        specificBox.getChildren().addAll(hostName,numberReviwes,priceProperty,maxNight);
        specificBox.setId("specificBox");
        VBox.setVgrow(specificBox,Priority.ALWAYS);
        specificBox.setOnMouseClicked(e -> fullDetailsPanel.display(currentProperty));
        VBox.setMargin(specificBox, new Insets(20, 20, 20, 20));
        box.getChildren().add(specificBox);
    }
    
    /**
     * show information dialog when there are no prorpeties that satisfy the price range
     */
    private void showEmptyListingMessage()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("There are no properties in this neighbourhood having price range between " + MainScene.priceFrom+" and " + MainScene.priceTo);
        alert.showAndWait();
    }

    /**
     * Apply the selected sorting.
     * @param sorts the selected sort option.
     * @param sortOrder increasing or decreasing.
     */
    private void applySort(SortingOptions sortOption, String sortOrder)
    {
        if(sortOption == currentSortingOption && sortOrder.equals(currentSortOrder)){
            displaySortingAlert(sortOption.getName(),sortOrder);
        }
        else{
            sortOption.sort(currentListings, sortOrder);
            currentSortingOption = sortOption;
            currentSortOrder = sortOrder;
            displayList();
        }
    }
  
    /**
     * When the client chooses a sort option that is currently applied,
     * an information dialog will apply.
     *
     * @param sortOptionName the sort option chosen
     * @param sortOrder the chosen sort order ascending/descending
     */
    private void displaySortingAlert(String sortOptionName, String sortOrder)
    {
        Alert alert=new Alert(AlertType.INFORMATION);
        alert.setTitle("About sorting option");
        alert.setHeaderText(null);
        alert.setContentText("The list is already sorted by "+sortOptionName+" in "+sortOrder);;
        alert.showAndWait();
    }
}