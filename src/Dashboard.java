import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dashboard extends Application {
    Stage stage = new Stage();

    Button rezBtn, stoloviBtn;

    private void loadData() throws IOException {
        Podaci.stavkaSkladiste = new StavkaSkladiste("res/stavke.csv");
        Podaci.racunSkladiste = new RacunSkladiste("res/racuni.csv", Podaci.stavkaSkladiste);
        Podaci.racuni = new RacunEntity[5];
        Podaci.stolovi = new Sto[5];
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                Podaci.stolovi[i/2] = new Sto(i/2+1, 4, i%3, i/3);
            }
        }
        Podaci.rezervacijeSkaldiste = new RezervacijeSkaldiste("res/rezervacije.csv");
    }

    @Override
    public void start(Stage stage) throws Exception {
        loadData();
        rezBtn = new Button("Rezerviši sto");
        rezBtn.setOnAction((actionEvent) -> {
            Rezervacija rez = new Rezervacija();
            try {
                rez.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stoloviBtn = new Button("Prikaži stolove");
        stoloviBtn.setOnAction(actionEvent -> {
            Stolovi stolovi = new Stolovi(Stolovi.Tip.Pregled);
            try {
                stolovi.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30.0);
        root.getChildren().addAll(rezBtn, stoloviBtn);

        stage.setTitle("Dashboard");
        stage.setScene(new Scene(root, 300, 300));
        stage.setResizable(false);
        stage.show();
    }

    public void show() throws Exception {
        start(stage);
    }
}
