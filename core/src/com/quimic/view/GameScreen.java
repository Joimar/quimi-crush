package com.quimic.view;

import java.util.ArrayList;

import javax.print.attribute.standard.Finishings;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.animation.HeroAnimation;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;

public class GameScreen implements Screen {
	private QuimiCrush parent;
	
	public static final int H     = 0;
	public static final int O     = 1;
	public static final int C     = 2;
	public static final int N     = 3;
	public static final int Na    = 4;
	public static final int H2    = 5;
	public static final int O2    = 6;
	public static final int N2    = 7;
	public static final int Na2   = 8;
	public static final int H2O   = 9;	
	public static final int CO2   = 10;	
	public static final int N2O   = 11;
	public static final int Na2O  = 12;	

//*************************************************************//
	public static final int HERO_IDLE   = 0;
	public static final int HERO_ATTACK = 1;
	public static final int HERO_DAMAGE = 2;
	public static final int HERO_DIE    = 3;
	
//*************************************************************//	
	public final float PROPORTION_WIDTH_BATTLE  = 1f;
	public final float PROPORTION_HEIGHT_BATTLE = 0.35f;
	public final float PROPORTION_WIDTH_INFO    = 0.2f;
	public final float PROPORTION_HEIGHT_INFO   = 0.65f;
	public final float PROPORTION_WIDTH_GAME    = 0.8f;
	public final float PROPORTION_HEIGHT_GAME   = 0.65f;	
	
	private final int sizeMapW = 6; // Largura da matriz do jogo
	private final int sizeMapH = 6; // Altura da matriz do jogo
	private final int QTD_INFO = 4; // Quantidade de itens para informação/ajuda do jogo

//*************************************************************//		
	private Stage          stage; // Controla e reage às entradas do usuário	
	private ScreenViewport sv; // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un	
	private OrthographicCamera cam;
			
	public float WIDTH_OBJECT_INFO;
	public float HEIGHT_OBJECT_INFO;
	public float WIDTH_TILE;
	public float HEIGHT_TILE; 	 

//*************************************************************//		
	Logic logic;
	
	Table view;
	Table battle;
	Table info;
	Table game;		
	
//*************************************************************//			
	ArrayList<Texture> elementsT;	
	Tile[][] tiles;
	Tile[] tilesInfo;
	
	private Label infoLabel;
	private TextureAtlas atlas; // Empacotamento das imagens 			
	private AtlasRegion  background; //
	private AtlasRegion  fieldBattle; //
	
	private AtlasRegion  hiei; //
	private AtlasRegion  yusuke; //
	private AtlasRegion  heart; //		
	
	Animation       heroIdleAnimation;	
	Animation       heroAttackAnimation;
	Animation       heroDamageAnimation;
	Animation       heroDieAnimation;
	
	/*TextureRegion[] HeroAttackRegions;	
	TextureRegion[] HeroDamageRegions;
	TextureRegion[] HeroDieRegions;*/
	
	HeroAnimation heroAnimation;
	//SpriteBatch batch;
	
	TextButton back;
	
//*************************************************************//		    
	/**
	 *     
	 * @param parent
	 */
	public GameScreen(QuimiCrush parent) {		
		this.parent = parent;
		
		cam = new OrthographicCamera(parent.windowWidth, parent.windowHeight);		
		sv = new ScreenViewport(cam);
		stage = new Stage(sv);
		
		//batch = new SpriteBatch();		
		
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
		elementsT.add(new Texture("images/game/chemic/Na2.png")); // 8		
		// Os matches (combinação completa)
		elementsT.add(new Texture("images/game/chemic/H2O.png"));  // 9
		elementsT.add(new Texture("images/game/chemic/CO2.png"));  // 10
		elementsT.add(new Texture("images/game/chemic/N2O.png"));  // 11
		elementsT.add(new Texture("images/game/chemic/Na2O.png")); // 12
		
		WIDTH_TILE  = (parent.windowWidth * PROPORTION_WIDTH_GAME) / sizeMapW;
		HEIGHT_TILE = (parent.windowHeight * PROPORTION_HEIGHT_GAME) / sizeMapH;
		
		WIDTH_OBJECT_INFO  = (parent.windowWidth * PROPORTION_WIDTH_INFO);
		HEIGHT_OBJECT_INFO = (parent.windowHeight * PROPORTION_HEIGHT_INFO) / (QTD_INFO+4);
		
		tiles = new Tile[sizeMapW][sizeMapH];
		tilesInfo = new Tile[QTD_INFO];
		for (int i = 0; i < QTD_INFO; i++) {
			tilesInfo[i] = new Tile(new Sprite(elementsT.get(H2O+i)), H2O+i, WIDTH_OBJECT_INFO*0.8f, HEIGHT_OBJECT_INFO*0.8f);
		}
		
		atlas = parent.assetsManager.MANAGER.get(parent.assetsManager.LOADING_IMAGES);
		this.loadAnimations();
	}

	/**
	 * 
	 */
	private void loadAnimations() {		
		hiei = atlas.findRegion("hiei_1"); // Captura o background da tela do loading
		yusuke = atlas.findRegion("attack_01"); // Captura o background da tela do loading
		heart = atlas.findRegion("heart"); // Captura o background da tela do loading
				
		Array<TextureRegion> regionsT;
		regionsT = this.addRegionsArray("attack_", 1, 1);
		heroIdleAnimation = new Animation(1f, regionsT, PlayMode.NORMAL);
		regionsT = this.addRegionsArray("attack_", 1, 4);
		heroAttackAnimation = new Animation(0.5f, regionsT, PlayMode.NORMAL);
		
		heroAnimation = new HeroAnimation(heroIdleAnimation);
		heroAnimation.finish = true;		
	}
	
	/**
	 * 
	 * @param name
	 * @param begin
	 * @param end
	 * @return
	 */
	private Array<TextureRegion> addRegionsArray(String name, int begin, int end) {
		Array<TextureRegion> array = new Array<TextureRegion>();		
		String auxN = "0";
		for (int i = begin; i <= end; i++) {
			if (i < 10)
				auxN = "0"+i;
			else 
				auxN = ""+i;
			array.add(atlas.findRegion(name+auxN));		
		}
		
		return array;
	}

	@Override
	public void show() {		
		//batch = new SpriteBatch();
		
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		battle = new Table();
		battle.setDebug(true);
		info   = new Table();
		//info.setDebug(true);
		game   = new Table();				
		//game.setDebug(true);
		view   = new Table();
		view.setFillParent(true);
		view.setDebug(true);							
							
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
				
		/*view.setSize(parent.windowWidth, parent.windowHeight);
		view.setPosition(0, 0);*/
		
		/*battle.setSize(parent.windowWidth, parent.windowHeight*0.4f);
		battle.setPosition(0, 0);*/
		
		//game.setSize(parent.windowWidth*PROPORTION_WIDTH_GAME, parent.windowHeight*PROPORTION_WIDTH_GAME);				
		
		/*info.setSize(parent.windowWidth*0.2f, parent.windowHeight*0.6f);
		info.setPosition(0, parent.windowHeight);*/
		
		//info.setPosition(0, 0);		
		background = atlas.findRegion("background"); // Captura o background da tela do loading	
		fieldBattle = atlas.findRegion("fieldBattle"); // Captura o background da tela do loading		
		
		infoLabel       = new Label("?", skin, "title");	
				
		view.top();
		view.add(battle).expandX().colspan(2);	
		Image iYusuke = new Image(yusuke);
		Image iHiei = new Image(hiei);
		Image iHeart[] = new Image[10]; 
		for (int i = 0; i < iHeart.length; i++) {
			iHeart[i] =	new Image(heart);
		}	
		battle.bottom().padBottom(20);
		battle.add(iHeart[0]);
		battle.add(iHeart[1]);
		battle.add(iHeart[2]);
		battle.add(iHeart[3]);
		battle.add(iHeart[4]).padRight(50f);
		battle.add(iHeart[5]);
		battle.add(iHeart[6]);
		battle.add(iHeart[7]);
		battle.add(iHeart[8]);
		battle.add(iHeart[9]);//.padRight(50f);	
		battle.row().bottom();
		battle.add(heroAnimation).colspan(5).padRight(50f);		
		battle.add(iHiei).colspan(5);
		
		view.row().left().bottom();
		view.add(game);
				
		for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				game.add(tiles[i][j]);					            	           
		    }
			game.row();
		}	
		view.right();				
		info.add(infoLabel);				
		for (int i = 0; i < QTD_INFO; i++) {
			info.row().padBottom(10f);
			info.add(tilesInfo[i]);			
		}
		
		info.setSize(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);
		back.setFillParent(true);
		//back.setScale(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);
		//info.add(back);
		view.add(info);		
					
		view.setBackground(new TiledDrawable(background));		
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
	}
	
	@Override
	public void render(float delta) {						
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);									
		
//////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		* 
		* 
		* PAREI AQUI TENTANDO COLOCAR YUSUKE PARA MEXER
		* 
		* 
		*/								
		if (logic.heroState == HERO_IDLE)
			heroAnimation.finish = true;				
		else
			heroAnimation.finish = heroAnimation.finished();
		
		System.out.println("HERO STATE: "+logic.heroState);
		System.out.println("FINISH: "+heroAnimation.finish);		
		
		if (heroAnimation.finish && logic.heroState != HERO_IDLE) {
			logic.gameState = logic.PLAYER_STATE;			
			logic.heroState = HERO_IDLE;
		}								
		
		if (heroAnimation.finish) {
			if (logic.heroState == HERO_IDLE) {				
				heroAnimation = new HeroAnimation(heroIdleAnimation);				
			} else if (logic.heroState == HERO_ATTACK) {
				heroAnimation = new HeroAnimation(heroAttackAnimation);				
			}
		}
//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		logic.update();
		
		for (int i = 0; i < game.getCells().size; i++) {								
			if (((Tile) game.getCells().get(i).getActor()).activated)  
				((Tile) game.getCells().get(i).getActor()).setDebug(true);
			else 
				((Tile) game.getCells().get(i).getActor()).setDebug(false);			
		}		
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		/*System.out.println(parent.windowWidth+" : "+parent.windowHeight);
		System.out.println(width+" : "+height);
		
		battle.setSize(parent.windowWidth*PROPORTION_WIDTH_BATTLE, parent.windowHeight*PROPORTION_HEIGHT_BATTLE);
		game.setSize(parent.windowWidth*PROPORTION_WIDTH_GAME, parent.windowHeight*PROPORTION_HEIGHT_GAME);
		info.setSize(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);	
		view.setSize(width, height);
		
		WIDTH_TILE  = (width * PROPORTION_WIDTH_GAME) / sizeMapW;
		HEIGHT_TILE = (height * PROPORTION_HEIGHT_GAME) / sizeMapH;
		
		WIDTH_OBJECT_INFO  = (width * PROPORTION_WIDTH_INFO);
		HEIGHT_OBJECT_INFO = (height * PROPORTION_HEIGHT_INFO) / (QTD_INFO+4);
		
		tiles = new Tile[sizeMapW][sizeMapH];
		tilesInfo = new Tile[QTD_INFO];
		for (int i = 0; i < QTD_INFO; i++) {
			info.getCells().get(i).getActor().setSize(WIDTH_OBJECT_INFO*0.8f, HEIGHT_OBJECT_INFO*0.8f);			
		}
		
		for (int i = 0; i < (sizeMapW*sizeMapH); i++) {
			game.getCells().get(i).getActor().setSize(WIDTH_TILE, HEIGHT_TILE);			
		}	
		
		stage.act();
		stage.draw();*/
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
