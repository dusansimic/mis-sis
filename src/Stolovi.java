import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stolovi extends Application {
    public enum Tip {
        Izbor,
        Pregled;
    }

    Stage stage = new Stage();
    Tip tip;
    Message<Sto> porukaZaSto;

    Button[] stolovi;

    public Stolovi(Tip tip) {
        super();
        this.tip = tip;
    }

    public Stolovi(Tip tip, Message<Sto> porukaZaSto) {
        super();
        this.tip = tip;
        this.porukaZaSto = porukaZaSto;
    }

    private Parent constructIzbor() {
        stolovi = new Button[5];

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(15));
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(45.0);
        gp.setHgap(45.0);

        List<RezervacijaEntity> rezervacije = Podaci.rezervacijeSkaldiste.sadrzaj().stream().filter(rez -> rez.getDatumVreme().toLocalDate().equals(LocalDate.now())).collect(Collectors.toList());

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            stolovi[i] = new Button(Integer.toString(i+1));
            stolovi[i].setPrefSize(60.0, 60.0);
            boolean exists = rezervacije.stream().filter(rez -> rez.getSto().getBroj() - 1 == finalI).count() > 0;
            stolovi[i].setDisable(exists);
            stolovi[i].setOnAction(actionEvent -> {
                porukaZaSto.setMessage(Podaci.stolovi[finalI]);
                stage.close();
            });

            gp.add(stolovi[i], Podaci.stolovi[i].getX(), Podaci.stolovi[i].getY());
        }

        return gp;
    }

    private Parent constructPregled() {
        stolovi = new Button[5];

        for (int i = 0; i < 5; i++) {
            stolovi[i] = new Button(Integer.toString(i+1));
            stolovi[i].setPrefSize(60.0, 60.0);
            int finalI = i;
            stolovi[i].setOnAction(actionEvent -> {
                Racun racun;
                if (Podaci.racuni[finalI] == null) {
                    racun = new Racun(Racun.Tip.Otvori, finalI);
                } else {
                    racun = new Racun(Racun.Tip.Pregled, finalI);
                }
                try {
                    racun.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(15));
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                gp.add(stolovi[i/2], i % 3, i/3);
            }
        }
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(45.0);
        gp.setHgap(45.0);
        return gp;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stolovi");
        stage.setScene(new Scene(tip == Tip.Izbor ? constructIzbor() : constructPregled(), 500, 500));
        stage.setResizable(false);
        stage.show();
    }

    public void show() throws Exception {
        start(stage);
    }
}
