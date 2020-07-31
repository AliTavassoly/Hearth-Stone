package hearthstone.models.card.heropower.heropowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;

public class Sacrificer extends HeroPowerCard {
    public Sacrificer() { }

    public Sacrificer(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility(Hero hero){
        Mapper.getInstance().reduceMana(getPlayerId(), getManaCost());
        Mapper.getInstance().setHealth(hero.getHealth() - 2, hero);

        if(Rand.getInstance().getProbability(1, 2)){
            try {
                Mapper.getInstance().drawCard(getPlayerId());
                Mapper.getInstance().updateBoard();
            } catch (HearthStoneException ignore) {}
        } else {
            MinionCard minionCard = DataTransform.getInstance().getRandomMinionFromLand(getPlayerId());
            if(minionCard != null){
                Mapper.getInstance().addAttack(1, minionCard);
                Mapper.getInstance().addHealth(1, minionCard);
                Mapper.getInstance().updateBoard();
            }
        }

        log();
    }

    @Override
    public void found(Object object) {
        if(object instanceof Hero && ((Hero)object).getPlayerId() == this.getPlayerId()) {
            doAbility(((Hero)object));
            numberOfAttack--;
        }
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.SEARCH;
    }
}
