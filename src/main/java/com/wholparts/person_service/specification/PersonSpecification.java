package com.wholparts.person_service.specification;

import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import com.wholparts.person_service.model.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

    public static Specification<Person> nameContains(
            String name
    ) {

        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Person> hasStatus(
            PersonStatus status
    ) {

        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Person> hasType(
            PersonType type
    ) {

        return (root, query, cb) ->
                cb.equal(root.get("type"), type);
    }
}
