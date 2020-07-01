package hearthstone.logic.models.card.heropower.heropowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerBehaviour;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;

public class Heal extends HeroPowerCard implements HeroPowerBehaviour {
    public Heal() { }

    public Heal(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }

    @Override
    public void startTurnBehave() {
        numberOfAttack = 1;
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
            Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());
            numberOfAttack--;
        } else if (object instanceof MinionCard){
            MinionCard minion = (MinionCard)object;
            Mapper.getInstance().restoreHealth(4, minion);
            Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());
            numberOfAttack--;
        }
    }

    @Override
    public boolean canAttack() {
        return numberOfAttack > 0 && getManaCost() <= DataTransform.getInstance().getMana(getPlayerId());
    }
}
