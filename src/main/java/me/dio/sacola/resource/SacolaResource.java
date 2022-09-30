package me.dio.sacola.resource;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.repository.model.Item;
import me.dio.sacola.repository.model.Sacola;
import me.dio.sacola.resource.dio.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ifood-devweek/sacola")
@RequiredArgsConstructor
public class SacolaResource {
    private final SacolaService sacolaService;

    @PostMapping
    public Item incluirItemSacola(@RequestBody ItemDto itemDto){
        return sacolaService.incluirItemSacola(itemDto);
    }

    @GetMapping("/{id}")
    public Sacola verSacola(@PathVariable("id") Long id){
        return sacolaService.verSacola(id);
    }

    @PatchMapping("/fecharSacola/{sacolaId}")
    public Sacola fecharSacola (@PathVariable("sacolaId") Long sacolaId, int formaPagamento){
        return sacolaService.fecharSacola(sacolaId, formaPagamento);
    }
}
