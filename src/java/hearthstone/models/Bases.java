package hearthstone.models;

import hearthstone.models.card.Card;
import hearthstone.models.hero.Hero;
import hearthstone.models.passive.Passive;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Bases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    /*@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Card> baseCards = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Passive> basePassives = new HashSet<>();
*/
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Hero> baseHeroes = new HashSet<>();

    public Bases(){ }

    /*public Set<Card> getBaseCards() {
        return baseCards;
    }
    public void setBaseCards(Set<Card> baseCards) {
        this.baseCards = baseCards;
    }

    public Set<Passive> getBasePassives() {
        return basePassives;
    }
    public void setBasePassives(Set<Passive> basePassives) {
        this.basePassives = basePassives;
    }*/

    public Set<Hero> getBaseHeroes() {
        return baseHeroes;
    }
    public void setBaseHeroes(Set<Hero> baseHeroes) {
        this.baseHeroes = baseHeroes;
    }
}
