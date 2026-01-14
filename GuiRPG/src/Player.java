import java.util.Random;

public class Player {
    String name;
    int level = 1;
    int hp, maxHp;
    int exp = 0;
    int baseAttack;

    Random r = new Random();

    Player(String name) {
        this.name = name;
        recalcStats();
    }

    void recalcStats() {
        maxHp = 10;
        baseAttack = 0;

        for (int i = 1; i <= level; i++) {
            maxHp += r.nextInt(6) + 5;
            baseAttack += r.nextInt(3) + 3;
        }
        hp = maxHp;
    }

    int getAttack() {
        return baseAttack + r.nextInt(3);
    }

    int expToNext() {
        return level * 50;
    }

    boolean gainExp(int amount) {
        exp += amount;
        boolean levelUp = false;

        while (exp >= expToNext()) {
            exp -= expToNext();
            level++;
            recalcStats();
            levelUp = true;
        }
        return levelUp;
    }
}
