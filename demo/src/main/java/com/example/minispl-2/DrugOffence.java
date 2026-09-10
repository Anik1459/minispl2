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
import javafx.stage.Window;

import java.io.File;

public class DrugOffence extends Crime {

    // Add constructor to create table
    public DrugOffence() {
        DatabaseHelper.createDrugOffenseTableIfNotExists();
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

        // 1. Type of drug involved
        Label drugTypeLabel = createStyledLabel("💊 Type of Drug Involved *:", true);
        ComboBox<String> drugTypeBox = createStyledComboBox();
        drugTypeBox.getItems().addAll(
                "Cannabis/Marijuana",
                "Heroin",
                "Yaba (Methamphetamine pills)",
                "Cocaine",
                "Methamphetamine (Ice/Crystal)",
                "Phensedyl/Codeine syrup",
                "Opium",
                "Ecstasy/MDMA",
                "LSD",
                "Prescription drugs (misused)",
                "Other"
        );
        drugTypeBox.setPromptText("Select drug type");

        TextField otherDrugField = createStyledTextField();
        otherDrugField.setPromptText("If 'Other', specify the drug name");
        otherDrugField.setDisable(true);
        drugTypeBox.setOnAction(e -> {
            String sel = drugTypeBox.getValue();
            otherDrugField.setDisable(sel == null || !"Other".equals(sel));
        });

        formGrid.add(drugTypeLabel, 0, row);
        formGrid.add(drugTypeBox, 1, row++);
        formGrid.add(otherDrugField, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 2. Quantity and packaging
        Label qtyLabel = createStyledLabel("⚖️ Quantity and Packaging Details *:", true);
        formGrid.add(qtyLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label qtyAmountLabel = createStyledLabel("📏 Approximate quantity:", false);
        TextField qtyField = createStyledTextField();
        qtyField.setPromptText("e.g., 500 gm, 20 tablets, 3 vials, 2 packets");
        formGrid.add(qtyAmountLabel, 0, row);
        formGrid.add(qtyField, 1, row++);

        Label packagingLabel = createStyledLabel("📦 How was it packaged?", false);
        ComboBox<String> packagingBox = createStyledComboBox();
        packagingBox.getItems().addAll(
                "Small plastic packets/pouches",
                "Bottles/vials",
                "Aluminum foil wraps",
                "Cigarette packets",
                "Loose/unwrapped",
                "Professional pharmaceutical packaging",
                "Other"
        );
        packagingBox.setPromptText("Select packaging type");
        formGrid.add(packagingLabel, 0, row);
        formGrid.add(packagingBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 3. Nature of the incident
        Label incidentLabel = createStyledLabel("🚨 Nature of Drug Incident *:", true);
        formGrid.add(incidentLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label incidentTypeLabel = createStyledLabel("📋 What type of incident?", false);
        CheckBox possession = createStyledCheckBox("Drug possession");
        CheckBox selling = createStyledCheckBox("Drug selling/trafficking");
        CheckBox consumption = createStyledCheckBox("Drug consumption");
        CheckBox manufacturing = createStyledCheckBox("Drug manufacturing");
        CheckBox smuggling = createStyledCheckBox("Drug smuggling");
        CheckBox distribution = createStyledCheckBox("Drug distribution");

        GridPane incidentPane = new GridPane();
        incidentPane.setHgap(15);
        incidentPane.setVgap(8);
        incidentPane.addRow(0, possession, selling, consumption);
        incidentPane.addRow(1, manufacturing, smuggling, distribution);

        formGrid.add(incidentTypeLabel, 0, row);
        formGrid.add(incidentPane, 1, row++);

        // 4. Location details
        Label whereLabel = createStyledLabel("📍 Incident Location Details *:", true);
        ComboBox<String> whereBox = createStyledComboBox();
        whereBox.getItems().addAll(
                "Street/Road",
                "House/Residence",
                "Vehicle (car/bus/rickshaw)",
                "Public place/Park",
                "Educational institution",
                "Workplace/Factory",
                "Hotel/Restaurant",
                "Transport terminal",
                "Border area",
                "Other"
        );
        whereBox.setPromptText("Select location type");

        TextField whereDetailsField = createStyledTextField();
        whereDetailsField.setPromptText("Exact address and specific location details");
        whereDetailsField.setDisable(true);
        whereBox.setOnAction(e -> {
            String sel = whereBox.getValue();
            whereDetailsField.setDisable(sel == null);
        });

        formGrid.add(whereLabel, 0, row);
        formGrid.add(whereBox, 1, row++);
        formGrid.add(whereDetailsField, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 5. Discovery method
        Label discoverLabel = createStyledLabel("🔍 How was the Drug Discovered? *:", true);
        ComboBox<String> discoverBox = createStyledComboBox();
        discoverBox.getItems().addAll(
                "Police raid",
                "Random security check",
                "Tip-off from informant",
                "Suspicious behavior noticed",
                "Public complaint",
                "Routine patrol",
                "Intelligence operation",
                "Border control",
                "Caught in the act",
                "Other"
        );
        discoverBox.setPromptText("Select discovery method");

        TextField discoverOtherField = createStyledTextField();
        discoverOtherField.setPromptText("If 'Other', provide details");
        discoverOtherField.setDisable(true);
        discoverBox.setOnAction(e -> {
            String sel = discoverBox.getValue();
            discoverOtherField.setDisable(sel == null || !"Other".equals(sel));
        });

        formGrid.add(discoverLabel, 0, row);
        formGrid.add(discoverBox, 1, row++);
        formGrid.add(discoverOtherField, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 6. Persons involved
        Label personsLabel = createStyledLabel("👥 Persons Involved:", false);
        formGrid.add(personsLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label personsCountLabel = createStyledLabel("🔢 Number of persons involved:", false);
        ComboBox<String> personsCountBox = createStyledComboBox();
        personsCountBox.getItems().addAll("1", "2", "3-5", "6-10", "More than 10", "Unknown");
        personsCountBox.setPromptText("Select count");
        formGrid.add(personsCountLabel, 0, row);
        formGrid.add(personsCountBox, 1, row++);

        Label ageGroupLabel = createStyledLabel("👶 Age group of persons:", false);
        CheckBox minors = createStyledCheckBox("Minors (under 18)");
        CheckBox youth = createStyledCheckBox("Youth (18-25)");
        CheckBox adults = createStyledCheckBox("Adults (26-50)");
        CheckBox elderly = createStyledCheckBox("Elderly (over 50)");

        HBox ageBox = new HBox(15, minors, youth, adults, elderly);
        formGrid.add(ageGroupLabel, 0, row);
        formGrid.add(ageBox, 1, row++);

        Label genderLabel = createStyledLabel("👨‍👩‍👧‍👦 Gender of persons involved:", false);
        CheckBox male = createStyledCheckBox("Male");
        CheckBox female = createStyledCheckBox("Female");
        CheckBox transgender = createStyledCheckBox("Transgender");

        HBox genderBox = new HBox(15, male, female, transgender);
        formGrid.add(genderLabel, 0, row);
        formGrid.add(genderBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 7. Drug activity details
        Label activityLabel = createStyledLabel("💰 Drug Activity Details:", false);
        formGrid.add(activityLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label traffickLabel = createStyledLabel("🚛 Drug trafficking/selling observed? *", false);
        ComboBox<String> traffickBox = createStyledComboBox();
        traffickBox.getItems().addAll("Yes", "No", "Not Sure");
        traffickBox.setPromptText("Select");
        formGrid.add(traffickLabel, 0, row);
        formGrid.add(traffickBox, 1, row++);

        Label exchangeLabel = createStyledLabel("💵 Money/items exchange witnessed? *", false);
        ComboBox<String> exchangeBox = createStyledComboBox();
        exchangeBox.getItems().addAll("Yes", "No", "Not Sure");
        exchangeBox.setPromptText("Select");
        formGrid.add(exchangeLabel, 0, row);
        formGrid.add(exchangeBox, 1, row++);

        Label priceLabel = createStyledLabel("💸 Approximate transaction amount:", false);
        TextField priceField = createStyledTextField();
        priceField.setPromptText("Amount in BDT (if observed)");
        formGrid.add(priceLabel, 0, row);
        formGrid.add(priceField, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 8. Evidence and items
        Label itemsLabel = createStyledLabel("🔍 Evidence and Items Found:", false);
        formGrid.add(itemsLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label recoveredLabel = createStyledLabel("📋 Drug-related items recovered:", false);
        CheckBox packets = createStyledCheckBox("Drug packets/containers");
        CheckBox syringes = createStyledCheckBox("Syringes/needles");
        CheckBox pipes = createStyledCheckBox("Smoking pipes/bongs");
        CheckBox scales = createStyledCheckBox("Weighing scales");
        CheckBox foil = createStyledCheckBox("Aluminum foil/wraps");
        CheckBox cash = createStyledCheckBox("Large amounts of cash");
        CheckBox chemicals = createStyledCheckBox("Manufacturing chemicals");
        CheckBox equipment = createStyledCheckBox("Manufacturing equipment");

        GridPane itemsPane = new GridPane();
        itemsPane.setHgap(15);
        itemsPane.setVgap(8);
        itemsPane.addRow(0, packets, syringes, pipes);
        itemsPane.addRow(1, scales, foil, cash);
        itemsPane.addRow(2, chemicals, equipment);

        formGrid.add(recoveredLabel, 0, row);
        formGrid.add(itemsPane, 1, row++);

        Label weaponsLabel = createStyledLabel("⚔️ Weapons or dangerous items found:", false);
        ComboBox<String> weaponsBox = createStyledComboBox();
        weaponsBox.getItems().addAll(
                "None",
                "Knife/Sharp objects",
                "Firearms/Guns",
                "Homemade weapons",
                "Explosives",
                "Acid/Chemicals",
                "Other dangerous items"
        );
        weaponsBox.setPromptText("Select weapon type");
        formGrid.add(weaponsLabel, 0, row);
        formGrid.add(weaponsBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 9. Suspicious activities
        Label suspiciousLabel = createStyledLabel("👀 Suspicious Activities Observed:", false);
        formGrid.add(suspiciousLabel, 0, row);
        formGrid.add(new Label(""), 1, row++);

        Label vehicleLabel = createStyledLabel("🚗 Suspicious vehicles or persons:", false);
        TextArea vehicleArea = createStyledTextArea();
        vehicleArea.setPromptText("Vehicle descriptions, license plates, person descriptions, suspicious behavior...");
        vehicleArea.setPrefRowCount(3);
        formGrid.add(vehicleLabel, 0, row);
        formGrid.add(vehicleArea, 1, row++);

        Label priorLabel = createStyledLabel("🔄 Previous involvement in drugs? *", false);
        ComboBox<String> priorBox = createStyledComboBox();
        priorBox.getItems().addAll("Yes", "No", "Not Sure", "First time witnessed");
        priorBox.setPromptText("Select");
        formGrid.add(priorLabel, 0, row);
        formGrid.add(priorBox, 1, row++);

        Label threatLabel = createStyledLabel("⚠️ Threats or evidence destruction attempts:", false);
        ComboBox<String> threatBox = createStyledComboBox();
        threatBox.getItems().addAll(
                "None observed",
                "Verbal threats made",
                "Attempt to flush drugs",
                "Attempt to burn/destroy evidence",
                "Evidence already destroyed",
                "Intimidation of witnesses",
                "Other suspicious behavior"
        );
        threatBox.setPromptText("Select");
        formGrid.add(threatLabel, 0, row);
        formGrid.add(threatBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 10. Witness information
        Label witnessLabel = createStyledLabel("👁️ Witness Information *:", true);
        ComboBox<String> witnessBox = createStyledComboBox();
        witnessBox.getItems().addAll("Yes", "No");
        witnessBox.setPromptText("Were there any witnesses?");

        TextField witnessNameField = createStyledTextField();
        witnessNameField.setPromptText("Witness full name");
        TextField witnessPhoneField = createStyledTextField();
        witnessPhoneField.setPromptText("Witness phone number");
        TextField witnessRelationField = createStyledTextField();
        witnessRelationField.setPromptText("Relationship to you");

        witnessNameField.setDisable(true);
        witnessPhoneField.setDisable(true);
        witnessRelationField.setDisable(true);

        witnessBox.setOnAction(e -> {
            String sel = witnessBox.getValue();
            boolean enable = "Yes".equals(sel);
            witnessNameField.setDisable(!enable);
            witnessPhoneField.setDisable(!enable);
            witnessRelationField.setDisable(!enable);
            if (!enable) {
                witnessNameField.clear();
                witnessPhoneField.clear();
                witnessRelationField.clear();
            }
        });

        formGrid.add(witnessLabel, 0, row);
        formGrid.add(witnessBox, 1, row++);
        formGrid.add(witnessNameField, 1, row++);
        formGrid.add(witnessPhoneField, 1, row++);
        formGrid.add(witnessRelationField, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 11. Evidence files
        Label evidenceLabel = createStyledLabel("📹 Digital Evidence Available? *:", true);
        ComboBox<String> evidenceBox = createStyledComboBox();
        evidenceBox.getItems().addAll("Yes", "No");
        evidenceBox.setPromptText("Do you have photos/videos?");

        Button uploadVideoBtn = createStyledButton("📹 Upload Video", false);
        Button uploadPhotoBtn = createStyledButton("📷 Upload Photos", false);
        uploadVideoBtn.setDisable(true);
        uploadPhotoBtn.setDisable(true);

        final FileChooser videoChooser = new FileChooser();
        videoChooser.setTitle("Select Video Evidence");
        videoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mov", "*.avi", "*.mkv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        final FileChooser photoChooser = new FileChooser();
        photoChooser.setTitle("Select Photo Evidence");
        photoChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        final File[] videoFileHolder = new File[1];
        final File[] photoFileHolder = new File[1];

        evidenceBox.setOnAction(e -> {
            boolean enable = "Yes".equals(evidenceBox.getValue());
            uploadVideoBtn.setDisable(!enable);
            uploadPhotoBtn.setDisable(!enable);
            if (!enable) {
                uploadVideoBtn.setText("📹 Upload Video");
                uploadPhotoBtn.setText("📷 Upload Photos");
                videoFileHolder[0] = null;
                photoFileHolder[0] = null;
            }
        });

        uploadVideoBtn.setOnAction(e -> {
            Window w = formGrid.getScene().getWindow();
            File f = videoChooser.showOpenDialog(w);
            if (f != null) {
                videoFileHolder[0] = f;
                uploadVideoBtn.setText("✅ Video: " + f.getName());
            }
        });

        uploadPhotoBtn.setOnAction(e -> {
            Window w = formGrid.getScene().getWindow();
            File f = photoChooser.showOpenDialog(w);
            if (f != null) {
                photoFileHolder[0] = f;
                uploadPhotoBtn.setText("✅ Photo: " + f.getName());
            }
        });

        formGrid.add(evidenceLabel, 0, row);
        formGrid.add(evidenceBox, 1, row++);
        HBox evidenceBtnBox = new HBox(10, uploadVideoBtn, uploadPhotoBtn);
        formGrid.add(evidenceBtnBox, 1, row++);

        // Add spacing before submit button
        formGrid.add(new Label(""), 0, row++);
        formGrid.add(new Label(""), 0, row++);

        // Submit Button - UPDATED WITH CORRECTED DATABASE STORAGE
        Button submitBtn = createStyledButton("🚨 Submit Drug Offence Report", true);
        submitBtn.setPrefWidth(300);
        submitBtn.setOnAction(e -> {
            if (validateForm(drugTypeBox, qtyField, whereBox, discoverBox, traffickBox, exchangeBox,
                    witnessBox, evidenceBox, witnessNameField, witnessPhoneField,
                    videoFileHolder, photoFileHolder, otherDrugField, discoverOtherField)) {

                // Collect all form data
                String incidentTypes = getSelectedCheckboxes(possession, selling, consumption, manufacturing, smuggling, distribution);
                String ageGroups = getSelectedCheckboxes(minors, youth, adults, elderly);
                String genders = getSelectedCheckboxes(male, female, transgender);
                String recoveredItems = getSelectedCheckboxes(packets, syringes, pipes, scales, foil, cash, chemicals, equipment);

                // Combine evidence file paths
                String evidenceFilePath = "";
                if (videoFileHolder[0] != null && photoFileHolder[0] != null) {
                    evidenceFilePath = "Video: " + videoFileHolder[0].getAbsolutePath() + " | Photo: " + photoFileHolder[0].getAbsolutePath();
                } else if (videoFileHolder[0] != null) {
                    evidenceFilePath = videoFileHolder[0].getAbsolutePath();
                } else if (photoFileHolder[0] != null) {
                    evidenceFilePath = photoFileHolder[0].getAbsolutePath();
                }

                // Store in database with corrected parameter count
                boolean success = DatabaseHelper.insertDrugOffenseReport(
                        // Common fields (10 parameters)
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

                        // Accused information (4 parameters)
                        accusedName.getText().trim(),
                        accusedPhone.getText().trim(),
                        accusedEmail.getText().trim(),
                        accusedAddress.getText().trim(),

                        // Drug information (4 parameters)
                        drugTypeBox.getValue() != null ? drugTypeBox.getValue() : "",
                        otherDrugField.getText().trim(),
                        qtyField.getText().trim(),
                        packagingBox.getValue() != null ? packagingBox.getValue() : "",

                        // Incident details (5 parameters)
                        incidentTypes,
                        whereBox.getValue() != null ? whereBox.getValue() : "",
                        whereDetailsField.getText().trim(),
                        discoverBox.getValue() != null ? discoverBox.getValue() : "",
                        discoverOtherField.getText().trim(),

                        // Persons involved (3 parameters)
                        personsCountBox.getValue() != null ? personsCountBox.getValue() : "",
                        ageGroups,
                        genders,

                        // Drug activity (3 parameters)
                        traffickBox.getValue() != null ? traffickBox.getValue() : "",
                        exchangeBox.getValue() != null ? exchangeBox.getValue() : "",
                        priceField.getText().trim(),

                        // Evidence and items (2 parameters)
                        recoveredItems,
                        weaponsBox.getValue() != null ? weaponsBox.getValue() : "",

                        // Suspicious activities (3 parameters)
                        vehicleArea.getText().trim(),
                        priorBox.getValue() != null ? priorBox.getValue() : "",
                        threatBox.getValue() != null ? threatBox.getValue() : "",

                        // Witness information (4 parameters)
                        witnessBox.getValue() != null ? witnessBox.getValue() : "",
                        witnessNameField.getText().trim(),
                        witnessPhoneField.getText().trim(),
                        witnessRelationField.getText().trim(),

                        // Digital evidence (2 parameters)
                        evidenceBox.getValue() != null ? evidenceBox.getValue() : "",
                        evidenceFilePath // Combined evidence file path
                );

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Report Submitted",
                            "✅ Your drug offence report has been successfully submitted to Bangladesh Police.\n\n" +
                                    "📋 Case Reference: DRUG" + System.currentTimeMillis() + "\n" +
                                    "📞 You will be contacted within 24 hours for follow-up.\n\n" +
                                    "🚨 Anti-Narcotics Hotline: 999 | 📱 Drug Abuse Helpline: 09611677777");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error",
                            "❌ There was an error saving your report. Please try again or contact support.");
                }
            }
        });

        HBox submitBox = new HBox(submitBtn);
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
        stage.setTitle("Drug Offence Case Report – Bangladesh Police");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b);" +
                        "-fx-border-color: #a93226;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Main title with emoji
        Label titleLabel = new Label("💊 DRUG OFFENCE CASE REPORT 💊");
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
                        "-fx-text-fill: #fadbd8;" +
                        "-fx-font-weight: bold;"
        );

        // Info section with better styling
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        infoBox.setStyle(
                "-fx-background-color: rgba(192, 57, 43, 0.7);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("⚠️ Important Information about Drug Offences:");
        infoTitle.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #fff3cd;"
        );

        Label infoText = new Label(
                "🚨 Report drug crimes immediately - protect your community\n" +
                        "📱 Document evidence safely but do not interfere with ongoing activity\n" +
                        "🚫 Never confront suspects directly - ensure your safety first\n" +
                        "📞 Emergency: 999 | 🚨 Anti-Narcotics: 16263 | 📱 Drug Abuse Helpline: 09611677777"
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

    private boolean validateForm(ComboBox<String> drugTypeBox, TextField qtyField, ComboBox<String> whereBox,
                                 ComboBox<String> discoverBox, ComboBox<String> traffickBox, ComboBox<String> exchangeBox,
                                 ComboBox<String> witnessBox, ComboBox<String> evidenceBox, TextField witnessNameField,
                                 TextField witnessPhoneField, File[] videoFileHolder, File[] photoFileHolder,
                                 TextField otherDrugField, TextField discoverOtherField) {

        // Validate common Crime fields
        if (!validateCommonFields()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "❌ Please fill in all required common fields:\n" +
                            "• Your name\n• Phone number\n• NID/BC number\n• Location\n• Date and time\n• Description\n• Father's and Mother's name");
            return false;
        }

        // Validate drug-specific required fields
        if (drugTypeBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please select the type of drug involved.");
            return false;
        }

        if ("Other".equals(drugTypeBox.getValue()) && otherDrugField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please specify the drug type.");
            return false;
        }

        if (qtyField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please provide the quantity of drugs involved.");
            return false;
        }

        if (whereBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please select where the incident took place.");
            return false;
        }

        if (discoverBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please select how the drug was discovered.");
            return false;
        }

        if ("Other".equals(discoverBox.getValue()) && discoverOtherField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please specify how the drug was discovered.");
            return false;
        }

        if (traffickBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please indicate if trafficking/selling was observed.");
            return false;
        }

        if (exchangeBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please indicate if money/items exchange was witnessed.");
            return false;
        }

        if (witnessBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please indicate if there were any witnesses.");
            return false;
        }

        if ("Yes".equals(witnessBox.getValue())) {
            if (witnessNameField.getText().trim().isEmpty() || witnessPhoneField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please provide witness name and phone number.");
                return false;
            }
        }

        if (evidenceBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please indicate if you have digital evidence.");
            return false;
        }

        if ("Yes".equals(evidenceBox.getValue()) && videoFileHolder[0] == null && photoFileHolder[0] == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "❌ Please upload at least one evidence file (photo or video).");
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
                        "-fx-text-fill: " + (required ? "#e74c3c" : "#424242") + ";";
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
                        "-fx-border-color: #e74c3c;" +
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
                                "-fx-border-color: #c0392b;" +
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
                        "-fx-border-color: #e74c3c;" +
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
                                "-fx-border-color: #c0392b;" +
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
                        "-fx-border-color: #e74c3c;" +
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
                    "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #e74c3c;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #c0392b, #a93226);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #c0392b;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #e74c3c;"
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
