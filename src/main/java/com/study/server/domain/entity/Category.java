package com.study.server.domain.entity;

import com.study.server.domain.dto.category.CategoryDto;
import com.study.server.domain.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, updatable = false)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String path;

    @Column(nullable = false)
    private Integer position;

    @Builder
    public Category(String name, String path, Integer position) {
        Assert.hasText(name, "name is empty");
        Assert.hasText(path, "path is empty");
        Assert.notNull(position, "position is null");
        this.name = name;
        this.path = path;
        this.position = position;
    }

    public void update(CategoryDto categoryDto) {
        this.name = categoryDto.getName();
        this.path = categoryDto.getPath();
        this.position = categoryDto.getPosition();
    }
}
