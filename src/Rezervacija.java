import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Rezervacija extends Application {
    Stage stage = new Stage();

    DatePicker datum;
    Label timeLbl, dateLbl, imeLbl;
    TextField ime, sat, min;
    Button stoBtn, sacuvajBtn;

    Message<Sto> porukaZaSto;

    private void proveraUnosaDatumaIVremena() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (datum.getValue() == null) {
            alert.setTitle("Greška");
            alert.setHeaderText("Greška!");
            alert.setContentText("Neispravan unos datuma!");
            alert.showAndWait();
            return;
        }
        if (sat.getText().length() == 0 || min.getText().length() == 0) {
            alert.setTitle("Greška");
            alert.setHeaderText("Greška!");
            alert.setContentText("Morate uneti sate i minute za rezervaciju!");
            alert.showAndWait();
            return;
        }
        try {
            int s = Integer.parseInt(sat.getText());
            int m = Integer.parseInt(min.getText());
            LocalTime odabrano = LocalTime.now().withHour(s).withMinute(m);
            LocalTime najranije = LocalTime.now().withHour(17).withMinute(0);
            LocalTime najkasnije = LocalTime.now().withHour(22).withMinute(0);
            if (odabrano.isBefore(najranije) || odabrano.isAfter(najkasnije)) {
                alert.setTitle("Greška");
                alert.setHeaderText("Greška!");
                alert.setContentText("Rezervacije su moguće samo u periodu od 17 do 22h.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            alert.setTitle("Greška");
            alert.setHeaderText("Greška!");
            alert.setContentText("Neispravno uneti sati i minuti!");
            alert.showAndWait();
            return;
        }
    }

    private LocalDateTime getDatumVreme() {
        return LocalDateTime.now()
            .withYear(datum.getValue().getYear())
            .withMonth(datum.getValue().getMonthValue())
            .withDayOfMonth(datum.getValue().getDayOfMonth())
            .withHour(Integer.parseInt(sat.getText()))
            .withMinute(Integer.parseInt(min.getText()))
            .withSecond(0)
            .withNano(0);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        gp.setPadding(new Insets(5));

        porukaZaSto = new Message<>();

        imeLbl = new Label("Ime:");
        dateLbl = new Label("Datum:");
        timeLbl = new Label("Vreme:");

        ime = new TextField();
        sat = new TextField();
        min = new TextField();

        stoBtn = new Button("Odaberi sto");
        stoBtn.setOnAction(actionEvent -> {
            proveraUnosaDatumaIVremena();
            Stolovi stolovi = new Stolovi(Stolovi.Tip.Izbor, porukaZaSto, getDatumVreme().toLocalDate());
            try {
                stolovi.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sacuvajBtn = new Button("Sačuvaj");
        sacuvajBtn.setOnAction(actionEvent -> {
            proveraUnosaDatumaIVremena();

            LocalDateTime vreme = getDatumVreme();
            LocalDateTime granica = LocalDateTime.now().withHour(14).withMinute(0);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (vreme.isBefore(LocalDateTime.now())) {
                alert.setTitle("Greška");
                alert.setHeaderText("Greška!");
                alert.setContentText("Ne može se izabrati prošlo vreme za rezervaciju!");
                alert.showAndWait();
                return;
            }

            if (LocalDateTime.now().isAfter(granica) && vreme.toLocalDate().equals(granica.toLocalDate())) {
                alert.setTitle("Greška");
                alert.setHeaderText("Greška!");
                alert.setContentText("Ne može se rezervisati za današnji dan posle 14h.");
                alert.showAndWait();
                return;
            }

            if (porukaZaSto.getMessage() == null) {
                alert.setTitle("Greška");
                alert.setHeaderText("Greška!");
                alert.setContentText("Morate odabrati sto za rezervaciju!");
                alert.showAndWait();
                return;
            }

            if (ime.getText().length() == 0) {
                alert.setTitle("Greška");
                alert.setHeaderText("Greška!");
                alert.setContentText("Mora se uneti ime za rezervaciju!");
                alert.showAndWait();
                return;
            }

            Podaci.rezervacijeSkaldiste.sadrzaj().add(new RezervacijaEntity(vreme, porukaZaSto.getMessage(), ime.getText()));
            try {
                Podaci.rezervacijeSkaldiste.zapisi();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert uspeh = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacija");
            alert.setHeaderText("Info");
            uspeh.setContentText(String.format("Uspešno rezervisan sto za ime %s", ime.getText()));
            uspeh.showAndWait();
            stage.close();
        });

        datum = new DatePicker();

        gp.add(dateLbl, 0, 0);
        gp.add(datum, 1,0);
        gp.add(timeLbl, 0, 1);
        gp.add(sat, 1, 1);
        gp.add(min, 2, 1);
        gp.add(stoBtn, 1, 2);
        gp.add(imeLbl, 0, 3);
        gp.add(ime, 1, 3);
        gp.add(sacuvajBtn, 1, 4);
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10.0);

        BorderPane bp = new BorderPane();
        bp.setCenter(gp);

        stage.setTitle("Rezervacija");
        stage.setScene(new Scene(bp, 600, 300));
        stage.setResizable(false);
        stage.show();
    }

    public void show() throws Exception {
        start(stage);
    }
}
