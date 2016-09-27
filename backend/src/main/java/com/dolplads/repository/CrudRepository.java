package com.dolplads.repository;

import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dolplads on 26/09/16.
 */
@NoArgsConstructor
public abstract class CrudRepository<T> {
    @PersistenceContext
    protected EntityManager entityManager;
    private Class<T> entityClass;

    public CrudRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T findById(Long id) {
        return entityManager.find(entityClass, id);
    }

    public void remove(@NotNull T entity) {
        entityManager.remove(update(entity)); // to avoid issue with detached objects
    }

    public T update(T entity) {
        return entityManager.merge(entity);
    }

    public List<T> findAll() {
        return queryForAll().getResultList();
    }

    public List<T> findAllPaginated(Integer start, Integer nOfElements) {
        TypedQuery<T> query = queryForAll();

        query.setFirstResult(start);
        query.setMaxResults(nOfElements);

        return query.getResultList();
    }

    private TypedQuery<T> queryForAll() {
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
        return entityManager.createQuery(criteriaQuery.select(criteriaQuery.from(entityClass)));
    }
}
