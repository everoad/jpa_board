package com.study.server.modules.base;

import com.study.server.modules.category.CategoryRepository;
import com.study.server.modules.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BaseService {

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;

}
