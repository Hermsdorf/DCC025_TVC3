package TVC3.Almoxarifado.sistema;

import java.util.ArrayList;
import java.util.List;

public class Estoque {
    Estoque() {
    }

    // Atributos

    List<Produto> produtos = new ArrayList<Produto>();

    // Métodos
    public void adicionarProduto(Produto produto_entrada) {

        for (Produto produto : produtos) 
        {
            if (produto.getId() == produto_entrada.getId()) {
                produto.setQtd(produto.getQtd() + produto_entrada.getQtd());
                return;
            }
        }

        produtos.add(produto_entrada);

    }

    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    public boolean verificaProduto(int id_produto) {
        for (Produto produto : produtos) {
            if (produto.getId() == id_produto) {
                return true;
            }
        }
        return false;
    }

    public void retiradaProduto(int produto_id, int quantidade) {
        if (quantidade <= 0 || quantidade > produtos.get(produto_id - 1).getQtd()) {
            System.out.println("Quantidade inválida para retirada.");
        } else {
            int quantidadeAtual = produtos.get(produto_id - 1).getQtd();
            produtos.get(produto_id - 1).setQtd(quantidadeAtual - quantidade);

        }
    }

    public List<Produto> listarProdutos() {
        return produtos;
    }

}
