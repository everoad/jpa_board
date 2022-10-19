package com.study.server.domain.entity;

import com.study.server.domain.entity.base.BaseEntity;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", nullable = false, updatable = false)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String path;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    public Menu(String name, String path, Integer position, Category category) {
        Assert.hasText(name, "name is empty");
        Assert.hasText(path, "path is empty");
        Assert.notNull(position, "position is null");
        Assert.notNull(category, "category is null");
        this.name = name;
        this.path = path;
        this.position = position;
        this.category = category;
    }
}
