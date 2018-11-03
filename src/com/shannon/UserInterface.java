package com.shannon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {

    JFrame frame = new JFrame("UserInterface");
    private JButton button1;
    private JPanel panel1;

    public UserInterface(){
        initialize();
    }

    public void initialize(){

        frame.setBounds(100,199,450,300);
        frame.pack();
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,300);

        frame.setVisible(true);

//        final JTextArea textArea = new JTextArea();
//        textArea.setFont(new Font("Monospaced",Font.PLAIN,14));
//        textArea.setForeground(Color.BLACK);
//        textArea.setEditable(false);
//        textArea.setBounds(82,34,275,107);
//        frame.getContentPane().add(textArea);

        JButton btnNewButton = new JButton("Get file");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shannon shannon = new Shannon();
                try{
                    shannon.MainClass();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
//                textArea.setText(shannon.stringBuilder.toString());
            }
        });

        btnNewButton.setBounds(160,182,121,43);
        frame.getContentPane().add(btnNewButton);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    UserInterface window = new UserInterface();
                    window.frame.setVisible(true);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }


}
