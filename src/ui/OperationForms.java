package ui;

import domain.MeasurementParam;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

final class OperationForms {

    private final Runnable createExperiment;
    private final Runnable deleteExperiment;
    private final Runnable createRun;
    private final Runnable deleteRun;
    private final Runnable createResult;
    private final Runnable deleteResult;

    private final VBox experimentPane;
    private final VBox runPane;
    private final VBox resultPane;

    private TextField experimentNameField;
    private TextField experimentOwnerField;
    private TextArea experimentDescriptionArea;

    private TextField runExperimentIdField;
    private TextField runNameField;
    private TextField runOperatorField;

    private TextField resultRunIdField;
    private ComboBox<MeasurementParam> resultParamBox;
    private TextField resultValueField;
    private TextField resultUnitField;
    private TextArea resultCommentArea;

    OperationForms(Runnable createExperiment,
                   Runnable deleteExperiment,
                   Runnable createRun,
                   Runnable deleteRun,
                   Runnable createResult,
                   Runnable deleteResult) {
        this.createExperiment = createExperiment;
        this.deleteExperiment = deleteExperiment;
        this.createRun = createRun;
        this.deleteRun = deleteRun;
        this.createResult = createResult;
        this.deleteResult = deleteResult;

        experimentPane = createExperimentPane();
        runPane = createRunPane();
        resultPane = createResultPane();
    }

    VBox experimentPane() {
        return experimentPane;
    }

    VBox runPane() {
        return runPane;
    }

    VBox resultPane() {
        return resultPane;
    }

    String experimentName() {
        return experimentNameField.getText();
    }

    String experimentOwner() {
        return experimentOwnerField.getText();
    }

    String experimentDescription() {
        return experimentDescriptionArea.getText();
    }

    String runName() {
        return runNameField.getText();
    }

    String runExperimentId() {
        return runExperimentIdField.getText();
    }

    String runOperator() {
        return runOperatorField.getText();
    }

    String resultRunId() {
        return resultRunIdField.getText();
    }

    MeasurementParam resultParam() {
        return resultParamBox.getValue();
    }

    String resultValue() {
        return resultValueField.getText();
    }

    String resultUnit() {
        return resultUnitField.getText();
    }

    String resultComment() {
        return resultCommentArea.getText();
    }

    void clearExperimentForm() {
        experimentNameField.clear();
        experimentOwnerField.clear();
        experimentDescriptionArea.clear();
    }

    void clearRunForm() {
        runExperimentIdField.clear();
        runNameField.clear();
        runOperatorField.clear();
    }

    void clearResultForm() {
        resultRunIdField.clear();
        resultValueField.clear();
        resultUnitField.clear();
        resultCommentArea.clear();
        resultParamBox.getSelectionModel().selectFirst();
    }

    private VBox createExperimentPane() {
        experimentNameField = new TextField();
        experimentOwnerField = new TextField();
        experimentDescriptionArea = new TextArea();
        experimentDescriptionArea.setPrefRowCount(3);
        experimentDescriptionArea.setWrapText(true);

        GridPane form = createFormGrid();
        addLabeled(form, 0, "Name", experimentNameField);
        addLabeled(form, 1, "Owner", experimentOwnerField);
        addLabeled(form, 2, "Description", experimentDescriptionArea);

        VBox content = new VBox(
                10,
                form,
                new HBox(8, actionButton("Create experiment", createExperiment),
                        actionButton("Delete selected experiment", deleteExperiment))
        );
        content.setPadding(new Insets(12));
        return content;
    }

    private VBox createRunPane() {
        runExperimentIdField = new TextField();
        runNameField = new TextField();
        runOperatorField = new TextField();

        GridPane form = createFormGrid();
        addLabeled(form, 0, "Experiment ID", runExperimentIdField);
        addLabeled(form, 1, "Name", runNameField);
        addLabeled(form, 2, "Operator", runOperatorField);

        VBox content = new VBox(
                10,
                form,
                new HBox(8, actionButton("Create run", createRun),
                        actionButton("Delete selected run", deleteRun))
        );
        content.setPadding(new Insets(12));
        return content;
    }

    private VBox createResultPane() {
        resultRunIdField = new TextField();
        resultParamBox = new ComboBox<>(FXCollections.observableArrayList(MeasurementParam.values()));
        resultParamBox.getSelectionModel().selectFirst();

        resultValueField = new TextField();
        resultUnitField = new TextField();
        resultCommentArea = new TextArea();
        resultCommentArea.setPrefRowCount(3);
        resultCommentArea.setWrapText(true);

        GridPane form = createFormGrid();
        addLabeled(form, 0, "Run ID", resultRunIdField);
        addLabeled(form, 1, "Parameter", resultParamBox);
        addLabeled(form, 2, "Value", resultValueField);
        addLabeled(form, 3, "Unit", resultUnitField);
        addLabeled(form, 4, "Comment", resultCommentArea);

        VBox content = new VBox(
                10,
                form,
                new HBox(8, actionButton("Create result", createResult),
                        actionButton("Delete selected result", deleteResult))
        );
        content.setPadding(new Insets(12));
        return content;
    }

    private Button actionButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(event -> action.run());
        return button;
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        return grid;
    }

    private void addLabeled(GridPane grid, int row, String label, Node control) {
        javafx.scene.control.Label fieldLabel = new javafx.scene.control.Label(label + ":");
        grid.add(fieldLabel, 0, row);
        grid.add(control, 1, row);
        GridPane.setHgrow(control, Priority.ALWAYS);
        if (control instanceof TextArea) {
            GridPane.setVgrow(control, Priority.ALWAYS);
        }
    }
}
