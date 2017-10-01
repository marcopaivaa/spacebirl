/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


/**
 *
 * @author Marco 152945
 */
public abstract class Base {

    protected int x;
    protected int y;
    protected int incX;
    protected int incY;
    protected int largura;
    protected int altura;
    protected Color color;
    protected Rectangle rect;
    protected int vida;

    public Base(int x, int y, int largura, int altura, Color color) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.color = color;
        this.incY = altura/2;
        rect = new Rectangle(x, y, largura, altura);

    }

    public void mover(int w, int h) {
        this.x = x + incX;
        rect.x = x;
        
        if(this.x >= w || this.x + this.largura <= 0 ){
            this.y = y + incY;
            rect.y = y;
        }
        
    }

    public boolean colisaoCom(Base b) {
        if (this.getClass().equals(b.getClass())) 
                return false;
        return this.rect.intersects(b.rect);
    }

    public abstract void desenhar(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIncX() {
        return incX;
    }

    public void setIncX(int incX) {
        this.incX = incX;
    }

    public int getIncY() {
        return incY;
    }

    public void setIncY(int incY) {
        this.incY = incY;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLife() {
        return vida;
    }

    public int hit() {
        return vida--;
    }

    public void setLife(int i) {
        this.vida = i;
    }

    public void setBonus(int tipo) {
    }

    public int getBonus() {
        return 0;
    }
    
    public boolean getSpeedAtivo(){
        return false;
    }
     
    public void toggleSpeedAtivo(){
    }

    public boolean timeOut() {
        return false;
    }

    public boolean isEspecial(){
         return false;
     }
}
