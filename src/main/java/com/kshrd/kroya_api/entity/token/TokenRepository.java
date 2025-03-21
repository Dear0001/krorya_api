package com.kshrd.kroya_api.entity.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token t inner join UserEntity u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.tokenExpired = false or t.tokenRevoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);
}
