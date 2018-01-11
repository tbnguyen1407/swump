package com.cs2105.swump.core.storage;

import com.cs2105.swump.core.Game;
import com.cs2105.swump.core.Puzzle;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage
{
    private static final String DATABASE = "jdbc:sqlite:db/sudoku.db";
    private static final int MAX_SCORE_COUNT = 10;
    public static final int GENERATE_NEW_SEQ = 9999;

    private static class Util
    {
        private static String toString(int[][] matrix)
        {
            String str = "";
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[i].length; j++)
                    str = str.concat(String.valueOf(matrix[i][j]));

            return str;
        }

        private static String toString(int[][][] matrix)
        {
            String str = "";
            for (int i = 0; i < matrix.length; i++)
            {
                for (int j = 0; j < matrix[i].length; j++)
                {
                    for (int k = 0; k < matrix[i][j].length; k++)
                        str = str.concat(String.valueOf(matrix[i][j][k]));
                    str = str.concat(",");
                }
            }
            return str;
        }

        private static int[][] to2DMatrix(String str)
        {
            int[][] matrix = new int[9][9];

            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[i].length; j++)
                    matrix[i][j] = Integer.parseInt(str.charAt((9 * i) + j) + "");
            return matrix;
        }

        private static int[][][] to3DMatrix(String str)
        {
            int[][][] matrix = new int[9][9][9];

            String[] thirdArrayStr = str.split(",");

            for (int i = 0; i < matrix.length; i++)
            {
                for (int j = 0; j < matrix[i].length; j++)
                {
                    int[] thirdArrayInt = new int[9];
                    for (int k = 0; k < thirdArrayInt.length; k++)
                        thirdArrayInt[k] = Integer.parseInt(thirdArrayStr[(9 * i) + j].charAt(k) + "");
                    matrix[i][j] = thirdArrayInt;
                }
            }
            return matrix;
        }
    }

    private static class StorageHolder
    {
        private static final Storage INSTANCE = new Storage();
    }

    private Storage()
    {
    }

    public static Storage getInstance()
    {
        return StorageHolder.INSTANCE;
    }

    public int getNumberOfPuzzles(int difficulty)
    {
        if (difficulty < 0 || difficulty > 3)
            return 0;

        int num = 0;

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery("SELECT COUNT(*) AS ROWS FROM PUZZLE WHERE DIFFICULTY=" + difficulty + ";");
            rs.next();
            num = rs.getInt("ROWS");

            stat.close();
            rs.close();

            conn.commit();
            conn.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        return num;
    }

    /*
       Operation: retrievePuzzle(int difficulty): Puzzle
       Parameter: Difficulty can only have 3 different values - Easy(0), Advance(1) and Hard(2).
       Description: Retrieve a random puzzle from the database based on the difficulty level.
       Algo:
       1. Get first puzzle from the db, filter by difficulty.
       2. All data (puzzleID for the puzzle will be retrieved from the db.
       3. Create puzzle object
       Precondition: none
       Post condition: A puzzle based on the difficulty level will be returned.
    */

    public Puzzle retrievePuzzle(int difficulty)
    {
        if (difficulty == 3)
            return new Puzzle();

        Puzzle puzzle = null;

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM PUZZLE WHERE DIFFICULTY=" + difficulty + " AND ID>1808 ORDER BY RANDOM() LIMIT 1;");
            rs.next();

            long id = rs.getLong("id");
            int diff = rs.getInt("difficulty");
            String solution = rs.getString("solution");
            String givens = rs.getString("givens");

            puzzle = new Puzzle(id, diff, solution, givens);

            stat.close();
            rs.close();
            conn.commit();
            conn.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return puzzle;
    }

    /*
        Operation: retrievePuzzleByID(int puzzleID): Puzzle
        Parameter: Puzzle's unique identification number
        Description: Retrieve puzzle from the database based on its unique id.
        Algo:
        1. Get first puzzle from the db by id.
        2. All data (puzzleID for the puzzle will be retrieved from the db.
        3. Create puzzle object
        Precondition: none
        Post condition: A puzzle based on unique id will be returned.
    */

    public Puzzle retrievePuzzleByID(int puzzleID)
    {
        Puzzle puzzle = null;

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM PUZZLE WHERE ID=" + puzzleID + ";");
            rs.next();

            long id = rs.getLong("id");
            int diff = rs.getInt("difficulty");
            String solution = rs.getString("solution");
            String givens = rs.getString("givens");

            puzzle = new Puzzle(id, diff, solution, givens);

            stat.close();
            rs.close();
            conn.commit();
            conn.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return puzzle;
    }

    /*
       Operation: addPuzzle(int[][] solution, int[][] givens, int difficulty): boolean
       Parameter: Difficulty can only have 3 different values - Easy(0), Advance(1) and Hard(2).
       Description: Retrieve a random puzzle from the database based on the difficulty level.
       Algo:
       1. Get first puzzle from the db, filter by difficulty.
       2. All data (puzzleID for the puzzle will be retrieved from the db.
       3. Create puzzle object
       Precondition: none
       Post condition: A puzzle based on the difficulty level will be returned.
    */

    public boolean addPuzzle(int[][] solution, int[][] givens, int difficulty)
    {

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);

            PreparedStatement prep = conn.prepareStatement("INSERT INTO PUZZLE (SOLUTION,GIVENS,DIFFICULTY) VALUES (?, ?, ?)");

            conn.setAutoCommit(false);

            prep.setString(1, Util.toString(solution));
            prep.setString(2, Util.toString(givens));
            prep.setInt(3, difficulty);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();

            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /*
    Operation: deletePuzzle(long id): boolean
    Parameter: Puzzle id
    Description: Deletes the specified puzzle from the database
    Precondition: Puzzle is solved by player
    Post condition: The specified puzzle is deleted from the database.
 */

    public void deletePuzzle(long id)
    {

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);

            PreparedStatement prep = conn.prepareStatement("DELETE FROM PUZZLE WHERE ID=?;");

            conn.setAutoCommit(false);

            prep.setLong(1, id);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
       Operation: updateScore(String playerName, int difficulty, Long score): boolean
       Parameter: playerName represents the player's name, difficulty represents the difficulty level of the game play, score is the timing that player used to complete the puzzle
       Description: This method is to update the score after the player
       Precondition: Player must completes the puzzle.
       Post condition: Record of the game will be saved to the database.
       Algo:
       1. Add record into scoreTable
    */

    public boolean updateScore(String playerName, int difficulty, long score)
    {

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);

            PreparedStatement prep = conn.prepareStatement("INSERT INTO SCORE VALUES (?, DATETIME('now','localtime'), ?, ?);");

            conn.setAutoCommit(false);

            prep.setString(1, playerName);
            prep.setInt(2, (int) score);
            prep.setInt(3, difficulty);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();

            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /*
       Operation: retrieveScoreboard()
       Parameter: None
       Description: Player choose to view the scoreboard.
       Precondition: None
       Post: Scoreboard will be displayed.
       Algo:
       1. Get all records from score table (table stores only top 10 for each difficulty)
       2. sort in descending order
    */

    public String[][][] retrieveScoreboard()
    {
        String[][][] scoreboard = new String[3][MAX_SCORE_COUNT][3];
        //String[EASY/ADV/HARD][TOP 10][PLAYERNAME/TIME/SCORE]

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);
            conn.setAutoCommit(false);

            scoreboard[0] = pullScoreboard(0, conn);
            scoreboard[1] = pullScoreboard(1, conn);
            scoreboard[2] = pullScoreboard(2, conn);

            conn.commit();
            conn.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return scoreboard;
    }

    private String[][] pullScoreboard(int diff, Connection conn) throws SQLException
    {
        String[][] scoreboard = new String[MAX_SCORE_COUNT][3];
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM SCORE WHERE DIFFICULTY=" + diff + " ORDER BY SCORE,TIMESTAMP LIMIT " + MAX_SCORE_COUNT + ";");

        int i = 0;
        while (rs.next())
        {
            String playerName = rs.getString("PLAYERNAME");
            String timeStamp = rs.getString("TIMESTAMP");
            String score = rs.getString("SCORE");

            scoreboard[i % MAX_SCORE_COUNT][0] = playerName;
            scoreboard[i % MAX_SCORE_COUNT][1] = timeStamp;
            scoreboard[i % MAX_SCORE_COUNT][2] = score;
            i++;
        }

        stat.close();
        rs.close();

        return scoreboard;
    }

    /*
       Operation: saveGame(String gameName, Puzzle puzzle, long timeElapsed): boolean
       Parameter: gameName is the name chosen by player in order to recall the saved game, Puzzle store the game state at which the player save the game.
       Precondition: A game must be in play.
       Post condition: The game will be saved into the database.
       Algo:
       1. Timestamp is autogenerated for PK.
       2. Save into DB savedGame table(include pencil marks, answers, puzzle ID, time stamp, time elapsed)
    */

    public boolean saveGame(String gameName, Puzzle puzzle, long timeElapsed)
    {

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);

            PreparedStatement prep = conn.prepareStatement("INSERT INTO SAVEDGAME VALUES (?, ?, ?, DATETIME('now','localtime'), ?, ?);");

            conn.setAutoCommit(false);

            prep.setString(1, gameName);
            prep.setString(2, Util.toString(puzzle.getPencilMarks()));
            prep.setString(3, Util.toString(puzzle.getUserAnswers()));
            prep.setInt(4, (int) timeElapsed);
            prep.setInt(5, (int) puzzle.getPuzzleID());
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();

            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /*
       Operation: loadGame(String name): Puzzle
       Parameter: name of the game to be loaded
       Description: Load a previously saved game state
       Precondition: none
       Postcondition: Specified game state is loaded
       Algo:
       1. get saved game from savedGame table
       2. (include pencil marks, answers, puzzle ID, time elapsed)
    */

    public Game loadGame(String name, String timeStamp)
    {
        Puzzle puzzle = null;
        Game game;

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery("SELECT PENCILMARKS,ANSWERS,TIMEELAPSED,PUZZLEID FROM SAVEDGAME WHERE GAMENAME='" + name + "' AND TIMESTAMP=DATETIME('" + timeStamp + "');");
            rs.next();
            String pencilMarks = rs.getString("PENCILMARKS");
            String userAnswers = rs.getString("ANSWERS");

            // how to tell logic-controller the time elapsed???????
            int timeElapsed = rs.getInt("TIMEELAPSED");
            int puzzleID = rs.getInt("PUZZLEID");

            stat.close();
            rs.close();
            conn.commit();
            conn.close();

            puzzle = retrievePuzzleByID(puzzleID);
            puzzle.setUserAnswers(Util.to2DMatrix(userAnswers));
            puzzle.setPencilMarks(Util.to3DMatrix(pencilMarks));

            game = new Game(puzzle, timeElapsed, name, timeStamp);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return game;
    }

    /*
       Operation: deleteGame(String name, String timeStamp): Puzzle
       Parameter: name of the game to be deleted
       Description: Delete a previously saved game state
       Precondition: none
       Postcondition: Saved game is deleted
    */

    public void deleteGame(String name, String timeStamp)
    {

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);

            PreparedStatement prep = conn.prepareStatement("DELETE FROM SAVEDGAME WHERE GAMENAME=? AND TIMESTAMP=?;");

            conn.setAutoCommit(false);

            prep.setString(1, name);
            prep.setString(2, timeStamp);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
       Operation: loadGameList():String[listOfGames][gameName & timeStamp]
       Parameter: none
       Description: Get the list of saved games
       Precondition: none
       Postcondition: List of saved game is retrieved
       Algo:
       1. Return list of all save games
    */

    public String[][] loadGameList()
    {
        String[][] savedGameList = null;

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DATABASE);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery("SELECT COUNT(*) AS ROWS FROM SAVEDGAME;");
            rs.next();
            savedGameList = new String[rs.getInt("ROWS")][2];
            rs.close();

            rs = stat.executeQuery("SELECT TIMESTAMP,GAMENAME FROM SAVEDGAME ORDER BY TIMESTAMP, GAMENAME;");
            int i = 0;
            while (rs.next())
            {
                savedGameList[i][0] = rs.getString("GAMENAME");
                savedGameList[i][1] = rs.getString("TIMESTAMP");
                i++;
            }

            stat.close();
            rs.close();

            conn.commit();
            conn.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return savedGameList;
    }
}
