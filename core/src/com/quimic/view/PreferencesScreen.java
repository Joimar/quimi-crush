package com.quimic.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.game.QuimiCrush;

public class PreferencesScreen implements Screen {
	private QuimiCrush parent; // Quem orquestra tudo
		
	private Stage          stage; // Controla e reage às entradas do usuário	
	private ScreenViewport sv; // Relaciona as medidas da tela do jogo com a do mundo real 1px = 1un
	private Table          table; // Grid do menu
	private Skin           skin; // Estilo dos botões
	private Skin           skin2; // O que faltou no skin
	
	// Slider para controle de volume
	private Slider musicSlider;
	private Slider soundEffectsSlider;	
	
	// CheckBox On/Off
	private CheckBox musicCheckBox;
	private CheckBox soundEffectsCheckBox;		
	
	// Campos texto indicativo
	private Label titleLabel; 
	private Label volumeMusicLabel;
	private Label volumeSoundLabel;
	private Label musicOnOffLabel;
	private Label soundOnOffLabel;	
	
	// Botão para voltar para a tela principal
	private TextButton back;	
	
//*************************************************************//	
	
	public PreferencesScreen(QuimiCrush parent) {		
		this.parent = parent;
		sv = new ScreenViewport();
		stage = new Stage(sv);
		
		parent.assetsManager.queueAddSkin(); // Carrega as skins  
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das skins
		skin = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN); // Recupera a skin		
		skin2 = parent.assetsManager.MANAGER.get(parent.assetsManager.SKIN2); // Recupera a skin2
	}

	/**
	 * 
	 */
	private void buttonsListener() { 		
		musicSlider.setValue(parent.savePreferences.getMusicVolume());
		musicSlider.addListener(new EventListener() {			
			@Override
			public boolean handle(Event event) {
				parent.savePreferences.setMusicVolume(musicSlider.getValue());
				parent.playingSong.setVolume(musicSlider.getValue());
				return false;
			}
		});
				
		soundEffectsSlider.setValue(parent.savePreferences.getSoundVolume());
		soundEffectsSlider.addListener(new EventListener() {			
			@Override
			public boolean handle(Event event) {
				parent.savePreferences.setSoundVolume(soundEffectsSlider.getValue());
				return false;
			}
		});			
				
		musicCheckBox.setChecked(parent.savePreferences.isMusicEnabled());
		musicCheckBox.addListener(new EventListener() {			
			@Override
			public boolean handle(Event event) {
				parent.savePreferences.setMusicEnabled(musicCheckBox.isChecked());
				if (musicCheckBox.isChecked())
					parent.playingSong.play();
				else 
					parent.playingSong.pause();
				return false;
			}
		});
				
		soundEffectsCheckBox.setChecked(parent.savePreferences.isSoundEffectsEnabled());
		soundEffectsCheckBox.addListener(new EventListener() {			
			@Override
			public boolean handle(Event event) {
				parent.savePreferences.setSoundEffectsEnabled(soundEffectsCheckBox.isChecked());
				return false;
			}
		});
			
		back.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(QuimiCrush.MAIN);
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
		
		// Cria os elementos/itens da tela de ajustes 
		musicSlider          = new Slider(0f, 1f, 0.1f, false, skin);
		musicCheckBox        = new CheckBox(null, skin2);
		soundEffectsSlider   = new Slider(0f, 1f, 0.1f, false, skin);
		soundEffectsCheckBox = new CheckBox(null, skin2);
		back                 = new TextButton("Back", skin);		

		// Cria os rótulos para a tela de ajustes
		titleLabel       = new Label("Settings", skin);	
		volumeMusicLabel = new Label("Music Volume", skin);
		volumeSoundLabel = new Label("Effects Volume", skin);
		musicOnOffLabel  = new Label("Music", skin);
		soundOnOffLabel  = new Label("Effects", skin);
		
		// Organiza os botões no grid		
		table.add(titleLabel).colspan(2);
		table.row().pad(10,0,0,10);
		table.add(volumeMusicLabel).left();
		table.add(musicSlider);
		table.row().pad(10,0,0,10);
		table.add(musicOnOffLabel).left();
		table.add(musicCheckBox);
		table.row().pad(10,0,0,10);
		table.add(volumeSoundLabel).left();
		table.add(soundEffectsSlider);
		table.row().pad(10,0,0,10);	
		table.add(soundOnOffLabel).left();
		table.add(soundEffectsCheckBox);
		table.row().pad(10,0,0,10);
		table.add(back).width(0.5f*back.getWidth()).colspan(2);		
		
		// Chama o método que dá ação aos itens
		this.buttonsListener();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
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
