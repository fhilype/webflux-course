package com.fhilype.webfluxcourse.controllers.impls;

import com.fhilype.webfluxcourse.controllers.UserController;
import com.fhilype.webfluxcourse.mappers.UserMapper;
import com.fhilype.webfluxcourse.models.requests.UserRequest;
import com.fhilype.webfluxcourse.models.responses.UserResponse;
import com.fhilype.webfluxcourse.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService service;

    private final UserMapper mapper;

    @Override
    public ResponseEntity<Mono<Void>> save(final UserRequest request) {
        return ResponseEntity.status(CREATED)
                .body(service.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> find(final String id) {
        return ResponseEntity.ok()
                .body(service.find(id).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.ok()
                .body(service.findAll().map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(final String id, final UserRequest request) {
        return ResponseEntity.ok()
                .body(service.update(id, request).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(final String id) {
        return ResponseEntity.ok()
                .body(service.delete(id).then());
    }

}
