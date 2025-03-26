package gui;

import javax.swing.*;
import java.awt.*;
import constant.commonConstant;

abstract class Form extends JFrame {

    public Form (String title) {
        super(title);
        setSize(520, 650);
        setLayout(null);
        setBackground(commonConstant.SECONDARY_COLOR);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(commonConstant.PRIMARY_COLOR);
    }
    // Overloaded createLabel() methods
    protected JLabel createLabel(String text, int x, int y) {
        return createLabel(text, x, y, 480, 20, 18);  // Default width & font size
    }

    protected JLabel createLabel(String text, int x, int y, int width, int height, int fontSize) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setForeground(commonConstant.TEXT_COLOR);
        label.setFont(new Font("Poppins", Font.PLAIN, fontSize));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        add(label);
        return label;
    }

    // Overloaded createTextField() methods
    protected JTextField createTextField(int x, int y) {
        return createTextField(x, y, 400, 50);  // Default width & height
    }

    protected JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setBackground(commonConstant.SECONDARY_COLOR);
        textField.setForeground(commonConstant.TEXT_COLOR);
        textField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(textField);
        return textField;
    }

    protected JPasswordField createPasswordField(int x, int y, int width, int height) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(x, y, width, height);
        passwordField.setBackground(commonConstant.SECONDARY_COLOR);
        passwordField.setForeground(commonConstant.TEXT_COLOR);
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(passwordField);
        return passwordField;
    }

    abstract void addGuiComponents ();

}
