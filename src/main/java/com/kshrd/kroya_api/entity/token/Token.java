package com.kshrd.kroya_api.entity.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshrd.kroya_api.entity.UserEntity;
import com.kshrd.kroya_api.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token_tb")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "token_id")
    public Integer tokenId;

    @Column(name = "token", unique = true)
    public String token;

    @Column(name = "token_expired")
    public boolean tokenExpired;

    @Column(name = "token_revoked")
    public boolean tokenRevoked;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    public TokenType tokenType = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserEntity user;
}
