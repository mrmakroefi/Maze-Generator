/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Makroefi
 */
public class MainWindow extends javax.swing.JFrame implements IMaze {

    Graphics2D g;
    int cols, rows;
    int w = 25;
    Maze maze;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        g = (Graphics2D) mazePanel.getGraphics();

        cols = mazePanel.getWidth() / w;
        rows = mazePanel.getHeight() / w;

        maze = new Maze(g, cols, rows, w);

        maze.addListener(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        generateButton = new javax.swing.JButton();
        solutionButton = new javax.swing.JButton();
        mazePanel = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        searchComboBox = new javax.swing.JComboBox();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Maze Generator DFS");
        setResizable(false);
        setSize(new java.awt.Dimension(520, 550));

        generateButton.setText("Generate");
        generateButton.setActionCommand("generateButton");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        solutionButton.setText("Solution");
        solutionButton.setActionCommand("solutionButton");
        solutionButton.setEnabled(false);
        solutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solutionButtonActionPerformed(evt);
            }
        });

        mazePanel.setBackground(new java.awt.Color(0, 0, 0));
        mazePanel.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                OnMazePanel(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout mazePanelLayout = new javax.swing.GroupLayout(mazePanel);
        mazePanel.setLayout(mazePanelLayout);
        mazePanelLayout.setHorizontalGroup(
            mazePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );
        mazePanelLayout.setVerticalGroup(
            mazePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );

        resetButton.setText("Reset");
        resetButton.setActionCommand("resetButton");
        resetButton.setEnabled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        searchComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        searchComboBox.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                OnSearchComboBox(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mazePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(generateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(solutionButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mazePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateButton)
                    .addComponent(solutionButton)
                    .addComponent(resetButton)
                    .addComponent(searchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed

        maze.ClearMaze();

        try {
            maze.Generate();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_generateButtonActionPerformed

    private void solutionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solutionButtonActionPerformed
        try {
            switch (searchComboBox.getSelectedIndex()) {
                case 0:
                    maze.Search();
                    break;
                case 1:
                    maze.AStarSearch();
                    break;
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_solutionButtonActionPerformed


    private void OnMazePanel(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_OnMazePanel
        maze.UpdateGraphics();
    }//GEN-LAST:event_OnMazePanel

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        maze.ResetMaze();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void OnSearchComboBox(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_OnSearchComboBox
        searchComboBox.removeAllItems();
        // item 0
        searchComboBox.addItem("DFS");
        // item 1
        searchComboBox.addItem("A*");
    }//GEN-LAST:event_OnSearchComboBox

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton generateButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mazePanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JComboBox searchComboBox;
    private javax.swing.JButton solutionButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void OnGenerateStart() {
        generateButton.setEnabled(false);
        solutionButton.setEnabled(false);
        resetButton.setEnabled(false);
        System.out.println("generate start");
    }

    @Override
    public void OnGenerateEnd() {
        generateButton.setEnabled(true);
        solutionButton.setEnabled(true);
        System.out.println("generate end");
    }

    @Override
    public void OnSearchStart() {
        generateButton.setEnabled(false);
        solutionButton.setEnabled(false);
        System.out.println("search start");
    }

    @Override
    public void OnSearchEnd() {
        generateButton.setEnabled(true);
        solutionButton.setEnabled(false);
        resetButton.setEnabled(true);
        System.out.println("search end");
    }

    @Override
    public void OnResetEnd() {
        solutionButton.setEnabled(true);
        resetButton.setEnabled(false);
        System.out.println("reset end");
    }
}
