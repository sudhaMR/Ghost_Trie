package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        int size,mid,first=0,last;
        size = words.size();
        last = size-1;
        if(prefix == null && prefix.isEmpty()){
        //if(prefix.equals("")){
            return words.get(random.nextInt(words.size()) + 1);
        }
        else if(words.contains(prefix)){
            /*int currentIndex = words.indexOf(prefix);
            currentIndex++;
            String nextWord = words.get(currentIndex);
            return nextWord;*/
            while(first<last){
                mid = (first + last)/2;

                if(words.get(mid).contains(prefix)){
                    return words.get(mid);
                }

                else if(words.get(mid).compareToIgnoreCase(prefix) < 0){
                    first = mid;
                }

                else if(words.get(mid).compareToIgnoreCase(prefix) > 0){
                    last = mid;
                }
            }//end of while

        }//end of outer else if
            return null;
    }

    @Override
    public boolean isSubstring(String word){
        if(word == null)
            return false;
        else {
            return words.contains(word);
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
