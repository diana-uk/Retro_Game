package com.example.hw2updated.logic;

import com.example.hw2updated.data.Record;
import com.example.hw2updated.data.RecordsDao;
import com.example.hw2updated.models.CoinObject;
import com.example.hw2updated.models.CollisionType;
import com.example.hw2updated.models.EnemyObject;
import com.example.hw2updated.models.FugitiveObject;
import com.example.hw2updated.models.GameBoard;
import com.example.hw2updated.models.GameBoardObject;
import com.example.hw2updated.models.GameType;
import com.example.hw2updated.models.MoveDirection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameManager {

    private final int MAX_LIVES = 3;

    private int score = 0;
    private int lives = MAX_LIVES;

    private FugitiveObject fugitivePlayer;
    private EnemyObject enemyPlayer;
    private CoinObject coinObject;
    private GameBoard gameBoard;
    private CollisionType currentCollisionType;
    private GameType currentGameType;

    private Record currentRecord;
    private RecordsDao recordsDao;


    public GameManager() {
        fugitivePlayer = new FugitiveObject ();
        enemyPlayer = new EnemyObject ();
        coinObject = new CoinObject ();
        gameBoard = new GameBoard ();
        recordsDao=new RecordsDao ();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getMaxLives() {
        return MAX_LIVES;
    }

    public int getMatRows() {
        return gameBoard.getMatRows();
    }

    public int getMatCols() {
        return gameBoard.getMatCols();
    }

    public int getCurrentFugitivePositionRow() {
        return fugitivePlayer.getCurrentPositionRow();
    }

    public int getCurrentFugitivePositionCol() {
        return fugitivePlayer.getCurrentPositionCol();
    }

    public int getCurrentEnemyPositionRow() {
        return enemyPlayer.getCurrentPositionRow();
    }

    public int getCurrentEnemyPositionCol() {
        return enemyPlayer.getCurrentPositionCol();
    }

    public int getCurrentCoinPositionRow() {
        return coinObject.getCurrentPositionRow();
    }

    public int getCurrentCoinPositionCol() {
        return coinObject.getCurrentPositionCol();
    }

    public MoveDirection getCurrentFugitiveDirection() {
        return fugitivePlayer.getCurrentDirection ();
    }
    public GameType getCurrentGameType() {
        return currentGameType;
    }

    public GameManager setCurrentGameType(GameType currentGameType) {
        this.currentGameType = currentGameType;
        return this;
    }

    public void updateScore() {
        score++;
    }

    public void reduceLives() {
        lives--;
    }

    public GameManager setCurrentCollisionType(CollisionType currentCollisionType) {
        this.currentCollisionType = currentCollisionType;
        return this;
    }

    public void initPosition() {
        fugitivePlayer.setToStartingPosition();
        enemyPlayer.setToStartingPosition();
        coinObject.setToStartingPosition();
    }

    public void initCollisionType() {
        currentCollisionType=CollisionType.NONE;
    }

    public void updateFugitivePlayerDirectionData(MoveDirection moveDirection) {
        fugitivePlayer.savePreviousDirection(fugitivePlayer.getCurrentDirection ());
        fugitivePlayer.setCurrentDirection(moveDirection);
    }

    public void updateEnemyPlayerDirectionData() {
        Random r = new Random();
        int randomNumber = r.ints(1, 1, 5).findFirst().getAsInt();
        switch (randomNumber) {
            case 1:
                enemyPlayer.setCurrentDirection(MoveDirection.UP);
                break;

            case 2:
                enemyPlayer.setCurrentDirection(MoveDirection.DOWN);
                break;

            case 3:
                enemyPlayer.setCurrentDirection(MoveDirection.RIGHT);
                break;

            case 4:
                enemyPlayer.setCurrentDirection(MoveDirection.LEFT);
                break;
        }
    }

    public void updateGameData() {
        //**************************************Enemy Player**************************************
        //Pick a random number for the direction of the enemy player and update direction data
        updateEnemyPlayerDirectionData();
        //Update enemy player's position by the current position data
        updateEnemyPlayerPositionData(enemyPlayer);

        //**************************************Fugitive Player**************************************
        /*
        The direction of the fugitive player was already updated by the on click listener
        so we update only the position data
         */
        updateFugitivePlayerPositionData(fugitivePlayer);

        //update score
        updateScore();

        if(coinObject.checkIfCoinReplacement())
            coinObject.setCoinExistenceToStartTime ();
        else
            coinObject.updateCoinExistenceTime ();
    }

    private void updateFugitivePlayerPositionData(GameBoardObject fugitivePlayer) {
        if (!checkIfInBoundaries(fugitivePlayer))
            updateObjectPlayerPositionData(fugitivePlayer);
    }

    private void updateEnemyPlayerPositionData(GameBoardObject enemyPlayer) {
        if (!checkIfInBoundaries(enemyPlayer))
            updateObjectPlayerPositionData(enemyPlayer);
    }

    private boolean checkIfInBoundaries(GameBoardObject gameBoardObject) {
        MoveDirection moveDirection = gameBoardObject.getCurrentDirection();
        int currentRow = gameBoardObject.getCurrentPositionRow();
        int currentCol = gameBoardObject.getCurrentPositionCol();

        if (moveDirection.equals(MoveDirection.UP) && currentRow == 0 ||
                moveDirection.equals(MoveDirection.DOWN) && currentRow == gameBoard.getMatRows() - 1 ||
                moveDirection.equals(MoveDirection.RIGHT) && currentCol == gameBoard.getMatCols() - 1 ||
                moveDirection.equals(MoveDirection.LEFT) && currentCol == 0)
            return true;

        return false;
    }


    //The method is general to update position of any object player
    private void updateObjectPlayerPositionData(GameBoardObject gameBoardObject) {
        switch (gameBoardObject.getCurrentDirection()) {
            case UP:
                gameBoardObject.setCurrentPositionRow(gameBoardObject.getCurrentPositionRow() - 1);
                break;

            case DOWN: {
                gameBoardObject.setCurrentPositionRow(gameBoardObject.getCurrentPositionRow() + 1);
                break;
            }
            case RIGHT: {
                gameBoardObject.setCurrentPositionCol(gameBoardObject.getCurrentPositionCol() + 1);
                break;
            }

            case LEFT: {
                gameBoardObject.setCurrentPositionCol(gameBoardObject.getCurrentPositionCol() - 1);
                break;
            }
            case NONE:
                break;
        }
    }


    public boolean checkIfCollision() {
        int fugitiveRow = fugitivePlayer.getCurrentPositionRow();
        int fugitiveCol = fugitivePlayer.getCurrentPositionCol();
        int enemyRow = enemyPlayer.getCurrentPositionRow();
        int enemyCol = enemyPlayer.getCurrentPositionCol();

        return (fugitiveRow == enemyRow) && (fugitiveCol == enemyCol);
    }

    public void updateCollisionEventGameData() {
        reduceLives();
    }

    public boolean isGameOver() {
        return (lives <= 0);
    }

    public void updateCoinData() {
        Random r = new Random();
        int randomRow = r.ints(1, 0, gameBoard.getMatRows()).findFirst().getAsInt();
        int randomCol = r.ints(1, 0, gameBoard.getMatCols()).findFirst().getAsInt();
        coinObject.setCurrentPositionRow(randomRow);
        coinObject.setCurrentPositionCol(randomCol);
    }

    public boolean checkIfCoinCollision() {
        int coinRow = coinObject.getCurrentPositionRow();
        int coinCol = coinObject.getCurrentPositionCol();
        int fugitiveRow = fugitivePlayer.getCurrentPositionRow();
        int fugitiveCol = fugitivePlayer.getCurrentPositionCol();

        if (coinRow == fugitiveRow && coinCol == fugitiveCol)
            return true;

        return false;
    }

    public void updateCollisionCoinEventData() {
        updateScoreCoinCollect();
    }

    private void updateScoreCoinCollect() {
        score+=10;
    }

    public void findDirectionSensors(float directionX, float directionY) {
        if(directionX>4)  {
            updateFugitivePlayerDirectionData(MoveDirection.LEFT);
        }
        else if(directionX<-4) {
            updateFugitivePlayerDirectionData(MoveDirection.RIGHT);
        }
        else if(directionY<-2) {
            updateFugitivePlayerDirectionData(MoveDirection.UP);
        }
        else if(directionY>4){
            updateFugitivePlayerDirectionData(MoveDirection.DOWN);
        }

    }
    public void saveData() {

        currentRecord.setScore(score);
        currentRecord.setTime(getCurrentTime());

        // Read all records
        List<Record> records = recordsDao.readRecords();
        // Add the new record
        records.add(currentRecord);
        // Save all records
        recordsDao.saveRecords(records);
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(new Date());
    }

    public void setCurrentRecord(Record currentRecord) {
        this.currentRecord = currentRecord;
    }

    public boolean checkIfCoinReplacement() {
        return coinObject.checkIfCoinReplacement();
    }
}
