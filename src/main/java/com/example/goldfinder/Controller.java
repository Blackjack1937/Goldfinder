package com.example.goldfinder;

import com.example.goldfinder.server.AppServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Controller {

    @FXML
    private Canvas gridCanvas;
    @FXML
    private Label score;

    private GridView gridView;
    private AppClient appClient;
    private int column; // Position actuelle du joueur en colonne
    private int row;    // Position actuelle du joueur en ligne

    public void initialize() {
        gridView = new GridView(gridCanvas, AppServer.COLUMN_COUNT, AppServer.ROW_COUNT);
        score.setText("0");
        gridView.repaint();
        // Initialisation de la position du joueur, cela pourrait aussi venir du serveur
        column = 10;
        row = 10;
        gridView.paintToken(column, row);
        gridCanvas.setFocusTraversable(true);
        gridCanvas.requestFocus();
        gridCanvas.setOnKeyPressed(this::handleMove);
    }

    public void pauseToggleButtonAction(ActionEvent actionEvent) {
        // Envoyer une commande de pause au serveur
        appClient.sendToServer("GAME_PAUSE");
    }


    public void playToggleButtonAction(ActionEvent actionEvent) {
        // Envoyer une commande pour démarrer ou reprendre le jeu
        appClient.sendToServer("GAME_PLAY");
    }


    public void oneStepButtonAction(ActionEvent actionEvent) {
        // Envoyer une commande pour avancer le jeu d'un pas
        appClient.sendToServer("GAME_STEP");
    }


    public void restartButtonAction(ActionEvent actionEvent) {
        restartGame();
    }

    public void handleMove(KeyEvent keyEvent) {
        String direction = null;
        switch (keyEvent.getCode()) {
            case Z -> direction = "UP";
            case Q -> direction = "LEFT";
            case S -> direction = "DOWN";
            case D -> direction = "RIGHT";
        }

        if (direction != null && appClient != null) {
            appClient.sendToServer(direction);
        }
    }

    public void setAppClient(AppClient appClient) {
        this.appClient = appClient;
    }

    public void updateGameState(String serverMessage) {
        Platform.runLater(() -> {
            // Parse serverMessage and update UI accordingly
            if (serverMessage.startsWith("VALIDMOVE")) {
                handleValidMove(serverMessage);
            } else if (serverMessage.startsWith("GAME_START")) {
                // Initialize game start
            } else if (serverMessage.startsWith("GAME_END")) {
                handleGameEnd(serverMessage);
            } else if (serverMessage.equals("INVALIDMOVE")) {
                // Handle invalid move
            }
            // Additional message types can be handled here
        });
    }

    private void handleValidMove(String message) {
        // Example: VALIDMOVE:GOLD:X:Y ou VALIDMOVE:EMPTY:X:Y
        String[] parts = message.split(":");
        if (parts.length > 3) {
            String item = parts[1];
            int column = Integer.parseInt(parts[2]);
            int row = Integer.parseInt(parts[3]);

            // Met à jour la position du joueur
            this.column = column; // Assurez-vous que ces variables existent et sont utilisées pour tracer le joueur
            this.row = row;

            switch (item) {
                case "GOLD":
                    int currentScore = Integer.parseInt(score.getText());
                    score.setText(String.valueOf(++currentScore));
                    gridView.updateGoldAt(column, row, false); // Supposer que cela efface l'or
                    break;
                case "EMPTY":
                    // Pas de mise à jour spécifique nécessaire autre que le déplacement du joueur
                    break;
            }
            gridView.repaint();
            gridView.paintToken(this.column, this.row); // Dessiner le joueur à la nouvelle position
        }
    }


    private void handleGameEnd(String message) {
        String[] parts = message.split(" ");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] playerScore = part.split(":");
                String playerName = playerScore[0];
                String scoreValue = playerScore[1];
                Platform.runLater(() -> {
                    score.setText(scoreValue);
                    showDialog("Game Over", "Final score for " + playerName + ": " + scoreValue);
                });
            }
        }
    }


    private void restartGame() {
        score.setText("0");
        // Potentiellement, demander l'état initial au serveur
        appClient.sendToServer("GAME_RESTART");
        // Réinitialisation de la position du joueur et mise à jour de l'interface
        column = 10; // Valeur d'exemple, devrait être définie par la réponse du serveur
        row = 10;    // Valeur d'exemple, devrait être définie par la réponse du serveur
        gridView.repaint();
        gridView.paintToken(column, row);
    }

    // Méthode pour afficher un message de dialogue
    private void showDialog(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de texte d'en-tête
        alert.setContentText(content);
        alert.showAndWait();
    }



}
