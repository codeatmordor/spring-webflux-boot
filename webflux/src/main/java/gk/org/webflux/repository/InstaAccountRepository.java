package gk.org.webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import gk.org.webflux.entities.InstaAccount;

public interface InstaAccountRepository extends ReactiveMongoRepository<InstaAccount,String>{
	


}
