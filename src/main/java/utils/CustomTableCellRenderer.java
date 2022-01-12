package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    public CustomTableCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (Integer.parseInt(table.getValueAt(row, 0).toString()) == 1) {
            c.setBackground(Color.GREEN.darker());
        } else c.setBackground(table.getBackground());

        return c;
    }
}
