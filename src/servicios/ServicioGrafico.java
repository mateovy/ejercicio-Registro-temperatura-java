package servicios;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class ServicioGrafico {
    public static JFreeChart crearGrafica(Map<String, Double> datos) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        datos.forEach((ciudad, prom) -> ds.addValue(prom, "°C", ciudad));
        return ChartFactory.createBarChart(
            "Promedio de Temperaturas",
            "Ciudad",
            "°C",
            ds
        );
    }
}
