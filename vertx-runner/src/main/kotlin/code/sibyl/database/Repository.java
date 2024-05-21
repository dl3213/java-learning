package code.sibyl.database;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.rxjava3.jdbcclient.JDBCPool;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlConnection;

public class Repository {

    private JsonObject config;
    private MySQLConnectOptions connectOptions;
    private PoolOptions poolOptions;
    private Pool pool;

    public JsonObject config(Vertx vertx) {
        FileSystem fileSystem = vertx.fileSystem();
        Buffer buffer = fileSystem.readFileBlocking("db.json");
        return new JsonObject(buffer);
    }

    public MySQLConnectOptions options(JsonObject jsonObject) {
        return new MySQLConnectOptions()
                .setPort(jsonObject.getInteger("port"))
                .setHost(jsonObject.getString("ip"))
                .setDatabase(jsonObject.getString("database"))
                .setUser(jsonObject.getString("username"))
                .setPassword(jsonObject.getString("password"));
    }

    public Pool pool(Vertx vertx, MySQLConnectOptions connectOptions, PoolOptions options) {
        return MySQLBuilder.pool().with(options).connectingTo(connectOptions).using(vertx).build();
    }

    public Future<SqlConnection> getConnection() {
        return this.pool.getConnection();
    }

    public Repository builder(Vertx vertx) {
        JsonObject jsonObject = this.config(vertx);
        this.config = jsonObject;
        MySQLConnectOptions connectOptions = this.options(jsonObject);
        this.connectOptions = connectOptions;
        PoolOptions poolOptions = new PoolOptions().setMaxSize(32);
        this.poolOptions = poolOptions;
        Pool pooled = this.pool(vertx, connectOptions, poolOptions);
        this.pool = pooled;
        return this;
    }

    public Future<io.vertx.sqlclient.RowSet<io.vertx.sqlclient.Row>> sql(String sql) {
        return this.pool.withConnection(con -> con.query(sql).execute());
    }

    public void test() {
        long start = System.currentTimeMillis();
        pool.getConnection()
                .compose(conn -> conn.query("SELECT * FROM th_crm_rent_out where is_del = '0'").execute())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Done");
                        System.err.println(ar.result().size());
                        System.err.println("cost => " + (System.currentTimeMillis() - start));
                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                });
    }


    public @NonNull Single<JsonObject> config(io.vertx.rxjava3.core.Vertx vertx) {
        return vertx.fileSystem().rxReadFile("db.json").map(buffer -> new JsonObject(buffer.toString()));
    }

    public void builder(io.vertx.rxjava3.core.Vertx vertx) {
        io.vertx.rxjava3.core.file.FileSystem filedSystem = vertx.fileSystem();
        Single<io.vertx.rxjava3.core.buffer.Buffer> single = filedSystem.rxReadFile("db.json");
        single.subscribe(buffer -> {
            JsonObject jsonObject = new JsonObject(buffer.toString());
            JsonObject config = new JsonObject()
                    .put("url", jsonObject.getString("url"))
//                .put("driver_class", "io.vertx.mysqlclient.spi.MySQLDriver")
                    .put("driver_class", jsonObject.getString("driver_class"))
                    .put("user", jsonObject.getString("username"))
                    .put("password", jsonObject.getString("password"));

            MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                    .setPort(3306)
                    .setHost("1127.0.0.1")
                    .setDatabase("test")
                    .setUser("root")
                    .setPassword("test");

            // Pool options
            PoolOptions poolOptions = new PoolOptions().setMaxSize(32);
//        JDBCPool pool = JDBCPool.pool(vertx, config);
            JDBCPool pool = JDBCPool.pool(vertx, config);
            long start = System.currentTimeMillis();
            Maybe<RowSet<Row>> resa = pool.rxWithConnection(conn -> conn
                    .query("SELECT * FROM th_crm_rent_out where is_del = '0'")
                    .rxExecute()
                    .flatMap(res -> conn.query("SELECT * FROM th_crm_rent_out where is_del = '0'").rxExecute())
//                .flatMap(res -> conn.query("SELECT * FROM test_20240520").rxExecute())
//                .flatMap(res -> conn.query("SELECT * FROM test_20240520").rxExecute())
                    .toMaybe());

            // Connect to the database
            resa.subscribe(rowSet -> {
                // Subscribe to the final result
                System.out.println("Results:");
                rowSet.forEach(row -> {
                    System.out.println(row.toJson());
                });
                System.err.println(rowSet.size());
                System.err.println("cost => " + (System.currentTimeMillis() - start)); //
            }, err -> {
                System.out.println("Database problem");
                err.printStackTrace();
            });
        });
    }
}
