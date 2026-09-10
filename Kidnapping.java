package org.example.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Kidnapping extends Crime {
    ToggleGroup ransomGroup;
    ToggleGroup previousGroup;
    ToggleGroup amberGroup;
    ToggleGroup witnessGroup;
    ToggleGroup kidnapperKnownGroup;
    ToggleGroup genderGroup;
    
    // Form fields
    TextField victimNameField;

    public Kidnapping() {
        DatabaseHelper.createKidnappingTableIfNotExists();
    }
    private String getSelectedCheckboxes(CheckBox... checkboxes) {
        StringBuilder result = new StringBuilder();
        for (CheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(cb.getText());
            }
        }
        return result.toString();
    }
    private String getSelectedRadioButton(ToggleGroup group) {
        Toggle selected = group.getSelectedToggle();
        if (selected != null) {
            return ((RadioButton) selected).getText();
        }
        return "";
    }
    @Override
    public void absMethod() {
        System.out.println("Kidnapping.absMethod() called!"); // Debug line

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

        // 1. Victim Information Section
        Label victimSectionLabel = createSectionLabel("👤 VICTIM INFORMATION");
        formGrid.add(victimSectionLabel, 0, row++, 2, 1);

        // Victim's Name
        Label nameLabel = createStyledLabel("👤 Victim's Name *:", true);
        victimNameField = createStyledTextField();
        victimNameField.setPromptText("Enter victim's full name");
        formGrid.add(nameLabel, 0, row);
        formGrid.add(victimNameField, 1, row++);

        // Victim's Age
        Label ageLabel = createStyledLabel("🎂 Victim's Age *:", true);
        TextField ageField = createStyledTextField();
        ageField.setPromptText("Age in years");
        formGrid.add(ageLabel, 0, row);
        formGrid.add(ageField, 1, row++);

        // Victim's Gender
        Label genderLabel = createStyledLabel("⚧️ Victim's Gender *:", true);
        genderGroup = new ToggleGroup();
        RadioButton maleRadio = createStyledRadioButton("Male");
        RadioButton femaleRadio = createStyledRadioButton("Female");
        RadioButton otherRadio = createStyledRadioButton("Other");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);
        otherRadio.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(20, maleRadio, femaleRadio, otherRadio);
        formGrid.add(genderLabel, 0, row);
        formGrid.add(genderBox, 1, row++);

        // Physical Description
        Label heightLabel = createStyledLabel("📏 Victim's Height:", false);
        TextField heightField = createStyledTextField();
        heightField.setPromptText("e.g., 5'6\" or 168 cm");
        formGrid.add(heightLabel, 0, row);
        formGrid.add(heightField, 1, row++);

        Label clothingLabel = createStyledLabel("👕 Clothing at Time of Kidnapping:", false);
        TextArea clothingArea = createStyledTextArea();
        clothingArea.setPromptText("Describe what the victim was wearing...");
        clothingArea.setPrefRowCount(3);
        formGrid.add(clothingLabel, 0, row);
        formGrid.add(clothingArea, 1, row++);

        Label marksLabel = createStyledLabel("🔍 Distinguishing Marks/Features:", false);
        TextArea marksArea = createStyledTextArea();
        marksArea.setPromptText("Scars, tattoos, birthmarks, disabilities, etc...");
        marksArea.setPrefRowCount(3);
        formGrid.add(marksLabel, 0, row);
        formGrid.add(marksArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 2. Last Known Information Section
        Label lastKnownSectionLabel = createSectionLabel("📍 LAST KNOWN INFORMATION");
        formGrid.add(lastKnownSectionLabel, 0, row++, 2, 1);

        Label lastLocationLabel = createStyledLabel("🗺️ Last Known Location *:", true);
        TextField lastLocationField = createStyledTextField();
        lastLocationField.setPromptText("Specific address or area where victim was last seen");
        formGrid.add(lastLocationLabel, 0, row);
        formGrid.add(lastLocationField, 1, row++);

        Label lastSeenTimeLabel = createStyledLabel("🕐 Approximate Time Last Seen *:", true);
        TextField lastSeenTimeField = createStyledTextField();
        lastSeenTimeField.setPromptText("e.g., 3:30 PM, Yesterday evening");
        formGrid.add(lastSeenTimeLabel, 0, row);
        formGrid.add(lastSeenTimeField, 1, row++);

        Label lastActivityLabel = createStyledLabel("🚶 Last Known Activity:", false);
        TextArea lastActivityArea = createStyledTextArea();
        lastActivityArea.setPromptText("What was the victim doing when last seen? (going to school, shopping, etc.)");
        lastActivityArea.setPrefRowCount(3);
        formGrid.add(lastActivityLabel, 0, row);
        formGrid.add(lastActivityArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 3. Suspected Kidnapper Information Section
        Label kidnapperSectionLabel = createSectionLabel("🔍 SUSPECTED KIDNAPPER INFORMATION");
        formGrid.add(kidnapperSectionLabel, 0, row++, 2, 1);

        Label kidnapperKnownLabel = createStyledLabel("❓ Is the kidnapper known to you?", false);
        kidnapperKnownGroup = new ToggleGroup();
        RadioButton kidnapperYes = createStyledRadioButton("Yes, known person");
        RadioButton kidnapperNo = createStyledRadioButton("No, stranger");
        RadioButton kidnapperUnsure = createStyledRadioButton("Unsure");
        kidnapperYes.setToggleGroup(kidnapperKnownGroup);
        kidnapperNo.setToggleGroup(kidnapperKnownGroup);
        kidnapperUnsure.setToggleGroup(kidnapperKnownGroup);

        HBox kidnapperKnownBox = new HBox(15, kidnapperYes, kidnapperNo, kidnapperUnsure);
        formGrid.add(kidnapperKnownLabel, 0, row);
        formGrid.add(kidnapperKnownBox, 1, row++);

        Label kidnapperDescLabel = createStyledLabel("🧑 Kidnapper Description:", false);
        TextArea kidnapperDescArea = createStyledTextArea();
        kidnapperDescArea.setPromptText("Physical description, clothing, vehicle, etc...");
        kidnapperDescArea.setPrefRowCount(4);
        formGrid.add(kidnapperDescLabel, 0, row);
        formGrid.add(kidnapperDescArea, 1, row++);

        Label relationshipLabel = createStyledLabel("👥 Relationship to Victim (if known):", false);
        ComboBox<String> relationshipBox = createStyledComboBox();
        relationshipBox.getItems().addAll(
                "Family member",
                "Friend/Acquaintance",
                "Neighbor",
                "Ex-partner/Spouse",
                "Teacher/Colleague",
                "Stranger",
                "Other"
        );
        relationshipBox.setPromptText("Select relationship");
        formGrid.add(relationshipLabel, 0, row);
        formGrid.add(relationshipBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 4. Witness Information Section
        Label witnessSectionLabel = createSectionLabel("👁️ WITNESS INFORMATION");
        formGrid.add(witnessSectionLabel, 0, row++, 2, 1);

        Label witnessAvailableLabel = createStyledLabel("🗣️ Are there any witnesses?", false);
        witnessGroup = new ToggleGroup();
        RadioButton witnessYes = createStyledRadioButton("Yes");
        RadioButton witnessNo = createStyledRadioButton("No");
        RadioButton witnessUnsure = createStyledRadioButton("Not sure");
        witnessYes.setToggleGroup(witnessGroup);
        witnessNo.setToggleGroup(witnessGroup);
        witnessUnsure.setToggleGroup(witnessGroup);

        HBox witnessBox = new HBox(20, witnessYes, witnessNo, witnessUnsure);
        formGrid.add(witnessAvailableLabel, 0, row);
        formGrid.add(witnessBox, 1, row++);

        Label witnessDetailsLabel = createStyledLabel("📋 Witness Details:", false);
        TextArea witnessDetailsArea = createStyledTextArea();
        witnessDetailsArea.setPromptText("Names, contact numbers, and what they saw...");
        witnessDetailsArea.setPrefRowCount(4);
        witnessDetailsArea.setDisable(true);
        formGrid.add(witnessDetailsLabel, 0, row);
        formGrid.add(witnessDetailsArea, 1, row++);

        // Enable/disable witness details based on selection
        witnessYes.setOnAction(e -> witnessDetailsArea.setDisable(false));
        witnessNo.setOnAction(e -> witnessDetailsArea.setDisable(true));
        witnessUnsure.setOnAction(e -> witnessDetailsArea.setDisable(true));

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 5. Emergency Alert Section
        Label alertSectionLabel = createSectionLabel("🚨 EMERGENCY ALERT");
        formGrid.add(alertSectionLabel, 0, row++, 2, 1);

        Label amberAlertLabel = createStyledLabel("📢 Should police initiate an Amber Alert?", false);
        amberGroup = new ToggleGroup();
        RadioButton amberYes = createStyledRadioButton("Yes, urgent alert needed");
        RadioButton amberNo = createStyledRadioButton("No, not necessary");
        RadioButton amberUnsure = createStyledRadioButton("Let police decide");
        amberYes.setToggleGroup(amberGroup);
        amberNo.setToggleGroup(amberGroup);
        amberUnsure.setToggleGroup(amberGroup);

        VBox amberBox = new VBox(8, amberYes, amberNo, amberUnsure);
        formGrid.add(amberAlertLabel, 0, row);
        formGrid.add(amberBox, 1, row++);

        Label urgencyLabel = createStyledLabel("⏰ Urgency Level:", false);
        ComboBox<String> urgencyBox = createStyledComboBox();
        urgencyBox.getItems().addAll(
                "Critical - Child under 12",
                "High - Vulnerable person",
                "Medium - Adult with disabilities",
                "Standard - Other cases"
        );
        urgencyBox.setPromptText("Select urgency level");
        formGrid.add(urgencyLabel, 0, row);
        formGrid.add(urgencyBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 6. Ransom Information Section
        Label ransomSectionLabel = createSectionLabel("💰 RANSOM INFORMATION");
        formGrid.add(ransomSectionLabel, 0, row++, 2, 1);

        Label ransomDemandLabel = createStyledLabel("💸 Have you received any ransom demands?", false);
        ransomGroup = new ToggleGroup();
        RadioButton ransomYes = createStyledRadioButton("Yes");
        RadioButton ransomNo = createStyledRadioButton("No");
        ransomYes.setToggleGroup(ransomGroup);
        ransomNo.setToggleGroup(ransomGroup);

        HBox ransomBox = new HBox(20, ransomYes, ransomNo);
        formGrid.add(ransomDemandLabel, 0, row);
        formGrid.add(ransomBox, 1, row++);

        Label ransomDetailsLabel = createStyledLabel("📞 Ransom Communication Details:", false);
        TextArea ransomDetailsArea = createStyledTextArea();
        ransomDetailsArea.setPromptText("Amount demanded, method of contact, exact words used, phone numbers...");
        ransomDetailsArea.setPrefRowCount(4);
        ransomDetailsArea.setDisable(true);
        formGrid.add(ransomDetailsLabel, 0, row);
        formGrid.add(ransomDetailsArea, 1, row++);

        Label ransomAmountLabel = createStyledLabel("💵 Ransom Amount (if specified):", false);
        TextField ransomAmountField = createStyledTextField();
        ransomAmountField.setPromptText("Amount in BDT");
        ransomAmountField.setDisable(true);
        formGrid.add(ransomAmountLabel, 0, row);
        formGrid.add(ransomAmountField, 1, row++);

        // Enable/disable ransom fields based on selection
        ransomYes.setOnAction(e -> {
            ransomDetailsArea.setDisable(false);
            ransomAmountField.setDisable(false);
        });
        ransomNo.setOnAction(e -> {
            ransomDetailsArea.setDisable(true);
            ransomAmountField.setDisable(true);
        });

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 7. Additional Information Section
        Label additionalSectionLabel = createSectionLabel("ℹ️ ADDITIONAL INFORMATION");
        formGrid.add(additionalSectionLabel, 0, row++, 2, 1);

        Label previousReportLabel = createStyledLabel("📝 Has this been reported elsewhere?", false);
        previousGroup = new ToggleGroup();
        RadioButton previousYes = createStyledRadioButton("Yes, reported earlier");
        RadioButton previousNo = createStyledRadioButton("No, first report");
        previousYes.setToggleGroup(previousGroup);
        previousNo.setToggleGroup(previousGroup);

        HBox previousBox = new HBox(20, previousYes, previousNo);
        formGrid.add(previousReportLabel, 0, row);
        formGrid.add(previousBox, 1, row++);

        Label motiveLabel = createStyledLabel("🤔 Suspected Motive:", false);
        CheckBox motiveMoney = createStyledCheckBox("Financial gain");
        CheckBox motiveRevenge = createStyledCheckBox("Revenge");
        CheckBox motiveFamily = createStyledCheckBox("Family dispute");
        CheckBox motivePolitical = createStyledCheckBox("Political");
        CheckBox motiveUnknown = createStyledCheckBox("Unknown");
        CheckBox motiveOther = createStyledCheckBox("Other");

        GridPane motivePane = new GridPane();
        motivePane.setHgap(15);
        motivePane.setVgap(8);
        motivePane.addRow(0, motiveMoney, motiveRevenge, motiveFamily);
        motivePane.addRow(1, motivePolitical, motiveUnknown, motiveOther);

        formGrid.add(motiveLabel, 0, row);
        formGrid.add(motivePane, 1, row++);

        Label actionRequestLabel = createStyledLabel("🚔 Specific action requested from police:", false);
        TextArea actionRequestArea = createStyledTextArea();
        actionRequestArea.setPromptText("Describe what specific actions you want police to take...");
        actionRequestArea.setPrefRowCount(4);
        formGrid.add(actionRequestLabel, 0, row);
        formGrid.add(actionRequestArea, 1, row++);

        // Add spacing before buttons
        formGrid.add(new Label(""), 0, row++);

        // Buttons
        Button submitBtn = createStyledButton("📤 Submit Kidnapping Report", true);
        Button clearBtn = createStyledButton("🗑️ Clear Form", false);

        HBox buttonBox = new HBox(15, submitBtn, clearBtn);
        buttonBox.setAlignment(Pos.CENTER);
        formGrid.add(buttonBox, 1, row++);

        // Submit button logic
        submitBtn.setOnAction(e -> {
            if (!validateCommonFields()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all required common fields.");
                return;
            }
            if (victimNameField.getText().trim().isEmpty() ||
                    ageField.getText().trim().isEmpty() ||
                    genderGroup.getSelectedToggle() == null ||
                    lastLocationField.getText().trim().isEmpty() ||
                    lastSeenTimeField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all mandatory kidnapping-specific fields including victim's name.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Kidnapping report submitted successfully! Police will prioritize this case.");
            DatabaseHelper.insertKidnappingReport(
                    nameField.getText().trim(),
                    fatherNameField.getText().trim(),
                    motherNameField.getText().trim(),
                    complainantPhoneField.getText().trim(),
                    locationField.getText().trim(),
                    datePicker.getValue().toString(), // Converts LocalDate to String
                    timeField.getText().trim(),
                    descriptionArea.getText().trim(),
                    nidBcField.getText().trim(),
                    photopath, // Make sure photopath is a String storing the selected file path

                    accusedName.getText().trim(),
                    accusedPhone.getText().trim(),
                    accusedEmail.getText().trim(),
                    accusedAddress.getText().trim(),

                    // Victim Info
                    victimNameField.getText().trim(), // Victim's name
                    Integer.parseInt(ageField.getText().trim()), // Convert age text to int
                    getSelectedGender(), // You'll implement this to get selected gender string
                    heightField.getText().trim(),
                    clothingArea.getText().trim(),
                    marksArea.getText().trim(),

                    // Last Known Info
                    lastLocationField.getText().trim(),
                    lastSeenTimeField.getText().trim(),
                    lastActivityArea.getText().trim(),

                    // Kidnapper Info
                    getKidnapperKnownValue(), // e.g., "Yes, known person", "No, stranger", or "Unsure"
                    kidnapperDescArea.getText().trim(),
                    relationshipBox.getValue(),

                    // Witness Info
                    getWitnessAvailableValue(), // e.g., "Yes", "No", or "Not sure"
                    witnessDetailsArea.getText().trim(),

                    // Emergency Alert
                    getAmberAlertValue(), // e.g., "Yes, urgent alert needed", "No, not necessary", or "Let police decide"
                    urgencyBox.getValue(),

                    // Ransom Info
                    getRansomDemandValue(), // "Yes" or "No"
                    ransomDetailsArea.getText().trim(),
                    ransomAmountField.getText().trim(),

                    // Additional Info
                    getPreviousReportValue(), // "Yes, reported earlier" or "No, first report"
                    motiveMoney.isSelected() ? 1 : 0,
                    motiveRevenge.isSelected() ? 1 : 0,
                    motiveFamily.isSelected() ? 1 : 0,
                    motivePolitical.isSelected() ? 1 : 0,
                    motiveUnknown.isSelected() ? 1 : 0,
                    motiveOther.isSelected() ? 1 : 0,

                    actionRequestArea.getText().trim()
            );

        });

        // Clear button logic
        clearBtn.setOnAction(e -> {
            // Clear common fields by calling parent method
            nameField.clear();
            fatherNameField.clear();
            motherNameField.clear();
            complainantPhoneField.clear();
            locationField.clear();
            datePicker.setValue(null);
            timeField.clear();
            descriptionArea.clear();
            nidBcField.clear();

            // Clear kidnapping-specific fields
            ageField.clear();
            genderGroup.selectToggle(null);
            heightField.clear();
            clothingArea.clear();
            marksArea.clear();
            lastLocationField.clear();
            lastSeenTimeField.clear();
            lastActivityArea.clear();
            kidnapperKnownGroup.selectToggle(null);
            kidnapperDescArea.clear();
            relationshipBox.setValue(null);
            witnessGroup.selectToggle(null);
            witnessDetailsArea.clear();
            witnessDetailsArea.setDisable(true);
            amberGroup.selectToggle(null);
            urgencyBox.setValue(null);
            ransomGroup.selectToggle(null);
            ransomDetailsArea.clear();
            ransomDetailsArea.setDisable(true);
            ransomAmountField.clear();
            ransomAmountField.setDisable(true);
            previousGroup.selectToggle(null);
            motiveMoney.setSelected(false);
            motiveRevenge.setSelected(false);
            motiveFamily.setSelected(false);
            motivePolitical.setSelected(false);
            motiveUnknown.setSelected(false);
            motiveOther.setSelected(false);
            actionRequestArea.clear();
        });

        // Add header and form to main container
        mainContainer.getChildren().addAll(headerSection, formContainer);

        // Wrap the main container in ScrollPane
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #f8f9fa;");

        Scene scene = new Scene(scrollPane, 900, 750);
        Stage stage = new Stage();
        stage.setTitle("Kidnapping Case Report – Bangladesh Police");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #8b0000, #a52a2a);" +
                        "-fx-border-color: #660000;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Main title with emoji
        Label titleLabel = new Label("🚨 KIDNAPPING CASE REPORT 🚨");
        titleLabel.setStyle(
                "-fx-font-family: 'System Bold', Arial;" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 3, 0, 2, 2);"
        );

        Label subtitleLabel = new Label("🏛️ Bangladesh Police Emergency Response System");
        subtitleLabel.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #ffeeee;" +
                        "-fx-font-weight: bold;"
        );

        // Info section with better styling
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        infoBox.setStyle(
                "-fx-background-color: rgba(165, 42, 42, 0.7);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("⚠️ URGENT - Kidnapping Case Information:");
        infoTitle.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ffff99;"
        );

        Label infoText = new Label(
                "🆘 IMMEDIATE RESPONSE: Report kidnapping cases IMMEDIATELY\n" +
                        "📞 Emergency Hotline: 999 | 🚔 Rapid Action Battalion: 999\n" +
                        "⏰ First 24 hours are CRITICAL for victim recovery\n" +
                        "📸 Preserve all evidence, photos, messages, and witness information\n" +
                        "🚨 This report triggers immediate police response and investigation"
        );
        infoText.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #ffe6e6;" +
                        "-fx-line-spacing: 4px;"
        );

        infoBox.getChildren().addAll(infoTitle, infoText);
        header.getChildren().addAll(titleLabel, subtitleLabel, infoBox);

        return header;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #8b0000;" +
                        "-fx-padding: 10px 0 5px 0;" +
                        "-fx-border-color: #8b0000;" +
                        "-fx-border-width: 0 0 2px 0;"
        );
        return label;
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
        combo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                combo.setStyle(combo.getStyle() + "-fx-border-color: #2196f3; -fx-border-width: 2px;");
            } else {
                combo.setStyle(combo.getStyle().replace("-fx-border-color: #2196f3; -fx-border-width: 2px;", ""));
            }
        });
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
                    "-fx-background-color: linear-gradient(to bottom, #dc3545, #c82333);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #dc3545;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #c82333, #bd2130);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #c82333;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #dc3545, #c82333);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #dc3545;"
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
    // Example for gender
    private String getSelectedGender() {
        RadioButton selected = (RadioButton) genderGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

    // Similarly for kidnapper known
    private String getKidnapperKnownValue() {
        RadioButton selected = (RadioButton) kidnapperKnownGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

    // For witness availability
    private String getWitnessAvailableValue() {
        RadioButton selected = (RadioButton) witnessGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

    // For amber alert
    private String getAmberAlertValue() {
        RadioButton selected = (RadioButton) amberGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

    // For ransom demand
    private String getRansomDemandValue() {
        RadioButton selected = (RadioButton) ransomGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

    // For previous report
    private String getPreviousReportValue() {
        RadioButton selected = (RadioButton) previousGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "";
    }

}
