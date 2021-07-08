import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RacunEntity {
    private LocalDateTime datumVreme;
    private boolean otvoren;
    private boolean storniran;
    private HashMap<Stavka, Integer> stavke;
    private ObservableList<Kolicina<Stavka>> lista;

    public RacunEntity(LocalDateTime datumVreme) {
        this.datumVreme = datumVreme;
        this.otvoren = true;
        this.storniran = false;
        this.stavke = new HashMap<>();
        this.lista = FXCollections.observableArrayList();
    }

    public RacunEntity(LocalDateTime datumVreme, boolean otvoren, boolean storniran) {
        this.datumVreme = datumVreme;
        this.otvoren = otvoren;
        this.storniran = storniran;
        this.stavke = new HashMap<>();
        this.lista = FXCollections.observableArrayList();
    }

    public LocalDateTime getDatumVreme() {
        return datumVreme;
    }

    public boolean isOtvoren() {
        return otvoren;
    }

    public boolean isStorniran() {
        return storniran;
    }

    public void zatvori() {
        otvoren = false;
    }

    public boolean storniraj() {
        if (otvoren != false) {
            return false;
        }
        storniran = true;
        return true;
    }

    public void dodajStavku(Stavka s, Integer kolicina) {
        if (stavke.containsKey(s)) {
            stavke.put(s, kolicina + stavke.get(s));
        } else {
            stavke.put(s, kolicina);
        }
        lista.clear();
        for (Map.Entry<Stavka, Integer> entry : stavke.entrySet()) {
            lista.add(new Kolicina<>(entry.getKey(), entry.getValue()));
        }
    }

    public void ukloniStavku(Stavka s) {
        if (stavke.containsKey(s)) {
            if (stavke.get(s) == 1) {
                stavke.remove(s);
            } else {
                stavke.put(s, stavke.get(s) - 1);
            }
        }
        lista.clear();
        for (Map.Entry<Stavka, Integer> entry : stavke.entrySet()) {
            lista.add(new Kolicina<>(entry.getKey(), entry.getValue()));
        }
    }

    public Map<Stavka, Integer> getStavke() {
        return stavke;
    }

    public ObservableList<Kolicina<Stavka>> getLista() {
        return lista;
    }
}
