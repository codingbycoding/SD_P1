/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author onezero
 */
public class TestUtils {
    
    public static int get5To8() {
        List<Integer> arrList = new ArrayList<Integer>();
        for (int i = 5; i <= 8; i++) {        
            arrList.add(i);
        }

        Collections.shuffle(arrList);
        return arrList.get(0);
    }
    
     public static ArrayList<Integer> shuffle0To9() {
        ArrayList<Integer> arrList = new ArrayList<Integer>();
        for(int i=0; i<=9; i++) {
             arrList.add(i);
        }

        Collections.shuffle(arrList);
        return arrList;
    }      
     
     public static ArrayList<Integer> randomWithWeight(int dotNum) {
         //static int 80 15 5; 1, 2, 3;      
         WeightNumber wn1 = new WeightNumber(80, 1);
         WeightNumber wn2 = new WeightNumber(15, 2);
         //WeightNumber wn3 = new WeightNumber(5, 3);
         
         ArrayList<WeightNumber> arrListWN = new  ArrayList<WeightNumber>();
         arrListWN.add(wn1);
         arrListWN.add(wn2);
         //arrListWN.add(wn3); 
        
         ArrayList<Integer> arrList = new ArrayList<Integer>();
         for (int i = dotNum; i > 0; i--) {             
             Random random = new Random(System.nanoTime());
             int ran = random.nextInt() % 100;
             for (int j = 0; j < arrListWN.size(); j++) {
                 ran -= arrListWN.get(j).weight;
                 if(ran < 0) {        
                     int n = 1;
                     if(0 == arrList.size()) {
                         n = dotNum - 1;
                     }
                     n = Integer.min(n, arrListWN.get(j).number);
                     n = Integer.max(n, 1);
                     arrList.add(n);
                     i = i - n + 1;
                     break;
                 }
             }
         }
        
         return arrList;
     }
}

class WeightNumber {
    WeightNumber(int weight, int number) {
        this.weight = weight;
        this.number = number;
    }
    int weight;
    int number;
}
