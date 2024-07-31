package com.study.security6.domain.board.entity;

import com.study.security6.domain.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

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
