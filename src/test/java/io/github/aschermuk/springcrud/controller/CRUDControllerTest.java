package io.github.aschermuk.springcrud.controller;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import io.github.aschermuk.springcrud.service.CRUDService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

class ConcreteCRUDControllerTest {

    private final CRUDService<TestResponseDTO, ?, TestRequestDTO> crudService = mock(CRUDService.class);
    private final ConcreteCRUDController crudController = new ConcreteCRUDController(crudService);

    @Test
    void getAll() {
        // Arrange
        when(crudService.getAll(null)).thenReturn(Arrays.asList(new TestResponseDTO(1L), new TestResponseDTO(2L)));

        // Act
        ResponseEntity<List<TestResponseDTO>> responseEntity = crudController.getAll(null);

        // Assert
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(2, requireNonNull(responseEntity.getBody()).size());
    }

    @Test
    void get() {
        // Arrange
        Long id = 1L;
        when(crudService.get(id)).thenReturn(new TestResponseDTO(id));

        // Act
        ResponseEntity<TestResponseDTO> responseEntity = crudController.get(id);

        // Assert
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(id, requireNonNull(responseEntity.getBody()).id());
    }

    @Test
    void add() throws Exception {
        // Arrange
        TestRequestDTO requestDTO = new TestRequestDTO("Test");
        when(crudService.add(requestDTO)).thenReturn(1L);

        // Act
        ResponseEntity<Long> responseEntity = crudController.add(requestDTO);

        // Assert
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody());
    }

    @Test
    void addAll() throws Exception {
        // Arrange
        List<TestRequestDTO> requestDTOs = Arrays.asList(new TestRequestDTO("Test1"), new TestRequestDTO("Test2"));
        when(crudService.addAll(requestDTOs)).thenReturn(Arrays.asList(1L, 2L));

        // Act
        ResponseEntity<List<Long>> responseEntity = crudController.addAll(requestDTOs);

        // Assert
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(2, requireNonNull(responseEntity.getBody()).size());
    }

    @Test
    void update() {
        // Arrange
        Long id = 1L;
        TestRequestDTO requestDTO = new TestRequestDTO("UpdatedTest");
        when(crudService.update(id, requestDTO)).thenReturn(1L);

        // Act
        ResponseEntity<Long> responseEntity = crudController.update(id, requestDTO);

        // Assert
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody());
    }

    @Test
    void delete() {
        // Arrange
        Long id = 1L;
        when(crudService.delete(id)).thenReturn(1L);

        // Act
        ResponseEntity<Long> responseEntity = crudController.delete(id);

        // Assert
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody());
    }

    // Mock DTO classes for testing
        private record TestResponseDTO(Long id) {
    }

    private record TestRequestDTO(String name) {

    }

    // Concrete subclass for testing
    private static class ConcreteCRUDController extends CRUDController<TestResponseDTO, TestRequestDTO> {
        public ConcreteCRUDController(CRUDService<TestResponseDTO, ?, TestRequestDTO> crudService) {
            super(crudService);
        }
    }
}
