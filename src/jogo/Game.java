
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
    private int nivelCount;
    private int nivel;
    private int placar;
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
    private long timeBola;
    private long timeChefe;
    private boolean boss;
    private Chefe chefe;
    private long inicio;
    private Random r;
    private double proporcaoX, proporcaoY;

    public Game(int largura, int altura, double proporcaoX, double proporcaoY) {
        this.largura = largura;
        this.altura = altura;
        this.proporcaoX = proporcaoX;
        this.proporcaoY = proporcaoY;
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
        player = new Player(largura / 2 - (int)Math.ceil(40 * proporcaoX), altura - (int)Math.ceil(190 * proporcaoY), (int)Math.ceil(100 * proporcaoX), (int)Math.ceil(80 * proporcaoY), Color.WHITE);
        objetos = new ArrayList<Base>();
        lixo = new ArrayList<Base>();
        r = new Random();
        fimDeJogo = false;
        venceu = false;
        boss = false;
        boss = false;
        tiro = false;
        hasBonus = false;
        nivelCount = 0;
        placar = 0;
        nivel = 1;
        countSuper = 0;
        minTimeBonus = 5000;
        bonusLimit = 1;
        ultimoTiro = System.currentTimeMillis();
        ultimoTiroBoss = System.currentTimeMillis();
        timeChefe = System.currentTimeMillis();
        timeBola = System.currentTimeMillis();
        ultimoBonus = System.currentTimeMillis() + minTimeBonus;
        inicio = System.currentTimeMillis();
        add(player);
    }
    
    public boolean verificaPonto(ArrayList<Point> list,int x,int y){
        for(Point p: list){
            if((y >= p.y && y <= p.y + (int)Math.ceil(70 * proporcaoY)) || y < (int)Math.ceil(80 * proporcaoY))
                return true;
        }
        return false;
    }
    
    public void criaInimigo(Graphics bg){
        ArrayList <Point> pontos = new ArrayList<Point>();

        
        nivel = 1 + (nivelCount/10);
        nivel = nivel > 5 ? 5 : nivel;

        int x,y;
        if(System.currentTimeMillis() - timeBola >= 3000 && boss == false && objetos.size() < 50){
            for(int i = 0; i < nivel; i++){
                do{
                    x = r.nextInt(2);
                    x = x == 0 ? -50 : this.largura;
                    y = r.nextInt(altura/2);
                }while(verificaPonto(pontos,x,y));
                add(new Bola(x, y, (int)Math.ceil(70 * proporcaoY), (int)Math.ceil(70 * proporcaoY), Color.WHITE, proporcaoX));
                pontos.add(new Point(x,y));
            }
            timeBola = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - timeChefe >= 60000){
            criarChefe(bg);
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
                fimDeJogo(bg);
            }
            else
            {
                criaInimigo(bg);
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
                    int x = r.nextInt(largura-(int)Math.ceil(60 * proporcaoX));
                    int y = r.nextInt((int)Math.ceil(altura*0.8));
                    Bonus bonus = new Bonus(x,y,(int)Math.ceil(60 * proporcaoY),(int)Math.ceil(60 * proporcaoY),Color.WHITE,tipo);
                    add(bonus);
                }
            }
        }
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

    public boolean setPlayerActions(boolean direito,
                                boolean esquerdo,
                                boolean cima,
                                boolean baixo,
                                boolean reiniciar, 
                                boolean tiro,
                                boolean especial,
                                Graphics bg) {
            
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
            
            if(especial && countSuper >= 99){
                Tiro t = new Tiro(player.x + player.largura/2, player.y-(int)Math.ceil(60 * proporcaoY), (int)Math.ceil(80 * proporcaoX),(int)Math.ceil(60 * proporcaoY),Color.WHITE, true);
                t.incX=0;
                t.incY=(int)Math.ceil(-4 * proporcaoY);
                objetos.add(t);
                countSuper = 0;
            }
            
            this.tiro = tiro;
            
            if(fimDeJogo && reiniciar)
            {
                return true;
            }
            
            return false;
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
                    else if(b instanceof Chefe)
                        while(player.getLife() > 0)
                            player.hit();
                    else{
                        player.hit();
                        this.nivelCount++;
                    }
                }
            }
    }

    private void verificarColisaoComGame() {

        for(Base b: objetos)
        {
            if(b.getX() + b.getLargura( )< 0)
                if(b instanceof Chefe)
                    b.setIncX((int)Math.ceil(3 * proporcaoX));
                else
                    b.setIncX((int)Math.ceil(2 * proporcaoX));
            
            if(b.getY() < (int)Math.ceil(10 * proporcaoY))
                b.setIncY((int)Math.ceil(31 * proporcaoY));
            
            if(b.getX() > largura)
                if(b instanceof Chefe)
                    b.setIncX((int)Math.ceil(3 * proporcaoX)*-1);
                else
                    b.setIncX((int)Math.ceil(2 * proporcaoX)*-1);
            
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
        bg.drawString("Tempo: " + String.format("%02d:%02d", min, seg),(int)(this.largura*.8),(int)(this.altura*.95));
        bg.drawString("Placar: " + placar,(int)(this.largura*.45),(int)(this.altura*.95));
        
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
        
        if(player.getBonus() == 1)
            bg.drawImage(bonus_shield, (int)(this.largura * .095), (int)(this.altura * .92), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoX), null);
        else if(player.getBonus() == 2)
            bg.drawImage(bonus_speed, (int)(this.largura * .095), (int)(this.altura * .92), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoY), null);
        else{
            bg.drawImage(bonus, (int)(this.largura * .095), (int)(this.altura * .92), (int)Math.ceil(50 * proporcaoY), (int)Math.ceil(50 * proporcaoY), null);
            hasBonus = false;
        }
            
        for(int i = 1; i <= player.getLife(); i++)
            bg.drawImage(life, (int)(this.largura * .11) + (int)Math.ceil(30 * proporcaoY)*i, (int)(this.altura * .93), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);
        
        if(boss){
            int boss_life = chefe.getLife();
            int max_life  = chefe.getMaxLife();
            int i = 1;
            
            if(boss_life > 0)
                bg.drawImage(full_start, (int)(this.largura*.9535), (int)(this.altura*.055), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);
            else
                bg.drawImage(empty_start, (int)(this.largura*.9535), (int)(this.altura*.055), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);
            
            if(max_life > 2){
                for(i = 1; i < max_life -1 ; i++){
                    if(boss_life > 1){
                        bg.drawImage(full_mid, ((int)(this.largura*.9535) - ((int)Math.ceil(30 * proporcaoX)*i)), (int)(this.altura*.055), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);    
                        boss_life--;
                    }else{
                        bg.drawImage(empty_mid, ((int)(this.largura*.9535) - ((int)Math.ceil(30 * proporcaoX)*i)), (int)(this.altura*.055), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);    
                    }
                }
            }
            
            if(boss_life >= 2){
                    bg.drawImage(full_end, ((int)(this.largura*.9535) - ((int)Math.ceil(30 * proporcaoX)*i)), (int)(this.altura*.055), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);    
            }
            else{
                    bg.drawImage(empty_end, ((int)(this.largura*.9535) - ((int)Math.ceil(30 * proporcaoX)*i)), (int)(this.altura*.055), (int)Math.ceil(30 * proporcaoY), (int)Math.ceil(30 * proporcaoY), null);    
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
        bg.drawString(msg,(int)(largura * .30),(int)(altura * .45));
        msg = "Sua pontuação: " + placar;
        bg.drawString(msg,(int)(largura * .42),(int)(altura * .55));
    }
    
//    private void venceuJogo(Graphics bg) {
//        
//        bg.setColor(Color.WHITE);
//        String msg = "Você venceu o jogo, BIRL! - Tecla 'R' para reiniciar.";
//        bg.drawString(msg,(int)Math.ceil(20 * proporcaoX),(int)Math.ceil(100 * proporcaoY));
//    }

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
            Tiro t = new Tiro(player.x + player.largura/2, player.y-(int)Math.ceil(20 * proporcaoY), (int)Math.ceil(20 * proporcaoX),(int)Math.ceil(20 * proporcaoY),Color.WHITE, false);
            t.incX=0;
            t.incY=(int)Math.ceil((-3 * multiplicador) * proporcaoY);
            objetos.add(t);
            
        }
      } 
      
      if(boss)
      {
        long tempoAtual = System.currentTimeMillis();
        if(tempoAtual >  ultimoTiroBoss + 1000)
        {
            ultimoTiroBoss = tempoAtual;
            Tiro t = new Tiro(chefe.x + chefe.largura/2, chefe.y+chefe.altura, (int)Math.ceil(40 * proporcaoX), (int)Math.ceil(40 * proporcaoY),Color.RED, false);
            t.incX=0;
            t.incY=(int)Math.ceil(3 * proporcaoY);
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
                        placar += 10;
                        int vida = y.hit();
                        if(vida <= 1){
                            boss = false;
                            timeChefe = System.currentTimeMillis();
                            lixo.add(y);
                            placar += 1000;
                        }
                    }
                    else if(y instanceof Player){
                        int vida = y.getLife();
                        if(vida <= 0){
                            fimDeJogo = true;
                        }
                    }else if(!(y instanceof Bonus)){
                        if(x.getIncY() < 0){
                            lixo.add(y);
                            this.nivelCount++;
                        }
                    }
                    
                    if(!(y instanceof Bonus) && !(y instanceof Player)){
                        if(x.isEspecial())
                            if(x.getLife() > 0)
                                x.hit();
                        
                        if((!x.isEspecial() || x.getLife() < 1) || x.getIncY() > 0){
                            lixo.add(x);
                            this.nivelCount++;
                        }
                        countSuper++;
                        placar +=10;
                    }
                }
    }

    private void criarChefe(Graphics bg) {
        if(!boss){
            chefe = new Chefe((int)Math.ceil(80 * proporcaoX),(int)Math.ceil(80 * proporcaoY),(int)Math.ceil(200 * proporcaoX),(int)Math.ceil(200 * proporcaoY),Color.PINK, proporcaoX);
            objetos.add(chefe); 
            boss = true;
        }
          
    }
    
}
