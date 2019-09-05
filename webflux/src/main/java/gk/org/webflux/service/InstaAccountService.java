package gk.org.webflux.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import gk.org.webflux.entities.InstaAccount;
import gk.org.webflux.repository.InstaAccountRepository;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class InstaAccountService {
	
	private final ApplicationEventPublisher publisher; 
    private final InstaAccountRepository instaAccountRepo; 

    InstaAccountService(ApplicationEventPublisher publisher, InstaAccountRepository profileRepository) {
        this.publisher = publisher;
        this.instaAccountRepo = profileRepository;
    }
    
    public Flux<InstaAccount> all() { 
        return this.instaAccountRepo.findAll();
    }

    public Mono<InstaAccount> get(String id) { 
        return this.instaAccountRepo.findById(id);
    }
    
    public Mono<InstaAccount> update(String id, String email) { 
        return this.instaAccountRepo
            .findById(id)
            .map(p -> new InstaAccount(p.getId(), email))
            .flatMap(this.instaAccountRepo::save);
    }

    public Mono<InstaAccount> delete(String id) { 
        return this.instaAccountRepo
            .findById(id)
            .flatMap(p -> this.instaAccountRepo.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<InstaAccount> create(String email) { 
        return this.instaAccountRepo
            .save(new InstaAccount(null, email))
            .doOnSuccess(profile -> this.publisher.publishEvent(new InstaAccountCreatedEvent(profile)));
    }

}
