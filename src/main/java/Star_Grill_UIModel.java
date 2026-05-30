import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Star_Grill_UIModel extends Application {
    public static final Path filePath = Path.of("C:\\Users\\hassa\\IdeaProjects\\Star_Grill\\Star_Grill_info\\food_options");
    public static Map<String, Integer> foodPrice = new HashMap<>();
    public static double stageWidth;
    public static double stageHeight;
    public static Integer foodNumber;

    public static void setFoodPrice() {
        try {
            List<String> lines = Files.readAllLines(filePath);

            foodNumber = lines.size();

            for (String line : lines) {

                String[] newLine = line.split(" ");
                foodPrice.put(newLine[0], Integer.parseInt(newLine[1]));
            }

        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }

    public VBox buttonCreator() {
        VBox buttonLayout = new VBox();
        buttonLayout.setSpacing(stageHeight);

        for (String keys: foodPrice.keySet()) {
            Button button = new Button();
            button.setText(keys);
            buttonLayout.getChildren().add(button);
        }
        return buttonLayout;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initializer
        stageWidth = primaryStage.getWidth();
        stageHeight = primaryStage.getHeight();
        setFoodPrice();

        primaryStage.setTitle("Star Grill"); // Making a stage
        primaryStage.setFullScreen(true);

        Pane root = new Pane();
        root.setStyle("-fx-background-color: #e4dada;");

        Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        VBox buttonLayout = buttonCreator();
        root.getChildren().add(buttonLayout);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}