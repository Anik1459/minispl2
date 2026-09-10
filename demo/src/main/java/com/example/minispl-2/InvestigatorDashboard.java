package org.example.java;

import com.example.oopproject.strategy.InvestigatorAssignmentService;
import com.example.oopproject.strategy.SpecializationBasedAssignment;
import com.example.oopproject.strategy.LocationBasedAssignment;
import com.example.oopproject.model.CrimeReport;
import com.example.oopproject.model.Investigator;
import com.example.oopproject.state.CaseContext;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class InvestigatorDashboard extends Application {

    private TableView<Case> caseTable;
    private ObservableList<Case> caseList;
    private ComboBox<String> caseTypeFilter;
    private TextField searchField;
    private Label totalCasesLabel;
    private Label inProgressLabel;
    private Label solvedLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Police Investigation Dashboard");
        primaryStage.setMaximized(true);

        // Initialize database
        DatabaseHelper.initializeDatabaseWithInvestigationTables();

        // Create main layout
        BorderPane root = createMainLayout();

        // Load cases
        loadApprovedCases();
        updateStatistics();

        Scene scene = new Scene(root, 1400, 800);
//        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();

        // Top: Header and Controls
        VBox topSection = createTopSection();
        root.setTop(topSection);

        // Center: Cases Table
        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);

        // Right: Statistics Panel
        VBox rightSection = createStatisticsPanel();
        root.setRight(rightSection);

        return root;
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(20));
        topSection.setStyle("-fx-background-color: #2c3e50;");

        // Header row with back button and title
        HBox headerRow = new HBox(15);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // Back button
        Button backButton = new Button("⬅ Back");
        backButton.setFont(Font.font("System", FontWeight.BOLD, 14));
        backButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8 15; -fx-cursor: hand;");
        
        // Add hover effects
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle("-fx-background-color: #5a6fd8; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8 15; -fx-cursor: hand;");
        });
        backButton.setOnMouseExited(e -> {
            backButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8 15; -fx-cursor: hand;");
        });
        
        backButton.setOnAction(e -> {
            try {
                RoleSelection roleSelection = new RoleSelection();
                Stage currentStage = (Stage) backButton.getScene().getWindow();
                roleSelection.start(currentStage);
            } catch (Exception ex) {
                System.err.println("Error navigating to RoleSelection: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Header label
        Label headerLabel = new Label("🔍 POLICE INVESTIGATION DASHBOARD");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        headerRow.getChildren().addAll(backButton, headerLabel);

        // Controls row
        HBox controlsRow = new HBox(15);
        controlsRow.setAlignment(Pos.CENTER_LEFT);

        // Case type filter
        Label filterLabel = new Label("Filter by Type:");
        filterLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        caseTypeFilter = new ComboBox<>();
        caseTypeFilter.getItems().addAll("All Cases", "Fraud", "Money Laundering",
                "Kidnapping", "Drug Offense", "Extortion", "Robbery");
        caseTypeFilter.setValue("All Cases");
        caseTypeFilter.setOnAction(e -> filterCases());

        // Search field
        Label searchLabel = new Label("Search:");
        searchLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Search by complainant name, location...");
        searchField.setPrefWidth(250);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCases());

        // Refresh button
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshBtn.setOnAction(e -> {
            loadApprovedCases();
            updateStatistics();
        });

        controlsRow.getChildren().addAll(filterLabel, caseTypeFilter, searchLabel, searchField, refreshBtn);

        topSection.getChildren().addAll(headerRow, controlsRow);
        return topSection;
    }

    private VBox createCenterSection() {
        VBox centerSection = new VBox(10);
        centerSection.setPadding(new Insets(20));

        Label tableTitle = new Label("📋 Active Investigation Cases");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create table
        createCaseTable();

        centerSection.getChildren().addAll(tableTitle, caseTable);
        VBox.setVgrow(caseTable, Priority.ALWAYS);
        return centerSection;
    }

    private void createCaseTable() {
        caseTable = new TableView<>();
        caseList = FXCollections.observableArrayList();
        caseTable.setItems(caseList);

        // Case ID Column
        TableColumn<Case, Integer> idCol = new TableColumn<>("Case ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        idCol.setPrefWidth(80);

        // Case Type Column
        TableColumn<Case, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("caseType"));
        typeCol.setPrefWidth(120);

        // Complainant Column
        TableColumn<Case, String> complainantCol = new TableColumn<>("Complainant");
        complainantCol.setCellValueFactory(new PropertyValueFactory<>("complainantName"));
        complainantCol.setPrefWidth(150);

        // Location Column
        TableColumn<Case, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(150);

        // Date Column
        TableColumn<Case, String> dateCol = new TableColumn<>("Incident Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));
        dateCol.setPrefWidth(120);

        // Status Column
        TableColumn<Case, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        // Priority Column
        TableColumn<Case, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(100);

        // Actions Column
        TableColumn<Case, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(200);

        Callback<TableColumn<Case, Void>, TableCell<Case, Void>> cellFactory = new Callback<TableColumn<Case, Void>, TableCell<Case, Void>>() {
            @Override
            public TableCell<Case, Void> call(final TableColumn<Case, Void> param) {
                final TableCell<Case, Void> cell = new TableCell<Case, Void>() {
                    private final Button viewBtn = new Button("👁️ View");
                    private final Button notesBtn = new Button("📝 Notes");
                    private final Button evidenceBtn = new Button("🔍 Evidence");

                    {
                        viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                        notesBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 10px;");
                        evidenceBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 10px;");

                        viewBtn.setOnAction(event -> {
                            Case selectedCase = getTableView().getItems().get(getIndex());
                            showCaseDetails(selectedCase);
                        });

                        notesBtn.setOnAction(event -> {
                            Case selectedCase = getTableView().getItems().get(getIndex());
                            showInvestigationNotes(selectedCase);
                        });

                        evidenceBtn.setOnAction(event -> {
                            Case selectedCase = getTableView().getItems().get(getIndex());
                            showEvidenceManagement(selectedCase);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(5);
                            buttons.setAlignment(Pos.CENTER);
                            buttons.getChildren().addAll(viewBtn, notesBtn, evidenceBtn);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };

        actionsCol.setCellFactory(cellFactory);

        caseTable.getColumns().addAll(idCol, typeCol, complainantCol, locationCol, dateCol, statusCol, priorityCol, actionsCol);
        caseTable.setRowFactory(tv -> {
            TableRow<Case> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showCaseDetails(row.getItem());
                }
            });
            return row;
        });
    }

    private VBox createStatisticsPanel() {
        VBox statsPanel = new VBox(15);
        statsPanel.setPadding(new Insets(20));
        statsPanel.setPrefWidth(300);
        statsPanel.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7;");

        Label statsTitle = new Label("📊 Case Statistics");
        statsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Statistics cards
        totalCasesLabel = createStatCard("Total Cases", "0", "#3498db");
        inProgressLabel = createStatCard("In Progress", "0", "#f39c12");
        solvedLabel = createStatCard("Solved", "0", "#27ae60");

        // Case type breakdown
        Label breakdownTitle = new Label("Case Type Breakdown:");
        breakdownTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        VBox typeBreakdown = new VBox(5);
        typeBreakdown.getChildren().addAll(
                createTypeStatLabel("Fraud", 0),
                createTypeStatLabel("Money Laundering", 0),
                createTypeStatLabel("Kidnapping", 0),
                createTypeStatLabel("Drug Offense", 0),
                createTypeStatLabel("Extortion", 0),
                createTypeStatLabel("Robbery", 0)
        );

        statsPanel.getChildren().addAll(statsTitle, totalCasesLabel, inProgressLabel,
                solvedLabel, new Separator(), breakdownTitle, typeBreakdown);
        return statsPanel;
    }

    private Label createStatCard(String title, String value, String color) {
        Label card = new Label(title + "\n" + value);
        card.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 15; -fx-background-radius: 5; " +
                "-fx-font-size: 14px; -fx-alignment: center;", color));
        card.setPrefWidth(250);
        return card;
    }

    private Label createTypeStatLabel(String type, int count) {
        Label label = new Label(String.format("• %s: %d", type, count));
        label.setStyle("-fx-font-size: 12px;");
        return label;
    }

    private void loadApprovedCases() {
        caseList.clear();

        String[] tables = {"fraud_reports", "money_laundering_reports", "kidnapping_reports",
                "drug_offense_reports", "extortion_reports", "robbery_reports"};
        String[] types = {"Fraud", "Money Laundering", "Kidnapping", "Drug Offense", "Extortion", "Robbery"};

        for (int i = 0; i < tables.length; i++) {
            try {
                ResultSet rs = DatabaseHelper.getApprovedCasesByTable(tables[i]);
                while (rs != null && rs.next()) {
                    Case case_ = new Case();
                    case_.setCaseId(rs.getInt("id"));
                    case_.setCaseType(types[i]);
                    case_.setComplainantName(rs.getString("complainant_name"));
                    case_.setComplainantPhone(rs.getString("complainant_phone"));
                    case_.setLocation(rs.getString("location"));
                    case_.setIncidentDate(rs.getString("incident_date"));
                    case_.setIncidentTime(rs.getString("incident_time"));
                    case_.setStatus(rs.getString("status"));
                    case_.setReportDate(rs.getString("report_date"));
                    case_.setDescription(rs.getString("description"));

                    // Set additional fields based on case type
                    setCaseSpecificFields(case_, rs, types[i]);

                    // Determine priority
                    case_.setPriority(determinePriority(case_));

                    caseList.add(case_);
                }
                if (rs != null) rs.close();
            } catch (SQLException e) {
                showError("Error loading cases from " + tables[i] + ": " + e.getMessage());
            }
        }
    }

    private void setCaseSpecificFields(Case case_, ResultSet rs, String type) throws SQLException {
        switch (type) {
            case "Fraud":
                case_.setAccusedName(rs.getString("accused_name"));
                case_.setTransactionAmount(rs.getString("transaction_amount"));
                case_.setFraudType(rs.getString("type_of_fraud"));
                break;
            case "Money Laundering":
                case_.setTotalAmount(rs.getString("total_amount"));
                case_.setFundSources(rs.getString("fund_sources"));
                case_.setUrgencyLevel(rs.getString("urgency_level"));
                break;
            case "Kidnapping":
                case_.setVictimAge(rs.getInt("victim_age"));
                case_.setVictimGender(rs.getString("victim_gender"));
                case_.setLastLocation(rs.getString("last_location"));
                case_.setUrgencyLevel(rs.getString("urgency_level"));
                break;
            case "Drug Offense":
                case_.setDrugType(rs.getString("drug_type"));
                case_.setQuantity(rs.getString("quantity"));
                case_.setLocationType(rs.getString("location_type"));
                break;
            case "Extortion":
                case_.setExtortionType(rs.getString("extortion_type"));
                case_.setThreatTypes(rs.getString("threat_types"));
                case_.setMoneyAmount(rs.getString("money_amount"));
                break;
            case "Robbery":
                case_.setArmedRobbery(rs.getString("armed_robbery"));
                case_.setItemsStolen(rs.getString("items_stolen"));
                case_.setNumberOfRobbers(rs.getString("number_of_robbers"));
                break;
        }
    }

    private String determinePriority(Case case_) {
        if ("Kidnapping".equals(case_.getCaseType()) ||
                (case_.getUrgencyLevel() != null && case_.getUrgencyLevel().contains("Critical"))) {
            return "HIGH";
        } else if ("Drug Offense".equals(case_.getCaseType()) ||
                "Extortion".equals(case_.getCaseType())) {
            return "MEDIUM";
        }
        return "NORMAL";
    }

    private void filterCases() {
        ObservableList<Case> filteredList = FXCollections.observableArrayList();

        for (Case case_ : caseList) {
            boolean matchesType = caseTypeFilter.getValue().equals("All Cases") ||
                    case_.getCaseType().equals(caseTypeFilter.getValue());

            boolean matchesSearch = searchField.getText().isEmpty() ||
                    case_.getComplainantName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                    case_.getLocation().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                    String.valueOf(case_.getCaseId()).contains(searchField.getText());

            if (matchesType && matchesSearch) {
                filteredList.add(case_);
            }
        }

        caseTable.setItems(filteredList);
    }

    private void updateStatistics() {
        int total = caseList.size();
        int inProgress = (int) caseList.stream().filter(c -> "in_progress".equals(c.getStatus())).count();
        int solved = (int) caseList.stream().filter(c -> "solved".equals(c.getStatus())).count();

        totalCasesLabel.setText("Total Cases\n" + total);
        inProgressLabel.setText("In Progress\n" + inProgress);
        solvedLabel.setText("Solved\n" + solved);
    }

    private void showCaseDetails(Case selectedCase) {
        Stage detailStage = new Stage();
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.setTitle("Case Details - ID: " + selectedCase.getCaseId());

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Case header
        Label headerLabel = new Label("📋 Case Details");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Basic information
        VBox basicInfo = createInfoSection("Basic Information",
                "Case ID: " + selectedCase.getCaseId(),
                "Case Type: " + selectedCase.getCaseType(),
                "Status: " + selectedCase.getStatus(),
                "Priority: " + selectedCase.getPriority(),
                "Report Date: " + selectedCase.getReportDate()
        );

        // Complainant information
        VBox complainantInfo = createInfoSection("Complainant Information",
                "Name: " + selectedCase.getComplainantName(),
                "Phone: " + selectedCase.getComplainantPhone(),
                "Location: " + selectedCase.getLocation()
        );

        // Incident details
        VBox incidentInfo = createInfoSection("Incident Details",
                "Date: " + selectedCase.getIncidentDate(),
                "Time: " + selectedCase.getIncidentTime(),
                "Description: " + (selectedCase.getDescription() != null ? selectedCase.getDescription() : "Not provided")
        );

        // Case-specific information
        VBox specificInfo = createCaseSpecificInfo(selectedCase);

        // Action buttons
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);

        Button updateStatusBtn = new Button("Update Status");
        updateStatusBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        updateStatusBtn.setOnAction(e -> {
            updateCaseStatus(selectedCase);
            detailStage.close();
        });

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        closeBtn.setOnAction(e -> detailStage.close());

        actionButtons.getChildren().addAll(updateStatusBtn, closeBtn);

        content.getChildren().addAll(headerLabel, basicInfo, complainantInfo, incidentInfo, specificInfo, actionButtons);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 600, 700);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private VBox createInfoSection(String title, String... info) {
        VBox section = new VBox(5);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox infoBox = new VBox(3);
        infoBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        for (String infoItem : info) {
            Label infoLabel = new Label(infoItem);
            infoLabel.setWrapText(true);
            infoBox.getChildren().add(infoLabel);
        }

        section.getChildren().addAll(titleLabel, infoBox);
        return section;
    }

    private VBox createCaseSpecificInfo(Case selectedCase) {
        VBox specificInfo = new VBox(5);

        switch (selectedCase.getCaseType()) {
            case "Fraud":
                return createInfoSection("Fraud Specific Information",
                        "Accused: " + (selectedCase.getAccusedName() != null ? selectedCase.getAccusedName() : "Not specified"),
                        "Transaction Amount: " + (selectedCase.getTransactionAmount() != null ? selectedCase.getTransactionAmount() : "Not specified"),
                        "Fraud Type: " + (selectedCase.getFraudType() != null ? selectedCase.getFraudType() : "Not specified")
                );
            case "Money Laundering":
                return createInfoSection("Money Laundering Information",
                        "Total Amount: " + (selectedCase.getTotalAmount() != null ? selectedCase.getTotalAmount() : "Not specified"),
                        "Fund Sources: " + (selectedCase.getFundSources() != null ? selectedCase.getFundSources() : "Not specified"),
                        "Urgency Level: " + (selectedCase.getUrgencyLevel() != null ? selectedCase.getUrgencyLevel() : "Not specified")
                );
            case "Kidnapping":
                return createInfoSection("Kidnapping Information",
                        "Victim Age: " + selectedCase.getVictimAge(),
                        "Victim Gender: " + (selectedCase.getVictimGender() != null ? selectedCase.getVictimGender() : "Not specified"),
                        "Last Known Location: " + (selectedCase.getLastLocation() != null ? selectedCase.getLastLocation() : "Not specified"),
                        "Urgency Level: " + (selectedCase.getUrgencyLevel() != null ? selectedCase.getUrgencyLevel() : "Not specified")
                );
            case "Drug Offense":
                return createInfoSection("Drug Offense Information",
                        "Drug Type: " + (selectedCase.getDrugType() != null ? selectedCase.getDrugType() : "Not specified"),
                        "Quantity: " + (selectedCase.getQuantity() != null ? selectedCase.getQuantity() : "Not specified"),
                        "Location Type: " + (selectedCase.getLocationType() != null ? selectedCase.getLocationType() : "Not specified")
                );
            case "Extortion":
                return createInfoSection("Extortion Information",
                        "Extortion Type: " + (selectedCase.getExtortionType() != null ? selectedCase.getExtortionType() : "Not specified"),
                        "Threat Types: " + (selectedCase.getThreatTypes() != null ? selectedCase.getThreatTypes() : "Not specified"),
                        "Money Amount: " + (selectedCase.getMoneyAmount() != null ? selectedCase.getMoneyAmount() : "Not specified")
                );
            case "Robbery":
                return createInfoSection("Robbery Information",
                        "Armed/Unarmed: " + (selectedCase.getArmedRobbery() != null ? selectedCase.getArmedRobbery() : "Not specified"),
                        "Items Stolen: " + (selectedCase.getItemsStolen() != null ? selectedCase.getItemsStolen() : "Not specified"),
                        "Number of Robbers: " + (selectedCase.getNumberOfRobbers() != null ? selectedCase.getNumberOfRobbers() : "Not specified")
                );
            default:
                return new VBox();
        }
    }

    private void showInvestigationNotes(Case selectedCase) {
        Stage notesStage = new Stage();
        notesStage.initModality(Modality.APPLICATION_MODAL);
        notesStage.setTitle("Investigation Notes - Case ID: " + selectedCase.getCaseId());

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label headerLabel = new Label("📝 Investigation Notes");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Notes display area
        TextArea notesDisplay = new TextArea();
        notesDisplay.setEditable(false);
        notesDisplay.setPrefRowCount(10);

        // Load existing notes
        loadInvestigationNotes(selectedCase, notesDisplay);

        // New note input
        Label newNoteLabel = new Label("Add New Note:");
        newNoteLabel.setStyle("-fx-font-weight: bold;");

        TextField investigatorField = new TextField();
        investigatorField.setPromptText("Investigator Name");

        TextArea newNoteArea = new TextArea();
        newNoteArea.setPromptText("Enter investigation note...");
        newNoteArea.setPrefRowCount(4);

        Button saveNoteBtn = new Button("💾 Save Note");
        saveNoteBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        saveNoteBtn.setOnAction(e -> {
            if (!investigatorField.getText().trim().isEmpty() && !newNoteArea.getText().trim().isEmpty()) {
                boolean saved = DatabaseHelper.saveInvestigationNotes(
                        selectedCase.getCaseId(),
                        selectedCase.getCaseType().toLowerCase().replace(" ", "_") + "_reports",
                        investigatorField.getText().trim(),
                        newNoteArea.getText().trim()
                );

                if (saved) {
                    newNoteArea.clear();
                    investigatorField.clear();
                    loadInvestigationNotes(selectedCase, notesDisplay);
                    showInfo("Note saved successfully!");
                } else {
                    showError("Failed to save note!");
                }
            } else {
                showError("Please fill in both investigator name and note!");
            }
        });

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        closeBtn.setOnAction(e -> notesStage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveNoteBtn, closeBtn);

        content.getChildren().addAll(headerLabel, new Label("Existing Notes:"), notesDisplay,
                new Separator(), newNoteLabel, investigatorField, newNoteArea, buttonBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 600, 600);
        notesStage.setScene(scene);
        notesStage.show();
    }

    private void loadInvestigationNotes(Case selectedCase, TextArea notesDisplay) {
        try {
            ResultSet rs = DatabaseHelper.getInvestigationNotes(
                    selectedCase.getCaseId(),
                    selectedCase.getCaseType().toLowerCase().replace(" ", "_") + "_reports"
            );

            StringBuilder notes = new StringBuilder();
            while (rs != null && rs.next()) {
                notes.append("=== ").append(rs.getString("investigator_name")).append(" ===\n");
                notes.append("Date: ").append(rs.getString("created_date")).append("\n");
                notes.append(rs.getString("notes")).append("\n\n");
            }

            if (notes.length() == 0) {
                notes.append("No investigation notes found for this case.");
            }

            notesDisplay.setText(notes.toString());

            if (rs != null) rs.close();
        } catch (SQLException e) {
            showError("Error loading investigation notes: " + e.getMessage());
        }
    }

    private void showEvidenceManagement(Case selectedCase) {
        Stage evidenceStage = new Stage();
        evidenceStage.initModality(Modality.APPLICATION_MODAL);
        evidenceStage.setTitle("Evidence Management - Case ID: " + selectedCase.getCaseId());

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label headerLabel = new Label("🔍 Evidence Management");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Evidence list
        ListView<String> evidenceList = new ListView<>();
        evidenceList.setPrefHeight(200);
        loadCaseEvidence(selectedCase, evidenceList);

        // Add evidence form
        Label addEvidenceLabel = new Label("Add New Evidence:");
        addEvidenceLabel.setStyle("-fx-font-weight: bold;");

        GridPane evidenceForm = new GridPane();
        evidenceForm.setHgap(10);
        evidenceForm.setVgap(10);

        TextField evidenceTypeField = new TextField();
        evidenceTypeField.setPromptText("Evidence Type (e.g., Document, Video, Photo)");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Evidence Description");
        descriptionArea.setPrefRowCount(3);

        TextField filePathField = new TextField();
        filePathField.setPromptText("File Path (optional)");

        TextField collectedByField = new TextField();
        collectedByField.setPromptText("Collected By");

        TextArea custodyArea = new TextArea();
        custodyArea.setPromptText("Chain of Custody Details");
        custodyArea.setPrefRowCount(2);

        evidenceForm.add(new Label("Evidence Type:"), 0, 0);
        evidenceForm.add(evidenceTypeField, 1, 0);
        evidenceForm.add(new Label("Description:"), 0, 1);
        evidenceForm.add(descriptionArea, 1, 1);
        evidenceForm.add(new Label("File Path:"), 0, 2);
        evidenceForm.add(filePathField, 1, 2);
        evidenceForm.add(new Label("Collected By:"), 0, 3);
        evidenceForm.add(collectedByField, 1, 3);
        evidenceForm.add(new Label("Chain of Custody:"), 0, 4);
        evidenceForm.add(custodyArea, 1, 4);

        Button addEvidenceBtn = new Button("📎 Add Evidence");
        addEvidenceBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addEvidenceBtn.setOnAction(e -> {
            if (!evidenceTypeField.getText().trim().isEmpty() &&
                    !descriptionArea.getText().trim().isEmpty() &&
                    !collectedByField.getText().trim().isEmpty()) {

                boolean added = DatabaseHelper.addEvidence(
                        selectedCase.getCaseId(),
                        selectedCase.getCaseType().toLowerCase().replace(" ", "_") + "_reports",
                        evidenceTypeField.getText().trim(),
                        descriptionArea.getText().trim(),
                        filePathField.getText().trim(),
                        collectedByField.getText().trim(),
                        custodyArea.getText().trim()
                );

                if (added) {
                    evidenceTypeField.clear();
                    descriptionArea.clear();
                    filePathField.clear();
                    collectedByField.clear();
                    custodyArea.clear();
                    loadCaseEvidence(selectedCase, evidenceList);
                    showInfo("Evidence added successfully!");
                } else {
                    showError("Failed to add evidence!");
                }
            } else {
                showError("Please fill in all required fields!");
            }
        });

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        closeBtn.setOnAction(e -> evidenceStage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addEvidenceBtn, closeBtn);

        content.getChildren().addAll(headerLabel, new Label("Current Evidence:"), evidenceList,
                new Separator(), addEvidenceLabel, evidenceForm, buttonBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 700, 700);
        evidenceStage.setScene(scene);
        evidenceStage.show();
    }

    private void loadCaseEvidence(Case selectedCase, ListView<String> evidenceList) {
        ObservableList<String> evidence = FXCollections.observableArrayList();

        try {
            ResultSet rs = DatabaseHelper.getCaseEvidence(
                    selectedCase.getCaseId(),
                    selectedCase.getCaseType().toLowerCase().replace(" ", "_") + "_reports"
            );

            while (rs != null && rs.next()) {
                String evidenceItem = String.format("[%s] %s - %s\nCollected by: %s on %s",
                        rs.getString("evidence_type"),
                        rs.getString("evidence_description"),
                        rs.getString("file_path") != null ? rs.getString("file_path") : "No file",
                        rs.getString("collected_by"),
                        rs.getString("collected_date")
                );
                evidence.add(evidenceItem);
            }

            if (evidence.isEmpty()) {
                evidence.add("No evidence recorded for this case yet.");
            }

            evidenceList.setItems(evidence);

            if (rs != null) rs.close();
        } catch (SQLException e) {
            showError("Error loading case evidence: " + e.getMessage());
        }
    }

    private void updateCaseStatus(Case selectedCase) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Case Status");
        alert.setHeaderText("Update status for Case ID: " + selectedCase.getCaseId());

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("approved", "in_progress", "solved", "closed");
        statusCombo.setValue(selectedCase.getStatus());

        alert.getDialogPane().setContent(new VBox(10, new Label("Select new status:"), statusCombo));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newStatus = statusCombo.getValue();
            boolean updated = false;

            // Update status based on case type
            switch (selectedCase.getCaseType()) {
                case "Fraud":
                    updated = DatabaseHelper.updateFraudReportStatus(selectedCase.getCaseId(), newStatus);
                    break;
                case "Money Laundering":
                    updated = DatabaseHelper.updateMoneyLaunderingReportStatus(selectedCase.getCaseId(), newStatus);
                    break;
                case "Kidnapping":
                    updated = DatabaseHelper.updateKidnappingReportStatus(selectedCase.getCaseId(), newStatus);
                    break;
                case "Drug Offense":
                    updated = DatabaseHelper.updateDrugOffenseReportStatus(selectedCase.getCaseId(), newStatus);
                    break;
                case "Extortion":
                    updated = DatabaseHelper.updateExtortionReportStatus(selectedCase.getCaseId(), newStatus);
                    break;
                case "Robbery":
                    updated = DatabaseHelper.updateRobberyReportStatus(selectedCase.getCaseId(), newStatus);
                    break;
            }

            if (updated) {
                selectedCase.setStatus(newStatus);
                caseTable.refresh();
                updateStatistics();
                showInfo("Case status updated successfully!");
            } else {
                showError("Failed to update case status!");
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Case model class
    public static class Case {
        private int caseId;
        private String caseType;
        private String complainantName;
        private String complainantPhone;
        private String location;
        private String incidentDate;
        private String incidentTime;
        private String status;
        private String reportDate;
        private String description;
        private String priority;

        // Fraud specific fields
        private String accusedName;
        private String transactionAmount;
        private String fraudType;

        // Money Laundering specific fields
        private String totalAmount;
        private String fundSources;
        private String urgencyLevel;

        // Kidnapping specific fields
        private int victimAge;
        private String victimGender;
        private String lastLocation;

        // Drug Offense specific fields
        private String drugType;
        private String quantity;
        private String locationType;

        // Extortion specific fields
        private String extortionType;
        private String threatTypes;
        private String moneyAmount;

        // Robbery specific fields
        private String armedRobbery;
        private String itemsStolen;
        private String numberOfRobbers;

        // Constructors
        public Case() {}

        // Getters and Setters
        public int getCaseId() { return caseId; }
        public void setCaseId(int caseId) { this.caseId = caseId; }

        public String getCaseType() { return caseType; }
        public void setCaseType(String caseType) { this.caseType = caseType; }

        public String getComplainantName() { return complainantName; }
        public void setComplainantName(String complainantName) { this.complainantName = complainantName; }

        public String getComplainantPhone() { return complainantPhone; }
        public void setComplainantPhone(String complainantPhone) { this.complainantPhone = complainantPhone; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getIncidentDate() { return incidentDate; }
        public void setIncidentDate(String incidentDate) { this.incidentDate = incidentDate; }

        public String getIncidentTime() { return incidentTime; }
        public void setIncidentTime(String incidentTime) { this.incidentTime = incidentTime; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getReportDate() { return reportDate; }
        public void setReportDate(String reportDate) { this.reportDate = reportDate; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }

        // Fraud specific getters/setters
        public String getAccusedName() { return accusedName; }
        public void setAccusedName(String accusedName) { this.accusedName = accusedName; }

        public String getTransactionAmount() { return transactionAmount; }
        public void setTransactionAmount(String transactionAmount) { this.transactionAmount = transactionAmount; }

        public String getFraudType() { return fraudType; }
        public void setFraudType(String fraudType) { this.fraudType = fraudType; }

        // Money Laundering specific getters/setters
        public String getTotalAmount() { return totalAmount; }
        public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }

        public String getFundSources() { return fundSources; }
        public void setFundSources(String fundSources) { this.fundSources = fundSources; }

        public String getUrgencyLevel() { return urgencyLevel; }
        public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

        // Kidnapping specific getters/setters
        public int getVictimAge() { return victimAge; }
        public void setVictimAge(int victimAge) { this.victimAge = victimAge; }

        public String getVictimGender() { return victimGender; }
        public void setVictimGender(String victimGender) { this.victimGender = victimGender; }

        public String getLastLocation() { return lastLocation; }
        public void setLastLocation(String lastLocation) { this.lastLocation = lastLocation; }

        // Drug Offense specific getters/setters
        public String getDrugType() { return drugType; }
        public void setDrugType(String drugType) { this.drugType = drugType; }

        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }

        public String getLocationType() { return locationType; }
        public void setLocationType(String locationType) { this.locationType = locationType; }

        // Extortion specific getters/setters
        public String getExtortionType() { return extortionType; }
        public void setExtortionType(String extortionType) { this.extortionType = extortionType; }

        public String getThreatTypes() { return threatTypes; }
        public void setThreatTypes(String threatTypes) { this.threatTypes = threatTypes; }

        public String getMoneyAmount() { return moneyAmount; }
        public void setMoneyAmount(String moneyAmount) { this.moneyAmount = moneyAmount; }

        // Robbery specific getters/setters
        public String getArmedRobbery() { return armedRobbery; }
        public void setArmedRobbery(String armedRobbery) { this.armedRobbery = armedRobbery; }

        public String getItemsStolen() { return itemsStolen; }
        public void setItemsStolen(String itemsStolen) { this.itemsStolen = itemsStolen; }

        public String getNumberOfRobbers() { return numberOfRobbers; }
        public void setNumberOfRobbers(String numberOfRobbers) { this.numberOfRobbers = numberOfRobbers; }
    }


// Design-pattern integration: Strategy allows assignment policy to change at runtime.
    private final InvestigatorAssignmentService designPatternAssignmentService =
            new InvestigatorAssignmentService(new SpecializationBasedAssignment());

    public void useLocationAssignmentStrategy() {
        designPatternAssignmentService.setStrategy(new LocationBasedAssignment());
    }

    public void useSpecializationAssignmentStrategy() {
        designPatternAssignmentService.setStrategy(new SpecializationBasedAssignment());
    }

    public Investigator chooseInvestigator(CrimeReport report,
                                            java.util.List<Investigator> investigators) {
        return designPatternAssignmentService.assign(report, investigators);
    }

    public CaseContext createInvestigationCaseState(int caseId) {
        return new CaseContext(caseId);
    }
}
