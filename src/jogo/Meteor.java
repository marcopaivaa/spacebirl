/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.Color;

/**
 *
 * @author jonat
 */
public class Meteor extends Base{
    
    protected BufferedImage meteor;
    protected String color;
    protected int tipo;
    Random r = new Random();

    public Meteor(int x, int y, int tipo, String color) {
        super(x, y, 0, 0, Color.BLACK);
        String tam = "";
        this.color = color;
        this.tipo = tipo;
        switch(tipo)
        {
            case 1:
                tam = "tiny";
                break;
            case 2:
                tam = "small";
                break;
            case 3:
                tam = "med";
                break;
            case 4:
                tam = "big";
                break;
        }
        try {
            int i = r.nextInt(2)+1;
            File f = new File("./src/imagens/Meteors/meteor" + color + "_" + tam + i + ".PNG");
            meteor= ImageIO.read(f);  
            setAltura(meteor.getHeight());
            setLargura(meteor.getWidth());
        } catch (IOException ex) {
            Logger.getLogger(Meteor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public void desenhar(Graphics g) {
        g.drawImage(meteor, x, y, null);
    }
    
    public void redesenhar(){
        if(meteor != null && largura != 0 && altura !=0)
            meteor = scale(meteor, largura, altura);
    }
    
}
