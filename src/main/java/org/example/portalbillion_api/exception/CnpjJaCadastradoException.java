package org.example.portalbillion_api.exception;

public class CnpjJaCadastradoException extends RuntimeException{

    public CnpjJaCadastradoException(){
        super("Já existe um cliente com esse cnpj");
    }

}
