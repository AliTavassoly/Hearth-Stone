package hearthstone.client.gui.controls.buttons;

import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.network.HSClient;
import hearthstone.models.AccountInfo;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AccountInfoButton extends ImageButton {
    int width, height;
    private AccountInfo accountInfo;

    private Color color;

    private static BufferedImage accountIcon;
    private static BufferedImage accountInfoImage;

    private final int stringDis = 30;
    private final int stringStartY = 35;
    private final int stringsX = 150;

    public AccountInfoButton(AccountInfo accountInfo, int width, int height) {
        this.accountInfo = accountInfo;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    public AccountInfoButton(AccountInfo accountInfo, Color color, int width, int height) {
        this.accountInfo = accountInfo;
        this.width = width;
        this.height = height;

        this.color = color;

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
            if (accountIcon == null)
                accountIcon = ImageResource.getInstance().getImage(
                        "/images/account_icon.png");

            if (accountInfoImage == null)
                accountInfoImage = ImageResource.getInstance().getImage("/images/account_info.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.drawImage(accountIcon.getScaledInstance(95, 95,
                Image.SCALE_SMOOTH), 15, 15,
                95, 95, null);

        g2.drawImage(accountInfoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,
                width, height, null);

        drawStringOnAccountInfo(g2);
    }

    private void drawStringOnAccountInfo(Graphics2D g2) {
        Font font = GameFrame.getInstance().getCustomFont(FontType.TEXT, 0, 15);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        g2.setColor(new Color(69, 27, 27));

        if(this.color != null && accountInfo.getUsername().equals(HSClient.currentAccount.getUsername()))
            g2.setColor(this.color);

        g2.drawString("Rank: " + accountInfo.getRank(), stringsX, stringStartY);
        g2.drawString("Username: " + accountInfo.getUsername(), stringsX, stringStartY + stringDis);
        g2.drawString("Cup: " + accountInfo.getCup(), stringsX, stringStartY + 2 * stringDis);
    }
}
