/**
 * 
 */
package com.quimic.levels;

import com.quimic.game.QuimiCrush;
import com.quimic.view.GameScreen;

/**
 * @author Oto
 *
 */
public class GameFactory {
	private QuimiCrush parent;
	private int        currentLevel;
    
//*************************************************************//   		
	
	/**
	 * 
	 * @param parent
	 * @param level
	 */
	public GameFactory(QuimiCrush parent, int currentLevel) {
		this.parent = parent;
		this.currentLevel = currentLevel;
	}

	/**
	 * 
	 * @return
	 */
	public GameScreen getGameLevelScreen() {
		GameScreen screenLevel = null;
		switch (currentLevel) {								
			case 0:
				screenLevel = new Tutorial(parent);				
				break;
			default:			
				screenLevel = new Level1(parent);
				break;
		}
		screenLevel.setCurrentLevel(currentLevel);
		return screenLevel;
	}
}