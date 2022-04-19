package bootcamp.microservices.app.clientmovements.services;

import bootcamp.microservices.app.clientmovements.documents.ClientMovement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientMovementService {

	public Flux<ClientMovement> findAll();

	public Mono<ClientMovement> findById(String id);

	public Mono<ClientMovement> save(ClientMovement clientMovement);

	public Mono<ClientMovement> update(ClientMovement clientMovement);

	public Mono<Void> deleteNonLogic(ClientMovement clientMovement);

	public Mono<ClientMovement> deleteLogic(ClientMovement clientMovement);

	public Flux<ClientMovement> findByMovementTypeOrigin(String idOriginMovement);

	public Flux<ClientMovement> findByMovementTypeDestiny(String idDestinyMovement);

}
