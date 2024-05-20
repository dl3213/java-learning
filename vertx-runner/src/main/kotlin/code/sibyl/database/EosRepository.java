package code.sibyl.database;

import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.rxjava3.jdbcclient.JDBCPool;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class EosRepository {

    public void builder(Vertx vertx) {
        long start = System.currentTimeMillis();
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("127.0.0.1")
                .setDatabase("test")
                .setUser("root")
                .setPassword("test");

        // Pool options
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        // Create the pooled client
        Pool pool = MySQLBuilder.pool().with(poolOptions).connectingTo(connectOptions).using(vertx).build();

        //pool.query()

        // Get a connection from the pool
        pool.getConnection().compose(conn -> {
            System.out.println("Got a connection from the pool");

            // All operations execute on the same connection
            return conn.query("SELECT * FROM test_20240520 WHERE 1=1").execute()
                    .compose(res -> conn.query("SELECT * FROM test_20240520 WHERE 1=1").execute())
                    .map(rows -> {
                        System.err.println(rows.next());
                        return rows;
                    })
                    .onSuccess(rows -> {
                    })
                    .onComplete(ar -> {
                        // Release the connection to the pool
                        System.err.println(ar.result().size());
                        ar.result().forEach(row ->{
                            System.err.println(row.toJson());
                        });
                        conn.close();
                    });
        }).onComplete(ar -> {
            if (ar.succeeded()) {
                System.out.println("Done");
                System.err.println("cost => " + (System.currentTimeMillis() - start));
            } else {
                System.out.println("Something went wrong " + ar.cause().getMessage());
            }
        });
    }


    public void builder(io.vertx.rxjava3.core.Vertx vertx) {
        long start = System.currentTimeMillis();
        JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&useUnicode=true&autoReconnect=true&serverTimezone=Asia/Shanghai")
//                .put("driver_class", "io.vertx.mysqlclient.spi.MySQLDriver")
                .put("driver_class", "io.vertx.mysqlclient.spi.MySQLDriver")
                .put("user", "root")
                .put("password", "root");

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

        Maybe<RowSet<Row>> resa = pool.rxWithConnection(conn -> conn
                .query("SELECT * FROM test_20240520 WHERE 1=1")
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
