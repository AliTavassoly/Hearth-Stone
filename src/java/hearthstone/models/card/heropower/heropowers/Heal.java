package hearthstone.models.card.heropower.heropowers;

import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;

public class Heal extends HeroPowerCard {
    public Heal() { }

    public Heal(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.HEAL;
    }

    @Override
    public void found(Object object) {
        if(object instanceof Hero){
            Hero hero = (Hero)object;

            Mapper.getInstance().restoreHealth(4, hero);
            log();
            Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());

            numberOfAttack--;
        } else if (object instanceof MinionCard){
            MinionCard minion = (MinionCard)object;

            Mapper.getInstance().restoreHealth(4, minion);
            log();
            Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());

            numberOfAttack--;
        }
    }
}