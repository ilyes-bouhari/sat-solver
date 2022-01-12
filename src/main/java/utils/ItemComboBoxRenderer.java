package utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class ItemComboBoxRenderer extends BasicComboBoxRenderer {

    private String prompt;

    public ItemComboBoxRenderer(String prompt) {
        this.prompt = prompt;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (index == -1 && value == null) setText( prompt );
        else {
            ComboBoxItem item = (ComboBoxItem) value;
            setText(item.getValue());
        };

        return this;
    }
}
