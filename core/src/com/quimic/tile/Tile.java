package com.quimic.tile;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

//extends Actor
public class Tile extends Actor {
  
	public Sprite sprite;
	public int type;
    public float x = 0;
    public float y = 0;
    public boolean activated = false;
    public boolean destroy = false;

    public Tile(Sprite sprite, int type, float x, float y) {
        super();
    	this.sprite = sprite;
        this.type = type;
        this.x = x;
        this.y = y;
        
        this.setWidth(x);  // Setando a largura do bloco contendo o ator
        this.setHeight(y); // Setando a altura do bloco contendo o ator
    }
    
    
    /**
     * 
     * @return
     */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * 
	 * @param sprite
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);				
		batch.draw(sprite, getX(), getY(), x, y);			
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);		
	}	
}