package com.quimic.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.game.QuimiCrush;

public class MainScreen implements Screen {
	private QuimiCrush parent; // Quem orquestra tudo
	
	private Stage          stage; // Controla e reage às entradas do usuário
	private ScreenViewport sv; // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un
	private Table          table; // Grid do menu
	private Skin           skin; // Estilo dos botões	
	
//*************************************************************//	
	private TextButton preferences;
	private TextButton gameLevel_1;
	private TextButton gameLevel_2;
	
//*************************************************************//
	
	/**
	 * 
	 * @param parent
	 */
	public MainScreen(QuimiCrush parent) {		
		this.parent = parent;
		sv = new ScreenViewport();
		stage = new Stage(sv);
		
		parent.assetsManager.queueAddSkin(); // Carrega as skins  
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das skins
		skin = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN); // Recupera a skin		
	}

	/**
	 * 
	 */
	private void buttonsListener() {						
		preferences.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(QuimiCrush.PREFERENCES);
			}
		});
		
		gameLevel_1.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(QuimiCrush.GAME);
			}
		});
		
		gameLevel_2.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(QuimiCrush.GAME);
			}
		});
	}
		
	@Override
	public void show() {
		// Limpa o palco para uma novo controle
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		// Cria o grid para inserir os botões do jogo
		table = new Table(); // Cria a tabela
		table.setFillParent(true);
		table.setDebug(true);
		stage.addActor(table); // Adiciona a tabela no stage	

		// Cria os botões da tela principal
		preferences = new TextButton("Settings", skin);
		gameLevel_1 = new TextButton("1", skin);
		gameLevel_2 = new TextButton("2", skin);		
		
		// Organiza os botões no grid		
		table.add(preferences).colspan(2);
		table.row().pad(10, 0, 10, 0);		
		table.add(gameLevel_1).left();		
		table.row();
		table.add(gameLevel_2).left();
		
		// Chama o método que dá ação aos botões
		this.buttonsListener();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
