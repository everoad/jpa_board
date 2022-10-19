package com.study.server.modules.menu;

import com.study.server.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
