package TVC3.Almoxarifado.sistema;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Farmacia extends Setor {
    public Farmacia() {
        this.estoque = new Estoque();
        System.out.println("Carregando Base de dados");
        this.carregarDados(); // Carrega os produtos do arquivo CSV
        System.out.println("Base de dados carregada com sucesso!");        
    }

    Setor retornaSetor()
    {
        return this;
    }

    
    // Atributos
    public static final String PRODUTOS_CSV ="src/main/resources/data/farmacia/produtos.csv";

    private Estoque estoque;
    public void entradaProduto(Produto produto) 
    {
        this.estoque.adicionarProduto(produto);
        System.out.println("Produto adicionado ao estoque da farmacia.");

        salvarDados(); // Salva os produtos no arquivo CSV
        
    }

    @Override
    public void saidaProduto(Produto produto) {}

    public void saidaProduto(int id,int qtd, Setor setor){}

    @Override
    public void salvarDados() 
    {
        this.salvarProdutos();      
    }

    public void salvarProdutos()
    {
        try {
            List<String> linhas = new ArrayList<>();
            linhas.add("ID,Qtd,Nome,ID_fornecedor"); // Cabeçalho
            for (Produto produto : this.estoque.listarProdutos()) {
                String linha = produto.getId() + "," + produto.getQtd() + "," + produto.getNome() + ","
                        + produto.getFornecedor_id();
                linhas.add(linha);
            }
            Files.write(Paths.get(PRODUTOS_CSV), linhas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
        
    

    @Override
    public void carregarDados()
    {
        this.carregarProdutos();        
    }

    public void carregarProdutos()
    {
        System.out.println("Carregando produtos Farmacia...");
        try {
            List<String> linhas = Files.readAllLines(Paths.get(PRODUTOS_CSV));
            for (String linha : linhas.subList(1, linhas.size())) 
            { // Ignora o cabeçalho
                String[] partes = linha.split(",");
                Produto produto = new Produto(partes[2]);
                produto.setId(Integer.parseInt(partes[0]));
                produto.setQtd(Integer.parseInt(partes[1]));
                produto.setFornecedor_id(Integer.parseInt(partes[3]));
                estoque.adicionarProduto(produto);
            }
            System.out.println("Produtos carregados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    public void listarEstoque()
    {
        System.out.println("Estoque da Farmacia:");
        for (Produto produto : this.estoque.listarProdutos()) {
            System.out.println("ID: " + produto.getId() + ", Nome: " + produto.getNome() + ", Qtd: " + produto.getQtd());
        }
    }

    public Estoque getEstoque() {
        return this.estoque;
    }
    
}
