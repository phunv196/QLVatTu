package com.app.util;

import com.app.model.user.UserModel;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenUtil {

    private static Logger log = LoggerFactory.getLogger(TokenUtil.class);

    private static final long VALIDITY_TIME_MS =  2 * 60 * 60 * 1000; // 2 hours  validity
    private static String secret="phunv";

    public static String createTokenForUser(UserModel user)  {
        if (user == null) {
            throw new NullPointerException("username or roles is illegal");
        }

        return Jwts.builder()
            .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
            .claim("userId"     , user.getUserId())
            .claim("loginName"     , user.getLoginName())
            .claim("role"   , user.getRole())
            .claim("empId"  , user.getEmployeeId())
            .claim("name"   , user.getFullName())
            .claim("email"  , user.getEmail())
            .claim("phone" , user.getPhone())
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public static UserModel getUserFromToken(String strToken) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(strToken).getBody();
            return new UserModel(
                (Integer)claims.get("userId"),
                (String)claims.get("loginName"),
                (String)claims.get("role"),
                (String)claims.get("name"),
                (String)claims.get("email"),
                (Integer)claims.get("empId"),
                (String)claims.get("phone")
            ) ;
        } catch (ExpiredJwtException e){
            log.error("Token Expired");
            return null;
        } catch (ClaimJwtException e){
            log.error("Invalid Token");
            return null;
        }
    }

    public static boolean isExpiringIn30Minutes(String strToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(strToken).getBody();
        long remainingMinutes =ChronoUnit.MINUTES.between( Instant.now() , claims.getExpiration().toInstant() );
        return (remainingMinutes < 30)?true:false;
    }



}
