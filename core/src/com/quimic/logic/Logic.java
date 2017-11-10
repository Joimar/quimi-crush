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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.quimic.game.QuimiCrush;
import com.quimic.loader.Loader;
import com.quimic.tile.Tile;

public class Logic {

	public boolean matched = false;
	public int savedType = 0;
	
	public Loader assetsManager;
	public Sound combine;
	public Sound match;
	
	//
	public int windowWidth = Gdx.graphics.getWidth();
    public int windowHeight = Gdx.graphics.getHeight();
    public OrthographicCamera camera;
    public StretchViewport viewport;
	
	// Active Tile
    public boolean tileIsActive = false;
    public int activeX = 0;
    public int activeY = 0;
    
	public int gameState = 1;	
	public Tile[][] tiles;
	
	public Vector3 mouse_position = new Vector3(0,0,0);
	
	public int clickDelayCounter = 0;
    public int clickDelayCounterLength = 10;
	
	// ajeitar depois
	ArrayList<Texture> elementsT;
	//
	
	boolean soundEffects = false; 
	
	public Logic(QuimiCrush parent, Tile[][] tiles, ArrayList<Texture> elementsT, OrthographicCamera cam) {
		assetsManager = parent.assetsManager;
		// tells our asset manger that we want to load the sounds set in loadSounds method
		assetsManager.queueAddSounds();
		// tells the asset manager to load the sound and wait until finsihed loading.
		assetsManager.finishLoading();
		// loads the 2 sounds we use
		combine = assetsManager.MANAGER.get(assetsManager.COMBINE_SOUND, Sound.class);
		match = assetsManager.MANAGER.get(assetsManager.MATCH_SOUND, Sound.class);
		
		match.setVolume(0, parent.savePreferences.getSoundVolume());
		combine.setVolume(0, parent.savePreferences.getSoundVolume());
		soundEffects = parent.savePreferences.isSoundEffectsEnabled();
		
		this.tiles = tiles;
		
		//
		this.elementsT = elementsT;	
		//
		
		camera = cam;
        //viewport = new StretchViewport(windowWidth, windowHeight, camera);
        //viewport.apply();
	}
	
	
	  public void update() {
		  
	        // Players Turn
	        if (gameState == 0) {

	            mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            camera.unproject(mouse_position);
	            //mouse_position.x += 500;
	            //mouse_position.y = Gdx.graphics.getHeight() - mouse_position.y;
	            mouse_position.x += windowWidth / 2;
	            mouse_position.y += windowHeight / 2;
	            
	            //System.out.println("Largura(x) e Altura(y): "+Gdx.graphics.getWidth()+" "+Gdx.graphics.getHeight());
	            //System.out.println("Mouse Position x|y    : " + mouse_position.x + " " + mouse_position.y);

	            clickDelayCounter++;

	            // Check for Tile Swap
	            //if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
	            if (Gdx.input.isTouched()) {
	                if (clickDelayCounter > clickDelayCounterLength) {	                	
	                    // Check tile to left side
	                    if (tileIsActive) {
	                        if (activeX - 1 >= 0) {	                        	
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX - 1][activeY].type != 100)) {	                            
	                            	// Verica se está a esquerda
	                            	if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {	                            		                            		
	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    
	                                    tiles[activeX][activeY].type = tiles[activeX - 1][activeY].type;
	                                    tiles[activeX][activeY].sprite = new Sprite(this.elementsT.get(tiles[activeX][activeY].type));
	                                    
	                                    tiles[activeX - 1][activeY].type = savedType;	                                    
	                                    tiles[activeX - 1][activeY].sprite = new Sprite(this.elementsT.get(savedType));
	                                    
	                                    
	                                    clickDelayCounter = 0;
	                                    if (soundEffects)
	                                    	combine.play();

	                                }
	                            }
	                        }
	                    }
	                    // Check tile to right side
	                    if (tileIsActive) {
	                        if (activeX + 1 <= tiles.length - 1) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX + 1][activeY].type != 100)) {
	                            	// Verica se está a direita
	                            	if ((mouse_position.x > tiles[activeX + 1][activeY].x) && (mouse_position.x < tiles[activeX + 1][activeY].x + 64) && (mouse_position.y > tiles[activeX + 1][activeY].y) && (mouse_position.y < tiles[activeX + 1][activeY].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;	                                    

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX + 1][activeY].type;
	                                    tiles[activeX][activeY].sprite = new Sprite(this.elementsT.get(tiles[activeX][activeY].type));
	                                    
	                                    tiles[activeX + 1][activeY].type = savedType;
	                                    tiles[activeX + 1][activeY].sprite = new Sprite(this.elementsT.get(savedType));

	                                    clickDelayCounter = 0;
	                                    if (soundEffects)
	                                    	combine.play();

	                                }
	                            }
	                        }
	                    }
	                    // Check tile up
	                    if (tileIsActive) {
	                        if (activeY + 1 <= tiles.length - 1) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX][activeY + 1].type != 100)) {
	                            	// Verica se está em cima
	                            	if ((mouse_position.x > tiles[activeX][activeY + 1].x) && (mouse_position.x < tiles[activeX][activeY + 1].x + 64) && (mouse_position.y > tiles[activeX][activeY + 1].y) && (mouse_position.y < tiles[activeX][activeY + 1].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX][activeY + 1].type;
	                                    tiles[activeX][activeY].sprite = new Sprite(this.elementsT.get(tiles[activeX][activeY].type));
	                                    
	                                    tiles[activeX][activeY + 1].type = savedType;
	                                    tiles[activeX][activeY + 1].sprite = new Sprite(this.elementsT.get(savedType));

	                                    clickDelayCounter = 0;
	                                    if (soundEffects)
	                                    	combine.play();

	                                }
	                            }
	                        }
	                    }
	                    // Check tile down
	                    if (tileIsActive) {
	                        if (activeY - 1 >= 0) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX][activeY - 1].type != 100)) {
	                            	// Verica se está em baixo	                                
	                            	if ((mouse_position.x > tiles[activeX][activeY - 1].x) && (mouse_position.x < tiles[activeX][activeY - 1].x + 64) && (mouse_position.y > tiles[activeX][activeY - 1].y) && (mouse_position.y < tiles[activeX][activeY - 1].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX][activeY - 1].type;
	                                    tiles[activeX][activeY].sprite = new Sprite(this.elementsT.get(tiles[activeX][activeY].type));
	                                    	                                    
	                                    tiles[activeX][activeY - 1].type = savedType;
	                                    tiles[activeX][activeY - 1].sprite = new Sprite(this.elementsT.get(savedType));
	                                    
	                                    clickDelayCounter = 0;
	                                    if (soundEffects)
	                                    	combine.play();

	                                }
	                            }
	                        }
	                    }

	                    // Check for Matches
	                    gameState = 1;

	                }
	            }


	            //if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
	            if (Gdx.input.isTouched()) {
	                if (clickDelayCounter > clickDelayCounterLength) {	                	
	                    for (int i = 0; i < tiles.length; i++) {
	                        for (int j = 0; j < tiles.length; j++) {
	                        		                        	
	                            if ((mouse_position.x > tiles[i][j].x) && (mouse_position.x < tiles[i][j].x + 64) && (mouse_position.y > tiles[i][j].y) && (mouse_position.y < tiles[i][j].y + 64)) {	                            	
	                            
	                            	if (tiles[i][j].type != 100) {

	                                    // De-activate previous tile
	                                    tiles[activeX][activeY].activated = false;

	                                    // Active new tile
	                                    tiles[i][j].activated = true;
	                                    tileIsActive = true;
	                                    activeX = i;
	                                    activeY = j;

	                                    clickDelayCounter = 0;

	                                    System.out.println("Activated: " + i + " " + j);

	                                }

	                            }

	                        }
	                    }
	                }


	            }
	        }

	        // Detect Matches
	        if (gameState == 1) {
	            detectMatches();
	        }

	        // Tiles Falling
	        if (gameState == 2) {
	            tilesFalling();
	        }

	    }

	
	
	public void detectMatches() {
	        matched = false;

	        detectMatch5Hori();
	        detectMatch5Vert();
            detectMatch4Hori();
	        detectMatch4Vert();
	        detectMatch3Hori();
	        detectMatch3Vert();

	        if (matched) {
	            gameState = 2;
	        }
	        else {
	            // Player Turn
	            gameState = 0;
	        }
	}
	
	
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
	                        if (soundEffects)
	                        	match.play();
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


 
}