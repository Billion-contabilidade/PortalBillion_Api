package org.example.portalbillion_api.controller;

import jakarta.validation.Valid;
import org.example.portalbillion_api.model.Cliente;
import org.example.portalbillion_api.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }
    @GetMapping
    public List<Cliente> listarClientes(){
        return clienteService.listarClientes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente cadastrarCliente(@Valid @RequestBody Cliente cliente){
            return clienteService.cadastrarCliente(cliente);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscaPorId(
            @PathVariable("id")UUID id){
        Optional<Cliente> cliente = clienteService.buscarPorId(id);

        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente.get());

    }
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @PathVariable("id") UUID id,
            @Valid
            @RequestBody
            Cliente dados ){
        Optional<Cliente> cliente =
                clienteService.atualizarCliente(id, dados);

        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(
            @PathVariable("id") UUID id){
        boolean excluido = clienteService.excluirCliente(id);

        if (!excluido){
            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.noContent().build();
    }

}
