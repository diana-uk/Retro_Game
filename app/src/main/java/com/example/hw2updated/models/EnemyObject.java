package com.example.hw2updated.models;

public class EnemyObject extends GameBoardObject {

    private final int PLAYER_STARTING_POSITION_ROW = 6;
    private final int PLAYER_STARTING_POSITION_COLUMN = 2;

    public EnemyObject() {
        super();
    }

    @Override
    int getStartingPositionRow() {
        return PLAYER_STARTING_POSITION_ROW;
    }

    @Override
    int getStartingPositionColumn() {
        return PLAYER_STARTING_POSITION_COLUMN;
    }
}
