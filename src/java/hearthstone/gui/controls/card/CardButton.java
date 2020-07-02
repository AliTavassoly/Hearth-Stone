package hearthstone.gui.controls.card;

import hearthstone.HearthStone;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.CardType;
import hearthstone.logic.models.card.minion.MinionCard;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.util.FontType;
import hearthstone.util.SoundPlayer;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CardButton extends ImageButton implements MouseListener {
    int width, height, number;
    private Card card;
    private boolean markable;
    private boolean writeInfo;

    private boolean isMark;

    private BufferedImage cardImage;
    private static BufferedImage numberImage, redMark;

    public CardButton(Card card, int width, int height, int number) {
        this.card = card;
        this.width = width;
        this.height = height;
        this.number = number;

        configButton();
    }

    public CardButton(Card card, int width, int height, int number, boolean markable) {
        this.card = card;
        this.width = width;
        this.height = height;
        this.number = number;
        this.markable = markable;

        isMark = false;

        configButton();
    }

    public CardButton(Card card, boolean writeInfo, int width, int height, int number) {
        this.card = card;
        this.writeInfo = writeInfo;
        this.width = width;
        this.height = height;
        this.number = number;

        isMark = false;

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

        try {
            String path;
            if (cardImage == null) {
                if (HearthStone.currentAccount.getUnlockedCards().contains(card.getId())) {
                    path = "/images/cards/" + card.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + ".png";
                } else {
                    path = "/images/cards/" + card.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + "_bw" + ".png";
                }
                cardImage = ImageResource.getInstance().getImage(path);
            }

            if (numberImage == null)
                numberImage = ImageResource.getInstance().getImage("/images/flag.png");
            if (redMark == null) {
                redMark = ImageResource.getInstance().getImage("/images/red_mark.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.drawImage(cardImage.getScaledInstance(
                width, height - 20,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height - 20,
                null);

        if (number != -1) {
            g2.drawImage(numberImage.getScaledInstance(
                    SizeConfigs.numberOfCardFlagWidth,
                    SizeConfigs.numberOfCardFlagHeight,
                    Image.SCALE_SMOOTH),
                    width / 2 - SizeConfigs.numberOfCardFlagWidth / 2, height - 38,
                    SizeConfigs.numberOfCardFlagWidth,
                    SizeConfigs.numberOfCardFlagHeight,
                    null);
        }

        if (isMark) {
            g2.drawImage(redMark.getScaledInstance(
                    SizeConfigs.bigRedMarkWidth,
                    SizeConfigs.bigRedMarkHeight,
                    Image.SCALE_SMOOTH),
                    width / 2 - SizeConfigs.bigRedMarkWidth / 2,
                    height / 2 - SizeConfigs.bigRedMarkHeight / 2,
                    SizeConfigs.bigRedMarkWidth,
                    SizeConfigs.bigRedMarkHeight,
                    null);
        }

        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, 0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        drawCardInfo(g2, Color.WHITE, fontMetrics);

        if (number != -1)
            drawCardNumber(g2, fontMetrics);
    }

    private void drawCardInfo(Graphics2D g, Color color, FontMetrics fontMetrics) {
        final int spellManaX = 24;
        final int spellManaY = 39;

        final int heroPowerManaX = width / 2 + 1;
        final int heroPowerManaY = 28;

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

    private void drawCardNumber(Graphics2D g, FontMetrics fontMetrics) {
        drawStringOnCard(g, String.valueOf(number),
                width / 2 - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                height - 10, Color.WHITE);
    }

    private void drawStringOnCard(Graphics2D g, String text, int x, int y, Color color) {
        if(!writeInfo)
            return;
        g.setColor(color);
        g.drawString(text, x, y);
    }

    public boolean isMark() {
        return isMark;
    }

    public boolean isWriteInfo() {
        return writeInfo;
    }

    public void setWriteInfo(boolean writeInfo) {
        this.writeInfo = writeInfo;
    }

    public Card getCard() {
        return card;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!markable) {
            String path;
            if (card.getCardType() == CardType.MINIONCARD) {
                path = "/sounds/minions/" + card.getName().toLowerCase().replace(' ', '_') + ".wav";
            } else if (card.getCardType() == CardType.SPELL) {
                path = "/sounds/spells/" + "spell" + ".wav";
            } else if (card.getCardType() == CardType.WEAPONCARD) {
                path = "/sounds/weapons/" + "weapon" + ".wav";
            } else if (card.getCardType() == CardType.REWARDCARD) {
                path = "/sounds/rewards/" + "reward" + ".wav";
            } else {
                return;
            }
            SoundPlayer soundPlayer = new SoundPlayer(path);
            soundPlayer.playOnce();
        } else {
            changeMarkState();
            repaint();
            revalidate();
        }
    }

    private void changeMarkState() {
        if (isMark)
            isMark = false;
        else
            isMark = true;
    }
}