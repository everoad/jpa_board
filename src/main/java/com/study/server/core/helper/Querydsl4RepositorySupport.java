package com.study.server.core.helper;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Querydsl 4.x 버전에 맞춘 Querydsl 지원 라이브러리
 * (수정) Generic Type, Alias, Sort 기능 추가
 *
 * @author Younghan Kim
 * @author bjkim
 * @see org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
 */
public abstract class Querydsl4RepositorySupport {

    private final PathBuilder<?> builder;
    private Querydsl querydsl;
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    public Querydsl4RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.builder = new PathBuilderFactory().create(domainClass);
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(builder, "builder must not be null!");
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, builder);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }


    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery<T>> contentQuery) {
        JPAQuery<T> jpaQuery = contentQuery.apply(getQueryFactory());
        List<T> content = jpaQuery.orderBy(getOrderBy(pageable))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        return PageableExecutionUtils.getPage(content, pageable, () -> jpaQuery.fetch().size());
    }

    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery<T>> contentQuery,
                                          Function<JPAQueryFactory, JPAQuery<T>> countQuery) {
        JPAQuery<T> jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = jpaContentQuery.orderBy(getOrderBy(pageable))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        JPAQuery<T> jpaCountQuery = countQuery.apply(getQueryFactory());
        return PageableExecutionUtils.getPage(content, pageable, () -> jpaCountQuery.fetch().size());
    }

    protected <T> List<T> applyPaginationForList(Pageable pageable, Function<JPAQueryFactory, JPAQuery<T>> contentQuery) {
        JPAQuery<T> jpaQuery = contentQuery.apply(getQueryFactory());
        return jpaQuery.orderBy(getOrderBy(pageable))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
    }

    protected <T> Slice<T> applyPaginationForSlice(Pageable pageable, Function<JPAQueryFactory, JPAQuery<T>> contentQuery) {
        JPAQuery<T> jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = jpaContentQuery.orderBy(getOrderBy(pageable))
                .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();
        return toSlice(content, pageable);
    }

    protected <T> Expression<T> myAs(Expression<T> expression) {
        String expressionStr = expression.toString();
        String alias = convertPathToCamelCase(expressionStr.substring(expressionStr.indexOf(".") + 1));
        return ExpressionUtils.as(expression, alias);
    }

    protected BooleanExpression contains(StringPath expression, String keyword) {
        String[] items = keyword.trim().split(" ");
        BooleanExpression result = null;
        for (String item : items) {
            if (result == null) {
                result = expression.contains(item);
            } else {
                result = result.and(expression.contains(item));
            }
        }
        return result;
    }


    private OrderSpecifier<?>[] getOrderBy(Pageable pageable) {
        List<Sort.Order> sorts = pageable.getSort().toList();
        OrderSpecifier<?>[] orderSpecifiers = new OrderSpecifier[sorts.size()];
        for (int i = 0, iLen = sorts.size(); i < iLen; i++) {
            Sort.Order sort = sorts.get(i);
            orderSpecifiers[i] = new OrderSpecifier<>(
                    sort.isAscending() ? Order.ASC : Order.DESC,
                    Expressions.stringPath(convertPathToCamelCase(sort.getProperty())),
                    convertNullHandling(sort.getNullHandling())
            );
        }
        return orderSpecifiers;
    }

    private OrderSpecifier.NullHandling convertNullHandling(Sort.NullHandling sortNullHandling) {
        return switch (sortNullHandling) {
            case NULLS_FIRST -> OrderSpecifier.NullHandling.NullsFirst;
            case NULLS_LAST -> OrderSpecifier.NullHandling.NullsLast;
            default -> OrderSpecifier.NullHandling.Default;
        };
    }

    private String convertPathToCamelCase(String searchPath) {
        Pattern pattern = Pattern.compile("[.][a-z+]");
        Matcher matcher = pattern.matcher(searchPath);
        return matcher.replaceAll(matchResult -> matchResult.group().replace(".", "").toUpperCase());
    }

    protected <T> Slice<T> toSlice(List<T> content, Pageable pageable) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

}
