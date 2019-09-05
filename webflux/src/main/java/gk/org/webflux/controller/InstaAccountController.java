package gk.org.webflux.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gk.org.webflux.entities.InstaAccount;
import gk.org.webflux.service.InstaAccountService;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController 
@RequestMapping(value = "/instaaccount", produces = MediaType.APPLICATION_JSON_VALUE)  
@org.springframework.context.annotation.Profile("classic")
public class InstaAccountController {
	
	
	 private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
	    private final InstaAccountService instaAccountService;

	    InstaAccountController(InstaAccountService profileRepository) {
	        this.instaAccountService = profileRepository;
	    }

	    
	    @GetMapping
	    Publisher<InstaAccount> getAll() {
	        return this.instaAccountService.all();
	    }

	    
	    @GetMapping("/{id}")
	    Publisher<InstaAccount> getById(@PathVariable("id") String id) {
	        return this.instaAccountService.get(id);
	    }
	    
	    @PostMapping
	    Publisher<ResponseEntity<InstaAccount>> create(@RequestBody InstaAccount profile) {
	        return this.instaAccountService
	            .create(profile.getEmail())
	            .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
	                .contentType(mediaType)
	                .build());
	    }

	    @DeleteMapping("/{id}")
	    Publisher<InstaAccount> deleteById(@PathVariable String id) {
	        return this.instaAccountService.delete(id);
	    }

	    @PutMapping("/{id}")
	    Publisher<ResponseEntity<InstaAccount>> updateById(@PathVariable String id, @RequestBody InstaAccount profile) {
	        return Mono
	            .just(profile)
	            .flatMap(p -> this.instaAccountService.update(id, p.getEmail()))
	            .map(p -> ResponseEntity
	                .ok()
	                .contentType(this.mediaType)
	                .build());
	    }

}
