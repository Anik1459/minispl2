package org.example.java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class MoneyLaundering extends Crime {
    public MoneyLaundering() {
        // Ensure table exists when creating this object
        DatabaseHelper.createMoneyLaunderingTableIfNotExists();
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

        // 1. Financial Information Section
        Label financialSectionLabel = createSectionLabel("💰 FINANCIAL INFORMATION");
        formGrid.add(financialSectionLabel, 0, row++, 2, 1);

        // Total Amount Laundered
        Label amountLabel = createStyledLabel("💵 Total Amount Purportedly Laundered *:", true);
        TextField amountField = createStyledTextField();
        amountField.setPromptText("Amount in BDT (e.g., 10,000,000)");
        formGrid.add(amountLabel, 0, row);
        formGrid.add(amountField, 1, row++);

        // Currency Type
        Label currencyLabel = createStyledLabel("🌍 Currency Type:", false);
        ComboBox<String> currencyBox = createStyledComboBox();
        currencyBox.getItems().addAll(
                "BDT (Bangladeshi Taka)",
                "USD (US Dollar)",
                "EUR (Euro)",
                "GBP (British Pound)",
                "INR (Indian Rupee)",
                "Cryptocurrency",
                "Multiple Currencies",
                "Other"
        );
        currencyBox.setPromptText("Select currency type");
        formGrid.add(currencyLabel, 0, row);
        formGrid.add(currencyBox, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 2. Source of Funds Section
        Label sourceSectionLabel = createSectionLabel("🏦 SOURCE OF FUNDS");
        formGrid.add(sourceSectionLabel, 0, row++, 2, 1);

        Label sourceLabel = createStyledLabel("📈 Source(s) of Funds *:", true);
        CheckBox cashCB = createStyledCheckBox("Cash Deposits");
        CheckBox wireCB = createStyledCheckBox("Wire Transfers");
        CheckBox cryptoCB = createStyledCheckBox("Cryptocurrency");
        CheckBox checkCB = createStyledCheckBox("Check/Cheque");
        CheckBox businessCB = createStyledCheckBox("Business Revenue");
        CheckBox investmentCB = createStyledCheckBox("Investment Returns");
        CheckBox loanCB = createStyledCheckBox("Loan Proceeds");
        CheckBox otherSourceCB = createStyledCheckBox("Other Sources");

        GridPane sourcePane = new GridPane();
        sourcePane.setHgap(15);
        sourcePane.setVgap(8);
        sourcePane.addRow(0, cashCB, wireCB, cryptoCB, checkCB);
        sourcePane.addRow(1, businessCB, investmentCB, loanCB, otherSourceCB);

        formGrid.add(sourceLabel, 0, row);
        formGrid.add(sourcePane, 1, row++);

        // Other Source Details
        Label otherSourceLabel = createStyledLabel("📝 Other Source Details:", false);
        TextArea otherSourceArea = createStyledTextArea();
        otherSourceArea.setPromptText("If 'Other Sources' selected, provide details...");
        otherSourceArea.setPrefRowCount(3);
        otherSourceArea.setDisable(true);
        formGrid.add(otherSourceLabel, 0, row);
        formGrid.add(otherSourceArea, 1, row++);

        // Enable/disable other source details
        otherSourceCB.setOnAction(e -> otherSourceArea.setDisable(!otherSourceCB.isSelected()));

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 3. Account and Institution Information Section
        Label accountSectionLabel = createSectionLabel("🏛️ ACCOUNT & INSTITUTION INFORMATION");
        formGrid.add(accountSectionLabel, 0, row++, 2, 1);

        Label bankDetailsLabel = createStyledLabel("🏦 Bank/Account Numbers Involved:", false);
        TextArea bankDetailsArea = createStyledTextArea();
        bankDetailsArea.setPromptText("List bank names, account numbers, routing numbers...");
        bankDetailsArea.setPrefRowCount(4);
        formGrid.add(bankDetailsLabel, 0, row);
        formGrid.add(bankDetailsArea, 1, row++);

        Label walletLabel = createStyledLabel("₿ Cryptocurrency Wallet Addresses:", false);
        TextArea walletArea = createStyledTextArea();
        walletArea.setPromptText("Bitcoin, Ethereum, or other wallet addresses...");
        walletArea.setPrefRowCount(3);
        formGrid.add(walletLabel, 0, row);
        formGrid.add(walletArea, 1, row++);

        Label jurisdictionLabel = createStyledLabel("🌐 Geographic Jurisdictions Involved:", false);
        TextArea jurisdictionArea = createStyledTextArea();
        jurisdictionArea.setPromptText("Countries, states, or regions where transactions occurred...");
        jurisdictionArea.setPrefRowCount(3);
        formGrid.add(jurisdictionLabel, 0, row);
        formGrid.add(jurisdictionArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 4. Entities and Intermediaries Section
        Label entitiesSectionLabel = createSectionLabel("🏢 ENTITIES & INTERMEDIARIES");
        formGrid.add(entitiesSectionLabel, 0, row++, 2, 1);

        Label shellCompaniesLabel = createStyledLabel("🏭 Shell Companies or Intermediaries:", false);
        TextArea shellCompaniesArea = createStyledTextArea();
        shellCompaniesArea.setPromptText("Names of companies, LLCs, trusts, or other entities involved...");
        shellCompaniesArea.setPrefRowCount(4);
        formGrid.add(shellCompaniesLabel, 0, row);
        formGrid.add(shellCompaniesArea, 1, row++);

        Label beneficialOwnerLabel = createStyledLabel("👤 Beneficial Owner Information:", false);
        TextArea beneficialOwnerArea = createStyledTextArea();
        beneficialOwnerArea.setPromptText("Names and details of actual owners behind shell companies...");
        beneficialOwnerArea.setPrefRowCount(4);
        formGrid.add(beneficialOwnerLabel, 0, row);
        formGrid.add(beneficialOwnerArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 5. Transaction Pattern Section
        Label transactionSectionLabel = createSectionLabel("📊 TRANSACTION PATTERNS");
        formGrid.add(transactionSectionLabel, 0, row++, 2, 1);

        Label dateRangeLabel = createStyledLabel("📅 Date Range of Suspicious Activity:", false);
        HBox dateRangeBox = new HBox(10);
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        Label toLabel = new Label("to");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");
        dateRangeBox.getChildren().addAll(startDatePicker, toLabel, endDatePicker);
        formGrid.add(dateRangeLabel, 0, row);
        formGrid.add(dateRangeBox, 1, row++);

        Label frequencyLabel = createStyledLabel("🔄 Transaction Frequency:", false);
        ComboBox<String> frequencyBox = createStyledComboBox();
        frequencyBox.getItems().addAll(
                "Daily",
                "Weekly",
                "Monthly",
                "Quarterly",
                "Irregular/Sporadic",
                "One-time large transaction",
                "Other pattern"
        );
        frequencyBox.setPromptText("Select frequency pattern");
        formGrid.add(frequencyLabel, 0, row);
        formGrid.add(frequencyBox, 1, row++);

        Label transactionDetailsLabel = createStyledLabel("📈 Transaction Details:", false);
        TextArea transactionDetailsArea = createStyledTextArea();
        transactionDetailsArea.setPromptText("Describe transaction patterns, amounts, timing, methods...");
        transactionDetailsArea.setPrefRowCount(4);
        formGrid.add(transactionDetailsLabel, 0, row);
        formGrid.add(transactionDetailsArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 6. Evidence and Documentation Section
        Label evidenceSectionLabel = createSectionLabel("📋 EVIDENCE & DOCUMENTATION");
        formGrid.add(evidenceSectionLabel, 0, row++, 2, 1);

        Label evidenceLabel = createStyledLabel("📄 Available Evidence:", false);
        CheckBox receiptsEv = createStyledCheckBox("Transaction Receipts");
        CheckBox statementsEv = createStyledCheckBox("Bank Statements");
        CheckBox logsEv = createStyledCheckBox("Transaction Logs");
        CheckBox emailsEv = createStyledCheckBox("Email Communications");
        CheckBox contractsEv = createStyledCheckBox("Contracts/Agreements");
        CheckBox photosEv = createStyledCheckBox("Photos/Screenshots");
        CheckBox recordingsEv = createStyledCheckBox("Audio/Video Recordings");
        CheckBox otherEv = createStyledCheckBox("Other Documentation");

        GridPane evidencePane = new GridPane();
        evidencePane.setHgap(15);
        evidencePane.setVgap(8);
        evidencePane.addRow(0, receiptsEv, statementsEv, logsEv, emailsEv);
        evidencePane.addRow(1, contractsEv, photosEv, recordingsEv, otherEv);

        formGrid.add(evidenceLabel, 0, row);
        formGrid.add(evidencePane, 1, row++);

        Label evidenceDescLabel = createStyledLabel("📝 Evidence Description:", false);
        TextArea evidenceDescArea = createStyledTextArea();
        evidenceDescArea.setPromptText("Describe the evidence you have and how it supports your report...");
        evidenceDescArea.setPrefRowCount(4);
        formGrid.add(evidenceDescLabel, 0, row);
        formGrid.add(evidenceDescArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 7. Law Enforcement Action Section
        Label actionSectionLabel = createSectionLabel("⚖️ REQUESTED LAW ENFORCEMENT ACTION");
        formGrid.add(actionSectionLabel, 0, row++, 2, 1);

        Label assetFreezeLabel = createStyledLabel("🔒 Asset Freeze Recommendation:", false);
        ToggleGroup assetFreezeGroup = new ToggleGroup();
        RadioButton freezeYes = createStyledRadioButton("Yes, recommend asset freeze");
        RadioButton freezeNo = createStyledRadioButton("No freeze needed");
        RadioButton freezeUnsure = createStyledRadioButton("Let authorities decide");
        freezeYes.setToggleGroup(assetFreezeGroup);
        freezeNo.setToggleGroup(assetFreezeGroup);
        freezeUnsure.setToggleGroup(assetFreezeGroup);

        VBox assetFreezeBox = new VBox(8, freezeYes, freezeNo, freezeUnsure);
        formGrid.add(assetFreezeLabel, 0, row);
        formGrid.add(assetFreezeBox, 1, row++);

        Label subpoenaLabel = createStyledLabel("📜 Financial Subpoena Recommendation:", false);
        ToggleGroup subpoenaGroup = new ToggleGroup();
        RadioButton subpoenaYes = createStyledRadioButton("Yes, recommend financial subpoenas");
        RadioButton subpoenaNo = createStyledRadioButton("No subpoenas needed");
        RadioButton subpoenaUnsure = createStyledRadioButton("Let authorities decide");
        subpoenaYes.setToggleGroup(subpoenaGroup);
        subpoenaNo.setToggleGroup(subpoenaGroup);
        subpoenaUnsure.setToggleGroup(subpoenaGroup);

        VBox subpoenaBox = new VBox(8, subpoenaYes, subpoenaNo, subpoenaUnsure);
        formGrid.add(subpoenaLabel, 0, row);
        formGrid.add(subpoenaBox, 1, row++);

        Label actionRequestLabel = createStyledLabel("🎯 Specific Actions Requested:", false);
        TextArea actionRequestArea = createStyledTextArea();
        actionRequestArea.setPromptText("Describe specific investigative actions you're requesting from law enforcement...");
        actionRequestArea.setPrefRowCount(4);
        formGrid.add(actionRequestLabel, 0, row);
        formGrid.add(actionRequestArea, 1, row++);

        // Add spacing
        formGrid.add(new Label(""), 0, row++);

        // 8. Additional Information Section
        Label additionalSectionLabel = createSectionLabel("ℹ️ ADDITIONAL INFORMATION");
        formGrid.add(additionalSectionLabel, 0, row++, 2, 1);

        Label reportedElsewhereLabel = createStyledLabel("📝 Reported Elsewhere:", false);
        ToggleGroup reportedGroup = new ToggleGroup();
        RadioButton reportedYes = createStyledRadioButton("Yes, reported to other authorities");
        RadioButton reportedNo = createStyledRadioButton("No, first report");
        reportedYes.setToggleGroup(reportedGroup);
        reportedNo.setToggleGroup(reportedGroup);

        HBox reportedBox = new HBox(20, reportedYes, reportedNo);
        formGrid.add(reportedElsewhereLabel, 0, row);
        formGrid.add(reportedBox, 1, row++);

        Label urgencyLabel = createStyledLabel("⏰ Urgency Level:", false);
        ComboBox<String> urgencyBox = createStyledComboBox();
        urgencyBox.getItems().addAll(
                "Critical - Ongoing large-scale operation",
                "High - Substantial amounts involved",
                "Medium - Moderate financial impact",
                "Standard - Regular investigation pace"
        );
        urgencyBox.setPromptText("Select urgency level");
        formGrid.add(urgencyLabel, 0, row);
        formGrid.add(urgencyBox, 1, row++);

        Label additionalInfoLabel = createStyledLabel("💭 Additional Information:", false);
        TextArea additionalInfoArea = createStyledTextArea();
        additionalInfoArea.setPromptText("Any other relevant information about the money laundering activity...");
        additionalInfoArea.setPrefRowCount(4);
        formGrid.add(additionalInfoLabel, 0, row);
        formGrid.add(additionalInfoArea, 1, row++);

        // Add spacing before buttons
        formGrid.add(new Label(""), 0, row++);

        // Buttons
        Button submitBtn = createStyledButton("📤 Submit Money Laundering Report", true);
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
            if (amountField.getText().trim().isEmpty() ||
                    (!cashCB.isSelected() && !wireCB.isSelected() && !cryptoCB.isSelected() &&
                            !checkCB.isSelected() && !businessCB.isSelected() && !investmentCB.isSelected() &&
                            !loanCB.isSelected() && !otherSourceCB.isSelected())) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill the total amount and select at least one source of funds.");
                return;
            }
            try {
                // Collect fund sources
                String fundSources = getSelectedCheckboxes(cashCB, wireCB, cryptoCB, checkCB,
                        businessCB, investmentCB, loanCB, otherSourceCB);

                // Collect evidence
                String availableEvidence = getSelectedCheckboxes(receiptsEv, statementsEv, logsEv, emailsEv,
                        contractsEv, photosEv, recordingsEv, otherEv);

                // Get date values
                String startDateStr = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : "";
                String endDateStr = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : "";
                String incidentDateStr = datePicker.getValue() != null ? datePicker.getValue().toString() : "";

                // Insert into database
                boolean success = DatabaseHelper.insertMoneyLaunderingReport(
                        // Common fields
                        nameField.getText().trim(),
                        fatherNameField.getText().trim(),
                        motherNameField.getText().trim(),
                        complainantPhoneField.getText().trim(),
                        nidBcField.getText().trim(),
                        locationField.getText().trim(),
                        incidentDateStr,
                        timeField.getText().trim(),
                        descriptionArea.getText().trim(),

                        // Financial information
                        amountField.getText().trim(),
                        currencyBox.getValue() != null ? currencyBox.getValue() : "",

                        // Fund sources
                        fundSources,
                        otherSourceArea.getText().trim(),

                        // Account information
                        bankDetailsArea.getText().trim(),
                        walletArea.getText().trim(),
                        jurisdictionArea.getText().trim(),

                        // Entities
                        shellCompaniesArea.getText().trim(),
                        beneficialOwnerArea.getText().trim(),

                        // Transaction patterns
                        startDateStr,
                        endDateStr,
                        frequencyBox.getValue() != null ? frequencyBox.getValue() : "",
                        transactionDetailsArea.getText().trim(),

                        // Evidence
                        availableEvidence,
                        evidenceDescArea.getText().trim(),

                        // Law enforcement
                        getSelectedRadioButton(assetFreezeGroup),
                        getSelectedRadioButton(subpoenaGroup),
                        actionRequestArea.getText().trim(),

                        // Additional info
                        getSelectedRadioButton(reportedGroup),
                        urgencyBox.getValue() != null ? urgencyBox.getValue() : "",
                        additionalInfoArea.getText().trim()
                );

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Money Laundering report submitted successfully!\n" +
                                    "Report ID will be sent to your phone.\n" +
                                    "Financial investigation unit will prioritize this case.");

                    // Optionally close the window or clear the form
                    // ((Stage) submitBtn.getScene().getWindow()).close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error",
                            "Failed to submit report. Please try again or contact support.");
                }

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "An unexpected error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
          });

        // Clear button logic
        clearBtn.setOnAction(e -> {
            // Clear common fields
            nameField.clear();
            fatherNameField.clear();
            motherNameField.clear();
            complainantPhoneField.clear();
            locationField.clear();
            datePicker.setValue(null);
            timeField.clear();
            descriptionArea.clear();
            nidBcField.clear();

            // Clear money laundering specific fields
            amountField.clear();
            currencyBox.setValue(null);
            cashCB.setSelected(false);
            wireCB.setSelected(false);
            cryptoCB.setSelected(false);
            checkCB.setSelected(false);
            businessCB.setSelected(false);
            investmentCB.setSelected(false);
            loanCB.setSelected(false);
            otherSourceCB.setSelected(false);
            otherSourceArea.clear();
            otherSourceArea.setDisable(true);
            bankDetailsArea.clear();
            walletArea.clear();
            jurisdictionArea.clear();
            shellCompaniesArea.clear();
            beneficialOwnerArea.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            frequencyBox.setValue(null);
            transactionDetailsArea.clear();
            receiptsEv.setSelected(false);
            statementsEv.setSelected(false);
            logsEv.setSelected(false);
            emailsEv.setSelected(false);
            contractsEv.setSelected(false);
            photosEv.setSelected(false);
            recordingsEv.setSelected(false);
            otherEv.setSelected(false);
            evidenceDescArea.clear();
            assetFreezeGroup.selectToggle(null);
            subpoenaGroup.selectToggle(null);
            actionRequestArea.clear();
            reportedGroup.selectToggle(null);
            urgencyBox.setValue(null);
            additionalInfoArea.clear();
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
        stage.setTitle("Money Laundering Case Report – Bangladesh Police");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 25, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1a5490, #2874a6);" +
                        "-fx-border-color: #154360;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Main title with emoji
        Label titleLabel = new Label("🚨 MONEY LAUNDERING CASE REPORT 🚨");
        titleLabel.setStyle(
                "-fx-font-family: 'System Bold', Arial;" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 3, 0, 2, 2);"
        );

        Label subtitleLabel = new Label("🏛️ Bangladesh Police Financial Crimes Unit");
        subtitleLabel.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #d6eaf8;" +
                        "-fx-font-weight: bold;"
        );

        // Info section with better styling
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20, 0, 0, 0));
        infoBox.setStyle(
                "-fx-background-color: rgba(40, 116, 166, 0.7);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("⚠️ Important Information about Money Laundering Cases:");
        infoTitle.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f7dc6f;"
        );

        Label infoText = new Label(
                "💰 Money laundering cases require detailed financial evidence\n" +
                        "📊 Provide transaction records, bank statements, and documentation\n" +
                        "🏦 Financial Crimes Unit works with banking authorities\n" +
                        "🌐 International cooperation may be required for cross-border cases\n" +
                        "🚨 Financial Crimes Hotline: 999 | 💼 Anti-Money Laundering Unit: 16547"
        );
        infoText.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #d6eaf8;" +
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
                        "-fx-text-fill: #1a5490;" +
                        "-fx-padding: 10px 0 5px 0;" +
                        "-fx-border-color: #1a5490;" +
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
                    "-fx-background-color: linear-gradient(to bottom, #2874a6, #1f5f99);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #2874a6;"
            );
            button.setOnMouseEntered(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #1f5f99, #1a5490);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #1f5f99;"
            ));
            button.setOnMouseExited(e -> button.setStyle(baseStyle +
                    "-fx-background-color: linear-gradient(to bottom, #2874a6, #1f5f99);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #2874a6;"
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
