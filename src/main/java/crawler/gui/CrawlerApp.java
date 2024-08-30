package crawler.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class CrawlerApp extends Application {

    private TextField urlInput;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Web Crawler");

        // Create GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // URL Input
        Label urlLabel = new Label("URL:");
        GridPane.setConstraints(urlLabel, 0, 0);
        urlInput = new TextField();
        urlInput.setPromptText("Enter URL");
        GridPane.setConstraints(urlInput, 1, 0);

        // Package Selection
        Label packageLabel = new Label("Select Package:");
        GridPane.setConstraints(packageLabel, 0, 1);
        ComboBox<String> packageSelect = new ComboBox<>();
        packageSelect.getItems().addAll("OLX", "Publi24");
        packageSelect.setValue("OLX");
        GridPane.setConstraints(packageSelect, 1, 1);

        // Start Button
        Button startButton = new Button("Start");
        GridPane.setConstraints(startButton, 1, 2);
        startButton.setOnAction(e -> processSingleUrl(packageSelect.getValue()));

        // Load File Button
        Button loadFileButton = new Button("Load URLs from File");
        GridPane.setConstraints(loadFileButton, 1, 3);
        loadFileButton.setOnAction(e -> processUrlsFromFile(packageSelect.getValue()));

        // Add all elements to the grid
        grid.getChildren().addAll(urlLabel, urlInput, packageLabel, packageSelect, startButton, loadFileButton);

        // Setup the scene and show the stage
        Scene scene = new Scene(grid, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processSingleUrl(String selectedPackage) {
        String url = urlInput.getText().trim();
        if (url.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "URL cannot be empty");
            return;
        }

        try {
            String result = processUrl(url, selectedPackage);
            writeResultToFile(url, result);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Crawling started successfully.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to start crawler: " + ex.getMessage());
        }
    }

    private void processUrlsFromFile(String selectedPackage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No file selected");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> urls = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());

            if (urls.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No URLs found in file");
                return;
            }

            for (String url : urls) {
                String result = processUrl(url, selectedPackage);
                writeResultToFile(url, result);
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Crawling started successfully for all URLs.");
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read file: " + ex.getMessage());
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to start crawler: " + ex.getMessage());
        }
    }

    private String processUrl(String url, String selectedPackage) throws Exception {
        if (selectedPackage.equals("OLX")) {
            return runOlxCrawler(url);
        } else {
            return runPubli24Crawler(url);
        }
    }

    private String runOlxCrawler(String baseUrl) {
        crawler.olx.PaginationHandler.BASE_URL = baseUrl;
        return crawler.olx.PaginationHandler.runCrawler();
    }

    private String runPubli24Crawler(String baseUrl) {
        crawler.publi24.PaginationHandler.BASE_URL = baseUrl;
        return crawler.publi24.PaginationHandler.runCrawler();
    }

    private void writeResultToFile(String url, String result) {
        String fileName = "results_" + url.hashCode() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("URL: " + url + "\n");
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
