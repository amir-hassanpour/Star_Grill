import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public static int drinkNumber() { return DRINK_NUMBER; }

    public static int frozenNumber() { return FROZEN_NUMBER; }

    public static int toppingNumber() { return TOPPING_NUMBER; }

    public static Map<String, Integer> food() { return FOOD; }

    public static Map<String, Integer> drink() { return DRINK; }

    public static Map<String, Integer> frozen() { return FROZEN; }

    public static Map<String, Integer> topping() { return TOPPING; }
}

abstract class ButtonLayout {
    abstract Node buttonCreator();

    protected HBox buttonCreator(char itemCode) {
        HBox buttonLayoutHBox = new HBox();

        VBox drinkColumnVBox = null;

        int counter = 0;

        for (String keys: UIInformation.drink().keySet()) {
            int leng = keys.length();

            if (keys.charAt(leng - 1) != itemCode) {
                continue;
            }
            else if (counter % 8 == 0) {
                drinkColumnVBox = new VBox();
                drinkColumnVBox.setSpacing(UIInformation.stageHeight() * 0.05);
                buttonLayoutHBox.getChildren().add(drinkColumnVBox);
            }
            // ToDo add functionality to the button by making chnages to the total amount and display in the pane
            Button drinkButton = new Button();
            drinkButton.setText(keys.substring(0, leng - 1));

            drinkColumnVBox.getChildren().add(drinkButton);
        }

        return buttonLayoutHBox;
    }
}

class FoodButtonLayout extends ButtonLayout {
    @Override
    public VBox buttonCreator() {
        VBox buttonLayout = new VBox();
        buttonLayout.setSpacing(UIInformation.stageHeight() * 0.015);

        double buttonHeight = Math.min(UIInformation.stageHeight() * 0.45 /
                UIInformation.foodNumber(), UIInformation.stageHeight() * 0.06);

        for (String keys: UIInformation.food().keySet()) {
            // ToDo add functionality to the buttons
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

class DrinkButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() {
        return super.buttonCreator('@');
    }
    public void DrinkTaken() {
        // ToDo add method for when the button is created button functionality
    }
}

class FrozenButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() {
        return super.buttonCreator('#');
    }
    public void FrozenTaken() {
        // ToDo add method for when the button is created button functionality
    }
}

class ToppingButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() {
        return super.buttonCreator('$');
    }
    public void ToppingTaken() {
        // ToDo add method for when the button is created button functionality
    }
}

class PaneCreator {
    private static Pane mainPane;

    public static Pane orderLayout(double width, double height) {
        mainPane = new Pane();
        mainPane.setPrefSize(width, height);

        mainPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: black;"
        );
        mainPane.setTranslateY(UIInformation.stageHeight() * 0.1);
        return mainPane;
    }

    public static Pane updateOrderLayout(List<String> orders) {
        HBox mainOrderHBox = new HBox();

        int num = 0;

        VBox columnOrder = null;

        for  (String order: orders) {
            if (num % 10 == 0) {
                columnOrder = new VBox();
                mainOrderHBox.getChildren().add(columnOrder);
            }
            // ToDo add functionality to the buttons to get removed when click
            Button orderButton = new Button();

            orderButton.setText(order);
            columnOrder.getChildren().add(orderButton);

            num ++;
        }

        mainPane.getChildren().add(mainOrderHBox);

        return mainPane;
    }
}

// ToDo this is the popup button at the top of the orderpane
abstract class PopUpsLayout {
    abstract void PopUpsLayout(String itemCode);
}

class FinalLayout {
    private static Label foodNumberLabel;

    private static Label orderNumber(int orderNum) {
        foodNumberLabel = new Label("Food Number: " + orderNum);
        foodNumberLabel.setFont(new Font(UIInformation.stageHeight() * 0.025));

        return foodNumberLabel;
    }

    public static void updateOrderNumber(int orderNum) {
        foodNumberLabel.setText("Food Number: " + orderNum);
    }

    private static VBox buttonNumber() {
        VBox buttonNumberVBox = new VBox();

        VBox buttonLayout = new FoodButtonLayout().buttonCreator();
        buttonNumberVBox.setSpacing(UIInformation.stageHeight() * 0.1);

        Label foodNumberLabel = orderNumber(1);

        buttonNumberVBox.getChildren().add(foodNumberLabel);
        buttonNumberVBox.getChildren().add(buttonLayout);

        buttonNumberVBox.setTranslateY(UIInformation.stageHeight() * 0.03);

        return buttonNumberVBox;
    }

    private static TextField customAmount () {
        TextField customAmount = new TextField();

        customAmount.setPromptText("Custom Amount");

        customAmount.setPrefWidth(UIInformation.stageWidth() * 0.1);
        customAmount.setPrefHeight(UIInformation.stageHeight() * 0.05);

        customAmount.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }

            return null;
        }));

        customAmount.setOnAction(event -> {
           String custom = customAmount.getText();

           if (!custom.isEmpty() && !custom.equals(".")) {
               double amount = Double.parseDouble(custom);
               // TODO make a funciton that recieves this amount and puts it on the screen as well as adding it to the
               //  total cost
               System.out.println(amount);
           }
        });

        return customAmount;
    }

    private static VBox orderExtraDetails () {
        VBox orderExtraDetailsVBox = new VBox();
        orderExtraDetailsVBox.setSpacing(UIInformation.stageHeight() * 0.01);

        Button toGoButton = new Button();

        toGoButton.setPrefWidth(UIInformation.stageWidth() * 0.15);
        toGoButton.setPrefHeight(UIInformation.stageHeight() * 0.05);
        toGoButton.setText("To Go");

        toGoButton.setStyle("-fx-border-color: black;" +
                "-fx-font-size: " + UIInformation.stageWidth() * 0.01 + "px;" +
                "-fx-font-weight: bold;");

        TextField extraDetails = new TextField();
        extraDetails.setPrefWidth(UIInformation.stageWidth() * 0.15);
        extraDetails.setPrefHeight(UIInformation.stageHeight() * 0.05);
        extraDetails.setPromptText("Extra Details");

        extraDetails.setOnAction(event -> {
           String details = extraDetails.getText();

           if (!details.isEmpty()) {
               // ToDo make a funtions to receieve extra details
               System.out.println(details);
           }
        });

        orderExtraDetailsVBox.getChildren().addAll(extraDetails, toGoButton);

        return orderExtraDetailsVBox;
    }


    private static VBox orderFinalization() {
        VBox extraDetail = new VBox();
        extraDetail.setSpacing(UIInformation.stageHeight() * 0.2);

        extraDetail.getChildren().add(customAmount());
        extraDetail.getChildren().add(orderExtraDetails());
        extraDetail.getChildren().add(paymentMethod());

        extraDetail.setTranslateY(UIInformation.stageHeight() * 0.03);

        return extraDetail;
    }

    private static HBox paymentMethod() {
        HBox paymentMethodHBox = new HBox();

        Button cashPay = new Button("Cash");
        Button cardPay = new Button("Card");

        paymentMethodHBox.getChildren().addAll(cashPay, cardPay);

        cashPay.setPrefWidth(UIInformation.stageWidth() * 0.075);
        cashPay.setPrefHeight(UIInformation.stageHeight() * 0.05);

        cardPay.setPrefWidth(UIInformation.stageWidth() * 0.075);
        cardPay.setPrefHeight(UIInformation.stageHeight() * 0.05);

        cashPay.setStyle("-fx-border-color: black;");
        cardPay.setStyle("-fx-border-color: black;");

        return paymentMethodHBox;
    }

    public static HBox mainHBoxLayout () {
        HBox mainLayout = new HBox();

        mainLayout.setSpacing(UIInformation.stageHeight() * 0.1);

        mainLayout.getChildren().add(buttonNumber());
        mainLayout.getChildren().add(PaneCreator.orderLayout(UIInformation.stageWidth() * 0.53, UIInformation.stageHeight() * 0.6));
        mainLayout.getChildren().add(orderFinalization());

        mainLayout.setTranslateX(UIInformation.stageWidth() * 0.03);

        return mainLayout;
    }
}

public class Star_Grill_UIModel extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e4dada;");

        Scene scene = new Scene(root, UIInformation.stageWidth(), UIInformation.stageHeight());

        root.getChildren().add(FinalLayout.mainHBoxLayout());

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
