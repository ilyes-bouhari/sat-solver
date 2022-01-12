package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class CustomBasicComboBoxRenderer extends BasicComboBoxRenderer {

    private String prompt;

    public CustomBasicComboBoxRenderer(String prompt) {
        this.prompt = prompt;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (index == -1 && value == null) setText( prompt );
        else setText(value.toString());

        return this;
    }
}
