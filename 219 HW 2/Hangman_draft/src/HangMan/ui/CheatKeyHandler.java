package HangMan.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class CheatKeyHandler{
	private HangManUI ui;
	public CheatKeyHandler(HangManUI initUI){
		ui = initUI;
	}
	
	public void keyPressed(KeyEvent ke)
    {
        // GET THE KEY THAT WAS PRESSED IN ASSOCIATION WITH
        // THIS METHOD CALL.
    	//int keyCode = ke.getKeyCode();
       KeyCode keyCode = ke.getCode();

        // IS CONTROL-C PRESSED?
        if ((keyCode == KeyCode.CONTROL) 
                && ke.isControlDown())
        {
            // A CHEAT TO DISPLAY THE SECRET WORD
            ui.displaySecretWord();
        }
    }
}
