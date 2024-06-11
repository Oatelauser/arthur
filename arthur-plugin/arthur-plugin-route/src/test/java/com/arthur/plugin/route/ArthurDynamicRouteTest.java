package com.arthur.plugin.route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ArthurDynamicRouteTest {

    @Mock
    private RouteDefinitionWriter mockWriter;
    @Mock
    private RouteDefinitionLocator mockLocator;

    private ArthurDynamicRoute arthurDynamicRouteUnderTest;

    @BeforeEach
    void setUp() {
        openMocks(this);
        arthurDynamicRouteUnderTest = new ArthurDynamicRoute(mockWriter, mockLocator);
    }

    @Test
    void testGetAllRouteDefinition() {
        // Setup
        final List<RouteDefinition> expectedResult = List.of(new RouteDefinition("text"));
        when(mockLocator.getRouteDefinitions()).thenReturn(Flux.just(new RouteDefinition("text")));

        // Run the test
        final List<RouteDefinition> result = arthurDynamicRouteUnderTest.getAllRouteDefinition();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAllRouteDefinition_RouteDefinitionLocatorReturnsNoItem() {
        // Setup
        final List<RouteDefinition> expectedResult = List.of(new RouteDefinition("text"));
        when(mockLocator.getRouteDefinitions()).thenReturn(Flux.empty());

        // Run the test
        final List<RouteDefinition> result = arthurDynamicRouteUnderTest.getAllRouteDefinition();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAllRouteDefinition_RouteDefinitionLocatorReturnsError() {
        // Setup
        final List<RouteDefinition> expectedResult = List.of(new RouteDefinition("text"));
        when(mockLocator.getRouteDefinitions()).thenReturn(Flux.error(new Exception("message")));

        // Run the test
        final List<RouteDefinition> result = arthurDynamicRouteUnderTest.getAllRouteDefinition();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testAdd() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.just(null));

        // Run the test
        arthurDynamicRouteUnderTest.add(definition);

        // Verify the results
    }

    @Test
    void testAdd_RouteDefinitionWriterReturnsNoItem() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.empty());

        // Run the test
        arthurDynamicRouteUnderTest.add(definition);

        // Verify the results
    }

    @Test
    void testAdd_RouteDefinitionWriterReturnsError() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.error(new Exception("message")));

        // Run the test
        arthurDynamicRouteUnderTest.add(definition);

        // Verify the results
    }

    @Test
    void testUpdate() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.just(null));
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.just(null));

        // Run the test
        arthurDynamicRouteUnderTest.update(definition);

        // Verify the results
    }

    @Test
    void testUpdate_RouteDefinitionWriterDeleteReturnsNoItem() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.empty());
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.just(null));

        // Run the test
        arthurDynamicRouteUnderTest.update(definition);

        // Verify the results
    }

    @Test
    void testUpdate_RouteDefinitionWriterDeleteReturnsError() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.error(new Exception("message")));

        // Run the test
        arthurDynamicRouteUnderTest.update(definition);

        // Verify the results
    }

    @Test
    void testUpdate_RouteDefinitionWriterSaveReturnsNoItem() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.just(null));
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.empty());

        // Run the test
        arthurDynamicRouteUnderTest.update(definition);

        // Verify the results
    }

    @Test
    void testUpdate_RouteDefinitionWriterSaveReturnsError() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.just(null));
        when(mockWriter.save(any(Mono.class))).thenReturn(Mono.error(new Exception("message")));

        // Run the test
        arthurDynamicRouteUnderTest.update(definition);

        // Verify the results
    }

    @Test
    void testDelete1() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.just(null));

        // Run the test
        arthurDynamicRouteUnderTest.delete(definition);

        // Verify the results
    }

    @Test
    void testDelete1_RouteDefinitionWriterReturnsNoItem() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.empty());

        // Run the test
        arthurDynamicRouteUnderTest.delete(definition);

        // Verify the results
    }

    @Test
    void testDelete1_RouteDefinitionWriterReturnsError() {
        // Setup
        final RouteDefinition definition = new RouteDefinition("text");
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.error(new Exception("message")));

        // Run the test
        arthurDynamicRouteUnderTest.delete(definition);

        // Verify the results
    }

    @Test
    void testDelete2() {
        // Setup
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.just(null));

        // Run the test
        arthurDynamicRouteUnderTest.delete("routeId");

        // Verify the results
    }

    @Test
    void testDelete2_RouteDefinitionWriterReturnsNoItem() {
        // Setup
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.empty());

        // Run the test
        arthurDynamicRouteUnderTest.delete("routeId");

        // Verify the results
    }

    @Test
    void testDelete2_RouteDefinitionWriterReturnsError() {
        // Setup
        when(mockWriter.delete(any(Mono.class))).thenReturn(Mono.error(new Exception("message")));

        // Run the test
        arthurDynamicRouteUnderTest.delete("routeId");

        // Verify the results
    }
}
