package org.example.portalbillion_api.controller;

import jakarta.validation.Valid;
import org.example.portalbillion_api.model.Cliente;
import org.example.portalbillion_api.repository.ClienteRepository;
import org.example.portalbillion_api.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
