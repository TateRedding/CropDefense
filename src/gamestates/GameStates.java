package gamestates;

public enum GameStates {

	CREDITS, EDIT, EDIT_MAP, EDIT_TUTORIAL, LOAD_GAME, MENU, PLAY, PLAY_NEW_GAME, PLAY_TUTORIAL, SAVE_GAME;

	public static GameStates gameState = MENU;

	public static void setGameState(GameStates state) {
		gameState = state;
	}

}
