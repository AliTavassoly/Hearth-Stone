package hearthstone.util;

import java.net.URL;
import java.net.URLClassLoader;

public class Reflection {
    public static Class getClassByName(String pathToJar, String className){
        Class ans = null;

        try {
            URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);

            ans = classLoader.loadClass(className);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ans;
    }
}
