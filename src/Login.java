import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login extends Application {
    Label usernameLbl, passwordLbl;
    TextField username, password;
    Button loginBtn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        gp.setPadding(new Insets(5));

        usernameLbl = new Label("Korisničko ime:");
        passwordLbl = new Label("Lozinka:");
        username = new TextField();
        password = new TextField();
        loginBtn = new Button("Uloguj se");
        loginBtn.setOnAction((actionEvent) -> {
            if (!username.getText().equals("user") || !password.getText().equals("pass")) {
                Alert podaci = new Alert(Alert.AlertType.ERROR);
                podaci.setContentText("Netačni podaci!");
                podaci.showAndWait();
                return;
            }
            Dashboard dash = new Dashboard();
            try {
                dash.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        gp.add(usernameLbl, 0, 0);
        gp.add(username, 1, 0);
        gp.add(passwordLbl, 0, 1);
        gp.add(password, 1, 1);
        gp.add(loginBtn, 1, 2);
        gp.setAlignment(Pos.CENTER);

        BorderPane bp = new BorderPane();
        bp.setCenter(gp);

        stage.setTitle("Login");
        stage.setScene(new Scene(bp, 400, 300));
        stage.setResizable(false);
        stage.show();
    }
}
