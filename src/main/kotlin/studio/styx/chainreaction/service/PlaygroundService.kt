package studio.styx.chainreaction.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import studio.styx.chainreaction.domain.document.Player
import studio.styx.chainreaction.domain.document.Playground
import studio.styx.chainreaction.domain.model.DefaultPlayerResponseDto
import studio.styx.chainreaction.domain.model.DefaultPlaygroundRequestDto
import studio.styx.chainreaction.domain.model.DefaultPlaygroundResponseDto
import java.util.concurrent.ConcurrentHashMap


@Service
class PlaygroundService() {

    private val runningGames = ConcurrentHashMap<Long, Playground>()

    fun createPlayground(defaultStationRequest: DefaultPlaygroundRequestDto): DefaultPlaygroundResponseDto {
        val playground = Playground(defaultStationRequest.private, defaultStationRequest.maxAllowedPlayers);
        playground.players[defaultStationRequest.host.nickname] = Player(defaultStationRequest.host.nickname, defaultStationRequest.host.color, isHost = true, turn = false, killed = false)
        runningGames[playground.id] = playground;
        return DefaultPlaygroundResponseDto(playground.players.values.map { player: Player -> DefaultPlayerResponseDto(player.nickname, player.color, player.isHost, player.turn, player.killed) }, playground.id, playground.maxAllowedPlayers, playground.status)

    }

    fun getAllPlaygrounds(page: Pageable): List<Playground> {
        return runningGames.values.toList();
    }

    fun getPlayground(id: Long): Playground? {
        return if (runningGames.containsKey(id)) {
            runningGames[id]
        } else null
    }

    @Throws(Exception::class)
    fun run() {
        //logger.info("Saving user: {}", testUser)

        // Save the User class to Azure CosmosDB database.
        ///val firstNamePlaygroundFlux: Flux<Playground> = repository.findByFirstName("testFirstName")

        //  Nothing happens until we subscribe to these Monos.
        //  findById will not return the user as user is not present.
        /*  val findByIdMono: Mono<Playground> = repository.findById(testUser.id)
          val findByIdPlayground: Playground? = findByIdMono.block()
          Assert.isNull(findByIdPlayground, "User must be null")
          Assert.state(savedPlayground.firstName == testUser.firstName, "Saved user first name doesn't match")
          //LOGGER.info("Saved user")
          firstNamePlaygroundFlux.collectList().block()
          val optionalPlaygroundResult: Optional<Playground> = repository.findById(testUser.id).blockOptional()
          val result: Playground = optionalPlaygroundResult.get()
          Assert.state(result.firstName.equals(testUser.firstName), "query result firstName doesn't match!")*/
    }

    fun userExists(playgroundId: Long, nickname: String): Boolean {
        return runningGames[playgroundId]!!.players.containsKey(nickname)
    }

    fun joinUser(playgroundId: Long, nickname: String) {


    }


}
