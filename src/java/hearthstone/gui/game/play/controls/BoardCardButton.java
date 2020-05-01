package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.cards.MinionCard;
import hearthstone.logic.models.card.cards.SpellCard;
import hearthstone.logic.models.card.cards.WeaponCard;
import hearthstone.util.SoundPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class BoardCardButton extends ImageButton implements MouseListener, MouseMotionListener {
    int width, height, initX, initY;
    private boolean showBig;
    private Card card;

    public BoardCardButton(Card card, int width, int height) {
        this.card = card;
        this.initX = initX;
        this.initY = initY;

        this.width = width;
        this.height = height;

        configButton();
    }

    public BoardCardButton(Card card, int width, int height, boolean showBig) {
        this.card = card;
        this.initX = initX;
        this.initY = initY;
        this.showBig = showBig;

        this.width = width;
        this.height = height;

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
        } else {
            return;
        }
        SoundPlayer soundPlayer = new SoundPlayer(path);
        soundPlayer.playOnce();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        BufferedImage buffy = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics buffyG = buffy.getGraphics();

        try {
            String path;
            path = "/images/cards/" + card.getName().toLowerCase().
                    replace(' ', '_').replace("'", "") + ".png";

            image = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        buffyG.drawImage(image.getScaledInstance(width, height,
                Image.SCALE_SMOOTH),
                0, 0, width, height, null);
        g2.drawImage(buffy, 0, 0, null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        drawStringOnCard(g2, Color.WHITE, fontMetrics);
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
}