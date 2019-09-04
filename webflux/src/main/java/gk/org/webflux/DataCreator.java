package gk.org.webflux;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import gk.org.webflux.repository.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2
@Component
@org.springframework.context.annotation.Profile("demo")
public class DataCreator implements ApplicationListener<ApplicationReadyEvent>{
	
	
	private  final ProfileRepository repository;
	
	public DataCreator(ProfileRepository repository) {
        this.repository = repository;
    }

	 @Override
	    public void onApplicationEvent(ApplicationReadyEvent event) {
	        repository
	            .deleteAll()
	            .thenMany(
	                Flux
	                    .just("A", "B", "C", "D") 
	                    .map(name -> new Profile(UUID.randomUUID().toString(), name + "@email.com")) 
	                    .flatMap(repository::save) 
	            )
	            .thenMany(repository.findAll()) 
	            .subscribe(profile -> log.info("Persisting " + profile.toString()));
	    }

}
