import lang.stride.*;
import java.io.Writer;
import java.io.*;

/**
 * Upload client's data in a csv file
 * @author xiayanjing 
 * @version 04/04/2021
 */
public class UserDataExport
{
    private  BufferedWriter userData;

    /**
     * Connect to the file in which we will upload the data
     */
     public void connectToUserFile(String fileName)
    {
        try {
            userData =  new  BufferedWriter( new  FileWriter(fileName, true));
        }
        catch (java.io.IOException ioe) {
            System.out.println("error during file uploading");
            ioe.printStackTrace();
        }
    }

    /**
     * Upload the given data
     */
     public void uploadData(String text)
    {
        try {
            userData.write(text);
            userData.write(",");
            
        }
        catch (java.io.IOException ioe) {
            System.out.println("failed to write data");
            ioe.printStackTrace();
        }
    }

    /**
     * Set a new line such that further data will be uploaded in the next line
     */
     public void setNewLine()
    {
        try {
            userData.newLine();
            userData.flush();
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
