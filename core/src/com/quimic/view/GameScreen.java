package com.quimic.view;

import java.awt.TextArea;
import java.awt.TextField;
import java.util.ArrayList;

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;

abstract public class GameScreen implements Screen {
	protected QuimiCrush parent;
	
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

	public static final int HERO_IDLE   = 0;
	public static final int HERO_ATTACK = 1;
	public static final int HERO_HEAL   = 2;
	public static final int HERO_DAMAGE = 3;
	public static final int HERO_DIE    = 4;	
	
	public static final int ENEMY_IDLE   = 6;
	public static final int ENEMY_ATTACK = 7;
	public static final int ENEMY_HEAL   = 8;
	public static final int ENEMY_DAMAGE = 9;
	public static final int ENEMY_DIE    = 10;
	
	public static final int GAME_IDLE     = 20;
	public static final int GAME_CONTINUE = 21;
	public static final int GAME_WIN      = 22;
	public static final int GAME_LOSE     = 23;
	public static final int GAME_END 	  = 24;
	
//*************************************************************//	
	public final float PROPORTION_WIDTH_BATTLE  = 1f;
	public final float PROPORTION_HEIGHT_BATTLE = 0.35f;
	public final float PROPORTION_WIDTH_INFO    = 0.2f;
	public final float PROPORTION_HEIGHT_INFO   = 0.65f;
	public final float PROPORTION_WIDTH_GAME    = 0.8f;
	public final float PROPORTION_HEIGHT_GAME   = 0.65f;	
	
	/*protected final int sizeMapW; // Largura da matriz do jogo
	protected final int sizeMapH; // Altura da matriz do jogo */
	protected int QTD_INFO   = 4; // Valor default da quantidade de itens para informação/ajuda do jogo
	protected int LIFE_HERO  = 5; // Valores default da vida maxima do heroi
	protected int LIFE_ENEMY = 3;  // Valores default da vida maxima do inimigo

//*************************************************************//		
	protected Stage          stage; // Controla e reage às entradas do usuário	
	protected Stage          stageInfo; // Controla e reage às entradas do usuário para a tela de informações
	protected ScreenViewport sv; // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un	
	protected OrthographicCamera cam;
			
	protected float WIDTH_OBJECT_INFO;
	protected float HEIGHT_OBJECT_INFO;
	protected float WIDTH_TILE;
	protected float HEIGHT_TILE; 	 

//*************************************************************//		
	protected Logic logic;	
	
	protected Table view;
	protected Table battle;
	protected Table info;
	protected Table game;		
	
	protected Skin skin;
	
	protected int battleState = GAME_CONTINUE;
	
	protected Window endGameWindow;
	protected Label msgTitleEndGame;
	protected Label backMainLabel;
	protected Label replayGameLabel;
	
//*************************************************************//
	protected boolean isInfoScreen;
		
	protected int pageInfo;
	
	protected Window infoWindow;
	
	protected Label msgTitleInfo;	
	protected Label backInfo;
	protected Label nextInfo;
	protected Label closeInfo;
	
	protected Table tableH2O;
	protected Table tableCO2;
	protected Table tableN2O;
	protected Table tableNa2O;
	
//*************************************************************//			
	protected ArrayList<Texture> elementsTSelected;	
	protected ArrayList<Texture> elementsT;	
	protected Tile[][] tiles;
	//protected Tile[] tilesInfo;
	
	protected Label infoLabel;
	protected Label preferencesLabel;
	protected Label mainLobbyLabel;
	protected TextureAtlas atlas; // Empacotamento das imagens 			
	protected AtlasRegion  background; //
	protected AtlasRegion  fieldBattle; //
	
	protected AtlasRegion  hero_idle; //
	protected AtlasRegion  enemy_idle; //
	protected AtlasRegion  hero_die; //
	protected AtlasRegion  enemy_die; //
	protected AtlasRegion  heart; //
	protected AtlasRegion  nonHeart; //		
		
	protected Animation heroAttackAnimation;
	//Animation       heroHealAnimation;
	protected Animation heroDamageAnimation;
	protected Animation heroDieAnimation;
	protected Animation heroWinAnimation;    
		
	protected Animation enemyAttackAnimation;
	//Animation       enemyHealAnimation;
	protected Animation enemyDamageAnimation;
	protected Animation enemyDieAnimation;
	
	protected boolean heroAnimationFinish  = true;
	protected boolean enemyAnimationFinish = true;
	protected int lifeHero;
	protected int lifeEnemy;	
	protected SpriteBatch batch;
	protected float stateTime;

//*************************************************************//			
	protected ShapeRenderer blockRect;
	
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
		stageInfo = new Stage(sv); // Palco para o catalogo de combinações
		
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
		
		elementsTSelected = new ArrayList<Texture>();
		// Os elementos simples da tabela quando selecionado		
		elementsTSelected.add(new Texture("images/game/chemic/selected/sH.png"));  // 0		
		elementsTSelected.add(new Texture("images/game/chemic/selected/sO.png"));  // 1
		elementsTSelected.add(new Texture("images/game/chemic/selected/sC.png"));  // 2
		elementsTSelected.add(new Texture("images/game/chemic/selected/sN.png"));  // 3
		elementsTSelected.add(new Texture("images/game/chemic/selected/sNa.png")); // 4			
		// Os elementos com duas combinações quando selecionado
		elementsTSelected.add(new Texture("images/game/chemic/selected/sH2.png"));  // 5
		elementsTSelected.add(new Texture("images/game/chemic/selected/sO2.png"));  // 6
		elementsTSelected.add(new Texture("images/game/chemic/selected/sN2.png"));  // 7
		elementsTSelected.add(new Texture("images/game/chemic/selected/sNa2.png")); // 8	
		
		/*WIDTH_TILE  = (parent.windowWidth * PROPORTION_WIDTH_GAME) / sizeMapW;
		HEIGHT_TILE = (parent.windowHeight * PROPORTION_HEIGHT_GAME) / sizeMapH;
		
		WIDTH_OBJECT_INFO  = (parent.windowWidth * PROPORTION_WIDTH_INFO);
		HEIGHT_OBJECT_INFO = (parent.windowHeight * PROPORTION_HEIGHT_INFO) / (QTD_INFO+4);
		
		tiles = new Tile[sizeMapW][sizeMapH];
		tilesInfo = new Tile[QTD_INFO];
		for (int i = 0; i < QTD_INFO; i++) {
			tilesInfo[i] = new Tile(new Sprite(elementsT.get(H2O+i)), H2O+i, WIDTH_OBJECT_INFO*0.8f, HEIGHT_OBJECT_INFO*0.8f);
		}*/
		
		atlas = parent.assetsManager.MANAGER.get(parent.assetsManager.LOADING_IMAGES);
		
		parent.assetsManager.queueAddSkin(); // Carrega as skins  
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das skins
		skin = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN); // Recupera a skin

		// Configura o componente que ira desenhar uma região escura na tela enquanto não for o turno do jogador  
		blockRect = new ShapeRenderer();
		blockRect.setColor(new Color(0, 0, 0, 0.5f));					
		
		//endGameWindow = new Window("", skin);			
		//String msgTitleEndGame;
		backMainLabel = new Label("Voltar ao menu", skin);
		replayGameLabel = new Label("Jogar novamente", skin);		
		this.endGameEventListener();
	}

	/**
	 * 
	 */
	protected void endGameEventListener() {
		backMainLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				parent.changeScreen(parent.MAIN);
			}	   
		});
		
		replayGameLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				parent.changeLevel(currentLevel);
			}	   
		});
	}
	
	/**
	 * Recupera a sequência de sprites númerados de '01' até um determinado número
	 * 
	 * @param name
	 * @param begin
	 * @param end
	 * @return
	 */
	protected Array<TextureRegion> addRegionsArray(String name, int begin, int end) {
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

	/**
	 * 
	 * @param on
	 */
	protected void DebugOnOf(Stage s, boolean on) {
		s.setDebugAll(on);
	}
			
	@Override
	public void show() {
		batch = new SpriteBatch();
		stateTime = 0;
		
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		battle = new Table();		
		info   = new Table();
		game   = new Table();				
		view   = new Table();
		view.setFillParent(true);						
							
		/*for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				int type = MathUtils.random(0,4);
				Tile newTile = new Tile(new Sprite(elementsT.get(type)), type, WIDTH_TILE, HEIGHT_TILE);				
	            tiles[i][j] = newTile;	            
		    }
		}
		
		//cam = new OrthographicCamera(32,24);			
		logic = new Logic(parent, tiles, elementsT, cam, game, sizeMapW, sizeMapH);		*/																			
							
		background = atlas.findRegion("background"); // Captura o background da tela do loading	
		fieldBattle = atlas.findRegion("fieldBattle"); // Captura o background da tela do loading		
		
		// Opções da area de informaçoes -> menu lateral
		infoLabel        = new Label("?", skin, "title");
		preferencesLabel = new Label("#", skin, "title");
		mainLobbyLabel   = new Label("=", skin, "title");
		
		preferencesLabel.setBounds(preferencesLabel.getX(), preferencesLabel.getY(), preferencesLabel.getWidth()*0.5f, preferencesLabel.getHeight()*0.5f);
		
		view.top();
		view.add(battle).expandX().colspan(2);
		view.row().left().bottom();
		view.add(game);
		view.right();				
		info.add(infoLabel);		
		info.row();
		//info.add(preferencesLabel);
		//info.row();
		info.add(mainLobbyLabel);
		
	/*	// BATTLE
		lifeHero  = LIFE_HERO;
		lifeEnemy = LIFE_ENEMY;
		
		battle.bottom().padBottom(20);
		battle.row().bottom();
		battle.add().colspan(5).padRight(100f);		
		battle.add().colspan(5);
		
		// GAME
		for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				game.add(tiles[i][j]);					            	           
		    }
			game.row();
		}	
		
		// INFO
		for (int i = 0; i < QTD_INFO; i++) {
			info.row().padBottom(10f);
			info.add(tilesInfo[i]);			
		}	*/									
		
		view.add(info);		
					
		view.setBackground(new TiledDrawable(background));		
		//info.setSize(parent.windowWidth*PROPORTION_WIDTH_INFO, parent.windowHeight*PROPORTION_HEIGHT_INFO);
		//battle.setSize(parent.windowWidth*PROPORTION_WIDTH_BATTLE, parent.windowHeight*PROPORTION_HEIGHT_BATTLE);
		
		//fieldBattle.setRegionHeight((int) Math.ceil(parent.windowHeight*PROPORTION_HEIGHT_BATTLE));		
		battle.setBackground(new TiledDrawable(fieldBattle));			
		
		stage.addActor(view);		
	}
	
	/**
	 * 
	 */
	protected void infoGamePanelAction() {			
		infoLabel.addListener(new TextTooltip("Manual de combinacoes", skin));
		preferencesLabel.addListener(new TextTooltip("Tela de ajustes", skin));
		mainLobbyLabel.addListener(new TextTooltip("Ir para o menu principal", skin));
		
		pageInfo = 1;		
		
		infoWindow   = new Window("Manual", skin);	
		infoWindow.getTitleLabel().setColor(0.9f, 0.9f, 0.5f, 1f);
		infoWindow.setBounds(0, 0, parent.windowWidth, parent.windowHeight-10);
		
		msgTitleInfo = new Label("Agua", skin, "bold");
		backInfo     = new Label("<", skin, "title");
		nextInfo     = new Label(">", skin, "title");
		closeInfo    = new Label("x", skin, "title");
		
		this.createInfosIn();

		infoWindow.add(msgTitleInfo).colspan(3).top().expand();
		infoWindow.row();
		infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
		infoWindow.row().bottom();
		infoWindow.add(tableH2O).colspan(3).top().expand().pad(10);
		infoWindow.row();
		infoWindow.add(backInfo).bottom().left().expandX().padLeft(15);
		infoWindow.add(closeInfo).bottom().expandX().padLeft(5).padRight(5);
		infoWindow.add(nextInfo).bottom().right().expandX().padRight(15);
		
		stageInfo.addActor(infoWindow);				
		
		// Ir para o menu principal do jogo
		mainLobbyLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {													
				new Dialog("", skin) {
					{
						text("Abandonar fase?");						
						button("nao", "n");
						button("sim", "s");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						if (object.toString().equals("s")) 
							parent.changeScreen(parent.MAIN);
					}
				}.show(stage).setWidth(parent.windowWidth);;				
			}		   
		});
		
		// Evento para mostrar as combinações possíveis no jogo
		infoLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {													
				isInfoScreen = true;
				Gdx.input.setInputProcessor(stageInfo);
			}		   
		});
		// Evento para fechar a página de informações
		closeInfo.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {													
				isInfoScreen = false;
				Gdx.input.setInputProcessor(stage);
			}		   
		});
		// Evento para ir para o proximo catalogo de combinações
		backInfo.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {													
				if ((pageInfo-1) <= 0)
					return;			
				infoWindow.clear();				
				switch (--pageInfo) {
					case 1:
						msgTitleInfo.setText("Agua");
						infoWindow.add(msgTitleInfo).colspan(3).top().expand();
						infoWindow.row();
						infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
						infoWindow.row().bottom();
						infoWindow.add(tableH2O).colspan(3).top().expand().pad(10);
						break;
					case 2:
						msgTitleInfo.setText("Gas Carbonico");
						infoWindow.add(msgTitleInfo).colspan(3).top().expand();
						infoWindow.row();
						infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
						infoWindow.row().bottom();
						infoWindow.add(tableCO2).colspan(3).top().expand().pad(10);
						break;
					case 3:	
						msgTitleInfo.setText("Oxido Nitroso");
						infoWindow.add(msgTitleInfo).colspan(3).top().expand();
						infoWindow.row();
						infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
						infoWindow.row().bottom();
						infoWindow.add(tableN2O).colspan(3).top().expand().pad(10);
						break;								
				}
				infoWindow.row();
				infoWindow.add(backInfo).bottom().left().expandX().padLeft(10);
				infoWindow.add(closeInfo).bottom().expandX().padLeft(5).padRight(5);
				infoWindow.add(nextInfo).bottom().right().expandX().padRight(10);
			}		   
		});
		// Evento para ir à pagina anterior do catalogo de combinações		
		nextInfo.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {				
				if ((pageInfo+1) > QTD_INFO)
					return;			
				infoWindow.clear();				
				switch (++pageInfo) {
					case 2:	
						msgTitleInfo.setText("Gas Carbonico");
						infoWindow.add(msgTitleInfo).colspan(3).top().expand();
						infoWindow.row();
						infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
						infoWindow.row().bottom();
						infoWindow.add(tableCO2).colspan(3).top().expand().pad(10);						
						break;
					case 3:	
						msgTitleInfo.setText("Oxido Nitroso");
						infoWindow.add(msgTitleInfo).colspan(3).top().expand();
						infoWindow.row();
						infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
						infoWindow.row().bottom();
						infoWindow.add(tableN2O).colspan(3).top().expand().pad(10); 
						break;
					case 4:
						msgTitleInfo.setText("Oxido de Sodio");
						infoWindow.add(msgTitleInfo).colspan(3).top().expand();
						infoWindow.row();
						infoWindow.add(pageInfo+"/"+QTD_INFO).colspan(3).top().expand();
						infoWindow.row().bottom();						
						infoWindow.add(tableNa2O).colspan(3).top().expand().pad(10);					
						break;														
				}
				infoWindow.row();
				infoWindow.add(backInfo).bottom().left().expandX().padLeft(10);
				infoWindow.add(closeInfo).bottom().expandX().padLeft(5).padRight(5);
				infoWindow.add(nextInfo).bottom().right().expandX().padRight(10);
			}		   
		});
	}
	
	/**
	 * Cria as páginas com informações sobre as combinações e sobre os compostos quimicos
	 */
	protected void createInfosIn() {
		tableH2O = new Table(skin);  // 1 	
		tableH2O.add(new Image(elementsT.get(H)));
		tableH2O.add("+", "title");
		tableH2O.add(new Image(elementsT.get(H)));
		tableH2O.add("}", "title");
		tableH2O.add(new Image(elementsT.get(H2)));
		tableH2O.row().pad(5);
		tableH2O.add(new Image(elementsT.get(H2)));
		tableH2O.add("+", "title");
		tableH2O.add(new Image(elementsT.get(O)));
		tableH2O.add("}", "title");
		tableH2O.add(new Image(elementsT.get(H2O)));
		
		tableCO2 = new Table(skin);  // 2
		tableCO2.add(new Image(elementsT.get(O)));
		tableCO2.add("+", "title");
		tableCO2.add(new Image(elementsT.get(O)));
		tableCO2.add("}", "title");
		tableCO2.add(new Image(elementsT.get(O2)));
		tableCO2.row().pad(5);
		tableCO2.add(new Image(elementsT.get(C)));
		tableCO2.add("+", "title");
		tableCO2.add(new Image(elementsT.get(O2)));
		tableCO2.add("}", "title");
		tableCO2.add(new Image(elementsT.get(CO2)));
		
		tableN2O = new Table(skin);  // 3
		tableN2O.add(new Image(elementsT.get(N)));
		tableN2O.add("+", "title");
		tableN2O.add(new Image(elementsT.get(N)));
		tableN2O.add("}", "title");
		tableN2O.add(new Image(elementsT.get(N2)));
		tableN2O.row().pad(5);
		tableN2O.add(new Image(elementsT.get(N2)));
		tableN2O.add("+", "title");
		tableN2O.add(new Image(elementsT.get(O)));
		tableN2O.add("}", "title");
		tableN2O.add(new Image(elementsT.get(N2O)));
		
		tableNa2O = new Table(skin); // 4
		tableNa2O.add(new Image(elementsT.get(Na)));
		tableNa2O.add("+", "title");
		tableNa2O.add(new Image(elementsT.get(Na)));
		tableNa2O.add("}", "title");
		tableNa2O.add(new Image(elementsT.get(Na2)));
		tableNa2O.row().pad(5);
		tableNa2O.add(new Image(elementsT.get(Na2)));
		tableNa2O.add("+", "title");
		tableNa2O.add(new Image(elementsT.get(O)));
		tableNa2O.add("}", "title");
		tableNa2O.add(new Image(elementsT.get(Na2O)));
	}
	
	/**
	 *  Talvez remova esse metodo por conta das classes de cada level...
	 * @param currentLevel
	 */
	public int currentLevel;
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	@Override
	public void render(float delta) {								
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (isInfoScreen) {
			stageInfo.act();
			stageInfo.draw();
			
			return;
		} 
			
		// Segue o fluxo da lógica do jogo
		logic.update();
		
		// Mudando contorno/imagem do item selecionado
		for (int i = 0; i < game.getCells().size; i++) {
			int typeTile = ((Tile) game.getCells().get(i).getActor()).type;
			if (((Tile) game.getCells().get(i).getActor()).activated)  
				((Tile) game.getCells().get(i).getActor()).sprite = new Sprite(elementsTSelected.get(typeTile)); 
			else 
				((Tile) game.getCells().get(i).getActor()).sprite = new Sprite(elementsT.get(typeTile));			
		}														
		
		stage.act();
		stage.draw();

		stateTime += delta;
		
		if (logic.logicBattleState == GAME_IDLE) {
			battleState = HERO_ATTACK;
			heroAnimationFinish = false;
			enemyAnimationFinish = true;			
			logic.logicBattleState = GAME_CONTINUE;
			stateTime = 0;
		} else if (logic.logicBattleState == ENEMY_ATTACK) {
			battleState = ENEMY_ATTACK;
			heroAnimationFinish = true;
			enemyAnimationFinish = false;			
			logic.logicBattleState = GAME_CONTINUE;
			stateTime = 0;
		}
		
		if (logic.gameState == logic.IDLE_STATE && battleState != GAME_END) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			blockRect.begin(ShapeRenderer.ShapeType.Filled);
			blockRect.setColor(new Color(0, 0, 0, 0.5f));
			blockRect.rect(0, 0, parent.windowWidth * PROPORTION_WIDTH_GAME, parent.windowHeight * PROPORTION_HEIGHT_GAME);
			blockRect.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
		
		batch.begin();
		//System.out.println("GAMESTATE: "+battleState);
		this.drawHero();
		//System.out.println("GAMESTATE_HERO: "+battleState);
		this.drawEnemy();
		//System.out.println("GAMESTATE_ENEMY: "+battleState+"\n");
		batch.end();		
		
		if (battleState == GAME_CONTINUE) {
			logic.gameState = logic.PLAYER_STATE;
		} else if (battleState == GAME_WIN || battleState == GAME_LOSE) {			
			if (battleState == GAME_WIN) {					
				// Evita que perca a última fase concluida e extrapole o número total de fases
				if (currentLevel >= parent.getLevelPass() && currentLevel < parent.TOTAL_LEVELS)
					parent.setLevelPass(currentLevel+1);			
				msgTitleEndGame = new Label(":) Bom trabalho!", skin, "xp");		
			} else if (battleState == GAME_LOSE) {				
				msgTitleEndGame = new Label(":( Nao desista!", skin, "err");
			}	
			
			battleState = GAME_END;			
			endGameWindow = new Window("", skin);
			
			endGameWindow.add(msgTitleEndGame).top().center().padBottom(40);
			endGameWindow.row().bottom();				
			endGameWindow.add(replayGameLabel).pad(20);
			endGameWindow.row();
			endGameWindow.add(backMainLabel);
			
			stage.addActor(endGameWindow);
			endGameWindow.setPosition(0, 0);
			endGameWindow.setSize(parent.windowWidth, parent.windowHeight*PROPORTION_HEIGHT_GAME);
		}		
	}
	
	/**
	 * 
	 */
	protected void drawHero() {		
		float x = battle.getCells().get(battle.getCells().size-2).getActorX();
		float y = battle.getCells().get(battle.getCells().size-2).getActorY() + (parent.windowHeight*PROPORTION_HEIGHT_GAME);				
				
		float yHeart = 50/*margem*/ + y + battle.getCells().get(battle.getCells().size-2).getActorHeight();   
		
		if (heroAnimationFinish && battleState != GAME_END) { // hero.idle
			if (lifeHero <= 0)
				batch.draw(hero_die, x, y);
			else
				batch.draw(hero_idle, x, y);			
			
		} else if (battleState == HERO_ATTACK) { // ATACK
			heroAnimationFinish = false;
			if (! heroAttackAnimation.isAnimationFinished(stateTime)) { // Animação herói ainda não terminou
				batch.draw((TextureRegion) heroAttackAnimation.getKeyFrame(stateTime, true), x, y);										
			} else { // Animação herói terminou
				batch.draw((TextureRegion) heroAttackAnimation.getKeyFrame(0, true), x, y);
				battleState = ENEMY_DAMAGE;				
				heroAnimationFinish = true;
				enemyAnimationFinish = false;
				stateTime = 0;
			}
			
		} else if (battleState == HERO_DAMAGE) { // DAMAGE
			if (! heroDamageAnimation.isAnimationFinished(stateTime)) { // Animação herói ainda não terminou
				batch.draw((TextureRegion) heroDamageAnimation.getKeyFrame(stateTime, true), x, y);				
			} else { // Animação herói terminou
				// Reduz uma vida do herói 
				if (--lifeHero <= 0) {
					battleState = HERO_DIE;
					heroAnimationFinish = false;
				} else {
					battleState = GAME_CONTINUE;
					heroAnimationFinish = true;
				}							
				enemyAnimationFinish = true;
				stateTime = 0;
			}	
			
		} else if (battleState == HERO_DIE) { // DIE
			if (! heroDieAnimation.isAnimationFinished(stateTime)) { // Animação herói ainda não terminou
				batch.draw((TextureRegion) heroDieAnimation.getKeyFrame(stateTime, true), x, y);				
			} else { 				
				battleState = GAME_LOSE;								
				heroAnimationFinish = true;
				enemyAnimationFinish = true;
				stateTime = 0;
			}
		}
				
		if (battleState != GAME_END) {
			// Desenhar a vida do herói
			for (int heartFill = 0; heartFill < lifeHero; heartFill++) { // Desenha os corações cheios
				batch.draw(heart, x - heartFill*heart.getRegionWidth(), yHeart);
			}
			for (int heartNFill = LIFE_HERO; heartNFill > lifeHero && heartNFill > 0 ; heartNFill--) { // Desenha os corações vazios
				batch.draw(nonHeart, x - (heartNFill-1)*nonHeart.getRegionWidth(), yHeart);
			}
		} else {
			if (lifeHero > 0)
				batch.draw((TextureRegion) heroWinAnimation.getKeyFrame(stateTime, true), x, y);
			else 
				batch.draw(hero_die, x, y);
		}
	}
	
	/**
	 * 
	 */
	protected void drawEnemy() {
		float x = battle.getCells().get(battle.getCells().size-1).getActorX();
		float y = battle.getCells().get(battle.getCells().size-1).getActorY() + (parent.windowHeight*PROPORTION_HEIGHT_GAME);

		float yHeart = 50/*margem*/ + y + battle.getCells().get(battle.getCells().size-1).getActorHeight();
		
		if (enemyAnimationFinish && battleState != GAME_END) { // enemy.idle
			if (lifeEnemy <= 0)
				batch.draw(enemy_die, x, y);
			else
				batch.draw(enemy_idle, x, y);			
			
		} else if (battleState == ENEMY_ATTACK) { // ATTACK						
			if (! enemyAttackAnimation.isAnimationFinished(stateTime)) { // Animação inimigo ainda não terminou				
				batch.draw((TextureRegion) enemyAttackAnimation.getKeyFrame(stateTime, true), x-20f, y); // Avanço do inimigo e atack								
			} else { 				
				battleState = HERO_DAMAGE;								
				heroAnimationFinish = false;
				enemyAnimationFinish = true;
				stateTime = 0;
			}
			
		} else if (battleState == ENEMY_DAMAGE) { // DAMAGE			
			if (! enemyDamageAnimation.isAnimationFinished(stateTime)) { // Animação inimigo ainda não terminou
				batch.draw((TextureRegion) enemyDamageAnimation.getKeyFrame(stateTime, true), x, y);				
			} else { // Animação inimigo terminou
				// Reduz uma vida do inimigo 
				if (--lifeEnemy <= 0) {
					battleState = ENEMY_DIE;
					enemyAnimationFinish = false;					
				} else {
					battleState = GAME_CONTINUE;
					enemyAnimationFinish = true;
				}
				heroAnimationFinish = true;				
				stateTime = 0;								
			}
			
		} else if (battleState == ENEMY_DIE) { // DIE
			if (! enemyDieAnimation.isAnimationFinished(stateTime)) { // Animação inimigo ainda não terminou
				batch.draw((TextureRegion) enemyDieAnimation.getKeyFrame(stateTime, true), x, y);				
			} else { 				
				battleState = GAME_WIN;								
				heroAnimationFinish = true;
				enemyAnimationFinish = true;
				stateTime = 0;
			}
		}
		
		if (battleState != GAME_END) {
			// Desenhar a vida do inimigo
			for (int heartFill = 0; heartFill < lifeEnemy; heartFill++) { // Desenha os corações cheios
				batch.draw(heart, x + heartFill*heart.getRegionWidth(), yHeart);
			}
			for (int heartNFill = LIFE_ENEMY; heartNFill > lifeEnemy && heartNFill > 0 ; heartNFill--) { // Desenha os corações vazios
				batch.draw(nonHeart, x-1 + (heartNFill-1)*nonHeart.getRegionWidth(), yHeart);
			}
		} else {
			if (lifeEnemy > 0)
				batch.draw(enemy_idle, x, y);
			else
				batch.draw(enemy_die, x, y);								
		}
	}
	
	@Override
	public void resize(int width, int height) {
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
		stage.clear();

	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}

}