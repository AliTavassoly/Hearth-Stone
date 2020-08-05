package hearthstone.util.jacksonserializers;

import com.fasterxml.jackson.databind.util.StdConverter;
import hearthstone.models.Deck;
import hearthstone.models.card.Card;

import java.util.ArrayList;
import java.util.List;

public class CardListSerializer extends StdConverter<List<Card>, List<Card>> {
    @Override
    public List<Card> convert(List<Card> cardList) {
        ArrayList arrayList = new ArrayList<Deck>();
        for(Card card : cardList){
            arrayList.add(card);
        }
        return arrayList;
    }
}