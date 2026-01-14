import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameFrame extends JFrame {

    Player player;
    Monster monster;

    JButton fightBtn = new JButton("Fight");
    JButton attackBtn = new JButton("Attack");
    JButton potionBtn = new JButton("Potion");
    JButton runBtn = new JButton("Run");
    JButton continueBtn = new JButton("Continue");

    JLabel monsterImg = new JLabel();
    JLabel playerImg = new JLabel();

    JProgressBar monsterHP = new JProgressBar();
    JProgressBar playerHP = new JProgressBar();
    JProgressBar playerEXP = new JProgressBar();

    LogPanel log = new LogPanel();
    Random r = new Random();

    public GameFrame() {
        setTitle("Simple RPG");
        setSize(1000, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createMonsterPanel(), BorderLayout.WEST);
        add(log, BorderLayout.CENTER);
        add(createPlayerPanel(), BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);

        resetGame();
        registerActions();

        setVisible(true);
    }

    void resetGame() {
        player = new Player("Hero");
        spawnMonster();

        log.clear();
        log.system("Monster(" + monster.name + ") appeared.");

        attackBtn.setEnabled(false);
        continueBtn.setEnabled(false);
        fightBtn.setEnabled(true);

        updatePlayerUI();
        updateMonsterUI();
    }

    void spawnMonster() {
        monster = new Monster(player.level);
    }

    JPanel createMonsterPanel() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(250, 500));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        monsterImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        monsterHP.setPreferredSize(new Dimension(200, 20));
        monsterHP.setStringPainted(true);

        p.add(Box.createVerticalStrut(40));
        p.add(monsterImg);
        p.add(Box.createVerticalStrut(10));
        p.add(monsterHP);
        return p;
    }

    JPanel createPlayerPanel() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(250, 500));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        playerImg.setIcon(scale("images/player.png"));
        playerImg.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerHP.setPreferredSize(new Dimension(200, 20));
        playerEXP.setPreferredSize(new Dimension(200, 15));

        playerHP.setStringPainted(true);
        playerEXP.setStringPainted(true);

        p.add(Box.createVerticalGlue());
        p.add(playerImg);
        p.add(Box.createVerticalStrut(10));
        p.add(playerHP);
        p.add(Box.createVerticalStrut(5));
        p.add(playerEXP);
        p.add(Box.createVerticalStrut(20));
        return p;
    }

    JPanel createButtonPanel() {
        JPanel p = new JPanel();
        p.add(fightBtn);
        p.add(attackBtn);
        p.add(potionBtn);
        p.add(runBtn);
        p.add(continueBtn);
        return p;
    }

    void registerActions() {

        fightBtn.addActionListener(e -> {
            log.system("Player decided to fight Monster(" + monster.name + ")");
            attackBtn.setEnabled(true);
            fightBtn.setEnabled(false);
        });

        attackBtn.addActionListener(e -> {
            int pDmg = player.getAttack();
            monster.hp -= pDmg;
            log.player("Player dealt " + pDmg + " damage");
            updateMonsterUI();

            if (monster.hp <= 0) {
                log.system("Monster died");
                boolean levelUp = player.gainExp(monster.expReward);
                updatePlayerUI();

                if (levelUp) {
                    JOptionPane.showMessageDialog(this,
                        "LEVEL UP!\nPlayer is now Lv." + player.level);
                }

                spawnMonster();
                log.system("Monster(" + monster.name + ") appeared.");
                updateMonsterUI();

                attackBtn.setEnabled(false);
                fightBtn.setEnabled(true);
                return;
            }

            int mDmg = monster.attack;
            player.hp -= mDmg;
            log.monster("Monster dealt " + mDmg + " damage");
            updatePlayerUI();

            if (player.hp <= 0) {
                JOptionPane.showMessageDialog(this, "Player has died.");
                attackBtn.setEnabled(false);
                fightBtn.setEnabled(false);
                continueBtn.setEnabled(true);
            }
        });

        potionBtn.addActionListener(e -> {
            int heal = r.nextInt(21) + 20;
            int before = player.hp;
            player.hp = Math.min(player.maxHp, player.hp + heal);
            log.potion("Recovered " + (player.hp - before) + " HP");
            updatePlayerUI();
        });

        runBtn.addActionListener(e -> {
            log.system("Player ran away.");
            spawnMonster();
            log.system("Monster(" + monster.name + ") appeared.");
            attackBtn.setEnabled(false);
            fightBtn.setEnabled(true);
            updateMonsterUI();
        });

        continueBtn.addActionListener(e -> resetGame());
    }

    void updateMonsterUI() {
        String path = switch (monster.name) {
            case "PinkBean" -> "images/pinkbean.png";
            case "StoneSpirit" -> "images/stone.png";
            default -> "images/yeti.png";
        };

        monsterImg.setIcon(scale(path));
        monsterHP.setMaximum(monster.maxHp);
        monsterHP.setValue(monster.hp);
        monsterHP.setString("HP " + monster.hp + "/" + monster.maxHp + "  Lv." + monster.level);
    }

    void updatePlayerUI() {
        playerHP.setMaximum(player.maxHp);
        playerHP.setValue(player.hp);
        playerHP.setString("HP " + player.hp + "/" + player.maxHp + "  Lv." + player.level);

        playerEXP.setMaximum(player.expToNext());
        playerEXP.setValue(player.exp);
        playerEXP.setString("EXP " + player.exp + "/" + player.expToNext());
    }

    ImageIcon scale(String path) {
        Image img = new ImageIcon(path).getImage();
        return new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
    }
}
