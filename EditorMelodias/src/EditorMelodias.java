import entidades.*;
import rpdc.ListaLigada;
import rpdc.ReproductorAudioMIDI;
import com.google.gson.Gson;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EditorMelodias extends JFrame {
    private Melodia melodiaActual;
    private DefaultListModel<NotaMusical> listModel;
    private JList<NotaMusical> notasList;

    private JComboBox<Nota> notaComboBox;
    private JComboBox<Figura> figuraComboBox;
    private JSpinner octavaSpinner;

    public EditorMelodias() {
        melodiaActual = new Melodia("Nueva Melodía");
        listModel = new DefaultListModel<>();
        notasList = new JList<>(listModel);
        initUI();
    }

    private ImageIcon createScaledIcon(String path, String description, int width, int height) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL, description);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("No se encontró el archivo: " + path);
            return null;
        }
    }

    private JButton createIconButton(ImageIcon icon, String tooltip, int width, int height) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(width, height));
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void initUI() {
        setTitle("Editor de Melodías Profesional");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon iconAdd = createScaledIcon("/imagenes/add.jpg", "Agregar Nota", 80, 80);
        ImageIcon iconEdit = createScaledIcon("/imagenes/edit.png", "Modificar Nota", 80, 80);
        ImageIcon iconDelete = createScaledIcon("/imagenes/delete.png", "Eliminar Nota", 80, 80);
        ImageIcon iconPlay = createScaledIcon("/imagenes/play.png", "Reproducir", 80, 80);
        ImageIcon iconSave = createScaledIcon("/imagenes/save.png", "Guardar Melodía", 80, 80);
        ImageIcon iconUpload = createScaledIcon("/imagenes/upload.png", "Cargar Melodía", 80, 80);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel controlPanel = new JPanel(new BorderLayout(15, 15));

        JPanel editPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        notaComboBox = new JComboBox<>(Nota.values());
        figuraComboBox = new JComboBox<>(Figura.values());
        octavaSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 7, 1));

        Font bigFont = new Font("Arial", Font.PLAIN, 16);
        notaComboBox.setFont(bigFont);
        figuraComboBox.setFont(bigFont);
        octavaSpinner.setFont(bigFont);

        editPanel.add(new JLabel("Nota:"));
        editPanel.add(notaComboBox);
        editPanel.add(new JLabel("Figura:"));
        editPanel.add(figuraComboBox);
        editPanel.add(new JLabel("Octava:"));
        editPanel.add(octavaSpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));

        // Botones con imqgenes
        JButton agregarBtn = createIconButton(iconAdd, "Agregar Nota", 90, 90);
        JButton modificarBtn = createIconButton(iconEdit, "Modificar Nota", 90, 90);
        JButton eliminarBtn = createIconButton(iconDelete, "Eliminar Nota", 90, 90);
        JButton reproducirBtn = createIconButton(iconPlay, "Reproducir", 90, 90);
        JButton guardarBtn = createIconButton(iconSave, "Guardar Melodía", 90, 90);
        JButton cargarBtn = createIconButton(iconUpload, "Cargar Melodía", 90, 90);

        // Botones de texto
        JButton nuevaBtn = new JButton("NUEVA");
        JButton mostrarBtn = new JButton("MOSTRAR");
        nuevaBtn.setPreferredSize(new Dimension(150, 60));
        mostrarBtn.setPreferredSize(new Dimension(150, 60));
        nuevaBtn.setFont(new Font("Arial", Font.BOLD, 14));
        mostrarBtn.setFont(new Font("Arial", Font.BOLD, 14));
        nuevaBtn.setBackground(new Color(240, 240, 240));
        mostrarBtn.setBackground(new Color(240, 240, 240));

        // Agregar los botones al panel
        buttonPanel.add(agregarBtn);
        buttonPanel.add(modificarBtn);
        buttonPanel.add(eliminarBtn);
        buttonPanel.add(reproducirBtn);
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cargarBtn);
        buttonPanel.add(nuevaBtn);
        buttonPanel.add(mostrarBtn);

        controlPanel.add(editPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        listModel = new DefaultListModel<>();
        notasList = new JList<>(listModel);
        notasList.setFont(bigFont);
        JScrollPane scrollPane = new JScrollPane(notasList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Melodía Actual"));

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        agregarBtn.addActionListener(e -> agregarNota());
        modificarBtn.addActionListener(e -> modificarNota());
        eliminarBtn.addActionListener(e -> eliminarNota());
        reproducirBtn.addActionListener(e -> reproducirMelodia());
        guardarBtn.addActionListener(e -> guardarMelodia());
        cargarBtn.addActionListener(e -> cargarMelodia());
        nuevaBtn.addActionListener(e -> nuevaMelodia());
        mostrarBtn.addActionListener(e -> mostrarNotas());

        add(mainPanel);
    }

    private void actualizarLista() {
        listModel.clear();
        for (int i = 0; i < melodiaActual.getNotas().tamaño(); i++) {
            listModel.addElement(melodiaActual.getNotas().obtener(i));
        }
    }

    private void agregarNota() {
        Nota nota = (Nota) notaComboBox.getSelectedItem();
        Figura figura = (Figura) figuraComboBox.getSelectedItem();
        int octava = (int) octavaSpinner.getValue();

        NotaMusical nuevaNota = new NotaMusical(nota, figura, octava);
        melodiaActual.agregarNota(nuevaNota);
        actualizarLista();
    }

    private void modificarNota() {
        int selectedIndex = notasList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una nota para modificar", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Nota nota = (Nota) notaComboBox.getSelectedItem();
        Figura figura = (Figura) figuraComboBox.getSelectedItem();
        int octava = (int) octavaSpinner.getValue();

        NotaMusical notaModificada = new NotaMusical(nota, figura, octava);
        melodiaActual.modificarNota(selectedIndex, notaModificada);
        actualizarLista();
    }

    private void eliminarNota() {
        int selectedIndex = notasList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una nota para eliminar", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        melodiaActual.eliminarNota(selectedIndex);
        actualizarLista();
    }

    private void reproducirMelodia() {
        if (melodiaActual.getNotas().estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay notas para reproducir", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            for (int i = 0; i < melodiaActual.getNotas().tamaño(); i++) {
                NotaMusical nota = melodiaActual.getNotas().obtener(i);
                ReproductorAudioMIDI.reproducirNota(nota);
            }
        }).start();
    }

    private void guardarMelodia() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Melodía");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".json")) {
                filePath += ".json";
            }

            try {
                rpdc.JsonUtil.guardarMelodia(melodiaActual, filePath);
                JOptionPane.showMessageDialog(this, "Melodía guardada exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarMelodia() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Melodía");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                melodiaActual = rpdc.JsonUtil.cargarMelodia(fileChooser.getSelectedFile().getAbsolutePath());
                actualizarLista();
                JOptionPane.showMessageDialog(this, "Melodía cargada exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void nuevaMelodia() {
        melodiaActual = new Melodia("Nueva Melodía");
        actualizarLista();
    }

    private void mostrarNotas() {
        if (melodiaActual.getNotas().estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay notas para mostrar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Melodía actual:\n");
        for (int i = 0; i < melodiaActual.getNotas().tamaño(); i++) {
            sb.append(i + 1).append(". ").append(melodiaActual.getNotas().obtener(i)).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Notas", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditorMelodias editor = new EditorMelodias();
            editor.setVisible(true);
        });
    }
}