package com.LSoftwareLTDA.diarioDigital.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;

@Repository
public interface CapituloRepositorio extends JpaRepository<Capitulo, Long> {
	

}
