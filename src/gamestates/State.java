package gamestates;

import main.Game;

public class State {

	protected Game game;

	public State() {

	}

	public State(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
