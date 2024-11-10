package code.sibyl.database;

import code.sibyl.common.Bean;
import code.sibyl.common.r;
import code.sibyl.service.BeanService;
import code.sibyl.service.SystemService;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.jdbcclient.JDBCPool;
import io.vertx.rxjava3.sqlclient.templates.SqlTemplate;
import lombok.Getter;

public class Repository implements Bean {

    @Getter
    private JsonObject config;
    @Getter
    private io.vertx.rxjava3.jdbcclient.JDBCPool jdbcPool;

    private Repository() {
        init();
    }

    private static Repository instance = new Repository();

    public static Repository getInstance() {
        return instance;
    }

    public Single<Repository> builder(io.vertx.rxjava3.core.Vertx vertx) {
        return vertx.fileSystem()
                .rxReadFile("db.json")
                .map(buffer -> {
                    this.config = new JsonObject(buffer.toString());
                    System.out.println("config -> " + config);
                    JDBCPool pool = JDBCPool.pool(vertx, this.config);
                    this.jdbcPool = pool;
                    return this;
                })
                .doOnSuccess(repository -> {
                    String sql = "select now();";
                    System.out.println(sql);
                    SqlTemplate
                            .forQuery(repository.getJdbcPool(), sql)
                            .execute(null)
                            .doOnSuccess(rows -> System.err.println("数据库连接成功 => " + rows.iterator().next().toJson()))
                            .subscribe()
                    ;
                })
                ;
    }


}
