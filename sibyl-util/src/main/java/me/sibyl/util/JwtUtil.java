package me.sibyl.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname JwtUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:14
 */

public class JwtUtil  {
    //签名私钥
    private static String key = "sibyl";
    //签名有效时间
    private static Long timeOut = 86400000L;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Long getTimeOut() {
        return timeOut;
    }
    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key","value");
        String dl3213 = createJwt("3213", "dl3213",map);
        System.err.println(dl3213);
        Claims claims = parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkbDMyMTMiLCJwYXNzd29yZCI6ImUxMGFkYzM5NDliYTU5YWJiZTU2ZTA1N2YyMGY4ODNlIiwiaXNEZWxldGVkIjowLCJjcmVhdGVUaW1lIjoxNjQ1NTM5NzQyMDAwLCJuYW1lIjoiZGwzMjEzIiwiaWQiOjMyMTMsImV4cCI6MTY0NTg1NjI0NiwiaWF0IjoxNjQ1ODUyNjQ2LCJhZ2UiOjI1LCJlbWFpbCI6InRlc3RAZW1haWwuY29tIiwianRpIjoiMzIxMyIsInVzZXJuYW1lIjoiZGwzMjEzIn0.bQYLTk54UqTD6FevWlmZSoL2dQGjmPaQ0TKQDdOb97k");
        System.err.println(claims.toString());
    }

    /**
     * 设置认证token
     *      id:用户id
     *      subject:用户名
     */
    public static String createJwt(String id, String name, Map<String,Object> map) {

        //1.设置失效时间
        long nowTime = System.currentTimeMillis();//当前毫秒
        long expirationTime = nowTime + timeOut;
        //2.创建jwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder();

        //3.根据map设置claims
        //当设置的是整个map时，就需放在最前面，下面的setId等参数才会有效，否则会把前面set的值置空
        jwtBuilder.setClaims(map);
        jwtBuilder.setId(id);
        jwtBuilder.setSubject(name);
        jwtBuilder.setIssuedAt(new Date());//设置当前时间
        jwtBuilder.signWith(SignatureAlgorithm.HS256, key);//设置加密方式
        jwtBuilder.setExpiration(new Date(expirationTime));//设置过期时间
        //4.创建token
        String token = jwtBuilder.compact();
        return token;
    }

    /**
     * 解析token字符串获取clamis
     */
    public static Claims parseJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }

}
