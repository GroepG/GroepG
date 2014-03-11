package be.kdg.spacecrack.unittests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.services.IStatisticsService;
import be.kdg.spacecrack.services.StatisticsService;
import be.kdg.spacecrack.services.handlers.MoveShipHandler;
import be.kdg.spacecrack.utilities.IFirebaseUtil;
import be.kdg.spacecrack.utilities.ViewModelConverter;
import be.kdg.spacecrack.viewmodels.StatisticsViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class StatisticsServiceTests extends BaseUnitTest{
    private IGameService gameService;
    private IStatisticsService statisticsService;

    private IPlayerRepository mockPlayerRepository;
    private IGameRepository mockGameRepository;

    private User user;
    private Profile opponentProfile;

    @Before
    public void setUp() throws Exception {
        PlayerRepository playerRepository = new PlayerRepository(sessionFactory);
        ColonyRepository colonyRepository = new ColonyRepository(sessionFactory);
        ShipRepository shipRepository = new ShipRepository(sessionFactory);
        gameService = new GameService(new PlanetRepository(sessionFactory), colonyRepository, shipRepository, playerRepository, new GameRepository(sessionFactory), new MoveShipHandler(colonyRepository), new ViewModelConverter(), mock(IFirebaseUtil.class));

        mockGameRepository = mock(GameRepository.class);
        statisticsService = new StatisticsService(mockGameRepository);

        user = new User();
        Profile profile = new Profile();
        opponentProfile = new Profile();
        User opponentUser = new User();
        opponentUser.setProfile(opponentProfile);
        user.setProfile(profile);
    }


    @Transactional
    @Test
    public void getStatistics_ProfilePlayed1GameWon1_Game100PercentWinRatio() throws Exception {
        Game game = createGame();
        game.setLoserPlayerId(game.getPlayers().get(1).getPlayerId());
        List<Game> games = new ArrayList<Game>();
        games.add(game);
        Player player = game.getPlayers().get(0);
        Profile profile = player.getProfile();

        stub(mockGameRepository.getGamesByProfile(profile)).toReturn(games);

        StatisticsViewModel statisticsViewModel = statisticsService.getStatistics(profile);

        verify(mockGameRepository, VerificationModeFactory.times(1)).getGamesByProfile(profile);

        assertEquals("Statistics should contain 1 game", 1, statisticsViewModel.getAmountOfGames());
        assertTrue("Statistics winratio should be 100% ", statisticsViewModel.getWinRatio() == 1.0);
        assertTrue("Statistics average amount of colonies per win should be 1", statisticsViewModel.getAverageAmountOfColoniesPerWin() == 1);
        assertTrue("Statistics average amount of ships per win should be 1", statisticsViewModel.getAverageAmountOfShipsPerWin() == 1);
    }
    
    @Transactional
    @Test
    public void getStatistics_ProfilePlayed3GamesWon2withmanycoloniesandships_Game66PercentWinratio() throws Exception {

        Profile profile = new Profile();
        profile.setProfileId(1);
        Game game1 = getOverGameWithColoniesAndShips(1,20,5, 1, profile, opponentProfile);
        Game game2 = getOverGameWithColoniesAndShips(2,100,3, 1, profile, opponentProfile);
        Game game3 = getOverGameWithColoniesAndShips(3,0,0, 0,profile, opponentProfile);

        List<Game> games = new ArrayList<Game>();
        games.add(game1);
        games.add(game2);
        games.add(game3);


        stub(mockGameRepository.getGamesByProfile(profile)).toReturn(games);

        StatisticsViewModel statisticsViewModel = statisticsService.getStatistics(profile);

        verify(mockGameRepository, VerificationModeFactory.times(1)).getGamesByProfile(profile);

        assertEquals("Statistics should contain 3 games", 3, statisticsViewModel.getAmountOfGames());
        assertTrue("Statistics winratio should be around .66 ", statisticsViewModel.getWinRatio() == (2/3));
        assertTrue("Statistics average amount of colonies per win should be 60", statisticsViewModel.getAverageAmountOfColoniesPerWin() == 60);
        assertTrue("Statistics average amount of ships per win should be 4", statisticsViewModel.getAverageAmountOfShipsPerWin() == 4);
    }

    private Game getOverGameWithColoniesAndShips(int gameId, int amountOfColonies, int amountOfShips, int lostPlayerIndex, Profile activePlayerProfile, Profile opponentProfile) {
        Game game = new Game();
        game.setGameId(gameId);
        game.setName("SpaceCrackGame");
        Player player1 = new Player();
        player1.setPlayerId(1);
        player1.setProfile(activePlayerProfile);
        game.addPlayer(player1);
        Player opponentPlayer = new Player();
        opponentPlayer.setProfile(opponentProfile);
        opponentPlayer.setPlayerId(2);
        game.addPlayer(opponentPlayer);

        for(int i = 0; i < amountOfShips; i++)
        {
            player1.addShip(new Ship());
        }

        for(int i = 0; i < amountOfColonies; i++){
            Colony colony = new Colony();
            colony.setPlanet(new Planet());
            player1.addColony(colony);
        }


        game.setLoserPlayerId(game.getPlayers().get(lostPlayerIndex).getPlayerId());
        return game;
    }

    private Game createGame() {
        int gameId = gameService.createGame(user.getProfile(), "SpaceCrackName8", opponentProfile);
        Game game = gameService.getGameByGameId(gameId);

        return game;
    }
}
