package uy.edu.ctc.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import uy.edu.ctc.entity.Electrodomestico;
import uy.edu.ctc.entity.LineaBlanca;
import uy.edu.ctc.entity.UsoPersonal;
import uy.edu.ctc.repository.ElectrodomesticoRepository;

@Controller
@RequestMapping("/usoPersonal")
public class UsoPersonalController {
	
		
	@Autowired
	ElectrodomesticoRepository usoPersonalRepository;

	@GetMapping({"", "/"}) 
	public String inicio(Model model){

	  List<Electrodomestico> electrodomesticos = usoPersonalRepository.findAll();

	  List<UsoPersonal> usoPersonal = electrodomesticos.stream()
	      .filter(e -> e instanceof UsoPersonal)
	      .map(e -> (UsoPersonal) e)
	      .collect(Collectors.toList());

	  model.addAttribute("usoPersonal", usoPersonal);
	  
	  return "index";
	}
	  
	  @GetMapping("/usoPersonalForm")
	  public String crear(Model model){
	    model.addAttribute("usoPersonal", new UsoPersonal());
	    return "usoPersonalForm";
	  }
	  
	  @PostMapping("/usoPersonalForm")
	  public String guardar(@Valid @ModelAttribute UsoPersonal usoPersonal, BindingResult result, RedirectAttributes attr) {
	    
	    if(result.hasErrors()){
	        return "usoPersonalForm";
	    }
	    
	    usoPersonalRepository.save(usoPersonal);
	    
	    attr.addFlashAttribute("msg", "Uso Personal guardada");
	    return "redirect:/usoPersonal";
	  }
	  
	  @PostMapping("/eliminar/{id}")
	  public String eliminar(@PathVariable Long id) {
		  usoPersonalRepository.deleteById(id);
	  	return "redirect:/usoPersonal";
	  }
	  
	  
	  @GetMapping("/editar/{id}")
	  public String mostrarFormEditarContacto(@PathVariable Long id, Model modelo) {
	      Optional<Electrodomestico> usoPersonal = usoPersonalRepository.findById(id);
	      modelo.addAttribute("usoPersonal", usoPersonal.orElse(new UsoPersonal()));
	      return "usoPersonalForm";
	  }
	  
	  @PostMapping("/editar/{id}")
	  public String actualizarLineaBlanca(@PathVariable Long id, @Validated UsoPersonal usoPersonal,
	          BindingResult bindingResult, RedirectAttributes redirect, Model modelo) {
	      Optional<Electrodomestico> usoPersonalDB = usoPersonalRepository.findById(id);
	      if (bindingResult.hasErrors()) {
	          modelo.addAttribute("usoPersonal", usoPersonal);
	          return "usoPersonalForm"; 
	      }

	      usoPersonalDB.ifPresent(electrodomestico -> {
	          if (electrodomestico instanceof UsoPersonal) {
	        	  UsoPersonal lb = (UsoPersonal) electrodomestico;

	              // Actualizar los campos según sea necesario
	              lb.setNombre(usoPersonal.getNombre());
	              lb.setModelo(usoPersonal.getModelo());
	              lb.setMarca(usoPersonal.getMarca());
	              lb.setPrecio(usoPersonal.getPrecio());
	              lb.setPotencia(usoPersonal.getPotencia());

	              // Guardar la actualización
	              usoPersonalRepository.save(lb);
	          }
	      });

	      redirect.addFlashAttribute("msgExito", "La Línea Blanca ha sido actualizada correctamente");
	      return "redirect:/usoPersonal";
	  }

	   
}
