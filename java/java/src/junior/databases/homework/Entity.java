package junior.databases.homework;

import java.util.*;
import java.sql.*;
import java.lang.reflect.Constructor;

public abstract class Entity {
    private static String DELETE_QUERY   = "DELETE FROM \"%1$s\" WHERE %1$s_id=?";
    private static String INSERT_QUERY   = "INSERT INTO \"%1$s\" (%2$s) VALUES (%3$s) RETURNING %1$s_id";
    private static String LIST_QUERY     = "SELECT * FROM \"%s\"";
    private static String SELECT_QUERY   = "SELECT * FROM \"%1$s\" WHERE %1$s_id=?";
    private static String CHILDREN_QUERY = "SELECT * FROM \"%1$s\" WHERE %2$s_id=?";
    private static String SIBLINGS_QUERY = "SELECT * FROM \"%1$s\" NATURAL JOIN \"%2$s\" WHERE %3$s_id=?";
    private static String UPDATE_QUERY   = "UPDATE \"%1$s\" SET %2$s WHERE %1$s_id=?";

    private static Connection db = null;

    protected boolean isLoaded = false;
    protected boolean isModified = false;
    private String table = null;
    private Integer id = null;
    protected Map<String, Object> fields = new HashMap<String, Object>();


    public Entity() {
        this.table = this.getClass().getSimpleName();
    }

    public Entity(Integer id) {
        this.id = id;
        this.table = this.getClass().getSimpleName();
    }

    public static final void setDatabase(Connection connection) {
    // throws NullPointerException
        if ( connection != null ) {
            db = connection;
        } else {
            throw new NullPointerException();
        }
    }

    public final int getId() {
        // try to guess youtself
        return (int) fields.get(table + "_id");
    }

    public final java.util.Date getCreated() {
        // try to guess youtself

        // getDate(table + "_created");
        return (java.util.Date) fields.get(table + "_created");
    }

    public final java.util.Date getUpdated() {
        // try to guess youtself
        
        getDate(table + "_updated");
        // return (java.util.Date) fields.get(table + "_updated");
    }

    public final Object getColumn(String name) {
        // return column name from fields by key
        return (Object) fields.get(name);
    }

    public final <T extends Entity> T getParent(Class<T> cls) {
        // get parent id from fields as <classname>_id, create and return an instance of class T with that id
    }

    public final <T extends Entity> List<T> getChildren(Class<T> cls) {
        // select needed rows and ALL columns from corresponding table
        // convert each row from ResultSet to instance of class T with appropriate id
        // fill each of new instances with column data
        // return list of children instances
    }

    public final <T extends Entity> List<T> getSiblings(Class<T> cls) {
        // select needed rows and ALL columns from corresponding table
        // convert each row from ResultSet to instance of class T with appropriate id
        // fill each of new instances with column data
        // return list of sibling instances
    }

    public final void setColumn(String name, Object value) {
        // put a value into fields with <table>_<name> as a key
        fields.put((table + name), value);
    }

    public final void setParent(String name, Integer id) {
        // put parent id into fields with <name>_<id> as a key
        fields.put(name + "_id", id);
    }

    private void load() {
        // check, if current object is already loaded
        // get a single row from corresponding table by id
        // store columns as object fields with unchanged column names as keys
    }

    private void insert() throws SQLException {
        // execute an insert query, built from fields keys and values

        db.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        db.setAutoCommit(false);

        try {
            PreparedStatement st = null;
            String insert = String.format(INSERT_QUERY, this.table, 
                join(fields.keySet()), join(genPlaceholders(fields.size())));

            st = db.prepareStatement(insert);
            Set values = fields.values();
            int counter = 1;

            for ( Object value: values ) {
                st.setObject(counter++, value);
            }
            st.executeUpdate();
            st.close();

        } catch ( SQLException e ) {
            db.rollback();
            throw e;
        }

    }

    private void update() throws SQLException {
        // execute an update query, built from fields keys and values

        db.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        db.setAutoCommit(false);

        try {
            PreparedStatement st = null;
            String update = String.format(UPDATE_QUERY, this.table, join(fields.keySet()), join(genPlaceholders(fields.size()))));

            st = db.prepareStatement(update);
            Set values = fields.values();
            int counter = 1;

            for ( Object value: values ) {
                st.setObject(counter++, value);
            }
            st.executeUpdate();
            st.close();

        } catch ( SQLException e ) {
            db.rollback();
            throw e;
        }

    }

    public final void delete() throws SQLException {
        // execute a delete query with current instance id

        db.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        db.setAutoCommit(false);

        try {
            PreparedStatement st = null;
            String delete = String.format(DELETE_QUERY, this.table);

            st = db.prepareStatement(delete);
            st.setInt(1, this.id);
            st.executeUpdate();
            st.close();

        } catch ( SQLException e ) {
            db.rollback();
            throw e;
        }
    }

    public final void save() throws SQLException {
        // execute either insert or update query, depending on instance id
        
    }

    protected static <T extends Entity> List<T> all(Class<T> cls) {
        // select ALL rows and ALL columns from corresponding table
        // convert each row from ResultSet to instance of class T with appropriate id
        // fill each of new instances with column data
        // aggregate all new instances into a single List<T> and return it
    }

    private static Collection<String> genPlaceholders(int size) {
        // return a string, consisting of <size> "?" symbols, joined with ", "
        // each "?" is used in insert statements as a placeholder for values (google prepared statements)

        List<String> list = new ArrayList<String>();

        for ( int i = 0; i < size; i++ ) list.add("?");
        return list;
    }

    private static Collection<String> genPlaceholders(int size, String placeholder) {
        // return a string, consisting of <size> <placeholder> symbols, joined with ", "
        // each <placeholder> is used in insert statements as a placeholder for values (google prepared statements)
        List<String> list = new ArrayList<String>();

        for ( int i = 0; i < size; i++ ) {
            list.add(placeholder);
        }
        return list;
    }

    private static String getJoinTableName(String leftTable, String rightTable) {
        // generate the name of associative table for many-to-many relation
        // sort left and right tables alphabetically
        // return table name using format <table>__<table>
    }

    private java.util.Date getDate(String column) {
        // pwoerful method, used to remove copypaste from getCreated and getUpdated methods
        return (java.util.Date) fields.get(column);
    }

    private static String join(Collection<String> sequence) {
        // join collection of strings with ", " as glue and return a joined string
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String element : sequence) {
            if (!isFirst) {
                sb.append(',');
            } else {
                isFirst = false;
            }
            sb.append(element);
        }
        return sb.toString();
    }

    private static String join(Collection<String> sequence, String glue) {
        // join collection of strings with glue and return a joined string
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String element : sequence) {
            if (!isFirst) {
                sb.append(glue);
            } else {
                isFirst = false;
            }
            sb.append(element);
        }
        return sb.toString();
    }

    private static <T extends Entity> List<T> rowsToEntities(Class<T> cls, ResultSet rows) {
        // convert a ResultSet of database rows to list of instances of corresponding class
        // each instance must be filled with its data so that it must not produce additional queries to database to get it's fields
    }
}
