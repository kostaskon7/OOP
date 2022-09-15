/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;


public class RGBPixel {
    
    private int pixel;
    
    
    public RGBPixel(short red, short green, short blue){
        
        pixel = red;
	pixel = pixel << 8;

	pixel += green;
	pixel = pixel << 8;

	pixel += blue;
        
    }
    
    public RGBPixel(RGBPixel pixel){
        this.pixel=pixel.getRGB();
    }
    
    public RGBPixel(YUVPixel pixel){
        int C = pixel.getY() - 16;
        int D = pixel.getU() - 128;
        int E = pixel.getV() - 128;

        setRed(clip(( 298 * C + 409 * E + 128) >> 8));
        setGreen(clip(( 298 * C - 100 * D - 208 * E + 128) >> 8));
        setBlue(clip(( 298 * C + 516 * D + 128) >> 8));

    }
    
    public final short clip(int value){
        if(value<0){
            return 0;
        }
        else if(value>255){
            return 255;
        }
        return ((short)value);
    }
    
    public short getRed(){
        Integer red=pixel&(0x00ff0000);
        red=red>>16;
        return(red.shortValue());
    }
    
    public short getGreen(){
        Integer green=pixel&(0x0000ff00);
        green=green>>8;
        return(green.shortValue());
    }
    public short getBlue(){
        Integer blue=pixel&(0x000000ff);
        return(blue.shortValue());
    }
    
    public void setRed(short red){
        int temp=red;
        temp=temp<<16;
        pixel=(pixel&0xff00ffff)|temp;
        
    }
    
    public void setGreen(short green){
        int temp=green;
        temp=temp<<8;
        pixel=(pixel&0xffff00ff)|temp;
        
    }
    public void setBlue(short blue){
        int temp=blue;
        pixel=(pixel&0xffffff00)|temp;
        
    }
    public int getRGB(){
        return(pixel);
    }
    public void setRGB(int value){
        pixel=value;
    }
    public final void setRGB(short red, short green, short blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    @Override
    public String toString(){
        return(String.valueOf(getRed())+' '+String.valueOf(getGreen())+' '+String.valueOf(getBlue()));
        
    }
}
