/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import ij.IJ;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import radiality.Radial_grad_map;
import ij.gui.GenericDialog;

/**
 *
 * @author danielaik
 */
public class GuiRadiality {

    JFrame frame = new JFrame("Panel");
    JButton btn1;
    JButton btn2;
    JButton btn3;
    JButton btn4;
    JButton btn5;
    JButton btn6;
    Radial_grad_map radial_obj;

    public GuiRadiality(Radial_grad_map radialobj) {
        this.radial_obj = radialobj;
    }

    public void createPanel() {

        frame.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new GridLayout(7, 3));
        frame.setLocation(new Point(50, 50));
        frame.setSize(new Dimension(500, 200));

        btn1 = new JButton("Step 1: Load image");
        btn2 = new JButton("Step 2: Set parameters");
        btn3 = new JButton("Step 3: Run and visualize");
        btn4 = new JButton("Step 4: NA");
        btn5 = new JButton("Step 5: NA");
        btn6 = new JButton("Step 6: NA");

        //row1
        frame.add(btn1);
        frame.add(btn2);
        frame.add(btn3);

        //row2
        frame.add(btn4);
        frame.add(btn5);
        frame.add(btn6);

        //row3
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        
        //row4
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));

        //row5
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));

        //row6
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));

        //row7
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));
        frame.add(new JLabel(""));

        btn1.addActionListener(btn1Pressed);
        btn2.addActionListener(btn2Pressed);
        btn3.addActionListener(btn3Pressed);
        btn4.addActionListener(btn4Pressed);
        btn5.addActionListener(btn5Pressed);
        btn6.addActionListener(btn6Pressed);

        frame.setVisible(true);

    }


    ActionListener btn1Pressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IJ.log("Loading image...");
            radial_obj.loadImage();

        }
    };

    ActionListener btn2Pressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IJ.log("Setting parameters...");
            radial_obj.initialize();
        }
    };

    ActionListener btn3Pressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IJ.log("Runing and visualizing results...");
            radial_obj.run();
            
        }
    };

    ActionListener btn4Pressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IJ.log("btn4 unused");

        }
    };

    ActionListener btn5Pressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IJ.log("btn5 unused");

        }
    };
    
    ActionListener btn6Pressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            IJ.log("btn6 unused");
            
        }
    };

}
