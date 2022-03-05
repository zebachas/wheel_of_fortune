package org.terminalteam.com;

public class Game {

    private int maxPlayers;
    private int port;
    private GameServer gameServer;
    private int numberOfRounds;

    public Game(int numberOfPlayers, int port, int numberOfRounds) {
        this.maxPlayers = numberOfPlayers;
        this.port = port;
        this.numberOfRounds = numberOfRounds;
    }

    public void start() {
        gameServer = new GameServer(maxPlayers, numberOfRounds);
        gameServer.listen(port);
    }

    public enum Category {
        FOOD_AND_DRINKS("resources/foodanddrink.txt", "Food and Drinks", "resources/foodanddrinkart.txt"),
        FUN_AND_GAMES("resources/funandgames.txt", "Fun and Games", "resources/funandgamesart.txt"),
        MOVIE_QUOTES("resources/moviequotes.txt", "Movie Quotes", "resources/moviequotesart.txt"),
        FICTIONAL_CHARACTERS("resources/fictionalcharacters.txt", "Fictional Characters", "resources/fictionalcharactersart.txt"),
        OCCUPATIONS("resources/occupations.txt", "Occupations", "resources/occupationsart.txt"),
        SLOGANS("resources/slogan.txt", "Slogan", "resources/sloganart.txt");

        private String filePath;
        private String name;
        private String artPath;

        Category(String filePath, String name, String artPath) {
            this.filePath = filePath;
            this.name = name;
            this.artPath = artPath;
        }

        public String getName() {
            return name;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getArtPath() {
            return artPath;
        }
    }
}
