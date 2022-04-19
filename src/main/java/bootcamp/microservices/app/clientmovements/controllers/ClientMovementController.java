package bootcamp.microservices.app.clientmovements.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import bootcamp.microservices.app.clientmovements.documents.ClientMovement;
import bootcamp.microservices.app.clientmovements.services.ClientMovementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ClientMovementController {

	@Autowired
	private ClientMovementService clientMovementService;

	@PostMapping
	public Mono<ClientMovement> createClient(@Valid @RequestBody ClientMovement companyCredit) {
		return clientMovementService.save(companyCredit);
	}

	@GetMapping("/all")
	public Flux<ClientMovement> searchAll() {
		return clientMovementService.findAll();
	}

	@GetMapping("/id/{id}")
	public Mono<ClientMovement> searchById(@PathVariable String id) {
		return clientMovementService.findById(id);
	}

	@PutMapping
	public Mono<ClientMovement> updateClientCredit(@RequestBody ClientMovement companyCredit) {
		return clientMovementService.update(companyCredit);
	}

	@DeleteMapping
	public Mono<ClientMovement> deleteClientCredit(@Valid @RequestBody ClientMovement companyCredit) {
		return clientMovementService.deleteLogic(companyCredit);
	}

	@GetMapping("idOriginMovement/{idOriginMovement}")
	public Flux<ClientMovement> searchByMovementAndIdOriginCompany(@PathVariable Integer movementType,
			@PathVariable String idOriginMovement) {
		return clientMovementService.findByMovementTypeOrigin(idOriginMovement);
	}

	@GetMapping("idDestinyMovement/{idDestinyMovement}")
	public Flux<ClientMovement> searchByMovementAndIdDestinyCompany(@PathVariable Integer movementType,
			@PathVariable String idDestinyMovement) {
		return clientMovementService.findByMovementTypeDestiny(idDestinyMovement);
	}
}
