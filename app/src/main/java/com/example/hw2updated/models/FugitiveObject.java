package com.example.hw2updated.models;

public class FugitiveObject extends GameBoardObject {

    private final int FUGITIVE_STARTING_POSITION_ROW = 0;
    private final int FUGITIVE_STARTING_POSITION_COLUMN = 2;

    public FugitiveObject() {
        super();
    }


    @Override
    int getStartingPositionRow() {
        return FUGITIVE_STARTING_POSITION_ROW;
    }

    @Override
    int getStartingPositionColumn() {
        return FUGITIVE_STARTING_POSITION_COLUMN;
    }
}
