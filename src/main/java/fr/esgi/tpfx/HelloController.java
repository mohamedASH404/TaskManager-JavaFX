package fr.esgi.tpfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.stage.FileChooser;

public class HelloController implements Initializable {

    @FXML
    private TextField newTask;

    @FXML
    private ListView<String> taskList;

    private final String FILE_NAME = "data.txt";

    // Ajouter une tâche
    @FXML
    private void addNewTask() {
        String task = newTask.getText().trim();
        if (task.isEmpty()) {
            System.out.println("Champ vide !");
        } else {
            taskList.getItems().add(task);
            newTask.clear();
        }
    }

    @FXML
    private void deleteTask() {
        String selected = taskList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskList.getItems().remove(selected);
        } else {
            System.out.println("Aucune tâche sélectionnée.");
        }
    }

    @FXML
    public void exitProgram(ActionEvent e) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            taskList.getItems().forEach(task -> {
                try {
                    writer.write(task + "\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException ex) {
            System.out.println("Erreur de sauvegarde : " + ex.getMessage());
        }
        System.out.println("Fermeture de l'application...");
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Fichier non trouvé. Aucun chargement.");
            showErrorDialog();
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\n");
            scanner.forEachRemaining(task -> taskList.getItems().add(task));
        } catch (FileNotFoundException e) {
            System.out.println("Erreur de lecture : " + e.getMessage());
            showErrorDialog();
        }
    }
    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Chargement impossible");
        alert.setContentText("Le fichier data.txt est introuvable.");
        alert.showAndWait();
    }


    @FXML
    private void saveFromDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir où sauvegarder le fichier");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt"));

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try (FileWriter writer = new FileWriter(selectedFile)) {
                taskList.getItems().forEach(task -> {
                    try {
                        writer.write(task + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                System.out.println("Fichier sauvegardé !");
            } catch (IOException e) {
                System.out.println("Erreur de sauvegarde : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }

    @FXML
    private void loadFromDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier à charger");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            taskList.getItems().clear(); // vider avant de charger

            try (Scanner scanner = new Scanner(selectedFile)) {
                scanner.useDelimiter("\n");
                scanner.forEachRemaining(task -> taskList.getItems().add(task));
            } catch (FileNotFoundException e) {
                System.out.println("Erreur de chargement : " + e.getMessage());
                showErrorDialog();
            }
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }




}