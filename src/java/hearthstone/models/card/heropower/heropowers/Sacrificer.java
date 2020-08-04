package hearthstone.models.card.heropower.heropowers;

import hearthstone.Mapper;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;
import hearthstone.util.Rand;

import javax.persistence.Entity;

@Entity
public class Sacrificer extends HeroPowerCard {
    public Sacrificer() { }

    public Sacrificer(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType){
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility(Hero hero){
        //Mapper.reduceMana(getPlayerId(), getManaCost());
        Mapper.getPlayer(getPlayerId()).reduceMana(getManaCost());

        //Mapper.setHealth(hero.getHealth() - 2, hero);
        hero.setHealth(hero.getHealth() - 2);
        Mapper.updateBoard();

        if(Rand.getInstance().getProbability(1, 2)){
            try {
                //Mapper.drawCard(getPlayerId());
                Mapper.getPlayer(getPlayerId()).drawCard();

                Mapper.updateBoard();
            } catch (HearthStoneException ignore) {}
        } else {
            //MinionCard minionCard = DataTransform.getRandomMinionFromLand(getPlayerId());
            MinionCard minionCard = Mapper.getPlayer(getPlayerId()).getFactory().getRandomMinionFromLand();
            if(minionCard != null){
                //Mapper.addAttack(1, minionCard.getCardGameId());
                minionCard.changeAttack(1);

                //Mapper.addHealth(1, minionCard);
                minionCard.gotHeal(1);

                Mapper.updateBoard();
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
