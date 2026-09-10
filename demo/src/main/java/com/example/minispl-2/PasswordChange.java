package org.example.java; // Change from com.example.oopproject

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class PasswordChange extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🔐 Reset Password - Bangladesh Portal");

        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        // Background gradient
        LinearGradient background = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#43cea2")),
                new Stop(1, Color.web("#185a9d"))
        );
        root.setBackground(new Background(new BackgroundFill(background, CornerRadii.EMPTY, Insets.EMPTY)));

        Label title = new Label("🔐 Reset Your Password");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Enter your registered phone and new password");
        subtitle.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        subtitle.setTextFill(Color.LIGHTGRAY);

        VBox header = new VBox(10, title, subtitle);
        header.setAlignment(Pos.CENTER);

        // Form input fields
        TextField phoneField = new TextField();
        phoneField.setPromptText("📱 Enter your phone number");
        styleInput(phoneField);

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("🔑 Enter new password");
        styleInput(newPassword);

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("🔁 Confirm new password");
        styleInput(confirmPassword);

        // Reset Button
        Button resetButton = createStyledButton("✅ Reset Password", "#007bff", "#0056b3");
        resetButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String pass1 = newPassword.getText();
            String pass2 = confirmPassword.getText();

            if (phone.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "❗ All fields are required.");
                return;
            }
             if (!pass1.equals(pass2)) {
                showAlert(Alert.AlertType.ERROR, "🔐 Passwords do not match.");
                return;
            }

            if(pass1.length() < 6) {
                showAlert(Alert.AlertType.ERROR , "Minimum 6 characters needed.");
                return;
            }

            // Fix: Use the correct method name
            boolean success = DatabaseHelper.updatePasswordByPhone(phone, pass1);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Password successfully reset!");
                new Login().start(primaryStage); // redirect to login screen
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to reset password. Phone number not found.");
            }
        });

        // Back to Login
        Hyperlink backToLogin = new Hyperlink("← Back to Login");
        backToLogin.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        backToLogin.setTextFill(Color.LIGHTGOLDENRODYELLOW);
        backToLogin.setBorder(Border.EMPTY);
        backToLogin.setStyle("-fx-cursor: hand;");
        backToLogin.setOnAction(e -> new Login().start(primaryStage));

        VBox form = new VBox(15, phoneField, newPassword, confirmPassword, resetButton, backToLogin);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);
        VBox.setVgrow(form, Priority.ALWAYS);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), form);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        root.getChildren().addAll(header, form);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

   public static void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType, s);
        alert.showAndWait();
    }

    private void styleInput(TextField field) {
        field.setPrefWidth(300);
        field.setStyle(
                "-fx-background-color: rgba(255,255,255,0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 14px;"
        );

        field.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                field.setStyle(field.getStyle() + "-fx-border-color: #e67e22;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #e67e22;", "-fx-border-color: #3498db;"));
            }
        });
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(45);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: " + baseColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(baseColor, hoverColor));
            ScaleTransition scale = new ScaleTransition(Duration.millis(120), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(hoverColor, baseColor));
            ScaleTransition scale = new ScaleTransition(Duration.millis(120), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }
}
