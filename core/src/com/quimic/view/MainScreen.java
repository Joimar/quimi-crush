package com.quimic.view;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.game.QuimiCrush;

public class MainScreen implements Screen {
	private final float PROPORTION_HEIGTH_MENU = 0.15f; // Porpor��o do grid do topMenu	
	
	private QuimiCrush parent; // Quem orquestra tudo
	
	private Stage          stage;  // Controla e reage �s entradas do usu�rio
	private ScreenViewport sv;     // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un	
	private Table 		   view;   // Grid para os outros menus;      
	private Table          levels; // Grid dos n�veis do jogo
	private Table          menu;   // Grid do menu da tela inicial
	private Skin           skin;   // Estilo dos bot�es	
	private ScrollPane     scrollLevels; // Responsav�l pela barra de rolagem na tela para visualizar as fases
	
//*************************************************************//	
	private TextButton preferences;
	private ArrayList<TextButton> gameLevels;
	
//*************************************************************//
	private TextureAtlas atlas; // Empacotamento das imagens
	private AtlasRegion  background;
	
//*************************************************************//	
	/**
	 * 
	 * @param parent
	 */
	public MainScreen(QuimiCrush parent) {		
		this.parent = parent;
		sv = new ScreenViewport();
		stage = new Stage(sv);
		
	//	parent.assetsManager.queueAddSkin(); // Carrega as skins  
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das skins		
		this.loadAssets();
	}
	
	/**
	 * 
	 */
	private void loadAssets() {
		// get images used to display loading progress
		atlas = parent.assetsManager.MANAGER.get(parent.assetsManager.LOADING_IMAGES);
		skin = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN); // Recupera a skin				
		background = atlas.findRegion("background");
	}

	/**
	 * 
	 */
	private void buttonsListener() {						
		// Adicionando evento para o bot�o de ajustes
		preferences.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(QuimiCrush.PREFERENCES);
			}
		});
		
		// Adicionando evento para os bot�es de fase
		for (int l = 0; l < parent.TOTAL_LEVELS; l++) {						
			gameLevels.get(l).addListener(new ChangeListener() {							
				@Override
				public void changed(ChangeEvent event, Actor actor) {					
					int indexLevel = gameLevels.lastIndexOf(actor);					
					parent.changeLevel(indexLevel);					
				}
			});
		}		
	}
		
	@Override
	public void show() {
		// Limpa o palco para uma novo controle
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		// Cria o grid para inserir os outros grids de menu do jogo
		view = new Table(); // Cria a tabela
		view.setFillParent(true);
		view.setBackground(new TiledDrawable(background));
		//view.setDebug(true); // linhas de depura��o na tela
		// Cria o grid para inserir as op��es e ajustes do jogo
		menu = new Table();
		menu.setSize(parent.windowWidth, parent.windowHeight * PROPORTION_HEIGTH_MENU);
		// Cria o grid para inser��o das fases do jogo
		levels = new Table();		
		scrollLevels = new ScrollPane(levels, skin);
		
		view.top();
		view.add(menu).expandX().pad(10, 0, 10, 0).right();
		view.row().pad(10, 0, 25, 0);;
		view.add(scrollLevels);
		stage.addActor(view); // Adiciona a tabela no stage	
				
		// Organiza bot�es de ajustes e op��es
		preferences = new TextButton("Ajustes", skin);		
		menu.add(preferences);
			
		// Organiza bot�es das fases
		gameLevels = new ArrayList<TextButton>(parent.TOTAL_LEVELS);				
		gameLevels.add(0, new TextButton("Tutorial teste", skin));
		levels.add(gameLevels.get(0));
		for (int l = 1; l < parent.TOTAL_LEVELS; l++) {
			levels.row().pad(10, 0, 0, 0);	
			gameLevels.add(l, new TextButton("Unidade "+l, skin));
			gameLevels.get(l).setDisabled( ((parent.getLevelPass() >= l)? false:true) );
			levels.add(gameLevels.get(l));
		}		
		
		// Chama o m�todo que d� a��o aos bot�es
		this.buttonsListener();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
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
		stage.clear();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
