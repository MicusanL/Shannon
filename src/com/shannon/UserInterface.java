package com.shannon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {

    JFrame frame = new JFrame("UserInterface");
    private JButton button1;
    private JPanel panel1;

    public UserInterface() {
        initialize();
    }

    public void initialize() {

        frame.setBounds(100, 199, 450, 300);
        frame.pack();
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        frame.setVisible(true);

//        final JTextArea textArea = new JTextArea();
//        textArea.setFont(new Font("Monospaced",Font.PLAIN,14));
//        textArea.setForeground(Color.BLACK);
//        textArea.setEditable(false);
//        textArea.setBounds(82,34,275,107);
//        frame.getContentPane().add(textArea);

// Code File bnt -------------------------------------------------------------------------
        JButton btnCodeFile = new JButton("Code File");
        btnCodeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Code shannonCode = new Code();
                try {
                    shannonCode.CodeFileUsingShannon();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnCodeFile.setBounds(100, 100, 121, 43);
        frame.getContentPane().add(btnCodeFile);

// Decode File bnt -------------------------------------------------------------------------

        JButton btnDecodeFile = new JButton("Decode File");
        btnDecodeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Decode shannonDecode = new Decode();
                try {
                    shannonDecode.DecodeFileUsingShannon();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnDecodeFile.setBounds(250, 100, 121, 43);
        frame.getContentPane().add(btnDecodeFile);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UserInterface window = new UserInterface();
                    window.frame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
