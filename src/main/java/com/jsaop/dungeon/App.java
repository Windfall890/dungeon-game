package com.jsaop.dungeon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.jsaop.dungeon.Action.*;
import static com.jsaop.dungeon.BlockValues.*;


public class App extends Application {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static final int CELL_DIMENSION = 10;
    public static final boolean FOW_ENABLED = true;

    public static final long DEFAULT_FRAME_DELAY = 100;

    public static final Color FLOOR_COLOR = Color.DARKKHAKI;
    public static final Color PLAYER_COLOR = Color.BLUEVIOLET;
    public static final Color GOAL_COLOR = Color.BLUE;
    public static final Color ENEMY_COLOR = Color.LIMEGREEN;
    public static final Color UNEXPLORED_COLOR = Color.BLACK;
    public static final Color WALL_COLOR = Color.DARKSLATEGRAY;

    private Game game;
    private GridPane grid;
    private Text turn;
    private Text hp;
    private Timeline timeline;

    public static void main(String[] args) {
        launch(args);
    }

    private static void setColor(Rectangle r, Color color) {
        r.setFill(color);
        r.setStroke(color);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        game = new Game(WIDTH, HEIGHT);
        System.out.println(game.getDungeon().getMapAsString());

        primaryStage.setTitle("Dungeon Game");

        grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setPrefSize((CELL_DIMENSION * WIDTH) + WIDTH, (CELL_DIMENSION * HEIGHT) + HEIGHT);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                createGridCell(x, y);
            }
        }

        turn = new Text();
        hp = new Text();

        Pane infoPane = new FlowPane();
        infoPane.getChildren().addAll(turn, hp);
        infoPane.setPrefSize((CELL_DIMENSION * WIDTH) + WIDTH, turn.getScaleY());

        BorderPane root = new BorderPane();
        root.setBackground(Background.EMPTY);
        root.setTop(infoPane);
        root.setCenter(grid);

        //handle input
        Scene scene = new Scene(root);
        scene.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE)
                handleKeyCodeSpace();
            else if (event.getCode() == KeyCode.DOWN) {
                game.takeTurn(DOWN);
            } else if (event.getCode() == KeyCode.UP)
                game.takeTurn(UP);
            else if (event.getCode() == KeyCode.LEFT)
                game.takeTurn(LEFT);
            else if (event.getCode() == KeyCode.RIGHT) {
                game.takeTurn(RIGHT);
            }

        });

        //show window and start game loop
        primaryStage.setScene(scene);
        primaryStage.show();
        startAnimation();
    }

    private void handleKeyCodeSpace() {
    }

    private void createGridCell(int x, int y) {
        Rectangle r = new Rectangle();
        r.setWidth(CELL_DIMENSION);
        r.setHeight(CELL_DIMENSION);
        Label glyph = new Label(".");
        r.widthProperty().bind(glyph.widthProperty());
        r.heightProperty().bind(glyph.heightProperty());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(r,glyph);
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
        turn.setText("Turn: " + game.getTurn());
        hp.setText("    HP: " + game.getPlayer().getHp());
        if (game.hasWon())
            gameWin();
        if (game.getPlayer().isDead())
            gameLose();

    }

    private void gameLose() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("YOU LOSE!");
        alert.setHeaderText("You were zapped by a Guy!");
        alert.setContentText("I have a sad message for you: You are dead.");
        timeline.pause();
        alert.show();
    }

    private void gameWin() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText("WOW YOU WIN!");
        alert.setContentText("I have a great message for you: You have won!");
        timeline.pause();
        alert.show();

    }

    private void updateGrid() {
        for (int y = 0; y < HEIGHT; y++)
            for (int x = 0; x < WIDTH; x++)
                updateCell(x, y);
    }

    private void updateCell(int x, int y) {
        Rectangle r = getRectangle(x, y);
        Color color = UNEXPLORED_COLOR;
        if (FOW_ENABLED && !game.isExplored(x, y)) {
            color = UNEXPLORED_COLOR;
        } else {
            char tile = game.getMap()[x][y];
            if (tile == WALL.getValue()) {
                color = WALL_COLOR;
            } else if (tile == FLOOR.getValue()) {
                color = FLOOR_COLOR;
            } else if (tile == PLAYER.getValue()) {
                color = PLAYER_COLOR;
            } else if (tile == GOAL.getValue()) {
                color = GOAL_COLOR;
            } else if (tile == ENEMY.getValue()) {
                color = ENEMY_COLOR;
            }
        }

        Player player = game.getPlayer();
        if(!player.playerCanSee(x,y)){
            if(color == ENEMY_COLOR){
                color = FLOOR_COLOR; //hack to make enemies disappear
            }
            color = color.darker().darker();
        }
        setColor(r,color);
    }

    private Rectangle getRectangle(int x, int y) {
        return (Rectangle) ((StackPane) grid.getChildren().get(getIndex(x, y))).getChildren().get(0);
    }

    private int getIndex(int x, int y) {
        return y * WIDTH + x;
    }
}


