/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw1;

/**
 *
 * @author Kostas
 */
public class Trie {
    static TrieNode root;
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    static int wordsAdded=0,count=0;
    String[] difference;

    public Trie(String []words){
        root=new TrieNode();
        for(int i=0;words[i]!=null;i++){
            add(words[i]);
        }
    }

    public static boolean add(String word){


        int length = word.length();
        int pos;

        TrieNode current = root;

        for (int level = 0; level < length; level++)
        {
            pos = word.charAt(level) - 'a';
            if (current.children[pos] == null){
                current.children[pos] = new TrieNode();
            }

            current = current.children[pos];
        }
        if(!(current.isTerminal)){
            wordsAdded++;
        }
        current.isTerminal = true;
        return(true);
    }

    public boolean contains(String word){

        int pos;
        TrieNode current = root;

        for (int level = 0; level < word.length(); level++)
        {
            pos = word.charAt(level) - 'a';

            if (current.children[pos] == null)
                return false;

            current = current.children[pos];
        }
        return (current.isTerminal);
    }


    public int size(){
        return(wordsAdded);
    }
    
    
    public void differBy_cont(int goal,int dif,int pos,TrieNode current,String word){        
        
        
    if((dif>=0)&&(goal>=dif)&&(word.length()==pos)&&(current.isTerminal==true)){

        difference[count]=word.substring(0,pos);
        count++;
    }

    for(int i=0;i<alphabet.length();i++){
            if((current.children[i]!=null)&&(word.length()>pos)){
                if(word.charAt(pos)!=((char)('a'+i))){
                    differBy_cont(goal,dif+1,pos+1,current.children[i],word.substring(0,pos)+((char)('a'+i))+word.substring(pos+1,word.length()));//edwwwwwwww
                }
                else{
                    differBy_cont(goal,dif,pos+1,current.children[i],word);
                }
            }
        }
    
    }
    
    
    public String[] differBy(String word, int max){
        difference=new String[wordsAdded];
        differBy_cont(max,0,0,root,word);
        return(difference);

    }

     public String[] differByOne(String word){
        difference=new String[wordsAdded];
        differBy_cont(1,0,0,root,word);
        return(difference);

    }

    

    public void display(TrieNode current){
        
        if(current.isTerminal){
            difference[0]=difference[0]+"!";
        }
        
        
        for(int i=0;i<alphabet.length();i++){
            if(current.children[i]!=null){
                difference[0]=difference[0]+' '+((char)('a'+i));
                //System.out.println(difference[0]);
                display(current.children[i]);
            }
        }

    }

    public String toString() {
        difference=new String[1];
        difference[0]="";
        display(root);
        return(difference[0]);
    }

    public boolean isEnd(TrieNode current){

        for(int i=0;i<alphabet.length();i++){

            if(current.children[i]!=null){
                return(false);
            }
        }
        return(true);
    }

    public void wordsOfprefix_cont(TrieNode current,String prefix){

    if (current.isTerminal) {
    difference[count]=prefix;
    count++;
    }

    if (isEnd(current)){
        return;
    }

    for (int i = 0; i < alphabet.length(); i++) {
        if (current.children[i]!=null){

            wordsOfprefix_cont(current.children[i],prefix+((char)('a'+i)));

        }
    }
}
    
    public void toDotString_cont(TrieNode current){
        char t='"';
        
        for(int i=0;i<alphabet.length();i++){
            if(current.children[i]!=null){ 
                difference[0]=difference[0]+current.hashCode()+" -- "+current.children[i].hashCode()+"\n\t";
                difference[0]=difference[0]+current.children[i].hashCode();
                difference[0]=difference[0]+" [label=";
                difference[0]=difference[0]+t+((char)('a'+i))+t;
                difference[0]=difference[0]+" ,shape=circle";
                if(current.children[i].isTerminal){
                    difference[0]=difference[0]+", color=red]\n\t";
                }
                else{
                    difference[0]=difference[0]+", color=black]\n\t";
                }
                toDotString_cont(current.children[i]);
            }           
                
        }
    
    
    
    }
    
    public String toDotString(){
        char t='"';
        difference=new String[1];
        difference[0]="Graph Trie {\n\t";
        difference[0]=difference[0]+root.hashCode();
        difference[0]=difference[0]+" [label=";
        difference[0]=difference[0]+t;
        difference[0]=difference[0]+"ROOT";
        difference[0]=difference[0]+t;
        difference[0]=difference[0]+" ,shape=circle, color=black]\n\t";
        toDotString_cont(root);
        difference[0]=difference[0]+'}';
        return(difference[0]);
    }


    public String[] wordsOfPrefix(String prefix){

        TrieNode current=root;
        int index;

        for (int level = 0; level < prefix.length(); level++) {
            index = prefix.charAt(level)-'a';

            if (current.children[index]==null) {
                return difference;
            }

            current = current.children[index];
        }

        difference=new String[wordsAdded];
        wordsOfprefix_cont(current,prefix);
        
        return(difference);
    }



}
