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

    /* logic */

    public void modifyPassword(String password){
        this.password = password;
    }

    /* builder */

    public static Builder builder(){
        return new Builder();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    static public class Builder{
        private String username;
        private String password;

        public User build(){
            return new User(username, password);
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }
    }
}
