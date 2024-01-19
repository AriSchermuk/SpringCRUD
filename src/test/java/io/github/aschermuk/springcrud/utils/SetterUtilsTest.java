package io.github.aschermuk.springcrud.utils;

import static io.github.aschermuk.springcrud.utils.SetterUtils.setEntityIfNotNull;
import static io.github.aschermuk.springcrud.utils.SetterUtils.setIfNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class SetterUtilsTest {

    @Test
    public void testSetIfNotNull() {
        // Arrange
        String value = "test";
        Consumer<String> setter = mock(Consumer.class);

        // Act
        setIfNotNull(value, setter);

        // Assert
        verify(setter).accept(value);
    }

    @Test
    public void testSetIfNotNullWithNullValue() {
        // Arrange
        Consumer<String> setter = mock(Consumer.class);

        // Act
        setIfNotNull(null, setter);

        // Assert
        verify(setter, never()).accept(any());
    }

    @Test
    public void testSetEntityIfNotNull() {
        // Arrange
        Long entityId = 1L;
        Consumer<Entity> setter = mock(Consumer.class);
        EntityManager entityManager = mock(EntityManager.class);
        Entity entity = new Entity();

        // Mocking EntityManager
        when(entityManager.getReference(Entity.class, entityId)).thenReturn(entity);

        // Act
        setEntityIfNotNull(entityId, setter, Entity.class, entityManager);

        // Assert
        verify(setter).accept(entity);
    }

    @Test
    public void testSetEntityIfNotNullWithNullId() {
        // Arrange
        Consumer<Entity> setter = mock(Consumer.class);
        EntityManager entityManager = mock(EntityManager.class);

        // Act
        setEntityIfNotNull(null, setter, Entity.class, entityManager);

        // Assert
        verify(setter, never()).accept(any());
    }

    @Test
    public void testSetEntityIfNotNullEntityNotFound() {
        // Arrange
        Long entityId = 1L;
        Consumer<Entity> setter = mock(Consumer.class);
        EntityManager entityManager = mock(EntityManager.class);

        // Mocking EntityManager to throw EntityNotFoundException
        when(entityManager.getReference(Entity.class, entityId)).thenThrow(EntityNotFoundException.class);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
            () -> setEntityIfNotNull(entityId, setter, Entity.class, entityManager));
        verify(setter, never()).accept(any());
    }

    // Sample entity class for testing
    private static class Entity {
    }
}
