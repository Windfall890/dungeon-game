package com.jsaop.dungeonGame.gui;

import com.jsaop.dungeonGame.dungeon.Level;
import com.jsaop.dungeonGame.entity.Enemy;
import com.jsaop.dungeonGame.entity.Entity;
import com.jsaop.dungeonGame.entity.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

import static com.jsaop.dungeonGame.dungeon.Action.*;
import static com.jsaop.dungeonGame.dungeon.BlockValues.*;


public class App extends Application {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 31;
    public static final int CELL_DIMENSION = 14;
    public static final long DEFAULT_FRAME_DELAY = 100;

    public static final Color FLOOR_COLOR = Color.DARKKHAKI.darker();
    public static final Color PLAYER_COLOR = Color.GOLD;
    public static final Color GOAL_COLOR = Color.BLUE;
    public static final Color ENEMY_COLOR = Color.LIMEGREEN;
    public static final Color UNEXPLORED_COLOR = Color.BLACK;
    public static final Color WALL_BG_COLOR = Color.DARKSLATEGRAY;
    private static final int WIN_LEVEL = 15;
    public static final int RADAR_PING_DURATION = 5;


    public static Random random = new Random();
    public boolean fogOfWarEnabled = true;
    private int radarPings = RADAR_PING_DURATION;
    private int radarUse;
    private int flashCounter = 0;
    private Level level;
    private GridPane grid;
    private Text turnText;
    private Text hpText;
    private Text radarText;
    private Timeline timeline;
    private TextArea console;
    private int currentLevel = 1;
    private Text levelText;

    private DialogConsole consoleOut;

    private SoundManager soundManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Dungeon Level");

        //grid
        grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setPrefSize((CELL_DIMENSION * WIDTH) + WIDTH, (CELL_DIMENSION * HEIGHT) + HEIGHT);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                createGridCell(x, y);
            }
        }

        //INFO
        FlowPane infoPane = new FlowPane();
        infoPane.setHgap(20);

        turnText = new Text();
        hpText = new Text();
        radarText = new Text();
        levelText = new Text("Level: " + currentLevel);
        infoPane.getChildren().addAll(new HBox(turnText), new HBox(hpText), new HBox(radarText), new HBox(levelText));
        infoPane.setPrefSize((CELL_DIMENSION * WIDTH) + WIDTH, turnText.getScaleY());


        //CONSOLE
        console = new TextArea("Use the Arrow keys/WASD to move\n(R) for Radar Ping\nEscape to the stairs!\nGood Luck!\n");
        console.setStyle("-fx-control-inner-background:" + colorToHex(WALL_BG_COLOR) + "; " +
                "-fx-font-family: Consolas; " +
                "-fx-highlight-text-fill: #000000; " +
                "-fx-text-fill: " + colorToHex(PLAYER_COLOR) + ";" +
                "-fx-padding: 0 0 50 0");
        console.setPrefWidth(CELL_DIMENSION * WIDTH);
        console.setEditable(false);

        //PUT IT IN THE POT MMMM TASTEY
        BorderPane root = new BorderPane();
        root.setBackground(Background.EMPTY);
        root.setTop(infoPane);
        root.setCenter(grid);
        root.setBottom(console);

        //handle input
        Scene scene = new Scene(root);
        scene.setOnKeyReleased(this::handleEvents);

        //start audio
        soundManager = new SoundManager();
        soundManager.ambiance.play();

        //show window and start level loop
        resetGame();
        primaryStage.setScene(scene);
        primaryStage.show();
        startAnimation();
    }


    private void handleEvents(KeyEvent event) {

        if (radarPings > 0) {
            radarPings--;
            if (radarPings <= 0) {
                consoleOut.write("Your radar hums and shuts off");
            }
        }

        switch (event.getCode()) {
            case BACK_QUOTE:
                handleDebugMode();
                break;
            case Q:
                resetGame();
                break;
            case CLOSE_BRACKET:
                nextLevel();
                break;
            case OPEN_BRACKET:
                previousLevel();
                break;
            case Z:
                level.takeTurn(WAIT);
                break;
            case DOWN:
            case S:
                level.takeTurn(DOWN);
                break;
            case UP:
            case W:
                level.takeTurn(UP);
                break;
            case LEFT:
            case A:
                level.takeTurn(LEFT);
                break;
            case RIGHT:
            case D:
                level.takeTurn(RIGHT);
                break;
            case R:
                if (radarUse > 0) {
                    radarUse--;
                    flashCounter = 2;
                    radarPings = RADAR_PING_DURATION;
                    soundManager.ping.play();
                    consoleOut.write("You ping your surroundings. " + radarPings + " turns remaining.");
                }
                if (radarUse <= 0) {
                    consoleOut.write("Your Radar goes dim as its power fades.");
                }
                level.takeTurn(WAIT);
                break;
        }
    }


    private static String colorToHex(Color color) {
        String s = color.toString();
        return "#" + s.substring(2);
    }

    private static void setTile(StackPane pane, Color bg, Color fg, char c) {
        Rectangle r = (Rectangle) pane.getChildren().get(0);
        r.setFill(bg);
        r.setStroke(bg);

        Label l = (Label) pane.getChildren().get(1);
        l.setText(c + "");
        l.setTextFill(fg);
    }

    private void resetGame() {
        changeLevel(1);
    }

    private void nextLevel() {
        soundManager.doorClose.play();
        changeLevel(currentLevel + 1);
    }


    private void previousLevel() {
        if (currentLevel > 1)
            changeLevel(currentLevel - 1);
    }

    private void changeLevel(int newLevel) {
        this.currentLevel = newLevel;
        radarUse = 1;
        radarPings = 0;
        levelText.setText("Level: " + currentLevel);
        consoleOut = new DialogConsole();
        consoleOut.write(" --- Level: " + currentLevel + " ---");

        level = new Level(WIDTH, HEIGHT, currentLevel, consoleOut);

        consoleOut.write("You feel refreshed and have " + level.getPlayer().getHp() + " health");

        int numberEnemies = level.getNumberEnemies();
        if (numberEnemies == 1)
            consoleOut.write("There is " + numberEnemies + " enemy");
        else
            consoleOut.write("There are " + numberEnemies + " enemies");
    }


    private void handleDebugMode() {
        fogOfWarEnabled = !fogOfWarEnabled;
    }

    private void createGridCell(int x, int y) {
        Rectangle r = new Rectangle();
        Label glyph = new Label("" + WALL.getValue());
        glyph.setPrefSize(CELL_DIMENSION, CELL_DIMENSION);
        r.widthProperty().bind(glyph.widthProperty());
        r.heightProperty().bind(glyph.heightProperty());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(r, glyph);
        GridPane.setConstraints(stackPane, x, y);

        grid.getChildren().add(stackPane);
    }

    private void startAnimation() {
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> updateAnimation()),
                new KeyFrame(Duration.millis(DEFAULT_FRAME_DELAY))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    private void updateAnimation() {
        updateGrid();
        turnText.setText("Turn: " + level.getTurn());
        hpText.setText("HP: " + level.getPlayer().getHp());
        radarText.setText("(R)adar: " + radarUse);
        if (radarPings > 0) radarText.setText(radarText.getText() + "Active for " + radarPings);

        updateConsole();

        if (currentLevel > WIN_LEVEL)
            gameWin();
        if (level.hasWon())

            nextLevel();
        if (level.getPlayer().isDead())
            gameLose();

    }

    private void updateConsole() {
        console.appendText(consoleOut.readAndFlush());
    }

    private void gameLose() {
        soundManager.ambiance.pause();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("You were zapped.");
        alert.setContentText("You reached the " + currentLevel + " currentLevel.");
        timeline.pause();
        soundManager.gameOver.play();
        alert.show();
        resetGame();
        soundManager.ambiance.play();
        timeline.play();
    }

    private void gameWin() {
        timeline.pause();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText("WOW YOU WIN!");
        alert.setContentText("I have a great message for you: You have won!");
        alert.show();
        soundManager.gameWin.play();
        resetGame();
        timeline.play();
    }

    private void updateGrid() {
        for (int y = 0; y < HEIGHT; y++)
            for (int x = 0; x < WIDTH; x++)
                updateCell(x, y);


        if (flashCounter > 0) flashCounter--;
        updateEntities();
    }

    private void updateCell(int x, int y) {
        StackPane pane = getPane(x, y);
        Color bgColor = UNEXPLORED_COLOR;
        Color fgColor = UNEXPLORED_COLOR;
        char glyph = level.getMap()[x][y];


        if (fogOfWarEnabled && !level.isExplored(x, y)) {
            setTile(pane, UNEXPLORED_COLOR, UNEXPLORED_COLOR, FLOOR.getValue());
            return;
        } else {
            if (glyph == WALL.getValue()) {
                bgColor = WALL_BG_COLOR;
                fgColor = Color.DARKCYAN.darker().darker().desaturate();
            } else if (glyph == FLOOR.getValue()) {
                bgColor = FLOOR_COLOR;
                fgColor = FLOOR_COLOR.darker().darker().desaturate();
            }
        }

        if (flashCounter > 0) {
            fgColor = Color.LIMEGREEN.desaturate();
            bgColor = UNEXPLORED_COLOR;
            glyph = randomGlyph();
            setTile(pane, bgColor, fgColor, glyph);
            return;
        }

        Player player = level.getPlayer();
        if (!player.playerCanSee(x, y) && fogOfWarEnabled) {
            bgColor = bgColor.darker().darker();
        }
        setTile(pane, bgColor, fgColor, glyph);


    }

    private char[] noiseGlyphs = new char[]{'#', '%', '*', '>', '<'};

    private char randomGlyph() {
        return noiseGlyphs[random.nextInt(noiseGlyphs.length)];
    }

    private void updateEntities() {
        for (Entity e : level.getEntities()) {
            if (level.getPlayer().playerCanSee(e.getX(), e.getY())) {
                renderEntity(e);
            } else if (!fogOfWarEnabled) {
                renderEntity(e);
            } else if (radarPings > 0 && e.getClass() == Enemy.class) {
                renderEntity(e);
            }

        }
    }

    private void renderEntity(Entity e) {
        StackPane pane = getPane(e.getX(), e.getY());
        char glyph = e.getGlyph();
        Color bgColor = Color.TRANSPARENT;
        Color fgColor = Color.TRANSPARENT;
        if (glyph == PLAYER.getValue()) {
            bgColor = FLOOR_COLOR;
            fgColor = PLAYER_COLOR;
        } else if (glyph == GOAL.getValue()) {
            bgColor = GOAL_COLOR;
            fgColor = Color.CORNFLOWERBLUE;
        } else if (glyph == ENEMY.getValue()) {
            bgColor = ENEMY_COLOR;
            fgColor = Color.FIREBRICK.saturate();
        }
        setTile(pane, bgColor, fgColor, e.getGlyph());
    }

    private StackPane getPane(int x, int y) {
        return (StackPane) grid.getChildren().get(getIndex(x, y));
    }

    private int getIndex(int x, int y) {
        return y * WIDTH + x;
    }
}


