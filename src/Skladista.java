import java.io.*;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


class StavkaSkladiste implements Skladiste<Stavka>, Pisar {
    private ArrayList<Stavka> skladiste;
    private String filename;

    public StavkaSkladiste(String filename) throws IOException {
        this.filename = filename;
        procitaj();
    }

    @Override
    public List<Stavka> sadrzaj() {
        return skladiste;
    }

    @Override
    public void procitaj() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        skladiste = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length != 3) {
                continue;
            }
            Stavka stavka = new Stavka(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2]));
            skladiste.add(stavka);
        }

        reader.close();
    }


    @Override
    public void zapisi() throws IOException {
        if (skladiste == null) {
            return;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (Stavka stavka : skladiste) {
            writer.write(String.format("%d;%s;%f\n", stavka.getId(), stavka.getNaziv(), stavka.getCena()));
        }

        writer.close();
    }
}

class RacunSkladiste implements Skladiste<RacunEntity>, Pisar {
    private ArrayList<RacunEntity> skladiste;
    private StavkaSkladiste stavke;
    private String filename;

    public RacunSkladiste(String filename, StavkaSkladiste stakve) throws IOException {
        this.stavke = stakve;
        this.filename = filename;
        procitaj();
    }

    @Override
    public List<RacunEntity> sadrzaj() {
        return skladiste;
    }

    @Override
    public void procitaj() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        skladiste = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length < 5) {
                continue;
            }
            RacunEntity racun = new RacunEntity(LocalDateTime.parse(data[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME), Boolean.getBoolean(data[1]), Boolean.getBoolean(data[2]));
            for (int i = 3; i < data.length; i++) {
                String[] stavkaData = data[i].split(",");
                if (stavkaData.length != 2) {
                    continue;
                }

                Stavka stavka = stavke.sadrzaj().stream().filter(s -> s.getId() == Integer.parseInt(stavkaData[0])).collect(toSingleton());
                racun.dodajStavku(stavka, Integer.parseInt(stavkaData[1]));
            }
            skladiste.add(racun);
        }

        reader.close();
    }

    private static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
                if (list.size() != 1) {
                    throw new IllegalStateException();
                }
                return list.get(0);
            }
        );
    }

    @Override
    public void zapisi() throws IOException {
        if (skladiste == null) {
            return;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (RacunEntity racun : skladiste) {
            String out = String.format("%s;%b;%b", racun.getDatumVreme().toString(), racun.isOtvoren(), racun.isStorniran());
            for (Kolicina<Stavka> kolicina : racun.getLista()) {
                out += String.format(";%d,%d", kolicina.getT().getId(), kolicina.getKolicina());
            }
            writer.write(String.format("%s\n", out));
        }

        writer.close();
    }
}

class RezervacijeSkaldiste implements Skladiste<RezervacijaEntity>, Pisar {
    private ArrayList<RezervacijaEntity> skladiste;
    String filename;

    public RezervacijeSkaldiste(String filename) throws IOException {
        this.filename = filename;
        procitaj();
    }

    @Override
    public List<RezervacijaEntity> sadrzaj() {
        return skladiste;
    }

    @Override
    public void procitaj() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        skladiste = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length != 3) {
                return;
            }

            RezervacijaEntity rezervacija = new RezervacijaEntity(LocalDateTime.parse(data[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME), Podaci.stolovi[Integer.parseInt(data[1]) - 1], data[2]);
            skladiste.add(rezervacija);
        }

        reader.close();
    }

    @Override
    public void zapisi() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (RezervacijaEntity rezervacija : skladiste) {
            writer.write(String.format("%s;%d;%s\n", rezervacija.getDatumVreme().toString(), rezervacija.getSto().getBroj(), rezervacija.getIme()));
        }

        writer.close();
    }
}
