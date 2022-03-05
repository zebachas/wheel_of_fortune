package org.terminalteam.com;

public class Main {

    public static final int DEFAULT_PORT = 9999;
    public static final int DEFAULT_PLAYERS = 3;
    public static final int DEFAULT_ROUNDS = 2;


    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        int numberOfPlayers = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PLAYERS;
        int numberOfRounds = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_ROUNDS;


        Game game = new Game(numberOfPlayers, port, numberOfRounds);
        game.start();

    }
}
