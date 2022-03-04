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
        FOOD_AND_DRINK("resources/foodanddrink.txt", "Food and Drinks"),
        FUN_AND_GAMES("resources/funandgames.txt", "Fun and Games"),
        MOVIE_QUOTES("resources/moviequotes.txt", "Movie Quotes"),
        FICTIONAL_CHARACTERS("resources/fictionalcharacters.txt", "Fictional Characters"),
        OCCUPATIONS("resources/occupations.txt", "Occupations"),
        SLOGANS("resources/slogan.txt", "Slogan");

        private String filePath;
        private String name;

        Category(String filePath, String name) {
            this.filePath = filePath;
            this.name = name;
        }
    }
}
