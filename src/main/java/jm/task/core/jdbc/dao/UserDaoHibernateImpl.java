package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;
    public UserDaoHibernateImpl() {
        this.sessionFactory =  Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query =
                    """
                            CREATE TABLE IF NOT EXISTS `mydbtest`.`users` (
                              `id` INT NOT NULL AUTO_INCREMENT,
                              `name` VARCHAR(45) NULL,
                              `lastname` VARCHAR(45) NULL,
                              `age` INT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);""";
            session.createSQLQuery(query).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String query  = "DROP TABLE IF EXISTS users";
            session.createSQLQuery(query).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            User u = session.get(User.class, id);
            session.delete(u);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            List<User> uList =  session.createQuery("from User", User.class).list();
            session.getTransaction().commit();
            return  uList;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            String query  = "DROP TABLE IF EXISTS users";
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }
}
