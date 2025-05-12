import javax.swing.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import entidades.RegistroTemperatura;
import servicios.CargadorDatos;
import servicios.ServicioAnalisis;
import servicios.ServicioGrafico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import datechooser.beans.DateChooserCombo;

public class FrmRegistroTemperaturas extends JFrame {
    private DateChooserCombo dccDesde, dccHasta, dccConsulta;
    private JTabbedPane tpRegistro;
    private JPanel pnlGrafica, pnlExtremos;
    private List<RegistroTemperatura> datos;
    private static final String RUTA_CSV = "src/datos/Temperaturas.csv";
    private static final Dimension PANEL_DIMENSION = new Dimension(750, 350);

    public FrmRegistroTemperaturas() {
        setTitle("Registro de Temperaturas");
        setSize(800, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JToolBar tb = new JToolBar();
        JButton btnGrafico = new JButton(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGrafico.setToolTipText("Generar Gráfica");
        btnGrafico.addActionListener(e -> generarGrafico());
        JButton btnExtremos = new JButton(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnExtremos.setToolTipText("Consultar Extremos");
        btnExtremos.addActionListener(e -> consultarExtremos());
        tb.add(btnGrafico);
        tb.add(btnExtremos);

        JPanel pnlFechas = new JPanel();
        pnlFechas.setLayout(null);
        pnlFechas.setPreferredSize(new Dimension(800, 50));
        JLabel lblDesde = new JLabel("Desde:");
        lblDesde.setBounds(10, 10, 50, 25);
        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(60, 10, 120, 25);
        JLabel lblHasta = new JLabel("Hasta:");
        lblHasta.setBounds(200, 10, 50, 25);
        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(250, 10, 120, 25);
        JLabel lblConsulta = new JLabel("Consulta:");
        lblConsulta.setBounds(390, 10, 60, 25);
        dccConsulta = new DateChooserCombo();
        dccConsulta.setBounds(460, 10, 120, 25);
        pnlFechas.add(lblDesde);
        pnlFechas.add(dccDesde);
        pnlFechas.add(lblHasta);
        pnlFechas.add(dccHasta);
        pnlFechas.add(lblConsulta);
        pnlFechas.add(dccConsulta);

        pnlGrafica = new JPanel();
        pnlGrafica.setPreferredSize(PANEL_DIMENSION);
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);
        spGrafica.setPreferredSize(PANEL_DIMENSION);

        pnlExtremos = new JPanel();
        pnlExtremos.setPreferredSize(PANEL_DIMENSION);

        tpRegistro = new JTabbedPane();
        tpRegistro.setPreferredSize(PANEL_DIMENSION);
        tpRegistro.addTab("Gráfica", spGrafica);
        tpRegistro.addTab("Extremos", pnlExtremos);

        datos = CargadorDatos.cargar(RUTA_CSV);

        add(tb, BorderLayout.NORTH);
        add(pnlFechas, BorderLayout.CENTER);
        add(tpRegistro, BorderLayout.SOUTH);
    }

    private void generarGrafico() {
        LocalDate desde = dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<RegistroTemperatura> filtrados = datos.stream()
            .filter(r -> !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta))
            .toList();
        Map<String, Double> proms = ServicioAnalisis.promedioPorCiudad(filtrados, desde, hasta);
        JFreeChart chart = ServicioGrafico.crearGrafica(proms);
        ChartPanel cp = new ChartPanel(chart);
        cp.setPreferredSize(PANEL_DIMENSION);
        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(cp, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    private void consultarExtremos() {
        LocalDate fecha = dccConsulta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Optional<RegistroTemperatura> cal = ServicioAnalisis.masCalurosa(datos, fecha);
        Optional<RegistroTemperatura> fri = ServicioAnalisis.masFria(datos, fecha);
        pnlExtremos.removeAll();
        pnlExtremos.setLayout(new GridBagLayout());
        pnlExtremos.setPreferredSize(PANEL_DIMENSION);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        pnlExtremos.add(new JLabel("Ciudad más cálida:"), gbc);
        gbc.gridx = 1;
        pnlExtremos.add(new JLabel(cal.map(r -> r.getCiudad() + " (" + r.getTemperatura() + "°C)").orElse("-")), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlExtremos.add(new JLabel("Ciudad más fría:"), gbc);
        gbc.gridx = 1;
        pnlExtremos.add(new JLabel(fri.map(r -> r.getCiudad() + " (" + r.getTemperatura() + "°C)").orElse("-")), gbc);

        pnlExtremos.revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrmRegistroTemperaturas frm = new FrmRegistroTemperaturas();
            frm.setVisible(true);
        });
    }
}
