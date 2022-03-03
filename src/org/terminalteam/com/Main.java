package org.terminalteam.com;

public class Main {

    public static final int DEFAULT_PORT = 9999;

    public static void main(String[] args) {

        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        Game game = new Game(3, port);
        game.start();

    }
}
