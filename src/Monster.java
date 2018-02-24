/**
 * Monster class
 */
public class Monster {
    private String name;
    private double attack, health, defence;
    private Item monsterDrop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getDefence() {
        return defence;
    }

    public Item getMonsterDrop() {
        return monsterDrop;
    }
}
