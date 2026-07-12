package de.notev2.notev2.config;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private CustomUserDetailsService userDetailsService;
    @InjectMocks private JwtFilter jwtFilter;

    @Test
    void expiredToken_returns401WithExpiredMessage() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer sometoken");
        request.setServletPath("/notes");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.extractUsername("sometoken"))
                .thenThrow(new ExpiredJwtException(null, null, "expired"));

        jwtFilter.doFilterInternal(request, response, chain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Token expired"));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void malformedToken_returns401WithInvalidMessage() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer brokentoken");
        request.setServletPath("/notes");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.extractUsername("brokentoken"))
                .thenThrow(new MalformedJwtException("malformed"));

        jwtFilter.doFilterInternal(request, response, chain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid token"));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void validToken_setsAuthenticationInSecurityContext() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validtoken");
        request.setServletPath("/notes");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        UserDetails userDetails = new User(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(jwtUtil.extractUsername("validtoken")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testuser", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain, times(1)).doFilter(request, response);

        // Wichtig: SecurityContext nach dem Test zurücksetzen, sonst leaked er in andere Tests
        SecurityContextHolder.clearContext();
    }
}