public class Kolicina<T> {
    private T t;
    private int kolicina;

    public Kolicina(T t, int kolicina) {
        this.t = t;
        this.kolicina = kolicina;
    }

    public T getT() {
        return t;
    }

    public int getKolicina() {
        return kolicina;
    }

    @Override
    public String toString() {
        return t.toString() + " - " + kolicina;
    }
}
