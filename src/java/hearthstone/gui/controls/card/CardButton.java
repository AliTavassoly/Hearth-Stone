package hearthstone.gui.controls.card;

import hearthstone.HearthStone;
import hearthstone.gui.DefaultSizes;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.cards.MinionCard;
import hearthstone.logic.models.card.cards.SpellCard;
import hearthstone.logic.models.card.cards.WeaponCard;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class CardButton extends ImageButton implements MouseListener {
    int width, height;
    private Card card;

    public CardButton(Card card, int width, int height) {
        this.card = card;
        this.width = width;
        this.height = height;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        /*AffineTransform at = new AffineTransform();
        at.scale(1, 1);
        g2.setTransform(at);*/

        BufferedImage image = null;
        try {
            String path;
            if (HearthStone.currentAccount.getUnlockedCards().contains(card.getId())) {
                path = "/images/cards/" + card.getName().
                        toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            } else {
                path = "/images/cards/bw_" + card.getName().
                        toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            }
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g2.drawImage(image.getScaledInstance(width, height,
                Image.SCALE_SMOOTH),
                0, 0, width, height,null);
        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        drawStringOnCard(g2, Color.WHITE, fontMetrics);
        //resize
    }

    void drawStringOnCard(Graphics2D g, Color color, FontMetrics fontMetrics) {
        final int spellManaX = 25;
        final int spellManaY = 37;

        final int heroPowerManaX = width / 2 + 5;
        final int heroPowerManaY = 25;

        final int minionManaX = 25;
        final int minionManaY = 37;
        final int minionAttackX = 25;
        final int minionAttackY = height - 15;
        final int minionHealthX = width - 15;
        final int minionHealthY = height - 15;

        final int weaponManaX = 25;
        final int weaponManaY = 37;
        final int weaponDurabilityX = width - 15;
        final int weaponDurabilityY = height - 15;
        final int weaponAttackX = 25;
        final int weaponAttackY = height - 15;

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

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);
        File file = null;
        AudioInputStream audioInputStream = null;

        try {
            if (card instanceof MinionCard) {
                file = new File(this.getClass().getResource(
                        "/sounds/cards/" + card.getName().toLowerCase().replace(' ', '_') + ".wav").getFile());
            } else if (card instanceof SpellCard) {
                file = new File(this.getClass().getResource(
                        "/sounds/spells/" + "spell" + ".wav").getFile());
            } else if (card instanceof WeaponCard) {
                file = new File(this.getClass().getResource(
                        "/sounds/weapons/" + "weapon" + ".wav").getFile());
            } else {
                return;
            }
            audioInputStream =
                    AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (
                Exception e) {
            System.out.println(e.getMessage());
        }
    }
}