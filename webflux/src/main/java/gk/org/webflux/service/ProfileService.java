package gk.org.webflux.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import gk.org.webflux.entities.Profile;
import gk.org.webflux.repository.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class ProfileService {
	
	private final ApplicationEventPublisher publisher; 
    private final ProfileRepository profileRepository; 

    ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }
    
    public Flux<Profile> all() { 
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(String id) { 
        return this.profileRepository.findById(id);
    }
    
    public Mono<Profile> update(String id, String email) { 
        return this.profileRepository
            .findById(id)
            .map(p -> new Profile(p.getId(), email))
            .flatMap(this.profileRepository::save);
    }

    public Mono<Profile> delete(String id) { 
        return this.profileRepository
            .findById(id)
            .flatMap(p -> this.profileRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Profile> create(String email) { 
        return this.profileRepository
            .save(new Profile(null, email))
            .doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
    }

}
