
package Vista;

import javax.swing.*;
import java.awt.*;

public class EstiloAplicacion {
    public static void aplicarEstilo() {
        try {
            // Establecer Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Personalizar colores
            UIManager.put("Table.gridColor", new Color(200, 200, 200));
            UIManager.put("Table.selectionBackground", new Color(100, 149, 237));
            UIManager.put("Table.selectionForeground", Color.WHITE);

            // Personalizar botones
            UIManager.put("Button.background", new Color(240, 248, 255));
            UIManager.put("Button.foreground", new Color(0, 0, 139));
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 11));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
