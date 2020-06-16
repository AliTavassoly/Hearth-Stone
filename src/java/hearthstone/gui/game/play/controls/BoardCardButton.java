package hearthstone.gui.game.play.controls;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.models.card.Card;
import hearthstone.models.card.cards.MinionCard;
import hearthstone.models.card.cards.RewardCard;
import hearthstone.models.card.cards.SpellCard;
import hearthstone.models.card.cards.WeaponCard;
import hearthstone.util.SoundPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BoardCardButton extends ImageButton implements MouseListener, MouseMotionListener {
    private int width, height;
    private double initialRotate, rotate;
    private boolean showBig;
    private Card card;
    private boolean shouldRotate;
    private boolean isEnemy, isBack, isInLand;
    private String minionFramePath;

    public BoardCardButton(Card card, int width, int height) {
        this.card = card;
        this.width = width;
        this.height = height;
        isBack = true;

        configButton();
    }

    public BoardCardButton(Card card, int width, int height, boolean isEnemy) {
        this.card = card;
        this.isEnemy = isEnemy;

        this.width = width;
        this.height = height;

        configButton();
    }

    public BoardCardButton(Card card, int width, int height, int initialRotate, boolean isEnemy) {
        this.card = card;
        this.initialRotate = initialRotate;
        this.isEnemy = isEnemy;
        rotate = initialRotate;
        shouldRotate = true;

        this.width = width;
        this.height = height;

        configButton();
    }

    public BoardCardButton(Card card, int width, int height, boolean showBig, boolean isEnemy) {
        this.card = card;
        this.isEnemy = isEnemy;
        this.showBig = showBig;

        this.width = width;
        this.height = height;

        configButton();
    }

    public BoardCardButton(Card card, int width, int height, boolean showBig, boolean isEnemy, boolean isInLand) {
        this.card = card;
        this.isEnemy = isEnemy;
        this.showBig = showBig;
        this.isInLand = true;

        this.width = width;
        this.height = height;

        configButton();
    }

    public BoardCardButton(Card card, int width, int height, int initialRotate, boolean showBig, boolean isEnemy) {
        this.card = card;

        this.width = width;
        this.height = height;

        this.initialRotate = initialRotate;
        rotate = initialRotate;

        this.showBig = showBig;
        shouldRotate = true;

        this.isEnemy = isEnemy;

        configButton();
    }

    public boolean isShowBig() {
        return showBig;
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    public void playSound() {
        String path;

        if (card instanceof MinionCard) {
            path = "/sounds/cards/" + card.getName().toLowerCase().replace(' ', '_') + ".wav";
        } else if (card instanceof SpellCard) {
            path = "/sounds/spells/" + "spell" + ".wav";
        } else if (card instanceof WeaponCard) {
            path = "/sounds/weapons/" + "weapon" + ".wav";
        } else if (card instanceof RewardCard) {
            path = "/sounds/rewards/" + "reward" + ".wav";
        } else {
            return;
        }
        SoundPlayer soundPlayer = new SoundPlayer(path);
        soundPlayer.playOnce();
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    public double getInitialRotate() {
        return initialRotate;
    }

    public boolean isShouldRotate() {
        return shouldRotate;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public Card getCard() {
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawCard(g);
    }

    public void drawCard(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        if (card instanceof MinionCard && isInLand) {
            drawMinionInLand(g2);
            return;
        }

        BufferedImage image = null;
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        BufferedImage buffy = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics buffyG = buffy.getGraphics();

        try {
            String path;
            if (!isBack) {
                path = "/images/cards/" + card.getName().toLowerCase().
                        replace(' ', '_').replace("'", "") + ".png";
            } else {
                path = "/images/cards/cards_back/" + "card_back2" + ".png";
            }
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rotate > 0) {
            g2.rotate(Math.toRadians(rotate), width / 2, height / 2);
        }

        buffyG.drawImage(image.getScaledInstance(width, height,
                Image.SCALE_SMOOTH),
                0, 0, width, height, null);

        g2.drawImage(buffy, 0, 0, null);

        if (isBack)
            return;

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        drawStringOnCard(g2, Color.WHITE, fontMetrics);
    }

    void drawMinionInLand(Graphics2D g) {
        String minionPath = "/images/cards/oval_minions/" + card.getName().
                toLowerCase().replace(' ', '_').replace("'", "") + ".png";
        String typePath = null;

        if (minionFramePath == null) {
            if (((MinionCard) card).isTaunt()) {
                minionFramePath = "/images/minion_shield.png";
            } else {
                minionFramePath = "/images/minion_played.png";
            }
        }

        if (((MinionCard) card).isDeathRattle()) {
            typePath = "/images/death_rattle.png";
        } else if (((MinionCard) card).isDivineShield()) {
            typePath = "/images/divine_shield.png";
        } else if (((MinionCard) card).isTriggeredEffect()) {
            typePath = "/images/triggered_effect.png";
        }

        BufferedImage minionImage = null;
        BufferedImage shieldImage = null;
        BufferedImage minionType = null;

        try {
            minionImage = ImageIO.read(this.getClass().getResourceAsStream(
                    minionPath));
            shieldImage = ImageIO.read(this.getClass().getResourceAsStream(
                    minionFramePath));
            minionType = ImageIO.read(this.getClass().getResourceAsStream(
                    typePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(minionImage.getScaledInstance(
                width - 18 - 10, height - 28 - 15,
                Image.SCALE_SMOOTH),
                9, 5,
                width - 18 - 10, height - 28 - 15,
                null);

        g.drawImage(shieldImage.getScaledInstance(
                width - 10, height - 15,
                Image.SCALE_SMOOTH),
                0, 0,
                width - 10, height - 15,
                null);

        if (((MinionCard) card).isDeathRattle()) {
            g.drawImage(minionType.getScaledInstance(
                    SizeConfigs.minionTypeWidth, SizeConfigs.minionTypeHeight,
                    Image.SCALE_SMOOTH),
                    (width - 10) / 2 - SizeConfigs.minionTypeWidth / 2, height - 42,
                    SizeConfigs.minionTypeWidth, SizeConfigs.minionTypeHeight,
                    null);
        } else if (((MinionCard) card).isDivineShield()) {
            g.drawImage(minionType.getScaledInstance(
                    width - 10, height - 5,
                    Image.SCALE_SMOOTH),
                    0, 0,
                    width - 10, height - 5,
                    null);
        } else if (((MinionCard) card).isTriggeredEffect()) {
            g.drawImage(minionType.getScaledInstance(
                    SizeConfigs.minionTypeWidth, SizeConfigs.minionTypeHeight,
                    Image.SCALE_SMOOTH),
                    (width - 10) / 2 - SizeConfigs.minionTypeWidth / 2, height - 42,
                    SizeConfigs.minionTypeWidth, SizeConfigs.minionTypeHeight,
                    null);
        }

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setFont(font);

        // DRAW TEXT

        drawStringOnMinionOnLand(g, Color.WHITE, fontMetrics);
    }

    void drawStringOnMinionOnLand(Graphics2D g, Color color, FontMetrics fontMetrics) {
        String text;
        int textWidth;

        final int minionAttackX = 25 - 5;
        final int minionAttackY = height - 37;
        final int minionHealthX = width - 30;
        final int minionHealthY = height - 37;

        g.setColor(color);
        Font font = GameFrame.getInstance().getCustomFont(0, 30);
        g.setFont(font);

        text = String.valueOf(((MinionCard) card).getAttack());
        textWidth = fontMetrics.stringWidth(text);
        g.drawString(text, minionAttackX - textWidth / 2,
                minionAttackY);

        text = String.valueOf(((MinionCard) card).getHealth());
        textWidth = fontMetrics.stringWidth(text);
        g.drawString(text, minionHealthX - textWidth / 2,
                minionHealthY);
    }

    void drawStringOnCard(Graphics2D g, Color color, FontMetrics fontMetrics) {
        final int spellManaX = 14;
        final int spellManaY = 25;

        final int heroPowerManaX = width / 2 + 3;
        final int heroPowerManaY = 20;

        final int minionManaX = 14;
        final int minionManaY = 25;
        final int minionAttackX = 25 - 10;
        final int minionAttackY = height - 15 + 10;
        final int minionHealthX = width - 15 + 10 - 3;
        final int minionHealthY = height - 15 + 10;

        final int weaponManaX = 14;
        final int weaponManaY = 25;
        final int weaponDurabilityX = width - 15 + 10;
        final int weaponDurabilityY = height - 15 + 10;
        final int weaponAttackX = 25 - 10;
        final int weaponAttackY = height - 15 + 10;

        g.setColor(color);
        Font font = GameFrame.getInstance().getCustomFont(0, 20);
        g.setFont(font);

        String text;
        int textWidth;
        switch (card.getCardType()) {
            case SPELL:
            case REWARDCARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, spellManaX - textWidth / 2,
                        spellManaY);
                break;
            case HEROPOWER:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, heroPowerManaX - textWidth / 2,
                        heroPowerManaY);
                break;
            case MINIONCARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, minionManaX - textWidth / 2,
                        minionManaY);

                text = String.valueOf(((MinionCard) card).getAttack());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, minionAttackX - textWidth / 2,
                        minionAttackY);

                text = String.valueOf(((MinionCard) card).getHealth());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, minionHealthX - textWidth / 2,
                        minionHealthY);
                break;
            case WEAPONCARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, weaponManaX - textWidth / 2,
                        weaponManaY);

                text = String.valueOf(((WeaponCard) card).getAttack());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, weaponAttackX - textWidth / 2,
                        weaponAttackY);

                text = String.valueOf(((WeaponCard) card).getDurability());
                textWidth = fontMetrics.stringWidth(text);
                g.drawString(text, weaponDurabilityX - textWidth / 2,
                        weaponDurabilityY);
                break;
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {

    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (card instanceof MinionCard && isInLand) {
            if (((MinionCard) card).isTaunt()) {
                minionFramePath = "/images/minion_shield_active.png";
            } else {
                minionFramePath = "/images/minion_played_active.png";
            }
        }
        this.repaint();
        this.revalidate();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (card instanceof MinionCard && isInLand) {
            if (((MinionCard) card).isTaunt()) {
                minionFramePath = "/images/minion_shield.png";
            } else {
                minionFramePath = "/images/minion_played.png";
            }
        }
        this.repaint();
        this.revalidate();
    }
}