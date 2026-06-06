import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

class UIInformation {
    private static final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private static int FOOD_NUMBER;
    private static int DRINK_NUMBER;
    private static int FROZEN_NUMBER;
    private static int TOPPING_NUMBER;

    private static final double STAGE_WIDTH = screenBounds.getWidth();
    private static final double STAGE_HEIGHT = screenBounds.getHeight();

    public static final Path filePath = Path.of("Star_Grill_info/food_options");

    public static Map<String, Integer> FOOD = new HashMap<>();
    public static Map<String, Integer> DRINK = new HashMap<>();
    public static Map<String, Integer> FROZEN = new HashMap<>();
    public static Map<String, Integer> TOPPING = new HashMap<>();

    public UIInformation() {
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {

                String[] newLine = line.split(" ");

                int lengthProduct = newLine[0].length();

                char lastChar = newLine[0].charAt(lengthProduct - 1);

                newLine[0] = newLine[0].substring(0, lengthProduct - 1);

                if (lastChar == '!') {
                    FOOD.put(newLine[0], Integer.parseInt(newLine[1]));
                    FOOD_NUMBER++;
                }
                else if (lastChar == '@') {
                    DRINK.put(newLine[0], Integer.parseInt(newLine[1]));
                    DRINK_NUMBER++;
                }
                else if (lastChar== '#') {
                    FROZEN.put(newLine[0], Integer.parseInt(newLine[1]));
                    FROZEN_NUMBER++;
                }
                else if (lastChar == '$') {
                    TOPPING.put(newLine[0], Integer.parseInt(newLine[1]));
                    TOPPING_NUMBER++;
                }
            }
        }
        catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }

    public static double stageHeight() { return STAGE_HEIGHT; }

    public static double stageWidth() { return STAGE_WIDTH; }

    public static int foodNumber() { return FOOD_NUMBER; }

    public static int drinkNumber() { return  DRINK_NUMBER; }

    public static int frozenNumber() { return FROZEN_NUMBER; }

    public static int toppingNumber() { return TOPPING_NUMBER; }

    public static Map<String, Integer> food() { return FOOD; }

    public static Map<String, Integer> drink() { return DRINK; }

    public static Map<String, Integer> frozen() { return FROZEN; }

    public static Map<String, Integer> topping() { return TOPPING; }

}

abstract class ButtonLayOut {
    public abstract Node buttonCreator();
}

class FoodButtonLayOut extends ButtonLayOut {
    @Override
    public VBox buttonCreator() {
        VBox vBox = new VBox();
        VBox buttonLayout = new VBox();
        buttonLayout.setSpacing(UIInformation.stageHeight() * 0.015);

        double buttonHeight = Math.min(UIInformation.stageHeight() * 0.45 /
                UIInformation.foodNumber(), UIInformation.stageHeight() * 0.06);

        for (String keys: UIInformation.food().keySet()) {
            Button button = new Button();
            button.setText(keys);
            button.setPrefWidth(UIInformation.stageWidth() * 0.1);
            button.setStyle("-fx-border-color: black;" +
                    "-fx-font-size: " + buttonHeight * 0.45 + "px;");

            buttonLayout.getChildren().add(button);
        }
        return buttonLayout;
    }
}

class ButtonGorpupLayOut extends ButtonLayOut {
    @Override
    public VBox buttonCreator() { return new VBox(); }
}
public class Star_Grill_UIModel extends Application {
    private static Label foodNumberLabel;

    public static Label orderNumber(int orderNum) {
        foodNumberLabel = new Label("Food Number: " + orderNum);
        foodNumberLabel.setFont(new Font(UIInformation.stageHeight() * 0.025));

        return foodNumberLabel;
    }

    public static void updateOrderNumber(int orderNum) {
        foodNumberLabel.setText("Food Number: " + orderNum);
    }

    private static VBox buttonNumber() {
        VBox buttonNumberVBox = new VBox();

        VBox buttonLayout = new FoodButtonLayOut().buttonCreator();
        buttonNumberVBox.setSpacing(UIInformation.stageHeight() * 0.1);

        Label foodNumberLabel = orderNumber(1);

        buttonNumberVBox.getChildren().add(foodNumberLabel);
        buttonNumberVBox.getChildren().add(buttonLayout);

        buttonNumberVBox.setTranslateX(UIInformation.stageWidth() * 0.03);
        buttonNumberVBox.setTranslateY(UIInformation.stageHeight() * 0.03);

        return buttonNumberVBox;
    }
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e4dada;");

        Scene scene = new Scene(root, UIInformation.stageWidth(), UIInformation.stageHeight());

        root.getChildren().add(buttonNumber());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Star Grill");
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        UIInformation starter = new UIInformation();
        launch(args);
    }
}