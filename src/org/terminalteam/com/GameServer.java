package org.terminalteam.com;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import javax.swing.plaf.RootPaneUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    //
    //

    private int userNumber;

    private ServerSocket serverSocket;
    private LinkedList<ServerWorker> serverWorkers;
    private int maxPlayers;


    public GameServer(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        userNumber = 0;
        serverWorkers = new LinkedList<>();
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

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ServerWorker serverWorker = new ServerWorker(clientSocket, ++userNumber);


            System.out.println(serverWorker.getUserName() + " connected!");

            serverWorkers.add(serverWorker);
            clientPool.submit(serverWorker);
        }
    }

    private class ServerWorker implements Runnable {
        private String userName;
        private String receivedMessage;
        private Prompt prompt;
        private BufferedReader in;
        private PrintWriter out;

        private ServerWorker(Socket clientSocket, int userNumber) throws IOException {
            userName = "Client " + userNumber;
            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        }

        public String getUserName() {
            return userName;
        }

        public void sendJoinMessageToAll() {
            for (ServerWorker sw : serverWorkers) {
                if (!sw.equals(this)) {
                    sw.out.println(userName + " has join the game!!");
                }
            }
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
            for (ServerWorker sw : serverWorkers) {
                sw.out.println("\n");
                sw.out.println(one);
                sw.out.println(two);
                sw.out.println(three);
                users.append(sw.getUserName()).append("\n");
            }

            return users.toString();
        }

        public void showLogo() {
            Path filePath = Paths.get("resources/logo.txt");
            try {
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    for (ServerWorker sw : serverWorkers) {
                        sw.out.println(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void askForUser() {
            StringInputScanner userName = new StringInputScanner();
            userName.setMessage("Write your user name: ");
            this.userName = prompt.getUserInput(userName);

        }

        public synchronized void waitMessage() {
            for (ServerWorker sw : serverWorkers) {
                sw.out.println("\n\n\n\n\n\n");
                sw.out.println("                        ---------------------------------");
                putThreadSleep(300);
                sw.out.println("                        |...WAITING FOR OTHER PLAYERS...|");
                putThreadSleep(300);
                sw.out.println("                        ---------------------------------");
            }
        }

        public void sendMessageToAll(String message) {
            for (ServerWorker sw: serverWorkers) {
                sw.out.println(message);
            }
        }

        @Override
        public void run() {
            askForUser();
            sendJoinMessageToAll();
            while (userNumber < maxPlayers){
                waitMessage();
                putThreadSleep(800);
            }
            showLogo();
            sendMessageToAll(getAllUsers());
            while (true) {
                /*try {
                    //receivedMessage = in.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
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