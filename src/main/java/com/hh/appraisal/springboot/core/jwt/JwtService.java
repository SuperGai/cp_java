package com.hh.appraisal.springboot.core.jwt;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.appraisal.springboot.core.baen.JwtTokenBean;
import com.hh.appraisal.springboot.core.baen.RestBean;
import com.hh.appraisal.springboot.core.constant.RestCode;
import com.hh.appraisal.springboot.core.exception.custom.RoleException;
import com.hh.appraisal.springboot.core.utils.StrUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JWT Token 服务
 * @author gaigai
 * @date 2019/05/15
 */
@Slf4j
@Component
public class JwtService {

    /**
     * app key，用于加密
     */
    @Value("${jwt.appKey}")
    private String appKey;

    /**
     * token有效时间(ms)
     *   1. 在 当前时间-签发时间>有效时间 时携带token访问接口，会重新刷新token
     *      在 当前时间-签发时间>有效时间*2 时，则需要重新登录。
     *   2. 这样可以分离长时间不活跃的用户和活跃用户
     *      活跃用户感受不到token的刷新
     *      不活跃用户需要登录才可以重新获取token
     */
    @Value("${jwt.period}")
    private Long period;

    /**
     * jwt token 签发人
     */
    @Value(("${jwt.issuer}"))
    private String issuer;

    /**
     * 默认token有效时间，1小时
     */
    public static final long DEFAULT_PERIOD = 60*60*1000;

    /**
     * 默认appkey
     */
    public static final String DEFAULT_APPKEY = "defaultAppKey7+)2%3j28%Odp4";

    /**
     * 默认签发人
     */
    public static final String DEFAULT_ISSUER = "SingleBootSystemIssuer";

    /**
     * 默认签名方式
     */
    private static final SignatureAlgorithm DEFAULT_SIGN_TYPE = SignatureAlgorithm.HS256;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final CompressionCodecResolver CODEC_RESOLVER = new DefaultCompressionCodecResolver();

    /**
     * 签发 JWT Token Token
     * @param id 令牌ID
     * @param subject subject 用户标识
     * @param roles 角色
     * @param permissions 权限集合，建议传入权限集合的json字符串
     * @return 生成的 JWT Token
     */
    public String issueJwt(String id, String subject, String roles, String permissions) {

        if(ObjectUtils.isEmpty(id) || ObjectUtils.isEmpty(subject)){
            log.error("id和subject不能为空");
            return null;
        }

        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(
                ObjectUtils.isEmpty(this.appKey) ? DEFAULT_APPKEY : this.appKey
        );

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setId(id);
        jwtBuilder.setSubject(subject);
        jwtBuilder.setIssuer(ObjectUtils.isEmpty(this.issuer) ? DEFAULT_ISSUER : this.issuer);
        if (!StringUtils.isEmpty(roles)) {
            jwtBuilder.claim("roles",roles);
        }
        if (!StringUtils.isEmpty(permissions)) {
            jwtBuilder.claim("perms",permissions);
        }

        // 设置签发时间
        Date nowTime = new Date();
        jwtBuilder.setIssuedAt(nowTime);

        // 设置到期时间
        this.period = this.period == null ? DEFAULT_PERIOD : this.period;
        jwtBuilder.setExpiration(
                //签发时间+有效期*2
                new Date(nowTime.getTime() + this.period + this.period)
        );

        // 压缩，可选GZIP
        jwtBuilder.compressWith(CompressionCodecs.DEFLATE);

        // 加密设置
        jwtBuilder.signWith(DEFAULT_SIGN_TYPE, secreKeyBytes);

        return jwtBuilder.compact();
    }

    /**
     * 验签JWT
     *
     * @param jwt
     * @return 如果验证通过，且刷新了token，则设置 JwtToken.isFlushed 为true
     */
    public RestBean parseJwt(String jwt) throws RoleException {
        if(ObjectUtils.isEmpty(appKey)){
            log.error("appKey无法读取：" + appKey);
            appKey = DEFAULT_APPKEY;
        }

        if(StringUtils.isEmpty(jwt)) {
            throw new RoleException("Token为空");
        }

        // 去除前缀
        if(jwt.indexOf(JwtTokenBean.JWT_TOKEN_PREFIX) >= 0){
            jwt = jwt.split(" ")[1];
        }

        // 检查 jwt token 合法性
        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(appKey))
                    .parseClaimsJws(jwt)
                    .getBody();

            if(claims == null ||
                    StringUtils.isEmpty(claims.getIssuer()) ||
                    !issuer.equals(claims.getIssuer())){
                return RestBean.error(RestCode.TOKEN_ERROR);
            }

        }catch (ExpiredJwtException ex){//token过期异常 token已经失效需要重新登录
            return RestBean.error(RestCode.EXPIRE_TOKEN);
        }catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){//不支持的token
            return RestBean.error(RestCode.TOKEN_ERROR);
        }catch (Exception e){
            log.error("验证token时出现未知错误: " + e);
            return RestBean.error(RestCode.TOKEN_ERROR);
        }

        JwtTokenBean jwtToken = new JwtTokenBean();

        // 检查是否需要刷新 jwt token
        // token签发时间
        long time = claims.getIssuedAt().getTime();
        // 当前时间
        long now = System.currentTimeMillis();
        period = (period == null ? JwtService.DEFAULT_PERIOD : period);
        if(time + period >= now){
            //还在有效期内，不需要刷新token
            jwtToken.setToken(jwt);
            jwtToken.setIsFlushed(false);
        }else if(time + period < now &&
                time + period + period >= now){
            //超过有效期，但未超过2倍有效期，此时应该刷新token
            log.debug("原token签发时间: " + time + ", 当前时间: " + now + ", 需要刷新token");
            jwtToken.setToken(
                    issueJwt(
                        //令牌id
                        StrUtils.getRandomString(20),
                        claims.getSubject(),
                        //访问角色
                        claims.get("roles", String.class),
                        //权限集合字符串，json
                        claims.get("perms", String.class)
                    )
            );
            jwtToken.setIsFlushed(true);
        }else{
            log.error("未知错误 - Jwts.parser() 方法未对过期token抛出异常");
        }

        // 设置其他字段
        jwtToken.setId(claims.getSubject());//用户id
        jwtToken.setPermissions(
                JSONObject.parseObject(
                        claims.get("perms", String.class),
                        List.class
                )
        );//用户权限集合,json转为list集合

        return RestBean.ok(jwtToken);
    }

    /**
     * 从json数据中读取格式化map
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }
}
