package com.study.security6.domain.comment.entity;

import com.study.security6.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column
    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", updatable = false, insertable = false)
    private Comment parent;

    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", updatable = false, insertable = false)
    private Board board;

    public void updateContent(String updateContent){
        this.content = updateContent;
    }

    Comment(Long id, String content, Long parentId, Long boardId) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.boardId = boardId;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private Long id;
        private String  content;
        private Long parentId;
        private Long boardId;

        public Comment build(){
            return new Comment(id, content, parentId, boardId);
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder content(String  content) {
            this.content = content;
            return this;
        }

        public Builder parentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder boardId(Long boardId) {
            this.boardId = boardId;
            return this;
        }
    }
}