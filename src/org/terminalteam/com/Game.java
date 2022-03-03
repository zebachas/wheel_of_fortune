package org.terminalteam.com;

public class Game {

    private int maxPlayers;
    private int port;
    private GameServer gameServer;

    public Game(int numberOfPlayers, int port) {
        this.maxPlayers = numberOfPlayers;
        this.port = port;
    }

    public void start() {
        gameServer = new GameServer(maxPlayers);
        gameServer.listen(port);
    }

    public enum Category {
        FOOD_AND_DRINK("categories/foodanddrink.txt"),
        FUN_AND_GAMES("categories/funandgames.txt"),
        MOVIE_QUOTES("categories/moviequotes.txt"),
        FICTIONAL_CHARACTERS("categories/fictionalcharacters.txt"),
        OCCUPATIONS("categories/occupations.txt"),
        SLOGANS("categories/slogan.txt");

        private String filePath;

        Category(String filePath) {
            this.filePath = filePath;
        }
    }
}
