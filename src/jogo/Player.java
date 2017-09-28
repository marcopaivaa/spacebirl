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
public class Player extends Base{
    
    private BufferedImage player;
    private BufferedImage player_shield;
    private int bonus = 0;
    private boolean speedAtivo = false;
    
     public Player(int x, int y, int largura, int altura, Color color)
     {
         super(x, y, largura, altura, color);
         try {
            player = ImageIO.read(new File("./src/imagens/player.png"));
            player_shield = ImageIO.read(new File("./src/imagens/player-shield.png"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
         vida = 3;
     }

     @Override
     public void desenhar(Graphics g)
     {
          g.setColor(color);
          BufferedImage img = (bonus == 1) ? player_shield : player;
          g.drawImage(img, x, y, largura, altura, null);
     }
     
    @Override
     public void mover(int w, int h) {
        
        int multiplicador = 1;
         
        if(speedAtivo){
            multiplicador = 2;
        }
         
        this.x = x + (incX * multiplicador);
        rect.x = x;
        
        this.y = y + (incY * multiplicador);
        rect.y = y;

        
    }
     
    @Override
    public int hit() {
        if(bonus == 1){
            bonus = 0;
            return vida;
        }
        return vida--;
    }
    
    @Override
    public void setBonus(int tipo){
        this.bonus = tipo;
    }
    
    @Override
    public int getBonus(){
        return this.bonus;
    }
    
    @Override
    public boolean getSpeedAtivo(){
        return this.speedAtivo;
    }
     
    @Override
    public void toggleSpeedAtivo(){
        this.speedAtivo = !this.speedAtivo;
    }
}








