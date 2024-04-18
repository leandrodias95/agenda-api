package br.com.agendaapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
	@Column(length = 150, nullable = false)
	private String nome;
	
	@Column(length = 150, nullable = false)
	private String email;
	
	@Column(length = 150, nullable = false)
	private Boolean favorito;
	
	@Column
	@Lob //diz que uma coluna de byte
	private byte[] foto; 
}
