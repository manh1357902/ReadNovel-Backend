package com.example.readnovel.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.access-token-expiration}")
    private String accessExpiration;

    @Value("${jwt.access-token-secret-key}")
    private String accessSecretKey;

    @Value("${jwt.refresh-token-expiration}")
    private String refreshExpiration;

    @Value("${jwt.refresh-token-secret-key}")
    private String refreshSecretKey;
    //tạo access token
    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(accessExpiration)))
                .signWith(getSignKey(accessSecretKey))
                .compact();
    }
    //tạo refresh token
    public String generateRefreshToken(String email, Date expiration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiration!=null? expiration: new Date(System.currentTimeMillis()+Long.parseLong(refreshExpiration)))
                .signWith(getSignKey(refreshSecretKey))
                .compact();
    }
    // lấy chữ ký số
    private Key getSignKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // lấy email từ token
    public String getEmailFromToken(String token, boolean isRefresh) {
        return Jwts.parserBuilder().setSigningKey(isRefresh ? getSignKey(refreshSecretKey) : getSignKey(accessSecretKey)).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token, boolean isRefresh) {
        return Jwts.parserBuilder().setSigningKey(isRefresh? getSignKey(refreshSecretKey): getSignKey(accessSecretKey)).build().parseClaimsJws(token).getBody().getExpiration();
    }
    //kiểm tra token hợp lệ
    public boolean validateJwtToken(String token, boolean isRefresh) {
        try{
            Jwts.parserBuilder().setSigningKey(isRefresh ? getSignKey(refreshSecretKey) : getSignKey(accessSecretKey)).build().parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e) {
            System.out.println("Token hết hạn!");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token không được hỗ trợ!");
        } catch (MalformedJwtException e) {
            System.out.println("Token không đúng định dạng!");
        } catch (SignatureException e) {
            System.out.println("Chữ ký token không hợp lệ!");
        } catch (IllegalArgumentException e) {
            System.out.println("Token rỗng!");
        }
        return false;
    }
}
