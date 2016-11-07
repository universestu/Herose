package com.oop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oop.game.Screen.GameScreen;

public class Herose extends Game {
	public SpriteBatch batch;
	public static final int V_WIDTH = 600;
	public static final int V_HEIGHT = 480;
	public static final float PPM = 100;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
