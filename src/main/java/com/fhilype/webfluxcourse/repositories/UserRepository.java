package com.fhilype.webfluxcourse.repositories;

import com.fhilype.webfluxcourse.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final ReactiveMongoTemplate template;

    public Mono<User> save(final User user) {
        return template.save(user);
    }

    public Mono<User> findById(String id) {
        return template.findById(id, User.class);
    }

    public Flux<User> findAll() {
        return template.findAll(User.class);
    }

    public Mono<User> findAndRemove(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        return template.findAndRemove(query.addCriteria(criteria), User.class);
    }
}
