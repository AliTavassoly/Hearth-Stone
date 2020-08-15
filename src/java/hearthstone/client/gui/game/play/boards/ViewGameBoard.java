package hearthstone.client.gui.game.play.boards;

import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.buttons.PassiveButton;
import hearthstone.client.gui.controls.dialogs.MessageDialog;
import hearthstone.client.gui.controls.dialogs.SureDialog;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.controls.panels.ImagePanel;
import hearthstone.client.gui.controls.panels.WatchersPanel;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.game.onlinegames.GamesPanel;
import hearthstone.client.gui.game.play.controls.*;
import hearthstone.client.network.ClientMapper;
import hearthstone.client.network.HSClient;
import hearthstone.models.player.Player;
import hearthstone.models.player.PlayerModel;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewGameBoard extends GameBoard{
    private static ViewGameBoard instance;

    private ViewGameBoard(PlayerModel myPlayer, PlayerModel enemyPlayer) {
        super(myPlayer, enemyPlayer);
    }

    public static ViewGameBoard makeInstance(PlayerModel myPlayer, PlayerModel enemyPlayer){
        return instance = new ViewGameBoard(myPlayer, enemyPlayer);
    }

    public static ViewGameBoard getInstance(){
        return instance;
    }

    @Override
    protected void makeCardOnHandMouseListener(BoardCardButton button, int startX, int startY, int width, int height) {
        // DO NOTHING
    }

    @Override
    protected void makeCardOnLandMouseListener(BoardCardButton button, int startX, int startY) {
        // DO NOTHING
    }

    @Override
    protected void makeWeaponMouseListener(WeaponButton button) {
        // DO NOTHING
    }

    @Override
    protected void makeHeroPowerMouseListener(HeroPowerButton button) {
        // DO NOTHING
    }

    @Override
    protected void makeHeroMouseListener(BoardHeroButton button) {
        // DO NOTHING
    }

    @Override
    protected void drawEndTurnTimeLine() {
        // DO NOTHING
    }

    @Override
    protected void makeGameStuff() {
        endTurnButton = new ImageButton("End Turn", "end_turn.png",
                "end_turn_hovered.png", 15, 1,
                GUIConfigs.endTurnButtonWidth, GUIConfigs.endTurnButtonHeight);

        sparkImage = new SparkImage(
                endTurnTimeLineStartX,
                endTurnTimeLineY - GUIConfigs.endTurnFireHeight / 2,
                GUIConfigs.endTurnFireWidth,
                GUIConfigs.endTurnFireHeight,
                "/images/spark_0.png");

        ropeImage = new ImagePanel("rope.png", GUIConfigs.endTurnRopeWidth + 7,
                GUIConfigs.endTurnRopeHeight, sparkImage.getX() + sparkImage.getWidth(),
                sparkImage.getY() + sparkImage.getHeight() / 2, true);
        add(ropeImage);

        myHero = new BoardHeroButton(myPlayer.getHero(),
                heroWidth, heroHeight, myPlayer.getPlayerId());
        makeHeroMouseListener(myHero);

        enemyHero = new BoardHeroButton(enemyPlayer.getHero(),
                heroWidth, heroHeight, enemyPlayer.getPlayerId());
        makeHeroMouseListener(enemyHero);

        myPassive = new PassiveButton(myPlayer.getPassive(),
                GUIConfigs.medCardWidth,
                GUIConfigs.medCardHeight);

        myMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(myMessageDialog);

        enemyMessageDialog = new MessageDialog("Not enough mana!", new Color(69, 27, 27),
                15, 0, -17, 2500, GUIConfigs.inGameErrorWidth, GUIConfigs.inGameErrorHeight);
        add(enemyMessageDialog);
    }

    @Override
    protected void makeIcons() {
        backButton = new ImageButton("icons/back.png",
                "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png",
                "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png",
                "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        backButton.addActionListener(actionEvent -> {
            ClientMapper.cancelViewRequest(myPlayer.getUsername(), HSClient.currentAccount.getUsername());
            GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(), new MainMenuPanel());
        });
    }
}
