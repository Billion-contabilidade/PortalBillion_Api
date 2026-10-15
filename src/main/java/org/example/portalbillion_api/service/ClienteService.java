package org.example.portalbillion_api.service;

import org.example.portalbillion_api.model.Cliente;
import org.example.portalbillion_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }
    public Optional<Cliente> buscarPorId(UUID id ){
        return clienteRepository.findById(id);
    }

    public Cliente cadastrarCliente(Cliente dados){
        Cliente cliente = new Cliente();

        cliente.setLegalName(dados.getLegalName());
        cliente.setCnpj(dados.getCnpj());
        cliente.setTaxRegime(dados.getTaxRegime());
        cliente.setStatus(dados.getStatus());

        return clienteRepository.save(cliente);
    }

}
