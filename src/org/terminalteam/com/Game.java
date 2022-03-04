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
        FOOD_AND_DRINK("resources/foodanddrink.txt", "Food and Drinks", 1),
        FUN_AND_GAMES("resources/funandgames.txt", "Fun and Games", 2),
        MOVIE_QUOTES("resources/moviequotes.txt", "Movie Quotes", 3),
        FICTIONAL_CHARACTERS("resources/fictionalcharacters.txt", "Fictional Characters", 4),
        OCCUPATIONS("resources/occupations.txt", "Occupations", 5),
        SLOGANS("resources/slogan.txt", "Slogan", 6);

        private String filePath;
        private String name;
        private int number;

        Category(String filePath, String name, int number) {
            this.filePath = filePath;
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}
