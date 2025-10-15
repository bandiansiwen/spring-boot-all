package com.imp.all.framework.security.core.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.imp.all.framework.security.core.properties.ImpJwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Longlin
 * @date 2021/3/30 13:44
 * @description
 */
public class ImpJwtTokenUtil implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImpJwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";     //该JWT所面向的用户
    private static final String CLAIM_KEY_CREATED = "iat";      //在什么时候签发的(UNIX时间)

    private SecretKey SECRET_KEY;

    @Resource
    ImpJwtProperties impJwtProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] API_KEY_SECRET_BYTES = DatatypeConverter.parseBase64Binary(impJwtProperties.getSecret());
        this.SECRET_KEY = Keys.hmacShaKeyFor(API_KEY_SECRET_BYTES);
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 当原来的token没过期时是可以刷新的
     */
    public String refreshToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        String token = oldToken.substring(impJwtProperties.getTokenPrefix().length());
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        // token校验
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        // token过期不支持刷新
        if (isTokenExpired(token)) {
            return null;
        }
        // 如果token在30分钟内刷新过，返回原token
        if (tokenRefreshJustBefore(token, 30*60)) {
            return token;
        }
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())    //过期时间
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取JWT中的信息
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            claims = claimsJws.getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + impJwtProperties.getExpiration() * 1000);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        if(refreshDate.after(created)&&refreshDate.before(DateUtil.offsetSecond(created,time))){
            return true;
        }
        return false;
    }
}
