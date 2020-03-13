package hearth.stone.credentials;

public class Crypt {
    static long hash(String password){
        long ans = 0, mod = 1000 * 1000 * 1000 + 7, base = 727, zarib = 1;
        for(int i = 0; i < password.length(); i++){
            ans += (zarib * password.charAt(i));
            ans %= mod;
            zarib *= base;
        }
        return ans;
    }
}
