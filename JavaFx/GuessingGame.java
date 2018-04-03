package JavaFx;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.concurrent.ThreadLocalRandom;

public class GuessingGame extends Application
{
    private static final String FONT_NAME = "roboto mono";
    private static final double FONT_SIZE_MEDIUM = 30;
    private javafx.stage.Stage primaryStage;
    private BorderPane rootNode;
    private VBox nestedTopHalf;
    private HBox nestedBottomHalf;
    private Scene scene;
    private int randomInt;
    private Text displayText;
    private Button buttonGuessNumber, buttonPlayAgainYes, buttonPlayAgainNo;
    private TextField stage1InputField;

    enum Stage
    {
        FIRST_GUESS("Guess a number\nbetween\n1 to 100\n"),
        TOO_LOW("You guessed too low\nguess again"),
        TOO_HIGH("You guessed too high\nguess again"),
        PLAY_AGAIN("Congratulations!!!!\nWould you like to\ncontinue?");
        private String stageText;
        Stage(String stageText) {this.stageText = stageText;}
        @Override public String toString() {return stageText;}
    }

    enum SceneSize{
        WIDTH(400),
        HEIGHT(260);
        private int size;
        SceneSize(int size) {this.size = size;}
        public int getLength(){return size;}
    }

    enum HexCode
    {
        BACKGROUND("#2B2B2B"),
        FONT("#43B156");
        private String hexCode;
        HexCode(String hexCode){this.hexCode = hexCode;}
        @Override public String toString(){return hexCode;}
    }

    enum ButtonLabel
    {
        YES("Yes"),
        NO("No"),
        GUESS("Guess");
        private String buttonName;
        ButtonLabel(String buttonName) {this.buttonName = buttonName;}
        @Override  public String toString(){return buttonName;}
    }

    private void compareNumbers(ActionEvent event)
    {
        int usersGuess = Integer.parseInt(stage1InputField.getText());
        if(randomInt != usersGuess)
        {
            startStage((usersGuess < randomInt) ? Stage.TOO_LOW : Stage.TOO_HIGH );
        }
        else
        {
            startStage(Stage.PLAY_AGAIN);
        }
    }

    private Text formatText(String stringArg)
    {
        Text text = new Text(stringArg);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(FONT_NAME, FONT_SIZE_MEDIUM));
        text.setFill(Paint.valueOf(HexCode.FONT.toString()));
        return text;
    }

    private BorderPane formatBorderPane(Node topHalf, Node bottomHalf)
    {
        BorderPane borderPane =new BorderPane(topHalf, null,null, bottomHalf, null);
        borderPane.setStyle("-fx-background-color:null;");
        return borderPane;
    }

    private Button formatButton(String buttonName)
    {
        Button button = new Button(buttonName);
        button.setFont(Font.font(FONT_NAME));
        button.setStyle("-fx-base: "+ HexCode.FONT.toString());
        return button;
    }

    private HBox formatHBox(Node...input)
    {
        HBox hBox = new HBox(input);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(0, 0, 30, 0));
        return hBox;
    }

    private Scene formatScene()
    {
        return new Scene(rootNode,
                SceneSize.WIDTH.getLength(),
                SceneSize.HEIGHT.getLength(),
                Color.web(HexCode.BACKGROUND.hexCode)
        );
    }

    private VBox formatTopHalf(Stage stage)
    {
        switch (stage)
        {
            case FIRST_GUESS: displayText = formatText(Stage.FIRST_GUESS.toString()); break;
            case TOO_LOW:     displayText = formatText(Stage.TOO_LOW.toString());     break;
            case TOO_HIGH:    displayText = formatText(Stage.TOO_HIGH.toString());    break;
            case PLAY_AGAIN:  displayText = formatText(Stage.PLAY_AGAIN.toString());  break;
        }
        VBox vBox = new VBox(displayText);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        return vBox;
    }

    private HBox formatBottomHalfGuessingStage()
    {
        stage1InputField = new TextField();
        buttonGuessNumber = formatButton(ButtonLabel.GUESS.buttonName);
        buttonGuessNumber.setOnAction(this::compareNumbers);
        HBox hBox = formatHBox(stage1InputField, buttonGuessNumber);
        return hBox;
    }

    private HBox formatBottomHalfPlayAgainStage()
    {
        buttonPlayAgainYes = formatButton(ButtonLabel.YES.buttonName);
        buttonPlayAgainNo =  formatButton(ButtonLabel.NO.buttonName);
        buttonPlayAgainYes.setOnAction( (event) -> startStage(Stage.FIRST_GUESS) );
        buttonPlayAgainNo.setOnAction(  (event) -> System.exit(1) );
        HBox hBox = formatHBox(buttonPlayAgainNo, buttonPlayAgainYes);
        return hBox;
    }

    private void startStage(Stage stage)
    {
        switch (stage)
        {
            case FIRST_GUESS:
                randomInt = ThreadLocalRandom.current().nextInt(1, 100);
                System.out.println("Random int: "+randomInt);
                nestedTopHalf = formatTopHalf(Stage.FIRST_GUESS);
                nestedBottomHalf = formatBottomHalfGuessingStage();
                break;
            case TOO_LOW:
                nestedTopHalf = formatTopHalf(Stage.TOO_LOW);
                nestedBottomHalf = formatBottomHalfGuessingStage();
                break;
            case TOO_HIGH:
                nestedTopHalf = formatTopHalf(Stage.TOO_HIGH);
                nestedBottomHalf = formatBottomHalfGuessingStage();
                break;
            case PLAY_AGAIN:
                nestedTopHalf = formatTopHalf(Stage.PLAY_AGAIN);
                nestedBottomHalf = formatBottomHalfPlayAgainStage();
                break;
        }
        rootNode = formatBorderPane(nestedTopHalf, nestedBottomHalf);
        scene = formatScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
        startStage(Stage.FIRST_GUESS);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
