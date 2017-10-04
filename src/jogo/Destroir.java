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
import static jogo.Base.rotate;

/**
 *
 * @author jsilva
 */
public class Destroir extends Base{
    
    protected BufferedImage destroir;

    public Destroir(int x, int y, int width, int height) {
        super(x, y, width, height, Color.BLACK);
        try {
            File f = new File("./src/imagens/destruir.png");
            destroir= ImageIO.read(f);  
        } catch (IOException ex) {
            Logger.getLogger(Meteor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public void desenhar(Graphics g) {
        g.drawImage(destroir, x, y, null);
    }
    
    @Override
    public void mover(int w, int h) {
        this.x = x + incX;
        rect.x = x;
        this.y = y + incY;
        rect.y = y;
    }
    
    @Override
    public void rotacionar(int rot){
        destroir = rotate(destroir,rot);
    }
    
    @Override
    public void redesenhar(){
        if(destroir != null && largura != 0 && altura !=0)
            destroir = scale(destroir, largura, altura);
    }
    
}
