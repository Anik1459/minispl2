package org.example.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Extortion extends Crime {

    // Add constructor to create table
    public Extortion() {
        DatabaseHelper.createExtortionTableIfNotExists();
    }

    // Helper method to get selected checkboxes
    private String getSelectedCheckboxes(CheckBox... checkboxes) {
        StringBuilder result = new StringBuilder();
        for (CheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                if (result.length() > 0) result.append(", ");
                result.append(cb.getText());
            }
        }
        return result.toString();
    }

    // Helper method to get selected radio button from ToggleGroup
    private String getSelectedRadioButton(ToggleGroup group) {
        if (group.getSelectedToggle() != null) {
            return ((RadioButton) group.getSelectedToggle()).getText();
        }
        return "";
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

        // 1. Type of Extortion
        Label typeLabel = createStyledLabel("⚡ Type of Extortion *:", true);
        ComboBox<String> extortionTypeBox = createStyledComboBox();
        extortionTypeBox.getItems().addAll(
                "Blackmail",
                "Threat for money",
                "Coercion for favors",
                "Digital/Cyber extortion",
                "Sexual extortion (Sextortion)",
                "Business extortion",
                "Political extortion",
                "Other"
        );
        extortionTypeBox.setPromptText("Select type of extortion");
        formGrid.add(typeLabel, 0, row);
        formGrid.add(extortionTypeBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 2. Details of the Threat
        Label threatLabel = createStyledLabel("⚠️ Threat Details *:", true);

        Label threatTypeLabel = createStyledLabel("💬 Type of threat:", false);
        CheckBox verbalThreat = createStyledCheckBox("Verbal threats");
        CheckBox writtenThreat = createStyledCheckBox("Written threats (letter/note)");
        CheckBox digitalThreat = createStyledCheckBox("Digital threats (SMS/email/social media)");
        CheckBox physicalThreat = createStyledCheckBox("Physical intimidation");

        GridPane threatPane = new GridPane();
        threatPane.setHgap(15);
        threatPane.setVgap(8);
        threatPane.addRow(0, verbalThreat, writtenThreat);
        threatPane.addRow(1, digitalThreat, physicalThreat);

        formGrid.add(threatLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);
        formGrid.add(threatTypeLabel, 0, row);
        formGrid.add(threatPane, 1, row++);

        Label threatDetailsLabel = createStyledLabel("📝 Detailed description of threats:", false);
        TextArea threatDetailsArea = createStyledTextArea();
        threatDetailsArea.setPromptText("Describe the exact nature of threats made against you...");
        threatDetailsArea.setPrefRowCount(3);
        formGrid.add(threatDetailsLabel, 0, row);
        formGrid.add(threatDetailsArea, 1, row++);

        // Evidence Upload
        Label evidenceLabel = createStyledLabel("📎 Evidence of threats:", false);
        Button evidenceButton = createStyledButton("Upload Evidence Files", false);
        Label evidenceFileLabel = new Label("No files selected");
        evidenceFileLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");

        final File[] evidenceFile = new File[1];

        evidenceButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Evidence Files");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                    new FileChooser.ExtensionFilter("Document Files", "*.pdf", "*.doc", "*.docx"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                evidenceFile[0] = selectedFile;
                evidenceFileLabel.setText(selectedFile.getName());
            }
        });

        VBox evidenceBox = new VBox(5, evidenceButton, evidenceFileLabel);
        formGrid.add(evidenceLabel, 0, row);
        formGrid.add(evidenceBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 3. Demanded Amount or Action
        Label demandLabel = createStyledLabel("💰 Demands Made by Extorter *:", true);
        formGrid.add(demandLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label demandTypeLabel = createStyledLabel("📋 Type of demand:", false);
        RadioButton moneyDemand = createStyledRadioButton("Money");
        RadioButton propertyDemand = createStyledRadioButton("Property");
        RadioButton favorDemand = createStyledRadioButton("Personal favors");
        RadioButton businessDemand = createStyledRadioButton("Business concessions");
        RadioButton otherDemand = createStyledRadioButton("Other");

        ToggleGroup demandGroup = new ToggleGroup();
        moneyDemand.setToggleGroup(demandGroup);
        propertyDemand.setToggleGroup(demandGroup);
        favorDemand.setToggleGroup(demandGroup);
        businessDemand.setToggleGroup(demandGroup);
        otherDemand.setToggleGroup(demandGroup);

        HBox demandTypeBox = new HBox(15, moneyDemand, propertyDemand, favorDemand);
        HBox demandTypeBox2 = new HBox(15, businessDemand, otherDemand);
        VBox demandTypeContainer = new VBox(8, demandTypeBox, demandTypeBox2);

        formGrid.add(demandTypeLabel, 0, row);
        formGrid.add(demandTypeContainer, 1, row++);

        Label amountLabel = createStyledLabel("💵 Amount demanded (if money):", false);
        TextField amountField = createStyledTextField();
        amountField.setPromptText("Amount in BDT");
        formGrid.add(amountLabel, 0, row);
        formGrid.add(amountField, 1, row++);

        Label demandDetailsLabel = createStyledLabel("📄 Specific details of demands:", false);
        TextArea demandDetailsArea = createStyledTextArea();
        demandDetailsArea.setPromptText("Describe exactly what the extorter is demanding...");
        demandDetailsArea.setPrefRowCount(2);
        formGrid.add(demandDetailsLabel, 0, row);
        formGrid.add(demandDetailsArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 4. Deadline Information
        Label deadlineLabel = createStyledLabel("⏰ Deadline Given by Extorter:", false);

        RadioButton hasDeadline = createStyledRadioButton("Yes, deadline given");
        RadioButton noDeadline = createStyledRadioButton("No specific deadline");

        ToggleGroup deadlineGroup = new ToggleGroup();
        hasDeadline.setToggleGroup(deadlineGroup);
        noDeadline.setToggleGroup(deadlineGroup);

        HBox deadlineBox = new HBox(20, hasDeadline, noDeadline);
        formGrid.add(deadlineLabel, 0, row);
        formGrid.add(deadlineBox, 1, row++);

        Label deadlineDateLabel = createStyledLabel("📅 Deadline date and time:", false);
        DatePicker deadlineDatePicker = new DatePicker();
        deadlineDatePicker.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        );
        deadlineDatePicker.setDisable(true);

        TextField deadlineTimeField = createStyledTextField();
        deadlineTimeField.setPromptText("Time (e.g., 6:00 PM)");
        deadlineTimeField.setDisable(true);

        hasDeadline.setOnAction(e -> {
            deadlineDatePicker.setDisable(false);
            deadlineTimeField.setDisable(false);
        });
        noDeadline.setOnAction(e -> {
            deadlineDatePicker.setDisable(true);
            deadlineTimeField.setDisable(true);
        });

        HBox deadlineDetailsBox = new HBox(10, deadlineDatePicker, deadlineTimeField);
        formGrid.add(deadlineDateLabel, 0, row);
        formGrid.add(deadlineDetailsBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 5. Extorter Identity/Description
        Label extorterLabel = createStyledLabel("🎭 Identity/Description of Extorter:", false);
        formGrid.add(extorterLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label extorterKnownLabel = createStyledLabel("👤 Do you know the extorter?", false);
        RadioButton knownExtorter = createStyledRadioButton("Yes, I know them");
        RadioButton unknownExtorter = createStyledRadioButton("No, unknown person");

        ToggleGroup extorterGroup = new ToggleGroup();
        knownExtorter.setToggleGroup(extorterGroup);
        unknownExtorter.setToggleGroup(extorterGroup);

        HBox extorterKnownBox = new HBox(20, knownExtorter, unknownExtorter);
        formGrid.add(extorterKnownLabel, 0, row);
        formGrid.add(extorterKnownBox, 1, row++);

        Label extorterNameLabel = createStyledLabel("📛 Name of extorter (if known):", false);
        TextField extorterNameField = createStyledTextField();
        extorterNameField.setPromptText("Full name of the person");
        formGrid.add(extorterNameLabel, 0, row);
        formGrid.add(extorterNameField, 1, row++);

        Label extorterContactLabel = createStyledLabel("📞 Contact details of extorter:", false);
        TextField extorterContactField = createStyledTextField();
        extorterContactField.setPromptText("Phone number, email, social media handles");
        formGrid.add(extorterContactLabel, 0, row);
        formGrid.add(extorterContactField, 1, row++);

        Label extorterDescLabel = createStyledLabel("🖼️ Physical description:", false);
        TextArea extorterDescArea = createStyledTextArea();
        extorterDescArea.setPromptText("Age, height, appearance, distinguishing features...");
        extorterDescArea.setPrefRowCount(2);
        formGrid.add(extorterDescLabel, 0, row);
        formGrid.add(extorterDescArea, 1, row++);

        Label relationshipLabel = createStyledLabel("👥 Relationship to you:", false);
        ComboBox<String> relationshipBox = createStyledComboBox();
        relationshipBox.getItems().addAll(
                "Family member",
                "Friend/Acquaintance",
                "Colleague/Business associate",
                "Ex-partner",
                "Neighbor",
                "Stranger",
                "Online contact only",
                "Other"
        );
        relationshipBox.setPromptText("Select relationship");
        formGrid.add(relationshipLabel, 0, row);
        formGrid.add(relationshipBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 6. Witness Information
        Label witnessLabel = createStyledLabel("👥 Witness Information:", false);
        formGrid.add(witnessLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label hasWitnessLabel = createStyledLabel("👁️ Were there any witnesses?", false);
        RadioButton hasWitness = createStyledRadioButton("Yes");
        RadioButton noWitness = createStyledRadioButton("No");

        ToggleGroup witnessGroup = new ToggleGroup();
        hasWitness.setToggleGroup(witnessGroup);
        noWitness.setToggleGroup(witnessGroup);

        HBox witnessBox = new HBox(20, hasWitness, noWitness);
        formGrid.add(hasWitnessLabel, 0, row);
        formGrid.add(witnessBox, 1, row++);

        Label witness1Label = createStyledLabel("👤 Witness 1 details:", false);
        TextField witness1Field = createStyledTextField();
        witness1Field.setPromptText("Name, phone number, relationship");
        witness1Field.setDisable(true);
        formGrid.add(witness1Label, 0, row);
        formGrid.add(witness1Field, 1, row++);

        Label witness2Label = createStyledLabel("👤 Witness 2 details:", false);
        TextField witness2Field = createStyledTextField();
        witness2Field.setPromptText("Name, phone number, relationship");
        witness2Field.setDisable(true);
        formGrid.add(witness2Label, 0, row);
        formGrid.add(witness2Field, 1, row++);

        hasWitness.setOnAction(e -> {
            witness1Field.setDisable(false);
            witness2Field.setDisable(false);
        });
        noWitness.setOnAction(e -> {
            witness1Field.setDisable(true);
            witness2Field.setDisable(true);
        });

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 7. Prior Reporting/Payment History
        Label historyLabel = createStyledLabel("📋 Prior Reporting/Payment History:", false);
        formGrid.add(historyLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label reportedBeforeLabel = createStyledLabel("🚨 Have you reported this before?", false);
        RadioButton reportedBefore = createStyledRadioButton("Yes");
        RadioButton notReportedBefore = createStyledRadioButton("No");

        ToggleGroup reportGroup = new ToggleGroup();
        reportedBefore.setToggleGroup(reportGroup);
        notReportedBefore.setToggleGroup(reportGroup);

        HBox reportBox = new HBox(20, reportedBefore, notReportedBefore);
        formGrid.add(reportedBeforeLabel, 0, row);
        formGrid.add(reportBox, 1, row++);

        Label reportDetailsLabel = createStyledLabel("📄 Previous reporting details:", false);
        TextArea reportDetailsArea = createStyledTextArea();
        reportDetailsArea.setPromptText("When, where, and to whom you reported...");
        reportDetailsArea.setPrefRowCount(2);
        reportDetailsArea.setDisable(true);
        formGrid.add(reportDetailsLabel, 0, row);
        formGrid.add(reportDetailsArea, 1, row++);

        Label paidBeforeLabel = createStyledLabel("💸 Have you paid any amount before?", false);
        RadioButton paidBefore = createStyledRadioButton("Yes");
        RadioButton notPaidBefore = createStyledRadioButton("No");

        ToggleGroup paymentGroup = new ToggleGroup();
        paidBefore.setToggleGroup(paymentGroup);
        notPaidBefore.setToggleGroup(paymentGroup);

        HBox paymentBox = new HBox(20, paidBefore, notPaidBefore);
        formGrid.add(paidBeforeLabel, 0, row);
        formGrid.add(paymentBox, 1, row++);

        Label paymentDetailsLabel = createStyledLabel("💰 Payment details:", false);
        TextArea paymentDetailsArea = createStyledTextArea();
        paymentDetailsArea.setPromptText("Amount, method, date of payment...");
        paymentDetailsArea.setPrefRowCount(2);
        paymentDetailsArea.setDisable(true);
        formGrid.add(paymentDetailsLabel, 0, row);
        formGrid.add(paymentDetailsArea, 1, row++);

        reportedBefore.setOnAction(e -> reportDetailsArea.setDisable(false));
        notReportedBefore.setOnAction(e -> reportDetailsArea.setDisable(true));
        paidBefore.setOnAction(e -> paymentDetailsArea.setDisable(false));
        notPaidBefore.setOnAction(e -> paymentDetailsArea.setDisable(true));

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 8. Protective Measures Request
        Label protectionLabel = createStyledLabel("🛡️ Protective Measures Request:", false);
        formGrid.add(protectionLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label needProtectionLabel = createStyledLabel("⚖️ Do you need immediate protection?", false);
        RadioButton needProtection = createStyledRadioButton("Yes, I need protection");
        RadioButton noProtection = createStyledRadioButton("No protection needed");

        ToggleGroup protectionGroup = new ToggleGroup();
        needProtection.setToggleGroup(protectionGroup);
        noProtection.setToggleGroup(protectionGroup);

        HBox protectionBox = new HBox(20, needProtection, noProtection);
        formGrid.add(needProtectionLabel, 0, row);
        formGrid.add(protectionBox, 1, row++);

        Label protectionTypeLabel = createStyledLabel("📋 Type of protection needed:", false);
        CheckBox restrainingOrder = createStyledCheckBox("Restraining order");
        CheckBox policeProtection = createStyledCheckBox("Police protection");
        CheckBox witnessProtection = createStyledCheckBox("Witness protection");
        CheckBox familyProtection = createStyledCheckBox("Family protection");

        GridPane protectionPane = new GridPane();
        protectionPane.setHgap(15);
        protectionPane.setVgap(8);
        protectionPane.addRow(0, restrainingOrder, policeProtection);
        protectionPane.addRow(1, witnessProtection, familyProtection);
        protectionPane.setDisable(true);

        formGrid.add(protectionTypeLabel, 0, row);
        formGrid.add(protectionPane, 1, row++);

        Label protectionReasonLabel = createStyledLabel("📝 Reason for protection request:", false);
        TextArea protectionReasonArea = createStyledTextArea();
        protectionReasonArea.setPromptText("Explain why you need protection and any immediate threats...");
        protectionReasonArea.setPrefRowCount(2);
        protectionReasonArea.setDisable(true);
        formGrid.add(protectionReasonLabel, 0, row);
        formGrid.add(protectionReasonArea, 1, row++);

        needProtection.setOnAction(e -> {
            protectionPane.setDisable(false);
            protectionReasonArea.setDisable(false);
        });
        noProtection.setOnAction(e -> {
            protectionPane.setDisable(true);
            protectionReasonArea.setDisable(true);
        });

        // Add spacing before submit button
        formGrid.add(new Label(""), 0, row++);
        formGrid.add(new Label(""), 0, row++);

        // Submit Button - UPDATED WITH DATABASE STORAGE
        Button submitButton = createStyledButton("🚨 Submit Extortion Report", true);
        submitButton.setPrefWidth(300);
        submitButton.setOnAction(e -> {
            if (validateForm(extortionTypeBox, threatDetailsArea)) {

                // Collect all form data
                String threatTypes = getSelectedCheckboxes(verbalThreat, writtenThreat, digitalThreat, physicalThreat);
                String demandType = getSelectedRadioButton(demandGroup);
                String hasDeadlineText = getSelectedRadioButton(deadlineGroup);
                String extorterKnown = getSelectedRadioButton(extorterGroup);
                String hasWitnessText = getSelectedRadioButton(witnessGroup);
                String reportedBeforeText = getSelectedRadioButton(reportGroup);
                String paidBeforeText = getSelectedRadioButton(paymentGroup);
                String needsProtection = getSelectedRadioButton(protectionGroup);
                String protectionTypes = getSelectedCheckboxes(restrainingOrder, policeProtection, witnessProtection, familyProtection);

                // Store in database
                boolean success = DatabaseHelper.insertExtortionReport(
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

                        // Extortion specific
                        extortionTypeBox.getValue() != null ? extortionTypeBox.getValue() : "",
                        threatTypes,
                        threatDetailsArea.getText().trim(),
                        evidenceFile[0] != null ? evidenceFile[0].getAbsolutePath() : "",

                        // Demands
                        demandType,
                        amountField.getText().trim(),
                        demandDetailsArea.getText().trim(),

                        // Deadline
                        hasDeadlineText,
                        deadlineDatePicker.getValue() != null ? deadlineDatePicker.getValue().toString() : "",
                        deadlineTimeField.getText().trim(),

                        // Extorter info
                        extorterKnown,
                        extorterNameField.getText().trim(),
                        extorterContactField.getText().trim(),
                        extorterDescArea.getText().trim(),
                        relationshipBox.getValue() != null ? relationshipBox.getValue() : "",

                        // Witnesses
                        hasWitnessText,
                        witness1Field.getText().trim(),
                        witness2Field.getText().trim(),

                        // Prior history
                        reportedBeforeText,
                        reportDetailsArea.getText().trim(),
                        paidBeforeText,
                        paymentDetailsArea.getText().trim(),

                        // Protection
                        needsProtection,
                        protectionTypes,
                        protectionReasonArea.getText().trim()
                );

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Report Submitted",
                            "✅ Your extortion case report has been successfully submitted to Bangladesh Police.\n\n" +
                                    "📋 Case Reference: EXT" + System.currentTimeMillis() + "\n" +
                                    "📞 You will be contacted within 24 hours for follow-up.\n\n" +
                                    "🚨 If you are in immediate danger, please call 999 immediately!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error",
                            "❌ There was an error saving your report. Please try again or contact support.");
                }
            }
        });

        HBox submitBox = new HBox(submitButton);
        submitBox.setAlignment(Pos.CENTER);
        submitBox.setPadding(new Insets(20, 0, 10, 0));

        formGrid.add(submitBox, 0, row, 2, 1);

        // Add all components to main container
        mainContainer.getChildren().addAll(headerSection, formContainer);

        // Create scroll pane
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Create scene and stage
        Scene scene = new Scene(scrollPane, 900, 750);
        Stage stage = new Stage();
        stage.setTitle("Extortion Case Report – Bangladesh Police");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #8e44ad, #9b59b6);" +
                        "-fx-border-color: #732d91;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Main title with emoji
        Label titleLabel = new Label("⚡ EXTORTION CASE REPORT ⚡");
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
                        "-fx-text-fill: #f4e6ff;" +
                        "-fx-font-weight: bold;"
        );

        // Info section with better styling
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        infoBox.setStyle(
                "-fx-background-color: rgba(155, 89, 182, 0.7);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("⚠️ Important Information about Extortion Cases:");
        infoTitle.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #fff3cd;"
        );

        Label infoText = new Label(
                "🚨 Report extortion immediately - time is critical for investigation\n" +
                        "📱 Do not delete any threatening messages or communications\n" +
                        "💰 Do not make payments without consulting police first\n" +
                        "📞 Emergency: 999 | 🚨 Anti-Crime Helpline: 16263 | 👮 Women Helpline: 109"
        );
        infoText.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #f8f9fa;" +
                        "-fx-line-spacing: 4px;"
        );

        infoBox.getChildren().addAll(infoTitle, infoText);
        header.getChildren().addAll(titleLabel, subtitleLabel, infoBox);

        return header;
    }

    private boolean validateForm(ComboBox<String> extortionTypeBox, TextArea threatDetailsArea) {
        // Validate common Crime fields
        if (!validateCommonFields()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "❌ Please fill in all required common fields:\n" +
                            "• Your name\n• Phone number\n• NID/BC number\n• Location\n• Date and time\n• Description\n• Father's and Mother's name");
            return false;
        }

        // Validate extortion-specific required fields
        if (extortionTypeBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "❌ Please select the type of extortion.");
            return false;
        }

        if (threatDetailsArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "❌ Please provide detailed description of the threats made.");
            return false;
        }

        return true;
    }

    private Label createStyledLabel(String text, boolean required) {
        Label label = new Label(text);
        String baseStyle =
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: " + (required ? "bold" : "normal") + ";" +
                        "-fx-text-fill: " + (required ? "#8e44ad" : "#424242") + ";";
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

        field.setOnMouseEntered(e -> field.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #9b59b6;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 7px 11px;"
        ));

        field.setOnMouseExited(e -> field.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 8px 12px;"
        ));

        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Focus gained
                field.setStyle(
                        "-fx-font-family: Arial, sans-serif;" +
                                "-fx-font-size: 13px;" +
                                "-fx-background-color: white;" +
                                "-fx-border-color: #8e44ad;" +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 5px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-padding: 7px 11px;"
                );
            } else {
                // Focus lost
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

        area.setOnMouseEntered(e -> area.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #9b59b6;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 7px 11px;"
        ));

        area.setOnMouseExited(e -> area.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 8px 12px;"
        ));

        area.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Focus gained
                area.setStyle(
                        "-fx-font-family: Arial, sans-serif;" +
                                "-fx-font-size: 13px;" +
                                "-fx-background-color: white;" +
                                "-fx-border-color: #8e44ad;" +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 5px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-padding: 7px 11px;"
                );
            } else {
                // Focus lost
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
            }
        });

        return area;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> combo = new ComboBox<>();
        combo.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        );

        combo.setOnMouseEntered(e -> combo.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #9b59b6;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        ));

        combo.setOnMouseExited(e -> combo.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        ));

        return combo;
    }

    private CheckBox createStyledCheckBox(String text) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: #424242;"
        );
        return checkBox;
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
                    "-fx-background-color: linear-gradient(to bottom, #8e44ad, #7d3c98);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #8e44ad;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #7d3c98, #6c3483);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #7d3c98;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #8e44ad, #7d3c98);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #8e44ad;"
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 13px;"
        );

        alert.showAndWait();
    }
}
