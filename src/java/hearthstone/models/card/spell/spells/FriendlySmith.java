package hearthstone.models.card.spell.spells;

import hearthstone.HearthStone;
import hearthstone.Mapper;
import hearthstone.gui.controls.dialogs.CardSelectionDialog;
import hearthstone.gui.game.GameFrame;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.Rarity;
import hearthstone.models.card.spell.SpellCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.models.hero.HeroType;
import hearthstone.util.Rand;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class FriendlySmith  extends SpellCard {
    public FriendlySmith() { }

    public FriendlySmith(int id, String name, String description, int manaCost, HeroType heroType, Rarity rarity, CardType cardType){
        super(id, name, description, manaCost, heroType, rarity, cardType);
    }

    @Override
    public void doAbility() {
        ArrayList<Card> allWeapons = new ArrayList<>();
        ArrayList<Card> discoverWeapons = new ArrayList<>();
        for(Card card: HearthStone.baseCards.values()){
            if(card.getCardType() == CardType.WEAPON_CARD){
                allWeapons.add(card.copy());
            }
        }

        ArrayList<Integer> randomArray = Rand.getInstance().getRandomArray(3, allWeapons.size());
        for(int i = 0; i < 3; i++){
            discoverWeapons.add(allWeapons.get(randomArray.get(i)));
        }

        CardSelectionDialog cardDialog = new CardSelectionDialog(GameFrame.getInstance(),
                discoverWeapons);

        WeaponCard selectedWeapon = (WeaponCard) cardDialog.getCard();
        Mapper.getInstance().addAttack(2, selectedWeapon);
        Mapper.getInstance().addDurability(2, selectedWeapon);
        Mapper.getInstance().makeAndPutDeck(getPlayerId(), selectedWeapon);

        Mapper.getInstance().updateBoard();
    }
}
