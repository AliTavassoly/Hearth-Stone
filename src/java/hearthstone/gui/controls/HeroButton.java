package hearthstone.gui.controls;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.heroes.Hero;
import jdk.swing.interop.SwingInterOpUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroButton extends ImageButton {
    private Hero hero;
    int width, height;

    public HeroButton(Hero hero, int width, int height) {
        this.hero = hero;
        this.height = height;
        this.width = width;

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
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/" + hero.getName().toLowerCase().replace(' ', '_') + ".png"));
        } catch (Exception e) {
            System.out.println(e);
            e.getStackTrace();
        }
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        //drawStringOnCard(g2, Color.WHITE, fontMetrics);

        //resize
    }

    /*void drawStringOnCard(Graphics2D g, Color color, FontMetrics fontMetrics){
       final int spellManaX = 20;
        final int spellManaY = 20;

        final int minionManaX = 0;
        final int minionManaY = 0;
        final int minionAttackX = 0;
        final int minionAttackY = 0;
        final int minionHealthX = 0;
        final int minionHealthY = 0;

        final int weaponManaX = 0;
        final int weaponManaY = 0;
        final int weaponDurabilityX = 0;
        final int weaponDurabilityY = 0;
        final int weaponAttackX = 0;
        final int weaponAttackY = 0;

        final int heroManaX = 0;
        final int heroManaY = 0;

        String text;
        g.setColor(color);
        switch (card.getCardType()){
            case SPELL:
                text = String.valueOf(card.getManaCost());
                g.drawString(text, spellManaX - fontMetrics.stringWidth(text) / 2, spellManaY);
                break;
            case HEROCARD:
                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));
                break;
            case MINIONCARD:
                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));
                break;
            case WEAPONCARD:
                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));
                break;
        }
    }*/
}
