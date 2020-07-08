package hearthstone.gui.game.play;

import hearthstone.HearthStone;
import hearthstone.data.DataBase;
import hearthstone.gui.BaseFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.ImageButton;
import hearthstone.gui.controls.icons.BackIcon;
import hearthstone.gui.controls.icons.CloseIcon;
import hearthstone.gui.controls.icons.LogoutIcon;
import hearthstone.gui.controls.icons.MinimizeIcon;
import hearthstone.gui.game.GameFrame;
import hearthstone.gui.game.MainMenuPanel;
import hearthstone.gui.game.play.boards.PracticeGameBoard;
import hearthstone.gui.game.play.boards.SoloGameBoard;
import hearthstone.logic.gamestuff.Game;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.hero.HeroType;
import hearthstone.logic.models.player.AIPlayer;
import hearthstone.logic.models.player.Player;
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
    private final int endIconY = SizeConfigs.gameFrameHeight - SizeConfigs.iconHeight - 20;
    private final int iconsDis = 70;
    private final int buttonDisY = 150;
    private final int startButtonY = SizeConfigs.gameFrameHeight / 2 - buttonDisY * 3 / 2 - SizeConfigs.largeButtonHeight / 2;
    private final int buttonX = SizeConfigs.gameFrameWidth - 200;

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
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight, new MainMenuPanel());

        logoutButton = new LogoutIcon("icons/logout.png", "icons/logout_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        minimizeButton = new MinimizeIcon("icons/minimize.png", "icons/minimize_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);

        closeButton = new CloseIcon("icons/close.png", "icons/close_hovered.png",
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
    }

    private void makeButtons() {
        playOnline = new ImageButton("Play Online", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        practicePlay = new ImageButton("Practice", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        soloPlay = new ImageButton("Solo play", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);

        deckReaderPlay = new ImageButton("Deck Reader", "buttons/long_pink_background.png",
                -1, Color.white, Color.yellow, 14, 0,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        // listeners
        playOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                // Nothing in this phase
            }
        });

        practicePlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    HearthStone.currentAccount.readyForPlay();

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
                    HearthStone.currentAccount.readyForPlay();

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
        setSize(new Dimension(SizeConfigs.gameFrameWidth, SizeConfigs.gameFrameHeight));
        setLayout(null);
        setVisible(true);
    }

    private void layoutComponent() {
        // ICONS
        backButton.setBounds(iconX, startIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(backButton);

        logoutButton.setBounds(iconX, startIconY + iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(logoutButton);

        minimizeButton.setBounds(iconX, endIconY - iconsDis,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(minimizeButton);

        closeButton.setBounds(iconX, endIconY,
                SizeConfigs.iconWidth,
                SizeConfigs.iconHeight);
        add(closeButton);

        // BUTTONS
        playOnline.setBounds(buttonX,
                startButtonY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(playOnline);

        practicePlay.setBounds(buttonX,
                startButtonY + buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(practicePlay);

        soloPlay.setBounds(buttonX,
                startButtonY + 2 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(soloPlay);

        deckReaderPlay.setBounds(buttonX,
                startButtonY + 3 * buttonDisY,
                SizeConfigs.largeButtonWidth,
                SizeConfigs.largeButtonHeight);
        add(deckReaderPlay);
    }

    private void makeNewPracticeGame(){
        Player player0 = HearthStone.currentAccount.getPlayer();
        Player player1 = HearthStone.currentAccount.getPlayer();

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new PracticeGameBoard(0, 1);
    }

    private void makeNewSoloGame(){
        Player player0 = HearthStone.currentAccount.getPlayer();
        Player player1 = new AIPlayer(HearthStone.currentAccount.getSelectedHero(),
                HearthStone.currentAccount.getSelectedHero().getSelectedDeck(), player0.getUsername());

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new SoloGameBoard(0, 1);
    }

    private void makeNewDeckReaderPlay() throws Exception{
        Map<String, ArrayList<String> > decks = DataBase.getDecks();

        if(decks == null)
            throw new HearthStoneException("There is a problem in deck reader!");

        Player player0 = new Player(Objects.requireNonNull(HearthStone.getHeroByName("Mage")),
                new Deck("Deck1", HeroType.MAGE,
                        HearthStone.getCardsArray(decks.get("friend"))),
                HearthStone.currentAccount.getUsername());

        Player player1 = new Player(Objects.requireNonNull(HearthStone.getHeroByName("Mage")),
                new Deck("Deck1", HeroType.MAGE,
                        HearthStone.getCardsArray(decks.get("enemy"))),
                HearthStone.currentAccount.getUsername());

        HearthStone.currentGame = new Game(player0, player1);
        HearthStone.currentGameBoard = new PracticeGameBoard(0, 1);
    }
}
