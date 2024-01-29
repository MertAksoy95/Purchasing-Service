package com.emlakjet.purchasing.filter;

import com.emlakjet.purchasing.entity.User;
import com.emlakjet.purchasing.repository.UserRepository;
import com.emlakjet.purchasing.util.JwtUtil;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This is our class where authentication is carried out for each request.
 * JWT based security steps are implemented.
 * An in-memory structure is used for performance.
 */
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${hazelcast.map.time-to-live-minutes}")
    private long hazelcastTimeToLive;
    private final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
    private final IMap<String, User> sessionMap = instance.getMap("SessionIdMappingInstance");

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public JwtTokenFilter(JwtUtil jwtUtil, UserRepository userRepo) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    /**
     * This method first extracts the jwt token from the header and then performs the validation process.
     * After these operations are completed successfully, it identifies the user on the spring side by taking the user details.
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        String username = jwtUtil.extractUsername(token);

        if (!jwtUtil.validateToken(token, username)) {
            chain.doFilter(request, response);
            return;
        }

        log.info("::: API req from username: {}", username);

        User user = getUserDetails(username);

        ArrayList<GrantedAuthority> authorityList = new ArrayList<>();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, authorityList);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(request, response);
    }


    /**
     * This method takes user information and returns it as response.
     * User information is also kept in-memory for performance purposes.
     * If the details of the desired user are included in the in-memory map, they are returned directly from there.
     * If the details of the desired user are not included in the in-memory map, the information is retrieved from the database and returned.
     */
    public User getUserDetails(String username) {
        User user = sessionMap.get(username);

        if (user == null) {
            user = userRepo.findByEmail(username);
            sessionMap.set(username, user, hazelcastTimeToLive, TimeUnit.MINUTES);
        }

        return user;
    }

}

