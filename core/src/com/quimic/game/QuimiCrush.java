package com.quimic.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.quimic.view.GameScreen;
import com.quimic.view.LoadingScreen;
import com.quimic.view.MainScreen;
import com.quimic.view.PreferencesScreen;
import com.quimic.loader.Loader;
import com.quimic.util.SavePreferences;

public class QuimiCrush extends Game {
	public static final int MAIN        = 0;
	public static final int PREFERENCES = 1;
	public static final int GAME        = 2;
	
//*************************************************************//			
	private LoadingScreen     loading;
	private MainScreen        main;
	private PreferencesScreen preferences;
	private GameScreen        game;	
	
//*************************************************************//		
	public int             windowWidth;
    public int             windowHeight;        
    public SavePreferences savePreferences;
    public Loader          assetsManager;
    public Music           playingSong;
    
//*************************************************************//
    /**
     * Altera entre as telas do jogo.
     * 
     * @param screen Identificação da nova tela 
     */
    public void changeScreen(int screen) {		
		switch (screen) {
			case MAIN:
				if (main == null) main = new MainScreen(this);
				this.setScreen(main);
				break;
			case PREFERENCES:
				if (preferences == null) preferences = new PreferencesScreen(this);
				this.setScreen(preferences);
				break;
			case GAME:
				if (game == null) game = new GameScreen(this);
				this.setScreen(game);
				break;
		}
	}
              
	@Override
	public void create () {
		// Inicializando configurações iniciais 
		assetsManager   = new Loader(); // Gerenciador de ativos
		savePreferences = new SavePreferences(); // Saves do jogo
		windowWidth     = Gdx.graphics.getWidth(); // Largura da tela do jogo
	    windowHeight    = Gdx.graphics.getHeight(); // Altura da tela do jogo	    
	    System.out.println("X: "+windowWidth+" ... Y: "+windowHeight);			    
	    
		// Carregando configurações iniciais			
		assetsManager.queueAddMusic(); // Carrega a música
		assetsManager.finishLoading(); // Finaliza o carregamento dos assets  
		playingSong = assetsManager.MANAGER.get(assetsManager.PLAYING_SONG); // Recupera a música
		
		// Inicia a música do jogo e a coloca em loop
		playingSong.play();
		playingSong.setLooping(true);		 
		
		// Carregando configurações iniciais para o volume
		playingSong.setVolume(savePreferences.getMusicVolume());						
		if (savePreferences.isMusicEnabled())
			playingSong.play();
		else 
			playingSong.pause();
				
		// Mudando para a tela de loading
		//this.changeScreen(GAME);
		loading = new LoadingScreen(this);
		this.setScreen(loading);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		windowWidth  = width;//Gdx.graphics.getWidth(); // Largura da tela do jogo
	    windowHeight = height;//Gdx.graphics.getHeight(); // Altura da tela do jogo	    
	}

	@Override
	public void dispose () {
		playingSong.dispose();
		assetsManager.MANAGER.dispose();
	}
}