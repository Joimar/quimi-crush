package com.quimic.view;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;

public class GameScreen implements Screen {
	private QuimiCrush parent;
	
	 SpriteBatch batch;	
		ArrayList<Texture> elementsT;	
		Tile[][] tiles;
		
		Logic logic;
		
		public float tilesXOffset = 0;
	    public float tilesYOffset = 0;	     
	
	
	public GameScreen(QuimiCrush parent) {		
		this.parent = parent;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");			
		
		elementsT = new ArrayList<Texture>();
		// Os elementos simples da tabela		
		elementsT.add(new Texture("chemic/H.png"));  // 0		
		elementsT.add(new Texture("chemic/O.png"));  // 1
		elementsT.add(new Texture("chemic/C.png"));  // 2
		elementsT.add(new Texture("chemic/N.png"));  // 3
		elementsT.add(new Texture("chemic/Na.png")); // 4	
		
		// Os elementos com duas combinações
		elementsT.add(new Texture("chemic/H2.png"));  // 5
		elementsT.add(new Texture("chemic/O2.png"));  // 6
		elementsT.add(new Texture("chemic/N2.png"));  // 7
		elementsT.add(new Texture("chemic/Na2.png"));  // 8
		
		// Os matches (combinação completa)
		
		
		tiles = new Tile[6][6];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				int type = MathUtils.random(0,4);
				Tile newTile = new Tile(new Sprite(elementsT.get(type)), type, (i * 64) + tilesXOffset, (j * 64) + tilesYOffset);				
	            tiles[i][j] = newTile;	            
		    }
		}
		
		logic = new Logic(tiles, elementsT);	

	}

	@Override
	public void render(float delta) {
		logic.update();
		
		// Limpar a tela
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Desenha a textura
		batch.begin();	
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {						
				if (tiles[i][j].activated) { 
                    batch.setColor(0, 0, 0, 0.5f);
				}                
				batch.draw(tiles[i][j].sprite, tiles[i][j].x, tiles[i][j].y, 64, 64);
	            batch.setColor(1,1,1,1);
		    }
		}
		
		
		batch.end();		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();		
		//img.dispose();
	}

}
