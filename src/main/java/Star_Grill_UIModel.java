import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.IOException;
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
    private static int foodNumber;
    private static int drinkNumber;
    private static int frozenNumber;
    private static int toppingNumber;
    private static int popUpButtonNumber;
    private static int allItemsNumber;

    private static final double STAGE_WIDTH = screenBounds.getWidth();
    private static final double STAGE_HEIGHT = screenBounds.getHeight();

    public static final Path filePath = Path.of("Star_Grill_info/food_options.txt");

    private static final Map<String, Double> FOOD = new HashMap<>();
    private static final Map<String, Double> DRINK = new HashMap<>();
    private static final Map<String, Double> FROZEN = new HashMap<>();
    private static final Map<String, Double> TOPPING = new HashMap<>();
    private static final Map<String, Double> popUpButton = new HashMap<>();
    private static final Map<String, Double> allItems = new HashMap<>();

    UIInformation() {
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {

                String[] newLine = line.split(" ");

                int lengthProduct = newLine[0].length();

                char lastChar = newLine[0].charAt(lengthProduct - 1);
                Double price = Double.parseDouble(newLine[1]);

                if (lastChar == '!') {
                    FOOD.put(newLine[0], price);
                    foodNumber++;
                }
                else if (lastChar == '@') {
                    DRINK.put(newLine[0], price);
                    drinkNumber++;
                }
                else if (lastChar== '#') {
                    FROZEN.put(newLine[0], price);
                    frozenNumber++;
                }
                else if (lastChar == '$') {
                    TOPPING.put(newLine[0], price);
                    toppingNumber++;
                }
                else if (lastChar == '%') {
                    popUpButton.put(newLine[0], price);
                    popUpButtonNumber++;
                }

                allItems.put(newLine[0], price);
                allItemsNumber ++;
            }
        }
        catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }

    public static double stageHeight() { return STAGE_HEIGHT; }

    public static double stageWidth() { return STAGE_WIDTH; }

    public static int foodNumber() { return foodNumber; }

    public static int drinkNumber() { return drinkNumber; }

    public static int frozenNumber() { return frozenNumber; }

    public static int toppingNumber() { return toppingNumber; }

    public static int popUpButtonNumber() { return popUpButtonNumber; }

    public static int allItemsNumber() { return allItemsNumber; }

    public static Map<String, Double> food() { return FOOD; }

    public static Map<String, Double> drink() { return DRINK; }

    public static Map<String, Double> frozen() { return FROZEN; }

    public static Map<String, Double> topping() { return TOPPING; }

    public static Map<String, Double> popUpButton() { return popUpButton; }

    public static Map<String, Double> allItems() { return allItems; }
}

abstract class ButtonLayout {
    abstract Node buttonCreator();

    protected HBox buttonCreator(char itemCode, int buttonNumber, double buttonWidth) {
        HBox buttonLayoutHBox = new HBox();
        buttonLayoutHBox.setSpacing(UIInformation.stageWidth() * 0.01);
        buttonLayoutHBox.setAlignment(Pos.CENTER);
        VBox drinkColumnVBox = null;

        int counter = 0;

        Map<String, Double> items = null;

        if (itemCode == '@') {
            items = UIInformation.drink();
        }
        else if (itemCode == '#') {
            items = UIInformation.frozen();
        }
        else if (itemCode == '$') {
            items = UIInformation.topping();
        }
        else {
            items = UIInformation.popUpButton();
        }

        for (String keys: items.keySet()) {
            int leng = keys.length();

            if (counter % buttonNumber == 0) {
                drinkColumnVBox = new VBox();
                drinkColumnVBox.setSpacing(UIInformation.stageHeight() * 0.02);
                buttonLayoutHBox.getChildren().add(drinkColumnVBox);
            }

            Button productButton = new Button();
            productButton.setText(keys.substring(0, leng - 1));

            productButton.setFont(Font.font("Arial", FontWeight.BOLD, UIInformation.stageWidth() * 0.015));

            productButton.setMinWidth(UIInformation.stageWidth() * buttonWidth);
            productButton.setMaxWidth(UIInformation.stageWidth() * buttonWidth);

            Map<String, Double> finalItems = items;

            productButton.setOnAction(e -> {
                if (itemCode == '@') {
                    DrinkButtonLayout drink = new DrinkButtonLayout();
                    drink.DrinkTaken(keys, finalItems.get(keys));
                }
                else if (itemCode == '#') {
                    FrozenButtonLayout frozen = new FrozenButtonLayout();
                    frozen.FrozenTaken(keys, finalItems.get(keys));
                }
                else if (itemCode == '$') {
                    ToppingButtonLayout topping = new ToppingButtonLayout();
                    topping.ToppingTaken(keys, finalItems.get(keys));
                }
                else if (itemCode == '%') {
                    PopUpButtonLayout.popUpAction(keys);
                }
                FinalLayout.mainPaneCreator.updateOrderLayout(Orders.orders);
            });

            drinkColumnVBox.getChildren().add(productButton);

            counter++;
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
            Button button = new Button();
            button.setText(keys.substring(0, keys.length() - 1));
            button.setPrefWidth(UIInformation.stageWidth() * 0.1);
            button.setStyle("-fx-border-color: black;" +
                    "-fx-font-size: " + buttonHeight * 0.45 + "px;");

            button.setOnAction(e -> foodTaken(keys, UIInformation.food().get(keys)));

            buttonLayout.getChildren().add(button);
        }
        return buttonLayout;
    }

    public void foodTaken(String food, double price) {
        Orders.addOrder(food);
        Payments.addTotal(price);
        FinalLayout.updateTotal();
        FinalLayout.mainPaneCreator.updateOrderLayout(Orders.orders);
    }
}

class DrinkButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() {
        return super.buttonCreator('@', 10, 0.1);
    }

    public void DrinkTaken(String drink, double price) {
        Orders.addOrder(drink);
        Payments.addTotal(price);
        DrinkOrders drinkOrders = new DrinkOrders();
        DrinkPopUpsLayout.paneCreator.updateOrderLayout(drinkOrders.getOrders());
    }
}

class FrozenButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() {
        return super.buttonCreator('#', 10, 0.1);
    }

    public void FrozenTaken(String frozen, double price) {
        Orders.addOrder(frozen);
        Payments.addTotal(price);
        FrozenOrders frozenOrders = new FrozenOrders();
        FrozenPopUpsLayout.paneCreator.updateOrderLayout(frozenOrders.getOrders());
    }
}

class ToppingButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() {
        return super.buttonCreator('$', 10, 0.1);
    }

    public void ToppingTaken(String topping, double price) {
        Orders.addOrder(topping);
        Payments.addTotal(price);
        ToppingOrders toppingOrders = new ToppingOrders();
        ToppingPopUpsLayout.paneCreator.updateOrderLayout(toppingOrders.getOrders());
    }
}

class PopUpButtonLayout extends ButtonLayout {
    @Override
    public HBox buttonCreator() { return super.buttonCreator('%', 1, 0.1799); }

    public static void popUpAction(String popUpButton) {
        if (popUpButton.equals("Drinks%")) {
            if (DrinkPopUpsLayout.visibleDrink)
                FinalLayout.popUpMiddleVBox(DrinkPopUpsLayout.drinkLayout());
            else
                DrinkPopUpsLayout.drinkPane.setVisible(true);
                FinalLayout.popUpMiddleVBox(DrinkPopUpsLayout.drinkPane);
        }
        else if (popUpButton.equals("Frozen%")) {
            if (FrozenPopUpsLayout.visibleFrozen)
                FinalLayout.popUpMiddleVBox(FrozenPopUpsLayout.frozenLayout());
            else
                FrozenPopUpsLayout.frozenPane.setVisible(true);
            FinalLayout.popUpMiddleVBox(FrozenPopUpsLayout.frozenPane);
        }
        else if (popUpButton.equals("Toppings%")) {
            if (ToppingPopUpsLayout.visibleTopping)
                FinalLayout.popUpMiddleVBox(ToppingPopUpsLayout.toppingLayout());
            else
                ToppingPopUpsLayout.toppingPane.setVisible(true);
            FinalLayout.popUpMiddleVBox(ToppingPopUpsLayout.toppingPane);
        }
    }
}

class PaneCreator {
    private Pane mainPane;
    public List<VBox> listVBox = new ArrayList<>();

    public Pane orderLayout(double width, double height) {
        mainPane = new Pane();
        mainPane.setPrefSize(width, height);

        mainPane.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: black;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;");

        for (int i = 0; i < 30; i++) {
            listVBox.add(new VBox());
        }

        return mainPane;
    }

    public Pane updateOrderLayout(List<String> orders) {
        HBox mainOrderHBox = new HBox();
        mainOrderHBox.setSpacing(UIInformation.stageWidth() * 0.01);
        int num = 0;

        VBox columnOrder = null;

        for (VBox column : listVBox) {
            column.getChildren().clear();
        }

        for (String order: orders) {
            if (num % 11 == 0) {
                columnOrder = listVBox.get(num/11);

                columnOrder.setSpacing(UIInformation.stageHeight() * 0.012);
                mainOrderHBox.getChildren().add(columnOrder);
            }

            Button orderButton = new Button();

            orderButton.setOnAction(e -> {
                try {
                    double number = Double.parseDouble(order);
                    Orders.removeOrder(order);
                    Payments.subtractTotal(number);
                }
                catch (NumberFormatException ex) {
                    if (order.charAt(order.length() - 1) == '^') {
                        Orders.removeOrder(order);
                    }
                    else {
                        Orders.removeOrder(order);
                        Payments.subtractTotal(UIInformation.allItems().get(order));
                        FinalLayout.updateTotal();
                        if (order.contains(">"))
                            ExtraDetails.removeExtraDetail(order);
                    }
                }
                UpdateOrdersLayouts update = new UpdateOrdersLayouts();
                update.updateOrderLayouts();
            });

            orderButton.setText(order.substring(0, order.length() - 1));
            columnOrder.getChildren().add(orderButton);

            num ++;
        }

        mainOrderHBox.setTranslateX(UIInformation.stageWidth() * 0.01);
        mainOrderHBox.setTranslateY(UIInformation.stageHeight() * 0.01);

        mainPane.getChildren().add(mainOrderHBox);
        return mainPane;
    }
}

class PopUpsLayout {
    public static Pane PopUpsLayout(Pane popUpPane, HBox drinkHBox) {
        popUpPane.getChildren().add(drinkHBox);

        drinkHBox.setTranslateY(UIInformation.stageHeight() * 0.7 * 0.1);
        drinkHBox.setTranslateX(UIInformation.stageWidth() * 0.56 * 0.02);

        popUpPane.setStyle("-fx-background-color: lightgray;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #cccccc;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;");

        popUpPane.setPrefHeight(UIInformation.stageHeight() * 0.7);
        popUpPane.setPrefWidth(UIInformation.stageWidth() * 0.56);

        popUpPane.setTranslateY(UIInformation.stageHeight() * 0.03);

        popUpPane.setOnMouseClicked(Event::consume);
        popUpPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (popUpPane.isVisible()
                            && !popUpPane.localToScene(popUpPane.getBoundsInLocal())
                            .contains(event.getSceneX(), event.getSceneY())) {
                        popUpPane.setVisible(false);
                        FinalLayout.middleVBox();
                    }
                });
            }
        });
        return popUpPane;
    }
}

class DrinkPopUpsLayout extends PopUpsLayout {
    public static boolean visibleDrink = true;
    public static final Pane drinkPane = new Pane();
    public static final PaneCreator paneCreator = new PaneCreator();
    public static final Pane drinkPaneOrders = paneCreator.orderLayout(UIInformation.stageWidth()*0.6*0.5,
            UIInformation.stageHeight()*0.6*0.8);

    public static Pane drinkLayout() {
        HBox drinkHBox = new HBox();

        DrinkButtonLayout buttonHBox = new DrinkButtonLayout();

        drinkHBox.setSpacing(UIInformation.stageHeight() * 0.03);
        drinkHBox.getChildren().addAll(buttonHBox.buttonCreator(), drinkPaneOrders);

        drinkHBox.setAlignment(Pos.CENTER);

        visibleDrink = false;

        return PopUpsLayout(drinkPane, drinkHBox);
    }
}

class FrozenPopUpsLayout extends PopUpsLayout {
    public static boolean visibleFrozen = true;
    public static final Pane frozenPane = new Pane();
    public static PaneCreator paneCreator = new PaneCreator();
    public static final Pane frozenPaneOrders = paneCreator.orderLayout(UIInformation.stageWidth()*0.6*0.5,
            UIInformation.stageHeight()*0.6*0.8);

    public static Pane frozenLayout() {
        HBox frozenHBox = new HBox();

        FrozenButtonLayout buttonHBox = new FrozenButtonLayout();

        frozenHBox.setSpacing(UIInformation.stageHeight() * 0.05);
        frozenHBox.getChildren().addAll(buttonHBox.buttonCreator(), frozenPaneOrders);
        visibleFrozen = false;

        return PopUpsLayout(frozenPane, frozenHBox);
    }
}

class ToppingPopUpsLayout extends PopUpsLayout {
    public static boolean visibleTopping = true;
    public static final Pane toppingPane = new Pane();
    public static final PaneCreator paneCreator = new PaneCreator();
    public static final Pane toppingPaneOrders = paneCreator.orderLayout(UIInformation.stageWidth()*0.6*0.5,
            UIInformation.stageHeight()*0.6*0.8);

    public static Pane toppingLayout() {
        HBox toppingHBox = new HBox();

        ToppingButtonLayout buttonHBox = new ToppingButtonLayout();

        toppingHBox.setSpacing(UIInformation.stageHeight() * 0.05);
        toppingHBox.getChildren().addAll(buttonHBox.buttonCreator(),
                toppingPaneOrders);

        visibleTopping = false;

        return PopUpsLayout(toppingPane, toppingHBox);
    }
}

class UpdateOrdersLayouts {
    public void updateOrderLayouts() {
        DrinkOrders drink = new DrinkOrders();
        FrozenOrders frozen = new FrozenOrders();
        ToppingOrders topping = new ToppingOrders();

        FinalLayout.mainPaneCreator.updateOrderLayout(Orders.orders);
        DrinkPopUpsLayout.paneCreator.updateOrderLayout(drink.getOrders());
        ToppingPopUpsLayout.paneCreator.updateOrderLayout(topping.getOrders());
        FrozenPopUpsLayout.paneCreator.updateOrderLayout(frozen.getOrders());
        FinalLayout.updateTotal();
    }
}

class FinalLayout {
    private static Label foodNumberLabel;

    private static Label orderNumber() {
        foodNumberLabel = new Label("Order Number: " + 1);
        foodNumberLabel.setFont(new Font(UIInformation.stageHeight() * 0.025));

        return foodNumberLabel;
    }

    public static void updateOrderNumber(int orderNum) { foodNumberLabel.setText("Order Number: " + orderNum); }

    private static VBox buttonNumber() {
        VBox buttonNumberVBox = new VBox();

        VBox buttonLayout = new FoodButtonLayout().buttonCreator();
        buttonNumberVBox.setSpacing(UIInformation.stageHeight() * 0.1);

        Label foodNumberLabel = orderNumber();

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
               Payments.addTotal(amount);
               Orders.addOrder(String.valueOf(amount) + "0");
               UpdateOrdersLayouts updatingLayouta = new UpdateOrdersLayouts();
               updatingLayouta.updateOrderLayouts();
           }
        });
        return customAmount;
    }

    private static int discountGiven = 0;

    private static Button discountButton() {
        Button discountButton = new Button("Discount 1");
        discountButton.setOnAction(event -> {
            if (discountGiven < 3) {
                Payments.subtractTotal(1.13);
                updateTotal();
            }
            discountGiven++;
        });

        discountButton.setPrefWidth(UIInformation.stageWidth() * 0.15);
        discountButton.setPrefHeight(UIInformation.stageHeight() * 0.05);

        discountButton.setStyle("-fx-border-color: black;" +
                "-fx-font-size: " + UIInformation.stageWidth() * 0.01 + "px;" +
                "-fx-font-weight: bold;");

        return discountButton;
    }

    private static int toGoNum = 1;

    private static Button toGoButton() {
        Button toGoButton = new Button();

        toGoButton.setPrefWidth(UIInformation.stageWidth() * 0.15);
        toGoButton.setPrefHeight(UIInformation.stageHeight() * 0.05);
        toGoButton.setText("To Go");

        toGoButton.setStyle("-fx-border-color: black;" +
                "-fx-font-size: " + UIInformation.stageWidth() * 0.01 + "px;" +
                "-fx-font-weight: bold;");

        toGoButton.setOnAction(event -> {
            ExtraDetails.toGo();
            if (toGoNum % 2 == 1)
                toGoButton.setText("To Go Clicked");
            else
                toGoButton.setText("To Go");
            toGoNum++;
        });

        return toGoButton;
    }

    private static TextField extraDetails() {
        TextField extraDetails = new TextField();
        extraDetails.setPrefWidth(UIInformation.stageWidth() * 0.15);
        extraDetails.setPrefHeight(UIInformation.stageHeight() * 0.05);
        extraDetails.setPromptText("Extra Details");

        extraDetails.setOnAction(event -> {
            String details = extraDetails.getText();

            if (!details.isEmpty()) {
                Orders.addOrder("> " + details + '^');
                UpdateOrdersLayouts updatingLayout = new UpdateOrdersLayouts();
                updatingLayout.updateOrderLayouts();
                ExtraDetails.addExtraDetails("> " + details);
            }
        });

        return extraDetails;
    }

    private static VBox orderExtraDetails () {
        VBox orderExtraDetailsVBox = new VBox();
        orderExtraDetailsVBox.setSpacing(UIInformation.stageHeight() * 0.01);

        orderExtraDetailsVBox.getChildren().addAll(extraDetails(), toGoButton(), discountButton());

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

    private static void changeFunction(Pane popupPane, double collected) {
        if (cashSubNum % 2 == 0) {
            mainStackPane.getChildren().remove(popupPane);
            RecordAllOrders.recordAllOrders("Cash");
            nextActionHelper();
        }
        else {
            String giveBack = String.valueOf(
                    Math.ceil((collected - Double.parseDouble(Payments.getTotal())) * 100) / 100);

            Label changeLabel = new Label(giveBack);
            changeLabel.setTranslateX(UIInformation.stageWidth() * 0.22 * 0.45);
            popupPane.getChildren().add(changeLabel);
        }
        cashSubNum++;
    }

    private static int cashSubNum = 1;

    private static void cashHelper() {
        double stageW = UIInformation.stageWidth();
        double stageH = UIInformation.stageHeight();
        double paneW = stageW * 0.22;
        double paneH = stageH * 0.18;

        Pane popupPane = new Pane();
        popupPane.setMaxSize(paneW, paneH);

        popupPane.setStyle(
                "-fx-background-color: lightgrey;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-background-radius: 12px;"
        );

        TextField inputBox = new TextField();
        inputBox.setPromptText("Enter amount");
        inputBox.setLayoutX(paneW * 0.10);
        inputBox.setLayoutY(paneH * 0.15);
        inputBox.setPrefWidth(paneW * 0.80);
        inputBox.setPrefHeight(paneH * 0.25);

        Button submitButton = new Button("Submit");
        submitButton.setLayoutX(paneW * 0.30);
        submitButton.setLayoutY(paneH * 0.50);
        submitButton.setPrefWidth(paneW * 0.40);
        submitButton.setPrefHeight(paneH * 0.22);

        Label messageLabel = new Label("");
        messageLabel.setLayoutX(paneW * 0.36);
        messageLabel.setLayoutY(paneH * 0.76);

        messageLabel.setStyle(
                "-fx-font-size: " + (stageW * 0.012) + "px;" +
                        "-fx-font-weight: bold;"
        );

        inputBox.setStyle(
                "-fx-font-size: " + (stageW * 0.010) + "px;"
        );

        submitButton.setStyle(
                "-fx-font-size: " + (stageW * 0.010) + "px;"
        );

        inputBox.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                inputBox.setText(oldValue);
            }
        });

        popupPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (popupPane.isVisible()
                            && !popupPane.localToScene(popupPane.getBoundsInLocal())
                            .contains(event.getSceneX(), event.getSceneY())) {
                        popupPane.setVisible(false);
                        FinalLayout.middleVBox();
                    }
                });
            }
        });

        inputBox.setOnAction(event -> {
            if (!inputBox.getText().isEmpty())
                changeFunction(popupPane, Double.parseDouble(inputBox.getText()));
            else {
                mainStackPane.getChildren().remove(popupPane);
                RecordAllOrders.recordAllOrders("Cash");
                nextActionHelper();
            }
        });
        submitButton.setOnAction(e -> {
            if (!inputBox.getText().isEmpty())
                changeFunction(popupPane, Double.parseDouble(inputBox.getText()));
            else {
                mainStackPane.getChildren().remove(popupPane);
                RecordAllOrders.recordAllOrders("Cash");
                nextActionHelper();
            }
        });

        popupPane.getChildren().addAll(inputBox, submitButton, messageLabel);
        mainStackPane.getChildren().add(popupPane);
    }

    private static void nextActionHelper() {
        NextReset.nextReset(false);
        updateOrderNumber(CustomerNumber.customerNumber);
        updateTotal();

        UpdateOrdersLayouts updatingLayouta = new UpdateOrdersLayouts();
        updatingLayouta.updateOrderLayouts();

        discountGiven = 0;
    }

    private static Button nextAction() {
        Button next = new Button("Next");
        next.setFont(Font.font("Arial", UIInformation.stageWidth() * 0.015));
        next.setPrefWidth(UIInformation.stageWidth() * 0.1);
        next.setPrefHeight(UIInformation.stageHeight() * 0.05);

        next.setOnAction(e -> nextActionHelper());

        return next;
    }

    private static Button resetAction() {
        Button reset = new Button("Reset");
        reset.setFont(Font.font("Arial", UIInformation.stageWidth() * 0.012));
        reset.setPrefWidth(UIInformation.stageWidth() * 0.1);
        reset.setMaxHeight(UIInformation.stageHeight() * 0.05);
        reset.setOnAction(event -> {NextReset.nextReset(true);
            updateTotal();
            updateOrderNumber(CustomerNumber.customerNumber);

            UpdateOrdersLayouts updatingLayouta = new UpdateOrdersLayouts();
            updatingLayouta.updateOrderLayouts();}
        );

        return reset;
    }

    private static Button exitAction() {
        Button exit = new Button("Exit");
        exit.setFont(Font.font("Arial", UIInformation.stageWidth() * 0.015));
        exit.setPrefWidth(UIInformation.stageWidth() * 0.1);
        exit.setPrefHeight(UIInformation.stageHeight() * 0.05);

        exit.setOnAction(event -> System.exit(0));

        return exit;
    }

    private static Button printAction() {
        Button print = new Button("Print");
        print.setFont(Font.font("Arial", UIInformation.stageWidth() * 0.015));
        print.setPrefWidth(UIInformation.stageWidth() * 0.1);
        print.setPrefHeight(UIInformation.stageHeight() * 0.05);

        print.setOnAction(event -> {
            try {
                CustomerReceiptPrinter.CustomerReceiptPrint();
                OrdersReceiptPrinter.OrdersReceiptPrint();
            }
            catch (IOException e)  {
                System.out.println("print didn't work");
            }
        });

        return print;
    }

    private static HBox finalization () {
        HBox finalization = new HBox();
        finalization.setSpacing(UIInformation.stageHeight() * 0.02);

        finalization.getChildren().addAll(exitAction(), resetAction(), nextAction(), printAction());

        finalization.setAlignment(Pos.CENTER);

        return finalization;
    }

    private static HBox paymentMethod() {
        HBox paymentMethodHBox = new HBox();

        Button cashPay = new Button("Cash");
        Button cardPay = new Button("Paid");

        cashPay.setOnAction(event -> {
            cashHelper();
            CashDrawerOpener.cashDrawerOpener();
        });

        cardPay.setOnAction(event -> {
            RecordAllOrders.recordAllOrders("Card");
            nextActionHelper();
        });

        paymentMethodHBox.getChildren().addAll(cashPay, cardPay);

        cashPay.setPrefWidth(UIInformation.stageWidth() * 0.075);
        cashPay.setPrefHeight(UIInformation.stageHeight() * 0.05);

        cardPay.setPrefWidth(UIInformation.stageWidth() * 0.075);
        cardPay.setPrefHeight(UIInformation.stageHeight() * 0.05);

        cashPay.setStyle("-fx-border-color: black;");
        cardPay.setStyle("-fx-border-color: black;");

        return paymentMethodHBox;
    }

    private static final VBox middleVBox = new VBox();
    public static final PaneCreator mainPaneCreator = new PaneCreator();
    public static final Pane mainPane = mainPaneCreator.orderLayout(UIInformation.stageWidth() * 0.53,
            UIInformation.stageHeight() * 0.6);

    public static VBox middleVBox() {
        middleVBox.getChildren().clear();
        middleVBox.setSpacing(UIInformation.stageHeight() * 0.03);

        PopUpButtonLayout popUpButtonLayout = new PopUpButtonLayout();

        middleVBox.getChildren().add(popUpButtonLayout.buttonCreator());
        middleVBox.getChildren().add(mainPane);
        middleVBox.getChildren().add(updateTotal());
        middleVBox.getChildren().add(finalization());

        middleVBox.setAlignment(Pos.CENTER);

        middleVBox.setTranslateY(UIInformation.stageHeight() * 0.03);

        return middleVBox;
    }

    public static void popUpMiddleVBox(Pane popUpPane) {
        middleVBox.getChildren().clear();
        middleVBox.getChildren().add(popUpPane);
    }

    public static final VBox customerTotalDisplay = new VBox();

    public static VBox updateTotal() {
        customerTotalDisplay.getChildren().clear();
        Label customerTotal = new Label("Customer Total: " + Payments.getTotal());
        customerTotal.setFont(new Font(UIInformation.stageHeight() * 0.03));

        customerTotalDisplay.setSpacing(UIInformation.stageHeight() * 0.03);
        customerTotalDisplay.getChildren().add(customerTotal);

        return customerTotalDisplay;
    }

    private static final HBox mainLayout = new HBox();

    public static HBox mainHBoxLayout () {
        mainLayout.setSpacing(UIInformation.stageHeight() * 0.1);

        mainLayout.getChildren().add(buttonNumber());
        mainLayout.getChildren().add(middleVBox());
        mainLayout.getChildren().add(orderFinalization());

        mainLayout.setTranslateX(UIInformation.stageWidth() * 0.03);

        mainLayout.setAlignment(Pos.CENTER);

        return mainLayout;
    }

    private static final StackPane mainStackPane = new StackPane();

    public static StackPane mainStackPaneLayout() {
        mainStackPane.getChildren().add(mainHBoxLayout());
        mainStackPane.setAlignment(Pos.CENTER);

        return mainStackPane;
    }
}

public class Star_Grill_UIModel extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e4dada;");

        Scene scene = new Scene(root, UIInformation.stageWidth(), UIInformation.stageHeight());

        root.getChildren().add(FinalLayout.mainStackPaneLayout());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Star Grill");
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        UIInformation init = new UIInformation();
        launch(args);
    }
}
