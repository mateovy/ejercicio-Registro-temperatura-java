import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JFrame;

public class FrmRegistroTemperaturas {
    public static void mostrar(Map<String, Double> datos) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        datos.forEach((ciudad, prom) -> ds.addValue(prom, "Temp", ciudad));

        JFreeChart g = ChartFactory.createBarChart(
            "Promedio temperaturas",
            "Ciudad",
            "°C",
            ds
        );

        JFrame v = new JFrame();
        v.setContentPane(new ChartPanel(g));
        v.pack(); v.setLocationRelativeTo(null);
        v.setVisible(true);
    }
}