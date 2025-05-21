package code.sibyl.service;

import code.sibyl.common.r;
import code.sibyl.config.CommonConfig;
import code.sibyl.domain.SteamFriend;
import code.sibyl.domain.sys.User;
import code.sibyl.service.sql.PostgresqlService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SteamService {

    private final WebClient webClient;

    public static SteamService getBean() {
        return r.getBean(SteamService.class);
    }


    //https://api.steamapis.com/steam/profile/76561199483616674?api_key=0E93DCE684003E07C471D6729E36E9F9
    public static void main(String[] args) {
        test();
    }

    public static void close(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    private static void test() {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {

            HttpGet get = new HttpGet("https://api.steampowered.com/<界面>/<方法>/v<版本>/");
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            String string = IOUtils.toString(content, Charset.forName("utf-8"));
            System.err.println("string => " + string);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mono<Long> friendList() {

        return PostgresqlService.getBean().template().selectOne(Query.query(Criteria.where("id").is(1L)), User.class)
                .flatMap(user -> {
                    log.info("[SteamFriend] user -> {}, {}", user.getSteamId(), user.getSteamWebApiKey());

//                    String GetFriendListUrl = "https://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key="+user.getSteamWebApiKey()+"&steamid="+user.getSteamId();
//                    try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
//                        HttpGet get_friend_list = new HttpGet(GetFriendListUrl);
//                        CloseableHttpResponse response = httpClient.execute(get_friend_list);
//                        InputStream content_get_friend_list = response.getEntity().getContent();
//                        String friend_list_str = IOUtils.toString(content_get_friend_list, Charset.forName("utf-8"));
//                        r.close(content_get_friend_list);
//                        JSONObject friend_list_json = JSONObject.parseObject(friend_list_str);
//                        if(response.getCode() != 200){
//                            return Mono.error(new RuntimeException(friend_list_str));
//                        }
//                        List<JSONObject> friendslist = friend_list_json.getJSONObject("friendslist").getList("friends", JSONObject.class);
//                        String steamids = friendslist.stream().map(e -> e.getString("steamid")).collect(Collectors.joining(","));
//                        String friend_summaries_url = STR."https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=\{user.getSteamWebApiKey()}&steamids=\{steamids}";
//                        HttpGet get_friend = new HttpGet(friend_summaries_url);
//                        InputStream content_get_friend = httpClient.execute(get_friend).getEntity().getContent();
//                        JSONObject data = JSONObject.parseObject(IOUtils.toString(content_get_friend, Charset.forName("utf-8")));
//                        r.close(content_get_friend);
//                        System.err.println(data);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    return SteamService.getBean().GetFriendList(user.getSteamWebApiKey(), user.getSteamId())
                            .flatMap(e -> Mono.zip(Mono.just(user), Mono.just(e.getJSONObject("friendslist").getList("friends", JSONObject.class))))
                            .onErrorResume(throwable -> {
                                System.err.println("error in GetFriendList...");
                                throwable.printStackTrace();
                                return Mono.empty();
                            })
                            ;

                })
                .flatMap(tuple -> {
                    System.err.println(tuple);
                    User currentUser = tuple.getT1();
                    List<JSONObject> friendList = tuple.getT2();
                    String steamFriendListIdStr = friendList.stream().map(e -> e.getString("steamid")).collect(Collectors.joining(","));

                    return SteamService.getBean().GetPlayerSummaries(currentUser.getSteamWebApiKey(), steamFriendListIdStr)
                            .flatMap(e -> Mono.zip(
                                    Mono.just(currentUser),
                                    Mono.just(friendList),
                                    Mono.just(e),
                                    PostgresqlService.getBean().template().getDatabaseClient()
                                            .sql("update t_biz_steam_friend set relation_ship = null where steam_id =:steamId")
                                            .bind("steamId", tuple.getT1().getSteamId()).fetch().rowsUpdated()
                            ))
                            .onErrorResume(throwable -> {
                                System.err.println("error in GetPlayerSummaries...");
                                throwable.printStackTrace();
                                return Mono.empty();
                            });
                })
                .flatMapMany(tuple -> {
                    User currentUser = tuple.getT1();
                    List<JSONObject> friendList = tuple.getT2();
                    JSONObject playerJson = tuple.getT3();

                    return Flux.fromIterable(friendList)
                            .flatMap(json -> {
                                SteamFriend friend = new SteamFriend();
                                friend.setId(null);
                                friend.setSteamId(currentUser.getSteamId());
                                friend.setStreamFriendId(json.getString("steamid"));
                                friend.setFriendSince(r.long2localDateTime(json.getLong("friend_since")));
                                friend.setRelationship(json.getString("relationship"));
                                friend.setDeleted("0");
                                return Mono.zip(Mono.just(currentUser), Mono.just(friend));
                            });
                })
                .flatMap(tuple -> Mono.zip(
                        Mono.just(tuple.getT1()),
                        Mono.just(tuple.getT2()),
                        PostgresqlService.getBean().template().selectOne(
                                        Query.query(
                                                Criteria.where("is_deleted").is("0")
                                                        .and("steam_id").is(tuple.getT2().getSteamId())
                                                        .and("stream_friend_id").is(tuple.getT2().getStreamFriendId())
                                        ), SteamFriend.class)
                                .switchIfEmpty(Mono.just(new SteamFriend())))
                )
                .flatMap(tuple -> {
                    SteamFriend entity = tuple.getT2();
                    System.err.println(entity);
                    SteamFriend ifExists = tuple.getT3();
                    if (Objects.isNull(ifExists.getId())) {
                        entity.setId(r.id());
                        return PostgresqlService.getBean().template().insert(entity);
                    } else {
                        return PostgresqlService.getBean().template().update(entity);
                    }
                })
                .count()
                .map(count -> {
                    log.info("[SteamFriend] count = {}", count);
                    return count;
                })
                .thenReturn(1L)
                ;
    }

//    @Cacheable(cacheNames = CommonConfig.cacheName_30MINUTES, key = "#root.targetClass+'-'+#root.methodName+'-'+#p0+''+#p1")
    public Mono<JSONObject> GetFriendList(String key, String steamid) {
        System.err.println("code.sibyl.service.SteamService.GetFriendList --> ");
        return webClient.get()
                .uri("https://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" + key + "&steamid=" + steamid)
                .retrieve()
                .toEntity(JSONObject.class)
                .map(response -> {
                    System.err.println("GetFriendList --> ");
                    System.err.println(response);
                    return response.getBody();
                });
    }

//    @Cacheable(cacheNames = CommonConfig.cacheName_30MINUTES, key = "#root.targetClass+'-'+#root.methodName+'-'+#p0+''+#p1")
    public Mono<JSONObject> GetPlayerSummaries(String key, String steamids) {
        System.err.println("code.sibyl.service.SteamService.GetPlayerSummaries --> ");
        return webClient.get()
                .uri("https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + key + "&steamids=" + steamids)
                .retrieve()
                .toEntity(JSONObject.class)
                .map(response -> {
                    System.err.println("GetPlayerSummaries --> ");
                    System.err.println(response);
                    return response.getBody();
                });
    }
}
