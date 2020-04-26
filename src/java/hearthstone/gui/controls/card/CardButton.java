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
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class CardButton extends ImageButton implements MouseListener {
    int width, height, number;
    private Card card;

    public CardButton(Card card, int width, int height, int number) {
        this.card = card;
        this.width = width;
        this.height = height;
        this.number = number;

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

        BufferedImage image = null;
        BufferedImage numberImage = null;

        try {
            String path;
            if (HearthStone.currentAccount.getUnlockedCards().contains(card.getId())) {
                path = "/images/cards/" + card.getName().
                        toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            } else {
                path = "/images/cards/" + card.getName().
                        toLowerCase().replace(' ', '_').replace("'", "") + "_bw" + ".png";
            }
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    path));

            numberImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/flag.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g2.drawImage(image.getScaledInstance(
                width, height - 20,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height - 20,
                null);

        g2.drawImage(numberImage.getScaledInstance(
                DefaultSizes.numberOfCardFlagWidth,
                DefaultSizes.numberOfCardFlagHeight,
                Image.SCALE_SMOOTH),
                width / 2 - DefaultSizes.numberOfCardFlagWidth / 2, height - 45,
                DefaultSizes.numberOfCardFlagWidth,
                DefaultSizes.numberOfCardFlagHeight,
                null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        drawCardInfo(g2, Color.WHITE, fontMetrics);

        drawStringOnCard(g2, String.valueOf(number),
                width / 2 - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                height - 12, Color.WHITE);
        //resize
    }

    void drawCardInfo(Graphics2D g, Color color, FontMetrics fontMetrics) {
        final int spellManaX = 24;
        final int spellManaY = 39;

        final int heroPowerManaX = width / 2 + 5;
        final int heroPowerManaY = 25;

        final int minionManaX = 24;
        final int minionManaY = 39;
        final int minionAttackX = 25;
        final int minionAttackY = height - 27;
        final int minionHealthX = width - 17;
        final int minionHealthY = height - 27;

        final int weaponManaX = 24;
        final int weaponManaY = 39;
        final int weaponDurabilityX = width - 17;
        final int weaponDurabilityY = height - 25;
        final int weaponAttackX = 25;
        final int weaponAttackY = height - 25;

        String text;
        int textWidth;

        switch (card.getCardType()) {
            case SPELL:
            case REWARDCARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, spellManaX - textWidth / 2,
                        spellManaY, color);
                break;
            case HEROPOWER:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, heroPowerManaX - textWidth / 2,
                        heroPowerManaY, color);
                break;
            case MINIONCARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, minionManaX - textWidth / 2,
                        minionManaY, color);

                text = String.valueOf(((MinionCard) card).getAttack());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, minionAttackX - textWidth / 2,
                        minionAttackY, color);

                text = String.valueOf(((MinionCard) card).getHealth());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, minionHealthX - textWidth / 2,
                        minionHealthY, color);
                break;
            case WEAPONCARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, weaponManaX - textWidth / 2,
                        weaponManaY, color);

                text = String.valueOf(((WeaponCard) card).getAttack());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, weaponAttackX - textWidth / 2,
                        weaponAttackY, color);

                text = String.valueOf(((WeaponCard) card).getDurability());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, weaponDurabilityX - textWidth / 2,
                        weaponDurabilityY, color);
                break;
        }
    }

    private void drawStringOnCard(Graphics2D g, String text, int x, int y, Color color){
        g.setColor(color);
        g.drawString(text, x, y);
        /*AffineTransform transform = g.getTransform();
        transform.translate(x, y);
        g.transform(transform);

        Color outlineColor = Color.black;
        Color fillColor = color;
        g.setColor(outlineColor);
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(text, g.getFont().deriveFont(20f), frc);
        Shape shape = tl.getOutline(null);
        g.setStroke(new BasicStroke(2f));
        g.draw(shape);
        g.setColor(fillColor);
        g.fill(shape);

        transform.translate(-x, -y);
        g.transform(transform);

        updateUI();*/
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