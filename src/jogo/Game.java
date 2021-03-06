
package jogo;

import java.awt.Color;
import java.awt.Font;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 *
 * @author Marco 152945
 */
public class Game  {
    
    ArrayList<Base> objetos;
    ArrayList <Base> lixo;
    ArrayList <Base> destruir;
    ArrayList <Clip> sounds;
    Base player;
    int largura;
    int altura;
    boolean tiro;
    boolean fimDeJogo;
    boolean ganhou;
    boolean som;
    long ultimoTiro;
    long ultimoTiroBoss;
    long ultimoBonus;
    long minTimeBonus;
    long ultimoMeteor;
    int bonusLimit;
    boolean hasBonus;
    long speedtime;
    int nivelCount;
    int nivel;
    int placar;
    int countSuper ;
    long lastCount;
    private BufferedImage img;
    BufferedImage life;
    private BufferedImage empty_start;
    private BufferedImage empty_mid;
    private BufferedImage empty_end;
    private BufferedImage full_start;
    private BufferedImage full_mid;
    private BufferedImage full_end; 
    BufferedImage bonus; 
    BufferedImage bonus_shield; 
    BufferedImage bonus_speed; 
    BufferedImage super_portrait; 
    BufferedImage shift;
    private BufferedImage menu;
    long timeBola;
    long timeChefe;
    boolean boss;
    private Chefe chefe;
    long inicio;
    Random r;
    double proporcaoX;
    double proporcaoY;

    public Game(int largura, int altura, double proporcaoX, double proporcaoY) {
        this.largura = largura;
        this.altura = altura;
        this.proporcaoX = proporcaoX;
        this.proporcaoY = proporcaoY;
        sounds = new ArrayList<Clip>();
        addSound("morri.wav");
        addSound("saindoDaJaula.wav");
        addSound("mentira.wav");
        addSound("ceuTemPão.wav");
        addSound("HelloDarknessFunk.wav");
        addSound("errouuu.wav");
        addSound("birl.wav");
        addSound("tetra.wav");
        initConfig();
        try {
            menu = ImageIO.read(new File("./src/imagens/menuSpace.png"));
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
        player = new Player(largura / 2 - (int)Math.ceil(40 * proporcaoX), altura - (int)Math.ceil(190 * proporcaoY),
                (int)Math.ceil(100 * proporcaoX), (int)Math.ceil(80 * proporcaoY), Color.WHITE, 0);
        objetos = new ArrayList<Base>();
        lixo = new ArrayList<Base>();
        destruir = new ArrayList<Base>();
        
        for(Clip c : sounds)
        {
            c.stop();
        }
        r = new Random();
        ganhou = true;
        som = false;
        fimDeJogo = false;
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
        ultimoMeteor = System.currentTimeMillis();
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
    
    public void menu(Graphics bg){
        bg.drawImage(menu, 0, 0, largura, altura, null);
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
                destruir(bg);
                tentaTiro();
                verificaTiro(bg);
                verificarColisaoComPlayer();
                tentaMeteor();
                verificaMeteor();
                verificarDano();
                verificarColisaoComGame();
                verificarFim(bg);
                
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

    public boolean setPlayerActions(boolean direito,boolean esquerdo,boolean cima,boolean baixo,boolean tiro,boolean especial,
                                     boolean direito2,boolean esquerdo2,boolean cima2,boolean baixo2,boolean tiro2,boolean especial2,
                                     boolean reiniciar,Graphics bg){
            
            if (direito && player.getX() < largura - player.getLargura()) {
                player.setIncX((int)Math.ceil(3 * proporcaoX));
            } else if (esquerdo && player.getX() > 0) {
                player.setIncX((int)Math.ceil(3 * proporcaoX)*-1);
            } else {
                player.setIncX(0);
            }
            
            if (baixo && player.getY() < altura - player.getAltura() -  (int)Math.ceil(80 * proporcaoY)) {
                player.setIncY((int)Math.ceil(3 * proporcaoY));
            } else if (cima && player.getY() > 0) {
                player.setIncY((int)Math.ceil(-3 * proporcaoY));
            } else {
                player.setIncY(0);
            }
            
            if(especial && countSuper >= 99){
                Tiro t = new Tiro(player.x + player.largura/2, player.y-(int)Math.ceil(60 * proporcaoY), (int)Math.ceil(80 * proporcaoX),(int)Math.ceil(60 * proporcaoY),Color.WHITE, true, 0);
                t.incX=0;
                t.incY=(int)Math.ceil(-4 * proporcaoY);
                objetos.add(t);
                countSuper = 0;
            }
            
            this.tiro = tiro;
            
            if(fimDeJogo && reiniciar)
            {
                for(Clip c : sounds)
                {
                    c.stop();
                }
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

    public void verificarColisaoComPlayer() {
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

    public void verificarColisaoComGame() {

        for(Base b: objetos)
        {
            if(!(b instanceof Meteor)){
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
                    playSound(5);
                    lixo.add(b);
                }
            }
            else
            {
                if(b.getX() + b.getLargura( ) + 50 < 0)
                    lixo.add(b);

                if(b.getY() + b.getAltura() + 50 < 0)
                    lixo.add(b);

                if(b.getX() - 50 > largura)
                    lixo.add(b);

                if(b.getY() - 50 > altura)
                    lixo.add(b);  
            }
            
        }
    }

    public void desenharPlacar(Graphics bg) {
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
           if(b.getY()+b.altura > altura  && !(b instanceof Meteor))
           {
               if(!(b instanceof Tiro))
                    player.hit();
               lixo.add(b);
           }    
        }
    }

    public void verificarFim(Graphics bg) {
    
           if(player.getLife() == 0)
           {
                fimDeJogo = true;
                objetos.clear();
                destruir(bg);
                objetos.add(player);
                ganhou = true;
                for(Clip c : sounds)
                {
                    c.stop();
                }
           }
           else
           {
                objetos.removeAll(lixo);
                destruir(bg);
           } 
        
    }

    public void fimDeJogo(Graphics bg) {
        if(ganhou)
        {
            bg.setColor(Color.WHITE);
            bg.setFont(new Font("Dialog",Font.ITALIC,70));
            String msg = "GANHOUUUU";
            bg.drawString(msg,(int)(largura * .35),(int)(altura * .45));
            bg.setFont(new Font("Dialog",Font.ITALIC,30));
            msg = "CALCULANDO PONTOS DA VITÓRIA...";
            bg.drawString(msg,(int)(largura * .30),(int)(altura * .60));
            ganhou = false;
            som = true;    
        }
        else if (som) {
            playSound(7);
            ArrayList<String> listPlacar = new ArrayList<String>();
            File arquivo = new File("./src/placar.txt");
            try {
                if (!arquivo.exists()) {
                    //cria um arquivo (vazio)
                    arquivo.createNewFile();
                }
                
                //faz a leitura do arquivo
                FileReader fr = new FileReader(arquivo);
                BufferedReader br = new BufferedReader(fr);
                //enquanto houver mais linhas
                while (br.ready()) {
                    //lê a proxima linha
                    listPlacar.add(br.readLine());
                }
                br.close();
                fr.close();
                int index = -1;
                int cont = 0;
                for(String s : listPlacar)
                {
                    if(!"".equals(s))
                    {
                        if(placar > Integer.parseInt(s.split(" - ")[1]))
                        {
                            index = cont;
                            break;
                        }
                    }
                    cont++;
                }
                if(listPlacar.size()<10 && index == -1)
                {
                    String name = JOptionPane.showInputDialog("Digite seu nome: ");
                    if("".equals(name))
                    {
                        name = "Player";
                    }
                    listPlacar.add(name + " - " + placar);
                }
                else if(index != -1)
                {
                    String name = JOptionPane.showInputDialog("Digite seu nome: ");
                    if("".equals(name))
                    {
                        name = "Player";
                    }
                    listPlacar.add(index,name + " - " + placar);
                }
                //escreve no arquivo
                FileWriter fw = new FileWriter(arquivo);
                BufferedWriter bw = new BufferedWriter(fw);
                for(int i = 0;i<10 && i<listPlacar.size();i++)
                {
                    bw.write(listPlacar.get(i));
                    bw.newLine();
                }
                bw.close();
                fw.close();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            for (Clip c : sounds) {
                c.stop();
            }
            playSound(2);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
            }
            playSound(3);
            playSound(4);
            som = false;
        }
        else
        {
            bg.setFont(new Font("Dialog", Font.ITALIC, 40));
            bg.setColor(Color.WHITE);
            String msg = "E MORREU...";
            bg.drawString(msg, (int) (largura * .10), (int) (altura * .20));
            msg = "Tecla 'R' para voltar ao menu.";
            bg.drawString(msg, (int) (largura * .10), (int) (altura * .30));
            msg = "Sua pontuação: " + placar;
            bg.drawString(msg, (int) (largura * .22), (int) (altura * .45));
            msg = "Placar: ";
            bg.drawString(msg, (int) (largura * .60), (int) (altura * .20));
            double tam = .30;
            File arquivo = new File("./src/placar.txt");
            try {
                FileReader fr = new FileReader(arquivo);
                BufferedReader br = new BufferedReader(fr);
                //enquanto houver mais linhas
                while (br.ready()) {
                    //lê a proxima linha
                    bg.drawString(br.readLine(), (int) (largura * .60), (int) (altura * tam));
                    tam += .05;
                }
                br.close();
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                bg.drawImage(ImageIO.read(new File("./src/imagens/SapoMeme.png")), (int) (largura * .20), (int) (altura * .60), null);
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void tentaTiro() {
      if(tiro)
      {
        long tempoAtual = System.currentTimeMillis();
        if(tempoAtual >  ultimoTiro + 500)
        {
            
            int multiplicador = 1;
            if(player.getSpeedAtivo())
                multiplicador = 2;
            ultimoTiro = tempoAtual;
            Tiro t = new Tiro(player.x + player.largura/2, player.y-(int)Math.ceil(20 * proporcaoY), (int)Math.ceil(20 * proporcaoX),(int)Math.ceil(20 * proporcaoY),Color.WHITE, false, 0);
            t.incX=0;
            t.incY=(int)Math.ceil((-3 * multiplicador) * proporcaoY);
            objetos.add(t);
            playSound(6);
        }
      } 
      
      if(boss)
      {
        long tempoAtual = System.currentTimeMillis();
        if(tempoAtual >  ultimoTiroBoss + 1000)
        {
            ultimoTiroBoss = tempoAtual;
            Tiro t = new Tiro(chefe.x + chefe.largura/2, chefe.y+chefe.altura, (int)Math.ceil(40 * proporcaoX), (int)Math.ceil(40 * proporcaoY),Color.RED, false, 0);
            t.incX=0;
            t.incY=(int)Math.ceil(3 * proporcaoY);
            objetos.add(t);
        }
      } 
      
    }

    public void verificaTiro(Graphics bg) {
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
    
    private void tentaMeteor() {
        long tempoAtual = System.currentTimeMillis();

        if (tempoAtual > ultimoMeteor + 10000) {
            int l = r.nextInt(largura);
            int mov;
            if (l > largura / 2) {
                l = largura;
                mov = -1;
            } else {
                l = 0;
                mov = 1;
            }
            ultimoMeteor = tempoAtual;
            ArrayList<String> lcolor = new ArrayList<String>();
            lcolor.add("Brown");
            lcolor.add("Grey");
            String color = lcolor.get(r.nextInt(2));
            Meteor m = new Meteor(l, r.nextInt(altura - 200), 4,color);
            m.incX = mov;
            m.incY = 1;
            objetos.add(m);
        }
    }
    
    private void verificaMeteor() {
        ArrayList<Meteor> meteors = new ArrayList<Meteor>();
        for (Base x : objetos) {
            for (Base y : objetos) {
                if (x.colisaoCom(y) && x instanceof Meteor && (y instanceof Player || y instanceof Tiro )) {
                    if (y instanceof Player) {
                        int vida = y.getLife();
                        if (vida <= 0) {
                            fimDeJogo = true;
                        }
                    }
                    if (y instanceof Tiro) {
                        lixo.add(y);
                    }
                    lixo.add(x);
                    Meteor me = (Meteor) x;
                    if (me.tipo > 1) {
                        me.tipo--;
                        for (int i = -10; i <= 10; i += 20) {
                            for (int j = -10; j <= 10; j += 20) {
                                Meteor m = new Meteor(x.x + i, x.y + j, me.tipo, me.color);
                                m.incX = i / 10;
                                m.incY = j / 10;
                                meteors.add(m);
                            }
                        }
                    }
                }
            }
        }
        objetos.addAll(meteors);
    }


    private void criarChefe(Graphics bg) {
        if(!boss){
            chefe = new Chefe((int)Math.ceil(80 * proporcaoX),(int)Math.ceil(80 * proporcaoY),(int)Math.ceil(200 * proporcaoX),(int)Math.ceil(200 * proporcaoY),Color.PINK, proporcaoX);
            objetos.add(chefe); 
            boss = true;
            playSound(1);
            
        }
    }
    
    public void playSound(int sound) {
        Clip c = sounds.get(sound);
        c.stop();
        c.setFramePosition(0);
        c.start();
    }
    
    public void addSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./src/sounds/" + soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            sounds.add(clip);
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void destruir(Graphics bg) {
        int cont = 0;
        for (Base l : lixo) {
            if (l instanceof Bola) {
                cont++;
                destruir.add(new Destruir(l.getX() + (l.getLargura() / 2) - 1, l.getY() + (l.getAltura() / 2) - 1, 40, 40));
            }

        }
        if (cont > 0) {
            playSound(0);
        }
        if (destruir != null) {
            for (Base d : destruir) {
                if (d.getLargura() < 70) {
                    d.setX(d.getX() - 1);
                    d.setY(d.getY() - 1);
                    d.setLargura(d.getLargura() + 2);
                    d.setAltura(d.getAltura() + 2);
                    d.desenhar(bg);
                } else {
                    lixo.add(d);
                }
            }
        }
        lixo.clear();
    }
    
    public void mostraPlacar(Graphics bg)
    {
            limpaTela(bg);
            bg.setColor(Color.WHITE);
            bg.setFont(new Font("Dialog", Font.ITALIC, 40));
            String msg = "Placar: ";
            bg.drawString(msg, (int) (largura * .40), (int) (altura * .20));
            double tam = .30;
            File arquivo = new File("./src/placar.txt");
            try {
                FileReader fr = new FileReader(arquivo);
                BufferedReader br = new BufferedReader(fr);
                //enquanto houver mais linhas
                while (br.ready()) {
                    //lê a proxima linha
                    bg.drawString(br.readLine(), (int) (largura * .45), (int) (altura * tam));
                    tam += .05;
                }
                br.close();
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            msg = "Pressione P para sair";
            bg.drawString(msg, (int) (largura * .40), (int) (altura * .90));
    }
    
}