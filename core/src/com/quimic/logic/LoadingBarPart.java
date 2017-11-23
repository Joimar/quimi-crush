/**
 * 
 */
package com.quimic.logic;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingBarPart extends Actor {	
	private AtlasRegion   atlasRegion;
	private Animation     animation;
	private float         stateTime;
	private TextureRegion currentFrame;		
	
	/**
	 * 
	 * @param animation
	 */
	public LoadingBarPart(Animation animation) {
		super();				
		this.animation = animation;
		
		//this.setWidth(120);  // Setando a largura do bloco contendo o ator
		//this.setHeight(120); // Setando a altura do bloco contendo o ator
		
		//this.setVisible(true);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);				
		//batch.draw(atlasRegion, getX(), getY(), 30, 30);
		//batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		batch.draw(currentFrame, getX(), getY(), 64, 118);		
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta; // Accumulate elapsed animation time		
	    currentFrame = (TextureRegion) animation.getKeyFrame(stateTime, true);  //new
	}
}