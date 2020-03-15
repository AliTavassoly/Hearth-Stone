package hearthstone.util;

public class Crypt {
    public static int hash(String text){
        long ans = 0, mod = 1000 * 1000 * 1000 + 7, base = 727, zarib = 1;
        for(int i = 0; i < text.length(); i++){
            ans += (zarib * text.charAt(i));
            ans %= mod;
            zarib *= base;
        }
        return (int)ans;
    }
}
