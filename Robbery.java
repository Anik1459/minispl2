package org.example.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class Robbery extends Crime {

    // Add constructor to create table
    public Robbery() {
        DatabaseHelper.createRobberyTableIfNotExists();
    }

    @Override
    public void absMethod() {
        // Create main container
        VBox mainContainer = new VBox();
        mainContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);" +
                        "-fx-padding: 0;"
        );

        // Header Section
        VBox headerSection = createHeaderSection();

        // Form Section
        GridPane formGrid = buildForm();
        formGrid.setVgap(15);
        formGrid.setHgap(20);
        formGrid.setPadding(new Insets(30, 40, 30, 40));
        formGrid.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Wrap form in a container with padding
        VBox formContainer = new VBox(formGrid);
        formContainer.setPadding(new Insets(20, 30, 30, 30));

        int row = formGrid.getRowCount();

        // 1. Location of robbery
        Label locationLabel = createStyledLabel("📍 Where exactly did the robbery happen? *", true);
        TextField locationField = createStyledTextField();
        locationField.setPromptText("Location details");
        formGrid.add(locationLabel, 0, row);
        formGrid.add(locationField, 1, row++);

        // 2. Armed or unarmed
        Label armedLabel = createStyledLabel("🔫 Was the robbery armed or unarmed? *", true);
        ToggleGroup armedGroup = new ToggleGroup();
        RadioButton armedYes = createStyledRadioButton("Armed");
        RadioButton armedNo = createStyledRadioButton("Unarmed");
        armedYes.setToggleGroup(armedGroup);
        armedNo.setToggleGroup(armedGroup);

        TextField weaponTypeField = createStyledTextField();
        weaponTypeField.setPromptText("What kind of weapon?");
        weaponTypeField.setDisable(true);

        armedYes.setOnAction(e -> weaponTypeField.setDisable(false));
        armedNo.setOnAction(e -> {
            weaponTypeField.clear();
            weaponTypeField.setDisable(true);
        });

        formGrid.add(armedLabel, 0, row);
        HBox armedBox = new HBox(15, armedYes, armedNo, weaponTypeField);
        armedBox.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(armedBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 3. Number of robbers involved
        Label numberLabel = createStyledLabel("👥 How many robbers were involved? *", true);
        TextField numberField = createStyledTextField();
        numberField.setPromptText("Number and description");
        formGrid.add(numberLabel, 0, row);
        formGrid.add(numberField, 1, row++);

        // 4. Description of robbers
        Label descLabel = createStyledLabel("👤 Can you describe the robbers? *", true);
        TextArea descArea = createStyledTextArea();
        descArea.setPromptText("Height, clothing, distinguishing features, language, accent");
        descArea.setPrefRowCount(3);
        formGrid.add(descLabel, 0, row);
        formGrid.add(descArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 5. Actions or words of robbers
        Label actionsLabel = createStyledLabel("💬 Did the robbers say or do anything specific?", false);
        TextArea actionsArea = createStyledTextArea();
        actionsArea.setPromptText("Threats, demands, actions");
        actionsArea.setPrefRowCount(3);
        formGrid.add(actionsLabel, 0, row);
        formGrid.add(actionsArea, 1, row++);

        // 6. Items or money taken
        Label itemsLabel = createStyledLabel("💰 What items or money were taken? *", true);
        TextArea itemsArea = createStyledTextArea();
        itemsArea.setPromptText("List valuables stolen: cash, jewelry, electronics, documents");
        itemsArea.setPrefRowCount(3);
        formGrid.add(itemsLabel, 0, row);
        formGrid.add(itemsArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 7. Injuries during robbery
        Label injuryLabel = createStyledLabel("🩹 Was anyone injured during the robbery?", false);
        TextArea injuryArea = createStyledTextArea();
        injuryArea.setPromptText("Victim or bystanders, details of injuries");
        injuryArea.setPrefRowCount(3);
        formGrid.add(injuryLabel, 0, row);
        formGrid.add(injuryArea, 1, row++);

        // 8. Vehicle used by robbers
        Label vehicleLabel = createStyledLabel("🚗 Did the robbers use any vehicle?", false);
        TextField vehicleField = createStyledTextField();
        vehicleField.setPromptText("Type, color, registration if possible");
        formGrid.add(vehicleLabel, 0, row);
        formGrid.add(vehicleField, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 9. Witness info
        Label witnessLabel = createStyledLabel("👁️ Did anyone witness the robbery?", false);
        TextArea witnessArea = createStyledTextArea();
        witnessArea.setPromptText("Names or descriptions of witnesses");
        witnessArea.setPrefRowCount(3);
        formGrid.add(witnessLabel, 0, row);
        formGrid.add(witnessArea, 1, row++);

        // 10. Suspicious activities noticed
        Label suspiciousLabel = createStyledLabel("🔍 Did you notice anything suspicious before or after the robbery?", false);
        TextArea suspiciousArea = createStyledTextArea();
        suspiciousArea.setPrefRowCount(3);
        formGrid.add(suspiciousLabel, 0, row);
        formGrid.add(suspiciousArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 11. Previously reported
        Label reportedLabel = createStyledLabel("📝 Have you reported this incident anywhere else before?", false);
        ToggleGroup reportedGroup = new ToggleGroup();
        RadioButton yesReported = createStyledRadioButton("Yes");
        RadioButton noReported = createStyledRadioButton("No");
        yesReported.setToggleGroup(reportedGroup);
        noReported.setToggleGroup(reportedGroup);
        formGrid.add(reportedLabel, 0, row);
        HBox reportedBox = new HBox(20, yesReported, noReported);
        reportedBox.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(reportedBox, 1, row++);

        // 12. CCTV or video footage available
        Label cctvLabel = createStyledLabel("📹 Is there any CCTV or video footage available?", false);
        ToggleGroup cctvGroup = new ToggleGroup();
        RadioButton cctvYes = createStyledRadioButton("Yes");
        RadioButton cctvNo = createStyledRadioButton("No");
        cctvYes.setToggleGroup(cctvGroup);
        cctvNo.setToggleGroup(cctvGroup);
        formGrid.add(cctvLabel, 0, row);
        HBox cctvBox = new HBox(20, cctvYes, cctvNo);
        cctvBox.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(cctvBox, 1, row++);

        // Video file upload button (enabled only if cctvYes selected)
        Button uploadVideoBtn = createStyledButton("📤 Upload Video", false);
        uploadVideoBtn.setDisable(true);
        final FileChooser fileChooser = new FileChooser();

        cctvYes.setOnAction(e -> uploadVideoBtn.setDisable(false));
        cctvNo.setOnAction(e -> uploadVideoBtn.setDisable(true));

        uploadVideoBtn.setOnAction(e -> {
            Window stage = formGrid.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                uploadVideoBtn.setText("✅ Uploaded: " + file.getName());
                uploadVideoBtn.setDisable(true);
            }
        });
        formGrid.add(uploadVideoBtn, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 13. Targeted or random
        Label targetedLabel = createStyledLabel("🎯 Do you know if this was a targeted robbery or random?", false);
        TextField targetedField = createStyledTextField();
        targetedField.setPromptText("Explain if known");
        formGrid.add(targetedLabel, 0, row);
        formGrid.add(targetedField, 1, row++);

        // 14. Threats received
        Label threatsLabel = createStyledLabel("⚠️ Have you or anyone else received any threats related to this robbery?", false);
        TextArea threatsArea = createStyledTextArea();
        threatsArea.setPrefRowCount(3);
        formGrid.add(threatsLabel, 0, row);
        formGrid.add(threatsArea, 1, row++);

        // Add spacing before buttons
        formGrid.add(new Label(""), 0, row++);

        // Submit and Cancel buttons
        Button submitBtn = createStyledButton("📤 Submit Report", true);
        Button cancelBtn = createStyledButton("❌ Cancel", false);
        HBox buttonBox = new HBox(15, submitBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);
        formGrid.add(buttonBox, 1, row++);

        // Submit validation and database insertion
        submitBtn.setOnAction(e -> {
            if (!validateCommonFields()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all required common fields.");
                return;
            }
            
            // Simple mandatory checks
            if (locationField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter the robbery location.");
                return;
            }
            if (armedGroup.getSelectedToggle() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify if the robbery was armed or unarmed.");
                return;
            }
            if (armedYes.isSelected() && weaponTypeField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify the weapon type.");
                return;
            }
            if (numberField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter number and description of robbers.");
                return;
            }
            if (descArea.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please describe the robbers.");
                return;
            }
            if (itemsArea.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please list the items or money taken.");
                return;
            }
            if (reportedGroup.getSelectedToggle() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify if this incident has been reported elsewhere.");
                return;
            }
            if (cctvGroup.getSelectedToggle() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify if CCTV/video footage is available.");
                return;
            }
            // If CCTV yes but no file uploaded
            if (cctvYes.isSelected() && uploadVideoBtn.getText().equals("📤 Upload Video")) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please upload the video footage.");
                return;
            }

            // All validations passed - Insert into database
            boolean success = DatabaseHelper.insertRobberyReport(
                    // Common fields
                    nameField.getText().trim(),
                    fatherNameField.getText().trim(),
                    motherNameField.getText().trim(),
                    complainantPhoneField.getText().trim(),
                    nidBcField.getText().trim(),
                    locationField.getText().trim(),
                    datePicker.getValue() != null ? datePicker.getValue().toString() : "",
                    timeField.getText().trim(),
                    descriptionArea.getText().trim(),
                    photopath != null ? photopath : "",

                    // Robbery specific
                    locationField.getText().trim(), // robbery location
                    getSelectedRadioButton(armedGroup), // "Armed" or "Unarmed"
                    weaponTypeField.getText().trim(),
                    numberField.getText().trim(),
                    descArea.getText().trim(),

                    // Incident details
                    actionsArea.getText().trim(),
                    itemsArea.getText().trim(),
                    injuryArea.getText().trim(),
                    vehicleField.getText().trim(),

                    // Witness and evidence
                    witnessArea.getText().trim(),
                    suspiciousArea.getText().trim(),
                    getSelectedRadioButton(reportedGroup),
                    getSelectedRadioButton(cctvGroup),
                    cctvYes.isSelected() && !uploadVideoBtn.getText().equals("📤 Upload Video") ? 
                        uploadVideoBtn.getText().replace("✅ Uploaded: ", "") : "",

                    // Additional info
                    targetedField.getText().trim(),
                    threatsArea.getText().trim()
            );

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Robbery report submitted successfully! Police will investigate this case.");
                
                // Close the window after successful submission
                Stage stage = (Stage) submitBtn.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to submit robbery report. Please try again.");
            }
        });

        // Cancel button just closes the form window
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });

        // Add header and form to main container
        mainContainer.getChildren().addAll(headerSection, formContainer);

        // Wrap in scroll pane
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #f8f9fa;");

        Scene scene = new Scene(scrollPane, 900, 750);
        Stage stage = new Stage();
        stage.setTitle("Robbery Case Report - Bangladesh Police");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #8b0000, #a52a2a);" +
                        "-fx-border-color: #5c0000;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Main title with emoji
        Label titleLabel = new Label("🔫 ROBBERY CASE REPORT 🔫");
        titleLabel.setStyle(
                "-fx-font-family: 'System Bold', Arial;" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 3, 0, 2, 2);"
        );

        Label subtitleLabel = new Label("🏛️ Bangladesh Police Crime Reporting System");
        subtitleLabel.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #ffe4e1;" +
                        "-fx-font-weight: bold;"
        );

        // Info section with better styling
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        infoBox.setStyle(
                "-fx-background-color: rgba(139, 0, 0, 0.7);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("⚠️ Important Information about Robbery Cases:");
        infoTitle.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ffd700;"
        );

        Label infoText = new Label(
                "🚨 Report robbery immediately for urgent police response\n" +
                        "📍 Provide exact location and time details\n" +
                        "👥 Describe suspects as accurately as possible\n" +
                        "🚨 Emergency Robbery Hotline: 999 | 📞 Police Control: 100"
        );
        infoText.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #ffe4e1;" +
                        "-fx-line-spacing: 4px;"
        );

        infoBox.getChildren().addAll(infoTitle, infoText);
        header.getChildren().addAll(titleLabel, subtitleLabel, infoBox);

        return header;
    }

    private Label createStyledLabel(String text, boolean required) {
        Label label = new Label(text);
        String baseStyle =
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: " + (required ? "bold" : "normal") + ";" +
                        "-fx-text-fill: " + (required ? "#d32f2f" : "#424242") + ";";
        label.setStyle(baseStyle);
        return label;
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 8px 12px;"
        );
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #2196f3; -fx-border-width: 2px;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #2196f3; -fx-border-width: 2px;", ""));
            }
        });
        return field;
    }

    private TextArea createStyledTextArea() {
        TextArea area = new TextArea();
        area.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 8px 12px;"
        );
        area.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                area.setStyle(area.getStyle() + "-fx-border-color: #2196f3; -fx-border-width: 2px;");
            } else {
                area.setStyle(area.getStyle().replace("-fx-border-color: #2196f3; -fx-border-width: 2px;", ""));
            }
        });
        return area;
    }

    private RadioButton createStyledRadioButton(String text) {
        RadioButton radio = new RadioButton(text);
        radio.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: #424242;"
        );
        return radio;
    }

    private Button createStyledButton(String text, boolean primary) {
        Button button = new Button(text);
        String baseStyle =
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 25px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-cursor: hand;";

        if (primary) {
            button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #8b0000, #a52a2a);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #8b0000;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #a52a2a, #b22222);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #a52a2a;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #8b0000, #a52a2a);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #8b0000;"
            ));
        } else {
            button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #f5f5f5, #e0e0e0);" +
                    "-fx-text-fill: #424242;" +
                    "-fx-border-color: #bdbdbd;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #e0e0e0, #d0d0d0);" +
                    "-fx-text-fill: #424242;" +
                    "-fx-border-color: #9e9e9e;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #f5f5f5, #e0e0e0);" +
                    "-fx-text-fill: #424242;" +
                    "-fx-border-color: #bdbdbd;"
            ));
        }

        return button;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;"
        );

        alert.showAndWait();
    }

    // Helper method to get selected radio button from ToggleGroup
    private String getSelectedRadioButton(ToggleGroup group) {
        Toggle selected = group.getSelectedToggle();
        if (selected != null) {
            return ((RadioButton) selected).getText();
        }
        return "";
    }
}
