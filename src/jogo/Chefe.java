/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Marco 152945
 */
public class Chefe extends Base {
    
    private BufferedImage boss;
    private int maxLife = 40;
    
    public Chefe(int x, int y, int largura, int altura, Color color, double proporcaoX) {
        super(x, y, largura, altura, color);
        incX = (int)Math.ceil(3 * proporcaoX);
        vida = maxLife;
        try {
            boss = ImageIO.read(new File("./src/imagens/boss.png"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void desenhar(Graphics g) {
         g.setColor(color);
         //g.fillRect(x, y , largura, altura);
         g.drawImage(boss, x, y, largura, altura, null);
    }
    @Override
    public void mover(int w, int h) {
        this.x = x + incX;
        rect.x = x;  
    }
    
    public int getMaxLife(){
        return maxLife;
    }
   
}
