/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.components.factory;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Swing Komponenseket (button, textfield, combobox) létrehozó segédosztály
 *
 * @author <Andó Sándor Zsolt>
 */
public final class SwingComponentFactory {

    private SwingComponentFactory() {
    }

    public static JButton generateButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        panel.add(button);
        return button;
    }

    public static JTextField generateTextField(JPanel panel, String text) {
        //add validation
        JLabel label = new JLabel(text);
        JTextField textField = new JTextField();
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    public static JTextField generateTextField(JPanel panel, String label, String text) {
        JTextField textField = generateTextField(panel, label);
        textField.setText(text);
        return textField;
    }

    public static JComboBox generateComboBox(JPanel panel, String text) {
        JComboBox comboBox = new JComboBox();
        JLabel label = new JLabel(text);
        panel.add(label);
        panel.add(comboBox);
        return comboBox;
    }

    public static JComboBox generateComboBox(JPanel panel, String text, Object[] values) {
        JComboBox comboBox = new JComboBox(values);
        JLabel label = new JLabel(text);
        panel.add(label);
        panel.add(comboBox);
        return comboBox;
    }
}
