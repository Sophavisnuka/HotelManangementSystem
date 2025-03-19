package gui;

import javax.swing.JFrame;

import constant.commonConstant;

public class Form extends JFrame {
    public Form (String title) {
        super(title);
        // set size of gui
        setSize(520, 700);
        // close the gui
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //place the gui in the middle
        setLayout(null); 
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(commonConstant.PRIMARY_COLOR);
    }
}
