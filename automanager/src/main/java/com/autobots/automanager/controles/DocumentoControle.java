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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private DocumentoAtualizador atualizador;
	@Autowired
	private ClienteRepositorio ClienteRepositorio;
	
	@GetMapping("/documento/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		return repositorio.findById(id).orElse(null);
	}
	
	@GetMapping("/documentos")
	public List<Documento> obterDocumentos() {
		return repositorio.findAll();
	}
	
	@PostMapping("/cliente/{clienteId}/cadastrar")
	public void cadastrarDocumento(@PathVariable Long clienteId, @RequestBody Documento documento) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        // Verifica se o documento já existe para o cliente
	        boolean documentoExiste = cliente.getDocumentos().stream()
	            .anyMatch(doc -> doc.getTipo().equals(documento.getTipo()) && doc.getNumero().equals(documento.getNumero()));

	        if (!documentoExiste) {
	            cliente.getDocumentos().add(documento);
	            ClienteRepositorio.save(cliente);
	        } else {
	            System.out.println("Este documento já está cadastrado para o cliente."); // Apenas para depuração
	        }
	    }
	}


	
	@PutMapping("/cliente/{clienteId}/atualizar/{documentoId}")
	public void atualizarDocumento(
	    @PathVariable Long clienteId,
	    @PathVariable Long documentoId,
	    @RequestBody Documento documentoAtualizado) {

	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        for (Documento documento : cliente.getDocumentos()) {
	            if (documento.getId().equals(documentoId)) {
	                if (documentoAtualizado.getTipo() != null) {
	                    documento.setTipo(documentoAtualizado.getTipo());
	                }
	                if (documentoAtualizado.getNumero() != null) {
	                    documento.setNumero(documentoAtualizado.getNumero());
	                }
	                ClienteRepositorio.save(cliente);
	                return;
	            }
	        }
	    }
	}

	
	@DeleteMapping("/cliente/{clienteId}/excluir/{documentoId}")
	public void excluirDocumento(@PathVariable Long clienteId, @PathVariable Long documentoId) {
	    Cliente cliente = ClienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        boolean documentoRemovido = cliente.getDocumentos().removeIf(documento -> documento.getId().equals(documentoId));
	        if (documentoRemovido) {
	            ClienteRepositorio.save(cliente);
	        }
	    }
	}


}
