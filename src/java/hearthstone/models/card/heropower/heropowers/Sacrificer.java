package hearthstone.models.card.heropower.heropowers;

import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.server.network.HSServer;
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
        HSServer.getInstance().getPlayer(getPlayerId()).reduceMana(getManaCost());

        hero.setHealth(hero.getHealth() - 2);

        HSServer.getInstance().updateGame(playerId);

        if(Rand.getInstance().getProbability(1, 2)){
            try {
                HSServer.getInstance().getPlayer(getPlayerId()).drawCard();

                HSServer.getInstance().updateGame(playerId);
            } catch (HearthStoneException ignore) {}
        } else {
            MinionCard minionCard = HSServer.getInstance().getPlayer(getPlayerId()).getFactory().getRandomMinionFromLand();
            if(minionCard != null){
                minionCard.changeAttack(1);

                minionCard.gotHeal(1);

                HSServer.getInstance().updateGame(playerId);
            }
        }
    }

    @Override
    public void found(Object object) {
        if(!canAttack)
            return;
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
