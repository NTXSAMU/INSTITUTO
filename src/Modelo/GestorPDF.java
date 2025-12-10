package Modelo;



import Vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

/**
 * Clase gestora que maneja las operaciones de PDF
 * Integra el VisorPDF y el GeneradorPDF
 */
public class GestorPDF {

    private VisorPDF visorPDF;
    private VentanaPrincipal componentePadre;

    public GestorPDF(VisorPDF visorPDF, VentanaPrincipal componentePadre) {
        this.visorPDF = visorPDF;
        this.componentePadre = componentePadre;
    }

    /**
     * Abre un diálogo para seleccionar y cargar un PDF
     */
    public void cargarPDFConDialogo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int resultado = fileChooser.showOpenDialog(componentePadre);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            cargarPDF(archivo);
        }
    }

    /**
     * Carga un archivo PDF en el visor
     */
    public void cargarPDF(File archivo) {
        if (archivo == null || !archivo.exists()) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "El archivo no existe",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "El archivo seleccionado no es un PDF",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            visorPDF.cargarPDF(archivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    componentePadre,
                    "Error al cargar el PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    /**
     * Carga un PDF desde una ruta
     */
    public void cargarPDF(String ruta) {
        cargarPDF(new File(ruta));
    }

    /**
     * Genera un PDF desde una tabla con diálogo de guardado
     */


    /**
     * Genera un PDF con múltiples tablas
     */


    /**
     * Cierra el documento actual del visor
     */
    public void cerrarDocumento() {
        visorPDF.cerrarDocumento();
    }

    /**
     * Abre un archivo con la aplicación predeterminada
     */
    private void abrirArchivo(String ruta) {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(new File(ruta));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VisorPDF getVisorPDF() {
        return visorPDF;
    }
}