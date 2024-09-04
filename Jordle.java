import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This file defines a word guessing game using javafx.
 *
 * @author agopal45
 * @version 1.0
 */
public class Jordle extends Application{
    private Backend backend = new Backend();
    private String target = backend.getTarget();
    private TextField[][] wordBank = new TextField[6][5];
    private int wordIndex = 0;
    private int letterIndex = 0;

@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jordle");

        //Intro Pane
        StackPane pane = new StackPane();
        Image image = new Image("jordleImage.jpg", 800, 800, true, true);
        ImageView imageView = new ImageView(image);
        Text text = new Text("\n\n\n\n\n\nJordle");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("trebuchet", FontWeight.BOLD, 36));
        Button playBtn = new Button("PLAY");
        StackPane.setMargin(playBtn, new Insets(8,8,8,8));
        pane.getChildren().addAll(imageView, text, playBtn);
        pane.setAlignment((Node) playBtn, Pos.BOTTOM_CENTER);

        primaryStage.setHeight(800);
        primaryStage.setWidth(800);
        primaryStage.setScene(new Scene(pane));

        //Game Pane
        BorderPane gamePane = new BorderPane();
        Text header = new Text("JORDLE");
        header.setFont(Font.font("helvetica", FontWeight.BOLD, 36));
        BorderPane.setAlignment(header, Pos.TOP_CENTER);
        BorderPane.setMargin(header, new Insets(28, 28, 28, 28));
        gamePane.setTop(header);

        gamePane.setCenter(resetWordBank());

        HBox toolboxPane = new HBox();
        Button restartBtn = new Button("Restart");
        Button instructBtn = new Button("Instructions");
        toolboxPane.setAlignment(Pos.CENTER);
        toolboxPane.setSpacing(10);
        toolboxPane.getChildren().addAll(new Text("Enter a guess!"), restartBtn, instructBtn);
        BorderPane.setMargin(toolboxPane, new Insets(12, 12, 12, 12));
        gamePane.setBottom(toolboxPane);

        //Instruction Stage
        Stage instructionStage = new Stage();
        instructionStage.setTitle("Instructions");
        instructionStage.setMinWidth(600);
        instructionStage.setMinHeight(200);
        Text instructionHeader = new Text("HOW TO PLAY:");
        instructionHeader.setFont(Font.font("trebuchet", 36));
        Text sentence = new Text("Enter a five-letter, java-related word to make a guess!");
        VBox instructionPane = new VBox();
        VBox.setMargin(instructionHeader, new Insets(20, 20, 20, 20));
        VBox.setMargin(sentence, new Insets(10, 20, 20, 20));
        instructionPane.getChildren().addAll(instructionHeader, sentence);
        instructionStage.setScene(new Scene(instructionPane));
        instructBtn.setOnAction(e -> instructionStage.show());


        //Play button handling with Anonymous Inner Class
        playBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                primaryStage.setScene(new Scene(gamePane));
            }
        });

        //Restart with Lambda
        restartBtn.setOnAction((e) -> {
            backend.reset();
            target = backend.getTarget();
            wordIndex = 0;
            letterIndex = 0;
            gamePane.setCenter(resetWordBank());
            toolboxPane.getChildren().set(0,
                                new Text("Enter a five-letter, java-related word to make a guess!"));
            gamePane.setBottom(toolboxPane);
        });

        //KeyTyped
        gamePane.setOnKeyReleased((e) -> {
            if (wordIndex >= 6) {
                    return;
            }
            KeyCode enterred = e.getCode();
            if (enterred == KeyCode.BACK_SPACE) {
                if (letterIndex < 0) {
                    return;
                }
                if (letterIndex == 0) {
                    wordBank[wordIndex][letterIndex] = new TextField("");
                    wordBank[wordIndex][letterIndex].setFont(Font.font("arial", FontWeight.BOLD, 20));
                    gamePane.setCenter(editWordBank());
                    return;
                }
                wordBank[wordIndex][--letterIndex] = new TextField("");
                wordBank[wordIndex][letterIndex].setFont(Font.font("arial", FontWeight.BOLD, 20));
                gamePane.setCenter(editWordBank());
            }
            else if (enterred == KeyCode.ENTER) {
                if (letterIndex != 5) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "You must enter a 5 letter word.");
                    error.showAndWait();
                } else {
                    String wordEnterred = "";
                    for (TextField tf : wordBank[wordIndex]) {
                        wordEnterred += tf.getCharacters();
                    }
                    try {
                        String wordCheck = backend.check(wordEnterred);
                        for (int i = 0; i < wordCheck.length(); i++) {
                            if (wordCheck.charAt(i) == 'g') {
                                wordBank[wordIndex][i].setStyle("-fx-text-fill: white; -fx-background-color: #6ca965;");
                            }
                            if (wordCheck.charAt(i) == 'y') {
                                wordBank[wordIndex][i].setStyle("-fx-text-fill: white; -fx-background-color: #C8B653;");
                            }
                            if (wordCheck.charAt(i) == 'i') {
                                wordBank[wordIndex][i].setStyle("-fx-text-fill: white; -fx-background-color: #787c7f;");
                            }
                        }
                        if (wordIndex == 5 && !wordCheck.equals("ggggg")) {
                            toolboxPane.getChildren().set(0,
                                new Text(String.format("Game over. The word was %s.", backend.getTarget())));
                            gamePane.setBottom(toolboxPane);
                        }
                        if (wordCheck.equals("ggggg")) {
                            wordIndex = 5;
                            toolboxPane.getChildren().set(0,
                                new Text("Congratulations! You guessed the word!"));
                            gamePane.setBottom(toolboxPane);
                        }
                        wordIndex++;
                        letterIndex = 0;
                    } catch (InvalidGuessException ie) {
                        Alert invalidGuessError = new Alert(Alert.AlertType.ERROR, "You must enter a 5 letter word.");
                        invalidGuessError.showAndWait();
                    }
                }
            }
            if (enterred.isLetterKey()) {
                if (letterIndex > 4) {
                    return;
                }
                String letter = enterred.getName();
                wordBank[wordIndex][letterIndex] = new TextField(letter.toUpperCase());
                wordBank[wordIndex][letterIndex++].setFont(Font.font("arial", FontWeight.BOLD, 20));
                gamePane.setCenter(editWordBank());
            }
        });
        
        primaryStage.show();
    }

    
    //Letter Array methods
    public VBox resetWordBank() {
        wordBank = new TextField[6][5];

        VBox wordsPane = new VBox();
        wordsPane.setSpacing(5);

        for (int i = 0; i < 6; i++) {
            HBox wordPane = new HBox();
            wordPane.setAlignment(Pos.CENTER);
            wordPane.setSpacing(5);
            for (int j = 0; j < 5; j++) {
                wordBank[i][j] = new TextField("");
                wordBank[i][j].setFont(Font.font("Arial", FontWeight.BOLD, 20));
                wordBank[i][j].setPrefColumnCount(1);
                wordBank[i][j].setMinHeight(40);
                wordBank[i][j].setMinWidth(40);
                wordBank[i][j].setEditable(false);
                wordPane.getChildren().add(wordBank[i][j]);
            }
            wordsPane.getChildren().add(wordPane);
            wordsPane.setAlignment(Pos.CENTER);
        }
        return wordsPane;
    }
    public VBox editWordBank() {
        VBox wordsPane = new VBox();
        wordsPane.setSpacing(5);

        for (int i = 0; i < 6; i++) {
            HBox wordPane = new HBox();
            wordPane.setAlignment(Pos.CENTER);
            wordPane.setSpacing(5);
            for (int j = 0; j < 5; j++) {
                wordBank[i][j].setPrefColumnCount(1);
                wordBank[i][j].setEditable(false);
                wordBank[i][j].setMinHeight(40);
                wordBank[i][j].setMinWidth(40);
                wordPane.getChildren().add(wordBank[i][j]);
            }
            wordsPane.getChildren().add(wordPane);
            wordsPane.setAlignment(Pos.CENTER);
        }
        return wordsPane;
    }
}