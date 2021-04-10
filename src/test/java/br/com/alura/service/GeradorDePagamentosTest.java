package br.com.alura.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.FinalizarLeilaoService;
import br.com.alura.leilao.service.GeradorDePagamento;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GeradorDePagamentosTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    PagamentoDao pagamentoDao;

    @Captor
    ArgumentCaptor<Pagamento> pagamentoCaptor;


    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao() {
        Leilao leilao = leilao();
        Lance lanceVencedor = leilao.getLanceVencedor();
        geradorDePagamento.gerarPagamento(lanceVencedor);
        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());
        Pagamento pagamentoCaptorValue = pagamentoCaptor.getValue();
        Assert.assertEquals(LocalDate.now().plusDays(1), pagamentoCaptorValue.getVencimento());
        Assert.assertEquals(lanceVencedor.getValor(), pagamentoCaptorValue.getValor());
        Assert.assertEquals(lanceVencedor.getUsuario(), pagamentoCaptorValue.getUsuario());
        Assert.assertEquals(lanceVencedor.getLeilao(), leilao);
        Assert.assertFalse(pagamentoCaptorValue.getPago());
    }


    private Leilao leilao() {

        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Beltrano"),
                new BigDecimal("600"));

        leilao.propoe(primeiro);
        leilao.setLanceVencedor(primeiro);
        return leilao;
    }


}
