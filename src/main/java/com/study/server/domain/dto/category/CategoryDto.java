package com.study.server.domain.dto.category;

import com.study.server.domain.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    private String name;
    private String path;
    private Integer position;

    @Builder
    public CategoryDto(Integer id, String name, String path, Integer position) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.position = position;
    }

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .path(path)
                .position(position)
                .build();
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .path(category.getPath())
                .position(category.getPosition())
                .build();
    }
}
