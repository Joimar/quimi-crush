package com.quimic.levels;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;
import com.quimic.view.GameScreen;

public class Level1 extends GameScreen {
	// Atributos do SUPER
    
//*************************************************************//   
	private final int sizeMapW = 6; // Largura da matriz do jogo
	private final int sizeMapH = 6; // Altura da matriz do jogo

//*************************************************************//
	private Animation       animation01;	
	
//*************************************************************//

	public Level1(QuimiCrush parent) {				
	    super(parent);
		
	    // Configura as vidas do heroi e vilão, respectivamente
	    LIFE_HERO = 8;
		LIFE_ENEMY = 4;	  
		// Configura a quantidade de elementos no catalogo
		QTD_INFO = 4;
	    
		// Configura a largura e altura do bloco quimico 
		WIDTH_TILE  = (parent.windowWidth * PROPORTION_WIDTH_GAME) / sizeMapW; // Configura a largura do bloco que contem o componente químico
		HEIGHT_TILE = (parent.windowHeight * PROPORTION_HEIGHT_GAME) / sizeMapH; // Configura a altura do bloco que contem o componente químico

		// Configura a largura e altura dos itens na area de informações 
		WIDTH_OBJECT_INFO  = (parent.windowWidth * PROPORTION_WIDTH_INFO); // Configura a largura dos blocos de ajuda do jogo
		HEIGHT_OBJECT_INFO = (parent.windowHeight * PROPORTION_HEIGHT_INFO) / (QTD_INFO+4); // Configura a altura dos blocos de ajuda do jogo
		
		// Cria os objetos que contém as informações sobre os blocos do jogo, tanto os de ajuda quanto os para combinação
		tiles = new Tile[sizeMapW][sizeMapH]; // Cria a matriz para as imagens dos blocos químicos quimícos 
		/*tilesInfo = new Tile[QTD_INFO];
		for (int i = 0; i < QTD_INFO; i++) { // Insere as informações nos objetos dos itens de ajuda
			tilesInfo[i] = new Tile(new Sprite(elementsT.get(H2O+i)), H2O+i, WIDTH_OBJECT_INFO*0.80f, HEIGHT_OBJECT_INFO*0.80f);
		}*/				 
						
		// Carrega animações
		this.loadAnimations(); // Carrega as animações do jogo							
		
		// Ativando debug do palco
		this.DebugOnOf(stage, false);				
	}
		
	/**
	 * Carrega as imagens e animações para a batalha no jogo	
	 */
	private void loadAnimations() {
		heart      = atlas.findRegion("heart");    // Captura a imagem de coração cheio
		nonHeart   = atlas.findRegion("heart-bg"); // Captura a imagem de coração vazio		
				
		Array<TextureRegion> regionsT;
		// Animações do hero		
		hero_idle = atlas.findRegion("hero_idle"); // Herói parado 
		// ...
		regionsT = this.addRegionsArray("attack_", 1, 4);
		heroAttackAnimation = new Animation(0.2f, regionsT, PlayMode.NORMAL); // Atack do herói
		// ..
		regionsT = this.addRegionsArray("damage1_", 1, 2);
		heroDamageAnimation = new Animation(0.3f, regionsT, PlayMode.NORMAL); // Dano no herói
		// ...
		regionsT = this.addRegionsArray("die_", 1, 3);
		heroDieAnimation = new Animation(0.6f, regionsT, PlayMode.NORMAL); // Herói morto
		hero_die = atlas.findRegion("hero_die");                           // Herói caido
		// ...
		regionsT = this.addRegionsArray("hero_win_", 1, 2); 
		heroWinAnimation = new Animation(0.4f, regionsT, PlayMode.LOOP); // Pose da vitória
		
		// Animações do enemy		
		enemy_idle = atlas.findRegion("enemy1_idle"); // Inimigo parado
		// ...
		regionsT = this.addRegionsArray("enemy1_attack1_",1, 5);
		enemyAttackAnimation = new Animation(0.2f, regionsT, PlayMode.NORMAL); // Atack inimigo
		// ..
		regionsT = this.addRegionsArray("enemy1_damage1_", 1, 3);
		enemyDamageAnimation = new Animation(0.3f, regionsT, PlayMode.NORMAL); // Dano no inimigo
		// ...
		regionsT = this.addRegionsArray("enemy1_die_", 1, 3);
		enemyDieAnimation = new Animation(0.6f, regionsT, PlayMode.NORMAL);	// Inimigo morto	
		enemy_die = atlas.findRegion("enemy_die");                          // Inimigo caido
	}
	
	/**
	 * Responsável por atribuir os eventos para cada bloco na área do jogo (os blocos quimicos das combinações)
	 */
	public void tilesListener() {
		for (int i = 0; i < game.getCells().size; i++) {
			game.getCells().get(i).getActor().addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// Simplificando o acesso aos itens
					Tile previousTile = (Tile) game.getCells().get(logic.activeXY).getActor();
					Tile currentTile = (Tile) event.getListenerActor();

					// Realiza os eventos do click apenas no estado de jogo jogavel
					if (logic.gameState != logic.PLAYER_STATE) 
						return;
					
					// Verificando por combinações de elementos
					if (logic.tileIsActive) {						
						if (logic.tileIsActive) {							
							// Verifica se o segundo item clicado foi do lado ESQUERDO
							if ((logic.activeXY % sizeMapW) != 0 && ((logic.activeXY - 1) == game.getCells().indexOf(game.getCell(event.getListenerActor()), true))) {
								if ((((Tile) game.getCells().get(logic.activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {
									
									logic.tileIsActive = false;
									previousTile.activated = false;

									logic.combineTile(previousTile, currentTile);
								}
							}
						}
						// Verifica se o segundo item clicado foi do lado DIREITO
						if (logic.tileIsActive) {
							if (((logic.activeXY + 1) % sizeMapW) != 0 && ((logic.activeXY + 1) == game.getCells().indexOf(game.getCell(event.getListenerActor()), true))) {
								if ((((Tile) game.getCells().get(logic.activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {

									logic.tileIsActive = false;
									previousTile.activated = false;

									logic.combineTile(previousTile, currentTile);
								}
							}
						}
						// Verifica se o segundo item clicado foi em CIMA
						if (logic.tileIsActive) {
							if ((logic.activeXY - sizeMapW) == game.getCells().indexOf(game.getCell(event.getListenerActor()), true)) {
								if ((((Tile) game.getCells().get(logic.activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {
									
									logic.tileIsActive = false;
									previousTile.activated = false;

									logic.combineTile(previousTile, currentTile);
								}
							}
						}
						// Verifica se o segundo item clicado foi em BAIXO
						if (logic.tileIsActive) {
							if ((logic.activeXY + sizeMapW) == game.getCells().indexOf(game.getCell(event.getListenerActor()), true)) {
								if ((((Tile) game.getCells().get(logic.activeXY).getActor()).type != 100) && (((Tile) event.getListenerActor()).type != 100)) {

									logic.tileIsActive = false;
									previousTile.activated = false;

									logic.combineTile(previousTile, currentTile);
								}
							}
						}

						// Check for Matches
						logic.gameState = logic.MATCH_STATE;
					}

					// Selecionando um bloco químico
					//if (!logic.secondTap) {
					if ( !(logic.combined || logic.matched) ) {
						if (!currentTile.destroy) { // if (currentTile.type != 100) {

							previousTile.activated = false; // Desselecionando (desativando) o tile anterior
							currentTile.activated = true; // Selecionando (ativando) o tile atual 
							
							logic.tileIsActive = true;
							logic.activeXY = game.getCells().indexOf(game.getCell(event.getListenerActor()), true);

							System.out.println("Activated: " + logic.activeXY + " ");

						}
					}
				}
			});
		}
	}

	@Override
	public void show() {
		super.show();		
		infoGamePanelAction();
		
		// Criação das tiles -> os componentes químicos
		for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				int type;
				/*if (((i+i)*(j+1))%(i+j+MathUtils.random(0,9)) == 0)
					type = O;
				else */
					type = MathUtils.random(0,4);
				Tile newTile = new Tile(new Sprite(elementsT.get(type)), type, WIDTH_TILE, HEIGHT_TILE);				
	            tiles[i][j] = newTile;	            
		    }
		}
				
		// Responsável pela lógica de combinações e matches do jogo
		logic = new Logic(parent, tiles, elementsT, cam, game, sizeMapW, sizeMapH); // Logica do jogo			
		
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
		/*for (int i = 0; i < QTD_INFO; i++) {
			info.row().padBottom(10f);
			info.add(tilesInfo[i]);			
		}	*/								
				
		this.tilesListener(); // Adiciona os eventos de click nos blocos quimicos a partir da logica (logic)		
	}

	@Override
	public void render(float delta) {
		//long time = System.currentTimeMillis();
		
		super.render(delta);			
		
		//System.out.println("Tempo Loop: "+(System.currentTimeMillis()-time));
	}

	@Override
	public void dispose() {
		super.dispose();
		this.dispose();		
	}
	
}