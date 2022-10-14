package com.study.server.domain.entity.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_file_id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(length = 50, nullable = false)
    private String originName;

    @Column(length = 100, nullable = false)
    private String path;

    @Builder
    public BoardFile(Integer id, Board board, String originName, String path) {
        Assert.notNull(board, "board is null.");
        Assert.hasText(originName, "originName is empty.");
        Assert.hasText(path, "path is empty.");
        this.id = id;
        this.board = board;
        this.originName = originName;
        this.path = path;
    }

}
