package com.LSoftwareLTDA.diarioDigital.repositorios;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;

@Repository
public interface LivroRepositorio extends JpaRepository<Livro, Long> {
	
	Optional<Livro> findByTituloAndUsuario_id(String titulo, Long id);
	Optional<Livro> findByIdAndUsuario_id(Long idlivro, Long id);
	
	Optional<Page<Livro>> findAllByUsuario_Id(UUID id, Pageable pageable);

}
