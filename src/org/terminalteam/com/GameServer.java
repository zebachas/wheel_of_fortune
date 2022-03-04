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
    //
    //

    private int userNumber;

    private ServerSocket serverSocket;
    private final LinkedList<ServerWorker> serverWorkers;
    private int maxPlayers;
    private int players;
    private LinkedList<Integer> votes;


    public GameServer(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        serverWorkers = new LinkedList<>();
        players = 0;
        votes = new LinkedList<>();
    }

    public void listen(int port) {
        ExecutorService clientPool = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(port);
            serveClients(clientPool);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serveClients(ExecutorService clientPool) throws IOException {
        synchronized (serverWorkers) {

            while (players != maxPlayers) {
                Socket clientSocket = serverSocket.accept();
                ServerWorker serverWorker = new ServerWorker(clientSocket);


                System.out.println(serverWorker.getUserName() + " connected!");

                serverWorkers.add(serverWorker);
                clientPool.submit(serverWorker);
            }
        }
    }


    private class ServerWorker implements Runnable {
        private String userName;
        private String receivedMessage;
        private Prompt prompt;
        private BufferedReader in;
        private PrintWriter out;

        private ServerWorker(Socket clientSocket) throws IOException {
            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            userName = null;
        }

        public String getUserName() {
            return userName;
        }

        public void sendDisconnectMessage() {
            for (ServerWorker sw : serverWorkers) {
                //sw.getOut().println(userName + " has disconnected.");
            }
        }

        public String getAllUsers() {
            StringBuilder users = new StringBuilder();
            String one = "                        ---------------------------------";
            String two = "                        |........PLAYERS PLAYING........|";
            String three = "                        ---------------------------------";

            out.println("\n");
            out.println(one);
            out.println(two);
            out.println(three);
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
            if (checkPlayers()) return;
            putThreadSleep(300);
            showFile("resources/waitingfor.txt");
            clearScreen();
            if (checkPlayers()) return;
            putThreadSleep(300);
            showFile("resources/waitingforplayers.txt");
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
            String one = "                         ------------------------------------------\n";
            String two = "                        |........PLEASE VOTE FOR A CATEGORY........|\n";
            String three = "                         ------------------------------------------\n";


            MenuInputScanner scanner = new MenuInputScanner(options);
            scanner.setMessage(one + two + three);

            int vote = prompt.getUserInput(scanner);
            votes.add(vote);
        }

        public Game.Category choseCategory() {
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

            return Files.readAllLines(path).get(randomLine);
        }

        public void createWord() throws IOException {
            String sentence = fetchRandomSentence(choseCategory());
            String[] words = sentence.split("\\W+");

            for (int i = 0; i < words.length; i++) {
                System.out.println("here");
                WordLine wordline = new WordLine(i, words[i]);
                wordline.writeWord();
                out.println(wordline.printLine());
                System.out.println(wordline.printLine());
            }
        }

        @Override
        public void run() {

            System.out.println(Thread.currentThread().getName());

            askForUser();

            while (players != maxPlayers) {
                waitMessage();
            }

            showFile("resources/logo.txt");
            out.println(getAllUsers());

            getVote();
            while (votes.size() < maxPlayers) ;
            System.out.println(choseCategory().getName());


            try {
                createWord();
            } catch (IOException e) {
                e.printStackTrace();

            }


            //sendMessageToAll(showWord());

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