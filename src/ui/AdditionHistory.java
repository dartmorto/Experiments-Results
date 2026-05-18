package ui;

import domain.Experiment;
import domain.Result;
import domain.Run;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

final class AdditionHistory {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    private final ObservableList<String> rows = FXCollections.observableArrayList();
    private final ListView<String> list = new ListView<>(rows);

    VBox createView() {
        Label title = new Label("Addition history");
        title.setStyle("-fx-font-weight: bold;");

        Button clearButton = new Button("Clear history");
        clearButton.setOnAction(event -> rows.clear());

        HBox header = new HBox(8, title, clearButton);
        VBox content = new VBox(10, header, list);
        content.setPadding(new Insets(10));
        VBox.setVgrow(list, Priority.ALWAYS);
        return content;
    }

    void recordExperiment(Experiment experiment) {
        add("Experiment #" + experiment.getId() + ": " + experiment.getName());
    }

    void recordRun(Run run) {
        add("Run #" + run.getId() + " for experiment #" + run.getExperimentId() + ": " + run.getName());
    }

    void recordResult(Result result) {
        add("Result #" + result.getId() + " for run #" + result.getRunId()
                + ": " + result.getParam() + " = " + result.getValue() + " " + result.getUnit());
    }

    private void add(String text) {
        rows.add(0, DATE_FORMAT.format(Instant.now()) + "  " + text);
    }
}
