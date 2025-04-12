package TVC3.Almoxarifado.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import TVC3.Almoxarifado.sistema.Almoxarifado;
import TVC3.Almoxarifado.sistema.Farmacia;
import TVC3.Almoxarifado.sistema.Fornecedor;
import TVC3.Almoxarifado.sistema.Produto;



@Controller
public class Sistema {
    
    public static final int FARMACIA = 1;  
    public static final int CENTRO_CIRURGICO = 2;
    public static final int NUTRICAO = 3;
    public static final int ALMOXARIFADO = 4;

    Almoxarifado almoxarifado = new Almoxarifado();
    Farmacia farmacia = new Farmacia();

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home";
    }

    @GetMapping("/entrada-produto")
    public String mostrarFormularioEntrada(Model model) {
        // Obter lista de fornecedores do almoxarifado
        List<Fornecedor> fornecedores = almoxarifado.getFornecedores();
        model.addAttribute("fornecedores", fornecedores);
        return "entrada-produto";
    }

    @PostMapping("/entrada-produto")
    public String processarEntradaProduto(
            @RequestParam String nomeProduto,
            @RequestParam int idFornecedor,
            @RequestParam int quantidade,
            RedirectAttributes redirectAttributes) {

        Produto produto = new Produto(nomeProduto);
        produto.setFornecedor_id(idFornecedor);
        produto.setQtd(quantidade);

        almoxarifado.entradaProduto(produto);

        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Entrada de " + quantidade + " unidades de " + nomeProduto + " registrada com sucesso!");

        return "redirect:/home";
    }

    @GetMapping("/cadastrar-fornecedor")
    public String cadastrar_fornecedor() {
        return "cadastroFornecedor";
    }

    @PostMapping("/cadastrar-fornecedor") // Deve bater com o action do formulário
    public String processarCadastro(
            @RequestParam String nome,
            @RequestParam String cnpj,
            @RequestParam String endereco,
            @RequestParam String telefone,
            RedirectAttributes redirectAttributes) {

        Fornecedor novoFornecedor = new Fornecedor(nome, cnpj, endereco, telefone);
        almoxarifado.cadastrarFornecedor(novoFornecedor);

        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Fornecedor " + nome + " cadastrado com sucesso!");

        return "redirect:/home";
    }

    @GetMapping("/listar-estoque")
    public String listarEstoque(
            @RequestParam(name = "setor", defaultValue = "almoxarifado") String setor,
            Model model) {

        List<Produto> produtos;

        if ("farmacia".equalsIgnoreCase(setor)) {
            produtos = farmacia.getEstoque().listarProdutos();
        } else {
            produtos = almoxarifado.getEstoque().listarProdutos();
        }

        // Sempre passar a lista de fornecedores do almoxarifado
        model.addAttribute("produtos", produtos);
        model.addAttribute("fornecedores", almoxarifado.getFornecedores());
        model.addAttribute("setor", setor.toLowerCase());

        return "listar-estoque";
    }

    @GetMapping("/listar-fornecedores")
    public String listarFornecedores(Model model) {
        // Obtém a lista de fornecedores do almoxarifado
        List<Fornecedor> fornecedores = almoxarifado.getFornecedores();
        model.addAttribute("fornecedores", fornecedores);
        return "listar-fornecedores";
    }

    @GetMapping("/retirada-produto")
public String mostrarFormularioRetirada(Model model) {
    // Obter produtos do almoxarifado e fornecedores
    List<Produto> produtos = almoxarifado.getEstoque().listarProdutos();
    List<Fornecedor> fornecedores = almoxarifado.getFornecedores();
    
    model.addAttribute("produtosAlmoxarifado", produtos);
    model.addAttribute("fornecedores", fornecedores);
    
    return "retirada-produto";
}

@PostMapping("/retirada-produto")
public String processarRetiradaProduto(
        @RequestParam int idProduto,
        @RequestParam int quantidade,
        @RequestParam int setorDestino,
        RedirectAttributes redirectAttributes) {
    
    Produto produto = almoxarifado.getProduto(idProduto);
    
    if (produto == null) {
        redirectAttributes.addFlashAttribute("erro", "Produto não encontrado!");
        return "redirect:/retirada-produto";
    }
    
    if (quantidade <= 0 || quantidade > produto.getQtd()) {
        redirectAttributes.addFlashAttribute("erro", "Quantidade inválida!");
        return "redirect:/retirada-produto";
    }
    
    try {
        switch (setorDestino) {
            case FARMACIA:
                almoxarifado.saidaProduto(produto, quantidade, farmacia);
                redirectAttributes.addFlashAttribute("mensagemSucesso", 
                    quantidade + " unidades de " + produto.getNome() + 
                    " transferidas para a Farmácia com sucesso!");
                break;
                
            case CENTRO_CIRURGICO:
                // Implementar quando o centro cirúrgico estiver disponível
                redirectAttributes.addFlashAttribute("erro", 
                    "Centro Cirúrgico ainda não implementado");
                return "redirect:/retirada-produto";
                
            case NUTRICAO:
                // Implementar quando a nutrição estiver disponível
                redirectAttributes.addFlashAttribute("erro", 
                    "Nutrição ainda não implementada");
                return "redirect:/retirada-produto";
                
            default:
                redirectAttributes.addFlashAttribute("erro", "Setor inválido!");
                return "redirect:/retirada-produto";
        }
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("erro", 
            "Erro ao processar retirada: " + e.getMessage());
        return "redirect:/retirada-produto";
    }
    
    return "redirect:/home";
}

}