package code.sibyl.database;

import code.sibyl.common.r;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.time.LocalDateTime;

public class Repository {

    private JsonObject config;
    private JDBCPool jdbcPool;

    private Repository() {
        System.out.println("Repository init => " + r.format(LocalDateTime.now(), r.yyyy_MM_dd_HH_mm_ss_SSS));
    }

    private static Repository instance = new Repository();

    public static Repository getInstance() {
        return instance;
    }

    public JsonObject config(Vertx vertx) {
        FileSystem fileSystem = vertx.fileSystem();
        Buffer buffer = fileSystem.readFileBlocking("db.json");
        return new JsonObject(buffer);
    }

    public JDBCPool jdbcPool(Vertx vertx, JsonObject jsonObject) {
        return JDBCPool.pool(vertx, jsonObject);
    }

    public JDBCPool jdbcPool() {
        return this.jdbcPool;
    }

    public Repository builder(Vertx vertx) {
        this.config = this.config(vertx);
        this.jdbcPool = this.jdbcPool(vertx, this.config);
        return this;
    }

    public void test() {
        String sql = "select now();";
        SqlTemplate
                .forQuery(Repository.getInstance().jdbcPool(), sql)
                .mapTo(row -> row.toJson())
                .execute(null)
                .onFailure(error -> error.printStackTrace())
                .onSuccess(rows -> rows.forEach(System.err::println));
    }

    public @NonNull Single<JsonObject> config(io.vertx.rxjava3.core.Vertx vertx) {
        return vertx.fileSystem().rxReadFile("db.json").map(buffer -> new JsonObject(buffer.toString()));
    }

    public void builder(io.vertx.rxjava3.core.Vertx vertx) {
        io.vertx.rxjava3.core.file.FileSystem filedSystem = vertx.fileSystem();
        Single<io.vertx.rxjava3.core.buffer.Buffer> single = filedSystem.rxReadFile("db.json");
        single.subscribe(buffer -> {
            JsonObject jsonObject = new JsonObject(buffer.toString());
//            JsonObject config = new JsonObject()
//                    .put("url", jsonObject.getString("url"))
////                .put("driver_class", "io.vertx.mysqlclient.spi.MySQLDriver")
//                    .put("driver_class", jsonObject.getString("driver_class"))
//                    .put("user", jsonObject.getString("user"))
//                    .put("password", jsonObject.getString("password"));


            // Pool options
            PoolOptions poolOptions = new PoolOptions().setMaxSize(32);
//        JDBCPool pool = JDBCPool.pool(vertx, config);
            io.vertx.rxjava3.jdbcclient.JDBCPool pool = io.vertx.rxjava3.jdbcclient.JDBCPool.pool(vertx, jsonObject);
            long start = System.currentTimeMillis();
            Maybe<RowSet<Row>> resa = pool.rxWithConnection(conn -> conn
                    .query("SELECT now()")
                    .rxExecute()
                    //.flatMap(res -> conn.query("SELECT * FROM th_crm_rent_out where is_del = '0'").rxExecute())
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
