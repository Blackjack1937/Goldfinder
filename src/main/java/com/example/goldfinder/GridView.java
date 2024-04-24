package com.example.goldfinder;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class GridView {
    private Canvas canvas;
    private int columnCount, rowCount;
    private boolean[][] goldAt, vWall, hWall;

    public GridView(Canvas canvas, int columnCount, int rowCount) {
        this.canvas = canvas;
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.goldAt = new boolean[columnCount][rowCount];
        this.vWall = new boolean[columnCount + 1][rowCount];
        this.hWall = new boolean[columnCount][rowCount + 1];

        // Initialisation par défaut, peut être remplacée par des données réelles du serveur
        initializeWallsAndGold();
    }

    private void initializeWallsAndGold() {
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                goldAt[i][j] = Math.random() < 0.1; // 10% de chance d'avoir de l'or
                vWall[i][j] = Math.random() < 0.2; // 20% de chance d'avoir un mur vertical
                hWall[i][j] = Math.random() < 0.2; // 20% de chance d'avoir un mur horizontal
            }
        }
    }

    public void updateGoldAt(int column, int row, boolean hasGold) {
        goldAt[column][row] = hasGold;
    }

    public void updateVWall(int column, int row, boolean hasWall) {
        vWall[column][row] = hasWall;
    }

    public void updateHWall(int column, int row, boolean hasWall) {
        hWall[column][row] = hasWall;
    }

    private double cellWidth() { return canvas.getWidth() / columnCount; }
    private double cellHeight() { return canvas.getHeight() / rowCount; }



    public void repaint(){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        for(int column =0; column<columnCount;column++)
            for(int row=0;row<rowCount;row++)
                if(goldAt[column][row]) {
                    canvas.getGraphicsContext2D().setFill(Color.YELLOW);
                    canvas.getGraphicsContext2D().fillOval(column * cellWidth(), row * cellHeight(), cellWidth(), cellHeight());
                }
        canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        for(int column =0; column<columnCount;column++)
            for(int row=0;row<rowCount;row++){
                    if(vWall[column][row])
                        canvas.getGraphicsContext2D().strokeLine(column * cellWidth(), row * cellHeight(),column * cellWidth(), (row+1) * cellHeight());
                if(hWall[column][row])
                    canvas.getGraphicsContext2D().strokeLine(column * cellWidth(), row * cellHeight(),(column+1) * cellWidth(), row * cellHeight());
            }

    }
    public void paintToken(int column, int row) {
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().fillRect(column*cellWidth(),row*cellHeight(),cellWidth(),cellHeight());
    }
}
