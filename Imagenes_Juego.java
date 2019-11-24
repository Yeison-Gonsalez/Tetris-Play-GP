
package tetrisjuego;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Imagenes_Juego {

    public void CargarFiguras(ArrayList<BufferedImage> ArrayImg) {
        ObtenerArchivos(ArrayImg,"Graficos/Figuras/","png");  
    }
    public void CargarListaFiguras(ArrayList<BufferedImage> ArrayImg){
        ObtenerArchivos(ArrayImg, "Graficos/ListaFiguras/","png");
    }
    public void CargarLetras(ArrayList<BufferedImage> ArrayImg){
        ObtenerArchivos(ArrayImg,"Graficos/Letras/","png");
    }
    public void CargarNumeros(ArrayList<BufferedImage> ArrayImg){
        ObtenerArchivos(ArrayImg,"Graficos/Numeros/","png");
    }
    public void CargarFondos(ArrayList<BufferedImage> ArrayImg){
        ObtenerArchivos(ArrayImg,"Graficos/Fondos/","png");
    }
    
    public void ObtenerArvhivos(ArrayList<BufferedImage> ArrayImg, String ruta,String extension){
         File archivos=new File(ruta).getAbsoluteFile();
         
         String lista_archivos[]=archivos.list();
         
         if(lista_archivos!=null){
            for(String arch:lista_archivos){
                int longi=arch.lastIndexOf('.');
                int long1=arch.length();
                String ob_Ext=arch.substring(longi+1 , long1);
                if(ob_Ext.equals(extension)){
                    try{
                        String rutt=archivos.getAbsoluteFile()+"/"+arch;
                        BufferedImage Img=ImageIO.read(new File(rutt));
                        ArrayImg.add(Img);
                    
                    }catch(IOException ex){
                        System.out.println("Error ruta..."+ rutt);
                        Logger.getLogger(Imagenes_Juego.class.getName()).log(Leve1.SEVERE,null, ex);
           
                    }
                }
            }
        }else{
             System.out.println("Lista_archivos esta vacia los directoris no contienen archivos: "+ruta);
         }
    
        
    }
}
