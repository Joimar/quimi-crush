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
	
	public boolean matched = false;
	public boolean combined = false;
	public int savedType = 0;
	
	public Loader assetsManager;
	public Sound combineSound;
	public Sound matchSound;
	
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
	public int heroState = GameScreen.HERO_IDLE; 
	public Tile[][] tiles;
	
	public Vector3 mouse_position = new Vector3(0,0,0);
	
	public int clickDelayCounter = 0;
    public int clickDelayCounterLength = 10;
	
	// ajeitar depois
	ArrayList<Texture> elementsT;
	//
	Table gameArea;
	private int sizeAreaW; // Largura da matriz do jogo
	private int sizeAreaH; // Altura da matriz do jogo
	
	boolean soundEffects = false; 
	
	public Logic(QuimiCrush parent, Tile[][] tiles, ArrayList<Texture> elementsT, OrthographicCamera cam, Table gameArea, int sizeAreaW, int sizeAreaH) {
		assetsManager = parent.assetsManager;
		// tells our asset manger that we want to load the sounds set in loadSounds method
		assetsManager.queueAddSounds();
		// tells the asset manager to load the sound and wait until finsihed loading.
		assetsManager.finishLoading();
		// loads the 2 sounds we use
		combineSound = assetsManager.MANAGER.get(assetsManager.COMBINE_SOUND, Sound.class);
		matchSound = assetsManager.MANAGER.get(assetsManager.MATCH_SOUND, Sound.class);				
		
		matchSound.setVolume(0, parent.savePreferences.getSoundVolume());
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
	}
	
	/**
	 * 
	 * @param previous
	 * @param current
	 */
	private void combineTile(Tile previous, Tile current) {
		matched = false;
		combined = false;
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
		} else if ((previous.type == GameScreen.C && current.type == GameScreen.O2) || (previous.type == GameScreen.O2 && current.type == GameScreen.C)) {
			matched = true;
			current.destroy = true;
		} else if ((previous.type == GameScreen.N2 && current.type == GameScreen.O) || (previous.type == GameScreen.O && current.type == GameScreen.N2)) {
			matched = true;
			current.destroy = true;
		} else if ((previous.type == GameScreen.Na2 && current.type == GameScreen.O) || (previous.type == GameScreen.O && current.type == GameScreen.Na2)) {
			matched = true;
			current.destroy = true;
		}
		
		if (matched) {
			if (soundEffects)
            	matchSound.play();
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
	public void shuffle(boolean fully) {				
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
		while(count-- > 0 && this.detectShuffle())
			this.shuffle(true);		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean detectShuffle() {
		boolean canShuffle = true;
	
		Tile tile; 
		for (int i = 0; i < (sizeAreaW * sizeAreaH); i++) {
			tile = ((Tile) gameArea.getCells().get(i).getActor());
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
	        	
	            mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            camera.unproject(mouse_position);
	            //mouse_position.x += 500;
	            //mouse_position.y = Gdx.graphics.getHeight() - mouse_position.y;
	            mouse_position.x += windowWidth / 2;
	            mouse_position.y += windowHeight / 2;
	            
	            //System.out.println("Largura(x) e Altura(y): "+Gdx.graphics.getWidth()+" "+Gdx.graphics.getHeight());
	            //System.out.println("Mouse Position x|y    : " + mouse_position.x + " " + mouse_position.y);

	            clickDelayCounter++;
	            	      
	            for (int i = 0; i < gameArea.getCells().size; i++) {								
	    			gameArea.getCells().get(i).getActor().addListener(new EventListener() {
						@Override
						public boolean handle(Event event) {		
							// Check for Tile Swap
				            //if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {			            	
				            if (Gdx.input.isTouched()) {
				                if (clickDelayCounter > clickDelayCounterLength) {
				                	Tile previous = (Tile) gameArea.getCells().get(activeXY).getActor();
					            	Tile current = (Tile) event.getListenerActor();
					            	
					            	System.out.println(activeXY+" : "+gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true));
					            	if (activeXY == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true))
					            		return false;
				                    // Check tile to left side
				                    if (tileIsActive) {				                    	
				                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
				                    	if ( (activeXY % sizeAreaW) != 0 &&  ((activeXY - 1) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) ) {
				                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
				                            	// Verica se está a esquerda
				                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
				                                    // De-activate
				                                    tileIsActive = false;
				                                    previous.activated = false;

				                                    combineTile(previous, current);				                                    
				                                    
				                                    clickDelayCounter = 0;				                                    

				                               // }
				                            }
				                        }
				                    }
				                    // Check tile to right side
				                    if (tileIsActive) {				                    	
				                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
				                    	if ( ((activeXY+1) % sizeAreaW) != 0 && ((activeXY + 1) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) ) {
				                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
				                            	// Verica se está a esquerda
				                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
				                                    // De-activate
				                                    tileIsActive = false;
				                                    previous.activated = false;

				                                    combineTile(previous, current);	
				                                    
				                                    clickDelayCounter = 0;
				                               // }
				                            }
				                        }
				                    }
				                    // Check tile up
				                    if (tileIsActive) {				                    	
				                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
				                    	if ((activeXY-sizeAreaW) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) {
				                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
				                            	// Verica se está a esquerda
				                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
				                                    // De-activate
				                                    tileIsActive = false;
				                                    previous.activated = false;

				                                    combineTile(previous, current);	
				                                    
				                                    clickDelayCounter = 0;
				                               // }
				                            }
				                        }
				                    }
				                    // Check tile down
				                    if (tileIsActive) {				                    	
				                        //if ((activeXY%sizeAreaW) - 1 >= 0) {
				                    	if ((activeXY+sizeAreaW) == gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true)) {
				                            if ((((Tile) gameArea.getCells().get(activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {	                            
				                            	// Verica se está a esquerda
				                            	//if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
				                                    // De-activate
				                                    tileIsActive = false;
				                                    previous.activated = false;
				                                    
				                                    combineTile(previous, current);					                                    
				                                    
				                                    clickDelayCounter = 0;
				                               // }
				                            }
				                        }
				                    }

				                    // Check for Matches
				                    gameState = MATCH_STATE;

				                }
				            }
							
				            //begin
				            //if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				            if (Gdx.input.isTouched()) {
				                if (clickDelayCounter > clickDelayCounterLength) {	                	
				                  // for (int i = 0; i < gameArea.getCells().size; i++) { // for (int i = 0; i < tiles.length; i++) {
				                     //   for (int j = 0; j < tiles.length; j++) {	                        		                        	
				                	//   if ((mouse_position.x > ((Tile) gameArea.getCells().get(i).getActor()).getX()) && (mouse_position.x < ((Tile) gameArea.getCells().get(i).getActor()).getX() + ((Tile) gameArea.getCells().get(i).getActor()).x) 
				                //	   && (mouse_position.y > ((Tile) gameArea.getCells().get(i).getActor()).getY()) && (mouse_position.y < ((Tile) gameArea.getCells().get(i).getActor()).getY() + ((Tile) gameArea.getCells().get(i).getActor()).y)) {//if ((mouse_position.x > tiles[i][j].x) && (mouse_position.x < tiles[i][j].x + 64) && (mouse_position.y > tiles[i][j].y) && (mouse_position.y < tiles[i][j].y + 64)) {	                            	
				                            
				                            	//if (((Tile) gameArea.getCells().get(i).getActor()).type != 100) {
				                				if (((Tile)event.getListenerActor()).type != 100) {

				                                    // De-activate previous tile
				                            		// tiles[activeX][activeY].activated = false;
				                            		((Tile) gameArea.getCells().get(activeXY).getActor()).activated = false;	                            		

				                                    // Active new tile
				                                    //tiles[i][j].activated = true;
				                            		//((Tile) gameArea.getCells().get(i).getActor()).activated = true;
				                            		((Tile)event.getListenerActor()).activated = true;
				                            		tileIsActive = true;
				                                    //activeX = i;
				                                    //activeY = j;
				                            		activeXY =  gameArea.getCells().indexOf(gameArea.getCell(event.getListenerActor()), true);				                            				

				                                    clickDelayCounter = 0;

				                                    //System.out.println("Activated: " + i + " " + j);
				                                    System.out.println("Activated: " + activeXY + " ");

				                                }

				                            }

				                       // }
				                    }
				              //  }

				         //   }
				            //end
							return false;
						}				    				
	    			}); 			
	    		}

	        }

	        // Detect Matches
	        if (gameState == MATCH_STATE) {
	            detectMatches();
	        }

	        // Tiles Falling
	        if (gameState == FALL_STATE) {
	          //  tilesFalling();
	        	tilesFall();
	        	if (matched) { /** trava de alguma forma **/
	        		gameState = IDLE_STATE;		        	
		    		heroState = GameScreen.HERO_ATTACK;
	        	} else { //combined	        		
	        		gameState = PLAYER_STATE;
	        		heroState = GameScreen.HERO_IDLE;
	        	}
	        	matched = false;
	        	combined = false;	    		
	        }
			
	        System.out.println("GAME STATE: "+gameState+"\n");
	    }
	
	/**
	 * 
	 */
	public void detectMatches() {

	        if (matched || combined) {
	            gameState = FALL_STATE;
	        }
	        else {
	            // Player Turn
	            gameState = PLAYER_STATE;
	        }
	}
	
	public void tilesFall() {				
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

	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
	public void tilesFalling() {

        boolean repeat = false;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].type != 100) {
                    if ((j - 1) >= 0) {
                        if (tiles[i][j - 1].type == 100) {

                            // Swap Tiles
                            savedType = tiles[i][j].type;
                            tiles[i][j].type = tiles[i][j - 1].type;
                            System.out.println("TIPO DO NULLPOINTER: "+tiles[i][j].type );
                            //tiles[i][j].sprite = new Sprite(this.elementsT.get(tiles[i][j].type));
                            
                            tiles[i][j - 1].type = savedType;
                            tiles[i][j - 1].sprite = new Sprite(this.elementsT.get(savedType));

                            repeat = true;

                        }
                    }
                }
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].type == 100) {
                    if (j == tiles.length-1) {
                        int type =  MathUtils.random(0,4);
                    	tiles[i][j].type = type;
                    	tiles[i][j].sprite = new Sprite(elementsT.get(type));
                    	
                        //System.out.println("Added a Tile in Row " + i);

                        repeat = true;
                    }
                }
            }
        }

        if (repeat) {
            tilesFalling();
        }


        // Check for Matches
        gameState = 1;

        System.out.println("Completed Tiles Falling!");

    }
	
	public void detectMatch3Hori() {
		        for (int i = 0; i < tiles.length; i++) {
		            for (int j = 0; j < tiles.length; j++) {
		                if (tiles[i][j].type != 100) {
		                    if ((i + 2) < tiles.length) {
		                        if ((tiles[i][j].type == tiles[i + 1][j].type) && (tiles[i][j].type == tiles[i + 2][j].type)) {
		                            for (int k = 0; k < 3; k++) {
		                                tiles[i + k][j].type = 100;
		                            }
		                            System.out.println("Match 3 Horizontal at " + i + " " + j);
	                        matched = true;
	                        
	                    }
	                }
	            }
	        }
	    }
	}

	public void detectMatch3Vert() {
	    for (int i = 0; i < tiles.length; i++) {
	        for (int j = 0; j < tiles.length; j++) {
	            if (tiles[i][j].type != 100) {
	                if ((j + 2) < tiles.length) {
	                    if ((tiles[i][j].type == tiles[i][j + 1].type) && (tiles[i][j].type == tiles[i][j + 2].type)) {
	                        for (int k = 0; k < 3; k++) {
	                            tiles[i][j + k].type = 100;
	                        }
	                        System.out.println("Match 3 Vertical at " + i + " " + j);
	                        matched = true;
	                        match.play();
	                    }
	                }
	            }
	        }
	    }
	}
	
	public void detectMatch4Hori() {
	    for (int i = 0; i < tiles.length; i++) {
	        for (int j = 0; j < tiles.length; j++) {
	            if (tiles[i][j].type != 100) {
	                if ((i + 3) < tiles.length) {
	                    if ((tiles[i][j].type == tiles[i + 1][j].type) && (tiles[i][j].type == tiles[i + 2][j].type) && (tiles[i][j].type == tiles[i + 3][j].type)) {
	                        for (int k = 0; k < 4; k++) {
	                            tiles[i + k][j].type = 100;
	                        }
	                        System.out.println("Match 4 Horizontal at " + i + " " + j);
	                        matched = true;
	                        match.play();
	                    }
	                }
	            }
	        }
	    }
	}
	
	public void detectMatch4Vert() {
	    for (int i = 0; i < tiles.length; i++) {
	        for (int j = 0; j < tiles.length; j++) {
	            if (tiles[i][j].type != 100) {
	                if ((j + 3) < tiles.length) {
	                    if ((tiles[i][j].type == tiles[i][j + 1].type) && (tiles[i][j].type == tiles[i][j + 2].type) && (tiles[i][j].type == tiles[i][j + 3].type)) {
	                        for (int k = 0; k < 4; k++) {
	                            tiles[i][j + k].type = 100;
	                        }
	                        System.out.println("Match 4 Vertical at " + i + " " + j);
	                        matched = true;
	                        match.play();
	                    }
	                }
	            }
	        }
	    }
	}
	
	public void detectMatch5Hori() {
	    for (int i = 0; i < tiles.length; i++) {
	        for (int j = 0; j < tiles.length; j++) {
	            if (tiles[i][j].type != 100) {
	                if ((i + 4) < tiles.length) {
	                    if ((tiles[i][j].type == tiles[i + 1][j].type) && (tiles[i][j].type == tiles[i + 2][j].type) && (tiles[i][j].type == tiles[i + 3][j].type) && (tiles[i][j].type == tiles[i + 4][j].type)) {
	                        for (int k = 0; k < 5; k++) {
	                            tiles[i + k][j].type = 100;
	                        }
	                        System.out.println("Match 5 Horizontal at " + i + " " + j);
	                        matched = true;
	                        match.play();
	                    }
	                }
	            }
	        }
	    }
	}
	
	public void detectMatch5Vert() {
	    for (int i = 0; i < tiles.length; i++) {
	        for (int j = 0; j < tiles.length; j++) {
	            if (tiles[i][j].type != 100) {
	                if ((j + 4) < tiles.length) {
	                    if ((tiles[i][j].type == tiles[i][j + 1].type) && (tiles[i][j].type == tiles[i][j + 2].type) && (tiles[i][j].type == tiles[i][j + 3].type) && (tiles[i][j].type == tiles[i][j + 4].type)) {
	                        for (int k = 0; k < 5; k++) {
	                            tiles[i][j + k].type = 100;
	                        }
	                        System.out.println("Match 5 Vertical at " + i + " " + j);
	                        matched = true;
	                        match.play();
	                    }
	                }
	            }
	        }
	    }
	}
*/ 
}