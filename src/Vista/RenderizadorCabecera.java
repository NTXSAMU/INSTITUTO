package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RenderizadorCabecera extends DefaultTableCellRenderer {
    public RenderizadorCabecera() {
        setHorizontalAlignment(CENTER);
        setOpaque(true);
        setBackground(new Color(23, 1, 1));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 16));
    }
}
