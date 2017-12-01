package com.quimic.view;

import java.util.ArrayList;

import javax.print.attribute.standard.Finishings;
import javax.xml.stream.events.EndElement;

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
	
	private final int LIFE_HERO  = 10;
	private final int LIFE_ENEMY = 5;

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
	
	private AtlasRegion  hero_idle; //
	private AtlasRegion  enemy_idle; //
	private AtlasRegion  hero_die; //
	private AtlasRegion  enemy_die; //
	private AtlasRegion  heart; //
	private AtlasRegion  nonHeart; //		
	
	//Animation       heroIdleAnimation;	
	Animation       heroAttackAnimation;
	//Animation       heroHealAnimation;
	Animation       heroDamageAnimation;
	Animation       heroDieAnimation;
	
	//Animation       enemyIdleAnimation;	
	Animation       enemyAttackAnimation;
	//Animation       enemyHealAnimation;
	Animation       enemyDamageAnimation;
	Animation       enemyDieAnimation;
	
	/*TextureRegion[] HeroAttackRegions;	
	TextureRegion[] HeroDamageRegions;
	TextureRegion[] HeroDieRegions;*/
	
	//HeroAnimation heroAnimation;
	boolean heroAnimationFinish  = true;
	boolean enemyAnimationFinish = true;
	int lifeHero;
	int lifeEnemy;	
	SpriteBatch batch;
	public float stateTime;
	
	int battleState = GAME_CONTINUE;

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
		heart      = atlas.findRegion("heart"); // Captura o background da tela do loading
		nonHeart   = atlas.findRegion("heart-bg"); // Captura o background da tela do loading
				
		Array<TextureRegion> regionsT;
		// Animações do hero		
		hero_idle = atlas.findRegion("hero_idle"); // Herói parado 
		// ...
		regionsT = this.addRegionsArray("attack_", 1, 4);
		heroAttackAnimation = new Animation(0.4f, regionsT, PlayMode.NORMAL); // Atack do herói
		// ..
		regionsT = this.addRegionsArray("damage1_", 1, 2);
		heroDamageAnimation = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Dano no herói
		// ...
		regionsT = this.addRegionsArray("die_", 1, 3);
		heroDieAnimation = new Animation(0.7f, regionsT, PlayMode.NORMAL); // Herói morto
		hero_die = atlas.findRegion("hero_die");                           // Herói caido
		
		// Animações do enemy		
		enemy_idle = atlas.findRegion("enemy1_idle"); // Inimigo parado
		// ...
		regionsT = this.addRegionsArray("enemy1_attack1_",1, 5);
		enemyAttackAnimation = new Animation(0.4f, regionsT, PlayMode.NORMAL); // Atack inimigo
		// ..
		regionsT = this.addRegionsArray("enemy1_damage1_", 1, 3);
		enemyDamageAnimation = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Dano no inimigo
		// ...
		regionsT = this.addRegionsArray("enemy1_die_", 1, 3);
		enemyDieAnimation = new Animation(0.7f, regionsT, PlayMode.NORMAL);	// Inimigo morto	
		enemy_die = atlas.findRegion("enemy_die");                          // Inimigo caido
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

	/**
	 * 
	 * @param on
	 */
	private void DebugOnOf(boolean on) {
		battle.setDebug(on);
		view.setDebug(on);
		info.setDebug(on);
		//game.setDebug(on);
	}
			
	@Override
	public void show() {		
		batch = new SpriteBatch();
		stateTime = 0;
		
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		battle = new Table();		
		info   = new Table();
		//info.setDebug(true);
		game   = new Table();				
		//game.setDebug(true);
		view   = new Table();
		view.setFillParent(true);						
							
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
							
		background = atlas.findRegion("background"); // Captura o background da tela do loading	
		fieldBattle = atlas.findRegion("fieldBattle"); // Captura o background da tela do loading		
		
		infoLabel       = new Label("?", skin, "title");	
				
		view.top();
		view.add(battle).expandX().colspan(2);
		view.row().left().bottom();
		view.add(game);
		view.right();				
		info.add(infoLabel);	
		
		// BATTLE
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
		
		
		// Ativando debug das tables (...)
		this.DebugOnOf(true);
	}
	
	@Override
	public void render(float delta) {						
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);									
		
		logic.update();
		
		for (int i = 0; i < game.getCells().size; i++) {								
			if (((Tile) game.getCells().get(i).getActor()).activated)  
				((Tile) game.getCells().get(i).getActor()).setDebug(true);
			else 
				((Tile) game.getCells().get(i).getActor()).setDebug(false);			
		}												
		
		stage.act();
		stage.draw();

//////////////////////////////////////////////////////////////////////////////////////////////////////
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
				
		batch.begin();
		System.out.println("GAMESTATE: "+battleState);
		this.drawHero();
		System.out.println("GAMESTATE_HERO: "+battleState);
		this.drawEnemy();
		System.out.println("GAMESTATE_ENEMY: "+battleState+"\n");
		batch.end();		
		
		if (battleState == GAME_CONTINUE)
			logic.gameState = logic.PLAYER_STATE;			
//////////////////////////////////////////////////////////////////////////////////////////////////////					
				
	}
	
	/**
	 * 
	 */
	private void drawHero() {		
		float x = battle.getCells().get(battle.getCells().size-2).getActorX();
		float y = battle.getCells().get(battle.getCells().size-2).getActorY() + (parent.windowHeight*PROPORTION_HEIGHT_GAME);				
		
		//float xHeart = x - heart.getRegionWidth(); 
		float yHeart = 50/*margem*/ + y + battle.getCells().get(battle.getCells().size-2).getActorHeight();   
		
		if (heroAnimationFinish) { // hero.idle
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
				battleState = GAME_CONTINUE;								
				heroAnimationFinish = true;
				enemyAnimationFinish = true;
				stateTime = 0;
			}
		}
		
		// Desenhar a vida do herói
		for (int heartFill = 0; heartFill < lifeHero; heartFill++) { // Desenha os corações cheios
			batch.draw(heart, x - heartFill*heart.getRegionWidth(), yHeart);
		}
		for (int heartNFill = LIFE_HERO; heartNFill > lifeHero && heartNFill > 0 ; heartNFill--) { // Desenha os corações vazios
			batch.draw(nonHeart, x - (heartNFill-1)*nonHeart.getRegionWidth(), yHeart);
		}
	}
	
	/**
	 * 
	 */
	private void drawEnemy() {
		float x = battle.getCells().get(battle.getCells().size-1).getActorX();
		float y = battle.getCells().get(battle.getCells().size-1).getActorY() + (parent.windowHeight*PROPORTION_HEIGHT_GAME);

		float yHeart = 50/*margem*/ + y + battle.getCells().get(battle.getCells().size-1).getActorHeight();
		
		if (enemyAnimationFinish) { // enemy.idle
			if (lifeEnemy <= 0)
				batch.draw(enemy_die, x, y);
			else
				batch.draw(enemy_idle, x, y);			
			
		} else if (battleState == ENEMY_ATTACK) { // ATTACK						
			if (! enemyAttackAnimation.isAnimationFinished(stateTime)) { // Animação inimigo ainda não terminou
				batch.draw((TextureRegion) enemyAttackAnimation.getKeyFrame(stateTime, true), x, y);								
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
				battleState = GAME_CONTINUE;								
				heroAnimationFinish = true;
				enemyAnimationFinish = true;
				stateTime = 0;
			}
		}
		
		// Desenhar a vida do inimigo
		for (int heartFill = 0; heartFill < lifeEnemy; heartFill++) { // Desenha os corações cheios
			batch.draw(heart, x + heartFill*heart.getRegionWidth(), yHeart);
		}
		for (int heartNFill = LIFE_ENEMY; heartNFill > lifeEnemy && heartNFill > 0 ; heartNFill--) { // Desenha os corações vazios
			batch.draw(nonHeart, x-1 + (heartNFill-1)*nonHeart.getRegionWidth(), yHeart);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();				
	}

}
