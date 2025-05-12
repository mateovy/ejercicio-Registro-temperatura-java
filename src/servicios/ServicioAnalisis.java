package servicios;
import entidades.RegistroTemperatura;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServicioAnalisis {
    public static Map<String, Double> promedioPorCiudad(
        List<RegistroTemperatura> datos,
        LocalDate inicio,
        LocalDate fin) {
        return datos.stream()
            .filter(r -> !r.getFecha().isBefore(inicio) && !r.getFecha().isAfter(fin))
            .collect(Collectors.groupingBy(
                RegistroTemperatura::getCiudad,
                Collectors.averagingDouble(RegistroTemperatura::getTemperatura)
            ));
    }

    public static Optional<RegistroTemperatura> masCalurosa(
        List<RegistroTemperatura> datos,
        LocalDate fecha) {
        return datos.stream()
            .filter(r -> r.getFecha().equals(fecha))
            .max(Comparator.comparingDouble(RegistroTemperatura::getTemperatura));
    }

    public static Optional<RegistroTemperatura> masFria(
        List<RegistroTemperatura> datos,
        LocalDate fecha) {
        return datos.stream()
            .filter(r -> r.getFecha().equals(fecha))
            .min(Comparator.comparingDouble(RegistroTemperatura::getTemperatura));
    }
}
