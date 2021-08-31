import java.net.URL;
import javafx.scene.Node;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;




/**
 * Stats panel of the airbnb project
 *
 * @Xiaocheng Liang
 * @version 6th April 2021
 */
public class StatsPanel extends Application
{
    @FXML
    private Label label1;
    
    @FXML
    private Label label2;
    
    @FXML
    private Label label3;
    
    @FXML
    private Label label4;
    
    @FXML
    private BorderPane bp1;
    
    @FXML
    private BorderPane bp2;
    
    @FXML
    private BorderPane bp3;
    
    @FXML
    private BorderPane bp4;
        
    private StatsLoader StatsLoader;

    ArrayList<String> notDisplayedTitles = new ArrayList<String>();

    /**
     * initiate the stats panel
     */
    @FXML
    private void initialize() {
        StatsLoader = new StatsLoader();
        StatsLoader.load();
        initialTitles();
        initialData();        
    }
    
    /**
     * Update the statistic box when '<' or '>' clicked
     * @param event The MouseEvent that used to find the button that clicked
     */
    @FXML
    private void updatePage(MouseEvent event)
    {
        //when '<' or '>' button is clicked
        Button p = (Button)event.getSource();        
        Node parentPane = p.getParent();
        BorderPane currentPane = (BorderPane)parentPane;
        Node topNode = currentPane.getTop();
        if(topNode instanceof Label){
            Label topLabel = (Label)topNode;
            //add the current label into the list of labels that are not displayed in the window 
            notDisplayedTitles.add(topLabel.getText());
            String labelToPut = (String)notDisplayedTitles.get(0);
            topLabel.setText((String)notDisplayedTitles.get(0));
            notDisplayedTitles.remove(0);
            //update data
            updateData(labelToPut, currentPane);
            updateChart(labelToPut, currentPane);
        }
             
    }   
    
    /**
     * Start method of the stasPanel
     * @param stage The current stage
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        URL url = getClass().getResource("stat_page.fxml");
        Pane root = FXMLLoader.load(url);        
        Scene scene = new Scene(root);        
    }
    
    /**
     * Initiate the non-displayed titles of statics box
     */
    private void initialTitles()
    {
        notDisplayedTitles.add("The borough with the highest PM2.5 value");
        notDisplayedTitles.add("Average resident income per week");
        notDisplayedTitles.add("The borough with the highest crime rate");
        notDisplayedTitles.add("The borough with the highest Covid-19 infection rate");
    }
    
    /**
     * Initiate the data of the displayed statistic box
     */
    private void initialData()
    {
        label1.setText(StatsLoader.getAverageReviewNumber());
        label2.setText(StatsLoader.getNumberOfProperties());
        label3.setText(StatsLoader.getNumberOfEntireRoom());
        label4.setText(StatsLoader.boroughExpenseData());
        updateChart("Average number of reviews per property", bp1);
        updateChart("Total number of available properties", bp2);
        updateChart("The number of entire home and apartments", bp3);
        updateChart("The most expensive borough", bp4);
    }
    
    /**
     * Update data of the statistic box
     * @param boxTitle The title of the box that needs to be updated
     * @param boxToSet The box that the data needs to be set in
     */
    private void updateData(String boxTitle, BorderPane boxToSet)
    {
        Node textNode = checkLabelAvailable(boxToSet);
        Label content = (Label)textNode;
        String data = "";
        if(textNode != null)
        {
            if(boxTitle.equals("Average number of reviews per property"))       
            {                             
                data = StatsLoader.getAverageReviewNumber();
            }
            
            else if(boxTitle.equals("Total number of available properties"))       
            {                
                data = StatsLoader.getNumberOfProperties();
            }
            
            else if(boxTitle.equals("The number of entire home and apartments"))
            {
                data = StatsLoader.getNumberOfEntireRoom();
            } 
            
            else if(boxTitle.equals("The most expensive borough"))
            {
                data = StatsLoader.boroughExpenseData();
            } 
            
            else if(boxTitle.equals("The borough with the highest crime rate"))
            {
                data = StatsLoader.highestCrimeRateBorough();
            }
            
            else if(boxTitle.equals("Average resident income per week"))
            {
                data = StatsLoader.getAverageResidentIncome();
            }
            
            else if(boxTitle.equals("The borough with the highest PM2.5 value"))
            {
                data = StatsLoader.boroughWithHighestPM2_5();
            }
            
            else if(boxTitle.equals("The borough with the highest Covid-19 infection rate"))
            {
                data = StatsLoader.boroughWithHighestCovidInfectionRate();
            }
                
            else
            {
                data = "Data Not Found!";
            }
            
            content.setText(data);
        }
        
    }
    
    /**
     * Update the chart in the statistic box
     * @param boxTitle The title of the statistic box
     * @param boxToSet The statistic box where the chart is set
     */
    private void updateChart(String boxTitle, BorderPane boxToSet)
    {
        Node chartNode = checkChartAvailable(boxToSet);
        BarChart chart = (BarChart)chartNode;
        if(chart != null)
        {
            chart.getData().clear();
            chart.layout();
            ArrayList<Pair> data = new ArrayList<Pair>();
            boolean dataInInt = false;

            if(boxTitle.equals("Average number of reviews per property"))
            {
                data = StatsLoader.updateReviewsStats();
                dataInInt = true;
            }
            
            else if(boxTitle.equals("Total number of available properties"))       
            {
                data = StatsLoader.getPropertiesSorted(); 
                dataInInt = true;
            }
            
            else if(boxTitle.equals("The number of entire home and apartments"))
            {
                data = StatsLoader.NumberOfEntireRoomsChart(); 
                dataInInt = true;
            }
            
            else if(boxTitle.equals("The most expensive borough"))
            {
                data = StatsLoader.boroughExpenseChart();
                dataInInt = true;
            }
            
            else if(boxTitle.equals("The borough with the highest crime rate"))
            {
                data = StatsLoader.crimeRateByBorough();
                dataInInt = false;
            }
            
            else if(boxTitle.equals("Average resident income per week"))
            {
                data = StatsLoader.getResidentIncome();
                dataInInt = false;
            }
            
            else if(boxTitle.equals("The borough with the highest PM2.5 value"))
            
            {
                data = StatsLoader.getPM2_5Data();
                dataInInt = false;
            }
            
            else if(boxTitle.equals("The borough with the highest Covid-19 infection rate"))
            {
                data = StatsLoader.getCovidInfectionData();
                dataInInt = false;
            }
            
            getChartData(data, chart, dataInInt);
        }
    }
    
    // data can be in int or boolean type
    /**
     * Get the chart data of the required statistic box
     * @param data The ArrayList where data are from
     * @param chart The chart to put the data in
     * @param dataInInt The data type of the data(True if it is in integer, False if
     *  it is in float)
     */
    private void getChartData(ArrayList<Pair> data, BarChart chart, boolean dataInInt)
    {
        XYChart.Series dataSeries = new XYChart.Series();
        for(Pair dataPair:data)
        {
            String borough = (String)dataPair.getKey();
            if(dataInInt)
            {
                int intValue = (int)dataPair.getValue();
                dataSeries.getData().add(new XYChart.Data(borough,intValue));
            }
            else
            {
                Float floatValue = (Float)dataPair.getValue();
                dataSeries.getData().add(new XYChart.Data(borough,floatValue));
            }
            
        }
        chart.getData().add(dataSeries);
    }

    /**
     * Check if there is a title label in the required borderPane
     * @param box The borderPane that needs to be checked
     * @return The node of the title label
     */
    private Node checkLabelAvailable(BorderPane box)
    {
        Node textNode = null;
        int i = 1;
        while(textNode == null && i<=4)
        {               
            String labelToFind = "#label" + i;
            textNode = box.lookup(labelToFind);
            i++;
        }
        return textNode;
    }
    
    /**
     * Check if there is a chart in the required borderPane
     * @param box The borderPane that needs to be checked
     * @return The node of the chart
     */
    private Node checkChartAvailable(BorderPane box)
    {
        Node chartNode = null;
        int i = 1;
        while(chartNode == null && i<=4)
        {               
            String chartToFind = "#chart" + i;
            chartNode = box.lookup(chartToFind);
            i++;
        }
        return chartNode;
    }
    
    
        
}
