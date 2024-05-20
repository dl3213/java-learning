package code.sibyl.database;

import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.jdbcclient.JDBCPool;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;
import io.vertx.sqlclient.PoolOptions;

public class EosClient extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", EosClient.class.getName());
    }

    @Override
    public void start() throws Exception {
        long start = System.currentTimeMillis();
        JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&useUnicode=true&autoReconnect=true&serverTimezone=Asia/Shanghai")
//                .put("driver_class", "io.vertx.mysqlclient.spi.MySQLDriver")
                .put("driver_class", "io.vertx.mysqlclient.spi.MySQLDriver")
                .put("user", "root")
                .put("password", "root");

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("127.0.0.1")
                .setDatabase("test")
                .setUser("root")
                .setPassword("test");

        // Pool options
        PoolOptions poolOptions = new PoolOptions().setMaxSize(32);
//        JDBCPool pool = JDBCPool.pool(vertx, config);
        JDBCPool pool = JDBCPool.pool(vertx, config);

        Maybe<RowSet<Row>> resa = pool.rxWithConnection(conn -> conn
                .query("select now()")
                .rxExecute()
                .flatMap(res -> conn.query("SELECT * FROM test_20240520").rxExecute())
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
            System.err.println("cost => " + (System.currentTimeMillis() - start)); //
        }, err -> {
            System.out.println("Database problem");
            err.printStackTrace();
        });
    }
}
