package org.example.java;

import com.example.oopproject.state.CaseContext;
import com.example.oopproject.observer.AdminNotification;
import com.example.oopproject.observer.CaseSubject;
import com.example.oopproject.service.InvestigationService;


import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class AdminDashboard extends Application {

    private TableView<ReportItem> reportsTable;
    private ObservableList<ReportItem> reportsList;
    private ComboBox<String> reportTypeFilter;
    private ComboBox<String> statusFilter;
    private VBox totalReportsLabel;
    private VBox pendingReportsLabel;
    private VBox approvedReportsLabel;
    private VBox rejectedReportsLabel;

    @Override
    public void start(Stage primaryStage) {
        // Initialize database
        DatabaseHelper.initializeDatabase();

        primaryStage.setTitle("Police Admin Dashboard - Case Management System");
        primaryStage.setMaximized(true);

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        VBox header = createHeader();
        mainLayout.setTop(header);

        // Center content
        VBox centerContent = createCenterContent();
        mainLayout.setCenter(centerContent);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load initial data
        loadAllReports();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");

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

        // Title and subtitle container
        VBox titleContainer = new VBox(5);
        
        Label titleLabel = new Label("Police Admin Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label subtitleLabel = new Label("Case Management & Report Review System");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #bdc3c7;");

        titleContainer.getChildren().addAll(titleLabel, subtitleLabel);
        headerRow.getChildren().addAll(backButton, titleContainer);

        // Statistics panel
        HBox statsBox = createStatsPanel();

        header.getChildren().addAll(headerRow, statsBox);
        return header;
    }

    private HBox createStatsPanel() {
        HBox statsBox = new HBox(30);
        statsBox.setPadding(new Insets(20, 0, 0, 0));
        statsBox.setAlignment(Pos.CENTER_LEFT);

        totalReportsLabel = createStatLabel("Total Reports", "0", "#3498db");
        pendingReportsLabel = createStatLabel("Pending", "0", "#f39c12");
        approvedReportsLabel = createStatLabel("Approved", "0", "#27ae60");
        rejectedReportsLabel = createStatLabel("Rejected", "0", "#e74c3c");

        statsBox.getChildren().addAll(totalReportsLabel, pendingReportsLabel,
                approvedReportsLabel, rejectedReportsLabel);
        return statsBox;
    }

    private VBox createStatLabel(String title, String count, String color) {
        VBox statBox = new VBox(5);
        statBox.setAlignment(Pos.CENTER);
        statBox.setPadding(new Insets(10));
        statBox.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8px;");

        Label countLabel = new Label(count);
        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        countLabel.setStyle("-fx-text-fill: white;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setStyle("-fx-text-fill: white;");

        statBox.getChildren().addAll(countLabel, titleLabel);
        return statBox;
    }

    private VBox createCenterContent() {
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));

        // Filters panel
        HBox filtersPanel = createFiltersPanel();

        // Reports table
        reportsTable = createReportsTable();

        // Action buttons
        HBox actionButtons = createActionButtons();

        centerContent.getChildren().addAll(filtersPanel, reportsTable, actionButtons);
        return centerContent;
    }

    private HBox createFiltersPanel() {
        HBox filtersPanel = new HBox(15);
        filtersPanel.setPadding(new Insets(10));
        filtersPanel.setAlignment(Pos.CENTER_LEFT);
        filtersPanel.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #ddd; -fx-border-radius: 8px;");

        Label filterLabel = new Label("Filters:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        reportTypeFilter = new ComboBox<>();
        reportTypeFilter.getItems().addAll(
                "All Types", "Fraud", "Money Laundering", "Kidnapping",
                "Drug Offense", "Extortion", "Robbery"
        );
        reportTypeFilter.setValue("All Types");
        reportTypeFilter.setOnAction(e -> filterReports());

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "pending", "approved", "rejected", "under_review");
        statusFilter.setValue("All Status");
        statusFilter.setOnAction(e -> filterReports());

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5px;");
        refreshButton.setOnAction(e -> loadAllReports());

        filtersPanel.getChildren().addAll(
                filterLabel,
                new Label("Report Type:"), reportTypeFilter,
                new Label("Status:"), statusFilter,
                refreshButton
        );

        return filtersPanel;
    }

    private TableView<ReportItem> createReportsTable() {
        TableView<ReportItem> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px;");

        // Create columns
        TableColumn<ReportItem, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<ReportItem, String> typeCol = new TableColumn<>("Report Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("reportType"));
        typeCol.setPrefWidth(120);

        TableColumn<ReportItem, String> complainantCol = new TableColumn<>("Complainant");
        complainantCol.setCellValueFactory(new PropertyValueFactory<>("complainantName"));
        complainantCol.setPrefWidth(150);

        TableColumn<ReportItem, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("complainantPhone"));
        phoneCol.setPrefWidth(120);

        TableColumn<ReportItem, String> nidBcCol = new TableColumn<>("NID/BC");
        nidBcCol.setCellValueFactory(new PropertyValueFactory<>("nidBc"));
        nidBcCol.setPrefWidth(120);

        TableColumn<ReportItem, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(150);

        TableColumn<ReportItem, String> dateCol = new TableColumn<>("Incident Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));
        dateCol.setPrefWidth(120);

        TableColumn<ReportItem, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        // Style status column
        statusCol.setCellFactory(column -> new TableCell<ReportItem, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.toUpperCase());
                    switch (status.toLowerCase()) {
                        case "pending":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 4px;");
                            break;
                        case "approved":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 4px;");
                            break;
                        case "rejected":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 4px;");
                            break;
                        case "under_review":
                            setStyle("-fx-background-color: #cce5ff; -fx-text-fill: #004085; -fx-background-radius: 4px;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<ReportItem, String> reportDateCol = new TableColumn<>("Report Date");
        reportDateCol.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        reportDateCol.setPrefWidth(150);

        table.getColumns().addAll(idCol, typeCol, complainantCol, phoneCol, nidBcCol,
                locationCol, dateCol, statusCol, reportDateCol);

        // Enable row selection
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        reportsList = FXCollections.observableArrayList();
        table.setItems(reportsList);

        return table;
    }

    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setPadding(new Insets(10));
        actionButtons.setAlignment(Pos.CENTER);

        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-background-radius: 5px; -fx-padding: 10px 20px;");
        viewDetailsButton.setOnAction(e -> viewReportDetails());

        Button approveButton = new Button("Approve Case");
        approveButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5px; -fx-padding: 10px 20px;");
        approveButton.setOnAction(e -> updateReportStatus("approved"));

        Button rejectButton = new Button("Reject Case");
        rejectButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5px; -fx-padding: 10px 20px;");
        rejectButton.setOnAction(e -> updateReportStatus("rejected"));

        Button underReviewButton = new Button("Under Review");
        underReviewButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5px; -fx-padding: 10px 20px;");
        underReviewButton.setOnAction(e -> updateReportStatus("under_review"));

        actionButtons.getChildren().addAll(viewDetailsButton, approveButton, rejectButton, underReviewButton);
        return actionButtons;
    }

    private void loadAllReports() {
        reportsList.clear();

        try {
            // Load fraud reports
            ResultSet fraudReports = DatabaseHelper.getAllFraudReports();
            if (fraudReports != null) {
                while (fraudReports.next()) {
                    reportsList.add(createReportItem(fraudReports, "Fraud"));
                }
                fraudReports.close();
            }

            // Load money laundering reports
            ResultSet mlReports = DatabaseHelper.getAllMoneyLaunderingReports();
            if (mlReports != null) {
                while (mlReports.next()) {
                    reportsList.add(createReportItem(mlReports, "Money Laundering"));
                }
                mlReports.close();
            }

            // Load kidnapping reports
            ResultSet kidnappingReports = DatabaseHelper.getAllKidnappingReports();
            if (kidnappingReports != null) {
                while (kidnappingReports.next()) {
                    reportsList.add(createReportItem(kidnappingReports, "Kidnapping"));
                }
                kidnappingReports.close();
            }

            // Load drug offense reports
            ResultSet drugReports = DatabaseHelper.getAllDrugOffenseReports();
            if (drugReports != null) {
                while (drugReports.next()) {
                    reportsList.add(createReportItem(drugReports, "Drug Offense"));
                }
                drugReports.close();
            }

            // Load extortion reports
            ResultSet extortionReports = DatabaseHelper.getAllExtortionReports();
            if (extortionReports != null) {
                while (extortionReports.next()) {
                    reportsList.add(createReportItem(extortionReports, "Extortion"));
                }
                extortionReports.close();
            }

            // Load robbery reports
            ResultSet robberyReports = DatabaseHelper.getAllRobberyReports();
            if (robberyReports != null) {
                while (robberyReports.next()) {
                    reportsList.add(createReportItem(robberyReports, "Robbery"));
                }
                robberyReports.close();
            }

            updateStatistics();

        } catch (SQLException e) {
            showAlert("Database Error", "Error loading reports: " + e.getMessage());
        }
    }

    private ReportItem createReportItem(ResultSet rs, String reportType) throws SQLException {
        ReportItem item = new ReportItem();
        item.setId(String.valueOf(rs.getInt("id")));
        item.setReportType(reportType);
        item.setComplainantName(rs.getString("complainant_name"));
        item.setComplainantPhone(rs.getString("complainant_phone"));

        // Handle new fields with null checks
        try {
            item.setNidBcNumber(rs.getString("nid_bc"));
        } catch (SQLException e) {
            item.setNidBcNumber("N/A");
        }

        try {
            item.setFatherName(rs.getString("father_name"));
        } catch (SQLException e) {
            item.setFatherName("N/A");
        }

        try {
            item.setMotherName(rs.getString("mother_name"));
        } catch (SQLException e) {
            item.setMotherName("N/A");
        }

        // Handle photo
        try {
            byte[] photoBytes = rs.getBytes("photo_path");
            item.setPhotoBytes(photoBytes);
        } catch (SQLException e) {
            item.setPhotoBytes(null);
        }

        item.setLocation(rs.getString("location"));

        // Handle different date column names
        try {
            item.setIncidentDate(rs.getString("incident_date"));
        } catch (SQLException e) {
            try {
                item.setIncidentDate(rs.getString("date_of_incident"));
            } catch (SQLException e2) {
                item.setIncidentDate("N/A");
            }
        }

        item.setStatus(rs.getString("status"));
        item.setReportDate(rs.getString("report_date"));
        item.setResultSet(rs); // Store for detailed view

        return item;
    }

    private void updateStatistics() {
        int total = reportsList.size();
        long pending = reportsList.stream().filter(r -> "pending".equals(r.getStatus())).count();
        long approved = reportsList.stream().filter(r -> "approved".equals(r.getStatus())).count();
        long rejected = reportsList.stream().filter(r -> "rejected".equals(r.getStatus())).count();

        // Update stat labels
        ((Label) ((VBox) totalReportsLabel).getChildren().get(0)).setText(String.valueOf(total));
        ((Label) ((VBox) pendingReportsLabel).getChildren().get(0)).setText(String.valueOf(pending));
        ((Label) ((VBox) approvedReportsLabel).getChildren().get(0)).setText(String.valueOf(approved));
        ((Label) ((VBox) rejectedReportsLabel).getChildren().get(0)).setText(String.valueOf(rejected));
    }

    private void filterReports() {
        String typeFilter = reportTypeFilter.getValue();
        String statusFilterValue = statusFilter.getValue();

        ObservableList<ReportItem> filteredList = FXCollections.observableArrayList();

        for (ReportItem item : reportsList) {
            boolean matchesType = "All Types".equals(typeFilter) || item.getReportType().equals(typeFilter);
            boolean matchesStatus = "All Status".equals(statusFilterValue) || item.getStatus().equals(statusFilterValue);

            if (matchesType && matchesStatus) {
                filteredList.add(item);
            }
        }

        reportsTable.setItems(filteredList);
    }

    private void viewReportDetails() {
        ReportItem selectedItem = reportsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select a report to view details.");
            return;
        }

        showReportDetailsDialog(selectedItem);
    }

    private void showReportDetailsDialog(ReportItem reportItem) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(reportItem.getReportType() + " Report Details - ID: " + reportItem.getId());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(900);
        dialogStage.setHeight(700);

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Create details based on report type
        content.getChildren().addAll(createReportDetailsContent(reportItem));

        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);

        Scene dialogScene = new Scene(scrollPane);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private VBox createReportDetailsContent(ReportItem reportItem) {
        VBox detailsBox = new VBox(15);

        // Header
        Label headerLabel = new Label(reportItem.getReportType() + " Report Details");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headerLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Create main content with photo and details side by side
        HBox mainContent = new HBox(20);

        // Photo section
        VBox photoSection = createPhotoSection(reportItem);

        // Details section
        VBox detailsSection = new VBox(15);

        // Basic information
        VBox basicInfo = createDetailSection("Basic Information",
                "Report ID: " + reportItem.getId(),
                "Report Type: " + reportItem.getReportType(),
                "Status: " + reportItem.getStatus().toUpperCase(),
                "Report Date: " + reportItem.getReportDate()
        );

        // Complainant information
        VBox complainantInfo = createDetailSection("Complainant Information",
                "Name: " + reportItem.getComplainantName(),
                "Phone: " + reportItem.getComplainantPhone(),
                "NID/BC Number: " + (reportItem. getNidBc() != null ? reportItem. getNidBc() : "N/A"),
                "Father's Name: " + (reportItem.getFatherName() != null ? reportItem.getFatherName() : "N/A"),
                "Mother's Name: " + (reportItem.getMotherName() != null ? reportItem.getMotherName() : "N/A")
        );

        // Incident information
        VBox incidentInfo = createDetailSection("Incident Information",
                "Location: " + reportItem.getLocation(),
                "Incident Date: " + reportItem.getIncidentDate()
        );

        detailsSection.getChildren().addAll(basicInfo, complainantInfo, incidentInfo);

        mainContent.getChildren().addAll(photoSection, detailsSection);
        HBox.setHgrow(detailsSection, Priority.ALWAYS);

        detailsBox.getChildren().addAll(headerLabel, new Separator(), mainContent);

        return detailsBox;
    }

    private VBox createPhotoSection(ReportItem reportItem) {
        VBox photoSection = new VBox(10);
        photoSection.setPrefWidth(200);
        photoSection.setAlignment(Pos.CENTER);
        photoSection.setPadding(new Insets(10));
        photoSection.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label photoLabel = new Label("Complainant Photo");
        photoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        photoLabel.setStyle("-fx-text-fill: #495057;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-border-color: #ddd; -fx-border-width: 1px;");

        if (reportItem.getPhotoBytes() != null) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(reportItem.getPhotoBytes());

                Image image = new Image(bis);
                imageView.setImage(image);
            } catch (Exception e) {
                // If photo loading fails, show placeholder
                Label noPhotoLabel = new Label("Photo\nUnavailable");
                noPhotoLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px; -fx-text-alignment: center;");
                VBox placeholder = new VBox();
                placeholder.setAlignment(Pos.CENTER);
                placeholder.setPrefSize(150, 180);
                placeholder.setStyle("-fx-background-color: #e9ecef; -fx-border-color: #ddd; -fx-border-width: 1px;");
                placeholder.getChildren().add(noPhotoLabel);
                photoSection.getChildren().addAll(photoLabel, placeholder);
                return photoSection;
            }
        } else {
            // No photo available - show placeholder
            Label noPhotoLabel = new Label("No Photo\nAvailable");
            noPhotoLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px; -fx-text-alignment: center;");
            VBox placeholder = new VBox();
            placeholder.setAlignment(Pos.CENTER);
            placeholder.setPrefSize(150, 180);
            placeholder.setStyle("-fx-background-color: #e9ecef; -fx-border-color: #ddd; -fx-border-width: 1px;");
            placeholder.getChildren().add(noPhotoLabel);
            photoSection.getChildren().addAll(photoLabel, placeholder);
            return photoSection;
        }

        photoSection.getChildren().addAll(photoLabel, imageView);
        return photoSection;
    }

    private VBox createDetailSection(String title, String... details) {
        VBox section = new VBox(8);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #495057;");

        section.getChildren().add(titleLabel);

        for (String detail : details) {
            if (detail != null && !detail.trim().isEmpty() && !detail.contains("null")) {
                Label detailLabel = new Label(detail);
                detailLabel.setFont(Font.font("Arial", 12));
                detailLabel.setWrapText(true);
                section.getChildren().add(detailLabel);
            }
        }

        return section;
    }

    private void updateReportStatus(String newStatus) {
        ReportItem selectedItem = reportsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select a report to update.");
            return;
        }

        boolean success = false;
        int reportId = Integer.parseInt(selectedItem.getId());
        String reportType = selectedItem.getReportType();

        try {
            switch (reportType) {
                case "Fraud":
                    success = DatabaseHelper.updateFraudReportStatus(reportId, newStatus);
                    break;
                case "Money Laundering":
                    success = DatabaseHelper.updateMoneyLaunderingReportStatus(reportId, newStatus);
                    break;
                case "Kidnapping":
                    success = DatabaseHelper.updateKidnappingReportStatus(reportId, newStatus);
                    break;
                case "Drug Offense":
                    success = DatabaseHelper.updateDrugOffenseReportStatus(reportId, newStatus);
                    break;
                case "Extortion":
                    success = DatabaseHelper.updateExtortionReportStatus(reportId, newStatus);
                    break;
                case "Robbery":
                    success = DatabaseHelper.updateRobberyReportStatus(reportId, newStatus);
                    break;
            }

            if (success) {
                selectedItem.setStatus(newStatus);
                reportsTable.refresh();
                updateStatistics();
                showAlert("Success", "Report status updated to: " + newStatus.toUpperCase());
            } else {
                showAlert("Error", "Failed to update report status.");
            }
        } catch (Exception e) {
            showAlert("Database Error", "Error updating status: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Enhanced ReportItem class for TableView
    public static class ReportItem {
        private SimpleStringProperty id = new SimpleStringProperty("");
        private SimpleStringProperty reportType = new SimpleStringProperty("");
        private SimpleStringProperty complainantName = new SimpleStringProperty("");
        private SimpleStringProperty complainantPhone = new SimpleStringProperty("");
        private SimpleStringProperty nidBc = new SimpleStringProperty("");
        private SimpleStringProperty fatherName = new SimpleStringProperty("");
        private SimpleStringProperty motherName = new SimpleStringProperty("");
        private byte[] photoBytes;
        private SimpleStringProperty location = new SimpleStringProperty("");
        private SimpleStringProperty incidentDate = new SimpleStringProperty("");
        private SimpleStringProperty status = new SimpleStringProperty("");
        private SimpleStringProperty reportDate = new SimpleStringProperty("");
        private ResultSet resultSet;

        // ID
        public String getId() { return id.get(); }
        public void setId(String id) { this.id.set(id); }
        public SimpleStringProperty idProperty() { return id; }

        // Report Type
        public String getReportType() { return reportType.get(); }
        public void setReportType(String reportType) { this.reportType.set(reportType); }
        public SimpleStringProperty reportTypeProperty() { return reportType; }

        // Complainant Name
        public String getComplainantName() { return complainantName.get(); }
        public void setComplainantName(String complainantName) { this.complainantName.set(complainantName); }
        public SimpleStringProperty complainantNameProperty() { return complainantName; }

        // Complainant Phone
        public String getComplainantPhone() { return complainantPhone.get(); }
        public void setComplainantPhone(String complainantPhone) { this.complainantPhone.set(complainantPhone); }
        public SimpleStringProperty complainantPhoneProperty() { return complainantPhone; }

        // ✅ NID/BC (FIXED)
        public String getNidBc() { return nidBc.get(); }
        public void setNidBcNumber (String nidBc) { this.nidBc.set(nidBc); }
        public SimpleStringProperty nidBcProperty() { return nidBc; }

        // Father Name
        public String getFatherName() { return fatherName.get(); }
        public void setFatherName(String fatherName) { this.fatherName.set(fatherName != null ? fatherName : "N/A"); }
        public SimpleStringProperty fatherNameProperty() { return fatherName; }

        // Mother Name
        public String getMotherName() { return motherName.get(); }
        public void setMotherName(String motherName) { this.motherName.set(motherName != null ? motherName : "N/A"); }
        public SimpleStringProperty motherNameProperty() { return motherName; }

        // Photo
        public byte[] getPhotoBytes() { return photoBytes; }
        public void setPhotoBytes(byte[] photoBytes) { this.photoBytes = photoBytes; }

        // Location
        public String getLocation() { return location.get(); }
        public void setLocation(String location) { this.location.set(location); }
        public SimpleStringProperty locationProperty() { return location; }

        // Incident Date
        public String getIncidentDate() { return incidentDate.get(); }
        public void setIncidentDate(String incidentDate) { this.incidentDate.set(incidentDate); }
        public SimpleStringProperty incidentDateProperty() { return incidentDate; }

        // Status
        public String getStatus() { return status.get(); }
        public void setStatus(String status) { this.status.set(status); }
        public SimpleStringProperty statusProperty() { return status; }

        // Report Date
        public String getReportDate() { return reportDate.get(); }
        public void setReportDate(String reportDate) { this.reportDate.set(reportDate); }
        public SimpleStringProperty reportDateProperty() { return reportDate; }

        // ResultSet (optional, for internal use)
        public ResultSet getResultSet() { return resultSet; }
        public void setResultSet(ResultSet resultSet) { this.resultSet = resultSet; }
    }


    public static void main(String[] args) {
        launch(args);
    }


// Design-pattern integration: State controls legal case transitions,
    // Observer broadcasts status changes, Service coordinates investigation workflow.
    private final InvestigationService designPatternInvestigationService =
            new InvestigationService();

    public CaseContext createCaseState(int caseId) {
        return new CaseContext(caseId);
    }

    public CaseSubject createAdminObservableCase(int caseId, String initialStatus) {
        CaseSubject subject = new CaseSubject(caseId, initialStatus);
        subject.addObserver(new AdminNotification());
        return subject;
    }

    public InvestigationService getInvestigationService() {
        return designPatternInvestigationService;
    }
}
