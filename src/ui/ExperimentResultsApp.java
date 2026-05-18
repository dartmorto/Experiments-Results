package ui;

import domain.Experiment;
import domain.Result;
import domain.Run;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manager.CollectionManager;
import storage.FileStorage;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * JavaFX interface for working with experiments, runs and measurement results.
 */
public final class ExperimentResultsApp extends Application {

    private final CollectionManager manager;
    private final FileStorage storage;
    private final BackgroundWorker background;
    private final AdditionHistory history;

    private ExperimentTables tables;
    private OperationForms forms;
    private TextField sourcePathField;
    private Label statusLabel;

    public ExperimentResultsApp() {
        this(new CollectionManager(), new FileStorage(), new AdditionHistory());
    }

    ExperimentResultsApp(CollectionManager manager, FileStorage storage, AdditionHistory history) {
        this.manager = Objects.requireNonNull(manager, "manager");
        this.storage = Objects.requireNonNull(storage, "storage");
        this.history = Objects.requireNonNull(history, "history");
        this.background = new BackgroundWorker(this::setStatus, UiDialogs::showError);
    }

    @Override
    public void start(Stage stage) {
        sourcePathField = new TextField(defaultSourcePath());
        sourcePathField.setPrefColumnCount(36);

        tables = new ExperimentTables();
        forms = new OperationForms(
                this::createExperiment,
                this::deleteSelectedExperiment,
                this::createRun,
                this::deleteSelectedRun,
                this::createResult,
                this::deleteSelectedResult
        );

        BorderPane root = new BorderPane();
        root.setTop(createToolbar(stage));
        root.setCenter(createContent());
        root.setBottom(createStatusBar());

        Scene scene = new Scene(root, 1180, 720);
        stage.setTitle("Experiments Results");
        stage.setMinWidth(960);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
        setStatus("Press Load to load data from source.");
    }

    @Override
    public void stop() {
        background.shutdown();
    }

    private String defaultSourcePath() {
        if (!getParameters().getRaw().isEmpty()) {
            return normalizePath(String.join(" ", getParameters().getRaw()));
        }
        return Path.of("data.bin").toAbsolutePath().normalize().toString();
    }

    private HBox createToolbar(Stage stage) {
        Button browseButton = new Button("Choose");
        browseButton.setOnAction(event -> chooseSourceFile(stage));

        Button loadButton = new Button("Load");
        loadButton.setDefaultButton(true);
        loadButton.setOnAction(event -> loadFromSource());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveToSource());

        Button clearButton = new Button("Clear local");
        clearButton.setOnAction(event -> clearLocalData());

        HBox toolbar = new HBox(
                8,
                new Label("Source:"),
                sourcePathField,
                browseButton,
                loadButton,
                saveButton,
                new Separator(Orientation.VERTICAL),
                clearButton
        );
        toolbar.setPadding(new Insets(10));
        HBox.setHgrow(sourcePathField, Priority.ALWAYS);
        toolbar.disableProperty().bind(background.busyProperty());
        return toolbar;
    }

    private TabPane createContent() {
        TabPane content = new TabPane(
                createTab("Experiments", tables.createExperimentView(forms.experimentPane())),
                createTab("Runs", tables.createRunView(forms.runPane())),
                createTab("Results", tables.createResultView(forms.resultPane())),
                createTab("History", history.createView())
        );
        content.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        content.disableProperty().bind(background.busyProperty());
        return content;
    }

    private Tab createTab(String title, javafx.scene.Node content) {
        return new Tab(title, content);
    }

    private HBox createStatusBar() {
        statusLabel = new Label("Ready");
        ProgressBar progressBar = new ProgressBar(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressBar.setPrefWidth(180);
        progressBar.visibleProperty().bind(background.busyProperty());
        progressBar.managedProperty().bind(progressBar.visibleProperty());

        HBox statusBar = new HBox(12, progressBar, statusLabel);
        statusBar.setPadding(new Insets(8, 10, 10, 10));
        return statusBar;
    }

    private void createExperiment() {
        try {
            Experiment experiment = manager.createExperiment(
                    forms.experimentName(),
                    forms.experimentDescription(),
                    forms.experimentOwner()
            );
            forms.clearExperimentForm();
            rebuildTables(experiment.getId(), null, null);
            history.recordExperiment(experiment);
            setStatus("Experiment created: " + experiment.getId());
        } catch (Exception e) {
            UiDialogs.showError("Cannot create experiment", e);
        }
    }

    private void createRun() {
        try {
            long experimentId = parseId(forms.runExperimentId(), "Experiment ID");
            Run run = manager.createRun(
                    experimentId,
                    forms.runName(),
                    forms.runOperator()
            );
            forms.clearRunForm();
            rebuildTables(experimentId, run.getId(), null);
            history.recordRun(run);
            setStatus("Run created: " + run.getId());
        } catch (Exception e) {
            UiDialogs.showError("Cannot create run", e);
        }
    }

    private void createResult() {
        try {
            long runId = parseId(forms.resultRunId(), "Run ID");
            Run run = manager.getRunById(runId);
            double value = parseDouble(forms.resultValue(), "Value");
            Result result = manager.createResult(
                    runId,
                    forms.resultParam(),
                    value,
                    forms.resultUnit(),
                    forms.resultComment()
            );
            forms.clearResultForm();
            rebuildTables(run.getExperimentId(), runId, result.getId());
            history.recordResult(result);
            setStatus("Result created: " + result.getId());
        } catch (Exception e) {
            UiDialogs.showError("Cannot create result", e);
        }
    }

    private void deleteSelectedExperiment() {
        Experiment selected = tables.selectedExperiment();
        if (selected == null) {
            UiDialogs.showError("Cannot delete experiment", "Select an experiment first.");
            return;
        }
        if (!UiDialogs.confirm("Delete experiment #" + selected.getId() + " and its runs/results?")) {
            return;
        }

        try {
            manager.removeExperiment(selected.getId());
            rebuildTables(null, null, null);
            setStatus("Experiment deleted: " + selected.getId());
        } catch (Exception e) {
            UiDialogs.showError("Cannot delete experiment", e);
        }
    }

    private void deleteSelectedRun() {
        Run selected = tables.selectedRun();
        if (selected == null) {
            UiDialogs.showError("Cannot delete run", "Select a run first.");
            return;
        }
        if (!UiDialogs.confirm("Delete run #" + selected.getId() + " and its results?")) {
            return;
        }

        try {
            long experimentId = selected.getExperimentId();
            manager.removeRun(selected.getId());
            rebuildTables(experimentId, null, null);
            setStatus("Run deleted: " + selected.getId());
        } catch (Exception e) {
            UiDialogs.showError("Cannot delete run", e);
        }
    }

    private void deleteSelectedResult() {
        Result selected = tables.selectedResult();
        if (selected == null) {
            UiDialogs.showError("Cannot delete result", "Select a result first.");
            return;
        }
        if (!UiDialogs.confirm("Delete result #" + selected.getId() + "?")) {
            return;
        }

        try {
            Run resultRun = manager.getRunById(selected.getRunId());
            manager.removeResult(selected.getId());
            rebuildTables(resultRun.getExperimentId(), resultRun.getId(), null);
            setStatus("Result deleted: " + selected.getId());
        } catch (Exception e) {
            UiDialogs.showError("Cannot delete result", e);
        }
    }

    private void loadFromSource() {
        String path;
        try {
            path = currentSourcePath();
        } catch (IllegalArgumentException e) {
            UiDialogs.showError("Cannot load data", e);
            return;
        }

        background.run(
                "Loading data...",
                () -> storage.load(manager, path),
                () -> {
                    rebuildTables(null, null, null);
                    setStatus("Loaded and merged from " + path);
                }
        );
    }

    private void saveToSource() {
        String path;
        try {
            path = currentSourcePath();
        } catch (IllegalArgumentException e) {
            UiDialogs.showError("Cannot save data", e);
            return;
        }

        background.run(
                "Saving data...",
                () -> storage.save(manager, path),
                () -> {
                    rebuildTables(null, null, null);
                    setStatus("Saved and merged to " + path);
                }
        );
    }

    private void clearLocalData() {
        if (!UiDialogs.confirm("Clear local data in this window? The source file will not be changed.")) {
            return;
        }
        manager.replaceData(Map.of(), Map.of(), Map.of());
        rebuildTables(null, null, null);
        setStatus("Local data cleared");
    }

    private void chooseSourceFile(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose data source");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Binary data", "*.bin"));

        String currentPath = sourcePathField.getText();
        if (currentPath != null && !currentPath.isBlank()) {
            File currentFile = Path.of(normalizePath(currentPath)).toFile();
            File parent = currentFile.getAbsoluteFile().getParentFile();
            if (parent != null && parent.isDirectory()) {
                chooser.setInitialDirectory(parent);
            }
            chooser.setInitialFileName(currentFile.getName());
        }

        File selected = chooser.showOpenDialog(stage);
        if (selected != null) {
            sourcePathField.setText(selected.toPath().toAbsolutePath().normalize().toString());
        }
    }

    private String currentSourcePath() {
        String value = normalizePath(sourcePathField.getText());
        if (value.isBlank()) {
            throw new IllegalArgumentException("Source file path is empty.");
        }
        return value;
    }

    private void rebuildTables(Long selectedExperimentId, Long selectedRunId, Long selectedResultId) {
        tables.rebuild(
                manager.getAllExperiments(),
                manager.getAllRuns(),
                manager.getAllResults(),
                selectedExperimentId,
                selectedRunId,
                selectedResultId
        );
    }

    private String normalizePath(String path) {
        if (path == null) {
            return "";
        }
        String result = path.trim();
        if (result.length() >= 2 && result.startsWith("\"") && result.endsWith("\"")) {
            return result.substring(1, result.length() - 1);
        }
        return result;
    }

    private double parseDouble(String raw, String fieldName) {
        try {
            return Double.parseDouble(raw.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    private long parseId(String raw, String fieldName) {
        try {
            long id = Long.parseLong(raw.trim());
            if (id <= 0) {
                throw new IllegalArgumentException(fieldName + " must be positive.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    private void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }
}
