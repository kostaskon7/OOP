/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;


public class PPMImageStacker  {
    LinkedList<PPMImage> images;
    RGBImage stacked;
    
    public PPMImageStacker(java.io.File dir) throws UnsupportedFileFormatException,FileNotFoundException{
        
        PPMFileFilter ff=new PPMFileFilter();
        images = new LinkedList<>(); 
        if(!dir.exists())
           throw new FileNotFoundException("[ERROR] Directory "+ dir.getName()+" does not exist!");
        
        if(!ff.accept(dir)){
            throw new UnsupportedFileFormatException("[ERROR] "+dir.getName()+" is not a directory!");
        }
        String[] fileList = dir.list();
        for(String file : fileList){
             if(file.endsWith(".ppm")){
                 System.out.println(dir.getPath()+file);
                 try{
                     images.add(new PPMImage(new File(dir.getPath()+"/"+file)));
                 }
                 catch(FileNotFoundException ex) {
                     System.out.println(ex.getMessage());
                 }
                 catch(UnsupportedFileFormatException ex) {
                     System.out.println(ex.getMessage());
                 }
             }
        }
    }
    
    public void stack(){
       int width=images.get(1).getWidth();
       int height=images.get(1).getHeight();
       int length=images.size();
       int avgRGB=0;
       
        stacked=new RGBImage(width,height,255);
        for(int i=0; i<height;i++){
            for(int j=0; j<width;j++){
                for(PPMImage img : images){
                    avgRGB=avgRGB+ img.pic[i][j].getRGB();
                }
                stacked.pic[i][j].setRGB(avgRGB/length);
                avgRGB=0;
            }
        }
    }
    public PPMImage getStackedImage(){
        return new PPMImage(stacked);
    }
}
