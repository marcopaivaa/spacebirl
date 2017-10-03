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
    private int posOriX, posOriY;
    private boolean speedAtivo = false;
    private int tipPlayer = 0;
    
     public Player(int x, int y, int largura, int altura, Color color, int tipPlayer)
     {
         super(x, y, largura, altura, color);
         posOriX = x;
         posOriY = y;
         this.tipPlayer = tipPlayer;
         try {
            if(tipPlayer == 0){
               player = ImageIO.read(new File("./src/imagens/player.png"));
               player_shield = ImageIO.read(new File("./src/imagens/player-shield.png")); 
            }else if(tipPlayer == 1){
               player = ImageIO.read(new File("./src/imagens/player1.png"));
               player_shield = ImageIO.read(new File("./src/imagens/player1-shield.png")); 
            }else{
               player = ImageIO.read(new File("./src/imagens/player2.png"));
               player_shield = ImageIO.read(new File("./src/imagens/player2-shield.png")); 
            }
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
        setX(posOriX);
        setY(posOriY);
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
    
    public int getTipo(){
        return this.tipPlayer;
    }

    public void rotacionar(int rot){
        player = rotate(player,rot);
        player_shield = rotate(player_shield,rot);
    }
    
    public void redesenhar(){
        
    }
}








