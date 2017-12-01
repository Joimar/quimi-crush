package com.quimic.animation;

import javax.print.attribute.standard.Finishings;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HeroAnimation extends Actor {
	private AtlasRegion   atlasRegion;
	public Animation      animation;	
	private TextureRegion currentFrame;
	public float         stateTime;
	public boolean finish = false;
	/**
	 * 
	 * @param animation
	 */
	public HeroAnimation(Animation animation) {
		super();				
		this.animation = animation;
		
		//this.setWidth(120);  // Setando a largura do bloco contendo o ator
		//this.setHeight(120); // Setando a altura do bloco contendo o ator
		
		//this.setVisible(false);
	}
	
	public boolean finished(float stateTime) {
		finish = animation.isAnimationFinished(stateTime);
		return finish;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);		
		//batch.draw(atlasRegion, getX(), getY(), 30, 30);
		//batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		batch.draw(currentFrame, getX(), getY());		
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);				
	}
	
	public TextureRegion getFrame(float stateTime) {
		return (TextureRegion) animation.getKeyFrame(stateTime, true);  //new
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta; // Accumulate elapsed animation time		
	    currentFrame = (TextureRegion) animation.getKeyFrame(stateTime, true);  //new	    	
	}
}