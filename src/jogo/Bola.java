/*
 * To change this template, choose Tools | Templates
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
public class Bola extends Base{
     
    private BufferedImage inimigo;
    
     public Bola(int x, int y, int largura, int altura, Color color, double proporcaoX)
     {
         super(x, y, largura, altura, color);
         incX = (int)Math.ceil(2 * proporcaoX);
         vida = 1;
         try {
            inimigo = ImageIO.read(new File("./src/imagens/inimigo.gif"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     @Override
     public void desenhar(Graphics g)
     {
          g.setColor(color);
          //g.fillOval(x, y, largura, altura);
          g.drawImage(inimigo, x, y, largura, altura, null);
     }

    @Override
    public void redesenhar() {
        
    }
     
     
    
    

}









