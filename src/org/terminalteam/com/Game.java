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
        FOOD_AND_DRINK("resources/foodanddrink.txt"),
        FUN_AND_GAMES("resources/funandgames.txt"),
        MOVIE_QUOTES("resources/moviequotes.txt"),
        FICTIONAL_CHARACTERS("resources/fictionalcharacters.txt"),
        OCCUPATIONS("resources/occupations.txt"),
        SLOGANS("resources/slogan.txt");

        private String filePath;

        Category(String filePath) {
            this.filePath = filePath;
        }
    }
}
