package com.example.goldfinder.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class AppServer {
    public static final int ROW_COUNT = 20;
    public static final int COLUMN_COUNT = 20;
    final static int serverPort = 1234;

    public static void main(String[] args) {
        try {
            Grid grid = new Grid(COLUMN_COUNT, ROW_COUNT, new Random());
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server listening on port " + serverPort);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String line;
            int playerColumn = new Random().nextInt(COLUMN_COUNT);
            int playerRow = new Random().nextInt(ROW_COUNT);
            // Send initial surroundings to the player
            sendSurroundings(out, grid, playerColumn, playerRow);

            while ((line = in.readLine()) != null) {
                if (line.startsWith("GAME_JOIN")) {
                    String playerName = line.split(":")[1];
                    out.println("GAME_START " + playerName + ":0 END");
                } else if (line.equals("SURROUNDING")) {
                    sendSurroundings(out, grid, playerColumn, playerRow);
                } else if (line.matches("^(UP|DOWN|LEFT|RIGHT)$")) {
                    // Process movement
                    boolean moved = processMovement(grid, line, playerColumn, playerRow);
                    if (moved) {
                        // Update player position based on the movement
                        switch (line) {
                            case "UP": playerRow--; break;
                            case "DOWN": playerRow++; break;
                            case "LEFT": playerColumn--; break;
                            case "RIGHT": playerColumn++; break;
                        }
                        out.println("VALIDMOVE");
                        sendSurroundings(out, grid, playerColumn, playerRow);
                    } else {
                        out.println("INVALIDMOVE");
                    }
                }
                if (checkGameOver(grid)) {
                    out.println("GAME_END");
                    break; // Sort de la boucle de communication avec le client
                }
            }

            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendSurroundings(PrintWriter out, Grid grid, int playerColumn, int playerRow) {
        StringBuilder surroundings = new StringBuilder("SURROUNDINGS ");
        // UP
        surroundings.append(grid.upWall(playerColumn, playerRow) ? "UP:WALL " : "UP:OPEN ");
        // DOWN
        surroundings.append(grid.downWall(playerColumn, playerRow) ? "DOWN:WALL " : "DOWN:OPEN ");
        // LEFT
        surroundings.append(grid.leftWall(playerColumn, playerRow) ? "LEFT:WALL " : "LEFT:OPEN ");
        // RIGHT
        surroundings.append(grid.rightWall(playerColumn, playerRow) ? "RIGHT:WALL " : "RIGHT:OPEN ");
        // Gold
        if (grid.hasGold(playerColumn, playerRow)) {
            surroundings.append("CURRENT:GOLD ");
        } else {
            surroundings.append("CURRENT:EMPTY ");
        }
        surroundings.append("END");
        out.println(surroundings.toString());
    }


    private static boolean processMovement(Grid grid, String direction, int playerColumn, int playerRow) {
        switch (direction) {
            case "UP":
                return !grid.upWall(playerColumn, playerRow) && playerRow > 0;
            case "DOWN":
                return !grid.downWall(playerColumn, playerRow) && playerRow < grid.rowCount - 1;
            case "LEFT":
                return !grid.leftWall(playerColumn, playerRow) && playerColumn > 0;
            case "RIGHT":
                return !grid.rightWall(playerColumn, playerRow) && playerColumn < grid.columnCount - 1;
            default:
                return false;
        }
    }

    private static boolean checkGameOver(Grid grid) {
        for (int i = 0; i < grid.columnCount; i++) {
            for (int j = 0; j < grid.rowCount; j++) {
                if (grid.hasGold(i, j)) {
                    return false; // Le jeu continue s'il reste de l'or
                }
            }
        }
        return true; // Le jeu se termine si tout l'or a été collecté
    }

}
