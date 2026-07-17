package de.notev2.notev2.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    @Test
    void allowsRequestsWithinLimit() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        FilterChain chain = mock(FilterChain.class);

        for (int i = 0; i < 5; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setServletPath("/auth/login");
            request.setRemoteAddr("127.0.0.1");
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, chain);

            assertNotEquals(429, response.getStatus());
        }
        verify(chain, times(5)).doFilter(any(), any());
    }

    @Test
    void blocksRequestsOverLimit() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        FilterChain chain = mock(FilterChain.class);

        // Bucket leerfahren (5 Tokens)
        for (int i = 0; i < 5; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setServletPath("/auth/login");
            request.setRemoteAddr("127.0.0.1");
            filter.doFilterInternal(request, new MockHttpServletResponse(), chain);
        }

        // 6. Request sollte geblockt werden
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/auth/login");
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, chain);

        assertEquals(429, response.getStatus());
        assertTrue(response.getContentAsString().contains("Too Many Requests"));
    }

    @Test
    void differentIpsGetSeparateBuckets() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        FilterChain chain = mock(FilterChain.class);

        for (int i = 0; i < 5; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setServletPath("/auth/login");
            request.setRemoteAddr("1.1.1.1");
            filter.doFilterInternal(request, new MockHttpServletResponse(), chain);
        }

        // Andere IP sollte ihren eigenen Bucket haben, nicht geblockt sein
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/auth/login");
        request.setRemoteAddr("2.2.2.2");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, chain);

        assertNotEquals(429, response.getStatus());
    }

    @Test
    void shouldNotFilter_skipsUnrelatedPaths() {
        RateLimitFilter filter = new RateLimitFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/notes");

        assertTrue(filter.shouldNotFilter(request));
    }
}