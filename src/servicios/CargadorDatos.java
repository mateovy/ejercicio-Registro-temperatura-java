package servicios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import entidades.RegistroTemperatura;

public class CargadorDatos {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static List<RegistroTemperatura> cargar(String rutaCsv) throws IOException {
        return Files.lines(Paths.get(rutaCsv))
            .skip(1)
            .map(linea -> {
                String[] c = linea.split(",");
                return new RegistroTemperatura(
                    c[0],
                    LocalDate.parse(c[1], FORMATO),
                    Double.parseDouble(c[2])
                );
            })
            .collect(Collectors.toList());
    }
}
