package helps;

public class Constants {

	public static class Buttons {

		public static final int BEIGE = 0;
		public static final int BLUE = 1;
		public static final int BROWN = 2;
		public static final int GRAY = 3;

		public static final int MAP = 0;
		public static final int SQUARE = 1;
		public static final int TEXT_LARGE = 2;
		public static final int TEXT_SMALL = 3;

		public static int getButtonWidth(int buttonType) {

			switch (buttonType) {

			case MAP:
				return 136;
			case TEXT_LARGE:
				return 190;
			case TEXT_SMALL:
				return 95;
			case SQUARE:
				return 72;
			}

			return 0;

		}

		public static int getButtonHeight(int buttonType) {

			switch (buttonType) {
			case MAP:
				return 92;
			case TEXT_LARGE:
				return 49;
			case TEXT_SMALL:
				return 25;
			case SQUARE:
				return 76;
			}

			return 0;

		}

	}

	public static class Crops {

		public static final int BELL_PEPPER = 0;
		public static final int CHILI = 1;
		public static final int CORN = 2;
		public static final int TOMATO = 3;

		public static int getCropCost(int cropType) {

			switch (cropType) {
			case BELL_PEPPER:
				return 75;
			case CHILI:
				return 55;
			case CORN:
				return 35;
			case TOMATO:
				return 45;
			}

			return 0;

		}

		public static String getCropName(int cropType) {

			switch (cropType) {

			case BELL_PEPPER:
				return "Bell Pepper";
			case CHILI:
				return "Chili";
			case CORN:
				return "Corn";
			case TOMATO:
				return "Tomato";
			}

			return "";

		}

		public static float getDefaultDamage(int cropType) {

			switch (cropType) {
			case BELL_PEPPER:
				return 30.0f;
			case CHILI:
				return 3.5f;
			case CORN:
				return 12.0f;
			case TOMATO:
				return 7.0f;
			}

			return 0.0f;
		}

		public static float getDefaultRange(int cropType) {

			switch (cropType) {
			case BELL_PEPPER:
				return 144.0f;
			case CHILI:
				return 128.0f;
			case CORN:
			case TOMATO:
				return 160.0f;
			}

			return 0.0f;
		}

		public static int getCooldown(int cropType) {

			switch (cropType) {
			case BELL_PEPPER:
				return 120;
			case CHILI:
				return 160;
			case CORN:
				return 75;
			case TOMATO:
				return 95;
			}

			return 0;
		}

		public static int getTicksBetweenAttacks(int cropType) {

			switch (cropType) {
			case CHILI:
				return 2;
			case BELL_PEPPER:
			case CORN:
				return 0;
			case TOMATO:
				return 4;
			}

			return 0;
		}

		public static int getProjectilesPerAttack(int cropType) {

			switch (cropType) {
			case CHILI:
				return 20;
			case BELL_PEPPER:
			case CORN:
				return 1;
			case TOMATO:
				return 3;
			}

			return 0;
		}

	}

	public static class Directions {

		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;

	}

	public static class Enemies {

		public static final int CROW = 0;
		public static final int CROW_BOSS = 1;
		public static final int MOLD = 2;
		public static final int MOLD_BOSS = 3;
		public static final int WORM = 4;
		public static final int WORM_BOSS = 5;

		public static int getReward(int enemyType) {

			switch (enemyType) {
			case CROW:
				return 10;
			case CROW_BOSS:
				return 70;
			case MOLD:
				return 15;
			case MOLD_BOSS:
				return 105;
			case WORM:
				return 5;
			case WORM_BOSS:
				return 35;
			}

			return 0;

		}

		public static float getSpeed(int enemyType) {

			switch (enemyType) {
			case CROW:
				return 0.7f;
			case CROW_BOSS:
				return 0.525f;
			case MOLD:
				return 0.4f;
			case MOLD_BOSS:
				return 0.3f;
			case WORM:
				return 0.5f;
			case WORM_BOSS:
				return 0.375f;
			}

			return 0;

		}

		public static int getStartHealth(int enemyType) {

			switch (enemyType) {
			case CROW:
				return 150;
			case CROW_BOSS:
				return 1500;
			case MOLD:
				return 275;
			case MOLD_BOSS:
				return 2750;
			case WORM:
				return 115;
			case WORM_BOSS:
				return 1150;
			}

			return 0;

		}

		public static class Animations {

			public final static int MOVE = 0;
			public final static int DEATH = 1;

		}

	}

	public static class Projectiles {

		public static final int KERNEL = 0;
		public static final int SEED = 1;
		public static final int SPRAY = 2;

		public static float getSpeed(int projectileType) {

			switch (projectileType) {
			case KERNEL:
				return 8.0f;
			case SEED:
				return 6.0f;
			case SPRAY:
				return 4.0f;
			}

			return 0.0f;
		}
	}

	public static class Tiles {

		public static final int GRASS = 0;
		public static final int WATER = 1;
		public static final int ROAD = 2;

		public static class PathPoints {

			public static final int START = 0;
			public static final int END = 1;

		}

	}

}
