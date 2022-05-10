package com.example.hw2updated.models;

public class CoinObject extends GameBoardObject {
    private final int COIN_STARTING_POSITION_ROW = 1;
    private final int COIN_STARTING_POSITION_COLUMN = 1;

    private int coinExistenceTime=0;
    private  boolean ifCoinReplacement=false;

    public CoinObject() {
        super();
        setCoinExistenceToStartTime();
    }

    public void setCoinExistenceToStartTime() {
        this.coinExistenceTime=0;
    }

    public void updateCoinExistenceTime() {
        this.coinExistenceTime++;
    }

    public int  getCoinExistenceTime() {
        return this.coinExistenceTime;
    }

    public boolean checkIfCoinReplacement() {
        if(getCoinExistenceTime ()==10)
          return this.ifCoinReplacement=true;

        return this.ifCoinReplacement=false;
    }

    @Override
    int getStartingPositionRow() {
        return COIN_STARTING_POSITION_ROW;
    }

    @Override
    int getStartingPositionColumn() {
        return COIN_STARTING_POSITION_COLUMN;
    }


    public boolean ifCoinReplacement() {
        return ifCoinReplacement;
    }
}
