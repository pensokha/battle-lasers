package com.pianist.battlelasers.screens;

import java.util.List;

import android.text.TextUtils;

import com.pianist.battlelasers.Assets;
import com.pianist.battlelasers.activities.BattleLaserActivity;
import com.pianist.battlelasers.game_objects.Button;
import com.pianist.battlelasers.game_objects.Match;
import com.pianist.battlelasers.graphics.Graphics;
import com.pianist.battlelasers.graphics.Pixmap;
import com.pianist.battlelasers.graphics.Graphics.PixmapFormat;
import com.pianist.battlelasers.input_handlers.Input;
import com.pianist.battlelasers.input_handlers.Input.KeyEvent;
import com.pianist.battlelasers.input_handlers.Input.TouchEvent;

/**
 * The MultiSetupScreen class is called when preparing than online game, it acts as a
 * game lobby for the online matching process
 * 
 * @author Peter Gokhshteyn
 */
public class MultiSetupScreen extends Screen
{
	// Whether to laod the images
	private boolean loadImages;
	
	// The navigation buttons 
	private Button leftButton;
	private Button matchSearchButton;
	
	// Match object that is passed from screen to screen
	private Match match;
	
	// Loading states
	private boolean startingGame;
	private boolean loaded;
	private boolean paused;
	
	public MultiSetupScreen(BattleLaserActivity game, boolean loadImages, Match match) {
		super(game, match);
		
		match.resetOnline();
		this.match = match;
		this.loadImages = loadImages;
		
		startingGame = false;
		
		loaded = false;
		
		paused = false;
	}
	
	/**
	 * Called when the user is registered
	 * 
	 * @param id the id that the server passed back
	 */
	public synchronized void registeredUser(int id) {
		game.showProgressDialog("Looking for a match...", true);
		match.onlineUserId = id;
	}
	
	/**
	 * Called when a match is created
	 * 
	 * @param otherPlayerName other player's name
	 * @param mapId the map that this match is played on
	 * @param playerNumber which player this user is (1 or 2)
	 * @param otherPlayerRating the other player rating
	 */
	public synchronized void createdMatch(String otherPlayerName, int mapId, int playerNumber, int otherPlayerRating) {
		game.dismissProgressDialog();
		game.showNewMatchDialog(otherPlayerName, otherPlayerRating);
		match.playerNumberForOnline = playerNumber;
		match.otherPlayerRating = otherPlayerRating;
		match.otherPlayerName = otherPlayerName;
		match.currentLayout = match.getLayout(mapId);
	}
	
	/**
	 * Called when the match has started
	 */
	public synchronized void startMatch() {
		startingGame = true;
		Screen screen = new GameScreen(game, match);
		game.setScreen(screen);
	}

	@Override
	public synchronized void update(float deltaTime)
	{
		Graphics g = game.getGraphics();

		// If the graphics haven't been loaded then loads them
		if (!loaded)
		{
			loaded = true;

			// If the game screen graphics haven't been loaded yet, load them
			if (loadImages)
			{
				Assets.gameMenuButtonNor = g.newPixmap(
						"GameMenuButtonNormal.png", PixmapFormat.ARGB4444);
				Assets.gameMenuButtonClck = g.newPixmap(
						"GameMenuButtonClicked.png", PixmapFormat.ARGB4444);
				Assets.undoButtonNor = g.newPixmap("UndoButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.undoButtonClck = g.newPixmap("UndoButtonClicked.png",
						PixmapFormat.ARGB4444);

				Assets.gameBackground = g.newPixmap("GameBackground.png",
						PixmapFormat.ARGB4444);
				Assets.gridHighlight = g.newPixmap("GridHighlight.png",
						PixmapFormat.ARGB4444);
				Assets.shadedTile = g.newPixmap("ShadedTile.png",
						PixmapFormat.ARGB4444);
				Assets.target = g
						.newPixmap("Target.png", PixmapFormat.ARGB4444);
				Assets.mirrorBorder = g.newPixmap("MirrorBorder.png",
						PixmapFormat.ARGB4444);
				Assets.gameTitleBar = g.newPixmap("GameTitleBar.png",
						PixmapFormat.ARGB4444);

				Assets.horAnimation = new Pixmap[5];
				Assets.verAnimation = new Pixmap[5];
				Assets.tileAnimation = new Pixmap[5];
				for (int index = 0; index < 5; index++)
				{
					Assets.horAnimation[index] = g.newPixmap(
							"HorizonalAnimation" + (index + 1) + ".png",
							PixmapFormat.ARGB4444);
					Assets.verAnimation[index] = g.newPixmap(
							"VerticalAnimation" + (index + 1) + ".png",
							PixmapFormat.ARGB4444);
					Assets.tileAnimation[index] = g.newPixmap("TileAnimation"
							+ (index + 1) + ".png", PixmapFormat.ARGB4444);
				}
				Assets.horLine = g.newPixmap("HorizonalLine.png",
						PixmapFormat.ARGB4444);
				Assets.verLine = g.newPixmap("VerticalLine.png",
						PixmapFormat.ARGB4444);

				Assets.mirrorHorizonal = g.newPixmap("MirrorHorizonal.png",
						PixmapFormat.ARGB4444);
				Assets.mirrorVertical = g.newPixmap("MirrorVertical.png",
						PixmapFormat.ARGB4444);

				Assets.gunBL = g.newPixmap("CannonBottomLeft.png",
						PixmapFormat.ARGB4444);
				Assets.gunBR = g.newPixmap("CannonBottomRight.png",
						PixmapFormat.ARGB4444);
				Assets.gunTL = g.newPixmap("CannonTopLeft.png",
						PixmapFormat.ARGB4444);
				Assets.gunTR = g.newPixmap("CannonTopRight.png",
						PixmapFormat.ARGB4444);

				Assets.gunBLSel = g.newPixmap("CannonBottomLeftSelected.png",
						PixmapFormat.ARGB4444);
				Assets.gunBRSel = g.newPixmap("CannonBottomRightSelected.png",
						PixmapFormat.ARGB4444);
				Assets.gunTLSel = g.newPixmap("CannonTopLeftSelected.png",
						PixmapFormat.ARGB4444);
				Assets.gunTRSel = g.newPixmap("CannonTopRightSelected.png",
						PixmapFormat.ARGB4444);

				Assets.timerBar = g.newPixmap("TimerBar.png",
						PixmapFormat.ARGB4444);

				Assets.gunBLHighlight = g.newPixmap(
						"CannonBottomLeftHighlight.png", PixmapFormat.ARGB4444);
				Assets.gunBRHighlight = g
						.newPixmap("CannonBottomRightHighlight.png",
								PixmapFormat.ARGB4444);
				Assets.gunTLHighlight = g.newPixmap(
						"CannonTopLeftHighlight.png", PixmapFormat.ARGB4444);
				Assets.gunTRHighlight = g.newPixmap(
						"CannonTopRightHighlight.png", PixmapFormat.ARGB4444);

				Assets.mainMenuBackground = g.newPixmap(
						"MainMenuBackground.png", PixmapFormat.ARGB4444);

				Assets.playGameButtonNor = g.newPixmap(
						"PlayGameButtonNormal.png", PixmapFormat.ARGB4444);
				Assets.playGameButtonClck = g.newPixmap(
						"PlayGameButtonClicked.png", PixmapFormat.ARGB4444);

				Assets.instructionsButtonNor = g.newPixmap(
						"InstructionsButtonNormal.png", PixmapFormat.ARGB4444);
				Assets.instructionsButtonClck = g.newPixmap(
						"InstructionsButtonClicked.png", PixmapFormat.ARGB4444);

				Assets.aboutButtonNor = g.newPixmap("AboutButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.aboutButtonClck = g.newPixmap("AboutButtonClicked.png",
						PixmapFormat.ARGB4444);

				Assets.exitButtonNor = g.newPixmap("ExitButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.exitButtonClck = g.newPixmap("ExitButtonClicked.png",
						PixmapFormat.ARGB4444);

				Assets.aboutBackground = g.newPixmap("AboutBackground.png",
						PixmapFormat.ARGB4444);
				Assets.aboutBackButtonNor = g.newPixmap(
						"AboutBackButtonNormal.png", PixmapFormat.ARGB4444);
				Assets.aboutBackButtonClck = g.newPixmap(
						"AboutBackButtonClicked.png", PixmapFormat.ARGB4444);

				Assets.gameInstructions1 = g.newPixmap("Instructions1.png",
						PixmapFormat.ARGB4444);
				Assets.gameInstructions2 = g.newPixmap("Instructions2.png",
						PixmapFormat.ARGB4444);
				Assets.gameInstructions3 = g.newPixmap("Instructions3.png",
						PixmapFormat.ARGB4444);
				
				Assets.rightButtonNor = g.newPixmap("RightButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.rightButtonClck = g.newPixmap("RightButtonClicked.png",
						PixmapFormat.ARGB4444);
				Assets.leftButtonNor = g.newPixmap("LeftButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.leftButtonClck = g.newPixmap("LeftButtonClicked.png",
						PixmapFormat.ARGB4444);

				Assets.background = g.newPixmap("Background.png",
						PixmapFormat.ARGB4444);
				
				Assets.singleButtonNor = g.newPixmap("SingleButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.singleButtonClck = g.newPixmap("SingleButtonClicked.png",
						PixmapFormat.ARGB4444);
				Assets.localMultButtonNor = g.newPixmap("LocalMulButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.localMultButtonClck = g.newPixmap("LocalMulButtonClicked.png",
						PixmapFormat.ARGB4444);
				Assets.onlineMultButtonNor = g.newPixmap("OnlineMulButtonNormal.png",
						PixmapFormat.ARGB4444);
				Assets.onlineMultButtonClck = g.newPixmap("OnlineMulButtonClicked.png",
						PixmapFormat.ARGB4444);

				Assets.singleSetupBackground = g.newPixmap("SingleGameSetupScreen.png",
						PixmapFormat.ARGB4444);
				Assets.multiSetupBackground = g.newPixmap("MultiSetupScreen.png",
						PixmapFormat.ARGB4444);
				Assets.onSetupBackground = g.newPixmap("GameSetupScreenOn.png",
						PixmapFormat.ARGB4444);
				Assets.offSetupBackground = g.newPixmap("GameSetupScreenOff.png",
						PixmapFormat.ARGB4444);
				
				Assets.matchSearchButtonNor = g.newPixmap("MatchSearchButtonNormal.png", 
						PixmapFormat.ARGB4444);
				Assets.matchSearchButtonClck = g.newPixmap("MatchSearchButtonClicked.png", 
						PixmapFormat.ARGB4444);
				
				Assets.easyModeSelect = g.newPixmap("EasyModeSelect.png",
						PixmapFormat.ARGB4444);
				Assets.mediumModeSelect = g.newPixmap("MediumModeSelect.png",
						PixmapFormat.ARGB4444);
				Assets.onOffSelect = g.newPixmap("OnOffSelect.png",
						PixmapFormat.ARGB4444);
				
				Assets.singleDigitSelect = g.newPixmap("SingleDigitSelect.png",
						PixmapFormat.ARGB4444);
				Assets.doubleDigitSelect = g.newPixmap("DoubleDigitSelect.png",
						PixmapFormat.ARGB4444);
				Assets.mixedSelect = g.newPixmap("MixedSelect.png",
						PixmapFormat.ARGB4444);
			}
			

			leftButton = new Button(28, 727, Assets.leftButtonNor,
					Assets.leftButtonClck);
			
			matchSearchButton = new Button(75, 420, Assets.matchSearchButtonNor,
					Assets.matchSearchButtonClck);
			
		}
		else 
		{
			// Check if the back button was clicked, and return to the main menu
			// if it was
			List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
			if (keyEvents.size() > 0
					&& keyEvents.get(0).type == KeyEvent.KEY_UP
					&& keyEvents.get(0).keyCode == android.view.KeyEvent.KEYCODE_BACK)
			{
				Screen nextScreen = new GameModeScreen(game, match);
				game.setScreen(nextScreen);
			}
			
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			int size = touchEvents.size();
			// For every touch event
			for (int event = 0; event < size; event++)
			{
				TouchEvent nextEvent = touchEvents.get(event);

				// Update the buttons
				leftButton.click(nextEvent.x, nextEvent.y, nextEvent.type);
				matchSearchButton.click(nextEvent.x, nextEvent.y, nextEvent.type);
			}
			
			// If the left button was released, go back to the game mode screen
			if (leftButton.wasReleased())
			{
				Screen screen = new GameModeScreen(game, match);
				game.setScreen(screen);
				match.reset();
			}
			else if (matchSearchButton.wasReleased()) 
			{
				// If the search button was released, register for gcm which registers the user with the server
				match.onlineUserId = BattleLaserActivity.settings.getInt(BattleLaserActivity.PREF_USER_ID, 0);
				game.showProgressDialog("Connecting to server...", true);
				if (TextUtils.isEmpty(match.userName)) {
					game.updateUsername();
				} else {
					game.registerGCM();
				}
			}
		}
	}

	@Override
	public synchronized void present(float deltaTime)
	{
		if (!startingGame && !paused) {
			try {
				Graphics g = game.getGraphics();
		
				// Draw the background
				g.drawPixmap(Assets.background, 0, 0);
				g.drawPixmap(Assets.multiSetupBackground, 0, 0);
				
				leftButton.draw(g);
				matchSearchButton.draw(g);
				int ratingPosition = (match.onlineRating >= 1000) ? 270 : 300;
				g.drawText(ratingPosition, 650, 60, "" + match.onlineRating);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public synchronized void pause()
	{
		paused = true;
	}

	@Override
	public synchronized void resume()
	{
		paused = false;
	}

	@Override
	public synchronized void dispose()
	{
		if (startingGame)
		{
			game.disposeImage(Assets.singleSetupBackground);
			game.disposeImage(Assets.onSetupBackground);
			game.disposeImage(Assets.offSetupBackground);
			game.disposeImage(Assets.multiSetupBackground);
			game.disposeImage(Assets.matchSearchButtonNor);
			game.disposeImage(Assets.matchSearchButtonClck);
			
			game.disposeImage(Assets.easyModeSelect);
			game.disposeImage(Assets.mediumModeSelect);
			game.disposeImage(Assets.onOffSelect);
			
			game.disposeImage(Assets.singleDigitSelect);
			game.disposeImage(Assets.doubleDigitSelect);
			game.disposeImage(Assets.mixedSelect);

			game.disposeImage(Assets.rightButtonNor);
			game.disposeImage(Assets.rightButtonClck);

			game.disposeImage(Assets.leftButtonNor);
			game.disposeImage(Assets.leftButtonClck);

			game.disposeImage(Assets.mainMenuBackground);

			game.disposeImage(Assets.playGameButtonNor);
			game.disposeImage(Assets.playGameButtonClck);

			game.disposeImage(Assets.instructionsButtonNor);
			game.disposeImage(Assets.instructionsButtonClck);

			game.disposeImage(Assets.aboutButtonNor);
			game.disposeImage(Assets.aboutButtonClck);

			game.disposeImage(Assets.exitButtonNor);
			game.disposeImage(Assets.exitButtonClck);

			game.disposeImage(Assets.aboutBackButtonNor);
			game.disposeImage(Assets.aboutBackButtonClck);
			game.disposeImage(Assets.aboutBackground);
			
			game.disposeImage(Assets.singleButtonNor);
			game.disposeImage(Assets.singleButtonClck);
			game.disposeImage(Assets.localMultButtonNor);
			game.disposeImage(Assets.localMultButtonClck);
			game.disposeImage(Assets.onlineMultButtonNor);
			game.disposeImage(Assets.onlineMultButtonClck);
			
			game.disposeImage(Assets.easyModeSelect);
			game.disposeImage(Assets.mediumModeSelect);
			game.disposeImage(Assets.onOffSelect);
		}
	}
}
