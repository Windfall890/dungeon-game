package com.jsaop.dungeon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.jsaop.dungeon.Directions.*;


public class App extends Application {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static final int CELL_DIMENSION = 10;

    public static final long DEFAULT_FRAME_DELAY = 100;

    private Game game;
    private GridPane grid;
    private Text turn;
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

        Pane infoPane = new FlowPane();
        infoPane.getChildren().addAll(turn);
        infoPane.setPrefSize((CELL_DIMENSION * WIDTH) + WIDTH, turn.getScaleY());

        BorderPane root = new BorderPane();
        root.setBackground(Background.EMPTY);
        root.setTop(infoPane);
        root.setCenter(grid);

        //handle input
        Scene scene = new Scene(root);
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE)
                handleKeyCodeSpace();
            else if (event.getCode() == KeyCode.DOWN)
                game.takeTurn(DOWN);
            else if (event.getCode() == KeyCode.UP)
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

        GridPane.setConstraints(r, x, y);
        grid.getChildren().add(r);
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
        if (game.hasWon())
            gameEnd();
    }

    private void gameEnd() {
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
        if (game.getMap()[x][y] == '#') {
            setColor(r, Color.DARKSLATEGRAY);
        } else if (game.getMap()[x][y] == '.') {
            setColor(r, Color.BEIGE);
        } else if (game.getMap()[x][y] == '@') {
            setColor(r, Color.BLUEVIOLET);
        } else if (game.getMap()[x][y] == '*') {
            setColor(r, Color.BLUE);
        }
    }

    private Rectangle getRectangle(int x, int y) {
        return (Rectangle) grid.getChildren().get(getIndex(x, y));
    }

    private int getIndex(int x, int y) {
        return y * WIDTH + x;
    }
}


