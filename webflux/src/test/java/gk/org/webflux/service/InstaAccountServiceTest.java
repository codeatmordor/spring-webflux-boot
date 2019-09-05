package gk.org.webflux.service;

import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import gk.org.webflux.entities.InstaAccount;
import gk.org.webflux.repository.InstaAccountRepository;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
@DataMongoTest 
@Import(InstaAccountService.class)
public class InstaAccountServiceTest {

	private final InstaAccountService service;
    private final InstaAccountRepository repository;

    public InstaAccountServiceTest(@Autowired InstaAccountService service, 
                              @Autowired InstaAccountRepository repository) {
        this.service = service;
        this.repository = repository;
    }
    
    
    @Test 
    public void getAll() {
        Flux<InstaAccount> saved = repository.saveAll(Flux.just(new InstaAccount(null, "Gaurav"), new InstaAccount(null, "Rahul"), new InstaAccount(null, "Jyoti")));

        Flux<InstaAccount> composite = service.all().thenMany(saved);

        Predicate<InstaAccount> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        StepVerifier
            .create(composite) 
            .expectNextMatches(match)  
            .expectNextMatches(match)
            .expectNextMatches(match)
            .verifyComplete(); 
    }
    
    @Test
    public void save() {
        Mono<InstaAccount> profileMono = this.service.create("email@email.com");
        StepVerifier
            .create(profileMono)
            .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
            .verifyComplete();
    }

    @Test
    public void delete() {
        String test = "test";
        Mono<InstaAccount> deleted = this.service
            .create(test)
            .flatMap(saved -> this.service.delete(saved.getId()));
        StepVerifier
            .create(deleted)
            .expectNextMatches(profile -> profile.getEmail().equalsIgnoreCase(test))
            .verifyComplete();
    }

    @Test
    public void update() throws Exception {
        Mono<InstaAccount> saved = this.service
            .create("test")
            .flatMap(p -> this.service.update(p.getId(), "test1"));
        StepVerifier
            .create(saved)
            .expectNextMatches(p -> p.getEmail().equalsIgnoreCase("test1"))
            .verifyComplete();
    }

    @Test
    public void getById() {
        String test = UUID.randomUUID().toString();
        Mono<InstaAccount> deleted = this.service
            .create(test)
            .flatMap(saved -> this.service.get(saved.getId()));
        StepVerifier
            .create(deleted)
            .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && test.equalsIgnoreCase(profile.getEmail()))
            .verifyComplete();
    }
}
