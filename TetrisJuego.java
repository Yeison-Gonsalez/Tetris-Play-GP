
package tetrisjuego;

//import imagenes_tetrix.ReproduceAudio;
//import imagenes_tetrix.Rotar_Figuras;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class TetrisJuego extends JFrame {
    
    public static void main(String[] args){
        //Por si se nos olvida arrancar el juego CUAKK
        new TetrisJuego();
    
    }
    private  String titulo="Tetris";//final
    private  int ancho=600;//final
    private  int alto=720;//final
    //Se usa para sumar valor a Y de su posicion.
    private final int increment=20;
    //Se usa para restar valor a Y de su posicion.
    private final int decrement=-20;
    //Determina la velocidad de caida de la figura.
    private int Velocidad=500;
    //Es el tiempo en ms que te queda para realizar el ultimo movimiento antes de asentar la figura.
    private int VelocidadCercania=400;
    //Almacena en esta variable la velocidad del juego que despues es modificada.  
    private int VelocidadMemoria;
    //Incrementa su variable en 4 a cada finalizacion de linea, para sumar su valor y obtener la posicion exacta de rotacion.
    private int IndiceFigura;
    //Esta VARIABLE almacena el premio que corresponde por eliminar las lineas de figuras.
    private int Premio;
    //Bonus cuando eliminas mas de una linea a la vez.
    private int Bonus;
    //Cuenta la cantidad de lineas que llevas en el juego.
    private int ContadorLineas;
    //Almacena el nivel de dificultad al que esta sometido el jugador durante la partida este varia segun la cantidad de figuras.
    private int Level_Map;
    //Almacena la cantidad total de puntos que lleva el jugador
    private int PuntosJuego;
    //Almacena el total de figuras jugadas durante la partida.
    private int TotalFiguras;
    
    //Determina posicion de la rejilla por defecto
    private final int Min_Izq=200;
    private final int Max_Der=400;
    private final int Max_Aba=660;
    
    //Pausa el juego
    private boolean Pausar;
    //Bloquea las teclas movimiento izquierdo y derecho segun convenga su situacion.
    private boolean BloqueaTeclado;
    //Bloquea la tecla de bajar figura.
    private boolean BloqueaAbajo;
    //Bloquea la tecla de rotacion segun convenga su situacion.
    private boolean BloqueaTecla_Rotar;
    //Determina si la pieza llego al final de su recorrido.
    private boolean Final_Carrera;
    //Bloquea temporalmente la bajada automatica creando el efecto de poder mover la pieza una vez abajo.
    private boolean NuevoCiclo;
    //Determina cuando el juego llego a su fin.
    private boolean FindelJuego;
    

    
    
    //Se almacenan los cuadrados de colores de las figuraas.
    private ArrayList<BufferedImage>Figura2D=new ArrayList<>();
    //Se almacena la lista de figuras.
    private ArrayList<BufferedImage>ListaFiguras=new ArrayList<>();
    //Almacena las imagenes de las letras graficas del menu.
    private ArrayList<BufferedImage> Letras=new ArrayList<>();
    //Almacena las imagenes de los numeros de puntos del juego.
    private ArrayList<BufferedImage> FondoGrafico=new ArrayList<>();
    //Almacena las imagenes de los Fondos graficos del juego.
    private ArrayList<BufferedImage> Numeros=new ArrayList<>();
    
    //Se almacenan todos los datos referentes a las figuras que van rotando en el juego
    private ArrayList<BufferedImage> Figura_2D=new ArrayList<>();
    //Posicion ancho de la figura en la pantalla Grafica.
    private ArrayList<Integer> Figura_PosiX=new ArrayList<>();
    //Posicion alto de la figura en la pantalla Grafica.
    private ArrayList<Integer> Figura_PosiY=new ArrayList<>();
    //Determina que tipo de figura es su color.
    private ArrayList<String> Figura_Tipo=new ArrayList<>();
    //Determina el estado de la figura (0 la figura no esta) (1 la figura esta estatica) (2 la figura esta en movimiento
    private ArrayList<Integer> Figura_Estado=new ArrayList<>();
    //Almacena los indices de las figuras (esatos van de 0 a 7) el indice indica cual es la proxima figura en salir.
    private ArrayList<Integer> Figura_Ronda=new ArrayList<>();
    
    
    private ReproduceAudio ReproAudio= new ReproduceAudio();
    //agregado
    private Tiempo MusicaDeFondo=new Tiempo();
    private Rotar_Figuras Rot_Fig=new Rotar_Figuras(this, Figura_PosiX,Figura_PosiY ,Figura_Tipo,Figura_Estado);//falta algo
    
    //Constructor del juego tetris
    public TetrisJuego(){
        
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(titulo);
        setBounds(0,0,ancho,alto);
        
        Imagenes_Juego Img_Juego=new Imagenes_Juego();
        
        Img_Juego.CargarFiguras(Figura2D);
        Img_Juego.CargarListaFiguras(ListaFiguras);
        Img_Juego.CargarLetras(Letras);
        Img_Juego.CargarNumeros(Numeros); 
        Img_Juego.CargarFondos(FondoGrafico);
        
        add(new PlantillaGrafica());
        
        setVisible(true);
        
        EventosAccionEscucha();
        
        for(int x=0;x<1000;x++){
            Figura_Ronda.add(GenerarNumero());
             
        }
        CrearBucle();
        CrearFigura(Figura_Ronda.get(TotalFiguras));
        
        MusicaDeFondo.PlayMusica();
        
        //ReproAudio.Fx(7);
        //ReproAudio.FxStop(7);
    }
    
    
    //MARCO GRAFICO DEL JUEGO
    public class PlantillaGrafica extends JComponent{
        @Override  
        public void paint(Graphics g){
            
            Graphics2D g2 = (Graphics2D) g;
            
            try {
                
                g2.drawImage(FondoGrafico.get(Level_Map), 0, 0, rootPane);
            }catch(IndexOutOfBoundsException e){
                g2.drawImage(FondoGrafico.get(17), 0, 0, rootPane);
            }
            
            Dibujar_Texto_Grafico(g2, 10, 40, 25, 25, 22, "Puntos");
            Dibujar_Texto_Grafico(g2, 10, 80, 20, 20, 18, ""+PuntosJuego);     
            
            Dibujar_Texto_Grafico(g2, 10, 140, 25, 25, 22, "Lineas");
            Dibujar_Texto_Grafico(g2, 10, 180, 20, 20, 20, ""+ContadorLineas);
            
            Dibujar_Texto_Grafico(g2, 10, 220, 25, 25, 21, "Figuras");
            Dibujar_Texto_Grafico(g2, 10, 260, 15, 15, 18, ""+TotalFiguras);
            
            Dibujar_Texto_Grafico(g2, 10, 300, 25, 25, 21, "Nivel");
            Dibujar_Texto_Grafico(g2, 10, 340, 22, 22, 22, ""+Level_Map);
            
            try{
               if (!Pausar){
                   int Index=0;
                   for (int Estado:Figura_Estado){
                       if(Estado>=1){
                           g2.drawImage(Figura_2D.get(Index), getFx(Index), getFy(Index), rootPane);
                       }
                       Index++;
                   }
               } 
            }catch(ConcurrentModificationException e){
                e.getStackTrace();
            }
            int incrementa=0;
            for (int c=0; c<6;c++){
                g2.drawImage(ListaFiguras.get(Figura_Ronda.get(TotalFiguras+c)), 470, 50+incrementa, rootPane);
                //get/Figura_Ronda por  solo TotalFiguras
                incrementa=incrementa+50;
            }
            
            for (int Tex=0; Tex<680; Tex+=20){
                
                g2.drawImage(Figura2D.get(8), 160, 0+Tex, rootPane);
                g2.drawImage(Figura2D.get(8), 180, 0+Tex, rootPane);
                g2.drawImage(Figura2D.get(8), 400, 0+Tex, rootPane);
                g2.drawImage(Figura2D.get(8), 420, 0+Tex, rootPane);
            }
            
            for (int Tex=0; Tex<240; Tex+=20){
                  g2.drawImage(Figura2D.get(8), 180+Tex, 660, rootPane);
            }
            for (int Tex=0; Tex<600; Tex+=20){
                  g2.drawImage(Figura2D.get(8), Tex, 680, rootPane);
            }
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            //Crea una regilla grafica que ayuda a visualizar las figuras cuando estas caen por la pantalla ....
            
            for (int x=Min_Izq; x<Max_Der; x=x+20){
                g2.setColor(Color.DARK_GRAY);
                g2.drawLine(x, 0, x, Max_Aba);
            }
            
            for(int y=20; y<Max_Aba+20; y=y+20){
                g2.drawLine(Min_Izq, y, Max_Der, y);
            }
            
            
            if (FindelJuego){
                Dibujar_Texto_Grafico(g2, 30, 500, 40, 40, 40, "FIN DEL JUEGO");
            }
            
            if (Pausar){
                Dibujar_Texto_Grafico(g2, 70, 300, 40, 40, 30, "JUEGO PAUSADO");
            }
            
            g2.setFont(new Font("Verdana",Font.BOLD, 15));//bold por ....
            g2.setColor(Color.WHITE);
            g2.drawString(""+Velocidad,400,675);
            
        }//Fin del metodo graficos panint().....
            
    }
    
    
    
    /*Este metodo se encarga de dibujar graficos de texto numero y letras en la pantalla grafica del frame*/
    public void Dibujar_Texto_Grafico(Graphics2D g2,int X,int Y,int Ancho,int Alto, int separacion,String texto){
        int longitud= texto.length();
        
        int vueltas=0;
        String LE[]={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        String NU[]={"0","1","2","3","4","5","6","7","8","9"};
        for(int p=0;p<longitud;p++){
            String letra=""+texto.charAt(p);
            BufferedImage img=null;
            for(int indx=0;indx<LE.length;indx++){
                if(img==null){
                    img=Selec(Letras, letra,LE[indx],indx);
                
                }
            }
            for(int indx=0;indx<NU.length;indx++){
                if(img==null){
                    img=Selec(Numeros, letra,NU[indx],indx);
                
                }
            }
            if(Ancho!=0){
                g2.drawImage(img,X+vueltas,Y,Ancho, Alto,rootPane);
            }
            vueltas+=separacion;//Revisar vaiable separacion, no definida
        }
    }
    
    
    //Este metodo devuelve la imagen correspondiente dependiente del caracter e indice que sea igual
    public BufferedImage Selec(ArrayList<BufferedImage> Buff,String letra,String text, int indx){
        BufferedImage img=null;
        if(letra.equalsIgnoreCase(text)){
            img=Buff.get(indx);
    }      
        return img;
        
    }
    
    
    /*Este metodo se encarga de generar las figuras del tetris dependiendo del indice
    que obtenga de figura*/
    public void CrearFigura(int Figura){
        TotalFiguras++;
        
        setDificultad();
        // Crea la linea recta......
        if(Figura==0){
            FiguraPosixy(260,40);
            FiguraPosixy(280,40);
            FiguraPosixy(300,40);
            FiguraPosixy(320,40);
            CreaDatos("A",2, Figura);
        }
        //Crea la  ELe "L" azul .....
        if(Figura==1){
            FiguraPosixy(260,20);
            FiguraPosixy(260,40);
            FiguraPosixy(280,40);
            FiguraPosixy(300,40);
            CreaDatos("B",2, Figura);
        }
        
        //Crea el  cuadrado Amarillo .....
        if(Figura==2){
            FiguraPosixy(280,40);
            FiguraPosixy(280,20);
            FiguraPosixy(300,40);
            FiguraPosixy(300,20);
            CreaDatos("G",2, Figura);
        }
        
        //Crea la  cuatro verde .....
        if(Figura==3){
            FiguraPosixy(260,40);
            FiguraPosixy(280,40);
            FiguraPosixy(280,20);
            FiguraPosixy(300,20);
            CreaDatos("C",2, Figura);
        }
        
        //Crea la  ELE NARANJA ......
        if(Figura==4){
            FiguraPosixy(280,40);
            FiguraPosixy(300,40);
            FiguraPosixy(320,40);
            FiguraPosixy(320,20);
            CreaDatos("E",2, Figura);
        }
        
        //Crea la  cuatro Rojo .....
        if(Figura==5){
            FiguraPosixy(280,20);
            FiguraPosixy(300,20);
            FiguraPosixy(300,40);
            FiguraPosixy(320,40);
            CreaDatos("F",2, Figura);
        }
        
        //Crea la  Te morada .....
        //correccion de la Te morada encontrado y corregida con exito 
        
        if(Figura==6){
            FiguraPosixy(260,40);
            FiguraPosixy(280,40);
            FiguraPosixy(280,20);
            FiguraPosixy(300,40);
            CreaDatos("D",2, Figura);
        }
    }
    
    //Metodo que estabece la posicion incial de la figura y sus caracteristicas ......    
    public void CreaDatos (String tipo, int estado, int Figura){
        for( int x=0;x<4;x++){
            Figura_Estado.add(estado);
            Figura_Tipo.add(tipo);
            Figura_2D.add(Figura2D.get(Figura));
        }
        Genera_Movimiento();
    }
    
    //Metodo que crea el movimiento de la figura dentro de un Thread
    public void Genera_Movimiento(){
        Thread moverFigura=new Thread(()->{ 
            VelocidadMemoria=Velocidad;
            while(!Final_Carrera &&  !FindelJuego){
                Evaluar_Figuras("A");
                NuevoCiclo=false;
                try{Thread.sleep(Velocidad);}catch(InterruptedException e){
                }
                NuevoCiclo=true;
                Velocidad=VelocidadMemoria;
                
            }//Fin del While; :"v
            if(!FindelJuego){
            
                Velocidad=VelocidadMemoria;
                Final_Carrera=false;
                CambiaEstado();
                EvaluarLineas();//Este metodo aun no esta;
                ReproAudio.Fx(2);
                CrearFigura(Figura_Ronda.get(TotalFiguras));
                BloqueaTeclado=false;
                IndiceFigura=IndiceFigura+4;
                BloqueaTecla_Rotar=false;
                BloqueaAbajo=false;
                
                Premio=Premio*Bonus;
                PuntosJuego=PuntosJuego+Premio;
                Bonus=-1;
                Premio=0;
            }
            
        });
        moverFigura.start();
    }
    
    //Este Metodo getter devuelve la dificultada del nivel de juego dependiendo de la cantidad de figuras totales
    public void setDificultad(){
        
        for (int figuras=0; figuras<1000; figuras= figuras+40){
            if (TotalFiguras == figuras){
                Velocidad=Velocidad-10;
                Level_Map++;//Modificacion Level_Mapp
                
            }
        }
    }
    
    //El metodo encargado de poner a la escucha las teclas que permiten la Jugabilidad.
    public void EventosAccionEscucha(){//EventosAccionesEscucha por...
        //Implementacion de acciones del juego al presionar las teclas
        
        addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e){
                    
                    int TeclaCode = e.getKeyCode();
                    //Barra spaciadora ......
                    if(TeclaCode==32){
                        if (Evaluar_Figuras("R") && !Pausar && !BloqueaTecla_Rotar){
                            ReproAudio.Fx(4);
                            Rot_Fig.Rotar_Figuras();
                            //ReproAudio.FxStop(7);
                        }
                    }
        
                    //Scape Pausar Juego....
                    if (TeclaCode==27){
                        Pausar=!Pausar;
                        if(Pausar==true){
                            ReproAudio.Fx(7);
                        }else{
                            ReproAudio.Fx(6);
                        }
                        
                    }
                    
                }
                @Override
                public void keyPressed(KeyEvent e){
                    int TeclaCode = e.getKeyCode();
                    //Tecla Cursor derecho.....
                    if(TeclaCode == 39 && !BloqueaTeclado && !Pausar){
                        ReproAudio.Fx(3);
                        Desplazamiento_Lateral(increment, "D");
                         
                    }
                    //Tecla Cursor izquierdo.....
                    if(TeclaCode == 37 && !BloqueaTeclado && !Pausar){
                        ReproAudio.Fx(3);
                        Desplazamiento_Lateral(decrement, "I");
                    }
                    
                    //Tecla Cursos Abajo....
                    if(TeclaCode == 40 && !BloqueaAbajo && !Pausar){
                        Desplazamiento_Abajo(increment);
                    }
                        
                }
            });
                
    
    }
    
    //Incrementamos el desplazamiento lateral de la figura tanto en izquierda como derecha
    public void Desplazamiento_Lateral(int accion, String lados){
        if(Evaluar_Figuras(lados)){
            int index=0;
            for(int Estado:Figura_Estado){
                
                if(Estado==2){
                    Figura_PosiX.set(index,getFx(index)+accion); 
                    } 
                index++;
          
            }
           
        }   
    }
    
    //Incrementamos el desplazamiento de la figura hacia abajo    
    public void Desplazamiento_Abajo(int  accion){
        if(Evaluar_Figuras("B")){
            int index=0;
            for(int Estados:Figura_Estado){
                if(Estados==2){
                    Figura_PosiY.set(index, getFy(index)+accion);
                }
                index++;
            }
        }
             
    }
        
    /*El metodo mas importante del juego evalua todos los posibles movimientos y rotaciones de la figura,
    devolviento true o false*/
    public boolean Evaluar_Figuras(String Direccion){
        Rectangle Figuras_Estaticas=null;
        Rectangle N_F_abajo=null;
        Rectangle N_F_derecha=null;
        Rectangle N_F_izquierda=null;
        Rectangle N_F_CercaAbajo=null;
        boolean EsNull=true;
        int index=0;
        for(int Estado:Figura_Estado){
            //Evalua la condicion que la figura este solamente en movimiento.
            if(Estado==1){
                Figuras_Estaticas =new Rectangle(getFx(index),getFy(index),20,20);
            }
            int Proxi_PosX;
            int Proxi_PosY;
            if(Rot_Fig.ProximaRotacion(getFtipo())!=null&& Direccion.equals("R")){
                ArrayList<Rectangle> ProximaRotacion =Rot_Fig.ProximaRotacion(getFtipo());
                for(int ind=0;ind<4;ind++){
                    try{
                        Rectangle rec=ProximaRotacion.get(ind);
                        Proxi_PosX=(int) rec.getX()*20+getFx(ind+IndiceFigura);
                        Proxi_PosY=(int) rec.getY()*20+getFy(ind+IndiceFigura);//cambio x por y
                        
                        Rectangle ProximaR=new Rectangle(Proxi_PosX,Proxi_PosY,20,20);
                        //Evaluamos nuestra proxima rotacion con la parte izquierda del juego
                        if(ProximaR.intersects(new Rectangle(180,0,20,getHeight()))){
                            return false;
                        }
                        //Evaluamos nuuestra proxima rotacion con la parte derecha del juego
                        if(ProximaR.intersects(new Rectangle(400,0,20,getHeight()))){
                            return false;
                        }
                        //Evaluamos nuestra proxima rotacion con la parte inferior del juego
                        if(ProximaR.intersects(new Rectangle(200,660,200,60))){
                            return false;
                        }
                        //Evaluamos nuestra proxima rotacion on las figuras estaticas
                        if(Figuras_Estaticas!=null){
                            if(ProximaR.intersects(new Rectangle(Figuras_Estaticas))){
                                  return false;
                            
                            }
                        }
                    }catch(NullPointerException e){System.out.println("Error"+Arrays.toString(e.getStackTrace()));}
                }
            }
            //Evalua la condicon que la figura este solamente en movimiento
            if(Estado==2){
                EsNull=false;
                //Almacenamos nuestra Figura de indice en un variable rectangle
                N_F_abajo =new Rectangle(getFx(index),getFy(index)+20,20,20);
                N_F_CercaAbajo =new Rectangle(getFx(index),getFy(index)+40,20,20);
                N_F_derecha =new Rectangle(getFx(index)+20,getFy(index),20,20);
                N_F_izquierda =new Rectangle(getFx(index)-20,getFy(index),20,20);
            }
            //Evalua partes de la pantalla los dos laterales y la parte inferior..
            //Recorremos todos el array en busca de alguna Figura coincida con nuestra figura
            for(int x=0;x<getFsice();x++){
                Rectangle ParteInferior= new Rectangle(200,660,200,60);
                Rectangle ParteIzquierda=new Rectangle(180,0,30,getHeight());
                Rectangle ParteDerecha=new Rectangle(380,0,20,getHeight());
                
                if(!EsNull){
                    if(N_F_abajo.intersects(ParteInferior)){
                        Velocidad=VelocidadCercania;
                        
                    }
                    //Evaluamos la parte izquierdad cuando baja con las teclas
                    if(N_F_abajo.intersects(ParteIzquierda) && Direccion.equals("I") && !Final_Carrera){
                        return false;
                    }
                    //Evaluamos la parte derecha cuando bajan las teclas...
                    if(N_F_abajo.intersects(ParteDerecha) && Direccion.equals("D") && !Final_Carrera){
                        return false;
                    }
                    //Cuando la figura baja con las teclas abajo y coincide con la Figura de abajo....
                    if(N_F_abajo.intersects(ParteInferior) && Direccion.equals("B") && !Final_Carrera){
                        return false;
                    }
                    //Evaluamos la parte inferior cuando baja esta Automaticamente
                    if(N_F_abajo.intersects(ParteInferior) && !Final_Carrera&& NuevoCiclo){
                        Final_Carrera=true;
                        BloqueaAbajo=true;
                        BloqueaTecla_Rotar=true;
                        BloqueaTeclado=true;
                        return false;
                    }
                }//Fin EsNull
                
             //Evalua las figuras movimientos con teclas y las propia caida de las figuras por la patnalla
             //Evalua la condicion de las iguras Estaticas del programa 1.
              if(getEstado(x)==1){
                Rectangle Figura=new Rectangle(getFx(x),getFy(x),20,20);
                if(!EsNull){
                    if(N_F_CercaAbajo.intersects(Figura)){
                        Velocidad=VelocidadCercania;
                    }
                    //Evakuamos la parte derecha cuando bajas con la teclas...
                    if(N_F_izquierda.intersects(Figura)&& Direccion.equals("I") && !Final_Carrera){
                    return false;
                    
                    }
                    if(N_F_derecha.intersects(Figura)&& Direccion.equals("D") && !Final_Carrera){
                    return false;
                    
                    }
                    if(N_F_abajo.intersects(Figura)&& Direccion.equals("B") && !Final_Carrera){
                    return false;
                    
                    }
                    //Cuando la figura baja automaticamente y coincide con la figura de abajo.
                    if(N_F_abajo.intersects(Figura)&& !Final_Carrera && NuevoCiclo){
                       Final_Carrera=true;
                       BloqueaAbajo=true;
                       BloqueaTecla_Rotar=true;
                       BloqueaTeclado=true;
                       return false;
                
                   }
                }//Fin EsNull
              }//Fin  de Evalua Estado 1 Estatico figuras en no movimiento.
        
            }//Fin del Bucle For X.
            index++;
        }//Fin Estado_Figura....
        if(!Pausar &&  !Final_Carrera && Direccion.equals("A")){
            int indx=0;
            for(int Status:Figura_Estado){
                if(Status==2){
                    Figura_PosiY.set(indx, getFy(indx)+20);
                }
                indx++;
            }
        }
        return true;
    }
    
    
    public void EvaluarLineas(){
        Bonus++;
        for(int Posicion_Y=640;Posicion_Y>60;Posicion_Y-=20){
            int indx=0;
            int Cantidad=0;
            ArrayList<Integer> FiguraIndice=new ArrayList<>();
            try{
                for(int Estado:Figura_Estado){
                    if(Estado==1){
                        int Ultima_PosicionY=Posicion_Y;
                        if(Posicion_Y==getFy(indx)){
                        Cantidad++;
                        FiguraIndice.add(indx);

                        }
                        if(getFy(indx)==40&& !FindelJuego){
                        FindelJuego=true;
                        ReproAudio.Fx(0);
                        ReproAudio.Fx(6);
                        
                        }
                        if(Cantidad==10){
                            Limpiar_Linea(FiguraIndice,Ultima_PosicionY);
                            Cantidad=0;
                            FiguraIndice.clear();
                            EvaluarLineas();
                        
                        }
                    
                
                }
                    indx++;
                }
            
            }catch(ConcurrentModificationException e){
                System.out.println(e.getStackTrace());
                
            }
        }
        int longitud=Figura_Estado.size();
        for(int x=0;x<longitud;x++){
            try{
                int Estado=Figura_Estado.get(x);
                if(Estado==0){
                    Figura_2D.remove(x);
                    Figura_Estado.remove(x);
                    Figura_PosiX.remove(x);
                    Figura_PosiY.remove(x);
                    Figura_Tipo.remove(x);
                    IndiceFigura=IndiceFigura-1;
                    
                    
                }
            }catch(IndexOutOfBoundsException e){}
        }
    } 
    
    
    public void Limpiar_Linea(ArrayList<Integer> figura, int Ultima_PosicionY){
        for(int fig:figura){
            Figura_Estado.set(fig,0);
        
        }
        int indx=0;
        for(int Est:Figura_Estado){
            if(Est==1 && Ultima_PosicionY>=getFy(indx)){
                Figura_PosiY.set(indx,getFy(indx)+20);
            }
            indx++;
        }
        ContadorLineas++;
        ReproAudio.Fx(1);
        Premio=Premio+100;
                
    }
    
    
    //Metodo que establece la posicion inicial de las figuras y sus caracteristicas
    public void FiguraPosixy( int PosiX, int PosiY){
            
        Figura_PosiX.add(PosiX);
        Figura_PosiY.add(PosiY);

    }
    //metodo que crea el movimiento de la figura dentro de un Thread.
    public int GenerarNumero(){
        int Figura=(int)(Math.random()*7);
        return Figura;
    }
   
    public void CambiaEstado(){
            for (int x=0;x<getFsice();x++){
                if(getEstado(x)==2){
                    Figura_Estado.set(x,1);
                }
            }
        }
        
    public String getFtipo(){
            
            String tipo="";
            int index=0;
            for(String T:Figura_Tipo){
                if (Figura_Estado.get(index)==2){
                    tipo=T;
                }
                index++;
            }
            
            return tipo;
        }
        
        public int getFsice(){
            return Figura_2D.size();
        }
        public int getFx(int indx){
            
            return Figura_PosiX.get(indx);
        }
        
        public int getFy(int indx){
            
            return Figura_PosiY.get(indx);
        }
        
        public int getEstado(int indx){
            
            return Figura_Estado.get(indx);//cambie por //
        }
        
    //Metodo encargado de crear el Bucle infinito con refresco.
        
    public void CrearBucle(){
        Thread hilo=new Thread(()->{
            while(!FindelJuego){
                try{
                    Thread.sleep(33);
                }catch(InterruptedException ex){}
                
                repaint();
                }
            });
        hilo.start();
    }
        
    }

