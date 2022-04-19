package bootcamp.microservices.app.clientmovements.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bootcamp.microservices.app.clientmovements.documents.ClientMovement;
import bootcamp.microservices.app.clientmovements.exceptions.customs.CustomNotFoundException;
import bootcamp.microservices.app.clientmovements.repository.ClientMovementRepository;
import bootcamp.microservices.app.clientmovements.utils.BalanceCalculate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ClientMovementServiceImpl implements ClientMovementService {

	private static final Logger log = LoggerFactory.getLogger(ClientMovementServiceImpl.class);

	@Autowired
	private ClientMovementRepository clientMovementRepository;

	@Autowired
	private BalanceCalculate balanceCalculate;
	
	
	@Override
	public Flux<ClientMovement> findAll() {
		return clientMovementRepository.findAll()
				.switchIfEmpty(Mono.error(new CustomNotFoundException("Clients not exist")));
	}

	@Override
	public Mono<ClientMovement> findById(String id) {
		return clientMovementRepository.findById(id)
				.switchIfEmpty(Mono.error(new CustomNotFoundException("ClientMovement not found")));
	}

	@Override
	public Mono<ClientMovement> update(ClientMovement clientMovement) {
		return clientMovementRepository.findById(clientMovement.getId()).flatMap(c -> {
			clientMovement.setModifyUser(clientMovement.getModifyUser());
			clientMovement.setModifyDate(new Date());
			return clientMovementRepository.save(clientMovement);
		}).switchIfEmpty(Mono.error(new CustomNotFoundException("ClientMovement not found")));
	}

	@Override
	public Mono<ClientMovement> save(ClientMovement clientMovement) {
		if (Double.compare(balanceCalculate.balanceAmount(clientMovement.getId()), clientMovement.getAmount()) > 0) {
			return clientMovementRepository.save(clientMovement).flatMap(cm -> {
				if (cm.getOperationType().getShortName().equalsIgnoreCase("TRANS")
						|| cm.getOperationType().getShortName().equalsIgnoreCase("CREPAY")) {
					cm.setMovementType(1);
					return clientMovementRepository.save(cm);
				}
				return Mono.just(clientMovement);
			});
		}

		return (Mono.error(new CustomNotFoundException("Insufficient balance")));
	}

	@Override
	public Mono<Void> deleteNonLogic(ClientMovement clientMovement) {
		return clientMovementRepository.findById(clientMovement.getId()).flatMap(c -> {
			return clientMovementRepository.delete(c);
		}).switchIfEmpty(Mono.error(new CustomNotFoundException("ClientMovement not found")));
	}

	@Override
	public Mono<ClientMovement> deleteLogic(ClientMovement clientMovement) {
		return clientMovementRepository.findById(clientMovement.getId()).flatMap(c -> {
			c.setModifyUser(clientMovement.getModifyUser());
			c.setModifyDate(new Date());
			return clientMovementRepository.save(c);
		}).switchIfEmpty(Mono.error(new CustomNotFoundException("ClientMovement not found")));
	}

	@Override
	public Flux<ClientMovement> findByMovementTypeOrigin(String idDestinyMovement) {
		return clientMovementRepository.findByIdDestinyMovement(idDestinyMovement);
	}

	@Override
	public Flux<ClientMovement> findByMovementTypeDestiny(String idOriginMovement) {
		return clientMovementRepository.findByIdOriginMovement(idOriginMovement);
	}

}
