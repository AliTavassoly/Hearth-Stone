package hearthstone.models.card.heropower.heropowers;

import hearthstone.Mapper;
import hearthstone.models.behaviours.IsAttacked;
import hearthstone.models.card.CardType;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.hero.Hero;
import hearthstone.models.hero.HeroType;
import hearthstone.util.CursorType;
import hearthstone.util.HearthStoneException;

import javax.persistence.Entity;

@Entity
public class Fireblast extends HeroPowerCard {
    public Fireblast() {
    }

    public Fireblast(int id, String name, String description, int manaCost, HeroType heroType, CardType cardType) {
        super(id, name, description, manaCost, heroType, cardType);
    }

    private void doAbility(Object object) throws HearthStoneException {
        if (object instanceof Hero) {
            Hero hero = (Hero) object;
            if (Mapper.getPlayer(hero.getPlayerId()).haveTaunt())
                throw new HearthStoneException("There is taunt in front of you!");

            //Mapper.damage(1, hero);
            hero.gotDamage(1);
            Mapper.updateBoard();

            log();
            //Mapper.reduceMana(getPlayerId(), this.getManaCost());
            Mapper.getPlayer(getPlayerId()).reduceMana(this.getManaCost());

            numberOfAttack--;
        } else if (object instanceof MinionCard) {
            MinionCard minion = (MinionCard) object;
            if (minion.isHeroPowerSafe())
                throw new HearthStoneException("This minion is hero power safe!");
            if (!minion.isImmune() && minion.isDivineShield()) {
                minion.removeDivineShield();
                Mapper.updateBoard();
                return;
            }

            //Mapper.damage(1, minion);
            minion.gotDamage(1);
            Mapper.updateBoard();
            log();

            //Mapper.reduceMana(getPlayerId(), this.getManaCost());
            Mapper.getPlayer(getPlayerId()).reduceMana(this.getManaCost());

            if (minion instanceof IsAttacked) {
                //Mapper.isAttacked((IsAttacked) minion);
                ((IsAttacked) minion).isAttacked();
            }

            numberOfAttack--;
        }
        Mapper.updateBoard();
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
            if (/*DataTransform.haveTaunt(hero.getPlayerId())*/Mapper.getPlayer(hero.getPlayerId()).haveTaunt())
                throw new HearthStoneException("There is taunt in front of you!");

            doAbility(hero);
            numberOfAttack--;
        }
    }
}
