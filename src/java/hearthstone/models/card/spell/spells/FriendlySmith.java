package hearthstone.models.card.spell.spells;

import hearthstone.models.behaviours.ChooseCardAbility;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;
import hearthstone.server.data.ServerData;
import hearthstone.server.network.HSServer;
import hearthstone.util.Rand;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class FriendlySmith  extends SpellCard implements ChooseCardAbility {
    public FriendlySmith() { }

    public FriendlySmith(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        ArrayList<Card> allWeapons = new ArrayList<>();
        ArrayList<Card> discoverWeapons = new ArrayList<>();
        for(Card card: ServerData.baseCards.values()){
            if(card.getCardType() == CardType.WEAPON_CARD){
                allWeapons.add(card.copy());
            }
        }

        ArrayList<Integer> randomArray = Rand.getInstance().getRandomArray(3, allWeapons.size());
        for(int i = 0; i < 3; i++){
            discoverWeapons.add(allWeapons.get(randomArray.get(i)));
        }

        HSServer.getInstance().chooseCardAbilityRequest(cardGameId, discoverWeapons, playerId);
    }

    @Override
    public void doAfterChoosingCard(Card card) {
        WeaponCard selectedWeapon = (WeaponCard) card;
        selectedWeapon.setAttack(selectedWeapon.getAttack() + 2);

        selectedWeapon.setDurability(selectedWeapon.getDurability() + 2);

        HSServer.getInstance().getPlayer(getPlayerId()).getFactory().makeAndPutDeck(selectedWeapon);

        HSServer.getInstance().updateGame(playerId);
    }
}
