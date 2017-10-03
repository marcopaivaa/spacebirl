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
public class Tiro extends Base {
    
    private BufferedImage player;
    private BufferedImage boss;
    private BufferedImage player_especial;
    private boolean especial;
    private int tipTiro;

    public Tiro(int x, int y, int largura, int altura, Color color, boolean especial, int tipTipo)
     {
         super(x, y, largura, altura, color);
         vida = 5;
         this.especial = especial;
         this.tipTiro = tipTipo;
         try {
            if(tipTiro == 0){
                player = ImageIO.read(new File("./src/imagens/player-shoot.png"));
                player_especial = ImageIO.read(new File("./src/imagens/super.png"));
                boss = ImageIO.read(new File("./src/imagens/boss-shoot.png"));
            }
            else if( tipTiro == 1){
                player = ImageIO.read(new File("./src/imagens/player1-shoot.png"));
                player_especial = ImageIO.read(new File("./src/imagens/super1.png"));
            }
            else {
                player = ImageIO.read(new File("./src/imagens/player2-shoot.png"));
                player_especial = ImageIO.read(new File("./src/imagens/super2.png"));
            }
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     @Override
     public void desenhar(Graphics g)
     {
          BufferedImage img;
          g.setColor(color);
          if(tipTiro == 0){
            img = (color.RED == color) ? boss : player;
          }
          else{
            img = player;  
          }
          img = especial ? player_especial : img;
          g.drawImage(img, x, y, largura, altura, null);
     }
     @Override
     public void mover(int w, int h) {
        this.x = x + incX;
        this.y = y + incY;
        rect.x = x;
        rect.y = y;        
    }
    
     @Override
     public boolean isEspecial(){
         return especial;
     }

    @Override
    public void redesenhar() {
        
    }
     
}
