/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author Antony Kwok
 */
public class Timer extends Thread implements Runnable{
    private int count = 25;          //count time
    private Label timerLabel = new Label(count + "  ");
    
    
    public void TimerStart()
    { timerLabel.setMinSize(75,75);
    timerLabel.setStyle("-fx-background-color: green;");
        new Thread(this).start();}
    public Label getTimerLabel()
    {return timerLabel;}
    public int getCount(){return count;}
    
   public void run()
   {for(int x = 25; x >= 1; x--)
   { try{count--;       //decrement count
   Platform.runLater(() ->{
   timerLabel.setText(count + "  ");
   if(count==0)
   { timerLabel.setText(count +"  ");
     timerLabel.setStyle("-fx-background-color: red;");}});
   
    Thread.sleep(1000);
       } 
   catch(InterruptedException e){
       System.out.println("error with Timer");
   } }}
}
