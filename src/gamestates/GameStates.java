package gamestates;

public enum GameStates {

	CREDITS, EDIT, EDIT_MAP, LOAD_GAME, MENU, PLAY, PLAY_NEW_GAME, TUTORIAL;

	public static GameStates gameState = MENU;

	public static void setGameState(GameStates state) {
		gameState = state;
	}

}
