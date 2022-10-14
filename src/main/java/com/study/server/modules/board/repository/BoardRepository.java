package com.study.server.modules.board.repository;

import com.study.server.domain.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query(value = "select b from Board b where b.title = :title")
    Optional<Board> findByTitle(@Param("title") String title);

    @Query(value = "select b from Board b join BoardFile c on b.id = c.board.id where c.originName = :fileName")
    Optional<Board> findByFileName(@Param("fileName") String fileName);

}
