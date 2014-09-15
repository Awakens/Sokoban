package HangMan.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import application.Main.HangManPropertyType;
import properties_manager.PropertiesManager;
import HangMan.ui.HangManUI;

public class HangManFileLoader {
	/**
    * This method loads the complete contents of the textFile argument into
    * a String and returns it.
    * 
    * @param textFile The name of the text file to load. Note that the path
    * will be added by this method.
    * 
    * @return All the contents of the text file in a single String.
    * 
    * @throws IOException This exception is thrown when textFile is an invalid
    * file or there is some problem in accessing the file.
    */
   public static String loadTextFile(  String textFile) throws IOException
   {
       // ADD THE PATH TO THE FILE
       PropertiesManager props = PropertiesManager.getPropertiesManager();
       textFile = props.getProperty(HangManPropertyType.DATA_PATH) + textFile;
       
       // WE'LL ADD ALL THE CONTENTS OF THE TEXT FILE TO THIS STRING
       String textToReturn = "";
      
       // OPEN A STREAM TO READ THE TEXT FILE
       FileReader fr = new FileReader(textFile);
       BufferedReader reader = new BufferedReader(fr);
           
       // READ THE FILE, ONE LINE OF TEXT AT A TIME
       String inputLine = reader.readLine();
       while (inputLine != null)
       {
           // APPEND EACH LINE TO THE STRING
           textToReturn += inputLine + "\n";
           
           // READ THE NEXT LINE
           inputLine = reader.readLine();        
       }
       
       // RETURN THE TEXT
       return textToReturn;
   }    
}
