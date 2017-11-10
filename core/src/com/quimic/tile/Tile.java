package com.quimic.tile;

import com.badlogic.gdx.graphics.g2d.Sprite;

//extends Actor
public class Tile {
  
	public Sprite sprite;
	public int type;
    public float x = 0;
    public float y = 0;
    public boolean activated = false;
    public boolean destroy = false;

    public Tile(Sprite sprite, int type, float x, float y) {
        this.sprite = sprite;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
