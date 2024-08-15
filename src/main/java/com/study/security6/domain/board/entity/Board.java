package com.study.security6.domain.board.entity;

import com.study.security6.domain.comment.entity.Comment;
import com.study.security6.domain.role.board.entity.BoardRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "boards")
public class Board {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardRole> roles;

    public void updateBoardName(String name){
        this.name = name;
    }

    Board(String name) {
        this.name = name;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String name;

        public Board build(){
            return new Board(name);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
    }
}
