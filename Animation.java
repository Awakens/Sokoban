/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban.ui;

import java.io.File;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

/**
 *
 * @author Antony Kwok
 */
public class Animation extends Thread implements Runnable{
    private int count = 25;          //count time
    Image image; double pressedX; double pressedY; double x; double y;
    private Button anime = new Button();
    Animation(){}
    public Animation(Image image, double pressedX, double pressedY, double x, double y)
    {  image = image; pressedX = pressedX; pressedY = pressedY; x = x; y= y;}
    public void start()
    { File fileToOpen = new File("images/box.png");
            String fileName = fileToOpen.toURI().toString(); System.out.println(fileName);
    anime.setStyle("-fx-background-image: url(fileName);");

   
        new Thread(this).start();}
    public Button getAnime()
    {return anime;}
    public int getCount(){return count;}
    
   public void run()
   {for(int x = 25; x >= 1; x--)
   { try{count--;       //decrement count
   Platform.runLater(() ->{
   anime.setText(count + "  ");
   if(count==0)
   { anime.setText(count +"  ");
     anime.setStyle("-fx-background-color: red;");}});
   
    Thread.sleep(1000);
       } 
   catch(InterruptedException e){
       System.out.println("error with Timer");
   } }}
}
