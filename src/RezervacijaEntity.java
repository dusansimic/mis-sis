import java.time.LocalDateTime;

public class RezervacijaEntity {
    private LocalDateTime datumVreme;
    private Sto sto;
    private String ime;

    public RezervacijaEntity(LocalDateTime datumVreme, Sto sto, String ime) {
        this.datumVreme = datumVreme;
        this.sto = sto;
        this.ime = ime;
    }

    public LocalDateTime getDatumVreme() {
        return datumVreme;
    }

    public Sto getSto() {
        return sto;
    }

    public String getIme() {
        return ime;
    }
}
