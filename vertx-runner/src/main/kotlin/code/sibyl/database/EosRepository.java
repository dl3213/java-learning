package code.sibyl.database;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
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

public class EosRepository {

    public void builder(Vertx vertx) {
        FileSystem fileSystem = vertx.fileSystem();
        Buffer buffer = fileSystem.readFileBlocking("db.json");
        JsonObject jsonObject = new JsonObject(buffer);
        System.err.println(jsonObject);
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(jsonObject.getInteger("port"))
                .setHost(jsonObject.getString("ip"))
                .setDatabase(jsonObject.getString("database"))
                .setUser(jsonObject.getString("username"))
                .setPassword(jsonObject.getString("password"));

        // Pool options
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        // Create the pooled client
        Pool pool = MySQLBuilder.pool().with(poolOptions).connectingTo(connectOptions).using(vertx).build();

        //pool.query()
        long start = System.currentTimeMillis();
        // Get a connection from the pool
        pool.getConnection().compose(conn -> {
            System.out.println("Got a connection from the pool");
            ;
            // All operations execute on the same connection
            return conn.query("SELECT * FROM th_crm_rent_out where is_del = '0'").execute()
                    .compose(res -> conn.query("SELECT * FROM th_crm_rent_out where is_del = '0'").execute())
                    .onSuccess(rows -> {
                    })
                    .onComplete(ar -> {
                        // Release the connection to the pool
                        System.err.println(ar.result().size());
                        ar.result().forEach(row -> {
                            System.err.println(row.toJson());
                        });
                        conn.close();
                    });
        }).onComplete(ar -> {
            if (ar.succeeded()) {
                System.out.println("Done");
                System.err.println(ar.result().size());
                System.err.println("cost => " + (System.currentTimeMillis() - start));
            } else {
                System.out.println("Something went wrong " + ar.cause().getMessage());
            }
        });
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
