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
 * @author marco
 */
public class Bonus extends Base {
    
    private BufferedImage bonus;
    private int tipo;
    private long createTime;
    
     public Bonus(int x, int y, int largura, int altura, Color color, int tipo)
     {
         super(x, y, largura, altura, color);
         this.tipo = tipo;
         this.createTime = System.currentTimeMillis();
         String icon;
         if(tipo == 1)
             icon = "./src/imagens/shield.png"; 
         else if (tipo == 2)
             icon = "./src/imagens/speed.png";    
         else
            icon = "./src/imagens/energy.png";
         try {
            bonus = ImageIO.read(new File(icon));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

     @Override
     public void desenhar(Graphics g)
     {
          g.setColor(color);
          //g.fillRect(x, y, largura, altura);
          g.drawImage(bonus, x, y, largura, altura, null);
     }
     
    @Override
     public void mover(int w, int h) {

    }
     
    @Override
     public int getBonus(){
        return this.tipo;
     }
     
     public boolean timeOut(){
         return (createTime + 5000) < System.currentTimeMillis();
     }

    @Override
    public void redesenhar() {
        }
}
