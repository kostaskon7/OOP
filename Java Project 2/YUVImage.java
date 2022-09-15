/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class YUVImage {
    YUVPixel pic[][];
    
    public YUVImage(int width, int height){
        pic=new YUVPixel [height][width];    
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                pic[i][j]=new YUVPixel((short)16,(short)128,(short)128);
            }
        }
    }
    public YUVImage(YUVImage copyImg){
        pic=new YUVPixel [copyImg.pic.length][copyImg.pic[0].length];
        for(int i=0;i<pic.length;i++){
            for(int j=0;j<pic[0].length;j++){
                pic[i][j]= new YUVPixel(copyImg.pic[i][j]);
            }
        }
    }
    public YUVImage(RGBImage RGBImg){
        pic=new YUVPixel [RGBImg.pic.length][RGBImg.pic[0].length];
        
        for(int i=0;i<pic.length;i++){
            for(int j=0;j<pic[0].length;j++){
                pic[i][j]=new YUVPixel(RGBImg.pic[i][j]);
            }
        }
    }
    public YUVImage(java.io.File file) throws FileNotFoundException, UnsupportedFileFormatException{
        Scanner sc;
        YUVFileFilter ff=new YUVFileFilter();
        String str;
        
        if(!file.exists())
           throw new FileNotFoundException();
        if(!ff.accept(file)){
            throw new UnsupportedFileFormatException("Not a .yuv file");
        }
        
        sc = new Scanner(file);
            if(sc.hasNext()){
                str=sc.next();
                if(!str.equals("YUV3")){
                    throw new UnsupportedFileFormatException("Not rigth format");
                }
                if(sc.hasNext()){
                    int width=sc.nextInt();
                    if(sc.hasNext()){
                        int height=sc.nextInt();
                        pic=new YUVPixel [height][width]; 
                    }
                }
            }
            int i=0;
            int j=0;
            short Y,U,V;
            while(sc.hasNext()) {
                if(j==pic[0].length){
                    i++;
                    j=0;
                }
                Y=sc.nextShort();
                U=sc.nextShort();
                V=sc.nextShort();
                
                pic[i][j]=new YUVPixel(Y,U,V);

                j++;
            }
    }
    
     @Override
    public String toString(){
        StringBuilder str=new StringBuilder("YUV3\n"+pic[0].length+" "+pic.length+"\n");
        for (YUVPixel[] pic1 : pic) {
            for (int j = 0; j<pic[0].length; j++) {
                str.append(pic1[j].getY()).append(" ").append(pic1[j].getU()).append(" ").append(pic1[j].getV()).append("\n");
            }
        }
        return(str.toString());
    }
    
    public void toFile(java.io.File file) {
        try (FileWriter out = new FileWriter(file,false);) {
            out.write(toString());
        }
        catch(FileNotFoundException ex){
            System.out.println("File not found");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        
    }
    public void equalize(){
        Histogram p=new Histogram(this);
        p.equalize();
        for(int i=0;i<pic.length;i++){
            for(int j=0;j<pic[0].length;j++){
                pic[i][j].setY(p.getEqualizedLuminocity(p.luminocity[pic[i][j].getY()]));
            }
        }
    }
}
