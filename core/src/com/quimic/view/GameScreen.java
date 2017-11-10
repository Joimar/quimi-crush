package com.quimic.view;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;

public class GameScreen implements Screen {
	private QuimiCrush parent;
	
	private Stage          stage; // Controla e reage às entradas do usuário	
	private ScreenViewport sv; // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un
	
	 SpriteBatch batch;	
		ArrayList<Texture> elementsT;	
		Tile[][] tiles;
		
		Logic logic;
		private OrthographicCamera cam;
		
		public float tilesXOffset = 0;
	    public float tilesYOffset = 0;	     		   
	
	    TextButton back;
	    
	public GameScreen(QuimiCrush parent) {		

		this.parent = parent;
	}
		

	@Override
	public void show() {
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");			
		
		elementsT = new ArrayList<Texture>();
		// Os elementos simples da tabela		
		elementsT.add(new Texture("images/game/chemic/H.png"));  // 0		
		elementsT.add(new Texture("images/game/chemic/O.png"));  // 1
		elementsT.add(new Texture("images/game/chemic/C.png"));  // 2
		elementsT.add(new Texture("images/game/chemic/N.png"));  // 3
		elementsT.add(new Texture("images/game/chemic/Na.png")); // 4	
		
		// Os elementos com duas combinações
		elementsT.add(new Texture("images/game/chemic/H2.png"));  // 5
		elementsT.add(new Texture("images/game/chemic/O2.png"));  // 6
		elementsT.add(new Texture("images/game/chemic/N2.png"));  // 7
		elementsT.add(new Texture("images/game/chemic/Na2.png"));  // 8
		
		// Os matches (combinação completa)
		
		
		tiles = new Tile[6][6];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				int type = MathUtils.random(0,4);
				Tile newTile = new Tile(new Sprite(elementsT.get(type)), type, (i * 64) + tilesXOffset, (j * 64) + tilesYOffset);				
	            tiles[i][j] = newTile;	            
		    }
		}
		
		//cam = new OrthographicCamera(32,24);
		cam = new OrthographicCamera(parent.windowWidth, parent.windowHeight);				
		logic = new Logic(parent, tiles, elementsT, cam);		
		
		parent.assetsManager.queueAddSkin(); // Carrega as skins  
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das skins
		Skin skin = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN); // Recupera a skin
					
		stage = new Stage(new ScreenViewport());		
		back = new TextButton("Voltar", skin);
		back.padLeft(parent.windowWidth);		
		stage.addActor(back);
		Gdx.input.setInputProcessor(stage);
		back.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
			}
		});		
	}

	@Override
	public void render(float delta) {						
		logic.update();
		
		// Limpar a tela
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		BitmapFont font = new BitmapFont();

		// Desenha a textura	
		back.right();
		batch.begin();
		font.setColor(Color.BLACK);
		font.draw(batch, "Voltar", 50, parent.windowHeight-100, parent.windowWidth-100, 100, false);		
		
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
		
		if (Gdx.input.isTouched()) 
			if (Gdx.input.getX() >= (parent.windowWidth-100) && (parent.windowHeight-Gdx.input.getY()) >= (parent.windowHeight-150))
				parent.changeScreen(QuimiCrush.MAIN);
		
		System.out.println(Gdx.input.getX()+ " : "+ Gdx.input.getY());
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
