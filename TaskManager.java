import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Collections;
import java.util.Optional;

public class TaskManager extends Application {

    private ObservableList<String> tasks;
    private ListView<String> taskListView;
    private TextField taskInputField;

    private final String SAVE_FILE = "tasks.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Manager");

        tasks = FXCollections.observableArrayList();

        initTaskListView();
        initTaskInputField();
        loadTasks();

        BorderPane borderPane = createBorderPane();

        Scene scene = new Scene(borderPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initTaskListView() {
        taskListView = new ListView<>(tasks);
        taskListView.setPrefWidth(300);
    }

    private void initTaskInputField() {
        taskInputField = new TextField();
        taskInputField.setPromptText("Enter a new task");
    }

    private BorderPane createBorderPane() {
        Button addButton = new Button("Add Task");
        addButton.setOnAction(event -> addTask());

        Button deleteButton = new Button("Delete Task");
        deleteButton.setOnAction(event -> confirmDeleteTask());

        Button editButton = new Button("Edit Task");
        editButton.setOnAction(event -> editTask());

        Button saveButton = new Button("Save Tasks");
        saveButton.setOnAction(event -> saveTasks());

        Button loadButton = new Button("Load Tasks");
        loadButton.setOnAction(event -> loadTasks());

        Button sortButton = new Button("Sort Tasks");
        sortButton.setOnAction(event -> sortTasks());

        HBox inputBox = new HBox(taskInputField, addButton, deleteButton, editButton, saveButton, loadButton, sortButton);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(taskListView);
        borderPane.setBottom(inputBox);
        return borderPane;
    }

    private void addTask() {
        String newTask = taskInputField.getText();
        if (!newTask.isEmpty()) {
            tasks.add(newTask);
            taskInputField.clear();
        }
    }

    private void confirmDeleteTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedTask = taskListView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Task");
            alert.setContentText("Are you sure you want to delete the task: \"" + selectedTask + "\"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteTask(selectedIndex);
            }
        }
    }

    private void deleteTask(int index) {
        tasks.remove(index);
    }

    private void editTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String editedTask = taskInputField.getText();
            if (!editedTask.isEmpty()) {
                tasks.set(selectedIndex, editedTask);
                taskInputField.clear();
                taskListView.getSelectionModel().clearSelection();
            }
        }
    }

    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (String task : tasks) {
                writer.println(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortTasks() {
        Collections.sort(tasks);
    }
}
