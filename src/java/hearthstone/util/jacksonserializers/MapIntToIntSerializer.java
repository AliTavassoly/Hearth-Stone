package hearthstone.util.jacksonserializers;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.HashMap;
import java.util.Map;

public class MapIntToIntSerializer extends StdConverter<Map<Integer, Integer>, Map<Integer, Integer>> {
    @Override
    public Map<Integer, Integer> convert(Map<Integer, Integer> integerIntegerMap) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(Integer integer: integerIntegerMap.keySet()){
            map.put(integer, integerIntegerMap.get(integer));
        }
        return map;
    }
}