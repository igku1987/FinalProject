package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    String fileName = "";
    Alphabet alphabet = new Alphabet();
    int key;
    ArrayList<Character> alphabetCripto = new ArrayList<>();
    HashMap<Character, Double> hashMapTrue = new HashMap<>();
    HashMap<Character, Double> hashMapFalse = new HashMap<>();
    HashMap<Character, Character> hashMap = new HashMap<>();

    @FXML
    private Button open_file;

    @FXML
    private Button encrypt_file;

    @FXML
    private TextArea sourceTextField;

    @FXML
    private TextArea encryptTextField;

    @FXML
    private TextArea decryptWithKeyTextField;

    @FXML
    private Button decryptFileWithKey;

    @FXML
    private TextArea decryptBrutForceTextField;

    @FXML
    private Button decryptFileBrutForce;

    @FXML
    private TextArea decryptSaTextField;

    @FXML
    private Button decryptFileSa;

    @FXML
    private Button loadReferenceFile;

    @FXML
    private BarChart<String, Number> barTrue;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private BarChart<String, Number> barFalse;

    @FXML
    private CategoryAxis xAxis1;

    @FXML
    private NumberAxis yAxis1;

    @FXML
    private TextField keyField;

    @FXML
    private Label file_name;

    @FXML
    private Button showStat;

    static File openDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open TXT files");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("texts"));
        File file1 = fileChooser.showOpenDialog(null);
        return file1;
    }

    @FXML
    void initialize() throws IOException {

        ArrayList<Character> alphabetRus = alphabet.CreateAlphabetRus();
        xAxis.setLabel("Letters of the alphabet");
        yAxis.setLabel("Probability of occurrence");
        xAxis1.setLabel("Letters of the alphabet");
        yAxis1.setLabel("Probability of occurrence");
        open_file.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                encrypt_file.setDisable(false);
                decryptFileBrutForce.setDisable(false);
                loadReferenceFile.setDisable(false);
                decryptFileWithKey.setDisable(false);
                encrypt_file.setDisable(false);
                fileName = openDialog().getPath();
                file_name.setText(fileName);
                try {
                    sourceTextField.setText(Files.readString(Paths.get(fileName)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        encrypt_file.setOnAction(actionEvent -> {

            try {
                key = Integer.valueOf(keyField.getText());
            }
            catch (NumberFormatException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Key error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid key value!");
                alert.showAndWait();

            }
            if (!keyField.getText().isEmpty()) {
                try {
                    alphabetCripto = alphabet.createAlphabetCripto(alphabetRus,key);
                    encryptTextField.setText(alphabet.ceaserCript(Path.of(fileName),alphabetRus,alphabetCripto));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        decryptFileWithKey.setOnAction(actionEvent -> {
            String fileDecrypt = openDialog().getPath();
                try {
                    decryptWithKeyTextField.setText(alphabet.ceaserKeyDecript(Path.of(fileDecrypt),alphabetCripto,key));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });

        decryptFileBrutForce.setOnAction(actionEvent -> {
            String fileDecrypt = openDialog().getPath();
            try {
                decryptBrutForceTextField.setText(alphabet.ceaserDecript(Path.of(fileDecrypt),alphabetCripto));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadReferenceFile.setOnAction(actionEvent -> {
            decryptFileSa.setDisable(false);
            String fileReference = openDialog().getPath();
            try {
                hashMapTrue = alphabet.Statistic(Path.of(fileReference), alphabetRus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        decryptFileSa.setOnAction(actionEvent -> {
            String fileDecrypt = openDialog().getPath();
            try {
                hashMapFalse = alphabet.Statistic(Path.of(fileDecrypt), alphabetRus);
                ArrayList<Character> keys = alphabet.sortHash(hashMapTrue);
                ArrayList<Character> values = alphabet.sortHash(hashMapFalse);
                hashMap = alphabet.keyHash(keys,values);
                decryptSaTextField.setText(alphabet.decryptStat(Path.of(fileDecrypt),hashMap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        showStat.setOnAction(actionEvent -> {
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
            XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
            ArrayList<Character> charactersTrue = new ArrayList<>(hashMapTrue.keySet());
            ArrayList<Double> probabilitiesTrue = new ArrayList<>(hashMapTrue.values());
            ArrayList<Character> charactersFalse = new ArrayList<>(hashMapFalse.keySet());
            ArrayList<Double> probabilitiesFalse = new ArrayList<>(hashMapFalse.values());
            xAxis.setAnimated(false);
            xAxis1.setAnimated(false);
            for (int i = 0; i < charactersTrue.size(); i++) {
                dataSeries.getData().add(new XYChart.Data<>(String.valueOf(charactersTrue.get(i)), probabilitiesTrue.get(i)));
            }
            for (int i = 0; i < charactersFalse.size(); i++) {
                dataSeries1.getData().add(new XYChart.Data<>(String.valueOf(charactersFalse.get(i)), probabilitiesFalse.get(i)));
            }
            barTrue.getData().add(dataSeries);
            barFalse.getData().add(dataSeries1);
        });
    }
}