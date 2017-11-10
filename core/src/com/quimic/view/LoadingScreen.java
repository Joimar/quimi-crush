package com.quimic.view;

import com.badlogic.gdx.Screen;
import com.quimic.game.QuimiCrush;

public class LoadingScreen implements Screen {
	private QuimiCrush parent;
	
	public LoadingScreen(QuimiCrush parent) {		
		this.parent = parent;
	}

	@Override
	public void show() {
		parent.changeScreen(QuimiCrush.MAIN);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
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
