package com.quimic.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	private final int LIFE_HERO  = 10;
	private final int LIFE_ENEMY = 5;

	private final int sizeMapW = 6; // Largura da matriz do jogo
	private final int sizeMapH = 6; // Altura da matriz do jogo
	private final int QTD_INFO = 4; // Quantidade de itens para informação/ajuda do jogo
	
	private final int END_TUTORIAL = 12;

//*************************************************************//
	private Animation       animation01;
	private Animation       tutorialPart01;
	private Animation       tutorialPart02;
	private Animation       tutorialPart03;
	private Animation       tutorialPart04;
	private Animation       tutorialPart05;
	private Animation       tutorialPart06;
	private Animation       tutorialPart07;
	private Animation       tutorialPart08;
	private Animation       tutorialPart09;
	private Animation       tutorialPart10;
	private Animation       tutorialPart11;
	
	private int historyPart  = 1;
	private int tutorialPart = 1;	

	float widthTutorial;
	float heightTutorial;
	
	float widthHistory;
	float heightHistory;
	
	private boolean historyScene;
	private boolean tutorialScene;
	
	private Label nextLabel;
	private Label skipLabel;		
	private Label beginTitle;
	private Label historyTitle;
	
	private TextArea textTutorial;
	private TextField TextiHistory;
	
	private Window tutorialWindow; 
	private Table beginTable;	
	
	private TextureRegion currentFrameTutorialHistory;
	
	private Stage noobStage; // Controla e reage às entradas do usuário para a etapa de historia e tutorial	
	
	private String msgWindow;
	
//*************************************************************//

	public Tutorial(QuimiCrush parent) {				
	    super(parent);
		
	    noobStage = new Stage(stage.getViewport()); // Cria o palco para a etapa de história e tutorial
	    
		WIDTH_TILE  = (parent.windowWidth * PROPORTION_WIDTH_GAME) / sizeMapW; // Configura a largura do bloco que contem o componente químico
		HEIGHT_TILE = (parent.windowHeight * PROPORTION_HEIGHT_GAME) / sizeMapH; // Configura a altura do bloco que contem o componente químico
		
		WIDTH_OBJECT_INFO  = (parent.windowWidth * PROPORTION_WIDTH_INFO); // Configura a largura dos blocos de ajuda do jogo
		HEIGHT_OBJECT_INFO = (parent.windowHeight * PROPORTION_HEIGHT_INFO) / (QTD_INFO+4); // Configura a altura dos blocos de ajuda do jogo
		
		tiles = new Tile[sizeMapW][sizeMapH]; // Cria a matriz para as imagens dos blocos químicos quimícos 
		tilesInfo = new Tile[QTD_INFO];
		for (int i = 0; i < QTD_INFO; i++) {
			tilesInfo[i] = new Tile(new Sprite(elementsT.get(H2O+i)), H2O+i, WIDTH_OBJECT_INFO*0.80f, HEIGHT_OBJECT_INFO*0.80f);
		}				 
		
		historyScene  = true;
		tutorialScene = true;				
		
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
		this.DebugOnOf(stage, true);		
		this.DebugOnOf(noobStage, true);
		
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
	 */
	private void loadHistoryAnimations() {
		
	}
	
	/**
	 * 
	 */
	private void loadTutorialAnimations() {
		Array regionsT;		
		regionsT       = this.addRegionsArray("tuto_", 1, 4);
		tutorialPart01 = new Animation(0.4f, regionsT, PlayMode.LOOP);   // Indicar o elemento
		
		regionsT       = this.addRegionsArray("tuto_", 4, 5);
		tutorialPart02 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Clicar no elemento
		
		regionsT       = this.addRegionsArray("tuto_", 5, 8);
		tutorialPart03 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Posicionar no segundo elemento
		
		regionsT       = this.addRegionsArray("tuto_", 8, 10);
		tutorialPart04 = new Animation(0.4f, regionsT, PlayMode.LOOP);   // Indicar o segundo elemento
		
		regionsT       = this.addRegionsArray("tuto_", 10, 11);
		tutorialPart05 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Clicar no segundo elemento
		
		regionsT       = this.addRegionsArray("tuto_", 11, 14);
		tutorialPart06 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Posicionar no terceiro elemento
		
		regionsT       = this.addRegionsArray("tuto_", 15, 17);
		tutorialPart07 = new Animation(0.4f, regionsT, PlayMode.LOOP);   // Indicar o terceiro elemento
		
		regionsT       = this.addRegionsArray("tuto_", 17, 18);
		tutorialPart08 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Clicar no terceiro elemento
		
		regionsT       = this.addRegionsArray("tuto_", 18, 20);
		tutorialPart09 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Posicionar no último elemento
		
		regionsT       = this.addRegionsArray("tuto_", 20, 23);
		tutorialPart10 = new Animation(0.4f, regionsT, PlayMode.LOOP);   // Indicar o último elemento
		
		regionsT       = this.addRegionsArray("tuto_", 23, 24);
		tutorialPart11 = new Animation(0.5f, regionsT, PlayMode.NORMAL); // Gerar um elemento (finalizar a combinação máxima)
	}

	/**
	 * Adiciona os eventos de avançar na historia/tutorial ('> proximo') e 	
	 * de pular historia/tutorial ('>> pular')
	 */
	private void eventsListener() {
		// Adicionando evento para o "botão" de avançar na historia/tutorial				
		nextLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stateTime = 0;
				if (historyScene) {
					
					historyScene = false;
					beginTitle.setText("Tutorial");
					
					beginTable.row();		
					beginTable.add(tutorialWindow).colspan(3).top().center();
				} else if (tutorialScene) {
					tutorialPart++;
					
					if (tutorialPart == 2) {
						msgWindow = "Selecione o componente quimico\n"
								+ "e veja sua imagem mudar";						
					} else if (tutorialPart <= 4) {
						msgWindow = "Busque por outro componente para\n"
								+ "combinacao. O vizinho do lado\n"
								+ "esquerdo, direito, cima ou baixo";						
					} else if (tutorialPart == 5) {
						msgWindow = "Selecione este outro componente\n"
								+ "e veja a combinacao. Mas ainda\n"
								+ "nao sera o suficiente para atacar";
					} else if (tutorialPart <= 7) {
						msgWindow = "Busque pelo ultimo componente da\n"
								+ "combinacao";
					} else if (tutorialPart == 8) {
						msgWindow = "Selecione o componente quimico\n"
								+ "e veja sua imagem mudar";			
					} else if (tutorialPart <= 10) {
						msgWindow = "Busque pelo outro par da ultima\n"
								+ "combinacao";
					} else if (tutorialPart == 11) {
						msgWindow = "Finalmente selecione-o e vera o\n"
								+ "composto quimico combinado se\n"
								+ "transformar em um ataque especial";
					} else if (tutorialPart == 12) {
						msgWindow = "Para visualizar todas as\n"
								+ "combinacoes, olhe o catalogo de\n"
								+ "ataques ao lado direito da arena";
					} else if (tutorialPart > END_TUTORIAL) {
						stateTime = 0;
						tutorialScene = false;
						Gdx.input.setInputProcessor(stage);
					}
					tutorialWindow.clear();
					tutorialWindow.add(msgWindow);
				}				
			}		   
		});
		
		// Adicionando evento para o "botão" de pular historia/tutorial
		skipLabel.addListener(new ClickListener() {			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stateTime = 0;
				if (historyScene) {
					
					historyScene = false;			
					beginTitle.setText("Tutorial");	
					
					beginTable.row();		
					beginTable.add(tutorialWindow).colspan(3).top().center();
				} else if (tutorialScene) {
					tutorialScene = false;
					Gdx.input.setInputProcessor(stage);
				}		
				
			}		   
		});
	}			
	
	/**
	 * 
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
		
		noobStage.clear();
		Gdx.input.setInputProcessor(noobStage);			
		
		// Criação das tiles -> os componentes químicos
		for (int i = 0; i < sizeMapW; i++) {
			for (int j = 0; j < sizeMapH; j++) {
				int type = MathUtils.random(0,4);
				Tile newTile = new Tile(new Sprite(elementsT.get(type)), type, WIDTH_TILE, HEIGHT_TILE);				
	            tiles[i][j] = newTile;	            
		    }
		}
				
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
		for (int i = 0; i < QTD_INFO; i++) {
			info.row().padBottom(10f);
			info.add(tilesInfo[i]);			
		}									
		
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
		
		if (!(historyScene || tutorialScene)) 
			super.render(delta);
		else {
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
	
	/**
	 * 
	 */
	private void drawTutorial() {			
		switch (tutorialPart) {
			case  1: 
				currentFrameTutorialHistory = (TextureRegion) tutorialPart01.getKeyFrame(stateTime, true);				
				break;
			case  2:
				if (!tutorialPart02.isAnimationFinished(stateTime)) {
					currentFrameTutorialHistory = (TextureRegion) tutorialPart02.getKeyFrame(stateTime, true);
				}
				break;
			case  3: 
				if (!tutorialPart03.isAnimationFinished(stateTime)) {
					currentFrameTutorialHistory = (TextureRegion) tutorialPart03.getKeyFrame(stateTime, true);			
				} else 					
					tutorialPart++; 
				break;
			case  4: 
				currentFrameTutorialHistory = (TextureRegion) tutorialPart04.getKeyFrame(stateTime, true);			
				break;
			case  5: 
				if (!tutorialPart05.isAnimationFinished(stateTime)) {
					currentFrameTutorialHistory = (TextureRegion) tutorialPart05.getKeyFrame(stateTime, true);
				}
				break;
			case  6: 				
				if (!tutorialPart06.isAnimationFinished(stateTime)) { 
					currentFrameTutorialHistory = (TextureRegion) tutorialPart06.getKeyFrame(stateTime, true);			
				} else 					
					tutorialPart++;
				break;
			case  7: 
				currentFrameTutorialHistory = (TextureRegion) tutorialPart07.getKeyFrame(stateTime, true);			
				break;
			case  8: 
				if (!tutorialPart08.isAnimationFinished(stateTime)) { 
					currentFrameTutorialHistory = (TextureRegion) tutorialPart08.getKeyFrame(stateTime, true);			
				}
				break;
			case  9:
				if (!tutorialPart09.isAnimationFinished(stateTime)) {
					currentFrameTutorialHistory = (TextureRegion) tutorialPart09.getKeyFrame(stateTime, true);			
				} else 					
					tutorialPart++;
				break;
			case 10: 
				currentFrameTutorialHistory = (TextureRegion) tutorialPart10.getKeyFrame(stateTime, true);			
				break;
			case 11: 
			case 12:
				if (!tutorialPart11.isAnimationFinished(stateTime)) {
					currentFrameTutorialHistory = (TextureRegion) tutorialPart11.getKeyFrame(stateTime, true);
				}
				break;
		}
			
		batch.draw(currentFrameTutorialHistory, 0, 0, widthTutorial, heightTutorial);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		noobStage.dispose();
		this.dispose();		
	}
	
}