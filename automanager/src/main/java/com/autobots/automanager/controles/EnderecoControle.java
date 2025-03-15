
package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.autobots.automanager.entidades.Endereco;
//import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	
	@Autowired
	private ClienteRepositorio ClienteRepositorio;
	
	@Autowired
	private EnderecoRepositorio repositorio;
	
	// @Autowired
	// private EnderecoAtualizador atualizador;
	
	@GetMapping("/endereco/{id}")
	public Map<String, Object> obterEndereco(@PathVariable long id) {
	    Endereco endereco = repositorio.findById(id).orElse(null);
	    
	    if (endereco == null) {
	        return Collections.singletonMap("Erro", "Endereço não encontrado");
	    }

	    // Buscar o cliente que tem esse endereço
	    Cliente cliente = ClienteRepositorio.findByEndereco(endereco);

	    // Criar um objeto de resposta contendo o endereço e o cliente associado
	    Map<String, Object> resposta = new HashMap<>();
	    resposta.put("endereco", endereco);
	    resposta.put("cliente", cliente);

	    return resposta;
	}

	
	@GetMapping("/enderecos-clientes")
	public List<String> obterEnderecosComClientes() {
	    List<Cliente> clientes = ClienteRepositorio.findAll();
	    List<String> resposta = new ArrayList<>();

	    for (Cliente cliente : clientes) {
	        if (cliente.getEndereco() != null) {
	            resposta.add("Cliente: " + cliente.getNome() + 
	                         " | Endereço: " + cliente.getEndereco().getRua() +
	                         ", " + cliente.getEndereco().getNumero() +
	                         ", " + cliente.getEndereco().getBairro() +
	                         ", " + cliente.getEndereco().getCidade());
	        }
	    }
	    return resposta;
	}

	
	@PostMapping("/cliente/{clienteId}/cadastrar-endereco")
	public void cadastrarEndereco(@PathVariable Long clienteId, @RequestBody Endereco endereco) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        cliente.setEndereco(endereco);
	        ClienteRepositorio.save(cliente); // Salva o cliente com o novo endereço
	    }
	}

	
	@PutMapping("/cliente/{clienteId}/atualizar-endereco")
	public void atualizarEndereco(@PathVariable Long clienteId, @RequestBody Endereco enderecoAtualizado) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null && cliente.getEndereco() != null) {
	        Endereco endereco = cliente.getEndereco();
	        
	        // Atualiza os campos do endereço existente
	        if (enderecoAtualizado.getEstado() != null) endereco.setEstado(enderecoAtualizado.getEstado());
	        if (enderecoAtualizado.getCidade() != null) endereco.setCidade(enderecoAtualizado.getCidade());
	        if (enderecoAtualizado.getBairro() != null) endereco.setBairro(enderecoAtualizado.getBairro());
	        if (enderecoAtualizado.getRua() != null) endereco.setRua(enderecoAtualizado.getRua());
	        if (enderecoAtualizado.getNumero() != null) endereco.setNumero(enderecoAtualizado.getNumero());
	        if (enderecoAtualizado.getCodigoPostal() != null) endereco.setCodigoPostal(enderecoAtualizado.getCodigoPostal());
	        if (enderecoAtualizado.getInformacoesAdicionais() != null) 
	            endereco.setInformacoesAdicionais(enderecoAtualizado.getInformacoesAdicionais());

	        ClienteRepositorio.save(cliente);
	    }
	}

	
	@DeleteMapping("/cliente/{clienteId}/excluir")
	public void excluirEndereco(@PathVariable Long clienteId) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null && cliente.getEndereco() != null) {
	        cliente.setEndereco(null);
	        ClienteRepositorio.save(cliente);
	    }
	}


}
