package com.quimic.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quimic.animation.LoadingBarPart;
import com.quimic.game.QuimiCrush;

public class LoadingScreen implements Screen {
	public final int IMAGE = 0; // Identificação para o carregamento de imagens
	public final int FONT  = 1;	// Identificação para o carregamento de fontes	
	public final int PARTY = 2;	// Identificação para o carregamento de efeitos de particulas
	public final int SKIN  = 3; // Identificação para o carregamento de skins
	public final int SOUND = 4;	// Identificação para o carregamento de efeitos sonoros
	public final int MUSIC = 5;	// Identificação para o carregamento das músicas
	public final int END   = 6;  // Termino dos carregamentos

//*************************************************************//	
	private QuimiCrush parent; // Quem orquestra tudo
	private int        currentLoadingStage = 0; // Estado inicial de carregamento	
	private float      countDown           = 2.5f; // Tempo, em segundos, para se manter na tela de loading após carregar tudo	
	private Animation  animation; // Animação de progresso do loading 
	
//*************************************************************//
	private TextureAtlas atlas; // Empacotamento das imagens 	
	private AtlasRegion  title; //
	//private AtlasRegion  dash; // 	
	private AtlasRegion  background; //
	
//*************************************************************//	
	Stage stage;
	Table table;
	Table loadingTable;	

//*************************************************************//		
	/**
	 * 
	 * @param parent
	 */
	public LoadingScreen(QuimiCrush parent) {		
		this.parent = parent;
		stage = new Stage(new ScreenViewport());
		
		this.loadAssets();
	}
	
	/**
	 * 
	 */
	private void loadAssets() {
		parent.assetsManager.queueAddLoadingImages(); // Carrega as imagens de loading
		parent.assetsManager.finishLoading(); // Finaliza o carregamento das imagens de loading
		
		// Captura imagens para mostrar o progresso, o logo e o background da tela
		atlas = parent.assetsManager.MANAGER.get(parent.assetsManager.LOADING_IMAGES);
		
		title = atlas.findRegion("logo"); // Captura o titulo do jogo
		//dash = atlas.findRegion("loading-dash");								
		background = atlas.findRegion("background"); // Captura o background da tela do loading
			
		Array<TextureRegion> dashes = this.addRegionsArray("bowl_", 1, 5); // Captura os sprites para mostrar o progresso do loading 				
		animation = new Animation(0.6f, dashes, PlayMode.NORMAL); // Inicio do progresso da animação		
				
		parent.assetsManager.queueAddImages();
		System.out.println("Loading images....");
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
		
	@Override
	public void show() {			
		Image titleImage = new Image(title);

		table = new Table();
		table.setFillParent(true);
		table.setDebug(false);
		table.setBackground(new TiledDrawable(background));		
			
		loadingTable = new Table();		
		loadingTable.add(new LoadingBarPart(animation));		
		
		//table.top();			
		table.add(titleImage).width(titleImage.getWidth()*1.5f).height(titleImage.getHeight()*1.5f);//; 
		table.row().align(Align.center).pad(30, 0, 0, 0);
		table.add(loadingTable);
			
		stage.addActor(table);			
	}

	@Override
	public void render(float delta) {
		// Limpa a tela
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//
		if (parent.assetsManager.MANAGER.update()) { // Load some, will return true if done loading
			currentLoadingStage+= 1;
			
		/*	if(currentLoadingStage <= END){
				loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);  // new
				//loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true); 
			}*/
			
	        switch(currentLoadingStage) {
	            case FONT:
	            	System.out.println("Loading fonts....");
	            	parent.assetsManager.queueAddFonts(); // first load done, now start fonts
	            	break;
	            case PARTY:	
	            	System.out.println("Loading Particle Effects....");
	            	parent.assetsManager.queueAddParticleEffects(); // fonts are done now do party effects
	            	break;
	            case SKIN:
	            	System.out.println("Loading skins....");
	            	parent.assetsManager.queueAddSkin(); // Carregamento de skins 
	            case SOUND:
	            	System.out.println("Loading Sounds....");
	            	parent.assetsManager.queueAddSounds();
	            	break;
	            case MUSIC:
	            	System.out.println("Loading fonts....");
	            	parent.assetsManager.queueAddMusic();
	            	break;	            
	            case END:	
	            	System.out.println("Finished"); // all done
	            	break;
	            }
		    if (currentLoadingStage > END){
		    	countDown -= delta;  // timer to stay on loading screen for short preiod once done loading
		    	currentLoadingStage = END;  // cap loading stage to 5 as will use later to display progress bar anbd more than 5 would go off the screen
		    	if(countDown < 0){ // Tempo de carregamento finalizado
		    		parent.changeScreen(QuimiCrush.MAIN); // Vai para tela de menu
		    	}
	        }			
	      }
		
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
		// TODO Auto-generated method stub

	}

}
