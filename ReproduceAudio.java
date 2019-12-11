package tetrisjuego;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class ReproduceAudio {
    
    private  ArrayList<File> EFX = new ArrayList<>();
    
    public ReproduceAudio(){
        
        introducirFx("Efecto0.wav");
        introducirFx("Efecto1.wav");
        introducirFx("Efecto2.wav");
        introducirFx("Efecto3.wav");
        introducirFx("Efecto4.wav");
        introducirFx("Efecto5.wav");
        introducirFx("Efecto6.wav");
        introducirFx("Efecto7.wav");
        
    }
    
    public void introducirFx (String Ruta){//FX
        try{
            File file = new File("").getAbsoluteFile();
            
            String rutt = file + "/Efectos/" + Ruta;
            file = new File(rutt);
            EFX.add(file);
            
        }catch(NullPointerException e){
            System.out.println("Error en la ruta o archivo no encontrado de audio...");
        }
    }
    
    public void Fx (int indice){
        
        try {
            File file = EFX.get(indice);
            
            Clip sonido = AudioSystem.getClip();
            
            sonido.open(AudioSystem.getAudioInputStream(file));
            
            sonido.start();
            
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(ReproduceAudio.class.getName()).log(Level.SEVERE, null, ex); 
        }

    }
}
