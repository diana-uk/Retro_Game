package com.example.hw2updated.models;

public class CoinObject extends GameBoardObject {
    private final int COIN_STARTING_POSITION_ROW = 1;
    private final int COIN_STARTING_POSITION_COLUMN = 1;

    public CoinObject() {
        super();
    }

    @Override
    int getStartingPositionRow() {
        return COIN_STARTING_POSITION_ROW;
    }

    @Override
    int getStartingPositionColumn() {
        return COIN_STARTING_POSITION_COLUMN;
    }
}
