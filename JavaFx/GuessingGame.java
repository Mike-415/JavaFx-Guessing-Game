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
    private static final int SCENE_WIDTH = 400;
    private static final int SCENE_HEIGHT = 260;
    private javafx.stage.Stage primaryStage;
    private BorderPane rootNode;
    private VBox nestedTopHalf;
    private HBox nestedBottomHalf;
    private Scene scene;
    private int randomInt;
    private Text displayText;
    private Button stage1GuessButton, stage2YesButton, stage2NoButton;
    private TextField stage1InputField;

    enum Stage
    {
        FIRST_GUESS(1, "Guess a number\nbetween\n1 to 100\n"),
        TOO_LOW(2, "You guessed too low\nguess again"),
        TOO_HIGH(3, "You guessed too high\nguess again"),
        PLAY_AGAIN(4, "Congratulations!!!!\nWould you like to\ncontinue?");
        private int stageNum;
        private String stageText;
        Stage(int stageNum, String stageText)
        {
            this.stageNum = stageNum;
            this.stageText = stageText;
        }
        @Override
        public String toString()
        {
            return stageText;
        }
    }

    enum HexCode
    {
        BACKGROUND("#2B2B2B"),
        FONT("#43B156");
        private String hexCode;
        HexCode(String hexCode){this.hexCode = hexCode;}
    }

    enum ButtonLabels
    {
        YES("Yes"),
        NO("No"),
        GUESS("Guess");
        private String buttonName;
        ButtonLabels(String buttonName)
        {
            this.buttonName = buttonName;
        }
    }

    private void compareNumbers(ActionEvent event)
    {
        int usersGuess = Integer.parseInt(stage1InputField.getText());
        if(randomInt != usersGuess)
        {
            if(usersGuess < randomInt)
            {
                startStage(Stage.TOO_LOW.stageNum);
            }
            else
            {
                startStage(Stage.TOO_HIGH.stageNum);
            }
        }
        else
        {
            startStage(Stage.PLAY_AGAIN.stageNum);
        }
    }

    private void exitProgram(ActionEvent event)
    {
        System.exit(1);
    }


    private void restartProgram(ActionEvent actionEvent)
    {
        startStage(1);
    }
    private Text formatText(String stringArg)
    {
        Text text = new Text(stringArg);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(FONT_NAME, FONT_SIZE_MEDIUM));
        text.setFill(Paint.valueOf(HexCode.FONT.hexCode));
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
        button.setStyle("-fx-base: "+ HexCode.FONT.hexCode);
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
                SCENE_WIDTH,
                SCENE_HEIGHT,
                Color.web(HexCode.BACKGROUND.hexCode)
        );
    }

    private VBox formatTopHalf(int stageNumber)
    {
        if(stageNumber == 1)
        {
            displayText = formatText(Stage.FIRST_GUESS.stageText);
        }
        else if(stageNumber == 2)
        {
            displayText = formatText(Stage.TOO_LOW.stageText);
        }
        else if(stageNumber == 3)
        {
            displayText = formatText(Stage.TOO_HIGH.stageText);
        }
        else if(stageNumber == 4)
        {
            displayText = formatText(Stage.PLAY_AGAIN.stageText);
        }
        else
        {
            throw new IllegalArgumentException("You only have two valid arguments, integers 1 and 2");
        }
        VBox vBox = new VBox(displayText);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        return vBox;
    }

    private HBox formatStage1BottomHalf()
    {
        stage1InputField = new TextField();
        stage1GuessButton = formatButton(ButtonLabels.GUESS.buttonName);
        stage1GuessButton.setOnAction(this::compareNumbers);
        HBox hBox = formatHBox(stage1InputField, stage1GuessButton);
        return hBox;
    }

    private HBox formatStage4BottomHalf()
    {
        stage2YesButton = formatButton(ButtonLabels.YES.buttonName);
        stage2NoButton = formatButton(ButtonLabels.NO.buttonName);
        stage2YesButton.setOnAction(this::restartProgram);
        stage2NoButton.setOnAction(this::exitProgram);
        HBox hBox = formatHBox(stage2NoButton, stage2YesButton);
        return hBox;
    }


    private void completePrimaryStage(Scene scene)
    {
        //primaryStage.stageText("JavaFx Guessing Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startStage(int stageNumber)
    {
        if(stageNumber == Stage.FIRST_GUESS.stageNum)
        {
            randomInt = ThreadLocalRandom.current().nextInt(1, 100);
            System.out.println("Random int: "+randomInt);
            nestedTopHalf = formatTopHalf(Stage.FIRST_GUESS.stageNum);
            nestedBottomHalf = formatStage1BottomHalf();
        }
        else if(stageNumber == Stage.TOO_LOW.stageNum)
        {
            nestedTopHalf = formatTopHalf(Stage.TOO_LOW.stageNum);
            nestedBottomHalf = formatStage1BottomHalf();
        }
        else if(stageNumber == Stage.TOO_HIGH.stageNum)
        {
            nestedTopHalf = formatTopHalf(Stage.TOO_HIGH.stageNum);
            nestedBottomHalf = formatStage1BottomHalf();
        }
        else if(stageNumber == Stage.PLAY_AGAIN.stageNum)
        {
            nestedTopHalf = formatTopHalf(Stage.PLAY_AGAIN.stageNum);
            nestedBottomHalf = formatStage4BottomHalf();
        }
        else
        {
            throw new IllegalArgumentException("You can only enter the integers 1 or 2");
        }

        rootNode = formatBorderPane(nestedTopHalf, nestedBottomHalf);
        scene = formatScene();
        completePrimaryStage(scene);
    }

    private void setPrimaryStage(javafx.stage.Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        startStage(Stage.FIRST_GUESS.stageNum);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception
    {
        setPrimaryStage(primaryStage);

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
