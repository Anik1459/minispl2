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

public class Fraud extends Crime {

    public Fraud(){
        DatabaseHelper.createFraudTableIfNoExists();
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

        // 1. Type of Fraud
        Label typeLabel = createStyledLabel("💰 Type of Fraud *:", true);
        ComboBox<String> fraudTypeBox = createStyledComboBox();
        fraudTypeBox.getItems().addAll(
                "Financial fraud",
                "Land/Property fraud",
                "Job scam",
                "Online scam",
                "Bank/Cheque fraud",
                "Identity fraud",
                "Other"
        );
        fraudTypeBox.setPromptText("Select fraud type");

        TextField otherFraudField = createStyledTextField();
        otherFraudField.setPromptText("If 'Other', specify here");
        otherFraudField.setDisable(true);

        fraudTypeBox.setOnAction(e -> {
            String selected = fraudTypeBox.getValue();
            otherFraudField.setDisable(selected == null || !selected.equals("Other"));
        });

        formGrid.add(typeLabel, 0, row);
        formGrid.add(fraudTypeBox, 1, row++);
        formGrid.add(otherFraudField, 1, row++);

        // 2. Mode of Communication
        Label commLabel = createStyledLabel("📞 Mode of Communication *:", true);
        CheckBox phoneCB = createStyledCheckBox("Phone Call");
        CheckBox smsCB = createStyledCheckBox("SMS/Message");
        CheckBox socialCB = createStyledCheckBox("WhatsApp/Facebook");
        CheckBox emailCB = createStyledCheckBox("Email");
        CheckBox meetCB = createStyledCheckBox("In-person Meeting");
        CheckBox otherCommCB = createStyledCheckBox("Other");

        GridPane commPane = new GridPane();
        commPane.setHgap(15);
        commPane.setVgap(8);
        commPane.addRow(0, phoneCB, smsCB, socialCB);
        commPane.addRow(1, emailCB, meetCB, otherCommCB);

        formGrid.add(commLabel, 0, row);
        formGrid.add(commPane, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 3. Financial Transaction
        Label transLabel = createStyledLabel("💳 Was a financial transaction involved?", false);
        ToggleGroup transGroup = new ToggleGroup();
        RadioButton yesTrans = createStyledRadioButton("Yes");
        RadioButton noTrans = createStyledRadioButton("No");
        yesTrans.setToggleGroup(transGroup);
        noTrans.setToggleGroup(transGroup);

        HBox transBox = new HBox(20, yesTrans, noTrans);
        formGrid.add(transLabel, 0, row);
        formGrid.add(transBox, 1, row++);

        Label amountLabel = createStyledLabel("💵 Transaction Amount:", false);
        TextField amountField = createStyledTextField();
        amountField.setPromptText("Amount in BDT");
        amountField.setDisable(true);

        Label methodLabel = createStyledLabel("🏦 Transaction Method:", false);
        ComboBox<String> methodBox = createStyledComboBox();
        methodBox.getItems().addAll("Cash", "bKash", "Bank Transfer");
        methodBox.setDisable(true);

        yesTrans.setOnAction(e -> {
            amountField.setDisable(false);
            methodBox.setDisable(false);
        });
        noTrans.setOnAction(e -> {
            amountField.setDisable(true);
            methodBox.setDisable(true);
        });

        formGrid.add(amountLabel, 0, row);
        formGrid.add(amountField, 1, row++);
        formGrid.add(methodLabel, 0, row);
        formGrid.add(methodBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 4. Supporting Documents
        Label docLabel = createStyledLabel("📋 Supporting Documents:", false);
        CheckBox doc1 = createStyledCheckBox("Contract/Agreement");
        CheckBox doc2 = createStyledCheckBox("Receipt/Bill");
        CheckBox doc3 = createStyledCheckBox("Bank Statement");
        CheckBox doc4 = createStyledCheckBox("Cheque/Promissory Note");
        CheckBox doc5 = createStyledCheckBox("Chat/Screenshot");
        CheckBox doc6 = createStyledCheckBox("Video/Photo");
        CheckBox doc7 = createStyledCheckBox("Witness Statement");

        GridPane docPane = new GridPane();
        docPane.setHgap(15);
        docPane.setVgap(8);
        docPane.addRow(0, doc1, doc2, doc3);
        docPane.addRow(1, doc4, doc5);
        docPane.addRow(2, doc6, doc7);

        formGrid.add(docLabel, 0, row);
        formGrid.add(docPane, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 5. Accused promise
        Label promiseLabel = createStyledLabel("🤝 Has the accused promised to return?", false);
        ToggleGroup promiseGroup = new ToggleGroup();
        RadioButton yesPromise = createStyledRadioButton("Yes");
        RadioButton noPromise = createStyledRadioButton("No");
        yesPromise.setToggleGroup(promiseGroup);
        noPromise.setToggleGroup(promiseGroup);

        HBox promiseBox = new HBox(20, yesPromise, noPromise);
        formGrid.add(promiseLabel, 0, row);
        formGrid.add(promiseBox, 1, row++);

        // 6. Previously reported
        Label reportedLabel = createStyledLabel("📝 Has this fraud been reported elsewhere?", false);
        ToggleGroup reportedGroup = new ToggleGroup();
        RadioButton yesReported = createStyledRadioButton("Yes, reported earlier");
        RadioButton noReported = createStyledRadioButton("No, first time");
        yesReported.setToggleGroup(reportedGroup);
        noReported.setToggleGroup(reportedGroup);

        HBox reportedBox = new HBox(20, yesReported, noReported);
        formGrid.add(reportedLabel, 0, row);
        formGrid.add(reportedBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 7. Action from police
        Label actionLabel = createStyledLabel("🚔 What action are you seeking from police?", false);
        TextArea actionArea = createStyledTextArea();
        actionArea.setPromptText("Describe the action you want from the police...");
        actionArea.setPrefRowCount(4);

        formGrid.add(actionLabel, 0, row);
        formGrid.add(actionArea, 1, row++);

        // Add spacing before buttons
        formGrid.add(new Label(""), 0, row++);

        // Buttons
        Button submitBtn = createStyledButton("📤 Submit Report", true);
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
            if (fraudTypeBox.getValue() == null ||
                    (!phoneCB.isSelected() && !smsCB.isSelected() && !socialCB.isSelected() && !emailCB.isSelected() && !meetCB.isSelected() && !otherCommCB.isSelected())) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all mandatory fields (Type of Fraud and Mode of Communication).");
                return;
            }
            if (fraudTypeBox.getValue().equals("Other") && otherFraudField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify the 'Other' fraud type.");
                return;
            }
            if ("Other".equals(fraudTypeBox.getValue()) && otherFraudField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please specify the 'Other' fraud type.");
                return;
            }
            String typeOfFraud       = fraudTypeBox.getValue().equals("Other")
                    ? otherFraudField.getText().trim()
                    : fraudTypeBox.getValue();
            String modeOfComm        = "";
            if (phoneCB.isSelected())  modeOfComm += "Phone,";
            if (smsCB.isSelected())    modeOfComm += "SMS,";
            if (socialCB.isSelected()) modeOfComm += "WhatsApp,";
            if (emailCB.isSelected())  modeOfComm += "Email,";
            if (meetCB.isSelected())   modeOfComm += "In-person,";
            if (otherCommCB.isSelected()) modeOfComm += otherCommCB.getText() + ",";
            // trim trailing comma
            if (!modeOfComm.isEmpty()) modeOfComm = modeOfComm.substring(0, modeOfComm.length()-1);

            String transactionAmount  = yesTrans.isSelected() ? amountField.getText().trim() : "";
            String transactionMethod  = yesTrans.isSelected() && methodBox.getValue() != null
                    ? methodBox.getValue()
                    : "";

            String documents = "";
            if (doc1.isSelected()) documents += "Contract,";
            if (doc2.isSelected()) documents += "Receipt,";
            if (doc3.isSelected()) documents += "Statement,";
            if (doc4.isSelected()) documents += "Cheque,";
            if (doc5.isSelected()) documents += "Chat,";
            if (doc6.isSelected()) documents += "Photo,";
            if (doc7.isSelected()) documents += "Witness,";
            if (!documents.isEmpty()) documents = documents.substring(0, documents.length()-1);

            String accusedPromise      = yesPromise.isSelected() ? "Yes" : "No";
            String reportedElsewhere   = yesReported.isSelected() ? "Yes" : "No";
            String actionRequested     = actionArea.getText().trim();

            // 3) Call the helper
            boolean ok = DatabaseHelper.insertFraudReport(
                    nameField.getText().trim(),         // complainantName
                    fatherNameField.getText().trim(),   // fatherName
                    motherNameField.getText().trim(),   // motherName
                    complainantPhoneField.getText().trim(), // complaintPhone
                    nidBcField.getText().trim(),        // nidBc
                    locationField.getText().trim(),     // location
                    datePicker.getValue() != null ? datePicker.getValue().toString() : "",   // dateOfIncident
                    descriptionArea.getText().trim(),   // descriptionOfIncident
                    accusedName.getText().trim(),       // accusedName
                    accusedPhone.getText().trim(),      // accusedPhone
                    accusedEmail.getText().trim(),      // accusedEmail
                    accusedAddress.getText().trim(),    // accusedAddress
                    typeOfFraud,                        // typeOfFraud
                    modeOfComm,                         // modeOfCommunication
                    transactionAmount,                  // transactionAmount
                    transactionMethod,                  // transactionMethod
                    documents,                          // supportingDocuments
                    accusedPromise,                     // hasAccusedPromisedToReturn
                    actionRequested                     // actionRequestedFromPolice
            );

            // 4) Feedback to user
            if (ok) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Fraud report submitted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save your report. Please try again.");
            }

        });

        // Clear button logic
        clearBtn.setOnAction(e -> {
            fraudTypeBox.setValue(null);
            otherFraudField.clear();
            phoneCB.setSelected(false);
            smsCB.setSelected(false);
            socialCB.setSelected(false);
            emailCB.setSelected(false);
            meetCB.setSelected(false);
            otherCommCB.setSelected(false);
            transGroup.selectToggle(null);
            amountField.clear();
            amountField.setDisable(true);
            methodBox.setValue(null);
            methodBox.setDisable(true);
            doc1.setSelected(false);
            doc2.setSelected(false);
            doc3.setSelected(false);
            doc4.setSelected(false);
            doc5.setSelected(false);
            doc6.setSelected(false);
            doc7.setSelected(false);
            promiseGroup.selectToggle(null);
            reportedGroup.selectToggle(null);
            actionArea.clear();
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
        stage.setTitle("Fraud Case Report - Bangladesh Police");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);" +
                        "-fx-border-color: #1a252f;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Main title with emoji
        Label titleLabel = new Label("🚨 FRAUD CASE REPORT 🚨");
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
                        "-fx-text-fill: #ecf0f1;" +
                        "-fx-font-weight: bold;"
        );

        // Info section with better styling
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        infoBox.setStyle(
                "-fx-background-color: rgba(52, 73, 94, 0.7);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("⚠️ Important Information about Fraud Cases:");
        infoTitle.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f39c12;"
        );

        Label infoText = new Label(
                "📅 Report fraud cases within 24-48 hours for better investigation\n" +
                        "📄 Keep all evidence and documents safe\n" +
                        "📞 Provide accurate contact information for follow-up\n" +
                        "🚨 Emergency Fraud Hotline: 999 | 💬 Anti-Fraud Helpline: 16263"
        );
        infoText.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #bdc3c7;" +
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
                    "-fx-background-color: linear-gradient(to bottom, #4caf50, #45a049);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #4caf50;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #45a049, #3d8b40);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #45a049;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #4caf50, #45a049);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #4caf50;"
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
