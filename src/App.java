import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import entidades.RegistroTemperatura;
import servicios.CargadorDatos;
import servicios.ServicioAnalisis;

public class App {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Archivo CSV: ");
            String ruta = sc.nextLine();
            List<RegistroTemperatura> lista = CargadorDatos.cargar(ruta);

            System.out.print("Fecha inicio: ");
            LocalDate ini = LocalDate.parse(sc.nextLine(), F);
            System.out.print("Fecha fin: ");
            LocalDate fin = LocalDate.parse(sc.nextLine(), F);

            Map<String, Double> proms = ServicioAnalisis.promedioPorCiudad(lista, ini, fin);
            FrmRegistroTemperaturas.mostrar(proms);

            System.out.print("Fecha consulta: ");
            LocalDate f = LocalDate.parse(sc.nextLine(), F);

            Optional<RegistroTemperatura> cal = ServicioAnalisis.masCalurosa(lista, f);
            Optional<RegistroTemperatura> fri = ServicioAnalisis.masFria(lista, f);

            cal.ifPresent(r -> System.out.println("Más calurosa: " + r.getCiudad() + " " + r.getTemperatura()));
            fri.ifPresent(r -> System.out.println("Más fría: " + r.getCiudad() + " " + r.getTemperatura()));

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
