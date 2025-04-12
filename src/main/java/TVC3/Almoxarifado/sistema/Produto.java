package TVC3.Almoxarifado.sistema;


public class Produto {

    public Produto(String nome) {
        this.nome = nome;
        this.fornecedor_id= 0;
        this.qtd = 0;
    }

    public Produto(Fornecedor fornecedor, String nome) {
        this.fornecedor_id = fornecedor.getId();
        this.nome = nome;
        this.qtd = 0;
    }

    public Produto(int ID_fornecedor, String nome) {
        this.fornecedor_id = ID_fornecedor;
        this.nome = nome;
        this.qtd = 0;
    }

    // Atributos
    private String nome;
    private int id;
    private int qtd;
    private int fornecedor_id ;

    // Métodos
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor_id = fornecedor.getId();
    }

    public void setFornecedor_id(int fornecedor_id) {
        this.fornecedor_id = fornecedor_id;
    }
    
    public int getFornecedor_id() {
        return fornecedor_id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
    public int getQtd() {
        return qtd;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    

}
