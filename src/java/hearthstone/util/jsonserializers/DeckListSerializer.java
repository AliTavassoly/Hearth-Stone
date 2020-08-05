package hearthstone.util.jsonserializers;

import com.fasterxml.jackson.databind.util.StdConverter;
import hearthstone.models.Deck;

import java.util.ArrayList;
import java.util.List;

public class DeckListSerializer extends StdConverter<List<Deck>, List<Deck>> {
    @Override
    public List<Deck> convert(List<Deck> deckList) {
        ArrayList arrayList = new ArrayList<Deck>();
        for(Deck deck : deckList){
            arrayList.add(deck);
        }
        return arrayList;
    }
}