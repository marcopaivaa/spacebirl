/*
 * FrmJogo.java
 *
 * Created on 7 de Agosto de 2008, 09:51
 */
package jogo;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;


/**
 *
 * @author Marco 152945
 */
public class FrmJogo extends javax.swing.JFrame implements Runnable {

    private boolean esquerdo    = false;
    private boolean direito     = false;
    private boolean baixo       = false;
    private boolean cima        = false;
    private boolean reiniciar   = false;
    private boolean especial    = false;
    private boolean tiro;
    private boolean esquerdo2    = false;
    private boolean direito2     = false;
    private boolean baixo2       = false;
    private boolean cima2        = false;
    private boolean especial2    = false;
    private boolean tiro2;
    private double  proporcaoX, proporcaoY;
    private boolean restart = false;
    private boolean menu    = true;
    private boolean sair = false;
    private boolean multi = false;
    private boolean flag = false;
    private boolean placar = false;

    /**
     * Creates new form FrmJogo
     */
    public FrmJogo() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        proporcaoX = d.width/1920.0;
        proporcaoY = d.height/1080.0;
        
        initComponents();
        //Constroi um buffer duplo
        createBufferStrategy(2);

        //Constroi uma Thread
        Thread t = new Thread(this);

        //Start a Thread
        t.start();

    }
    
    /**
     * @param args the command line arguments
     */

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension d = tk.getScreenSize();

                FrmJogo jf = new FrmJogo();
                jf.setSize(d.width,d.height);
                jf.setVisible(true);
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SpaceBirl");
        setBounds(new java.awt.Rectangle(0, 0, 1024, 768));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(0, 0));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1280, 800));
        setResizable(false);
        setSize(getSize());
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1102, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 652, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
            if(menu){
                menu = false;
                multi = false;
            }
        }
        
        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE){
            sair = true;
        }
        
        if(evt.getKeyCode() == KeyEvent.VK_M){
            if(menu){
                multi = true;
                menu = false;
                flag = true;
            }
        }
        
        if(evt.getKeyCode() == KeyEvent.VK_NUMPAD0){
            tiro2 = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            tiro = true;
        }

        if (evt.getKeyCode() == KeyEvent.VK_DECIMAL) {
            especial2 = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            especial = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerdo2 = true;
        }

        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            direito2 = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            cima2 = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            baixo2 = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_A) {
            esquerdo = true;
        }

        if (evt.getKeyCode() == KeyEvent.VK_D) {
            direito = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_W) {
            cima = true;
        }
        
        if (evt.getKeyCode() == KeyEvent.VK_S) {
            baixo = true;
        }

        if(evt.getKeyCode()  == KeyEvent.VK_R)
            reiniciar = true;
        
        if(evt.getKeyCode()  == KeyEvent.VK_P)
            if(placar)
                placar = false;
            else
                placar = true;
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (evt.getKeyCode()  == KeyEvent.VK_SPACE) {
            tiro = false;
        }
        
        if (evt.getKeyCode()  == KeyEvent.VK_SHIFT) {
            especial = false;
        }

        if (evt.getKeyCode()  == KeyEvent.VK_LEFT) {
            esquerdo2 = false;
        }

        if (evt.getKeyCode()  == KeyEvent.VK_RIGHT) {
            direito2 = false;
        }
        
         if (evt.getKeyCode() == KeyEvent.VK_UP) {
            cima2 = false;
        }
        
        if (evt.getKeyCode()  == KeyEvent.VK_DOWN) {
            baixo2 = false;
        }
        
        if (evt.getKeyCode()  == KeyEvent.VK_NUMPAD0) {
            tiro2 = false;
        }
        
        if (evt.getKeyCode()  == KeyEvent.VK_DECIMAL) {
            especial2 = false;
        }

        if (evt.getKeyCode()  == KeyEvent.VK_A) {
            esquerdo = false;
        }

        if (evt.getKeyCode()  == KeyEvent.VK_D) {
            direito = false;
        }
        
         if (evt.getKeyCode() == KeyEvent.VK_W) {
            cima = false;
        }
        
        if (evt.getKeyCode()  == KeyEvent.VK_S) {
            baixo = false;
        }

        if(evt.getKeyCode()   == KeyEvent.VK_R)
            reiniciar = false;
        
        
    }//GEN-LAST:event_formKeyReleased

    public void run() {
        BufferStrategy buffer = getBufferStrategy();
        Graphics bg;
        Game g;
        g = multi ? new Multiplayer(getWidth(),getHeight(), proporcaoX, proporcaoY)
                  : new Game(getWidth(),getHeight(), proporcaoX, proporcaoY);
        bg = buffer.getDrawGraphics();
        g.initConfig();
        
        while (true) {

            if(sair){
                if(menu){
                    this.dispose();
                    System.exit(0);
                }else{
                    restart = true;
                    sair = false;
                } 
            }
            
            if(flag){
                flag = false;
                g = new Multiplayer(getWidth(),getHeight(), proporcaoX, proporcaoY);
            }
            
            
            
            if(restart){
                g = new Game(getWidth(),getHeight(), proporcaoX, proporcaoY);
                g.menu(bg);
                direito = false;
                direito2 = false;
                esquerdo = false;
                esquerdo2 = false;
                cima = false;
                cima2 = false;
                baixo = false;
                baixo2 = false;
                menu = true;
                restart = false;
                multi = false;
            }
            
            
            //Aloca o Graphics
            bg = buffer.getDrawGraphics();

            bg.setFont(new Font("Dialog",Font.ITALIC,(int)Math.ceil(25 * proporcaoY)));
            
            if(!menu){
                    g.upDate(bg);
            }else{
                if(placar)
                {
                    g.mostraPlacar(bg);
                }
                else{
                    g.menu(bg);
                }
            }
            
            restart = g.setPlayerActions(direito, esquerdo, cima, baixo, tiro, especial,
                                         direito2, esquerdo2, cima2, baixo2, tiro2, especial2,
                                         reiniciar, bg);
            
            bg.setFont(new Font("Dialog", Font.ITALIC, 20));
            String msg = "BETA";
            bg.drawString(msg, (int) (getWidth() * .95), (int) (getHeight() * .98));
            
            //Libera o Graphics
            bg.dispose();

            //Mostra o desenho
            buffer.show();

            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {

            }

        }

    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
