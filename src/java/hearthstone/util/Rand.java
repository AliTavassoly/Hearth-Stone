package hearthstone.util;

import java.util.ArrayList;
import java.util.Random;

public class Rand {
    private static Rand rand;
    private Random random;

    private Rand(){
        random = new Random(System.currentTimeMillis());
    }

    public static Rand getInstance(){
        if(rand == null){
            return rand = new Rand();
        } else {
            return rand;
        }
    }

    public boolean getProbability(int a, int b){
        int ap = random.nextInt(b);
        return ap < a;
    }

    public int getRandomNumber(int n){
        return random.nextInt(n);
    }

    public ArrayList<Integer> getRandomArray(int cnt, int max){
        ArrayList<Integer> ans = new ArrayList<>();
        for(int i = 0; i < cnt; i++){
            int randomNumber;
            while(true){
                randomNumber = random.nextInt(max);
                if(!ans.contains(randomNumber)){
                    ans.add(randomNumber);
                    break;
                }
            }
        }
        return ans;
    }
}
