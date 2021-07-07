import java.time.LocalDateTime;

public class RezervacijaEntity {
    private LocalDateTime datumVreme;
    private Sto sto;

    public RezervacijaEntity(Sto sto) {
        this.datumVreme = LocalDateTime.now();
        this.sto = sto;
    }

    public RezervacijaEntity(LocalDateTime datumVreme, Sto sto) {
        this.datumVreme = datumVreme;
        this.sto = sto;
    }

    public LocalDateTime getDatumVreme() {
        return datumVreme;
    }

    public Sto getSto() {
        return sto;
    }
}
