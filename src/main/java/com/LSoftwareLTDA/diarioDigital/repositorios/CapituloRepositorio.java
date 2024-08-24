package com.LSoftwareLTDA.diarioDigital.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;

@Repository
public interface CapituloRepositorio extends JpaRepository<Capitulo, Long> {
	
	Optional<Capitulo> findByTituloAndLivro_id(String tituloCap, Long idLivro);
	
	Optional<Capitulo> findByIdAndLivro_id(Long id, Long idlivro);
	
	Optional<Page<Capitulo>> findAllByLivro_id(Long idLivro, Pageable pageable);
	
	boolean existsByTituloAndLivro_id(String titulo, Long id);

}
