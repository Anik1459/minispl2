package org.example.java;

import com.example.oopproject.service.UserService;
import com.example.oopproject.model.User; // Change from com.example.oopproject

import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import static org.example.java.PasswordChange.showAlert;

// Import the static method from PasswordChange in the correct package
// Change the import

public class Login extends Application {

    private TextField phoneField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🇧🇩 Login - Bangladesh Portal");
        primaryStage.setScene(createLoginScene(primaryStage, "Complainant"));
        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public Scene createLoginScene(Stage primaryStage) {
        return createLoginScene(primaryStage, "Complainant");
    }

    public Scene createLoginScene(Stage primaryStage, String userRole) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        // Responsive gradient background
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#43cea2")),
                new Stop(1, Color.web("#185a9d"))
        );
        root.setBackground(new Background(new BackgroundFill(backgroundGradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Flag Image
        ImageView flagImage = new ImageView(new Image("https://flagcdn.com/w320/bd.png", true));
        flagImage.setFitWidth(100);
        flagImage.setPreserveRatio(true);
        flagImage.setSmooth(true);

        // Resize flag on window resize
        flagImage.fitWidthProperty().bind(primaryStage.widthProperty().multiply(0.15)); // 15% of width

        // Title
        Label welcomeLabel = new Label("👋 Welcome Back!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        welcomeLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Please log in to your " + userRole + " account");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        VBox header = new VBox(5, flagImage, welcomeLabel, subtitleLabel);
        header.setAlignment(Pos.CENTER);

        // Input Form
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);
        form.setMinWidth(250);
        VBox.setVgrow(form, Priority.ALWAYS);

        phoneField = new TextField();
        phoneField.setPromptText("📱 Enter your phone number");
        styleInput(phoneField);

        passwordField = new PasswordField();
        passwordField.setPromptText("🔒 Enter your password");
        styleInput(passwordField);

        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setTextFill(Color.LIGHTGOLDENRODYELLOW);
        forgotPassword.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        forgotPassword.setBorder(Border.EMPTY);
        forgotPassword.setStyle("-fx-cursor: hand;");

        forgotPassword.setOnAction(e -> {
            if (userRole.equals("Admin") || userRole.equals("Investigating Officer")) {
                showAlert(Alert.AlertType.WARNING, "❌ Access Denied!\n\nForgot Password feature is not available for " + userRole + " accounts.\nPlease contact your system administrator for assistance.");
            } else {
                new PasswordChange().start(primaryStage);
            }
        });

        Button loginButton = createStyledButton("🔓 Sign In", "#28a745", "#218838");

        Label signUpPrompt = new Label("Don't have an account?");
        signUpPrompt.setTextFill(Color.WHITE);

        Button signUpButton = new Button("👉 Sign Up");
        signUpButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #ffd700;" +
                        "-fx-underline: true;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: var(20);"+
                        "-fx-cursor: hand;"
        );

        // Disable appearance for Admin and Investigating Officer
        if (userRole.equals("Admin") || userRole.equals("Investigating Officer")) {
            forgotPassword.setStyle("-fx-text-fill: #888888; -fx-cursor: default;");
            signUpButton.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #888888;" +
                            "-fx-underline: false;" +
                            "-fx-font-weight: normal;" +
                            "-fx-font-size: var(20);" +
                            "-fx-cursor: default;"
            );
        }

        loginButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String password = passwordField.getText();

            if (phone.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "❗ Phone and password must not be empty.");
                return;
            }
            else if(password.equals("I am Admin") && phone.equals(123456))
            {
                //Switch to Admin Dashboard
                showAlert(Alert.AlertType.INFORMATION, "🔑 Admin Access Granted!");

                new AdminDashboard().start(primaryStage);
            }
            else if(password.equals("I am Investigation Officer") && phone.equals(123456))
            {
                //Switch to Officer Dashboard
                showAlert(Alert.AlertType.INFORMATION, "👮 Officer Access Granted!");

                new InvestigatorDashboard().start(primaryStage);
            }
            else {
                boolean success = DatabaseHelper.validateLogin(phone, password);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "✅ Login successful!");

                    // Navigate to UserDashboard - ADD THIS LINE
                    try {
                        new UserDashboard().start(primaryStage);
                        System.out.println("UserDashboard launched successfully!");
                    } catch (Exception ex) {
                        System.err.println("Error launching UserDashboard: " + ex.getMessage());
                        ex.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Failed to load dashboard. Please try again.");
                    }

                } else {
                    showAlert(Alert.AlertType.ERROR, "❌ Invalid phone or password.");
                }
            }
        });

        signUpButton.setOnAction(e -> {
            if (userRole.equals("Admin") || userRole.equals("Investigating Officer")) {
                showAlert(Alert.AlertType.WARNING, "❌ Access Denied!\n\nSign Up feature is not available for " + userRole + " accounts.\nOnly authorized personnel can create " + userRole + " accounts.\nPlease contact your system administrator.");
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Redirect to Sign Up scene...");
                alert.showAndWait();
                new SignUp().start(primaryStage);
            }
        });

        HBox bottomBox = new HBox(5, signUpPrompt, signUpButton);
        bottomBox.setAlignment(Pos.CENTER);

        form.getChildren().addAll(phoneField, passwordField, loginButton, forgotPassword, bottomBox);

        // Animation
        FadeTransition fade = new FadeTransition(Duration.seconds(1), form);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        root.getChildren().addAll(header, form);

        Scene scene = new Scene(root, 600, 600);

        // Responsive font resizing (optional enhancement)
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, width / 20));
            subtitleLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, width / 40));
        });
        return scene;
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




// Design-pattern integration: Service Layer delegates authentication to DAO.
    private final UserService designPatternUserService = new UserService();

    public User authenticateWithService(String email, String password)
            throws java.sql.SQLException {
        return designPatternUserService.login(email, password);
    }
}
