package ui;

import domain.Experiment;
import domain.Result;
import domain.Run;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

final class ExperimentTables {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    private final ObservableList<Experiment> experimentRows = FXCollections.observableArrayList();
    private final ObservableList<Run> runRows = FXCollections.observableArrayList();
    private final ObservableList<Result> resultRows = FXCollections.observableArrayList();

    private final TableView<Experiment> experimentTable = new TableView<>(experimentRows);
    private final TableView<Run> runTable = new TableView<>(runRows);
    private final TableView<Result> resultTable = new TableView<>(resultRows);

    ExperimentTables() {
        configureTables();
    }

    VBox createExperimentView(Node form) {
        return createEntityPane("Experiments", experimentTable, form);
    }

    VBox createRunView(Node form) {
        return createEntityPane("Runs", runTable, form);
    }

    VBox createResultView(Node form) {
        return createEntityPane("Results", resultTable, form);
    }

    Experiment selectedExperiment() {
        return experimentTable.getSelectionModel().getSelectedItem();
    }

    Run selectedRun() {
        return runTable.getSelectionModel().getSelectedItem();
    }

    Result selectedResult() {
        return resultTable.getSelectionModel().getSelectedItem();
    }

    void rebuild(Map<Long, Experiment> experiments,
                 Map<Long, Run> runs,
                 Map<Long, Result> results,
                 Long selectedExperimentId,
                 Long selectedRunId,
                 Long selectedResultId) {
        experimentRows.setAll(new TreeMap<>(experiments).values());
        runRows.setAll(new TreeMap<>(runs).values());
        resultRows.setAll(new TreeMap<>(results).values());

        selectExperiment(selectedExperimentId);
        selectRun(selectedRunId);
        selectResult(selectedResultId);
    }

    private VBox createEntityPane(String title, TableView<?> table, Node form) {
        Label label = new Label(title);
        label.setStyle("-fx-font-weight: bold;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        VBox pane = new VBox(10, label, table, form);
        pane.setPadding(new Insets(10));
        VBox.setVgrow(table, Priority.ALWAYS);
        return pane;
    }

    private void configureTables() {
        TableColumn<Experiment, Long> experimentId = new TableColumn<>("ID");
        experimentId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        TableColumn<Experiment, String> experimentName = new TableColumn<>("Name");
        experimentName.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        TableColumn<Experiment, String> experimentOwner = new TableColumn<>("Owner");
        experimentOwner.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getOwner()));
        TableColumn<Experiment, String> experimentCreated = new TableColumn<>("Created");
        experimentCreated.setCellValueFactory(data -> new ReadOnlyStringWrapper(formatInstant(data.getValue().getCreatedAt())));
        experimentTable.getColumns().setAll(List.of(experimentId, experimentName, experimentOwner, experimentCreated));

        TableColumn<Run, Long> runId = new TableColumn<>("ID");
        runId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        TableColumn<Run, Long> runExperimentId = new TableColumn<>("Experiment ID");
        runExperimentId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getExperimentId()));
        TableColumn<Run, String> runName = new TableColumn<>("Name");
        runName.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        TableColumn<Run, String> runOperator = new TableColumn<>("Operator");
        runOperator.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getOperator()));
        runTable.getColumns().setAll(List.of(runId, runExperimentId, runName, runOperator));

        TableColumn<Result, Long> resultId = new TableColumn<>("ID");
        resultId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        TableColumn<Result, Long> resultRunId = new TableColumn<>("Run ID");
        resultRunId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getRunId()));
        TableColumn<Result, String> resultParam = new TableColumn<>("Param");
        resultParam.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getParam())));
        TableColumn<Result, Double> resultValue = new TableColumn<>("Value");
        resultValue.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getValue()));
        TableColumn<Result, String> resultUnit = new TableColumn<>("Unit");
        resultUnit.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUnit()));
        TableColumn<Result, String> resultComment = new TableColumn<>("Comment");
        resultComment.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getComment()));
        resultTable.getColumns().setAll(List.of(resultId, resultRunId, resultParam, resultValue, resultUnit, resultComment));
    }

    private void selectExperiment(Long id) {
        selectById(experimentTable, id, Experiment::getId);
        if (experimentTable.getSelectionModel().getSelectedItem() == null && !experimentRows.isEmpty()) {
            experimentTable.getSelectionModel().selectFirst();
        }
    }

    private void selectRun(Long id) {
        selectById(runTable, id, Run::getId);
        if (runTable.getSelectionModel().getSelectedItem() == null && !runRows.isEmpty()) {
            runTable.getSelectionModel().selectFirst();
        }
    }

    private void selectResult(Long id) {
        selectById(resultTable, id, Result::getId);
        if (resultTable.getSelectionModel().getSelectedItem() == null && !resultRows.isEmpty()) {
            resultTable.getSelectionModel().selectFirst();
        }
    }

    private <T> void selectById(TableView<T> table, Long id, LongExtractor<T> extractor) {
        table.getSelectionModel().clearSelection();
        if (id == null) {
            return;
        }
        for (T item : table.getItems()) {
            if (extractor.idOf(item) == id) {
                table.getSelectionModel().select(item);
                table.scrollTo(item);
                return;
            }
        }
    }

    private String formatInstant(Instant instant) {
        if (instant == null) {
            return "";
        }
        return DATE_FORMAT.format(instant);
    }

    @FunctionalInterface
    private interface LongExtractor<T> {
        long idOf(T value);
    }
}
