package com.example.goldfinder;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

public class AppClient extends javafx.application.Application {
    private static final String VIEW_RESOURCE_PATH = "/com/example/goldfinder/gridView.fxml";
    private static final String APP_NAME = "Gold Finder";


    private Stage primaryStage;
    private Parent view;
    private void initializePrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(APP_NAME);
        this.primaryStage.setOnCloseRequest(event -> Platform.exit());
        this.primaryStage.setResizable(true);
        this.primaryStage.sizeToScene();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initializePrimaryStage(primaryStage);
        initializeView();
        connectToServer("localhost", 1234); // Remplacer "localhost" et 1234 par les valeurs appropriées
        showScene();
    }




    private void showScene() {
        Scene scene = new Scene(view);
        primaryStage.setScene(scene);
        primaryStage.show();
        view.requestFocus(); // Demande le focus pour capturer les événements clavier
    }


    public static void main(String[] args) {
        launch(args);
    }


    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private void connectToServer(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Thread serverListener = new Thread(this::listenToServer);
        serverListener.setDaemon(true);
        serverListener.start();

        sendToServer("GAME_JOIN:playerName");
    }


    private void listenToServer() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                String finalMessage = message; // Cette étape est en fait redondante dans ce cas précis
                Platform.runLater(() -> controller.updateGameState(finalMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    void sendToServer(String message) {
        out.println(message);
    }

    private void handleServerMessage(String message) {
        System.out.println("Message from server: " + message);
    }


    private void initializeView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL location = AppClient.class.getResource(VIEW_RESOURCE_PATH);
        loader.setLocation(location);
        view = loader.load();

        // Capture la référence au Controller
        controller = loader.getController();

        // Donne au Controller une référence à cet AppClient pour permettre la communication avec le serveur
        controller.setAppClient(this);

        view.setOnKeyPressed(controller::handleMove);
        controller.initialize();
    }


    private Controller controller;

    // Dans AppClient.java
    public void receiveMessageFromServer(String message) {
        // Supposons que cette méthode soit appelée chaque fois qu'un message est reçu du serveur.
        if (controller != null) {
            controller.updateGameState(message);
        }
    }


}