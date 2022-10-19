package com.study.server.domain.entity.board;

import com.study.server.domain.dto.board.BoardDetailDto;
import com.study.server.domain.dto.board.BoardFileDto;
import com.study.server.domain.entity.Menu;
import com.study.server.domain.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false, updatable = false)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BoardFile> fileList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder
    public Board(String title, String content, List<BoardFileDto> fileList, Menu menu) {
        Assert.hasText(title, "title is empty.");
        Assert.hasText(content, "content is empty.");
        Assert.notNull(fileList, "fileList is null.");
        this.title = title;
        this.content = content;
        this.menu = menu;
        this.updateFileList(fileList);
    }

    public void update(BoardDetailDto boardDetailDto) {
        this.title = boardDetailDto.getTitle();
        this.content = boardDetailDto.getContent();
        this.updateFileList(boardDetailDto.getFileList());
    }

    private void updateFileList(List<BoardFileDto> fileList) {
        Map<Integer, BoardFile> fileMap = this.fileList.stream()
                .collect(Collectors.toMap(BoardFile::getId, entity -> entity));

        List<BoardFile> newFileList = fileList.stream()
                .map(dto -> fileMap.containsKey(dto.getFileId()) ? fileMap.get(dto.getFileId()) : dto.toEntity(this))
                .toList();

        this.fileList.clear();
        this.fileList.addAll(newFileList);
    }
}
