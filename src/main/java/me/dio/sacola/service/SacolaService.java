package me.dio.sacola.service;

import me.dio.sacola.repository.model.Item;
import me.dio.sacola.repository.model.Sacola;
import me.dio.sacola.resource.dio.ItemDto;

public interface SacolaService {

    Item incluirItemSacola(ItemDto itemDto);
    Sacola verSacola(Long id);
    Sacola fecharSacola(Long id, int formaPagamento);
}
