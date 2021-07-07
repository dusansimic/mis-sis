import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class IzborStavke extends Application {

    Stage stage = new Stage();

    int sto;

    Label odaberiLbl, kolicinaLbl;
    TextField kolicinaTf;
    Button dodajBtn, otkaziBtn;

    public IzborStavke(int sto) {
        super();
        this.sto = sto;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        HBox child = new HBox();
        HBox buttons = new HBox();
        HBox kolicina = new HBox();
        ComboBox meni = new ComboBox();
        for (Stavka stavka : Podaci.stavkaSkladiste.sadrzaj()) {
            meni.getItems().addAll(stavka);
        }
//        meni.getItems().addAll(new Stavka(1,"Stavka 1", 100.0), new Stavka(2,"Stavka 2", 150.0), new Stavka(3, "Stavka 3", 180.0));
        odaberiLbl = new Label("Odaberi stavku:");
        child.getChildren().addAll(odaberiLbl, meni);
        child.setSpacing(15.0);
        child.setAlignment(Pos.BASELINE_LEFT);
        child.setPadding(new Insets(10.0));
        kolicinaLbl = new Label("Količina");
        kolicinaTf = new TextField();
        dodajBtn = new Button("Dodaj");
        otkaziBtn = new Button("Otkaži");
        dodajBtn.setOnAction(actionEvent -> {
            if (kolicinaTf.getText().length() == 0) {
                Alert prazno = new Alert(Alert.AlertType.ERROR);
                prazno.setContentText("Količina nije uneta.");
                prazno.showAndWait();
                return;
            }
            try {
                if (Integer.parseInt(kolicinaTf.getText()) < 1) {
                    Alert negativanBroj = new Alert(Alert.AlertType.ERROR);
                    negativanBroj.setContentText("Količina ne može biti negativna.");
                    negativanBroj.showAndWait();
                    return;
                }
            } catch (Exception e) {
                Alert NaN = new Alert(Alert.AlertType.ERROR);
                NaN.setContentText("Nije unet broj.");
                NaN.showAndWait();
                return;
            }
            Podaci.racuni[sto].dodajStavku((Stavka) meni.getSelectionModel().getSelectedItem(), Integer.parseInt(kolicinaTf.getText()));
            try {
                Podaci.racunSkladiste.zapisi();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        });
        otkaziBtn.setOnAction(actionEvent -> {
            stage.close();
        });
        kolicina.getChildren().addAll(kolicinaLbl, kolicinaTf);
        kolicina.setSpacing(15.0);
        kolicina.setAlignment(Pos.BASELINE_LEFT);
        kolicina.setPadding(new Insets(10.0));
        buttons.getChildren().addAll(dodajBtn, otkaziBtn);
        buttons.setSpacing(15.0);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10.0));
        root.getChildren().addAll(child, kolicina, buttons);
        stage.setTitle("Izbor Stavke");
        stage.setScene(new Scene(root, 300, 300));
        stage.setResizable(false);
        stage.show();
    }

    public void show() throws Exception {
        start(stage);
    }
}
