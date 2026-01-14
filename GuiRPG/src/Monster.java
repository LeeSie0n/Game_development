import java.util.Random;

public class Monster {
    String name;
    int level;
    int hp, maxHp;
    int attack;
    int expReward;

    static String[] NAMES = {"PinkBean", "StoneSpirit", "Yeti"};
    Random r = new Random();

    Monster(int playerLevel) {
        name = NAMES[r.nextInt(NAMES.length)];
        level = playerLevel + r.nextInt(5);

        maxHp = 10;
        attack = 0;

        for (int i = 1; i <= level; i++) {
            maxHp += r.nextInt(3) + 3;
            attack += r.nextInt(7) + 1;
        }

        hp = maxHp;
        expReward = r.nextInt(21) + 30;
    }
}
