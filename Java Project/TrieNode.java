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
public class TrieNode {
    TrieNode children [];
    boolean isTerminal;

    public TrieNode(){
          children = new  TrieNode[26] ;
          isTerminal=false;
   }
    
    
}
