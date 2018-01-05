package com.quimic.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;

public class Loader {	
	// Gerenciador de itens (ativos/assets)
	public final AssetManager MANAGER = new AssetManager();
	// Skin
	public final String SKIN = "skin/craftacular-ui.json";
	public final String SKIN2 = "skin/support/pixthulhu-ui.json";	
	// Textures
	public final String LOADING_IMAGES = "images/loading.atlas";
	// Sounds
	public final String COMBINE_SOUND = "sounds/combine.wav";	
	public final String WATER_ATTACK_SOUND = "sounds/water.mp3";
	public final String CARBON_ATTACK_SOUND = "sounds/diCarbon.mp3";
	public final String GLASS_ATTACK_SOUND = "sounds/glass.mp3";
	public final String LAUGH_ATTACK_SOUND = "sounds/laugh.wav";
	
	// Music
	public final String PLAYING_SONG = "music/music.mp3";
//*************************************************************//	

	/**
	 * 
	 */
	public void queueAddSkin(){
		SkinParameter params = new SkinParameter("skin/craftacular-ui.atlas");
		MANAGER.load(SKIN, Skin.class, params);
		
		params = new SkinParameter("skin/support/pixthulhu-ui.atlas");
		MANAGER.load(SKIN2, Skin.class, params);
	}	
	
	/**
	 * 
	 */
	public void queueAddImages() {
		//MANAGER.load(PLAYER_IMAGE, Texture.class);
		//MANAGER.load(ENEMY_IMAGE, Texture.class);
	}	
	
	/**
	 * 
	 */
	public void queueAddLoadingImages(){
		MANAGER.load(LOADING_IMAGES, TextureAtlas.class);
	}
	
	/**
	 * 
	 */
	public void queueAddFonts(){	
	}

	/**
	 * 
	 */
	public void queueAddParticleEffects(){
	}
	
	/**
	 * 
	 */
	public void queueAddSounds() {
		MANAGER.load(COMBINE_SOUND, Sound.class);		
		//MANAGER.load(MATCH_SOUND, Sound.class);
		MANAGER.load(WATER_ATTACK_SOUND, Sound.class);
		MANAGER.load(CARBON_ATTACK_SOUND, Sound.class);
		MANAGER.load(GLASS_ATTACK_SOUND, Sound.class);
		MANAGER.load(LAUGH_ATTACK_SOUND, Sound.class);
	}
	
	/**
	 * 
	 */
	public void queueAddMusic(){
		MANAGER.load(PLAYING_SONG, Music.class);
	}	
	
	/**
	 * 
	 */
	public void finishLoading() {
		MANAGER.finishLoading();
	}
}