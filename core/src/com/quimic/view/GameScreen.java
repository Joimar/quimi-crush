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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;

public class GameScreen implements Screen {
	private QuimiCrush parent;
	
	private static final int H   = 0;
	private static final int O   = 1;
	private static final int C   = 2;
	private static final int N   = 3;
	private static final int Na  = 4;
	private static final int H2   = 5;
	private static final int O2   = 6;
	private static final int N2   = 7;
	private static final int Na2  = 8;	
	
	
	private Stage          stage; // Controla e reage às entradas do usuário	
	private ScreenViewport sv; // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un
	
	private OrthographicCamera cam;
	private final int sizeMapW = 6; // Largura da matriz do jogo
	private final int sizeMapH = 6; // Altura da matriz do jogo
	
	Table view;
	Table battle;
	Table info;
	Table game;
	
	 //SpriteBatch batch;	
		ArrayList<Texture> elementsT;	
		Tile[][] tiles;
		
		private TextureAtlas atlas; // Empacotamento das imagens 			
		private AtlasRegion  background; //
		private AtlasRegion  fieldBattle; //
		
		private AtlasRegion  hiei; //
		private AtlasRegion  yusuke; //
		private AtlasRegion  heart; //
		
		private Label infoLabel;
		Logic logic;
		
		public final float WIDTH_TILE;
		public final float HEIGHT_TILE; 
		public final float PROPORTION_WIDTH_BATTLE  = 1f;
		public final float PROPORTION_HEIGHT_BATTLE = 0.35f;
		public final float PROPORTION_WIDTH_INFO    = 0.2f;
		public final float PROPORTION_HEIGHT_INFO   = 0.65f;
		public final float PROPORTION_WIDTH_GAME    = 0.8f;
		public final float PROPORTION_HEIGHT_GAME   = 0.65f;				
		
		//public float tilesXOffset = 0;
	   // public float tilesYOffset = 0;	     		   
	
	    TextButton back;
	    
	public GameScreen(QuimiCrush parent) {		
		this.parent = parent;
		
		cam = new OrthographicCamera(parent.windowWidth, parent.windowHeight);		
		sv = new ScreenViewport(cam);
		stage = new Stage(sv);
		
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
		//..
		
		WIDTH_TILE = (parent.windowWidth * PROPORTION_WIDTH_GAME) / sizeMapW;
		HEIGHT_TILE = (parent.windowHeight * PROPORTION_HEIGHT_GAME) / sizeMapH;
	}
		

	@Override
	public void show() {		
		//batch = new SpriteBatch();
		
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		battle = new Table();
		//battle.setDebug(true);
		info   = new Table();
		//info.setDebug(true);
		game   = new Table();				
		//game.setDebug(true);
		view   = new Table();
		view.setFillParent(true);
		view.setDebug(true);							
				
		tiles = new Tile[sizeMapW][sizeMapH];
		for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				int type = MathUtils.random(0,4);
				Tile newTile = new Tile(new Sprite(elementsT.get(type)), type, WIDTH_TILE, HEIGHT_TILE);				
	            tiles[i][j] = newTile;	            
		    }
		}
		
		//cam = new OrthographicCamera(32,24);			
		logic = new Logic(parent, tiles, elementsT, cam, game, sizeMapW, sizeMapH);		
		
		parent.assetsManager.queueAddSkin(); // Carrega as skins  
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das skins
		Skin skin = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN); // Recupera a skin
								
		back = new TextButton("<", skin);
		//back.padLeft(parent.windowWidth);						
				
		/*view.setSize(parent.windowWidth, parent.windowHeight);
		view.setPosition(0, 0);*/
		
		/*battle.setSize(parent.windowWidth, parent.windowHeight*0.4f);
		battle.setPosition(0, 0);*/
		
		//game.setSize(parent.windowWidth*PROPORTION_WIDTH_GAME, parent.windowHeight*PROPORTION_WIDTH_GAME);				
		
		/*info.setSize(parent.windowWidth*0.2f, parent.windowHeight*0.6f);
		info.setPosition(0, parent.windowHeight);*/
		
		//info.setPosition(0, 0);
		atlas = parent.assetsManager.MANAGER.get(parent.assetsManager.LOADING_IMAGES);
		background = atlas.findRegion("background"); // Captura o background da tela do loading	
		fieldBattle = atlas.findRegion("fieldBattle"); // Captura o background da tela do loading
		hiei = atlas.findRegion("hiei_1"); // Captura o background da tela do loading
		yusuke = atlas.findRegion("yusuke_1"); // Captura o background da tela do loading
		heart = atlas.findRegion("heart"); // Captura o background da tela do loading
		
		infoLabel       = new Label("?", skin, "title");	
		
		/*view.add(battle).colspan(2);//.fillX();
		view.row();		
		view.add(info).left();//.width(parent.windowWidth*0.8f).height(parent.windowHeight*0.7f);
		view.add(game);			*/	
		view.top();
		view.add(battle).fillX().colspan(2);		
		Image iYusuke = new Image(yusuke);
		Image iHiei = new Image(hiei);
		Image iHeart[] = new Image[6]; 
		for (int i = 0; i < iHeart.length; i++) {
			iHeart[i] =	new Image(heart);
		}		
		battle.add(iHeart[0]);
		battle.add(iHeart[1]);
		battle.add(iHeart[2]).padRight(50f);
		battle.add(iHeart[3]);
		battle.add(iHeart[4]);
		battle.add(iHeart[5]);
		battle.row();
		battle.add(iYusuke).colspan(3).padRight(50f);
		battle.add(iHiei).colspan(3);
		
		view.row().left().bottom();
		view.add(game);
				
		for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				game.add(tiles[i][j]);					            	           
		    }
			game.row();
		}	
		view.right();		
		info.top();
		info.add(infoLabel);
		info.row();
		info.setSize(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);
		back.setFillParent(true);
		//back.setScale(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);
		//info.add(back);
		view.add(info);		
		
			
		//info.setBackground(new TiledDrawable(background));
		//info.setSize(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);
		//battle.setSize(parent.windowWidth*PROPORTION_WIDTH_BATTLE, parent.windowHeight*PROPORTION_HEIGHT_BATTLE);
		
		//fieldBattle.setRegionHeight((int) Math.ceil(parent.windowHeight*PROPORTION_HEIGHT_BATTLE));
		battle.setBackground(new TiledDrawable(fieldBattle));
		
		stage.addActor(view);
		
		back.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(QuimiCrush.MAIN);
			}
		});
		
		for (int i = 0; i < game.getCells().size; i++) {								
			System.out.println((((Tile) game.getCells().get(i).getActor()).getRight())); 			
		}		
	}
	
	@Override
	public void render(float delta) {						
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		logic.update();
		
		for (int i = 0; i < game.getCells().size; i++) {								
			if (((Tile) game.getCells().get(i).getActor()).activated) { 
				game.getCells().get(i).getActor().setColor(0, 0, 0, 0.5f);					
			}                
			game.getCells().get(i).getActor().setColor(1f, 1f, 1f, 1f);	      			
		}		
		
		stage.act();
		stage.draw();
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
		//batch.dispose();		
		//img.dispose();
	}

}
