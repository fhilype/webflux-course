package com.fhilype.webfluxcourse.services;

import com.fhilype.webfluxcourse.entities.User;
import com.fhilype.webfluxcourse.mappers.UserMapper;
import com.fhilype.webfluxcourse.models.requests.UserRequest;
import com.fhilype.webfluxcourse.repositories.UserRepository;
import com.fhilype.webfluxcourse.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void save() {
        UserRequest request = new UserRequest("Atos", "atos@email.com", "123");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void findById() {
        when(repository.findById(anyString())).thenReturn(Mono.just(User.builder()
                .id("123")
                .build()));

        Mono<User> result = service.find("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class
                        && user.getId().equals("123"))
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findById(anyString());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findAll();
    }

    @Test
    void update() {
        UserRequest request = new UserRequest("Atos", "atos@email.com", "123");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(entity);
        when(repository.findById(anyString())).thenReturn(Mono.just(entity));
        when(repository.save(any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = service.update("123", request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void delete() {
        User entity = User.builder().build();
        when(repository.findAndRemove(anyString())).thenReturn(Mono.just(entity));

        Mono<User> result = service.delete("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, times(1)).findAndRemove(anyString());
    }

    @Test
    void testHandleNotFound() {
        when(repository.findById(anyString())).thenReturn(Mono.empty());
        try {
            service.find("123").block();
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(format("Object not found. Id: %s, Type: %s", "123", User.class.getSimpleName()), e.getMessage());
        }

    }

}