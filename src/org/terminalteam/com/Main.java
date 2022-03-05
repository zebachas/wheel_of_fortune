package org.terminalteam.com;

public class Main {

    public static final int DEFAULT_PORT = 9999;
    public static final int DEFAULT_PLAYERS = 3;

    public static void main(String[] args) {
        /*WordLine wordLine = new WordLine(0, "afonso");
        System.out.println(wordLine.printLine());*/

        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        int numberOfPlayers = args.length > 0 ? Integer.parseInt(args[1]) : DEFAULT_PLAYERS;

        Game game = new Game(numberOfPlayers, port);
        game.start();

    }
}
