package hearthstone.util.jacksonserializers;

import com.fasterxml.jackson.databind.util.StdConverter;
import hearthstone.models.Deck;
import hearthstone.models.hero.Hero;

import java.util.ArrayList;
import java.util.List;

public class IntegerListSerializer extends StdConverter<List<Integer>, List<Integer>> {
    @Override
    public List<Integer> convert(List<Integer> integerList) {
        ArrayList arrayList = new ArrayList<Integer>();
        for(Integer integer : integerList){
            arrayList.add(integer);
        }
        return arrayList;
    }
}
