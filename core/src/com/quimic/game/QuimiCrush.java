package com.quimic.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.quimic.view.GameScreen;
import com.quimic.view.LoadingScreen;
import com.quimic.view.MainScreen;
import com.quimic.view.PreferencesScreen;
import com.quimic.levels.GameFactory;
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
	private GameFactory       gameFactory;
	
//*************************************************************//		
	public int             windowWidth;
    public int             windowHeight;        
    public SavePreferences savePreferences;
    public Loader          assetsManager;
    public Music           playingSong;
    
//*************************************************************//    
    public final int TOTAL_LEVELS = 10; // Quantidade de fases do jogo (contando com o tutorial); de 0 -> (TOTAL_LEVELS-1)
    private int levelPass;
    
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
		}
	}
    
    /**
     * Entra em uma determinada fase do jogo
     * 
     * @param screen Identificação da nova fase 
     */
    public void changeLevel(int level) {				
		game = new GameFactory(this, level).getGameLevelScreen();
		this.setScreen(game);				
	}
    
    /**
     * 
     * @return
     */
	public int getLevelPass() {
		return levelPass;
	}

	/**
	 * Salva em disco a fase concluida
	 * 
	 * @param levelPass A fase do jogo 
	 */
	public void setLevelPass(int levelPass) {
		this.levelPass = levelPass;
		savePreferences.setLevelPass(levelPass);
	}
    
	@Override
	public void create () {
		// Inicializando configurações iniciais 
		assetsManager   = new Loader(); // Gerenciador de ativos
		savePreferences = new SavePreferences(); // Saves do jogo
		windowWidth     = Gdx.graphics.getWidth(); // Largura da tela do jogo
	    windowHeight    = Gdx.graphics.getHeight(); // Altura da tela do jogo	    
	    
		// Carregando configurações iniciais			
		assetsManager.queueAddMusic(); // Carrega a música
		assetsManager.finishLoading(); // Finaliza o carregamento dos assets  
		playingSong = assetsManager.MANAGER.get(assetsManager.PLAYING_SONG); // Recupera a música
		
		// Carregando configurações iniciais para o volume e música (em loop)
		playingSong.setVolume(savePreferences.getMusicVolume());						
		if (savePreferences.isMusicEnabled())
			playingSong.play();
		else 
			playingSong.pause();
		playingSong.setLooping(true);
		
		// Recuperando os saves do jogador
		levelPass = savePreferences.getLevelPass();				
		
		// Mudando para a tela de loading		
		loading = new LoadingScreen(this);
		this.setScreen(loading);
	}		

	@Override
	public void dispose () {
		playingSong.dispose();
		assetsManager.MANAGER.dispose();
	}
}