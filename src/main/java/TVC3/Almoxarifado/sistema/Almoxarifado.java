package TVC3.Almoxarifado.sistema;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Almoxarifado extends Setor {
    public Almoxarifado() {

        this.estoque = new Estoque();
        this.fornecedores = new ArrayList<>();
        System.out.println("Carregando Base de dados...");
        this.carregarDados(); // Carrega os dados do arquivo CSV ao iniciar o sistema
        System.out.println("Base de dados carregada com sucesso!");
        //listarEstoque();
    }

 

    // Atributos
    private Estoque estoque;
    private List<Fornecedor> fornecedores;
    
    public static final int FARMACIA = 1;  
    public static final int CENTRO_CIRURGICO = 2;
    public static final int NUTRICAO = 3;

    public static final String PRODUTOS_CSV ="src/main/resources/data/almoxarifado/produtos.csv"; 
    public static final String FORNECEDORES_CSV ="src/main/resources/data/almoxarifado/fornecedores.csv";


    private static int ID = 0;

    public void entradaProduto(Produto produto, Fornecedor fornecedor) {

        // Verifica dse o fornecedor do produto é o usual
        if (fornecedor == null) 
        {
            if (produto.getFornecedor_id() < 0 || produto.getFornecedor_id() > fornecedores.size()) 
            {
                produto.setFornecedor_id(0); // Fornecedor não informado                
            } 
            else 
            {
                fornecedor = fornecedores.get(produto.getFornecedor_id() - 1); // Fornecedor informado
            }
        }
        else 
        {
            produto.setFornecedor(fornecedor);
        }

        if(produto.getId() > ID)
        {
            ID = produto.getId();
        }
        if(produto.getId() == 0)
        {
            produto.setId(++ID);
        }

        
        this.estoque.adicionarProduto(produto);
        System.out.println("Produto adicionado ao estoque do Almoxarifado.");
        salvarDados(); // Salva os produtos no arquivo CSV

    }

    @Override
    public void entradaProduto(Produto produto) {
        entradaProduto(produto, null);
    }

    @Override
    public void saidaProduto(Produto produto) 
    {


    }

    public void saidaProduto(Produto produto, int qtd, Setor setor)
    {
        if(estoque.verificaProduto(produto.getId()))
        {
            estoque.retiradaProduto(produto.getId(), qtd);
            Produto produtoRetirado = new Produto(produto.getNome());
            produtoRetirado.setId(produto.getId());
            produtoRetirado.setQtd(qtd);
            produtoRetirado.setFornecedor_id(produto.getFornecedor_id());
            System.out.println("DEBUG");
            setor.entradaProduto(produtoRetirado);
            System.out.println("Produto retirado do estoque.");
        }
        else
        {
            System.out.println("Produto não encontrado no estoque.");
        }
        salvarDados(); // Salva os produtos no arquivo CSV

    }

    void listarEstoque() {
        List<Produto> produtos = estoque.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Estoque vazio!");
        } else {
            System.out.println("Produtos no estoque:");
            System.out.println("ID - Qtd. - Nome - Fornecedor");
            for (Produto produto : produtos) {
                Fornecedor fornecedor = null;
                if (produto.getFornecedor_id() != 0) {
                    fornecedor = fornecedores.get(produto.getFornecedor_id() - 1);
                }
                System.out.println(produto.getId() + " - " + produto.getQtd() + " - " + produto.getNome() + " - "
                        + (fornecedor != null ? fornecedor.getNome() : "Fornecedor não informado"));
            }

        }
    }

    public Produto getProduto(int id) {
        for (Produto produto : estoque.listarProdutos()) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    public int cadastrarFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        Scanner scanner = new Scanner(System.in);
        String teclado;
        System.out.println("Digite o nome do fornecedor: ");
        teclado = scanner.nextLine();
        fornecedor.setNome(teclado);
        System.out.println("Digite o CNPJ do fornecedor: ");
        teclado = scanner.nextLine();
        fornecedor.setCnpj(teclado);
        System.out.println("Digite o endereço do fornecedor: ");
        teclado = scanner.nextLine();
        fornecedor.setEndereco(teclado);
        System.out.println("Digite o telefone do fornecedor: ");
        teclado = scanner.nextLine();
        fornecedor.setTelefone(teclado);
        fornecedor.setId(fornecedores.size() + 1); // Atribui um ID único ao fornecedor

        this.fornecedores.add(fornecedor);
        System.out.println("Fornecedor cadastrado com sucesso!");
        System.out.println(fornecedor.toString());
        salvarFornecedores();

        return fornecedor.getId();

    }

    public int cadastrarFornecedor(Fornecedor fornecedor) {
        fornecedor.setId(fornecedores.size() + 1); // Atribui um ID único ao fornecedor
        this.fornecedores.add(fornecedor);
        salvarFornecedores();

        return fornecedor.getId();
    }

    public void cadastroFornecedor_teste(Fornecedor fornecedor) {
        this.fornecedores.add(fornecedor);
        System.out.println("Fornecedor cadastrado com sucesso!");
        System.out.println(fornecedor.toString());
        salvarFornecedores();
    }

    public void listarFornecedores() {
        if (fornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado!");
        } else {
            System.out.println("Fornecedores cadastrados:");
            for (Fornecedor fornecedor : fornecedores) {
                System.out.println(fornecedor.getId() + " - " + fornecedor.getNome() + " - " + fornecedor.getCnpj()
                        + " - " + fornecedor.getEndereco() + " - " + fornecedor.getTelefone());
            }
        }
    }

    public void listaSimplesFornecedores() {
        if (fornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado!");
        } else {
            for (Fornecedor fornecedor : fornecedores) {
                System.out.println(fornecedor.getId() + " - " + fornecedor.getNome());
            }
        }
    }

    private void salvarProdutos() {
        try {
            List<String> linhas = new ArrayList<>();
            linhas.add("ID,Qtd,Nome,ID_fornecedor"); // Cabeçalho
            for (Produto produto : estoque.listarProdutos()) {
                String linha = produto.getId() + "," + produto.getQtd() + "," + produto.getNome() + ","
                        + produto.getFornecedor_id();
                linhas.add(linha);
            }
            Files.write(Paths.get(PRODUTOS_CSV), linhas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }

    private void carregarProdutos() 
    {
        System.out.println("Carregando produtos Almoxarifado...");
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
                if (produto.getId() > ID) {
                    ID = produto.getId();
                }
            }
            System.out.println("Produtos carregados com sucesso!");
        } catch (IOException e) {
            
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void salvarFornecedores() {
        try {
            List<String> linhas = new ArrayList<>();
            linhas.add("ID,Nome,CNPJ,Endereco,Telefone"); // Cabeçalho
            for (Fornecedor fornecedor : fornecedores) {
                String linha = fornecedor.getId() + "," + fornecedor.getNome() + "," + fornecedor.getCnpj() + ","
                        + fornecedor.getEndereco() + "," + fornecedor.getTelefone();
                linhas.add(linha);
            }
            Files.write(Paths.get(FORNECEDORES_CSV), linhas);
            System.out.println("Fornecedores salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar fornecedores: " + e.getMessage());
        }

    }

    private void carregarFornecedores() {
        System.out.println("Carregando fornecedores...");
        try {
            List<String> linhas = Files.readAllLines(Paths.get(FORNECEDORES_CSV));
            for (String linha : linhas.subList(1, linhas.size())) { // Ignora o cabeçalho
                String[] partes = linha.split(",");
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setNome(partes[1]);
                fornecedor.setCnpj(partes[2]);
                fornecedor.setEndereco(partes[3]);
                fornecedor.setTelefone(partes[4]);
                fornecedor.setId(Integer.parseInt(partes[0]));
                fornecedores.add(fornecedor);
            }
            System.out.println("Fornecedores carregados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    @Override
    public void salvarDados() {
        salvarProdutos();
        salvarFornecedores();
    }

    @Override
    public void carregarDados() {
        carregarProdutos();
        carregarFornecedores();
    }

	public void listaSetores() {
		System.out.println("Setores disponíveis:");
        System.out.println("1 - Farmácia");
        System.out.println("2 - Centro Cirúrgico");
        System.out.println("3 - Nutrição");
        System.out.println("4 - Almoxarifado");
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedores;
    }

    public Estoque getEstoque() {
        return this.estoque;
    }


}
