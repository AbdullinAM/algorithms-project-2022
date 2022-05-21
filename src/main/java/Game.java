public class Game {
    public static int[][] tiles;

    public static void move(int x, int y) {
        if (x + 1 < 4 && tiles[x + 1][y] == 0) {
            tiles[x + 1][y] = tiles[x][y];
            tiles[x][y] = 0;
        } else if (y + 1 < 4 && tiles[x][y + 1] == 0) {
            tiles[x][y + 1] = tiles[x][y];
            tiles[x][y] = 0;
        } else if (x - 1 > -1 && tiles[x - 1][y] == 0) {
            tiles[x - 1][y] = tiles[x][y];
            tiles[x][y] = 0;
        } else if (y - 1 > -1 && tiles[x][y - 1] == 0) {
            tiles[x][y - 1] = tiles[x][y];
            tiles[x][y] = 0;
        }
    }

    static {
        clean();
    }

    public static void clean() {
        tiles = new int[4][4];
        int m = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = m;
                m++;
            }
        }
        tiles[3][3] = 0;
    }
}