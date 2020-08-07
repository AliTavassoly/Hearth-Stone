package hearthstone.client.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.client.network.HSClient;
import hearthstone.server.data.DataBase;
import hearthstone.client.gui.BaseFrame;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.controls.icons.BackIcon;
import hearthstone.client.gui.controls.icons.CloseIcon;
import hearthstone.client.gui.controls.icons.LogoutIcon;
import hearthstone.client.gui.controls.icons.MinimizeIcon;
import hearthstone.client.gui.game.GameFrame;
import hearthstone.client.gui.game.MainMenuPanel;
import hearthstone.client.gui.game.play.boards.PracticeGameBoard;
import hearthstone.client.gui.game.play.boards.SoloGameBoard;
import hearthstone.server.logic.Game;
import hearthstone.models.Deck;
import hearthstone.models.hero.HeroType;
import hearthstone.models.player.AIPlayer;
import hearthstone.models.player.Player;
import hearthstone.server.network.HSServer;
import hearthstone.shared.GUIConfigs;
import hearthstone.util.HearthStoneException;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class PlaySelectionPanel extends JPanel {
    private ImageButton backButton, logoutButton, minimizeButton, closeButton;
    private ImageButton playOnline, practicePlay, soloPlay, deckReaderPlay;

    private static BufferedImage backgroundImage;

    private final int iconX = 20;
    private final int startIconY = 20;
    private final int endIconY = GUIConfigs.gameFrameHeight - GUIConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int buttonDisY = 150;
    private final int startButtonY = GUIConfigs.gameFrameHeight / 2 - buttonDisY * 3 / 2 - GUIConfigs.largeButtonHeight / 2;
    private final int buttonX = GUIConfigs.gameFrameWidth - 200;

    public PlaySelectionPanel() {
        configPanel();

        makeIcons();

        makeButtons();

        layoutComponent();
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (backgroundImage == null)
                backgroundImage = ImageResource.getInstance().getImage(
                        "/images/play_selection_background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(backgroundImage, 0, 0, null);
    }

    private void makeIcons() {
        backButton = new BackIcon("icons/back.png", "icons/back_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
    }

    private void makeButtons() {
        playOnline = new ImageButton("Play Online", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        practicePlay = new ImageButton("Practice", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        soloPlay = new ImageButton("Solo play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);

        deckReaderPlay = new ImageButton("Deck Reader", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        // listeners
        playOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Nothing in this phase
            }
        });

        practicePlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.readyForPlay();

                    makeNewPracticeGame();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                            HearthStone.currentGameBoard);
                } catch (HearthStoneException hse){
                    BaseFrame.error(hse.getMessage());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        soloPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HSClient.currentAccount.readyForPlay();

                    makeNewSoloGame();

                    GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                            HearthStone.currentGameBoard);
                } catch (HearthStoneException hse){
                    BaseFrame.error(hse.getMessage());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        deckReaderPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    makeNewDeckReaderPlay();
                } catch (Exception e){
                    BaseFrame.error("There is a problem in deck reader!");
                    return;
                }

                GameFrame.getInstance().switchPanelTo(GameFrame.getInstance(),
                        HearthStone.currentGameBoard);
            }
        });
    }

    private void configPanel() {
        setSize(new Dimension(GUIConfigs.gameFrameWidth, GUIConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                GUIConfigs.iconWidth,
                GUIConfigs.iconHeight);
        add(closeButton);

        // BUTTONS
        playOnline.setBounds(buttonX,
                startButtonY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(playOnline);

        practicePlay.setBounds(buttonX,
                startButtonY + buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(practicePlay);

        soloPlay.setBounds(buttonX,
                startButtonY + 2 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(soloPlay);

        deckReaderPlay.setBounds(buttonX,
                startButtonY + 3 * buttonDisY,
                GUIConfigs.largeButtonWidth,
                GUIConfigs.largeButtonHeight);
        add(deckReaderPlay);
    }

    private void makeNewPracticeGame(){
        Player player0 = HSClient.currentAccount.getPlayer();
        Player player1 = HSClient.currentAccount.getPlayer();

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new PracticeGameBoard(0, 1);
        //HearthStone.currentGame.startGameEndingTimer();
    }

    private void makeNewSoloGame(){
        Player player0 = HSClient.currentAccount.getPlayer();
        Player player1 = new AIPlayer(HSClient.currentAccount.getSelectedHero(),
                HSClient.currentAccount.getSelectedHero().getSelectedDeck(), player0.getUsername());

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new SoloGameBoard(0, 1);
    }

    private void makeNewDeckReaderPlay() throws Exception{
        Map<String, ArrayList<String> > decks = DataBase.getDecks();

        if(decks == null)
            throw new HearthStoneException("There is a problem in deck reader!");

        Player player0 = new Player(Objects.requireNonNull(HSServer.getHeroByName("Mage")),
                new Deck("Deck1", HeroType.MAGE,
                        HearthStone.getCardsArray(decks.get("friend"))),
                HSClient.currentAccount.getUsername());

        Player player1 = new Player(Objects.requireNonNull(HSServer.getHeroByName("Mage")),
                new Deck("Deck1", HeroType.MAGE,
                        HearthStone.getCardsArray(decks.get("enemy"))),
                HSClient.currentAccount.getUsername());

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new PracticeGameBoard(0, 1);
    }
}
