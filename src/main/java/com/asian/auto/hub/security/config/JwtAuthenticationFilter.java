package com.asian.auto.hub.security.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.asian.auto.hub.repository.UserRepository;
import com.asian.auto.hub.serviceimpl.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserRepository userRepository;
	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		logger.info("Authorization header : {}", header);

		if (header != null && header.startsWith("Bearer ")) {

			// token extract and validate then authentication create and then security
			// set into context 

			String token = header.substring(7);
			// check for access token

			try {

				if (!jwtService.isAccessToken(token)) {
					// message pass 
					filterChain.doFilter(request, response);
					return;
				}

				Jws<Claims> parse = jwtService.parse(token);

				Claims payload = parse.getPayload();

				String userId = payload.getSubject();

				Long id = Long.valueOf(userId);

				userRepository.findById(id).ifPresent(user -> {

					// check for user enable or not

					if (!user.isDeleted()) {
						// user found in database
						List<GrantedAuthority> authorities = user.getRoles() == null ? List.of()
								: user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
										.collect(Collectors.toList());
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								user.getEmail(), null, authorities);
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						// final line : to set the authentication to security context
						if (SecurityContextHolder.getContext().getAuthentication() == null)
							SecurityContextHolder.getContext().setAuthentication(authentication);
					}

				});

			} catch (ExpiredJwtException e) {
				request.setAttribute("error", "Token Expired");

			} catch (Exception e) {
				request.setAttribute("error", "Invalid Token");

			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getRequestURI().startsWith("/api/v1/auth");
	}
}
