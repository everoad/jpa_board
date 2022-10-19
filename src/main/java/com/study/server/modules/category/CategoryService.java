package com.study.server.modules.category;

import com.study.server.domain.dto.category.CategoryDto;
import com.study.server.domain.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategoryList() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::toDto).toList();
    }

    @Transactional
    public void addCategory(CategoryDto categoryDto) {
        Category category = categoryDto.toEntity();
        categoryRepository.save(category);
    }

    @Transactional
    public void editCategory(Integer id, CategoryDto categoryDto) {
        Category category = getCategory(id);
        category.update(categoryDto);
    }

    @Transactional
    public void removeCategory(Integer id) {
        Category category = getCategory(id);
        categoryRepository.delete(category);
    }

    private Category getCategory(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다.");
        }
        return optionalCategory.get();
    }
}
