package studio.styx.chainreaction.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import studio.styx.chainreaction.domain.document.Player
import studio.styx.chainreaction.domain.document.Playground
import studio.styx.chainreaction.model.*
import kotlin.collections.ArrayList


@Service
class PlaygroundService() {

    private val runningGames = mutableMapOf<Long, Playground>()
    fun createPlayground(defaultStationRequest: DefaultPlaygroundRequestDto): DefaultPlaygroundResponseDto {
        val playground = Playground(defaultStationRequest.private, defaultStationRequest.maxAllowedPlayers);
        playground.players.add(Player(defaultStationRequest.host.nickname, defaultStationRequest.host.color, isHost = true, turn = false, killed = false))
        runningGames[playground.id] = playground;
        return DefaultPlaygroundResponseDto(playground.players.map { player: Player -> DefaultPlayerResponseDto(player.nickname, player.color, player.isHost, player.turn, player.killed) }, playground.id, playground.maxAllowedPlayers, playground.status)

    }

    fun getAllPlaygrounds(page: Pageable): List<Playground> {
        return runningGames.values.toList();
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


}
