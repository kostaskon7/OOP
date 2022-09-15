/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;


public class YUVPixel {
    private int pixel;
    
    
    public YUVPixel(short Y, short U, short V){
        
        pixel = Y;
	pixel = pixel << 8;

	pixel += U;
	pixel = pixel << 8;

	pixel += V;
        
    }
    
    public YUVPixel(YUVPixel pixel){
        this.pixel=pixel.pixel;
    }
    
    public YUVPixel(RGBPixel pixel){
        
        int R=pixel.getRed();
        int G=pixel.getGreen();
        int B=pixel.getBlue();
        
        setY ((short)(( (  66 * R + 129 * G +  25 * B + 128) >> 8) +  16));
        setU ((short)(( ( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128));
        setV ((short)(( ( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128));


    }
    
    public short getY(){
        Integer Y=pixel&(0x00ff0000);
        Y=Y>>16;
        return(Y.shortValue());
    }
    
    public short getU(){
        Integer U=pixel&(0x0000ff00);
        U=U>>8;
        return(U.shortValue());
    }
    public short getV(){
        Integer V=pixel&(0x000000ff);
        return(V.shortValue());
    }
    
    public void setY(short Y){
        int temp=Y;
        temp=temp<<16;
        pixel=(pixel&0xff00ffff)|temp;
        
    }
    
    public void setU(short U){
        int temp=U;
        temp=temp<<8;
        pixel=(pixel&0xffff00ff)|temp;
        
    }
    public void setV(short V){
        int temp=V;
        pixel=(pixel&0xffffff00)|temp;
        
    }

}
