package ui;

import database.DatabaseStorage;
import domain.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import manager.*;
import user.AuthService;
import user.UserRepository;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Простое JavaFX окно. UI только вызывает менеджер и хранилище.
 */
public class ExperimentResultsApp extends Application {

    private final CollectionManager manager = new CollectionManager();
    private final UserRepository userRepository = new UserRepository();
    private final AuthService authService = new AuthService(userRepository);
    private final DatabaseStorage databaseStorage = new DatabaseStorage();

    private final ObservableList<Experiment> experimentList = FXCollections.observableArrayList();
    private final ObservableList<Run> runList = FXCollections.observableArrayList();
    private final ObservableList<Result> resultList = FXCollections.observableArrayList();

    private final TableView<Experiment> experimentTable = new TableView<>();
    private final TableView<Run> runTable = new TableView<>();
    private final TableView<Result> resultTable = new TableView<>();

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    private TextField fileField;
    private Label statusLabel;
    private Label userLabel;
    private ProgressBar progressBar;
    private Stage mainStage;

    private TextField experimentNameField;
    private TextArea experimentDescriptionArea;

    private TextField runExperimentIdField;
    private TextField runNameField;

    private TextField resultRunIdField;
    private ComboBox<MeasurementParam> resultParamBox;
    private TextField resultValueField;
    private TextField resultUnitField;
    private TextArea resultCommentArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        showAuthWindow(!authService.hasUsers());
    }

    private void showMainWindow() {

        fileField = new TextField(startFilePath());
        statusLabel = new Label("Ready");
        userLabel = new Label(authService.getCurrentUsername());
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setPrefWidth(160);

        if (!getParameters().getRaw().isEmpty()) {
            tryLoadAtStart(fileField.getText());
        }

        BorderPane root = new BorderPane();
        root.setTop(createTopPanel());
        root.setCenter(createTabs());
        root.setBottom(createBottomPanel());

        refreshTables();

        mainStage.setTitle("Experiments Results");
        Scene scene = new Scene(root, 1100, 700);
        applyTheme(scene);
        mainStage.setScene(scene);
        mainStage.show();
    }

    private String startFilePath() {
        if (!getParameters().getRaw().isEmpty()) {
            return String.join(" ", getParameters().getRaw()).trim();
        }
        return "data.bin";
    }

    private void showAuthWindow(boolean showRegister) {
        TabPane tabs = new TabPane();
        Tab loginTab = createLoginTab();
        Tab registerTab = createRegisterTab();

        tabs.getTabs().addAll(loginTab, registerTab);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getSelectionModel().select(showRegister ? registerTab : loginTab);

        Label title = new Label("Experiments Results");
        title.getStyleClass().add("app-title");

        VBox root = new VBox(12, title, tabs);
        root.setPadding(new Insets(16));

        mainStage.setTitle("Вход");
        Scene scene = new Scene(root, 380, 270);
        applyTheme(scene);
        mainStage.setScene(scene);
        mainStage.show();
    }

    private Tab createLoginTab() {
        TextField loginField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label messageLabel = new Label();

        Button loginButton = new Button("Войти");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> {
            try {
                authService.login(loginField.getText(), passwordField.getText());
                showMainWindow();
            } catch (Exception e) {
                messageLabel.setText("Ошибка: " + e.getMessage());
            }
        });

        VBox form = createAuthForm(loginField, passwordField, loginButton, messageLabel);
        return new Tab("Вход", form);
    }

    private Tab createRegisterTab() {
        TextField loginField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label messageLabel = new Label();

        Button registerButton = new Button("Зарегистрироваться");
        registerButton.setDefaultButton(true);
        registerButton.setOnAction(event -> {
            try {
                authService.register(loginField.getText(), passwordField.getText());
                authService.login(loginField.getText(), passwordField.getText());
                showMainWindow();
            } catch (Exception e) {
                messageLabel.setText("Ошибка: " + e.getMessage());
            }
        });

        VBox form = createAuthForm(loginField, passwordField, registerButton, messageLabel);
        return new Tab("Регистрация", form);
    }

    private VBox createAuthForm(TextField loginField,
                                PasswordField passwordField,
                                Button actionButton,
                                Label messageLabel) {
        loginField.setPromptText("Логин");
        passwordField.setPromptText("Пароль");
        messageLabel.setWrapText(true);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(new Label("Логин:"), 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);

        VBox form = new VBox(10, grid, actionButton, messageLabel);
        form.setPadding(new Insets(12));
        form.getStyleClass().add("form-panel");
        return form;
    }

    private HBox createTopPanel() {
        userLabel.getStyleClass().add("user-name");

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshTables());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveFile());

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> loadFile());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> logout());

        Region spacer = new Region();

        HBox panel = new HBox(8,
                userLabel,
                refreshButton,
                saveButton,
                loadButton,
                spacer,
                logoutButton
        );
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.getStyleClass().add("top-panel");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return panel;
    }

    private HBox createBottomPanel() {
        HBox panel = new HBox(10, progressBar, statusLabel);
        panel.setPadding(new Insets(8));
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.getStyleClass().add("bottom-panel");
        return panel;
    }

    private TabPane createTabs() {
        TabPane pane = new TabPane();
        pane.getTabs().add(createExperimentTab());
        pane.getTabs().add(createRunTab());
        pane.getTabs().add(createResultTab());
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return pane;
    }

    private Tab createExperimentTab() {
        prepareExperimentTable();

        experimentNameField = new TextField();
        experimentDescriptionArea = new TextArea();
        experimentDescriptionArea.setPrefRowCount(3);

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.add(new Label("Name:"), 0, 0);
        form.add(experimentNameField, 1, 0);
        form.add(new Label("Description:"), 0, 1);
        form.add(experimentDescriptionArea, 1, 1);

        Button createButton = new Button("Create");
        createButton.setOnAction(event -> createExperiment());

        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteExperiment());

        VBox right = new VBox(10, form, new HBox(8, createButton, deleteButton));
        right.setPadding(new Insets(10));
        right.setPrefWidth(330);
        right.getStyleClass().add("form-panel");

        return createTab("Experiments", experimentTable, right);
    }

    private Tab createRunTab() {
        prepareRunTable();

        runExperimentIdField = new TextField();
        runNameField = new TextField();

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.add(new Label("Experiment ID:"), 0, 0);
        form.add(runExperimentIdField, 1, 0);
        form.add(new Label("Name:"), 0, 1);
        form.add(runNameField, 1, 1);

        Button createButton = new Button("Create");
        createButton.setOnAction(event -> createRun());

        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteRun());

        VBox right = new VBox(10, form, new HBox(8, createButton, deleteButton));
        right.setPadding(new Insets(10));
        right.setPrefWidth(330);
        right.getStyleClass().add("form-panel");

        return createTab("Runs", runTable, right);
    }

    private Tab createResultTab() {
        prepareResultTable();

        resultRunIdField = new TextField();
        resultParamBox = new ComboBox<>(FXCollections.observableArrayList(MeasurementParam.values()));
        resultParamBox.getSelectionModel().selectFirst();
        resultValueField = new TextField();
        resultUnitField = new TextField();
        resultCommentArea = new TextArea();
        resultCommentArea.setPrefRowCount(3);

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.add(new Label("Run ID:"), 0, 0);
        form.add(resultRunIdField, 1, 0);
        form.add(new Label("Param:"), 0, 1);
        form.add(resultParamBox, 1, 1);
        form.add(new Label("Value:"), 0, 2);
        form.add(resultValueField, 1, 2);
        form.add(new Label("Unit:"), 0, 3);
        form.add(resultUnitField, 1, 3);
        form.add(new Label("Comment:"), 0, 4);
        form.add(resultCommentArea, 1, 4);

        Button createButton = new Button("Create");
        createButton.setOnAction(event -> createResult());

        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteResult());

        VBox right = new VBox(10, form, new HBox(8, createButton, deleteButton));
        right.setPadding(new Insets(10));
        right.setPrefWidth(350);
        right.getStyleClass().add("form-panel");

        return createTab("Results", resultTable, right);
    }

    private Tab createTab(String name, TableView<?> table, VBox rightPanel) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(table);
        root.setRight(rightPanel);
        BorderPane.setMargin(rightPanel, new Insets(0, 0, 0, 10));

        Tab tab = new Tab(name);
        tab.setContent(root);
        return tab;
    }

    private void prepareExperimentTable() {
        experimentTable.setItems(experimentList);
        experimentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Experiment, Number> id = new TableColumn<>("ID");
        id.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()));

        TableColumn<Experiment, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Experiment, String> owner = new TableColumn<>("Owner");
        owner.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOwner()));

        TableColumn<Experiment, String> date = new TableColumn<>("Created");
        date.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getCreatedAt())));

        experimentTable.getColumns().setAll(id, name, owner, date);
    }

    private void prepareRunTable() {
        runTable.setItems(runList);
        runTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Run, Number> id = new TableColumn<>("ID");
        id.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()));

        TableColumn<Run, Number> expId = new TableColumn<>("Experiment ID");
        expId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getExperimentId()));

        TableColumn<Run, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Run, String> operator = new TableColumn<>("Operator");
        operator.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOperator()));

        runTable.getColumns().setAll(id, expId, name, operator);
    }

    private void prepareResultTable() {
        resultTable.setItems(resultList);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Result, Number> id = new TableColumn<>("ID");
        id.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()));

        TableColumn<Result, Number> runId = new TableColumn<>("Run ID");
        runId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getRunId()));

        TableColumn<Result, String> param = new TableColumn<>("Param");
        param.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getParam())));

        TableColumn<Result, Number> value = new TableColumn<>("Value");
        value.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getValue()));

        TableColumn<Result, String> unit = new TableColumn<>("Unit");
        unit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnit()));

        TableColumn<Result, String> comment = new TableColumn<>("Comment");
        comment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComment()));

        resultTable.getColumns().setAll(id, runId, param, value, unit, comment);
    }

    private void refreshTables() {
        experimentList.setAll(manager.getAllExperiments().values());
        runList.setAll(manager.getAllRuns().values());
        resultList.setAll(manager.getAllResults().values());
        setStatus("Tables refreshed");
    }

    private void createExperiment() {
        try {
            manager.createExperiment(
                    experimentNameField.getText(),
                    experimentDescriptionArea.getText(),
                    authService.getCurrentUsername()
            );
            experimentNameField.clear();
            experimentDescriptionArea.clear();
            showInfo("Эксперимент создан. Нажмите Refresh, чтобы обновить таблицу.");
        } catch (Exception e) {
            showError("Ошибка создания эксперимента", e.getMessage());
        }
    }

    private void createRun() {
        try {
            long experimentId = parseLong(runExperimentIdField.getText(), "Experiment ID");
            manager.createRun(experimentId, runNameField.getText(), authService.getCurrentUsername());
            runExperimentIdField.clear();
            runNameField.clear();
            showInfo("Запуск создан. Нажмите Refresh, чтобы обновить таблицу.");
        } catch (Exception e) {
            showError("Ошибка создания запуска", e.getMessage());
        }
    }

    private void createResult() {
        try {
            long runId = parseLong(resultRunIdField.getText(), "Run ID");
            double value = parseDouble(resultValueField.getText(), "Value");

            manager.createResult(
                    runId,
                    resultParamBox.getValue(),
                    value,
                    resultUnitField.getText(),
                    resultCommentArea.getText()
            );

            resultRunIdField.clear();
            resultValueField.clear();
            resultUnitField.clear();
            resultCommentArea.clear();
            showInfo("Результат создан. Нажмите Refresh, чтобы обновить таблицу.");
        } catch (Exception e) {
            showError("Ошибка создания результата", e.getMessage());
        }
    }

    private void deleteExperiment() {
        Experiment experiment = experimentTable.getSelectionModel().getSelectedItem();
        if (experiment == null) {
            showError("Ошибка удаления", "Выберите эксперимент в таблице.");
            return;
        }

        try {
            if (!experiment.getOwnerUsername().equals(authService.getCurrentUsername())) {
                showError("Ошибка удаления", "У вас нет прав на удаление этого объекта.");
                return;
            }
            manager.removeExperiment(experiment.getId());
            showInfo("Эксперимент удален. Нажмите Refresh, чтобы обновить таблицу.");
        } catch (Exception e) {
            showError("Ошибка удаления", e.getMessage());
        }
    }

    private void deleteRun() {
        Run run = runTable.getSelectionModel().getSelectedItem();
        if (run == null) {
            showError("Ошибка удаления", "Выберите запуск в таблице.");
            return;
        }

        try {
            if (!run.getOperatorUsername().equals(authService.getCurrentUsername())) {
                showError("Ошибка удаления", "У вас нет прав на удаление этого объекта.");
                return;
            }
            manager.removeRun(run.getId());
            showInfo("Запуск удален. Нажмите Refresh, чтобы обновить таблицу.");
        } catch (Exception e) {
            showError("Ошибка удаления", e.getMessage());
        }
    }

    private void deleteResult() {
        Result result = resultTable.getSelectionModel().getSelectedItem();
        if (result == null) {
            showError("Ошибка удаления", "Выберите результат в таблице.");
            return;
        }

        try {
            Run run = manager.getRunById(result.getRunId());
            if (!run.getOperatorUsername().equals(authService.getCurrentUsername())) {
                showError("Ошибка удаления", "У вас нет прав на удаление этого объекта.");
                return;
            }
            manager.removeResult(result.getId());
            showInfo("Результат удален. Нажмите Refresh, чтобы обновить таблицу.");
        } catch (Exception e) {
            showError("Ошибка удаления", e.getMessage());
        }
    }

    private void saveFile() {
        try {
            databaseStorage.save(manager, userRepository);
            setStatus("Данные выгружены в БД");
            showInfo("Данные выгружены в БД.");
        } catch (Exception e) {
            setStatus("Ошибка БД");
            showError("Ошибка выгрузки в БД", e.getMessage());
        }
    }

    private void loadFile() {
        try {
            databaseStorage.load(manager, userRepository);
            refreshTables();
            setStatus("Данные загружены из БД");
            showInfo("Данные загружены из БД.");
        } catch (Exception e) {
            setStatus("Ошибка БД");
            showError("Ошибка загрузки из БД", e.getMessage());
        }
    }

    private void logout() {
        authService.logout();
        showAuthWindow(false);
    }

    private void tryLoadAtStart(String fileName) {
        setStatus("File storage disabled");
    }

    private void runFileTask(String status, FileAction action, String successMessage) {
        progressBar.setVisible(true);
        progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        setStatus(status);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                action.run();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            progressBar.setVisible(false);
            setStatus(successMessage);
            showInfo(successMessage);
        });

        task.setOnFailed(event -> {
            progressBar.setVisible(false);
            Throwable error = task.getException();
            setStatus("Error");
            showError("Ошибка файла", error == null ? "неизвестная ошибка" : error.getMessage());
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose data file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data files", "*.bin"));

        File file = chooser.showSaveDialog(mainStage);
        if (file != null) {
            fileField.setText(file.getAbsolutePath());
        }
    }

    private long parseLong(String text, String field) {
        try {
            long value = Long.parseLong(text.trim());
            if (value <= 0) {
                throw new IllegalArgumentException(field + " должен быть положительным.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " должен быть числом.");
        }
    }

    private double parseDouble(String text, String field) {
        try {
            double value = Double.parseDouble(text.trim());
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException(field + " должен быть обычным числом.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " должен быть числом.");
        }
    }

    private String formatDate(Instant date) {
        if (date == null) {
            return "";
        }
        return dateFormatter.format(date);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message == null ? "Неизвестная ошибка" : message);
        alert.showAndWait();
    }

    private void applyTheme(Scene scene) {
        String resource = getClass().getResource("purple-theme.css") == null
                ? null
                : getClass().getResource("purple-theme.css").toExternalForm();

        if (resource != null) {
            scene.getStylesheets().add(resource);
            return;
        }

        File cssFile = new File("src/ui/purple-theme.css");
        if (cssFile.exists()) {
            scene.getStylesheets().add(cssFile.toURI().toString());
        }
    }

    private void setStatus(String text) {
        statusLabel.setText(text);
    }

    private interface FileAction {
        void run() throws Exception;
    }
}
