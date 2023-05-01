package com.fhilype.webfluxcourse.services;

import com.fhilype.webfluxcourse.entities.User;
import com.fhilype.webfluxcourse.mappers.UserMapper;
import com.fhilype.webfluxcourse.models.requests.UserRequest;
import com.fhilype.webfluxcourse.repositories.UserRepository;
import com.fhilype.webfluxcourse.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    public Mono<User> find(final String id) {
        return handleNotFound(repository.findById(id), id);
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request) {
        return find(id)
                .map(user -> mapper.toEntity(request, user))
                .flatMap(repository::save);
    }

    public Mono<User> delete(final String id) {
        return handleNotFound(repository.findAndRemove(id), id);
    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id) {
        return mono.switchIfEmpty(Mono.error(
                new ObjectNotFoundException(
                        format("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName()))
        ));
    }

}
