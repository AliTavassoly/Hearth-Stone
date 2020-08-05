package hearthstone.util.jacksonserializers;

import com.fasterxml.jackson.databind.util.StdConverter;
import hearthstone.models.Deck;
import hearthstone.models.hero.Hero;

import java.util.ArrayList;
import java.util.List;

public class HeroListSerializer extends StdConverter<List<Hero>, List<Hero>> {
    @Override
    public List<Hero> convert(List<Hero> heroList) {
        ArrayList arrayList = new ArrayList<Deck>();
        for (Hero hero : heroList) {
            arrayList.add(hero);
        }
        return arrayList;
    }
}
