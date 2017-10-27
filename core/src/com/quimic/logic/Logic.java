package com.quimic.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.quimic.tile.Tile;

public class Logic {

	public boolean matched = false;
	public int savedType = 0;
	
	// Active Tile
    public boolean tileIsActive = false;
    public int activeX = 0;
    public int activeY = 0;
    
	public int gameState = 1;	
	public Tile[][] tiles;
	
	public Vector3 mouse_position = new Vector3(0,0,0);
	
	public int clickDelayCounter = 0;
    public int clickDelayCounterLength = 15;
	
	// ajeitar depois
	ArrayList<Texture> elementsT;
	//
	
	
	public Logic(Tile[][] tiles, ArrayList<Texture> elementsT) {
		this.tiles = tiles;
		
		//
		this.elementsT = elementsT;	
		//
	}
	
	
	  public void update() {
		  
	        // Players Turn
	        if (gameState == 0) {

	            mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            // camera.unproject(mouse_position);
	            //mouse_position.x += 500;
	            mouse_position.y = Gdx.graphics.getHeight() - mouse_position.y;
	            
	            //System.out.println("Largura(x) e Altura(y): "+Gdx.graphics.getWidth()+" "+Gdx.graphics.getHeight());
	            //System.out.println("Mouse Position x|y    : " + mouse_position.x + " " + mouse_position.y);

	            clickDelayCounter++;

	            // Check for Tile Swap
	            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {	            	
	                if (clickDelayCounter > clickDelayCounterLength) {	                	
	                    // Check tile to left side
	                    if (tileIsActive) {
	                        if (activeX - 1 >= 0) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX - 1][activeY].type != 100)) {
	                                if ((mouse_position.x > tiles[activeX - 1][activeY].x) && (mouse_position.x < tiles[activeX - 1][activeY].x + 64) && (mouse_position.y > tiles[activeX - 1][activeY].y) && (mouse_position.y < tiles[activeX - 1][activeY].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX - 1][activeY].type;
	                                    tiles[activeX - 1][activeY].type = savedType;

	                                    clickDelayCounter = 0;

	                                }
	                            }
	                        }
	                    }
	                    // Check tile to right side
	                    if (tileIsActive) {
	                        if (activeX + 1 <= tiles.length - 1) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX + 1][activeY].type != 100)) {
	                                if ((mouse_position.x > tiles[activeX + 1][activeY].x) && (mouse_position.x < tiles[activeX + 1][activeY].x + 64) && (mouse_position.y > tiles[activeX + 1][activeY].y) && (mouse_position.y < tiles[activeX + 1][activeY].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX + 1][activeY].type;
	                                    tiles[activeX + 1][activeY].type = savedType;

	                                    clickDelayCounter = 0;

	                                }
	                            }
	                        }
	                    }
	                    // Check tile up
	                    if (tileIsActive) {
	                        if (activeY + 1 <= tiles.length - 1) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX][activeY + 1].type != 100)) {
	                                if ((mouse_position.x > tiles[activeX][activeY + 1].x) && (mouse_position.x < tiles[activeX][activeY + 1].x + 64) && (mouse_position.y > tiles[activeX][activeY + 1].y) && (mouse_position.y < tiles[activeX][activeY + 1].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX][activeY + 1].type;
	                                    tiles[activeX][activeY + 1].type = savedType;

	                                    clickDelayCounter = 0;

	                                }
	                            }
	                        }
	                    }
	                    // Check tile down
	                    if (tileIsActive) {
	                        if (activeY - 1 >= 0) {
	                            if ((tiles[activeX][activeY].type != 100) && (tiles[activeX][activeY - 1].type != 100)) {
	                                if ((mouse_position.x > tiles[activeX][activeY - 1].x) && (mouse_position.x < tiles[activeX][activeY - 1].x + 64) && (mouse_position.y > tiles[activeX][activeY - 1].y) && (mouse_position.y < tiles[activeX][activeY - 1].y + 64)) {

	                                    // De-activate
	                                    tileIsActive = false;
	                                    tiles[activeX][activeY].activated = false;

	                                    // Swap Types
	                                    savedType = tiles[activeX][activeY].type;
	                                    tiles[activeX][activeY].type = tiles[activeX][activeY - 1].type;
	                                    tiles[activeX][activeY - 1].type = savedType;

	                                    clickDelayCounter = 0;

	                                }
	                            }
	                        }
	                    }

	                    // Check for Matches
	                    gameState = 1;

	                }
	            }


	            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
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

/*	        detectMatch5Hori();
	        detectMatch5Vert();
	        detectMatch4Hori();
	        detectMatch4Vert();
	        detectMatch3Hori();
	        detectMatch3Vert();
*/
	        if (matched) {
	            gameState = 2;
	        }
	        else {
	            // Player Turn
	            gameState = 0;
	        }
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
                            tiles[i][j - 1].type = savedType;

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


 
}