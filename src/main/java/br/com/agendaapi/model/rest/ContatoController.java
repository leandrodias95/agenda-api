package br.com.agendaapi.model.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.agendaapi.model.entity.Contato;
import br.com.agendaapi.model.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contatos")
@CrossOrigin("*")
public class ContatoController {

	private final ContatoRepository repository;
	
	@PostMapping(value="insert")
	@ResponseStatus(HttpStatus.CREATED)
	public  Contato save(@RequestBody Contato contato) {
		return repository.save(contato);
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		repository.deleteById(id);
		
	}
	
	@GetMapping
	public Page<Contato> list(
			@RequestParam(value= "page", defaultValue="0") Integer pagina, //primeira página
			@RequestParam(value= "pageSize", defaultValue="10") Integer tamanhoPagina //10 itens por página
			){
		Sort sort = Sort.by(Sort.Direction.ASC, "nome");
		PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
		return repository.findAll(pageRequest);
	}
	
	@PatchMapping("{id}/favorito") //quando não é uma atualização total
	public void favorite(@PathVariable Integer id, @RequestBody Boolean favorito) {
		Optional<Contato> contato = repository.findById(id);
		contato.ifPresent(c ->{
			c.setFavorito(favorito);
			repository.save(c);
		});
	}
	
	 @PutMapping("{id}/foto")
	    public byte[] addPhoto(@PathVariable Integer id, @RequestParam("foto") MultipartFile arquivo) {
	        Optional<Contato> optionalContato = repository.findById(id);
	        return optionalContato.map(contato -> {
	            try {
	                InputStream is = arquivo.getInputStream();
	                byte[] bytes = new byte[(int) arquivo.getSize()];
	                IOUtils.readFully(is, bytes);
	                contato.setFoto(bytes);
	                repository.save(contato);
	                is.close();
	                return bytes;
	            } catch (IOException e) {
	                e.printStackTrace();
	                return null;
	            }
	        }).orElse(null);
	    }
}
