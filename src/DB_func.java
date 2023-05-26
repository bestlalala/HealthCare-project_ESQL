import java.sql.SQLException;

public interface DB_func {
    public abstract void select() throws SQLException;
    public abstract void insert();
    public abstract void update(String id);
    public abstract void delete() throws SQLException;
}
