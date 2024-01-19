package io.github.aschermuk.springcrud.controller;

import static org.springframework.http.ResponseEntity.ok;

import io.github.aschermuk.springcrud.service.CRUDService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
public abstract class CRUDController<ResponseDTO, RequestDTO> {

    protected final CRUDService<ResponseDTO, ?, RequestDTO> crudService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ResponseDTO>> getAll(@RequestParam(required = false) String sort) {
        return ok(crudService.getAll(sort));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDTO> get(@NotNull @PathVariable Long id) {
        return ok(crudService.get(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Long> add(@NotNull @Valid @RequestBody RequestDTO requestDTO) throws Exception {
        return ok(crudService.add(requestDTO));
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<Long>> addAll(@RequestBody List<@NotNull @Valid RequestDTO> requestDTOs) throws Exception {
        return ok(crudService.addAll(requestDTOs));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> update(@NotNull @PathVariable Long id,
                                       @NotNull @Valid @RequestBody RequestDTO requestDTO) {
        return ok(crudService.update(id, requestDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ok(crudService.delete(id));
    }
}
