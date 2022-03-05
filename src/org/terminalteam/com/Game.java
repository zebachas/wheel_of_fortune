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
        FOOD_AND_DRINKS("resources/foodanddrink.txt", "Food and Drinks", 1, "resources/foodanddrinkart.txt"),
        FUN_AND_GAMES("resources/funandgames.txt", "Fun and Games", 2, "resources/funandgamesart.txt"),
        MOVIE_QUOTES("resources/moviequotes.txt", "Movie Quotes", 3, "resources/moviequotesart.txt"),
        FICTIONAL_CHARACTERS("resources/fictionalcharacters.txt", "Fictional Characters", 4, "resources/fictionalcharactersart.txt"),
        OCCUPATIONS("resources/occupations.txt", "Occupations", 5, "resources/occupationsart.txt"),
        SLOGANS("resources/slogan.txt", "Slogan", 6, "resources/sloganart.txt");

        private String filePath;
        private String name;
        private int number;
        private String artPath;

        Category(String filePath, String name, int number, String artPath) {
            this.filePath = filePath;
            this.name = name;
            this.number = number;
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
