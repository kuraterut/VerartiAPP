import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.*;


public class Help extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        int rows = 5;
        int cols = 5;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Создаем прямоугольник (ячейку)
                Rectangle cell = new Rectangle(100, 100, Color.TRANSPARENT); // Прозрачный
                final int roww = row;
                final int coll = col;
                cell.setOnMouseClicked(event -> showCellInfo(roww, coll));

                // Создаем текст для отображения в ячейке
                Text text = new Text("R: " + row + "\nC: " + col);
                text.setFill(Color.BLACK); // Цвет текста

                // Добавляем элементы в GridPane
                gridPane.add(text, col, row); // Текст поверх прямоугольника
                gridPane.add(cell, col, row);
            }
        }

        Scene scene = new Scene(gridPane, 600, 600);
        primaryStage.setTitle("Clickable Grid with Text");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCellInfo(int row, int col) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Cell Information");
        alert.setHeaderText(null);
        alert.setContentText("You clicked on cell at Row: " + row + ", Column: " + col);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}