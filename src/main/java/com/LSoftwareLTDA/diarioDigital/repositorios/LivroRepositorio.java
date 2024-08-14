package com.LSoftwareLTDA.diarioDigital.repositorios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;

@Repository
public interface LivroRepositorio extends JpaRepository<Livro, Long> {
	
	Optional<Livro> findByTituloAndUsuario_id(String titulo, UUID id);
	
	Optional<List<Livro>> findAllByUsuario_Id(UUID id);

}
