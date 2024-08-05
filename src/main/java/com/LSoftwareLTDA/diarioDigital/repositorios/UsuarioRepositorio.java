package com.LSoftwareLTDA.diarioDigital.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID> {

}
