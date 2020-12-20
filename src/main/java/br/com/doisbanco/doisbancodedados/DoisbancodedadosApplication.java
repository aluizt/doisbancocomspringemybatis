package br.com.doisbanco.doisbancodedados;

import br.com.doisbanco.doisbancodedados.repository.banco1.Banco1ProdutoRepository;
import br.com.doisbanco.doisbancodedados.repository.banco2.Banco2ProdutoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Log4j2
public class DoisbancodedadosApplication {

    Banco1ProdutoRepository banco1ProdutoRepository;
    Banco2ProdutoRepository banco2ProdutoRepository;

    public static void main(String[] args) {
        SpringApplication.run(DoisbancodedadosApplication.class, args);
    }

    @PostConstruct
    public void teste() {
        log.info("Produto do banco 1 : " + banco1ProdutoRepository.buscarProduto(101L));
        log.info("Produto do banco 2 : " + banco2ProdutoRepository.buscarProduto(101L));
    }
}
