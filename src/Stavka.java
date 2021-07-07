import java.util.Objects;

public class Stavka {

    private int id;
    private String naziv;
    private double cena;

    public Stavka(int id, String naziv, double cena) {
        this.id = id;
        this.naziv = naziv;
        this.cena = cena;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public double getCena() {
        return cena;
    }

    @Override
    public String toString() {
        return getNaziv() + "-" + getCena();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stavka stavka = (Stavka) o;
        return id == stavka.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(naziv, cena);
    }
}
