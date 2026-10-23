package org.example.portalbillion_api.service;

import org.example.portalbillion_api.exception.CnpjJaCadastradoException;
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

        if (clienteRepository.existsByCnpj(dados.getCnpj())){
            throw new CnpjJaCadastradoException();
        }
        Cliente cliente = new Cliente();

        cliente.setLegalName(dados.getLegalName());
        cliente.setCnpj(dados.getCnpj());
        cliente.setTaxRegime(dados.getTaxRegime());
        cliente.setStatus(dados.getStatus());

        return clienteRepository.save(cliente);
    }
    public Optional<Cliente> atualizarCliente(UUID id, Cliente dados){
        Optional<Cliente> resultado = clienteRepository.findById(id);

        if (resultado.isEmpty()){
            return Optional.empty();
        }
        if (clienteRepository.existsByCnpjAndIdNot(dados.getCnpj(), id)){
            throw new CnpjJaCadastradoException();
        }

        Cliente cliente = resultado.get();

        cliente.setLegalName(dados.getLegalName());
        cliente.setCnpj(dados.getCnpj());
        cliente.setTaxRegime(dados.getTaxRegime());
        cliente.setStatus(dados.getStatus());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return Optional.of(clienteSalvo);
    }

    public boolean excluirCliente(UUID id){
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isEmpty()){
            return false;
        }

        clienteRepository.delete(cliente.get());
        return true;
    }




}
