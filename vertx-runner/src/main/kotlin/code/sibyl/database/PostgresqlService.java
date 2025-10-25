package code.sibyl.database;

import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;

public class PostgresqlService {

    public static void main(String[] args) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("127.0.0.1")
                .setDatabase("postgres")
                .setUser("postgres")
                .setPassword("sibyl-postgres-0127");

// Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

// Create the client pool
        SqlClient client = PgBuilder
                .client()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .build();

// A simple query
        client
                .query("SELECT * FROM t_sys_user WHERE 1=1")
                .execute()
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        System.out.println("Got " + result.size() + " rows ");
                    } else {
                        System.out.println("Failure: " + ar.cause().getMessage());
                    }

                    // Now close the pool
                    client.close();
                });
    }
}
