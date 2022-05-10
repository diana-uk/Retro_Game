package com.example.hw2updated.models;

public abstract class GameBoardObject {
    private int currentPositionRow;
    private int currentPositionCol;
    private MoveDirection previousDirection;
    private MoveDirection currentDirection;

    public GameBoardObject() {
        setToStartingPosition();
    }

    abstract int getStartingPositionRow();

    abstract int getStartingPositionColumn();

    public void setToStartingPosition() {
        setCurrentPositionRow(getStartingPositionRow());
        setCurrentPositionCol(getStartingPositionColumn());
        setCurrentDirection(MoveDirection.NONE);
    }

    public int getCurrentPositionRow() {
        return currentPositionRow;
    }

    public int getCurrentPositionCol() {
        return currentPositionCol;
    }

    public MoveDirection getCurrentDirection() {
        return currentDirection;
    }

    public GameBoardObject setCurrentPositionRow(int currentPositionRow) {
        this.currentPositionRow = currentPositionRow;
        return this;
    }

    public GameBoardObject setCurrentPositionCol(int currentPositionColumn) {
        this.currentPositionCol = currentPositionColumn;
        return this;
    }

    public GameBoardObject setCurrentDirection(MoveDirection currentDirection) {
        this.currentDirection = currentDirection;
        return this;
    }

    public void savePreviousDirection(MoveDirection moveDirection) {
        this.previousDirection=moveDirection;
    }
}
