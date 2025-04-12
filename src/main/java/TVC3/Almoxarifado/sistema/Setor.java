package TVC3.Almoxarifado.sistema;


public abstract class Setor {
    
    
    public abstract void entradaProduto(Produto produto);
    public abstract void saidaProduto(Produto produto);
    public abstract void salvarDados();
    public abstract void carregarDados();  
    
}
