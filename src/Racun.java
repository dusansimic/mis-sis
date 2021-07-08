import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class Racun extends Application {
    public enum Tip {
        Otvori,
        Pregled;
    }
    Stage stage = new Stage();
    Tip tip;
    int sto;

    Button otvoriBtn, dodajStavkuBtn, obrisiStavkuBtn, zatvoriRacunBtn, ponistiRacunBtn;
    ListView<Kolicina<Stavka>> stavkeLista;

    public Racun(Tip tip, int sto) {
        this.tip = tip;
        this.sto = sto;
        if (tip == Tip.Pregled) {
            stavkeLista = new ListView<>(Podaci.racuni[sto].getLista());
        }
    }

    private Parent constructOtvori() {
        otvoriBtn = new Button("Otvori novi račun");
        otvoriBtn.setOnAction(actionEvent -> {
            Podaci.racuni[sto] = new RacunEntity(LocalDateTime.now());
            Podaci.racunSkladiste.sadrzaj().add(Podaci.racuni[sto]);
            stage.close();
        });
        return otvoriBtn;
    }

    private Parent constructPregled() {
        dodajStavkuBtn = new Button("Dodaj novu stavku");
        obrisiStavkuBtn = new Button("Obriši stavku");
        zatvoriRacunBtn = new Button("Zatvori račun");
        ponistiRacunBtn = new Button("Poništi račun");
        dodajStavkuBtn.setOnAction(actionEvent -> {
            IzborStavke izborStavke = new IzborStavke(sto);
            try {
                izborStavke.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        obrisiStavkuBtn.setOnAction(actionEvent -> {
            if (stavkeLista.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Greška!");
                alert.setContentText("Nije odabrana stavka za brisanje!");
                alert.showAndWait();
                return;
            }
            Podaci.racuni[sto].ukloniStavku(stavkeLista.getSelectionModel().getSelectedItem().getT());
            try {
                Podaci.racunSkladiste.zapisi();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        zatvoriRacunBtn.setOnAction(actionEvent -> {
            Podaci.racuni[sto].zatvori();
            try {
                Podaci.racunSkladiste.zapisi();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Podaci.racuni[sto] = null;
            stage.close();
        });
        ponistiRacunBtn.setOnAction(actionEvent -> {
            Podaci.racuni[sto].zatvori();
            Podaci.racuni[sto].storniraj();
            try {
                Podaci.racunSkladiste.zapisi();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Podaci.racuni[sto] = null;
            stage.close();
        });
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15.0);
        root.setPadding(new Insets(10.0));
        root.getChildren().addAll(dodajStavkuBtn, obrisiStavkuBtn, zatvoriRacunBtn, ponistiRacunBtn, stavkeLista);
        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Račun");
        stage.setScene(new Scene(tip == Tip.Otvori ? constructOtvori() : constructPregled(), 400, 400));
        stage.setResizable(false);
        stage.show();
    }

    public void show() throws Exception {
        start(stage);
    }
}
