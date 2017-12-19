package com.quimic.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.quimic.game.QuimiCrush;
import com.quimic.loader.Loader;
import com.quimic.tile.Tile;
import com.quimic.view.GameScreen;

public class Logic {

	public final int IDLE_STATE   = -1;
	public final int PLAYER_STATE = 0;
	public final int MATCH_STATE  = 1;	
	public final int FALL_STATE   = 2;
	
	private int tipPosition; // Posição no vetor do bloco quimico a ser sinalizado ao jogador 
	
	public boolean matched = false;
	public boolean combined = false;
	public int savedType = 0;
	
	public Loader assetsManager;
	public Sound combineSound;
	public Sound waterSound;
	public Sound smokeSound;
	public Sound glassSound;
	public Sound laughSound;
	//public Sound matchSound;
	
	//
	public int windowWidth = Gdx.graphics.getWidth();
    public int windowHeight = Gdx.graphics.getHeight();
    public OrthographicCamera camera;
    public StretchViewport viewport;
	
	// Active Tile
    public boolean tileIsActive = false;
    public int activeX = 0;
    public int activeY = 0;
    public int activeXY = 0;   
    
	public int gameState = PLAYER_STATE;
	public int logicBattleState = GameScreen.HERO_IDLE; 
	public Tile[][] tiles;
	
	public Vector3 mouse_position = new Vector3(0,0,0);
	
	/*public int clickDelayCounter = 0;
    public int clickDelayCounterLength = 5;*/
	
	// ajeitar depois
	ArrayList<Texture> elementsT;
	//
	Table gameArea;
	private int sizeAreaW; // Largura da matriz do jogo
	private int sizeAreaH; // Altura da matriz do jogo
	
	boolean soundEffects = false; 
	
	/**
	 * 
	 * @param parent
	 * @param tiles
	 * @param elementsT
	 * @param cam
	 * @param gameArea
	 * @param sizeAreaW
	 * @param sizeAreaH
	 */
	public Logic(QuimiCrush parent, Tile[][] tiles, ArrayList<Texture> elementsT, OrthographicCamera cam, Table gameArea, int sizeAreaW, int sizeAreaH) {
		assetsManager = parent.assetsManager;
		// tells our asset manger that we want to load the sounds set in loadSounds method
		assetsManager.queueAddSounds();
		// tells the asset manager to load the sound and wait until finsihed loading.
		assetsManager.finishLoading();
		// loads the 2 sounds we use
		combineSound = assetsManager.MANAGER.get(assetsManager.COMBINE_SOUND, Sound.class);
		//matchSound = assetsManager.MANAGER.get(assetsManager.MATCH_SOUND, Sound.class);	
		waterSound = assetsManager.MANAGER.get(assetsManager.WATER_ATTACK_SOUND, Sound.class);
		smokeSound = assetsManager.MANAGER.get(assetsManager.CARBON_ATTACK_SOUND, Sound.class);
		glassSound = assetsManager.MANAGER.get(assetsManager.GLASS_ATTACK_SOUND, Sound.class);
		laughSound = assetsManager.MANAGER.get(assetsManager.LAUGH_ATTACK_SOUND, Sound.class);
	
		//matchSound.setVolume(0, parent.savePreferences.getSoundVolume());
		waterSound.setVolume(0, parent.savePreferences.getSoundVolume());
		smokeSound.setVolume(0, parent.savePreferences.getSoundVolume());
		glassSound.setVolume(0, parent.savePreferences.getSoundVolume());
		laughSound.setVolume(0, parent.savePreferences.getSoundVolume());
		combineSound.setVolume(0, parent.savePreferences.getSoundVolume());
		soundEffects = parent.savePreferences.isSoundEffectsEnabled();
		
		this.tiles = tiles;
		this.gameArea = gameArea;
		this.sizeAreaW = sizeAreaW;
		this.sizeAreaH = sizeAreaH;		
		//		
		this.elementsT = elementsT;	
		//
		
		camera = cam;
        //viewport = new StretchViewport(windowWidth, windowHeight, camera);
        //viewport.apply();
		
		//this.tilesListener();
	}

	/**
	 * 
	 * @return
	 */
	public int findTip() {
		return tipPosition;
	}
	
	/**
	 * 
	 * @param previous
	 * @param current
	 */
	public void combineTile(Tile previous, Tile current) {
		matched = false;
		combined = false;
		
		boolean h2oSound=false;
		boolean co2Sound=false;
		boolean n2oSound=false;		
		boolean na2oSound=false;			
		
		if (previous.type == GameScreen.H && current.type == GameScreen.H) {
			current.type = GameScreen.H2;
			current.sprite = new Sprite(elementsT.get(GameScreen.H2));		
			combined = true;
		} else if (previous.type == GameScreen.O && current.type == GameScreen.O) {
			current.type = GameScreen.O2;
			current.sprite = new Sprite(elementsT.get(GameScreen.O2));
			combined = true;
		} else if (previous.type == GameScreen.N && current.type == GameScreen.N) {
			current.type = GameScreen.N2;
			current.sprite = new Sprite(elementsT.get(GameScreen.N2));
			combined = true;
		} else if (previous.type == GameScreen.Na && current.type == GameScreen.Na) {
			current.type = GameScreen.Na2;
			current.sprite = new Sprite(elementsT.get(GameScreen.Na2));
			combined = true;
		} else if ((previous.type == GameScreen.H2 && current.type == GameScreen.O) || (previous.type == GameScreen.O && current.type == GameScreen.H2)) {
			matched = true;			
			current.destroy = true;
			h2oSound = true;
		} else if ((previous.type == GameScreen.C && current.type == GameScreen.O2) || (previous.type == GameScreen.O2 && current.type == GameScreen.C)) {
			matched = true;
			current.destroy = true;
			co2Sound = true;
		} else if ((previous.type == GameScreen.N2 && current.type == GameScreen.O) || (previous.type == GameScreen.O && current.type == GameScreen.N2)) {
			matched = true;
			current.destroy = true;
			n2oSound = true;
		} else if ((previous.type == GameScreen.Na2 && current.type == GameScreen.O) || (previous.type == GameScreen.O && current.type == GameScreen.Na2)) {
			matched = true;
			current.destroy = true;
			na2oSound = true;
		}
		
		if (matched) {
			if (soundEffects) {
            	if (h2oSound) {
            		waterSound.play();
            	} else if (co2Sound) {
            		smokeSound.play();
            	} else if (n2oSound) {
            		laughSound.play();
            	} else if (na2oSound) {
            		glassSound.play();
            	}
			}
		} else {
			if (combined)
            	combineSound.play();		
		}
		
		previous.destroy = matched || combined;		
	}

	/**
	 * 
	 * @param fully
	 */
	private void shuffle(boolean fully) {				
		for (int i = 0; i < (sizeAreaW * sizeAreaH); i++) {			
			if (!fully) {
				if ( ((Tile) gameArea.getCells().get(i).getActor()).type <= 4) {
					int type = MathUtils.random(0,4);
					((Tile) gameArea.getCells().get(i).getActor()).sprite = new Sprite(elementsT.get(type));
					((Tile) gameArea.getCells().get(i).getActor()).type = type;
				}
			} else {
				int type = MathUtils.random(0,4);
				((Tile) gameArea.getCells().get(i).getActor()).sprite = new Sprite(elementsT.get(type));
				((Tile) gameArea.getCells().get(i).getActor()).type = type;
			}		    
		}
		
		int count = 5;
		while(count-- > 0 && this.detectShuffle()) // Verifica mais cinco vezes se o jogo não está sem combinações
			this.shuffle(true); // Remove até os componentes quimicos combinados
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean detectShuffle() {
		boolean canShuffle = true; // Verificador de esparalhamento dos itens
	
		Tile tile; 
		for (int i = 0; i < (sizeAreaW * sizeAreaH); i++) {
			tile = ((Tile) gameArea.getCells().get(i).getActor()); // Simplificar o acesso ao componente quimico
			switch (tile.type) {
				case GameScreen.H:							 	
				case GameScreen.O:		
				case GameScreen.N:							 
				case GameScreen.Na:							 
					if ( (i % sizeAreaW) != 0 ) { // Esquerda
						if (((Tile) gameArea.getCells().get(i-1).getActor()).type == tile.type)
							canShuffle = false;
					} if ( ((i+1) % sizeAreaW) != 0 ) { // Direita
						if (((Tile) gameArea.getCells().get(i+1).getActor()).type == tile.type)
							canShuffle = false;		
					} if ( (i - sizeAreaW) >= 0 ) { // Cima
						if (((Tile) gameArea.getCells().get(i-sizeAreaW).getActor()).type == tile.type)
							canShuffle = false;
					} if ( (i + sizeAreaW) < (sizeAreaW * sizeAreaH) ) { // Baixo
						if (((Tile) gameArea.getCells().get(i+sizeAreaW).getActor()).type == tile.type)
							canShuffle = false;
					}					
					break;
				case GameScreen.H2:					
				case GameScreen.N2:							 
				case GameScreen.Na2:							 
					if ( (i % sizeAreaW) != 0 ) { // Esquerda
						if (((Tile) gameArea.getCells().get(i-1).getActor()).type == GameScreen.O)
							canShuffle = false;
					} if ( ((i+1) % sizeAreaW) != 0 ) { // Direita
						if (((Tile) gameArea.getCells().get(i+1).getActor()).type == GameScreen.O)
							canShuffle = false;		
					} if ( (i - sizeAreaW) >= 0 ) { // Cima
						if (((Tile) gameArea.getCells().get(i-sizeAreaW).getActor()).type == GameScreen.O)
							canShuffle = false;
					} if ( (i + sizeAreaW) < (sizeAreaW * sizeAreaH) ) { // Baixo
						if (((Tile) gameArea.getCells().get(i+sizeAreaW).getActor()).type == GameScreen.O)
							canShuffle = false;
					}
					break;
				case GameScreen.O2:	
					if ( (i % sizeAreaW) != 0 ) { // Esquerda
						if (((Tile) gameArea.getCells().get(i-1).getActor()).type == GameScreen.C)
							canShuffle = false;
					} if ( ((i+1) % sizeAreaW) != 0 ) { // Direita
						if (((Tile) gameArea.getCells().get(i+1).getActor()).type == GameScreen.C)
							canShuffle = false;		
					} if ( (i - sizeAreaW) >= 0 ) { // Cima
						if (((Tile) gameArea.getCells().get(i-sizeAreaW).getActor()).type == GameScreen.C)
							canShuffle = false;
					} if ( (i + sizeAreaW) < (sizeAreaW * sizeAreaH) ) { // Baixo
						if (((Tile) gameArea.getCells().get(i+sizeAreaW).getActor()).type == GameScreen.C)
							canShuffle = false;
					}
					break;
				case GameScreen.C:			
					if ( (i % sizeAreaW) != 0 ) { // Esquerda
						if (((Tile) gameArea.getCells().get(i-1).getActor()).type == GameScreen.O2)
							canShuffle = false;
					} if ( ((i+1) % sizeAreaW) != 0 ) { // Direita
						if (((Tile) gameArea.getCells().get(i+1).getActor()).type == GameScreen.O2)
							canShuffle = false;		
					} if ( (i - sizeAreaW) >= 0 ) { // Cima
						if (((Tile) gameArea.getCells().get(i-sizeAreaW).getActor()).type == GameScreen.O2)
							canShuffle = false;
					} if ( (i + sizeAreaW) < (sizeAreaW * sizeAreaH) ) { // Baixo
						if (((Tile) gameArea.getCells().get(i+sizeAreaW).getActor()).type == GameScreen.O2)
							canShuffle = false;
					}
					break;
			}
			
			if (!canShuffle) {
				tipPosition = i;
				break;
			}
		}				
		
		return canShuffle;
	}
	
	/**
	 * 
	 */
	public void update() {					
	        // Players Turn
	        if (gameState == PLAYER_STATE) {	        	
	        	if (this.detectShuffle()) {
	        		System.out.println("Bagunça isso!");
	        		this.shuffle(false);	
	        	}
	        	
	            /*mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            camera.unproject(mouse_position);
	            //mouse_position.x += 500;
	            //mouse_position.y = Gdx.graphics.getHeight() - mouse_position.y;
	            mouse_position.x += windowWidth / 2;
	            mouse_position.y += windowHeight / 2;*/

	            //clickDelayCounter++;
	          

	        }	        	       

	        // Detect Matches
	        if (gameState == MATCH_STATE) {
	            detectMatches();
	        }
	        
	        // Tiles Falling
	        if (gameState == FALL_STATE) {
	        	tilesFall();
	        	if (matched) // match-(2+1)	
	        		logicBattleState = GameScreen.GAME_IDLE;		    			        
	        	else //combined (2)	        		
	        		logicBattleState = GameScreen.ENEMY_ATTACK;	        	
	        	gameState = IDLE_STATE;      	
	        	matched = false;
	        	combined = false;	    		
	        }
				       
	    }
	
	/**
	 * 
	 */
	private void detectMatches() {

	        if (matched || combined) {
	            gameState = FALL_STATE;
	        }
	        else {
	            // Player Turn
	            gameState = PLAYER_STATE;
	        }
	}
	
	/**
	 * 
	 */
	private void tilesFall() {				
		for (int i = 0; i < (sizeAreaW*sizeAreaH); i++) {
			if (((Tile) gameArea.getCells().get(i).getActor()).destroy) {
				int j = i;
				while((i-sizeAreaW) >= 0) {														
					j = i - sizeAreaW;
					
					((Tile) gameArea.getCells().get(i).getActor()).destroy = false;							
					((Tile) gameArea.getCells().get(j).getActor()).destroy = true;
					
					((Tile) gameArea.getCells().get(i).getActor()).type = ((Tile) gameArea.getCells().get(j).getActor()).type;
					((Tile) gameArea.getCells().get(i).getActor()).sprite = ((Tile) gameArea.getCells().get(j).getActor()).sprite;;
					
					i = j;
				}				
				((Tile) gameArea.getCells().get(i).getActor()).destroy = false;
				int type = MathUtils.random(0,4);
				((Tile) gameArea.getCells().get(i).getActor()).type = type;							
				((Tile) gameArea.getCells().get(i).getActor()).sprite = new Sprite(elementsT.get(type));
			}				
		}			
	}

	
}

//evento do click no tile 
/* 	      
for (int i = 0; i < gameArea.getCells().size; i++) {								
	gameArea.getCells().get(i).getActor().addListener(new EventListener() {
		@Override						
		public boolean handle(Event event) {.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {		
			// Simplificando o acesso aos itens
			Tile previousTile = (Tile) gameArea.getCells().get(activeXY).getActor();
        	Tile currentTile = (Tile) event.getListenerActor();

        	// Check for Tile Swap
            //if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
            //if (Gdx.input.justTouched()) {
        	if(true) {if(secondTap) {
                //if (clickDelayCounter > clickDelayCounterLength) {        						            	
	            						            
	            	// Verifica se o segundo item clicado foi do lado esquerdo
                    if (tileIsActive) { 				                    	
                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
                    	if ( (activeXY % sizeAreaW) != 0 &&  ((activeXY - 1) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) ) {
                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
                            	// Verica se está a esquerda
                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
                                    // De-activate
                                    tileIsActive = false;
                                    previousTile.activated = false;

                                    combineTile(previousTile, currentTile);				                                    
                                    
                                    clickDelayCounter = 0;				                                    

                               // }
                            }
                        }
                    }
                    // Verifica se o segundo item clicado foi do lado direito
                    if (tileIsActive) {				                    	
                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
                    	if ( ((activeXY+1) % sizeAreaW) != 0 && ((activeXY + 1) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) ) {
                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
                            	// Verica se está a esquerda
                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
                                    // De-activate
                                    tileIsActive = false;
                                    previousTile.activated = false;

                                    combineTile(previousTile, currentTile);	
                                    
                                    clickDelayCounter = 0;
                               // }
                            }
                        }
                    }
                    // Verifica se o segundo item clicado foi em cima
                    if (tileIsActive) {				                    	
                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
                    	if ((activeXY-sizeAreaW) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) {
                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
                            	// Verica se está a esquerda
                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
                                    // De-activate
                                    tileIsActive = false;
                                    previousTile.activated = false;

                                    combineTile(previousTile, currentTile);	
                                    
                                    clickDelayCounter = 0;
                               // }
                            }
                        }
                    }
                 // Verifica se o segundo item clicado foi em baixo
                    if (tileIsActive) {				                    	
                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
                    	if ((activeXY+sizeAreaW) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) {
                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
                            	// Verica se está a esquerda
                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
                                    // De-activate
                                    tileIsActive = false;
                                    previousTile.activated = false;
                                    
                                    combineTile(previousTile, currentTile);					                                    
                                    
                                    clickDelayCounter = 0;
                               // }
                            }
                        }
                    }
                    
                    // Check for Matches
                    gameState = MATCH_STATE;
                    secondTap = false;
                }				            	
            }
			
            //begin
            //if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            //if (Gdx.input.justTouched()) {
        	if(true) {if(!secondTap) {
                //if (clickDelayCounter > clickDelayCounterLength) {	

                  // for (int i = 0; i < gameArea.getCells().size; i++) { // for (int i = 0; i < tiles.length; i++) {
                     //   for (int j = 0; j < tiles.length; j++) {	                        		                        	
                	//   if ((mouse_position.x > ((Tile) gameArea.getCells().get(i).getActor()).getX()) && (mouse_position.x < ((Tile) gameArea.getCells().get(i).getActor()).getX() + ((Tile) gameArea.getCells().get(i).getActor()).x) 
                //	   && (mouse_position.y > ((Tile) gameArea.getCells().get(i).getActor()).getY()) && (mouse_position.y < ((Tile) gameArea.getCells().get(i).getActor()).getY() + ((Tile) gameArea.getCells().get(i).getActor()).y)) {//if ((mouse_position.x > tiles[i][j].x) && (mouse_position.x < tiles[i][j].x + 64) && (mouse_position.y > tiles[i][j].y) && (mouse_position.y < tiles[i][j].y + 64)) {	                            	
                            
                            	//if (((Tile) gameArea.getCells().get(i).getActor()).type != 100) {
                				//if (currentTile.type != 100) {
                				if (!currentTile.destroy) {	

                                    // De-activate previous tile
                            		// tiles[activeX][activeY].activated = false;
                            		previousTile.activated = false;	                            		

                                    // Active new tile
                                    //tiles[i][j].activated = true;
                            		//((Tile) gameArea.getCells().get(i).getActor()).activated = true;
                            		currentTile.activated = true;
                            		tileIsActive = true;
                            		secondTap = true;
                                    //activeX = i;
                                    //activeY = j;
                            		activeXY =  gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true);				                            				

                                    clickDelayCounter = 0;				                            		

                                    System.out.println("Activated: " + activeXY + " ");

                                } 
                            }

                       // }
                    }
              //  }

         //   }
            //end
			//return false;
		}				    				
	}); 			
}*/