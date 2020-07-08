package hearthstone.logic.models.card.heropower.heropowers;

import hearthstone.DataTransform;
import hearthstone.Mapper;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.hero.Hero;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

public class Fireblast extends HeroPowerCard {
    public Fireblast() {
    }

    public Fireblast(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility(Object object) throws HearthStoneException {
        if (object instanceof Hero) {
            Hero hero = (Hero) object;
            if (DataTransform.getInstance().haveTaunt(hero.getPlayerId()))
                throw new HearthStoneException("There is taunt in front of you!");

            Mapper.getInstance().damage(1, hero);
            log();
            Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());
            numberOfAttack--;
        } else if (object instanceof MinionCard) {
            MinionCard minion = (MinionCard) object;
            if (minion.isHeroPowerSafe())
                throw new HearthStoneException("This minion is hero power safe!");
            if (!minion.isImmune() && minion.isDivineShield()) {
                minion.removeDivineShield();
                Mapper.getInstance().updateBoard();
                return;
            }

            Mapper.getInstance().damage(1, minion);
            log();
            Mapper.getInstance().reduceMana(getPlayerId(), this.getManaCost());

            if (minion instanceof IsAttacked) {
                Mapper.getInstance().isAttacked((IsAttacked) minion);
            }

            numberOfAttack--;
        }
        Mapper.getInstance().updateBoard();
    }

    @Override
    public CursorType lookingForCursorType() {
        return CursorType.ATTACK;
    }

    @Override
    public void found(Object object) throws HearthStoneException {
        if (object instanceof MinionCard) {
            MinionCard minionCard = (MinionCard) object;
            if (minionCard.getPlayerId() == this.getPlayerId())
                throw new HearthStoneException("Choose enemy!");

            doAbility(minionCard);
            numberOfAttack--;
        } else if (object instanceof Hero) {
            Hero hero = (Hero) object;
            if (hero.getPlayerId() == this.getPlayerId())
                throw new HearthStoneException("Choose enemy!");
            if (DataTransform.getInstance().haveTaunt(hero.getPlayerId()))
                throw new HearthStoneException("There is taunt in front of you!");

            doAbility(hero);
            numberOfAttack--;
        }
    }
}
