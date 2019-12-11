package tetrisjuego;
import java.util.Timer;//libreria para crear objeto Timer
import java.util.TimerTask;
public class Tiempo {
    ReproduceAudio Play=new ReproduceAudio();
    public void PlayMusica(){
        
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            
            public void run(){
                Play.Fx(7);
            }
        };
        timer.schedule(task, 0,29000);
        //  ( tiene que asignar una tarea , timepo de iniciacion , tiempo cel bucle a repetir) 
    }   
}