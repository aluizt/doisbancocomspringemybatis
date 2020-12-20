package br.com.doisbanco.doisbancodedados.repository;

import br.com.doisbanco.doisbancodedados.model.ProdutoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProdutoRepository {

    @Select("SELECT * FROM PRODUTO WHERE CODIGO_PRODUTO = #{codigoProduto}")
    ProdutoModel buscarProduto(@Param("codigoProduto") Long codigoProduto);
}
