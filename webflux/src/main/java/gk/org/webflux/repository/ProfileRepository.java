package gk.org.webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import gk.org.webflux.entities.Profile;

public interface ProfileRepository extends ReactiveMongoRepository<Profile,String>{
	


}
