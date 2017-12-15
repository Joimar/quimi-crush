package com.quimic.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.quimic.game.QuimiCrush;
import com.quimic.logic.Logic;
import com.quimic.tile.Tile;
import com.quimic.view.GameScreen;

public class Tutorial extends GameScreen {
	// Atributos do SUPER
    
//*************************************************************//   
	private final int sizeMapW = 4; // Largura da matriz do jogo
	private final int sizeMapH = 4; // Altura da matriz do jogo
	
	private final int END_TUTORIAL = 10;

//*************************************************************//
	private Animation       animation01;
	
	private AtlasRegion seta; // Mão com o dedo apontando -> utilizado para o tutorial
	private float xSeta, ySeta, offsetSeta;
	
	private int historyPart  = 0;
	
	private int tutorialPart;			
	private int dialogPart;
	private boolean wait = false;
	
	private boolean historyScene;
	private boolean tutorialScene;
	
	float widthTutorial;
	float heightTutorial;
	
	float widthHistory;
	float heightHistory;		
	
	private Label nextLabel;
	private Label skipLabel;		
	private Label beginTitle;
	private Label historyTitle;
	
	private TextArea textTutorial;
	private TextField TextHistory;
	
	private Window tutorialWindow; 
	private Table beginTable;	
	
	private TextureRegion currentFrameHistory;
	
	private Stage noobStage; // Controla e reage às entradas do usuário para a etapa de historia e tutorial	
	
	private String msgWindow;
	
//*************************************************************//

	
//*************************************************************//	

	public Tutorial(QuimiCrush parent) {				
	    super(parent);

	    // Configura as vidas do heroi e vilão, respectivamente
		LIFE_HERO = 2;
		LIFE_ENEMY = 1;
	    
	    noobStage = new Stage(stage.getViewport()); // Cria o palco para a etapa de história e tutorial
	    
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
		
		// Permitindo a aparição do tutorial e da historia no inicio da fase
		historyScene  = true;
		tutorialScene = true;				
		// Sequência de aparição das etapas do tutorial
		tutorialPart = 0;			
		dialogPart   = 0;
		
		// Configura largura e altura da tela de tutorial
		widthTutorial  = parent.windowWidth;
		heightTutorial = parent.windowHeight * PROPORTION_HEIGHT_GAME;
		
		// Carrega animações
		this.loadAnimations(); // Carrega as animações do jogo		
		this.loadHistoryAnimations(); // Carrega animações da história
		this.loadTutorialAnimations(); // Carrega animações do tutorial						
		
		nextLabel = new Label("> Proximo", skin, "dim"); // Cria link para a proxima tela de historia/tutorial
		skipLabel = new Label(">> Pular", skin, "dim");	// Cria link para pular para o fim da historia/tutorial										
		this.eventsListener(); // Insere os eventos de click aos labels de 'next' e 'skip'
		
		// Ativando debug do palco
		this.DebugOnOf(stage, false);		
		this.DebugOnOf(noobStage, false);
		
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
	 * Carrega as imagens e animações da história do jogo
	 */
	private void loadHistoryAnimations() {
		
	}
	
	/**
	 * Carrega as imagens e animações do tutorial
	 */
	private void loadTutorialAnimations() {
		seta = atlas.findRegion("dedoTutorial"); // Captura a imagem da mão com o dedo					
	}

	/**
	 * Adiciona os eventos de avançar na historia/tutorial ('> proximo') e 	
	 * de pular historia ('>> pular')
	 */
	private void eventsListener() {
		// Adicionando evento para o "botão" de avançar na historia				
		nextLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {									
				historyPart++;
				// TODO história
				if (historyPart > 2) { // EM DESENVOLVIMENTO					
					historyScene = false;	
					stateTime = 0;					
					Gdx.input.setInputProcessor(stage);
				}
								
			}		   
		});
		
		// Adicionando evento para o "botão" de pular historia
		skipLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stateTime = 0;		
				historyScene = false;											
				Gdx.input.setInputProcessor(stage);			
			}		   
		});
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
									if (logic.combined)
										tutorialPart++;	
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
									if (logic.matched)
										tutorialPart++;										
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

							// Ativando etapas do tutorial
							if (logic.activeXY == 5 && tutorialPart == 4) {
								wait = false;
								tutorialPart++; // 5
							} else if (logic.activeXY == 4 && tutorialPart == 8) {
								wait = false;
								tutorialPart++; // 8
							} else if (logic.activeXY == 4 && (tutorialPart <= 7 || tutorialPart > 10)) { // Não está seguindo o tutorial
								logic.tileIsActive = false;
								previousTile.activated = false; // Não permite sair da sequência do tutorial
								currentTile.activated = false; // Não permite sair da sequência do tutorial
							} else if (logic.activeXY == 8) { // Não está seguindo o tutorial
								logic.tileIsActive = false;
								previousTile.activated = false; // Não permite sair da sequência do tutorial
								currentTile.activated = false; // Não permite sair da sequência do tutorial
							}
														

						}
					}
				}
			});
		}
	}

	@Override
	public void show() {
		super.show();
		//infoGamePanelAction();
		
		noobStage.clear();
		Gdx.input.setInputProcessor(noobStage);			
		
		// Criação das tiles -> os componentes químicos -> Para a fa se tutorial predefinida
		Tile newTile = 				
        tiles[0][0] = new Tile(new Sprite(elementsT.get(C)), C, WIDTH_TILE, HEIGHT_TILE);
		tiles[0][1] = new Tile(new Sprite(elementsT.get(C)), C, WIDTH_TILE, HEIGHT_TILE);
		tiles[0][2] = new Tile(new Sprite(elementsT.get(C)), C, WIDTH_TILE, HEIGHT_TILE);
		tiles[0][3] = new Tile(new Sprite(elementsT.get(O)), O, WIDTH_TILE, HEIGHT_TILE);
		tiles[1][0] = new Tile(new Sprite(elementsT.get(H)), H, WIDTH_TILE, HEIGHT_TILE);
		tiles[1][1] = new Tile(new Sprite(elementsT.get(H)), H, WIDTH_TILE, HEIGHT_TILE);
		tiles[1][2] = new Tile(new Sprite(elementsT.get(N)), N, WIDTH_TILE, HEIGHT_TILE);
		tiles[1][3] = new Tile(new Sprite(elementsT.get(Na)), Na, WIDTH_TILE, HEIGHT_TILE);
		tiles[2][0] = new Tile(new Sprite(elementsT.get(O)), O, WIDTH_TILE, HEIGHT_TILE);
		tiles[2][1] = new Tile(new Sprite(elementsT.get(C)), C, WIDTH_TILE, HEIGHT_TILE);
		tiles[2][2] = new Tile(new Sprite(elementsT.get(O)), O, WIDTH_TILE, HEIGHT_TILE);
		tiles[2][3] = new Tile(new Sprite(elementsT.get(N)), N, WIDTH_TILE, HEIGHT_TILE);
		tiles[3][0] = new Tile(new Sprite(elementsT.get(Na)), Na, WIDTH_TILE, HEIGHT_TILE);
		tiles[3][1] = new Tile(new Sprite(elementsT.get(N)), N, WIDTH_TILE, HEIGHT_TILE);
		tiles[3][2] = new Tile(new Sprite(elementsT.get(H)), H, WIDTH_TILE, HEIGHT_TILE);
		tiles[3][3] = new Tile(new Sprite(elementsT.get(Na)), Na, WIDTH_TILE, HEIGHT_TILE);
			        		
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
		
		// História e tutorial
		beginTable = new Table(skin); // Criação da tabela para a história/tutorial 
		beginTable.setFillParent(true);		
		beginTitle = new Label("Historia", skin, "bold"); 		
		
		tutorialWindow = new Window("", skin); // Criação de uma janela para inserir informações		
		tutorialWindow.align(Align.center);

		// Mensagem inicial para a janela
		msgWindow = "Oi, este tutorial vai lhe mostrar\n"
				+ "como o jogo funciona. Primeiro en-\n"
				+ "contre um componente qualquer";
		tutorialWindow.add(msgWindow);
		
		beginTable.top();
		beginTable.add(skipLabel).top().left().pad(5, 5, 5, 0).expandX();
		beginTable.add(beginTitle).top().center().expandX();;
		beginTable.add(nextLabel).top().right().pad(5, 0, 5, 5).expandX();		
			
		noobStage.addActor(beginTable);		
		
		this.tilesListener(); // Adiciona os eventos de click nos blocos quimicos a partir da logica (logic)						
	}
	
	@Override
	public void render(float delta) {
		//long time = System.currentTimeMillis();
		
		if (! historyScene) { 
			//Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			
			//Gdx.gl.glDisable(GL20.GL_BLEND);
			
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
														
			if (! wait)
				this.drawDialogTutorial();
			this.drawTutorial();
			
			int lifeAux = lifeHero;
					
			if (dialogPart != 1) {
				batch.begin();
				drawHero();			
				drawEnemy();
				batch.end();
			}				
			
			if (lifeAux > lifeHero)
				wait = false;
			
			// Casos para o jogo continuar ou quando finalizado
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
			
		} else {
			Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);						
			
			stateTime += delta;			
			
			batch.begin();
			if (historyScene) {
				
			} else if (tutorialScene) {				
				this.drawTutorial();			
			}
			batch.end();
			
			noobStage.act();
			noobStage.draw();
		}

		//System.out.println("Tempo Loop: "+(System.currentTimeMillis()-time));
	}

	/**
	 * 
	 */
	private void drawHistory() {	
		// TODO a historia
	}

	private void drawDialogTutorial() {
		float x, y, w, h;
		wait = true;
		switch (dialogPart) {					
			case  0: // Mostrando a região do jogo /game 				
				w = (parent.windowWidth*PROPORTION_WIDTH_GAME)*0.7f; 
				h = (parent.windowHeight*PROPORTION_HEIGHT_GAME)*0.7f;
				x = ((parent.windowWidth*PROPORTION_WIDTH_GAME) - w) / 2;
				y = ((parent.windowHeight*PROPORTION_HEIGHT_GAME) - h) / 2;
				
				new Dialog("", skin) {
					{
						text("Nesta regiao\n combine os\n elementos");
						button("continue", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						dialogPart++; // 1
						tutorialPart++; // 1
						wait = false;
					}
				}.show(stage).setBounds(x, y, w, h);									
				
				break;
			case 1: // Mostrando a região da batalha										
				w = (parent.windowWidth*PROPORTION_WIDTH_BATTLE)*0.7f; 
				h = (parent.windowHeight*PROPORTION_HEIGHT_BATTLE)*0.7f;
				x = ((parent.windowWidth*PROPORTION_WIDTH_BATTLE) - w) / 2;
				y = (((parent.windowHeight*PROPORTION_HEIGHT_BATTLE) - h) / 2) + parent.windowHeight*PROPORTION_HEIGHT_GAME;
				
				new Dialog("", skin) {
					{
						text("Campo de batalha");						
						button("continue", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						dialogPart++; // 2
						tutorialPart++; // 2
						wait = false;
					}
				}.show(stage).setBounds(x, y, w, h);									
				
				break;				
			case 2: // Mostrando a região do catalogo de informações de combinações						
				w = (parent.windowWidth*PROPORTION_WIDTH_INFO); 
				h = (parent.windowHeight*PROPORTION_HEIGHT_INFO)*0.5f;
				x = ((parent.windowWidth*PROPORTION_WIDTH_INFO) - w) + parent.windowWidth*PROPORTION_WIDTH_GAME; 
				y = ((parent.windowHeight*PROPORTION_HEIGHT_INFO) - h) / 2;
				
				new Dialog("", skin) {
					{
						text("Info");
						button("vai", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						dialogPart++; // 3
						tutorialPart++; // 3
						wait = false;
					}
				}.show(stage).setBounds(x, y, w, h);	
								
				break;
			case 3: // Informando sobre selecionar um componente
				/*w = (parent.windowWidth*PROPORTION_WIDTH_GAME)*0.7f; 
				h = (parent.windowHeight*PROPORTION_HEIGHT_GAME)*0.7f;
				x = ((parent.windowWidth*PROPORTION_WIDTH_GAME) - w) / 2;
				y = ((parent.windowHeight*PROPORTION_HEIGHT_GAME) - h) / 2;*/
				
				new Dialog("", skin) {
					{
						text("Selecione o\ncomponente");
						button("continue", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						dialogPart++; // 4
						tutorialPart++; // 4 						
					}
				}.show(stage);//.setBounds(x, y, w, h);
				
				break;
			case 4: // Informando sobre selecionar outro componente para combinação simples (combine)
				/*w = (parent.windowWidth*PROPORTION_WIDTH_GAME)*0.7f; 
				h = (parent.windowHeight*PROPORTION_HEIGHT_GAME)*0.7f;
				x = ((parent.windowWidth*PROPORTION_WIDTH_GAME) - w) / 2;
				y = ((parent.windowHeight*PROPORTION_HEIGHT_GAME) - h) / 2;*/
				
				new Dialog("", skin) {
					{
						text("Selecione outro\ncomponente");
						button("continue", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						new Dialog("", skin) {
							{
								text("Uma combinacao simples\nfara seu heroi levar dano");
								button("continue", "");
								this.setMovable(false);
							}
							
							@Override
							protected void result(final Object object) {
								dialogPart++; // 5
								tutorialPart++; // 6
							}
						}.show(stage);
					}
				}.show(stage);//.setBounds(x, y, w, h);
				
				break;
			case 5: // Informando sobre selecionar o componente combinando anteriormente 
				new Dialog("", skin) {
					{
						text("Selecione o\ncomponente combinado");
						button("continue", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						dialogPart++; // 6
						tutorialPart++; // 8 						
					}
				}.show(stage);
				
				break;
			case 6: // Informando sobre selecionar o último componente para combinação master (match)			
				new Dialog("", skin) {
					{
						text("Selecione o ultimo\ncomponente");
						button("continue", "");
						this.setMovable(false);
					}
					
					@Override
					protected void result(final Object object) {
						new Dialog("", skin) {
							{
								text("Ao formar um oxido\nseu heroi ataca");
								button("continue", "");
								this.setMovable(false);
							}
							
							@Override
							protected void result(final Object object) {
								dialogPart++; // 7
								tutorialPart++; // 9  						
							}
						}.show(stage);
					}
				}.show(stage);
					
				break;
		}				
	}
	
	/**
	 * 
	 */
	private void drawTutorial() {			
		switch (tutorialPart) {
			case  0: // Mostrando a região do jogo /game 
				Gdx.gl.glEnable(GL20.GL_BLEND);
				blockRect.begin(ShapeRenderer.ShapeType.Filled);
				blockRect.setColor(new Color(0, 0, 0, 0.6f));
				blockRect.rect(0, parent.windowHeight * PROPORTION_HEIGHT_GAME, parent.windowWidth * PROPORTION_WIDTH_BATTLE, parent.windowHeight * PROPORTION_HEIGHT_BATTLE);
				blockRect.rect(parent.windowWidth* PROPORTION_WIDTH_GAME, 0, parent.windowWidth * PROPORTION_WIDTH_INFO, parent.windowHeight * PROPORTION_HEIGHT_INFO);
				blockRect.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				break;
			case 1: // Mostrando a região da batalha
				Gdx.gl.glEnable(GL20.GL_BLEND);
				blockRect.begin(ShapeRenderer.ShapeType.Filled);
				blockRect.setColor(new Color(0, 0, 0, 0.6f));
				blockRect.rect(0, 0, parent.windowWidth * PROPORTION_WIDTH_GAME, parent.windowHeight * PROPORTION_HEIGHT_GAME);
				blockRect.rect(parent.windowWidth* PROPORTION_WIDTH_GAME, 0, parent.windowWidth * PROPORTION_WIDTH_INFO, parent.windowHeight * PROPORTION_HEIGHT_INFO);
				blockRect.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				break;
			case  2:  // Mostrando a região do catalogo de informações de combinações
				Gdx.gl.glEnable(GL20.GL_BLEND);
				blockRect.begin(ShapeRenderer.ShapeType.Filled);
				blockRect.setColor(new Color(0, 0, 0, 0.6f));
				blockRect.rect(0, parent.windowHeight * PROPORTION_HEIGHT_GAME, parent.windowWidth * PROPORTION_WIDTH_BATTLE, parent.windowHeight * PROPORTION_HEIGHT_BATTLE);
				blockRect.rect(0, 0, parent.windowWidth * PROPORTION_WIDTH_GAME, parent.windowHeight * PROPORTION_HEIGHT_GAME);
				blockRect.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				break;
			case 3: break;
			case 4: // Seta no elemento L(5) = M(1, 1) da tabela = H
				Gdx.gl.glEnable(GL20.GL_BLEND);
				xSeta = game.getCells().get(5).getActorX();
				ySeta = game.getCells().get(5).getActorY();
				offsetSeta = (WIDTH_TILE*0.45f);
				batch.begin();
				batch.draw(seta, xSeta + offsetSeta, ySeta, WIDTH_TILE*0.6f, HEIGHT_TILE*0.6f);
				batch.end();		
				Gdx.gl.glDisable(GL20.GL_BLEND);
				break;
			case 5: break;
			case 6: // Seta no elemento L(4) = M(1, 0) da tabela = H				
				xSeta = game.getCells().get(4).getActorX();
				ySeta = game.getCells().get(4).getActorY();
				offsetSeta = (WIDTH_TILE*0.45f);
				batch.begin();
				batch.draw(seta, xSeta + offsetSeta, ySeta, WIDTH_TILE*0.6f, HEIGHT_TILE*0.6f);
				batch.end();
				break;
			case 7: break;
			case 8: // Seta no elemento combinado L(4) = M(1, 0) da tabela = H2
				xSeta = game.getCells().get(4).getActorX();
				ySeta = game.getCells().get(4).getActorY();
				offsetSeta = (WIDTH_TILE*0.45f);
				batch.begin();
				batch.draw(seta, xSeta + offsetSeta, ySeta, WIDTH_TILE*0.6f, HEIGHT_TILE*0.6f);
				batch.end();				
				break;
			case 9: break;
			case 10: // Seta no elemento L(8) = M(2, 0) da tabela = O
				xSeta = game.getCells().get(8).getActorX();
				ySeta = game.getCells().get(8).getActorY();
				offsetSeta = (WIDTH_TILE*0.45f);
				batch.begin();
				batch.draw(seta, xSeta + offsetSeta, ySeta, WIDTH_TILE*0.6f, HEIGHT_TILE*0.6f);
				batch.end();				
				
				break;
		}			
	}

	
	@Override
	public void dispose() {
		super.dispose();
		noobStage.dispose();
		this.dispose();		
	}
	
}