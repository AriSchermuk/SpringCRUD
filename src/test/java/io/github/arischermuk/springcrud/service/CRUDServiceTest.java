package io.github.arischermuk.springcrud.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.arischermuk.springcrud.entities.AppEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

class ConcreteCRUDServiceTest {

    private final JpaRepository<TestEntity, Long> repository = mock(JpaRepository.class);
    private final EntityManager entityManager = mock(EntityManager.class);
    private final ModelMapper modelMapper = mock(ModelMapper.class);

    private final ConcreteCRUDService crudService = new ConcreteCRUDService(
        repository, entityManager, modelMapper, TestEntity.class, TestDTO.class
    );

    @Test
    void getAll() {
        // Arrange
        when(repository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(new TestEntity()));
        when(modelMapper.map(any(), eq(TestDTO.class))).thenReturn(new TestDTO());

        // Act
        List<TestDTO> result = crudService.getAll("name,asc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void get() {
        // Arrange
        Long entityId = 1L;
        when(repository.findById(entityId)).thenReturn(java.util.Optional.of(new TestEntity(entityId, "TestName")));
        when(modelMapper.map(any(), eq(TestDTO.class))).thenReturn(new TestDTO(entityId, "TestName"));

        // Act
        TestDTO result = crudService.get(entityId);

        // Assert
        assertNotNull(result);
        assertEquals(entityId, result.getId());
        assertEquals("TestName", result.getName());
    }

    @Test
    void getNotFound() {
        // Arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> crudService.get(1L));
    }

    @Test
    void add() throws Exception {
        // Arrange
        TestRequestDTO requestDTO = new TestRequestDTO("NewTestName");
        when(modelMapper.map(any(), eq(TestDTO.class))).thenReturn(new TestDTO(1L, "NewTestName"));
        when(entityManager.getReference(eq(TestEntity.class), anyLong())).thenReturn(new TestEntity(1L, "NewTestName"));
        when(repository.save(any())).thenReturn(new TestEntity(1L, "NewTestName"));

        // Act
        Long result = crudService.add(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result);
    }

    @Test
    void addAll() throws Exception {
        // Arrange
        List<TestRequestDTO> requestDTOs = Collections.singletonList(new TestRequestDTO("Test1"));
        when(modelMapper.map(any(), eq(TestDTO.class))).thenReturn(new TestDTO(1L, "Test1"));
        when(entityManager.getReference(eq(TestEntity.class), anyLong())).thenReturn(new TestEntity(1L, "Test1"));
        when(repository.save(any())).thenReturn(new TestEntity(1L, "Test1"));

        // Act
        List<Long> result = crudService.addAll(requestDTOs);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0));
    }

    @Test
    void update() {
        // Arrange
        Long entityId = 1L;
        TestRequestDTO requestDTO = new TestRequestDTO("UpdatedTestName");
        when(repository.findById(entityId)).thenReturn(java.util.Optional.of(new TestEntity(entityId, "OldTestName")));
        when(modelMapper.map(any(), eq(TestDTO.class))).thenReturn(new TestDTO(entityId, "UpdatedTestName"));
        when(repository.save(any())).thenReturn(new TestEntity(entityId, "UpdatedTestName"));

        // Act
        Long result = crudService.update(entityId, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(entityId, result);
    }

    @Test
    void updateNotFound() {
        // Arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> crudService.update(1L, new TestRequestDTO()));
    }

    @Test
    void delete() {
        // Arrange
        Long entityId = 1L;
        when(repository.findById(entityId)).thenReturn(java.util.Optional.of(new TestEntity(1L, "TestName")));

        // Act
        Long result = crudService.delete(entityId);

        // Assert
        assertNotNull(result);
        assertEquals(entityId, result);
    }

    // Your entity class for testing
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    private static class TestEntity extends AppEntity {
        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        private String name;

        // Constructors, getters, and setters as needed

        // Additional methods if required
    }


    // Your DTO class for testing
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestDTO {
        private Long id;
        private String name;

        // Constructors, getters, and setters as needed

        // Additional methods if required
    }

    // Your request DTO class for testing
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestRequestDTO {
        private String name;

        // Constructors, getters, and setters as needed

        // Additional methods if required
    }


    // Concrete subclass for testing
    private static class ConcreteCRUDService extends CRUDService<TestDTO, TestEntity, TestRequestDTO> {
        public ConcreteCRUDService(
            JpaRepository<TestEntity, Long> repository,
            EntityManager entityManager,
            ModelMapper modelMapper,
            Class<TestEntity> entityClass,
            Class<TestDTO> dtoClass
        ) {
            super(repository, entityManager, modelMapper, entityClass, dtoClass);
        }

        @Override
        protected void setEntityFieldsFromDTO(TestRequestDTO requestDTO, TestEntity entity) {
            // Replace the following with your actual mapping logic
            entity.setName(requestDTO.getName());
            // Add more mappings as needed
        }

    }
}
