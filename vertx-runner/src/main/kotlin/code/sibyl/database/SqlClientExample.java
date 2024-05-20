package code.sibyl.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;

import io.vertx.sqlclient.*;

public class SqlClientExample extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        MySQLConnectOptions options = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("127.0.0.1")
                .setDatabase("test")
                .setUser("root")
                .setPassword("test")
                ;
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SqlClientExample(options));
    }

    private final SqlConnectOptions options;

    public SqlClientExample(SqlConnectOptions options) {
        this.options = options;
    }

    @Override
    public void start() {
        long start = System.currentTimeMillis();
        Pool pool = Pool.pool(vertx, options, new PoolOptions().setMaxSize(4));

        // create a test table
        pool.query("select now()")
                .execute()
                .compose(r ->
                        // insert some test data
                        pool
                                .query("select now()")
                                .execute()
                ).compose(r ->
                        // query some data with arguments
                        pool
                                .preparedQuery("select * from test_20240520 where 1=1")
                                .execute()
                ).onSuccess(rows -> {
                    for (Row row : rows) {
                        System.out.println("row = " + row.toJson());
                    }
                    System.err.println("cost => " + (System.currentTimeMillis() - start)); //cost => 7274
                }).onFailure(Throwable::printStackTrace);
    }
}
