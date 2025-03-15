package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repositorio;
	//@Autowired
	//private TelefoneAtualizador atualizador;
	@Autowired
	private ClienteRepositorio ClienteRepositorio;
	
	@GetMapping("/telefone/{id}")
	public Telefone obterTelefone(@PathVariable long id) {
		return repositorio.findById(id).orElse(null);
	}
	
	@GetMapping("/telefones")
	public List<Telefone> obterTelefones() {
		return repositorio.findAll();
	}
	
	@PostMapping("/cliente/{clienteId}/cadastrar")
	public void cadastrarTelefone(@PathVariable Long clienteId, @RequestBody Telefone telefone) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        cliente.getTelefones().add(telefone); // Adiciona o telefone à lista do cliente
	        ClienteRepositorio.save(cliente); // Salva o cliente com o novo telefone
	    }
	}

	
	@PutMapping("/cliente/{clienteId}/atualizar/{telefoneId}")
	public void atualizarTelefone(
	    @PathVariable Long clienteId,
	    @PathVariable Long telefoneId,
	    @RequestBody Telefone telefoneAtualizado) {

	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        for (Telefone telefone : cliente.getTelefones()) {
	            if (telefone.getId().equals(telefoneId)) {
	                telefone.setDdd(telefoneAtualizado.getDdd());
	                telefone.setNumero(telefoneAtualizado.getNumero());
	                ClienteRepositorio.save(cliente);
	                return;
	            }
	        }
	    }
	}

	
	@DeleteMapping("/cliente/{clienteId}/excluir/{telefoneId}")
	public void excluirTelefone(@PathVariable Long clienteId, @PathVariable Long telefoneId) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        boolean telefoneRemovido = cliente.getTelefones().removeIf(telefone -> telefone.getId().equals(telefoneId));
	        if (telefoneRemovido) {
	            ClienteRepositorio.save(cliente); // Salva o cliente sem o telefone excluído
	        }
	    }
	}


}
