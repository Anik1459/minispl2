package org.example.java;

import com.example.oopproject.factory.CrimeFactory;
import com.example.oopproject.factory.Crime;
import com.example.oopproject.model.CrimeReport;
import com.example.oopproject.service.CrimeReportService;
import com.example.oopproject.observer.CaseObserver;
import com.example.oopproject.observer.CaseSubject;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

// Add this field to store current user's phone number

public class UserDashboard extends Application {
    Stage primaryStage;
    private String currentUserPhone = "";
    private ObservableList<CaseRecord> caseHistory = FXCollections.observableArrayList();
    private ListView<CaseRecord> historyListView;

    // Crime types (20 most common crimes)
    private final String[] crimes = {
            "Theft/Burglary", "Assault", "Fraud", "Vandalism", "Drug Offense",
            "Domestic Violence", "Robbery", "Cybercrime", "Traffic Violation", "Harassment",
            "Identity Theft", "Shoplifting", "Embezzlement", "Stalking", "Arson",
            "Kidnapping", "Sexual Assault", "Money Laundering", "Extortion", "Public Disorder"
    };

    // Emergency numbers
    private final String[][] emergencyNumbers = {
            {"Police", "999"},
            {"Fire Service", "998"},
            {"Ambulance", "997"},
            {"Women & Child Helpline", "109"},
            {"Anti-Terrorism", "322"}
    };

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Crime Reporting Dashboard");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Header with toggle and back button
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db); -fx-padding: 10;");

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
                roleSelection.start(this.primaryStage);
            } catch (Exception ex) {
                System.err.println("Error navigating to RoleSelection: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        HBox.setMargin(backButton, new Insets(0, 10, 0, 0));

        ToggleButton toggleButton = new ToggleButton("☰");
        toggleButton.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: transparent;");
        labelStyle(toggleButton);

        Label title = new Label("Crime Reporting Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        HBox.setMargin(title, new Insets(0, 0, 0, 10));

        header.getChildren().addAll(backButton, toggleButton, title);
        mainLayout.setTop(header);

        // Sidebar buttons
        Button reportBtn = createSidebarButton("Report Crime");
        Button historyBtn = createSidebarButton("Case History");
        Button emergencyBtn = createSidebarButton("Emergency");
        Button helpBtn = createSidebarButton("Help & FAQ");

        VBox sidebar = new VBox(10, reportBtn, historyBtn, emergencyBtn, helpBtn);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #34495e;");
        sidebar.setPrefWidth(200);

        // Main content tabs with references
        TabPane contentPane = new TabPane();
        contentPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab reportTab = new Tab("Report Crime", createReportCrimePane());
        Tab historyTab = new Tab("Case History", createCaseHistoryPane());
        Tab emergencyTab = new Tab("Emergency", createEmergencyPane());
        Tab helpTab = new Tab("Help & FAQ", createHelpPane());

        contentPane.getTabs().addAll(reportTab, historyTab, emergencyTab, helpTab);
        mainLayout.setCenter(contentPane);

        // Link sidebar buttons to tabs
        reportBtn.setOnAction(e -> contentPane.getSelectionModel().select(reportTab));
        historyBtn.setOnAction(e -> contentPane.getSelectionModel().select(historyTab));
        emergencyBtn.setOnAction(e -> contentPane.getSelectionModel().select(emergencyTab));
        helpBtn.setOnAction(e -> contentPane.getSelectionModel().select(helpTab));

        // Toggle sidebar
        toggleButton.setOnAction(e -> mainLayout.setLeft(toggleButton.isSelected() ? sidebar : null));

        // Create scene
        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add sample data
        addSampleData();
    }




    private ScrollPane createCaseHistoryPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        // Title
        Label title = new Label("Case History");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.DARKBLUE);

        // Filter section
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10));
        filterBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");

        // Crime type filter
        ComboBox<String> crimeTypeFilter = new ComboBox<>();
        crimeTypeFilter.getItems().addAll("All Cases", "Fraud", "Money Laundering", "Kidnapping",
                "Drug Offense", "Extortion", "Robbery");
        crimeTypeFilter.setValue("All Cases");
        crimeTypeFilter.setPrefWidth(150);

        // Status filter
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "pending", "approved", "in_progress", "solved");
        statusFilter.setValue("All Status");
        statusFilter.setPrefWidth(150);

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by case ID or details...");
        searchField.setPrefWidth(200);

        // Refresh button
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");

        // Search button
        Button searchBtn = new Button("Filter");
        searchBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");

        filterBox.getChildren().addAll(
                new Label("Crime Type:"), crimeTypeFilter,
                new Label("Status:"), statusFilter,
                new Label("Search:"), searchField,
                searchBtn, refreshBtn
        );

        // Case list
        historyListView = new ListView<>();
        historyListView.setPrefHeight(450);
        historyListView.setCellFactory(listView -> new EnhancedCaseListCell());

        // Statistics section
        HBox statsBox = createStatsBox();

        content.getChildren().addAll(title, statsBox, filterBox, historyListView);

        // Set up event handlers
        refreshBtn.setOnAction(e -> loadAllCaseHistory());
        searchBtn.setOnAction(e -> filterCaseHistory(
                crimeTypeFilter.getValue(),
                statusFilter.getValue(),
                searchField.getText()
        ));

        // Load data initially
        loadAllCaseHistory();

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        return scrollPane;
    }

    // Add these methods to your UserDashboard class

    private ScrollPane createReportCrimePane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        // Title
        Label title = new Label("Report a Crime");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.DARKBLUE);

        // Instruction label
        Label instruction = new Label("Select the type of crime you want to report:");
        instruction.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        instruction.setTextFill(Color.DARKGRAY);

        // Crime types grid
        GridPane crimeGrid = new GridPane();
        crimeGrid.setHgap(15);
        crimeGrid.setVgap(15);
        crimeGrid.setPadding(new Insets(20));
        crimeGrid.setAlignment(Pos.CENTER);

        // Create buttons for each crime type
        int row = 0, col = 0;
        for (String crime : crimes) {
            Button crimeButton = createCrimeButton(crime);
            crimeGrid.add(crimeButton, col, row);

            col++;
            if (col >= 4) { // 4 buttons per row
                col = 0;
                row++;
            }
        }

        // Quick report section
        VBox quickReportSection = new VBox(15);
        quickReportSection.setStyle("-fx-background-color: #e8f5e8; -fx-padding: 20; -fx-background-radius: 10;");

        Label quickReportTitle = new Label("Quick Report");
        quickReportTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        quickReportTitle.setTextFill(Color.DARKGREEN);

        Label quickReportText = new Label("For urgent matters, you can also:");

        Button emergencyButton = new Button("Call Emergency Services");
        emergencyButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        emergencyButton.setOnAction(e -> {
            // Switch to emergency tab or show emergency dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Emergency Services");
            alert.setHeaderText("Emergency Contact Numbers");
            alert.setContentText("Police: 999\nFire Service: 998\nAmbulance: 997\nWomen & Child Helpline: 109");
            alert.showAndWait();
        });

        quickReportSection.getChildren().addAll(quickReportTitle, quickReportText, emergencyButton);

        content.getChildren().addAll(title, instruction, crimeGrid, quickReportSection);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        return scrollPane;
    }

    private Button createCrimeButton(String crimeType) {
        Button button = new Button(crimeType);
        button.setPrefSize(200, 80);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        button.setWrapText(true);

        // Style based on crime type
        String baseStyle = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2;";

        if (crimeType.contains("Assault") || crimeType.contains("Violence") || crimeType.contains("Sexual")) {
            button.setStyle(baseStyle + "-fx-background-color: #ffcdd2; -fx-border-color: #f44336; -fx-text-fill: #c62828;");
        } else if (crimeType.contains("Fraud") || crimeType.contains("Theft") || crimeType.contains("Embezzlement")) {
            button.setStyle(baseStyle + "-fx-background-color: #fff3e0; -fx-border-color: #ff9800; -fx-text-fill: #e65100;");
        } else if (crimeType.contains("Drug") || crimeType.contains("Trafficking")) {
            button.setStyle(baseStyle + "-fx-background-color: #f3e5f5; -fx-border-color: #9c27b0; -fx-text-fill: #6a1b9a;");
        } else if (crimeType.contains("Cyber") || crimeType.contains("Identity")) {
            button.setStyle(baseStyle + "-fx-background-color: #e1f5fe; -fx-border-color: #03a9f4; -fx-text-fill: #0277bd;");
        } else {
            button.setStyle(baseStyle + "-fx-background-color: #e8f5e8; -fx-border-color: #4caf50; -fx-text-fill: #2e7d32;");
        }

        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);");
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });

        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);", ""));
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        // Action handler
        button.setOnAction(e -> {
            System.out.println("Crime button clicked: " + crimeType); // Debug
            reportCrime(crimeType);
        });

        return button;
    }

    private void filterCaseHistory(String crimeTypeFilter, String statusFilter, String searchText) {
        System.out.println("Filtering cases - Crime: " + crimeTypeFilter + ", Status: " + statusFilter + ", Search: " + searchText);

        // Create a filtered list
        ObservableList<CaseRecord> filteredCases = FXCollections.observableArrayList();

        for (CaseRecord caseRecord : caseHistory) {
            boolean matches = true;

            // Filter by crime type
            if (!crimeTypeFilter.equals("All Cases")) {
                if (!caseRecord.getCrimeType().equalsIgnoreCase(crimeTypeFilter)) {
                    matches = false;
                }
            }

            // Filter by status
            if (!statusFilter.equals("All Status")) {
                if (!caseRecord.getStatus().equalsIgnoreCase(statusFilter)) {
                    matches = false;
                }
            }

            // Filter by search text
            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchLower = searchText.toLowerCase().trim();
                boolean textMatches = false;

                // Check if search text matches case ID, crime type, location, or complainant name
                if (caseRecord.getCaseId().toLowerCase().contains(searchLower) ||
                        caseRecord.getCrimeType().toLowerCase().contains(searchLower) ||
                        caseRecord.getLocation().toLowerCase().contains(searchLower)) {
                    textMatches = true;
                }

                // Also check complainant name if it's an enhanced case record
                if (caseRecord instanceof EnhancedCaseRecord) {
                    EnhancedCaseRecord enhanced = (EnhancedCaseRecord) caseRecord;
                    if (enhanced.getComplainantName().toLowerCase().contains(searchLower)) {
                        textMatches = true;
                    }
                }

                if (!textMatches) {
                    matches = false;
                }
            }

            if (matches) {
                filteredCases.add(caseRecord);
            }
        }

        // Update the ListView with filtered results
        historyListView.setItems(filteredCases);

        // Show message if no results found
        if (filteredCases.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Filter Results");
            alert.setHeaderText("No cases found");
            alert.setContentText("No cases match your filter criteria. Try adjusting your filters.");
            alert.showAndWait();
        }

        System.out.println("Filter applied. Found " + filteredCases.size() + " matching cases.");
    }
    private HBox createStatsBox() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(15));
        statsBox.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-background-radius: 10;");

        // Get statistics from database
        int[] stats = getCaseStatistics();

        VBox totalBox = createStatBox("Total Cases", String.valueOf(stats[0]), "#ffffff");
        VBox pendingBox = createStatBox("Pending", String.valueOf(stats[1]), "#ffeb3b");
        VBox approvedBox = createStatBox("Approved", String.valueOf(stats[2]), "#4caf50");
        VBox progressBox = createStatBox("In Progress", String.valueOf(stats[3]), "#ff9800");
        VBox solvedBox = createStatBox("Solved", String.valueOf(stats[4]), "#2196f3");

        statsBox.getChildren().addAll(totalBox, pendingBox, approvedBox, progressBox, solvedBox);
        return statsBox;
    }

    // Helper method to create individual stat boxes
    private VBox createStatBox(String label, String count, String color) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);

        Label countLabel = new Label(count);
        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        countLabel.setTextFill(Color.web(color));

        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        titleLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(countLabel, titleLabel);
        return box;
    }

    private void loadAllCaseHistory() {
        caseHistory.clear();

        try {
            // Load from each crime table
            loadCasesFromTable("fraud_reports", "Fraud");
            loadCasesFromTable("money_laundering_reports", "Money Laundering");
            loadCasesFromTable("kidnapping_reports", "Kidnapping");
            loadCasesFromTable("drug_offense_reports", "Drug Offense");
            loadCasesFromTable("extortion_reports", "Extortion");
            loadCasesFromTable("robbery_reports", "Robbery");

            // Sort by report date (newest first)
            caseHistory.sort((a, b) -> b.getReportDate().compareTo(a.getReportDate()));

        } catch (Exception e) {
            showError("Error loading case history", e.getMessage());
        }
    }

    private void loadCasesFromTable(String tableName, String crimeType) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE complainant_phone = ? ORDER BY report_date DESC";

            // For now, we'll load all cases if currentUserPhone is empty
            // You should set currentUserPhone when user logs in
            ResultSet rs;
            if (currentUserPhone.isEmpty()) {
                // Load all cases for demonstration
                rs = getAllCasesFromTable(tableName);
            } else {
                // Load user-specific cases
                rs = getUserCasesFromTable(tableName, currentUserPhone);
            }

            if (rs != null) {
                while (rs.next()) {
                    CaseRecord record = createCaseRecordFromResultSet(rs, crimeType);
                    if (record != null) {
                        caseHistory.add(record);
                    }
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error loading cases from " + tableName + ": " + e.getMessage());
        }
    }

    private ResultSet getAllCasesFromTable(String tableName) throws SQLException {
        switch (tableName) {
            case "fraud_reports":
                return DatabaseHelper.getAllFraudReports();
            case "money_laundering_reports":
                return DatabaseHelper.getAllMoneyLaunderingReports();
            case "kidnapping_reports":
                return DatabaseHelper.getAllKidnappingReports();
            case "drug_offense_reports":
                return DatabaseHelper.getAllDrugOffenseReports();
            case "extortion_reports":
                return DatabaseHelper.getAllExtortionReports();
            case "robbery_reports":
                return DatabaseHelper.getAllRobberyReports();
            default:
                return null;
        }
    }

    private ResultSet getUserCasesFromTable(String tableName, String phone) throws SQLException {
        switch (tableName) {
            case "fraud_reports":
                return DatabaseHelper.getFraudReportsByPhone(phone);
            case "money_laundering_reports":
                // You'll need to add this method to DatabaseHelper
                return null; // DatabaseHelper.getMoneyLaunderingReportsByPhone(phone);
            case "kidnapping_reports":
                return DatabaseHelper.getKidnappingReportsByPhone(phone);
            case "drug_offense_reports":
                return DatabaseHelper.getDrugOffenseReportsByPhone(phone);
            case "extortion_reports":
                return DatabaseHelper.getExtortionReportsByPhone(phone);
            case "robbery_reports":
                return DatabaseHelper.getRobberyReportsByPhone(phone);
            default:
                return null;
        }
    }
    private CaseRecord createCaseRecordFromResultSet(ResultSet rs, String crimeType) {
        try {
            int id = rs.getInt("id");
            String caseId = crimeType.substring(0, 2).toUpperCase() + String.format("%03d", id);
            String complainantName = rs.getString("complainant_name");
            String location = rs.getString("location");
            String reportDateStr = rs.getString("report_date");
            String status = rs.getString("status");
            String incidentDate = rs.getString("incident_date");

            // Parse report date
            LocalDateTime reportDate;
            try {
                reportDate = LocalDateTime.parse(reportDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                reportDate = LocalDateTime.now(); // fallback
            }

            return new EnhancedCaseRecord(caseId, crimeType, location, reportDate, status,
                    complainantName, incidentDate, id);

        } catch (SQLException e) {
            System.err.println("Error creating case record: " + e.getMessage());
            return null;
        }
    }

    private int[] getCaseStatistics() {
        int total = 0, pending = 0, approved = 0, inProgress = 0, solved = 0;

        try {
            DatabaseHelper.CaseStatistics stats = DatabaseHelper.getCaseStatistics();
            total = stats.getTotalCases();
            pending = total - stats.getTotalApproved() - stats.getTotalInProgress() - stats.getTotalSolved();
            approved = stats.getTotalApproved();
            inProgress = stats.getTotalInProgress();
            solved = stats.getTotalSolved();

        } catch (Exception e) {
            System.err.println("Error getting statistics: " + e.getMessage());
        }

        return new int[]{total, pending, approved, inProgress, solved};
    }

    // Enhanced CaseRecord class with more details
    public static class EnhancedCaseRecord extends CaseRecord {
        private String complainantName;
        private String incidentDate;
        private int databaseId;

        public EnhancedCaseRecord(String caseId, String crimeType, String location,
                                  LocalDateTime reportDate, String status,
                                  String complainantName, String incidentDate, int databaseId) {
            super(caseId, crimeType, location, reportDate, status);
            this.complainantName = complainantName;
            this.incidentDate = incidentDate;
            this.databaseId = databaseId;
        }

        public String getComplainantName() { return complainantName; }
        public String getIncidentDate() { return incidentDate; }
        public int getDatabaseId() { return databaseId; }
    }

    // Enhanced cell for case history with more details
    private class EnhancedCaseListCell extends ListCell<CaseRecord> {
        @Override
        protected void updateItem(CaseRecord item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox mainBox = new HBox(15);
                mainBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; " +
                        "-fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-border-width: 1;");

                // Left section - Main info
                VBox leftBox = new VBox(5);
                leftBox.setPrefWidth(300);

                Label caseIdLabel = new Label("Case ID: " + item.getCaseId());
                caseIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                caseIdLabel.setTextFill(Color.DARKBLUE);

                Label crimeTypeLabel = new Label("Crime: " + item.getCrimeType());
                crimeTypeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                Label locationLabel = new Label("📍 " + item.getLocation());

                if (item instanceof EnhancedCaseRecord) {
                    EnhancedCaseRecord enhanced = (EnhancedCaseRecord) item;
                    Label complainantLabel = new Label("Complainant: " + enhanced.getComplainantName());
                    leftBox.getChildren().addAll(caseIdLabel, crimeTypeLabel, complainantLabel, locationLabel);
                } else {
                    leftBox.getChildren().addAll(caseIdLabel, crimeTypeLabel, locationLabel);
                }

                // Middle section - Dates
                VBox middleBox = new VBox(5);
                middleBox.setPrefWidth(200);

                Label reportDateLabel = new Label("Reported: " +
                        item.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

                if (item instanceof EnhancedCaseRecord) {
                    EnhancedCaseRecord enhanced = (EnhancedCaseRecord) item;
                    Label incidentDateLabel = new Label("Incident: " + enhanced.getIncidentDate());
                    middleBox.getChildren().addAll(reportDateLabel, incidentDateLabel);
                } else {
                    middleBox.getChildren().add(reportDateLabel);
                }

                // Right section - Status and Actions
                VBox rightBox = new VBox(10);
                rightBox.setAlignment(Pos.CENTER_RIGHT);
                rightBox.setPrefWidth(150);

                Label statusLabel = new Label(item.getStatus().toUpperCase());
                statusLabel.setStyle("-fx-background-color: " + getStatusColor(item.getStatus()) +
                        "; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15; -fx-font-weight: bold;");

                Button viewBtn = new Button("View Details");
                viewBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 10px;");
                viewBtn.setOnAction(e -> showCaseDetails(item));

                rightBox.getChildren().addAll(statusLabel, viewBtn);

                mainBox.getChildren().addAll(leftBox, middleBox, rightBox);
                setGraphic(mainBox);
            }
        }

        private String getStatusColor(String status) {
            switch (status.toLowerCase()) {
                case "solved": return "#28a745";
                case "in_progress": return "#ffc107";
                case "approved": return "#17a2b8";
                case "pending": return "#6c757d";
                default: return "#dc3545";
            }
        }
    }

    private void showCaseDetails(CaseRecord caseRecord) {
        Alert detailDialog = new Alert(Alert.AlertType.INFORMATION);
        detailDialog.setTitle("Case Details");
        detailDialog.setHeaderText("Case ID: " + caseRecord.getCaseId());

        StringBuilder details = new StringBuilder();
        details.append("Crime Type: ").append(caseRecord.getCrimeType()).append("\n");
        details.append("Location: ").append(caseRecord.getLocation()).append("\n");
        details.append("Report Date: ").append(caseRecord.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        details.append("Status: ").append(caseRecord.getStatus()).append("\n");

        if (caseRecord instanceof EnhancedCaseRecord) {
            EnhancedCaseRecord enhanced = (EnhancedCaseRecord) caseRecord;
            details.append("Complainant: ").append(enhanced.getComplainantName()).append("\n");
            details.append("Incident Date: ").append(enhanced.getIncidentDate()).append("\n");
        }

        detailDialog.setContentText(details.toString());
        detailDialog.showAndWait();
    }

    // Method to show error messages
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to set current user phone (call this when user logs in)
    public void setCurrentUserPhone(String phone) {
        this.currentUserPhone = phone;
        loadAllCaseHistory(); // Refresh the case history
    }

    private ScrollPane createEmergencyPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        // Title
        Label title = new Label("Emergency Services");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.DARKRED);

        // Emergency warning
        Label warning = new Label("⚠️ For immediate emergencies, call directly!");
        warning.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        warning.setTextFill(Color.RED);
        warning.setStyle("-fx-background-color: #ffebee; -fx-padding: 10; -fx-background-radius: 5;");

        // Emergency numbers grid
        GridPane emergencyGrid = new GridPane();
        emergencyGrid.setHgap(20);
        emergencyGrid.setVgap(20);
        emergencyGrid.setPadding(new Insets(20));

        int row = 0;
        for (String[] emergency : emergencyNumbers) {
            VBox emergencyBox = new VBox(10);
            emergencyBox.setAlignment(Pos.CENTER);
            emergencyBox.setStyle("-fx-background-color: #ffcdd2; -fx-padding: 20; -fx-background-radius: 10;");
            emergencyBox.setPrefSize(200, 120);

            Label serviceName = new Label(emergency[0]);
            serviceName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            serviceName.setTextFill(Color.DARKRED);

            Label number = new Label(emergency[1]);
            number.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            number.setTextFill(Color.RED);

            Button callBtn = new Button("Call Now");
            callBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
            callBtn.setOnAction(e -> simulateCall(emergency[1]));

            emergencyBox.getChildren().addAll(serviceName, number, callBtn);
            emergencyGrid.add(emergencyBox, row % 3, row / 3);
            row++;
        }

        content.getChildren().addAll(title, warning, emergencyGrid);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        return scrollPane;
    }

    private ScrollPane createHelpPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        // Title
        Label title = new Label("Help & Frequently Asked Questions");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.DARKBLUE);

        // FAQ Accordion
        Accordion faqAccordion = new Accordion();

        String[][] faqs = {
                {"How do I report a crime?", "Click on the 'Report Crime' tab and select the appropriate crime type from the list. Fill in the required details and submit your report."},
                {"How can I track my case?", "Go to the 'Case History' tab to view all your reported cases and their current status."},
                {"What information do I need to provide?", "You'll need to provide details about the incident, location, time, and any evidence or witnesses."},
                {"How long does it take to process a case?", "Processing time varies depending on the type of crime. You'll receive updates on your case status."},
                {"Can I report anonymously?", "Yes, you can choose to report anonymously, but this may limit follow-up investigations."},
                {"What if it's an emergency?", "For immediate emergencies, use the Emergency tab to call the appropriate service directly."},
                {"How do I contact support?", "You can contact support through the help section or call our support hotline at 111."},
                {"Can I edit my report after submission?", "Contact support to make changes to your submitted report."}
        };

        for (String[] faq : faqs) {
            TitledPane pane = new TitledPane();
            pane.setText(faq[0]);
            pane.setStyle("-fx-font-weight: bold;");

            Label answer = new Label(faq[1]);
            answer.setWrapText(true);
            answer.setStyle("-fx-padding: 10;");

            pane.setContent(answer);
            faqAccordion.getPanes().add(pane);
        }

        // Contact section
        VBox contactSection = new VBox(10);
        contactSection.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 20; -fx-background-radius: 10;");

        Label contactTitle = new Label("Need More Help?");
        contactTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        contactTitle.setTextFill(Color.DARKBLUE);

        Label contactInfo = new Label("Support Hotline: 111\nEmail: support@crimereport.gov\nWebsite: www.crimereport.gov");
        contactInfo.setStyle("-fx-text-fill: #1976d2;");

        contactSection.getChildren().addAll(contactTitle, contactInfo);

        content.getChildren().addAll(title, faqAccordion, contactSection);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        return scrollPane;
    }

    private void reportCrime(String crimeType) {
        // Create crime reporting dialog
        System.out.println("reportCrime called with: " + crimeType); // Debug line

        if(crimeType.equals("Fraud"))
        {
            System.out.println("Opening Fraud form..."); // Debug line
            new Fraud().absMethod();
        }
        else if (crimeType.equals("Robbery")) {
            System.out.println("Opening Robbery form..."); // Debug line
            new Robbery().absMethod();
        }
        else if(crimeType.equals("Kidnapping")){
            System.out.println("Opening Kidnapping form..."); // Debug line
            try {
                new Kidnapping().absMethod();
                System.out.println("Kidnapping form opened successfully!"); // Debug line
            } catch (Exception e) {
                System.err.println("Error opening Kidnapping form: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else if(crimeType.equals("Money Laundering")){
            System.out.println("Opening Money Laundering form...");
            try {
                new MoneyLaundering().absMethod();
                System.out.println("Money Laundering form opened successfully!"); // Debug line
            } catch (Exception e) {
                System.err.println("Error opening Money Laundering form: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else if(crimeType.equals("Extortion")){
            System.out.println("Opening Extortion form...");
            try {
                new Extortion().absMethod();
                System.out.println("Extortion form opened successfully!"); // Debug line
            } catch (Exception e) {
                System.err.println("Error opening Extortion form: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else if(crimeType.equals("Drug Offense")){
            System.out.println("Opening Drug Offense form...");
            try {
                new DrugOffence().absMethod();
                System.out.println("Drug Offense form opened successfully!"); // Debug line
            } catch (Exception e) {
                System.err.println("Error opening Drug Offense form: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            System.out.println("No handler for crime type: " + crimeType); // Debug line
        }
    }

    private void simulateCall(String number) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emergency Call");
        alert.setHeaderText("Calling " + number);
        alert.setContentText("In a real application, this would initiate a call to " + number);
        alert.showAndWait();
    }

    private void addSampleData() {
        caseHistory.addAll(
                new CaseRecord("CR001", "Theft/Burglary", "123 Main St", LocalDateTime.now().minusDays(5), "Closed"),
                new CaseRecord("CR002", "Assault", "456 Oak Ave", LocalDateTime.now().minusDays(3), "Under Investigation"),
                new CaseRecord("CR003", "Fraud", "789 Pine Rd", LocalDateTime.now().minusDays(1), "Evidence Collection")
        );
    }

    // Case Record class
    public static class CaseRecord {
        private String caseId;
        private String crimeType;
        private String location;
        private LocalDateTime reportDate;
        private String status;

        public CaseRecord(String caseId, String crimeType, String location, LocalDateTime reportDate, String status) {
            this.caseId = caseId;
            this.crimeType = crimeType;
            this.location = location;
            this.reportDate = reportDate;
            this.status = status;
        }

        // Getters
        public String getCaseId() { return caseId; }
        public String getCrimeType() { return crimeType; }
        public String getLocation() { return location; }
        public LocalDateTime getReportDate() { return reportDate; }
        public String getStatus() { return status; }

        @Override
        public String toString() {
            return caseId + " - " + crimeType + " (" + status + ")";
        }
    }

    // Custom cell for case history
    private class CaseListCell extends ListCell<CaseRecord> {
        @Override
        protected void updateItem(CaseRecord item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                VBox cellBox = new VBox(5);
                cellBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-background-radius: 5;");

                Label caseIdLabel = new Label("Case ID: " + item.getCaseId());
                caseIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                Label crimeTypeLabel = new Label("Crime: " + item.getCrimeType());
                Label locationLabel = new Label("Location: " + item.getLocation());
                Label dateLabel = new Label("Date: " + item.getReportDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                Label statusLabel = new Label("Status: " + item.getStatus());
                statusLabel.setStyle("-fx-text-fill: " + getStatusColor(item.getStatus()) + "; -fx-font-weight: bold;");

                cellBox.getChildren().addAll(caseIdLabel, crimeTypeLabel, locationLabel, dateLabel, statusLabel);
                setGraphic(cellBox);
            }
        }

        private String getStatusColor(String status) {
            switch (status.toLowerCase()) {
                case "closed": return "#4caf50";
                case "under investigation": return "#ff9800";
                case "evidence collection": return "#2196f3";
                default: return "#757575";
            }
        }
    }

    // Utility to style sidebar buttons
    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(160);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        btn.setStyle("-fx-background-color: transparent;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #3d5769;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent;"));
        return btn;
    }

    private void labelStyle(Control control) {
        control.setOnMouseEntered(e -> control.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
        control.setOnMouseExited(e -> control.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
    }

    private TabPane createMainContent() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab reportTab = new Tab("Report Crime", createReportCrimePane());
        Tab historyTab = new Tab("Case History", createCaseHistoryPane());
        Tab emergencyTab = new Tab("Emergency", createEmergencyPane());
        Tab helpTab = new Tab("Help & FAQ", createHelpPane());

        tabPane.getTabs().addAll(reportTab, historyTab, emergencyTab, helpTab);
        return tabPane;
    }

    // Test method to check reportCrime functionality
    public void testReportCrime(String crimeType) {
        reportCrime(crimeType);
    }

    public static void main(String[] args) {
        launch(args);
    }


// Design-pattern integration helpers. Existing JavaFX controls and workflows remain intact.
    private final CrimeReportService designPatternCrimeReportService =
            new CrimeReportService();

    public Crime createCrimeWithFactory(String crimeType) {
        return CrimeFactory.createCrime(crimeType);
    }

    public int submitReportWithService(CrimeReport report)
            throws java.sql.SQLException {
        return designPatternCrimeReportService.submitReport(report);
    }

    public CaseSubject createObservableCase(int caseId, String initialStatus,
                                            CaseObserver observer) {
        CaseSubject subject = new CaseSubject(caseId, initialStatus);
        subject.addObserver(observer);
        return subject;
    }
}
