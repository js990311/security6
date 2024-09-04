package com.study.security6.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "users")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String provider;

    /* logic */

    public void modifyPassword(String password){
        this.password = password;
    }

    /* builder */

    public static Builder builder(){
        return new Builder();
    }

    public User(String username, String password, String provider) {
        this.username = username;
        this.password = password;
        this.provider = provider;
    }

    static public class Builder{
        private String username;
        private String password;
        private String provider;

        public Builder() {
            this.provider = "local";
        }

        public User build(){
            return new User(username, password, provider);
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }
    }
}
