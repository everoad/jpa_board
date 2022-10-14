package com.study.server.modules.board.repository;

import com.study.server.core.helper.Querydsl4RepositorySupport;
import com.study.server.core.model.BaseSearch;
import com.study.server.domain.dto.board.BoardListDto;
import com.study.server.domain.dto.board.QBoardListDto;
import com.study.server.domain.entity.board.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.study.server.domain.entity.board.QBoard.board;
import static com.study.server.domain.entity.QMember.*;

@Repository
public class BoardQueryRepository extends Querydsl4RepositorySupport {

    public BoardQueryRepository() {
        super(Board.class);
    }

    public List<BoardListDto> findAll(BaseSearch search, Pageable pageable) {
        return applyPaginationForList(pageable, query -> query
                .select(
                        new QBoardListDto(
                                board.id,
                                board.title,
                                member.id,
                                member.name,
                                board.createdTime,
                                board.modifiedTime
                        )
                )
                .from(board)
                .join(member).on(board.createdBy.id.eq(member.id))
                .where(
                        keywordLike(search.getKeyword())
                )
        );
    }

    private BooleanExpression keywordLike(String keyword) {
        return StringUtils.hasText(keyword) ? contains(board.title, keyword).or(contains(board.content, keyword)) : null;
    }

}
