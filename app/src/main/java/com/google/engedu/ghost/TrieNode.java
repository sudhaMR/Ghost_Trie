package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord,isSubstr,isLeaf;
    private char c;
    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        isLeaf = false;
    }

    public void add(String s) {
        TrieNode node = this;
        Character ch=null;
        int i;
        for(i = 0; i <s.length();i++){
            ch = s.charAt(i);

            if(!node.children.containsKey(ch)){
                node.children.put(ch, new TrieNode());
            }
            node.isWord = false;

            /*
            When in last iteration of loop, add the last letter,
            Set node as the end node (isWord)
             */
            if(i==s.length()-1) {
                node.isLeaf = true;
                node.isWord = true;
            }
            node = node.children.get(ch);
        }
    }

    public boolean search(String s){
        TrieNode t = this;
        boolean wordStatus = false;
        int i;
        for(i = 0 ; i<s.length() ; i++) {
            Character ch = s.charAt(i);
            if (t.children.containsKey(ch)) {
            }
            else {
                return false;
            }

            t = t.children.get(ch);
            wordStatus = t.isWord;

        }
        if(wordStatus) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean searchSubstring(String s){
        TrieNode t = this;
        for(int i = 0 ; i<s.length() ; i++) {
            Character ch = s.charAt(i);
            if (t.children.containsKey(ch)) {
                t.isSubstr = true;
            }
           else {
                t.isSubstr = false;
                return false;
            }
            t = t.children.get(ch);

        }
        return true;
    }

    public boolean isWord(String s) {
        return search(s);
    }

    public boolean isSubstring(String s) {
        return searchSubstring(s);
    }

    public String getAnyWordStartingWith(String s) {
        HashMap<Character,TrieNode> child = children;
        TrieNode t = null;
        String str = "";

        str.concat(s);
        /*
            Check if string s is a string of valid prefix,
            i.e, key exists
         */
        for(int i = 0 ; i< s.length() ; i++){
            Character ch = s.charAt(i);

            if(child.containsKey(ch)){
                t = child.get(ch);
                str = str+Character.toString(ch);
                child = t.children;
            }
            else return null;
        }

        /*
            Add the next possible letter by adding a-z,
            and checking if the word is valid,
            break and return when a valid longer word is found
         */
       for(char i = 'a' ; i <= 'z' ; i++){
           if(child.containsKey(i)){
               str = str + Character.toString(i);
               t =  child.get(i);
               child = t.children;
               i = 'a';
               if(isWord(str)) {
                    break;
               }
           }
       }
        return str;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
