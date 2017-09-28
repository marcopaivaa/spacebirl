
package jogo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.Point;

/**
 *
 * @author Marco 152945
 */
public class Game  {
    
    private ArrayList<Base> objetos;
    private ArrayList <Base> lixo;
    private Base player;
    private int largura;
    private int altura;
    private boolean tiro;
    private boolean fimDeJogo;
    private boolean venceu;
    private long ultimoTiro;
    private long ultimoTiroBoss;
    private long ultimoBonus;
    private long minTimeBonus;
    private int bonusLimit;
    private boolean hasBonus;
    private long speedtime;
    private int nivel;
    private int countSuper ;
    private long lastCount;
    private BufferedImage img;
    private BufferedImage life;
    private BufferedImage empty_start;
    private BufferedImage empty_mid;
    private BufferedImage empty_end;
    private BufferedImage full_start;
    private BufferedImage full_mid;
    private BufferedImage full_end; 
    private BufferedImage bonus; 
    private BufferedImage bonus_shield; 
    private BufferedImage bonus_speed; 
    private BufferedImage super_portrait; 
    private BufferedImage shift; 
    private boolean boss;
    private Chefe chefe;
    private long inicio;
    private Random r;

    public Game(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        initConfig();
        try {
            img = ImageIO.read(new File("./src/imagens/background.jpg"));
            life = ImageIO.read(new File("./src/imagens/life.png"));
            empty_start = ImageIO.read(new File("./src/imagens/life_boss_empty_start.png"));
            empty_mid = ImageIO.read(new File("./src/imagens/life_boss_empty_mid.png"));
            empty_end = ImageIO.read(new File("./src/imagens/life_boss_empty_end.png"));
            full_start = ImageIO.read(new File("./src/imagens/life_boss_full_start.png"));
            full_mid = ImageIO.read(new File("./src/imagens/life_boss_full_mid.png"));
            full_end = ImageIO.read(new File("./src/imagens/life_boss_full_end.png"));
            bonus = ImageIO.read(new File("./src/imagens/bonus-portrait.png"));
            bonus_shield = ImageIO.read(new File("./src/imagens/bonus-portrait-shield.png"));
            bonus_speed = ImageIO.read(new File("./src/imagens/bonus-portrait-speed.png"));
            super_portrait = ImageIO.read(new File("./src/imagens/super-portrait.png"));
            shift = ImageIO.read(new File("./src/imagens/shift.png"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initConfig(){
        player = new Player(largura / 2 - 40, altura - 190, 100, 80, Color.WHITE);
        objetos = new ArrayList<Base>();
        lixo = new ArrayList<Base>();
        r = new Random();
        fimDeJogo = false;
        venceu = false;
        boss = false;
        boss = false;
        tiro = false;
        hasBonus = false;
        nivel = 0;
        countSuper = 0;
        minTimeBonus = 10000;
        bonusLimit = 1;
        ultimoTiro = System.currentTimeMillis();
        ultimoTiroBoss = System.currentTimeMillis();
        ultimoBonus = System.currentTimeMillis() + minTimeBonus;
        inicio = System.currentTimeMillis();
        add(player);
    }
    
    public boolean verificaPonto(ArrayList<Point> list,int x,int y){
        for(Point p: list){
            if(((x >= p.x && x <= p.x + 70) || x < 10) && ((y >= p.y && y <= p.y + 70) || y < 80))
                return true;
        }
        return false;
    }
    
    public void initGame(Graphics bg)
    {
        if(nivel < 3){
            ArrayList <Point> pontos = new ArrayList<Point>();
            
            nivel ++;
            int x,y;
            for(int i = 0; i < nivel; i++){
                for(int j = 0; j < 10; j++){
                    do{
                        x = r.nextInt(largura-60);
                        y = r.nextInt(altura/2);
                    }while(verificaPonto(pontos,x,y));
                    add(new Bola(x, y, 70, 70, Color.WHITE));
                    pontos.add(new Point(x,y));
                }
            }
        }
        else if(nivel == 3){
            criarChefe(bg);
        }
        else{
            venceuJogo(bg);
        } 
    }

    public Base getPlayer() {
        return player;
    }

    public void setPlayer(Base player) {
        this.player = player;
    }

    
    
    public void upDate(Graphics bg)
    {
            
            limpaTela(bg);
            if(fimDeJogo)
            {
                if(venceu)
                    venceuJogo(bg);
                else
                    fimDeJogo(bg);
            }
            else
            {
                tentaBonus();
                speedBonus();
                moverTodos();
                desenharPlacar(bg);
                desenharTodos(bg);
                tentaTiro();
                verificaTiro(bg);
                verificarColisaoComPlayer();
                verificarDano();
                verificarColisaoComGame();
                verificarFim();
                
                if(zeroInimigos())
                    initGame(bg);
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
    }
    
    public void tentaBonus(){
        if(bonusLimit > 0){
            long time = System.currentTimeMillis();
            if(time > ultimoBonus && !hasBonus){
                int flag = r.nextInt(10);
                int tipo = r.nextInt(3);
                tipo ++;
                if(flag == 0){
                    ultimoBonus = System.currentTimeMillis() + minTimeBonus;
                    bonusLimit--;
                    hasBonus = true;
                    int x = r.nextInt(largura-60);
                    int y = r.nextInt((int)(altura*0.8));
                    Bonus bonus = new Bonus(x,y,60,60,Color.WHITE,tipo);
                    add(bonus);
                }
            }
        }
    }
    
    public boolean zeroInimigos(){
        for(Base b: objetos){
            if(b instanceof Bola){
                return false;
            }
        }
        return true;
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
    
    
    public void add(Base b)
    {
        objetos.add(b);
    }

    public void setPlayerActions(boolean direito,
                                boolean esquerdo,
                                boolean cima,
                                boolean baixo,
                                boolean reiniciar, 
                                boolean tiro,
                                boolean especial,
                                Graphics bg) {
            
            if (direito && player.getX() < largura - player.getLargura()) {
                player.setIncX(2);
            } else if (esquerdo && player.getX() > 0) {
                player.setIncX(-2);
            } else {
                player.setIncX(0);
            }
            
            if (baixo && player.getY() < altura - player.getAltura() -  80) {
                player.setIncY(2);
            } else if (cima && player.getY() > 0) {
                player.setIncY(-2);
            } else {
                player.setIncY(0);
            }
            
            if(especial && countSuper >= 99){
                Tiro t = new Tiro(player.x + player.largura/2, player.y-60, 80,60,Color.WHITE, true);
                t.incX=0;
                t.incY=-4;
                objetos.add(t);
                countSuper = 0;
            }
            
            this.tiro = tiro;
            
            if(fimDeJogo && reiniciar)
            {
                reiniciar = false;
                objetos.clear();
                lixo.clear();
                initConfig();
                initGame(bg);
            }
    }

    private void moverTodos() {
        for (Base b : objetos) {
            b.mover(largura,altura);
        }
    }

    private void desenharTodos(Graphics bg) {
        for (Base b : objetos) {
            if(b instanceof Bonus)
                if(b.timeOut()){
                    lixo.add(b);
                    bonusLimit++;
                    ultimoBonus = System.currentTimeMillis() + minTimeBonus;
                }
            b.desenhar(bg);
        }
    }

    private void verificarColisaoComPlayer() {
            for(Base b: objetos)
            {
                if(player.colisaoCom(b))
                {
                    lixo.add(b);
                    
                    if(b instanceof Bonus){
                        if(b.getBonus() == 3)
                            countSuper += 25;
                        else
                            player.setBonus(b.getBonus());
                        bonusLimit++;
                        ultimoBonus = System.currentTimeMillis() + minTimeBonus;
                    }
                    else if(b instanceof Chefe)
                        while(player.getLife() > 0)
                            player.hit();
                    else
                        player.hit();
                }
            }
    }

    private void verificarColisaoComGame() {

        for(Base b: objetos)
        {
            if(b.getX() < 0)
                if(b instanceof Chefe)
                    b.setIncX(3);
                else
                    b.setIncX(2);
            
            if(b.getY() < 10)
                b.setIncY(31);
            
            if(b.getX() > largura - b.getLargura())
                if(b instanceof Chefe)
                    b.setIncX(-3);
                else
                    b.setIncX(-2);
            
            if(b instanceof Tiro && (b.y - b.altura) <= 0){
                lixo.add(b);
            }
            
        }
    }

    private void desenharPlacar(Graphics bg) {
        bg.setColor(Color.WHITE);
        long ms = System.currentTimeMillis() - inicio;
        long min = ms/60000;
        long seg = (ms%60000)/1000;
        bg.drawString("Tempo: " + String.format("%02d:%02d", min, seg),1100,780);
        
        if(countSuper < 100){
            if(lastCount + 500 < System.currentTimeMillis()){
                lastCount = System.currentTimeMillis();
                countSuper++;
            }
            bg.drawImage(bonus, 75, 735, 85, 50, null);
            bg.drawString(String.format("%02d",countSuper) + "%",90,768);
        }
        else{
            bg.drawImage(shift, 10, 745, 60, 30, null);
            bg.drawImage(super_portrait, 75, 735, 85, 50, null);
        }
        
        if(player.getBonus() == 1)
            bg.drawImage(bonus_shield, 165, 735, 50, 50, null);
        else if(player.getBonus() == 2)
            bg.drawImage(bonus_speed, 165, 735, 50, 50, null);
        else{
            bg.drawImage(bonus, 165, 735, 50, 50, null);
            hasBonus = false;
        }
            
        for(int i = 1; i <= player.getLife(); i++)
            bg.drawImage(life, (30*i)+190, 750, 30, 30, null);
        
        if(boss){
            int boss_life = chefe.getLife();
            int max_life  = chefe.getMaxLife();
            int i = 1;
            
            if(boss_life > 0)
                bg.drawImage(full_start, 1220, 40, 30, 30, null);
            else
                bg.drawImage(empty_start, 1220, 40, 30, 30, null);
            
            if(max_life > 2){
                for(i = 1; i < max_life -1 ; i++){
                    if(boss_life > 1){
                        bg.drawImage(full_mid, 1220 - (30*i), 40, 30, 30, null);    
                        boss_life--;
                    }else{
                        bg.drawImage(empty_mid, 1220 - (30*i), 40, 30, 30, null);    
                    }
                }
            }
            
            if(boss_life >= 2){
                    bg.drawImage(full_end, 1220 - (30*i), 40, 30, 30, null);    
            }
            else{
                    bg.drawImage(empty_end, 1220 - (30*i), 40, 30, 30, null);    
            }
                
                        
        }
    }

    private void limpaTela(Graphics bg) {
        bg.fillRect(0, 0, largura, altura);
        bg.drawImage(img, 0, 0,largura, altura, null);    
    }

    private void verificarDano() {
        
        
        for(Base b: objetos)
        {
           if(b.getY()+b.altura > altura)
           {
               if(!(b instanceof Tiro))
                    player.hit();
               lixo.add(b);
           }    
        }
    }

    
    private void verificarFim() {
    
           if(player.getLife() == 0)
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

    private void fimDeJogo(Graphics bg) {
        
        bg.setColor(Color.WHITE);
        String msg = "QUE NÃO VAI DAR O QUE PORRA! - Tecla 'R' para reiniciar.";
        bg.drawString(msg,20,100);
    }
    
    private void venceuJogo(Graphics bg) {
        
        bg.setColor(Color.WHITE);
        String msg = "Você venceu o jogo, BIRL! - Tecla 'R' para reiniciar.";
        bg.drawString(msg,20,100);
    }

    private void tentaTiro() {
      if(tiro)
      {
        long tempoAtual = System.currentTimeMillis();
        if(tempoAtual >  ultimoTiro + 500)
        {
            int multiplicador = 1;
            if(player.getSpeedAtivo())
                multiplicador = 2;
            ultimoTiro = tempoAtual;
            Tiro t = new Tiro(player.x + player.largura/2, player.y-20, 20,20,Color.WHITE, false);
            t.incX=0;
            t.incY=(-3 * multiplicador);
            objetos.add(t);
            
        }
      } 
      
      if(boss)
      {
        long tempoAtual = System.currentTimeMillis();
        if(tempoAtual >  ultimoTiroBoss + 1000)
        {
            ultimoTiroBoss = tempoAtual;
            Tiro t = new Tiro(chefe.x + chefe.largura/2, chefe.y+chefe.altura, 40, 40,Color.RED, false);
            t.incX=0;
            t.incY=3;
            objetos.add(t);
        }
      } 
      
    }

    private void verificaTiro(Graphics bg) {
        for(Base x: objetos)
            for(Base y: objetos)
                if(x.colisaoCom(y) && x instanceof Tiro)
                {
                    if(y instanceof Chefe){
                        int vida = y.hit();
                        if(vida <= 1){
                            boss = false;
                            venceu = true;
                            fimDeJogo = true;
                        }
                    }
                    else if(y instanceof Player){
                        int vida = y.getLife();
                        if(vida <= 0){
                            fimDeJogo = true;
                        }
                    }else if(!(y instanceof Bonus))
                        lixo.add(y);
                    
                    if(!(y instanceof Bonus) && !(y instanceof Player)){
                        if(x.isEspecial())
                            if(x.getLife() > 0)
                                x.hit();
                        
                        if(!x.isEspecial() || x.getLife() < 1)
                            lixo.add(x);
                        
                        countSuper++;
                    }
                }
    }

    private void criarChefe(Graphics bg) {
        if(!boss){
            chefe = new Chefe(80,80,200,200,Color.PINK);
            objetos.add(chefe); 
            boss = true;
        }
          
    }
    
}
