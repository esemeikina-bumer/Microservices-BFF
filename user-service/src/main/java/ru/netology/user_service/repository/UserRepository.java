package ru.netology.user_service.repository;

import ru.netology.user_service.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    public UserRepository() {
        users.put(1L, new User(1L, "Иванов Иван Иванович",
                "г. Москва, ул. Ленина, д. 1, кв. 10",
                "+7-900-111-22-33", "ivanov@example.com"));
        users.put(2L, new User(2L, "Петров Пётр Петрович",
                "г. Санкт-Петербург, ул. Мира, д. 5, кв. 42",
                "+7-900-444-55-66", "petrov@example.com"));
        users.put(3L, new User(3L, "Сидорова Анна Сергеевна",
                "г. Казань, ул. Баумана, д. 12",
                "+7-900-777-88-99", "sidorova@example.com"));
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}