package com.cs2105.swump.core.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cs2105.swump.core.Game;
import com.cs2105.swump.core.Puzzle;

public class SqlStorage implements Storage {
    // region fields

    private static SqlStorage instance;
    private String database = "jdbc:sqlite:swump.db";
    private int max_score_count = 10;

    // endregion

    // region constructors

    private SqlStorage() {
        initializeDb();
    }

    public static SqlStorage getInstance() {
        if (instance == null) {
            instance = new SqlStorage();
        }
        return instance;
    }

    // endregion

    // region public methods

    public int getNumberOfPuzzles(int difficulty) {
        if (difficulty < 0 || difficulty > 3)
            return 0;

        int num = 0;

        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();

            ResultSet rs = stat
                    .executeQuery("SELECT COUNT(*) AS rowCount FROM puzzle WHERE difficulty = " + difficulty + ";");
            rs.next();
            num = rs.getInt("rowCount");

            stat.close();
            rs.close();

            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return num;
    }

    public Puzzle retrievePuzzle(int difficulty) {
        if (difficulty == 3)
            return new Puzzle();

        Puzzle puzzle = null;

        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(
                    "SELECT * FROM puzzle WHERE difficulty = " + difficulty + " ORDER BY RANDOM() LIMIT 1;");
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return puzzle;
    }

    public Puzzle retrievePuzzleByID(int puzzleID) {
        Puzzle puzzle = null;

        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM puzzle WHERE id = " + puzzleID + ";");
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return puzzle;
    }

    public boolean addPuzzle(int[][] solution, int[][] givens, int difficulty) {

        try {
            Connection conn = DriverManager.getConnection(database);

            PreparedStatement prep = conn
                    .prepareStatement("INSERT INTO Puzzle (solution, givens, difficulty) VALUES (?, ?, ?)");

            conn.setAutoCommit(false);

            prep.setString(1, toString(solution));
            prep.setString(2, toString(givens));
            prep.setInt(3, difficulty);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deletePuzzle(long id) {

        try {
            Connection conn = DriverManager.getConnection(database);

            PreparedStatement prep = conn.prepareStatement("DELETE FROM puzzle WHERE id = ?;");

            conn.setAutoCommit(false);

            prep.setLong(1, id);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateScore(String player, int difficulty, long score) {
        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            PreparedStatement prep = conn
                    .prepareStatement("INSERT INTO Score (player, score, difficulty) VALUES (?, ?, ?);");

            prep.setString(1, player);
            prep.setInt(2, (int) score);
            prep.setInt(3, difficulty);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String[][][] retrieveScoreboard() {
        String[][][] scoreboard = new String[3][max_score_count][3];
        // String[EASY/ADV/HARD][TOP 10][PLAYERNAME/TIME/SCORE]

        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            scoreboard[0] = retrieveScoreboardPerDifficulty(0, conn);
            scoreboard[1] = retrieveScoreboardPerDifficulty(1, conn);
            scoreboard[2] = retrieveScoreboardPerDifficulty(2, conn);

            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoreboard;
    }

    private String[][] retrieveScoreboardPerDifficulty(int diff, Connection conn) throws SQLException {
        String[][] scoreboard = new String[max_score_count][3];
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM score WHERE difficulty = " + diff
                + " ORDER BY score,timestamp LIMIT " + max_score_count + ";");

        int i = 0;
        while (rs.next()) {
            String player = rs.getString("player");
            String timestamp = rs.getString("timestamp");
            String score = rs.getString("score");

            scoreboard[i % max_score_count][0] = player;
            scoreboard[i % max_score_count][1] = timestamp;
            scoreboard[i % max_score_count][2] = score;
            i++;
        }

        stat.close();
        rs.close();

        return scoreboard;
    }

    public boolean saveGame(String name, Puzzle puzzle, long timeElapsed) {

        try {
            Connection conn = DriverManager.getConnection(database);

            PreparedStatement prep = conn.prepareStatement(
                    "INSERT INTO game (name, pencil_marks, time_elapsed, puzzle_id) VALUES (?, ?, ?, ?, ?);");

            conn.setAutoCommit(false);

            prep.setString(1, name);
            prep.setString(2, toString(puzzle.getPencilMarks()));
            prep.setString(3, toString(puzzle.getUserAnswers()));
            prep.setInt(4, (int) timeElapsed);
            prep.setInt(5, (int) puzzle.getPuzzleID());
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Game loadGame(String name) {
        Puzzle puzzle = null;
        Game game;

        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery(
                    "SELECT pencil_marks,answers,time_elapsed,puzzle_id FROM game WHERE name = '" + name + "';");
            rs.next();
            String pencilMarks = rs.getString("pencil_marks");
            String userAnswers = rs.getString("answers");

            // how to tell logic-controller the time elapsed???????
            int timeElapsed = rs.getInt("time_elapsed");
            int puzzleID = rs.getInt("puzzle_id");

            stat.close();
            rs.close();
            conn.commit();
            conn.close();

            puzzle = retrievePuzzleByID(puzzleID);
            puzzle.setUserAnswers(to2DMatrix(userAnswers));
            puzzle.setPencilMarks(to3DMatrix(pencilMarks));

            game = new Game(puzzle, timeElapsed, name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return game;
    }

    public void deleteGame(String name) {

        try {
            Connection conn = DriverManager.getConnection(database);

            PreparedStatement prep = conn.prepareStatement("DELETE FROM game WHERE name = ?;");

            conn.setAutoCommit(false);

            prep.setString(1, name);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] loadGameList() {
        String[] savedGameList = null;

        try {
            Connection conn = DriverManager.getConnection(database);
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery("SELECT COUNT(*) AS rowCount FROM game;");
            rs.next();
            savedGameList = new String[rs.getInt("rowCount")];
            rs.close();

            rs = stat.executeQuery("SELECT name FROM game ORDER BY name;");
            int i = 0;
            while (rs.next()) {
                savedGameList[i] = rs.getString("name");
                i++;
            }

            stat.close();
            rs.close();

            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return savedGameList;
    }

    // endregion

    // region private methods

    private boolean initializeDb() {
        try {
            // ensure driver is present
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(database);
            Statement statement = conn.createStatement();

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS puzzle (id INTEGER PRIMARY KEY AUTOINCREMENT, difficulty INTEGER, solution TEXT, givens TEXT);");
            statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_puzzle_difficulty ON puzzle (difficulty);");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS score (id INTEGER PRIMARY KEY AUTOINCREMENT, difficulty INTEGER, player TEXT, score INTEGER, timestamp TEXT);");
            statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_score_difficulty ON score (difficulty);");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS game (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, answers TEXT, pencil_marks TEXT, time_elapsed TEXT, puzzle_id INTEGER);");
            statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_game_name ON game (name);");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String toString(int[][] matrix) {
        String str = "";
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                str = str.concat(String.valueOf(matrix[i][j]));

        return str;
    }

    private String toString(int[][][] matrix) {
        String str = "";
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                for (int k = 0; k < matrix[i][j].length; k++)
                    str = str.concat(String.valueOf(matrix[i][j][k]));
                str = str.concat(",");
            }
        }
        return str;
    }

    private int[][] to2DMatrix(String str) {
        int[][] matrix = new int[9][9];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = Integer.parseInt(str.charAt((9 * i) + j) + "");
        return matrix;
    }

    private int[][][] to3DMatrix(String str) {
        int[][][] matrix = new int[9][9][9];

        String[] thirdArrayStr = str.split(",");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int[] thirdArrayInt = new int[9];
                for (int k = 0; k < thirdArrayInt.length; k++)
                    thirdArrayInt[k] = Integer.parseInt(thirdArrayStr[(9 * i) + j].charAt(k) + "");
                matrix[i][j] = thirdArrayInt;
            }
        }
        return matrix;
    }

    // endregion
}
