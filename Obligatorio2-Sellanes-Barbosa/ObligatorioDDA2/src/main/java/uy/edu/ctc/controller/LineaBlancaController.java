package uy.edu.ctc.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import uy.edu.ctc.entity.Electrodomestico;
import uy.edu.ctc.entity.LineaBlanca;
import uy.edu.ctc.repository.ElectrodomesticoRepository;

@Controller
@RequestMapping("/lineaBlanca")
public class LineaBlancaController {

    @Autowired
    private ElectrodomesticoRepository lineaBlancaRepository;

    // Página de inicio
    @GetMapping({"", "/"})
    public String inicio(Model model) {
        List<Electrodomestico> electrodomesticos = lineaBlancaRepository.findAll();

        List<LineaBlanca> lineaBlanca = electrodomesticos.stream()
                .filter(e -> e instanceof LineaBlanca)
                .map(e -> (LineaBlanca) e)
                .collect(Collectors.toList());

        model.addAttribute("lineaBlanca", lineaBlanca);

        return "index";
    }

    // Crear nueva línea blanca - Formulario
    @GetMapping("/lineaBlancaForm")
    public String crear(Model model) {
        model.addAttribute("lineaBlanca", new LineaBlanca());
        return "lineaBlancaForm";
    }

    // Guardar nueva línea blanca
    @PostMapping("/lineaBlancaForm")
    public String guardar(@Valid @ModelAttribute LineaBlanca lineaBlanca, BindingResult result,
                          RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "lineaBlancaForm";
        }

        lineaBlancaRepository.save(lineaBlanca);

        attr.addFlashAttribute("msg", "Línea Blanca guardada");

        return "redirect:/lineaBlanca";
    }

    // Editar línea blanca - Formulario
    @GetMapping("/editar/{id}")
    public String mostrarFormEditarContacto(@PathVariable Long id, Model modelo) {
        Optional<Electrodomestico> lineaBlanca = lineaBlancaRepository.findById(id);
        modelo.addAttribute("lineaBlanca", lineaBlanca.orElse(new LineaBlanca()));
        return "lineaBlancaForm";
    }

    // Actualizar línea blanca
    @PostMapping("/editar/{id}")
    public String actualizarLineaBlanca(@PathVariable Long id, @Validated LineaBlanca lineaBlanca,
                                        BindingResult bindingResult, RedirectAttributes redirect, Model modelo) {
        Optional<Electrodomestico> lineaBlancaDB = lineaBlancaRepository.findById(id);
        if (bindingResult.hasErrors()) {
            modelo.addAttribute("lineaBlanca", lineaBlanca);
            return "lineaBlancaForm";
        }

        lineaBlancaDB.ifPresent(electrodomestico -> {
            if (electrodomestico instanceof LineaBlanca) {
                LineaBlanca lb = (LineaBlanca) electrodomestico;

                // Actualizar los campos según sea necesario
                lb.setNombre(lineaBlanca.getNombre());
                lb.setModelo(lineaBlanca.getModelo());
                lb.setMarca(lineaBlanca.getMarca());
                lb.setPrecio(lineaBlanca.getPrecio());
                lb.setCapacidad(lineaBlanca.getCapacidad());

                // Guardar la actualización
                lineaBlancaRepository.save(lb);
            }
        });

        redirect.addFlashAttribute("msgExito", "La Línea Blanca ha sido actualizada correctamente");
        return "redirect:/lineaBlanca";
    }

    // Eliminar por id
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        lineaBlancaRepository.deleteById(id);
        return "redirect:/lineaBlanca";
    }
}
