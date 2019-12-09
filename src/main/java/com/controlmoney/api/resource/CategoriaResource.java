package com.controlmoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.controlmoney.api.event.RecursoCriadoEvent;
import com.controlmoney.api.model.Categoria;
import com.controlmoney.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	//public ResponseEntity<?>   listar(){
	public List<Categoria> listar(){	
		List<Categoria> categorias = categoriaRepository.findAll();
		
		//return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.notFound().build();
		return categorias;
	}
	
	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid  @RequestBody Categoria categoria, HttpServletResponse response){
		
		Categoria categoriaSalva  = categoriaRepository.save(categoria);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable  Long codigo) {		
		
		Categoria categoria  = categoriaRepository.findById(codigo).orElse(null);
		
		
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	
}
