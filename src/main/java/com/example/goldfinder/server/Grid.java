package com.example.goldfinder.server;

import java.util.Random;

public class Grid {
    boolean[][] hWall, vWall, gold;
    int columnCount, rowCount;

    private final Random random;
    public Grid(int columnCount, int rowCount, Random random) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.random = random;

        RandomMaze randomMaze = new RandomMaze(columnCount,rowCount,.1, random);
        randomMaze.generate();
        hWall = randomMaze.hWall;
        vWall = randomMaze.vWall;

        gold = new boolean [columnCount][rowCount];
        generateGold(3);
    }

    private void generateGold(double v) {
        for(int column=0; column<columnCount; column++)
            for(int row=0;row<rowCount; row++)
                gold[column][row]=(random.nextInt(10)<v);
    }

    boolean leftWall(int column, int row){
        if (column==0) return true;
        return vWall[column][row];
    }

    boolean rightWall(int column, int row){
        if (column==columnCount-1) return true;
        return vWall[column+1][row];
    }

    boolean upWall(int column, int row){
        if (row==0) return true;
        return hWall[column][row];
    }

    boolean downWall(int column, int row){
        if (row==rowCount-1) return true;
        return hWall[column][row+1];
    }

    boolean hasGold(int column, int row){
        return gold[column][row];
    }

}
