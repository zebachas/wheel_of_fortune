package org.terminalteam.com;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private ServerSocket serverSocket;
    private final LinkedList<ServerWorker> serverWorkers;
    private int maxPlayers;
    private int players;
    private LinkedList<Integer> votes;
    private String gameSentence;
    private String playerSentence;
    private Game.Category chosenCategory;
    private boolean over;
    private String winnerName;


    public GameServer(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        serverWorkers = new LinkedList<>();
        this.gameSentence = null;
        players = 0;
        votes = new LinkedList<>();
        over = false;
    }

    public void listen(int port) {
        ExecutorService clientPool = Executors.newCachedThreadPool();


        try {
            serverSocket = new ServerSocket(port);
            serveClients(clientPool);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.print("");
            if (votes.size() == serverWorkers.size()) {
                System.out.println();
                chosenCategory = countVotes();
                try {
                    gameSentence = fetchRandomSentence(chosenCategory);
                    System.out.println(gameSentence);
                    generatePlayerSentence();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        showAll(chosenCategory.getArtPath());

        while (!over) {
            putThreadSleep(100);
            System.out.println();
            sendAll(playerSentence);
            putThreadSleep(8000);
            showRandomLetter();
        }
    }

    private void serveClients(ExecutorService clientPool) throws IOException {
        synchronized (serverWorkers) {

            while (serverWorkers.size() < maxPlayers) {
                Socket clientSocket = serverSocket.accept();
                ServerWorker serverWorker = new ServerWorker(clientSocket);

                serverWorkers.add(serverWorker);
                clientPool.submit(serverWorker);
            }
        }
    }

    public Game.Category countVotes() {
        HashMap<Integer, Integer> voteCount = new HashMap<>();
        for (int vote : votes) {
            if (!voteCount.containsKey(vote)) {
                voteCount.put(vote, 1);
            } else {
                voteCount.put(vote, voteCount.get(vote) + 1);
            }
        }

        int mostCommon = 0;

        for (int voteNum : voteCount.keySet()) {
            if (voteNum > mostCommon) {
                mostCommon = voteNum;
            }
        }

        return Game.Category.values()[mostCommon - 1];
    }

    public String fetchRandomSentence(Game.Category category) throws IOException {
        Path path = Paths.get(category.getFilePath());
        long numLines = 0;

        numLines = Files.lines(path).count();
        int randomLine = (int) (Math.random() * numLines);
        return Files.readAllLines(path).get(randomLine).toUpperCase();
    }

    public void generatePlayerSentence() {
        playerSentence = "";

        for (int i = 0; i < gameSentence.length(); i++) {
            if (String.valueOf(gameSentence.charAt(i)).equals(" ")) {
                playerSentence += " ";
            } else {
                playerSentence += "_";
            }
        }
    }

    public void showRandomLetter() {
        int randomLetterIndex = (int) (Math.random() * playerSentence.length() - 1);

        while (!String.valueOf(playerSentence.charAt(randomLetterIndex)).equals("_")) {
            randomLetterIndex = (int) (Math.random() * playerSentence.length() - 1);
        }
        playerSentence = playerSentence.substring(0, randomLetterIndex) + gameSentence.charAt(randomLetterIndex) + playerSentence.substring(randomLetterIndex + 1);
    }

    public void showAll(String filePath) {
        for (ServerWorker sw : serverWorkers) {
            sw.showFile(filePath);
        }
    }

    public void clearAllScreens() {
        for (ServerWorker sw: serverWorkers) {
            sw.clearScreen();
        }
    }

    public void sendAll(String message) {
        for (ServerWorker sw : serverWorkers) {
            sw.out.println(message);
        }
    }

    public void putThreadSleep(int num) {
        try {
            Thread.sleep(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ServerWorker implements Runnable {
        private String userName;
        private String receivedMessage;
        private Prompt prompt;
        private BufferedReader in;
        private PrintWriter out;
        private int score;
        private String answer;

        private ServerWorker(Socket clientSocket) throws IOException {
            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            userName = null;
            answer = "";
        }

        public String getUserName() {
            return userName;
        }


        public String getAllUsers() {
            StringBuilder users = new StringBuilder();

            /*String one = "                        ---------------------------------";
            String two = "                        |........PLAYERS PLAYING........|";
            String three = "                        ---------------------------------";

            out.println("\n");
            out.println(one);
            out.println(two);
            out.println(three);*/

            showFile("resources/playersplaying.txt");

            for (ServerWorker sw : serverWorkers) {
                users.append(sw.getUserName()).append("\n");
            }

            return users.toString();
        }


        public void showFile(String path) {
            Path filePath = Paths.get(path);
            try {
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void askForUser() {
            System.out.println(Thread.currentThread().getName());
            StringInputScanner name = new StringInputScanner();
            name.setMessage("Write your user name: ");
            userName = prompt.getUserInput(name);
            System.out.println(Thread.currentThread().getName());
            players++;
        }

        public void clearScreen() {
            out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }

        public void waitMessage() {
            showFile("resources/waiting.txt");
            clearScreen();
            clearScreen();
            if (checkPlayers()) return;
            putThreadSleep(300);
            showFile("resources/waitingfor.txt");
            clearScreen();
            clearScreen();
            if (checkPlayers()) return;
            putThreadSleep(300);
            showFile("resources/waitingforplayers.txt");
            clearScreen();
            clearScreen();
            putThreadSleep(800);
        }

        public boolean checkPlayers() {
            return players == maxPlayers;
        }

        public void sendMessageToAll(String message) {
            for (ServerWorker sw : serverWorkers) {
                sw.out.println(message);
            }
        }


        public void getVote() {
            String[] options = new String[Game.Category.values().length];

            for (int i = 0; i < options.length; i++) {
                options[i] = Game.Category.values()[i].getName();
            }
            MenuInputScanner scanner = new MenuInputScanner(options);
            showFile("resources/entervote.txt");

            int vote = prompt.getUserInput(scanner);
            votes.add(vote);
        }


        public void createWord() throws IOException {
            String[] words = gameSentence.split("\\W+");

            /*for (int i = 0; i < words.length; i++) {
                System.out.println("here");

                WordLine wordline = new WordLine(i, words[i]);
                wordline.writeWord();

                out.println(wordline.printLine());
                System.out.println(wordline.printLine());
            }*/
            out.println(gameSentence);
        }

        public void gameRules() {
            showFile("resources/gamerulesinitial.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP1.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP2.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP3.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP4.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP5.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP6.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesSTEP7.txt");
            clearAllScreens();
            putThreadSleep(1000);
            showFile("resources/gamerulesfinal.txt");
            putThreadSleep(5000);
        }

        @Override
        public void run() {

            System.out.println(Thread.currentThread().getName());

            askForUser();
            clearScreen();
            clearScreen();

            while (players != maxPlayers) {
                waitMessage();
            }
            putThreadSleep(2500);
            clearScreen();
            clearScreen();
            clearScreen();

            showFile("resources/logo.txt");
            putThreadSleep(2500);
            clearScreen();
            clearScreen();
            clearScreen();

            gameRules();
            putThreadSleep(2500);
            clearScreen();
            clearScreen();

            out.println(getAllUsers());
            putThreadSleep(2000);
            clearScreen();
            clearScreen();
            clearScreen();

            getVote();
            putThreadSleep(2000);
            clearScreen();
            clearScreen();
            clearScreen();

            showFile("resources/waitingforvotes.txt");

            while (votes.size() < serverWorkers.size()) {
                System.out.println();
            }

            clearScreen();
            clearScreen();
            clearScreen();

            while (true) {
                StringInputScanner scanner = new StringInputScanner();
                scanner.setMessage("Try to guess the sentence!!!\n");
                putThreadSleep(200);
                answer = prompt.getUserInput(scanner);
                System.out.println(answer);
                if (answer.equals(gameSentence)){
                    sendAll("PLAYER: " + userName + " HAS GUESSED THE PHRASE");
                    sendAll(userName + "IS THE WINNER!!!!!!!!");
                    over = true;
                    score++;
                    break;
                }
            }

        }

        public String getAnswer() {
            return answer;
        }

        public void putThreadSleep(int num) {
            try {
                Thread.sleep(num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}