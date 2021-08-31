import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.*;
import java.util.Iterator;

/**
 * Map panel of Airbnb, it can shows the density of properties
 * in each borough by generate different color (red) of the map
 * @author Jiajunhe Luo, Yanjing Xia, Lisha Zhu
 * @version 2021.04.07
 */

public class MapPanel extends Application
{    
    private PropertiesDetails propertiesDetails = new PropertiesDetails();    
    private ArrayList<Button> buttons= new ArrayList<>();
    // number of properties in each borough
    private HashMap<String,Integer> neighbourhoodMapToDensity;

    // buttons for borough
    @FXML
    private Button ENFI, BARN, HARI, WALT, HARR, BREN, CAMD, ISLI, HACK, REDB, HAVE,
    HILL, EALI, KENS, WEST, TOWE, NEWH, BARK, HOUN, HAMM, WAND, CITY, GREE, BEXL, RICH,
    MERT, LAMB, SOUT, LEWI, KING, SUTT, CROY, BROM;

    /**
     * Show the property details of each borough after click the button
     * @param event mouse click event
     */
    @FXML
    private void mapButtonPressed(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        propertiesDetails.display(button);
    }

    /**
     * Add all buttons to a button list
     */
    public void addAllButtons(){
        buttons.add(ENFI);  buttons.add(BARN);  buttons.add(HARI);  buttons.add(WALT);
        buttons.add(HARR);  buttons.add(BREN);  buttons.add(CAMD);  buttons.add(ISLI);
        buttons.add(HACK);  buttons.add(REDB);  buttons.add(HAVE);  buttons.add(HILL);
        buttons.add(EALI);  buttons.add(KENS);  buttons.add(WEST);  buttons.add(TOWE);
        buttons.add(NEWH);  buttons.add(BARK);  buttons.add(HOUN);  buttons.add(HAMM);  
        buttons.add(WAND);  buttons.add(CITY);  buttons.add(GREE);  buttons.add(BEXL);  
        buttons.add(RICH);  buttons.add(MERT);  buttons.add(LAMB);  buttons.add(SOUT);
        buttons.add(LEWI);  buttons.add(KING);  buttons.add(SUTT);  buttons.add(CROY);
        buttons.add(BROM);
    }

    /**
     * Count the number of properties in this borough.
     */
    public void generateMapDensity()
    {
        addAllButtons();
        neighbourhoodMapToDensity =new HashMap<>();

        for(int i = 0; i < MainScene.filteredListings.size(); i++){
            String key = MainScene.filteredListings.get(i).getNeighbourhood().substring(0,4).toUpperCase();
            if(neighbourhoodMapToDensity.containsKey(key)){
                int density = (int)neighbourhoodMapToDensity.get(key);
                neighbourhoodMapToDensity.put(key, density+1);
            }

            else{
                neighbourhoodMapToDensity.put(key,1);
            }
        }

        setButtonColor();
    } 

    /**
     * Set coler for each button.
     */
    private void setButtonColor()
    {
        for(int i = 0; i < buttons.size(); i++){
            String key=buttons.get(i).getId();
            if(neighbourhoodMapToDensity.get(key) == null){
                neighbourhoodMapToDensity.put(key,0);
            }
            buttons.get(i).setBackground(new Background(new BackgroundFill(setColor(neighbourhoodMapToDensity.get(key)), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Return the color for different density of properties
     * @param density of properties in each borough
     * @return different density of red to represent the density of properties
     */
    private Color setColor(int density)
    {
        int total = MainScene.filteredListings.size();
        double colorRange = (double)density/total;

        Color color=null;
        if(colorRange == 0){
            color=Color.WHITE;
        }
        else if (colorRange <= 0.02){
            color=Color.MISTYROSE;
        }
        else if (colorRange <= 0.04){
            color=Color.LIGHTCORAL;
        }
        else if (colorRange <= 0.08){
            color=Color.ORANGERED;
        }
        else if (colorRange <= 0.12){
            color=Color.RED;
        }
        else if (colorRange <= 0.16){
            color=Color.FIREBRICK;
        }
        else if (colorRange <= 0.2){
            color=Color.DARKRED;
        }
        else{
            color=Color.MAROON;
        }
        return color;
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
        URL url = getClass().getResource("map panel.fxml");
        Pane root = FXMLLoader.load(url);
        Scene scene = new Scene(root); 
    }   
}
