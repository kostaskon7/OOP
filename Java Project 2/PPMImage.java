/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.*;
import java.util.Scanner;

public class PPMImage extends RGBImage {
    
    public PPMImage(java.io.File file) throws UnsupportedFileFormatException,FileNotFoundException{ 
        super();
        int i,j;
        Scanner sc;
        String str;
  
        PPMFileFilter ff=new PPMFileFilter();
        
        if(!file.exists() || !file.canRead())
           throw new FileNotFoundException();
        
        if(!ff.accept(file)){
            throw new UnsupportedFileFormatException("Not a .ppm file");
        }
        
        sc = new Scanner(file);
        if(sc.hasNext()){
            str=sc.next();
            if(!str.equals("P3")){
                throw new UnsupportedFileFormatException("Not rigth format");
            }
            if(sc.hasNext()){
                int width=sc.nextInt();
                if(sc.hasNext()){
                    int height=sc.nextInt();
                    if(sc.hasNext()){
                        int depth=sc.nextInt();
                        super.pic=new RGBPixel [height][width];
                        super.colordepth=depth;
                        
                    }
                }
            }
        }
        i=0;
        j=0;
        short red,green,blue;
        while(sc.hasNext()) {
            if(j==getWidth()){
                i++;
                j=0;
            }
            red=sc.nextShort();
            green=sc.nextShort();
            blue=sc.nextShort();
            pic[i][j]=new RGBPixel(red,green,blue);
            j++;
            
        }
      
    }
    public PPMImage(RGBImage img){   
        super(img);
        
    }
    public PPMImage(YUVImage img){
        super(img);
    }
    
  @Override
    public String toString(){
        int height=super.getHeight();
        int width=super.getWidth();
        
        StringBuilder str=new StringBuilder("P3\n"+width+" "+height+"\n"+colordepth+"\n");
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                str.append(pic[i][j].getRed()).append(" ").append(pic[i][j].getGreen()).append(" ").append(pic[i][j].getBlue()).append("\n");
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
}
