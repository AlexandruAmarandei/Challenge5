/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilerch4;

/**
 * some ideas are from http://forum.codecall.net/topic/49721-simple-text-editor/
 *
 * @author alexa
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UI extends JFrame {

    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    private String currentFile = "C:\\JavaIDE\\Proiecte\\CompilerCh4\\src\\compilerch4\\program.txt";
    private JTextArea textField2;
    public static void main(String[] args) {
        new UI();
    }
    
    public UI() {
        JFrame frame = new JFrame();
        //frame.setLayout(new BorderLayout());
        setSize(500, 800);
        JTextArea textField = new JTextArea(100, 70);
        //textField.setBounds(21, 20, 400, 400);
        textField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scroll.setBounds(0, 21, 400, 400);

        textField2 = new JTextArea(5000, 60);
        //textField.setBounds(21, 20, 400, 400);
        textField2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll2 = new JScrollPane(textField2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scroll.setBounds(0, 21, 400, 400);

        frame.add(scroll, BorderLayout.WEST);
        frame.add(scroll2, BorderLayout.EAST);
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        JButton saveButton = new JButton("Save");
        //saveButton.setBounds(140, 0, 80, 20);
        saveButton.setText("Save");
        JButton openButton = new JButton("Open");
        //openButton.setBounds(60, 0, 80, 20);
        JButton newButton = new JButton("New");
        //newButton.setBounds(0, 0, 60, 20);
        JButton runButton = new JButton("Run");
        JButton saveOutputButton = new JButton("SaveOutput");

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(newButton);

        menuBar.add(openButton);
        menuBar.add(saveButton);
        menuBar.add(runButton);
        menuBar.add(saveOutputButton);
        frame.setJMenuBar(menuBar);
        frame.setSize(1000, 500);

        newButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textField.setText(null);

            }
        });
        
        saveOutputButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String fileName = dialog.getSelectedFile().getAbsolutePath();
                    try {
                        fileName = dialog.getSelectedFile().getAbsolutePath();
                        FileWriter w = new FileWriter(fileName);
                        BufferedWriter bw = new BufferedWriter(w);
                        textField2.write(w);
                        w.close();
                       
                    } catch (IOException e) {
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String fileName = dialog.getSelectedFile().getAbsolutePath();
                    try {
                        fileName = dialog.getSelectedFile().getAbsolutePath();
                        FileWriter w = new FileWriter(fileName);
                        BufferedWriter bw = new BufferedWriter(w);
                        textField.write(w);
                        w.close();
                        currentFile = fileName;
                    } catch (IOException e) {
                    }
                }
            }
        });

        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String fileName = dialog.getSelectedFile().getAbsolutePath();
                    try {
                        textField2.setText(null);
                        FileReader r = new FileReader(fileName);
                        BufferedReader br = new BufferedReader(r);
                        textField.read(br, null);
                        r.close();
                        currentFile = fileName;

                    } catch (IOException e) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Editor can't find the file called " + fileName);
                    }
                }
            }
        });
        runButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                //currentFile = "C:\\JavaIDE\\Proiecte\\CompilerCh4\\src\\compilerch4\\program.txt";
                if (currentFile != null) {
                    System.out.println(currentFile);
                    CompilerCh4 c = new CompilerCh4(currentFile);
                    try {
                        c.main(null);
                        
                        print(c.returnString);
                        c = null;
                    } catch (IOException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });

        frame.setTitle("Interpretor UI");
        //frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
    public void print (String s){
        textField2.append(s + " ");
    }
    public String getFile(){
        
        return currentFile;
    }
}
