package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

	Cliente findByEndereco(Endereco endereco);
}