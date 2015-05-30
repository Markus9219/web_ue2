package models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.log4j.LogManager;

import org.apache.log4j.Logger;
import play.db.jpa.JPA;

/**
 * Provides Data Access methods for JPA
 */
public class JeopardyDAO implements IGameDAO {
    public static final JeopardyDAO INSTANCE = new JeopardyDAO();
    public static final Logger log = LogManager.getLogger(JeopardyDAO.class);
    private JeopardyDAO() { }
    
    /**
     * Get a given quiz user based on the id
     * @param id
     * @return
     */
    public JeopardyUser findById(long id) {
        return em().find(JeopardyUser.class, id);
    }

    public JeopardyUser findByUserName(String name) {
        if (name != null && !name.isEmpty()) {
            return getByUserName(name);
        } else {
            return null;
        }
    }

    /**
     * Get a given quiz user based on the name
     * @param name
     * @return
     */
    private JeopardyUser getByUserName(String name) {
        String queryStr = "from JeopardyUser where userName = :userName";
        TypedQuery<JeopardyUser> query = em().createQuery(queryStr,
                JeopardyUser.class).setParameter("userName", name);
        List<JeopardyUser> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }


    /**
     * Save an entity. Throws an error if an entity with the given id already exists
     * @param entity
     * @return
     */
    @Override
    public void persist(BaseEntity entity) {
        // TODO: Implement Method abc
        log.debug("persisting " + entity);
        em().persist(entity);
    }


    /**
     * If no entity with the given id exists in the DB, a new entity is stored. If there is already
     * an entity with the given id, the entity is updated
     * @param entity
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseEntity> T merge(T entity) {
        // TODO: Implement Method
        return em().merge(entity);
    }

    /**
     * Get an entity of the given type using the id
     * @param id
     * @param entityClazz
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseEntity> T findEntity(Long id, Class<T> entityClazz) {
        // TODO: Implement Method
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClazz);
        Root<T> c = q.from(entityClazz);

        ParameterExpression<Long> p = cb.parameter(Long.class);
        q.select(c).where(cb.equal(c.get("id"), p));

        TypedQuery<T> query = em().createQuery(q);
        query.setParameter(p, id);

        T result = query.getSingleResult();

        return result;
    }


    /**
     * Get a list of all entities of a certain type
     *
     * @param entityClazz
     * @param <E>
     * @return
     */
    @Override
    public <E extends BaseEntity> List<E> findEntities(Class<E> entityClazz) {
        // TODO: Implement Method
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<E> q = cb.createQuery(entityClazz);
        Root<E> c = q.from(entityClazz);

        ParameterExpression<Long> p = cb.parameter(Long.class);
        q.select(c);

        TypedQuery<E> query = em().createQuery(q);

        List<E> result = query.getResultList();

        return result;
    }

    /**
     * Get the entity manager
     * @return
     */
    private EntityManager em() {
        return JPA.em();
    }

}
