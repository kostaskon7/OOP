/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Histogram {
    double eq[];
    int luminocity[];
    int NumberOfPixel;
    
    public Histogram(YUVImage img){
        eq= new double[236];
        luminocity=new int[236];
        NumberOfPixel=img.pic.length * img.pic[0].length;
        for(int i=0;i<img.pic.length;i++){
            for(int j=0;j<img.pic[0].length;j++){
                eq[img.pic[i][j].getY()]++;
            }
        }
    }
    @Override
    public String toString(){
       String str=null;
       String symbols=null;
       int length=0;
       for(int i=0;i<236;i++){
           str=luminocity[i]+"  ";
           symbols=String.valueOf(luminocity[i]);
           length=symbols.length();
           for(int j=0;j<Integer.parseInt(symbols.substring(0,length-3));j++){
            str=str +"#";
           }
           for(int j=0;j<Integer.parseInt(symbols.substring(length-3,length-2));j++){
            str=str +"$";
           }
           for(int j=0;j<Integer.parseInt(symbols.substring(length-2,length));j++){
            str=str +"*";
           }
           str=str +"\n";
       }
       
       return str;
    }
    public void toFile(java.io.File file){
        try (PrintWriter out = new PrintWriter(file)) {
            out.print(toString());
        }
        catch(FileNotFoundException ex){
            System.out.println("File not found");
        }
    }
    public void equalize(){
        int i;
        
        eq[0]=eq[0]/NumberOfPixel;
        for(i=1;i<236;i++){
            eq[i]=eq[i]/NumberOfPixel;
            eq[i]=eq[i]+eq[i-1];
            
        }
        for(i=0;i<236;i++){
            eq[i]=eq[i]*(double)235;
            luminocity[i]=(int)eq[i];
        }

    }
    public short getEqualizedLuminocity(int luminocity){
        return((short)luminocity);
    }  
    
}
