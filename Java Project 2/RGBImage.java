/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;


public class RGBImage implements Image {
    RGBPixel pic[][];
    static final int MAX_COLORDEPTH=255;
    int colordepth;
    public RGBImage(){}
    
    public RGBImage(int width, int height, int colordepth){
        pic=new RGBPixel [height][width];
        for(int i=0;i<pic.length;i++){
            for(int j=0;j<pic[0].length;j++){
                pic[i][j]= new RGBPixel((short)0,(short)0,(short)0);
            }
        }
        this.colordepth=colordepth;
    }
    public RGBImage(RGBImage copyImg){
        int i,j;
        pic=new RGBPixel [copyImg.pic.length][copyImg.pic[0].length];
        this.colordepth=copyImg.colordepth;
       
        for( i=0;i<pic.length;i++){
            for(j=0;j<pic[0].length;j++){
                
                this.pic[i][j]=new RGBPixel(copyImg.pic[i][j]);
            }
        }
    }
    public RGBImage(YUVImage YUVImg){
        pic=new RGBPixel [YUVImg.pic.length][YUVImg.pic[0].length];
        for(int i=0;i<pic.length;i++){
            for(int j=0;j<pic[0].length;j++){
                pic[i][j]=new RGBPixel(YUVImg.pic[i][j]);
            }
        }
    }
    
    int getWidth(){
        return(pic[0].length);
    }
    int getHeight(){
        return(pic.length);
    }
    int getColorDepth(){
        return(colordepth);
    }
    RGBPixel getPixel(int row, int col){
        return(pic[row][col]);
    }
    void setPixel(int row, int col,RGBPixel pixel){
        pic[row][col]=pixel;
    }
    
    public void grayscale(){
        int row, col;
	short gray;
	
	for(row=0;row < getHeight();row++) {
            for(col=0;col < getWidth();col++) {
                gray =(short) (pic[row][col].getRed()*0.3 + pic[row][col].getGreen()*0.59 +pic[row][col].getBlue() *0.11);
                pic[row][col].setRGB(gray,gray,gray);
            }
	}
    }
    public void doublesize(){
        
        RGBPixel temp[][]=new RGBPixel [getHeight()*2][getWidth()*2];
       
        
        for(int row=0; row <getHeight();row++) {
            for(int col=0; col< getWidth();col++) {
                temp[2*row][2*col] = new RGBPixel(pic[row][col]);
                temp[(2*row)+1][2*col] =new RGBPixel( pic[row][col]);
                temp[2*row][(2*col)+1] =new RGBPixel( pic[row][col]);
                temp[(2*row)+1][(2*col)+1] =new RGBPixel( pic[row][col]);
            }
        }
        pic=temp;
    }
    public void halfsize(){
        RGBPixel temp[][]=new RGBPixel [getHeight()/2][getWidth()/2];
     
        short red,green,blue;
        for(int i=0;i<getHeight()/2;i++){
            for(int j=0;j<getWidth()/2;j++){
                temp[i][j]= new RGBPixel((short)0,(short)0,(short)0);
            }
        }

       for(int row=0; row <getHeight()/2;row++) {
            for(int col=0; col< getWidth()/2;col++) {
                red = (short) (pic[2*row][2*col].getRed()+pic[(2*row)+1][2*col].getRed());
                red+=pic[2*row][(2*col)+1].getRed()+pic[(2*row)+1][(2*col)+1].getRed();
                temp[row][col].setRed((short)(red/4));
                
                green = (short) (pic[2*row][2*col].getGreen()+pic[(2*row)+1][2*col].getGreen());
                green+=pic[2*row][(2*col)+1].getGreen()+pic[(2*row)+1][(2*col)+1].getGreen();
                temp[row][col].setGreen((short)(green/4));
                
                blue = (short) (pic[2*row][2*col].getBlue()+pic[(2*row)+1][2*col].getBlue());
                blue+=pic[2*row][(2*col)+1].getBlue()+pic[(2*row)+1][(2*col)+1].getBlue();
                temp[row][col].setBlue((short)(blue/4));
            }
       }
        pic=temp;
    
    
    }
    public void rotateClockwise(){
    
        int row, col;
	RGBPixel temp[][]=new RGBPixel [getWidth()][getHeight()];

	
	for(row=0;row < getHeight();row++) {
            for(col=0;col < getWidth();col++) {
                temp[col][getHeight() - 1 - row]=new RGBPixel(pic[row][col]);
            }
	}
	
        pic=temp;
    }
}
