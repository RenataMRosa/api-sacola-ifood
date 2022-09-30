package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.repository.model.Item;
import me.dio.sacola.repository.model.Restaurante;
import me.dio.sacola.repository.model.Sacola;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resource.dio.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public Item incluirItemSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getIdSacola());
        if(sacola.isFechada())
            throw new RuntimeException("Essa sacola está fechada!");

        Item item = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItens();
        if(itensDaSacola.isEmpty()) {
            itensDaSacola.add(item);
        }else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteAdicionado = item.getProduto().getRestaurante();
            if(restauranteAtual.equals(restauranteAdicionado)){
                itensDaSacola.add(item);
            } else {
                throw new RuntimeException("Não é possível adicionar produtos de restaurantes diferentes. Feche a sacola ou esvazie");
            }
        }

        List<Double> valorItem = new ArrayList<>();
        for (Item i : itensDaSacola) {
            double valorTotalItem = i.getProduto().getValorUnitario() * i.getQuantidade();
            valorItem.add(valorTotalItem);
        }

        Double valorTotalSacola = 0.0;
        for(Double valorTotalPorItem : valorItem) valorTotalSacola += valorTotalPorItem;

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return item;
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numFormaPagamento) {
        Sacola sacola = verSacola(id);
        if(sacola.getItens().isEmpty())
            throw new RuntimeException("Inclua itens na sacola!");

        FormaPagamento formaPagamento =
                numFormaPagamento == 0? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;

        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
}
