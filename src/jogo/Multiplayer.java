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
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author marco
 */
public class Multiplayer extends Game{
    
    private Base player2;
    BufferedImage bonus2; 
    BufferedImage super2_portrait; 
    BufferedImage del; 
    int placar2;
    int countSuper2;
    long lastCount2;
    boolean tiro2;
    long tempoAtual2;
    long ultimoTiro2;
    long ultimoBonus2;
    boolean hasBonus2;
    long speedtime2;
    
    public Multiplayer(int largura, int altura, double proporcaoX, double proporcaoY){
        super(largura, altura, proporcaoX, proporcaoY);
        try {
            super2_portrait = ImageIO.read(new File("./src/imagens/super2-portrait.png"));
            super_portrait = ImageIO.read(new File("./src/imagens/super1-portrait.png"));
            del = ImageIO.read(new File("./src/imagens/del.png"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initConfig(){
        player = new Player((int)(largura*.1)- (int)Math.ceil(40 * proporcaoX), altura/2, 
                (int)Math.ceil(100 * proporcaoX), (int)Math.ceil(80 * proporcaoY), Color.WHITE, 1);
        player2 = new Player((int)(largura*.9) - (int)Math.ceil(40 * proporcaoX), altura/2,
                (int)Math.ceil(100 * proporcaoX), (int)Math.ceil(80 * proporcaoY), Color.WHITE, 2);
        objetos = new ArrayList<Base>();
        lixo = new ArrayList<Base>();
        r = new Random();
        fimDeJogo = false;
        boss = false;
        boss = false;
        tiro = false;
        tiro2=false;
        hasBonus = false;
        hasBonus2 = false;
        nivelCount = 0;
        placar = 0;
        placar2 = 0;
        nivel = 1;
        countSuper = 0;
        minTimeBonus = 5000;
        bonusLimit = 2;
        countSuper2 = 0;
        lastCount2 = System.currentTimeMillis();
        ultimoTiro = System.currentTimeMillis();
        ultimoTiro2 = System.currentTimeMillis();
        timeChefe = System.currentTimeMillis();
        timeBola = System.currentTimeMillis();
        ultimoBonus = System.currentTimeMillis() + minTimeBonus;
        ultimoBonus2 = System.currentTimeMillis() + minTimeBonus;
        inicio = System.currentTimeMillis();
        add(player);
        add(player2);
    }
    
    @Override
    public void criaInimigo(Graphics bg){
        
    }
    
    @Override
    public void tentaTiro() {
      if(tiro)
      {
        long tempoAtual = System.currentTimeMillis();
        if(tempoAtual >  ultimoTiro + 1000)
        {
            int multiplicador = 1;
            if(player.getSpeedAtivo())
                multiplicador = 2;
            ultimoTiro = tempoAtual;
            Tiro t = new Tiro(player.x + player.largura, player.y+player.altura/2-(int)Math.ceil(10 * proporcaoY), (int)Math.ceil(20 * proporcaoX),(int)Math.ceil(20 * proporcaoY),Color.WHITE, false, 1);
            t.incX=(int)Math.ceil((3 * multiplicador) * proporcaoY);
            t.incY=0;
            objetos.add(t);
            
        }
      } 
      
      if(tiro2)
      {
        long tempoAtual2 = System.currentTimeMillis();
        if(tempoAtual2 >  ultimoTiro2 + 1000)
        {
            ultimoTiro2 = tempoAtual2;
            Tiro t = new Tiro(player2.x, (int)(player2.y+player2.getAltura()/2.3), (int)Math.ceil(20 * proporcaoX), (int)Math.ceil(20 * proporcaoY),Color.RED, false, 2);
            t.incX=(int)Math.ceil(3 * proporcaoY)*-1;
            t.incY=0;
            objetos.add(t);
        }
      } 
    }
    
      @Override
      public void verificarColisaoComGame() {

        for(Base b: objetos)
        {   
            if(b instanceof Tiro && (b.x <= 0 || b.x + b.largura >= this.largura)){
                lixo.add(b);
            }
        }
    }
      
     @Override
    public void desenharPlacar(Graphics bg) {
        bg.setColor(Color.WHITE);
        long ms = System.currentTimeMillis() - inicio;
        long min = ms/60000;
        long seg = (ms%60000)/1000;
        bg.drawString("Tempo: " + String.format("%02d:%02d", min, seg),(int)(this.largura*.9),(int)(this.altura*.95));
        bg.drawString("Placar: " + placar,(int)(this.largura*.13),(int)(this.altura*.97));
        bg.drawString("Placar: " + placar2,(int)(this.largura*.81),(int)(this.altura*.07));
        
        if(countSuper < 100){
            if(lastCount + 500 < System.currentTimeMillis()){
                lastCount = System.currentTimeMillis();
                countSuper++;
            }
            bg.drawImage(bonus, (int)(this.largura * .045), (int)(this.altura * .92), (int)Math.ceil(85 * proporcaoX), (int)Math.ceil(50 * proporcaoY), null);
            bg.drawString(String.format("%02d",countSuper) + "%",(int)(this.largura * .055),(int)(this.altura * .95));
        }
        else{
            bg.drawImage(shift, (int)(this.largura*.01), (int)(this.altura * .92), (int)Math.ceil(60 * proporcaoX), (int)Math.ceil(30 * proporcaoY), null);
            bg.drawImage(super_portrait, (int)(this.largura * .045), (int)(this.altura * .92), (int)Math.ceil(85 * proporcaoX), (int)Math.ceil(50 * proporcaoY), null);
        }
        
        if(countSuper2 < 100){
            if(lastCount2 + 500 < System.currentTimeMillis()){
                lastCount2 = System.currentTimeMillis();
                countSuper2++;
            }
            bg.drawImage(bonus, (int)(this.largura * .905), (int)(this.altura * .06), (int)Math.ceil(85 * proporcaoX), (int)Math.ceil(50 * proporcaoY), null);
            bg.drawString(String.format("%02d",countSuper2) + "%",(int)(this.largura * .915),(int)(this.altura * .09));
        }
        else{
            bg.drawImage(del, (int)(this.largura*.95), (int)(this.altura * .06), (int)Math.ceil(60 * proporcaoX), (int)Math.ceil(30 * proporcaoY), null);
            bg.drawImage(super2_portrait, (int)(this.largura * .905), (int)(this.altura * .06), (int)Math.ceil(85 * proporcaoX), (int)Math.ceil(50 * proporcaoY), null);
        }
        
        if(player.getBonus() == 1)
            bg.drawImage(bonus_shield, (int)(this.largura * .095), (int)(this.altura * .92), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoX), null);
        else if(player.getBonus() == 2)
            bg.drawImage(bonus_speed, (int)(this.largura * .095), (int)(this.altura * .92), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoY), null);
        else{
            bg.drawImage(bonus, (int)(this.largura * .095), (int)(this.altura * .92), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoY), null);
            hasBonus = false;
        }
        
        if(player2.getBonus() == 1)
            bg.drawImage(bonus_shield, (int)(this.largura * .875), (int)(this.altura * .06), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoX), null);
        else if(player.getBonus() == 2)
            bg.drawImage(bonus_speed, (int)(this.largura * .875), (int)(this.altura * .06), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoY), null);
        else{
            bg.drawImage(bonus, (int)(this.largura * .875), (int)(this.altura * .06), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoY), null);
            hasBonus = false;
        }
            
        for(int i = 1; i <= player.getLife(); i++)
            bg.drawImage(life, (int)(this.largura * .115) + (int)Math.ceil(30 * proporcaoY)*i, (int)(this.altura * .92), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);
        
        for(int i = 1; i <= player2.getLife(); i++)
            bg.drawImage(life, (int)(this.largura * .86) - (int)Math.ceil(30 * proporcaoY)*i, (int)(this.altura * .08), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);
       
    }
    
     public boolean setPlayerActions(boolean direito,boolean esquerdo,boolean cima,boolean baixo,boolean tiro,boolean especial,
                                     boolean direito2,boolean esquerdo2,boolean cima2,boolean baixo2,boolean tiro2,boolean especial2,
                                     boolean reiniciar,Graphics bg) {
            
            if (direito && player.getX() < largura - player.getLargura()) {
                player.setIncX((int)Math.ceil(3 * proporcaoX));
            } else if (esquerdo && player.getX() > 0) {
                player.setIncX((int)Math.ceil(3 * proporcaoX)*-1);
            } else {
                player.setIncX(0);
            }
            
            if (baixo && player.getY() < altura - player.getAltura() -  (int)Math.ceil(80 * proporcaoY)) {
                player.setIncY((int)Math.ceil(2 * proporcaoY));
            } else if (cima && player.getY() > 0) {
                player.setIncY((int)Math.ceil(-2 * proporcaoY));
            } else {
                player.setIncY(0);
            }
            
            if (direito2 && player2.getX() < largura - player2.getLargura()) {
                player2.setIncX((int)Math.ceil(3 * proporcaoX));
            } else if (esquerdo2 && player2.getX() > 0) {
                player2.setIncX((int)Math.ceil(3 * proporcaoX)*-1);
            } else {
                player2.setIncX(0);
            }
            
            if (baixo2 && player2.getY() < altura - player2.getAltura() -  (int)Math.ceil(80 * proporcaoY)) {
                player2.setIncY((int)Math.ceil(2 * proporcaoY));
            } else if (cima2 && player2.getY() > 0) {
                player2.setIncY((int)Math.ceil(-2 * proporcaoY));
            } else {
                player2.setIncY(0);
            }
            
            if(especial && countSuper >= 99){
                Tiro t = new Tiro(player.x + player.largura, player.y+player.altura/2, (int)Math.ceil(80 * proporcaoX),(int)Math.ceil(60 * proporcaoY),Color.WHITE, true, 1);
                t.incX=(int)Math.ceil(-4 * proporcaoY) *-1;
                t.incY=0;
                objetos.add(t);
                countSuper = 0;
            }
            
            if(especial2 && countSuper2 >= 99){
                Tiro t = new Tiro(player2.x, player2.y+player2.altura/2, (int)Math.ceil(80 * proporcaoX),(int)Math.ceil(60 * proporcaoY),Color.WHITE, true, 2);
                t.incX=(int)Math.ceil(-4 * proporcaoY);
                t.incY=0;
                objetos.add(t);
                countSuper2 = 0;
            }
            
            this.tiro = tiro;
            this.tiro2 = tiro2;
            
            if(fimDeJogo && reiniciar)
            {
                return true;
            }
            
            return false;
    }
     
    @Override
    public void verificarColisaoComPlayer() {
            for(Base b: objetos)
            {
                if(player.colisaoCom(b) &&b instanceof Bonus )
                {
                    lixo.add(b);
                    placar +=25;
                    if(b.getBonus() == 3){
                        countSuper += 25;
                        placar += 25;
                    }
                    else
                        player.setBonus(b.getBonus());
                    bonusLimit++;
                    ultimoBonus = System.currentTimeMillis() + minTimeBonus;
                }
                
                if(player2.colisaoCom(b) && b instanceof Bonus)
                {
                    lixo.add(b);
                    placar2 +=25;
                    if(b.getBonus() == 3){
                        countSuper2 += 25;
                        placar2 += 25;
                    }
                    else
                        player2.setBonus(b.getBonus());
                    bonusLimit++;
                    ultimoBonus2 = System.currentTimeMillis() + minTimeBonus;
                }
            }
    }
    
    @Override
    public void verificaTiro(Graphics bg) {
        for(Base x: objetos)
            for(Base y: objetos)
                if(x.colisaoCom(y) && x instanceof Tiro)
                {
                    if(y instanceof Player){
                        if(y.getTipo() == 1 && x.getIncX() < 0){
                            int vida = y.hit();
                            if(vida <= 0){
                                fimDeJogo = true;
                            }
                            placar2 += 10;
                            countSuper2 += 25;
                            lixo.add(x);
                        }else if(y.getTipo() == 2 && x.getIncX() > 0){
                            int vida = y.hit();
                            if(vida <= 0){
                                fimDeJogo = true;
                            }
                            placar += 10;
                            countSuper += 25;
                            lixo.add(x);
                        }
                    }
                }
    }
 
    public void speedBonus(){
        if(player.getBonus() == 2 && !player.getSpeedAtivo()){
            speedtime = System.currentTimeMillis();
            player.toggleSpeedAtivo();
        }
        else if(player.getSpeedAtivo() && speedtime + 3000 < System.currentTimeMillis()){
            player.toggleSpeedAtivo();
            player.setBonus(0);
        }
        
        if(player2.getBonus() == 2 && !player2.getSpeedAtivo()){
            speedtime2 = System.currentTimeMillis();
            player2.toggleSpeedAtivo();
        }
        else if(player2.getSpeedAtivo() && speedtime2 + 3000 < System.currentTimeMillis()){
            player2.toggleSpeedAtivo();
            player2.setBonus(0);
        }
    }
    
    @Override
    public void fimDeJogo(Graphics bg) {
        
        int win = 0;
        if(player.getLife() > player2.getLife())
            win = 1;
        else if(player.getLife() < player2.getLife())
            win = 2;
        
        bg.setColor(Color.WHITE);
        String msg;
        if(win != 0)
            msg = "SAIU DA JAULA O MONXTRO, BIIIRL!! - Tecla 'R' para voltar ao menu.";
        else
            msg = "QUE NÃO VAI DAR O QUE PORRA, JOGA DE NOVO!! - Tecla 'R' para voltar ao menu.";
        bg.drawString(msg,(int)(largura * .30),(int)(altura * .35));
        msg = "Pontuação:";
        bg.drawString(msg,(int)(largura * .30),(int)(altura * .40));
        msg = "PLAYER 1: " + placar;
        bg.drawString(msg,(int)(largura * .30),(int)(altura * .45));
         msg = "PLAYER 2: " + placar2;
        bg.drawString(msg,(int)(largura * .30),(int)(altura * .50));
        
        if(win == 1){
            msg = "MOOOONXXTRO!!!!!";
            bg.drawString(msg,(int)(largura * .4),(int)(altura * .45));
            msg = "PRECISA COMER MAIS ANTES DE SAIR DE CASA!!";
            bg.drawString(msg,(int)(largura * .4),(int)(altura * .50));
        }else if (win == 2){
            msg = "PRECISA COMER MAIS ANTES DE SAIR DE CASA!!";
            bg.drawString(msg,(int)(largura * .4),(int)(altura * .45));
            msg = "MOOOONXXTRO!!!!!";
            bg.drawString(msg,(int)(largura * .4),(int)(altura * .50));
        }
    }
    
    @Override
    public void verificarFim() {
    
           if(player.getLife() == 0 || player2.getLife() == 0)
           {
                fimDeJogo = true;
                objetos.clear();
                lixo.clear();
                objetos.add(player);
           }
           else
           {
                objetos.removeAll(lixo);
                lixo.clear();
           } 
        
    }
    
}
