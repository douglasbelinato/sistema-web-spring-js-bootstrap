package com.algaworks.brewer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import com.algaworks.brewer.model.validation.ClienteGroupSequenceProvider;
import com.algaworks.brewer.model.validation.group.CnpjGroup;
import com.algaworks.brewer.model.validation.group.CpfGroup;

@Entity
@Table(name= "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class) // Valida o Bean segundo uma sequência que irei passar
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = -799713344644516271L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@NotNull(message = "Tipo Pessoa é obrigatório")
	// EnumType.ORDINAL --> Grava no BD os valores inteiros ordinais de cada item da enum (0, 1, ...)
	// EnumType.STRING --> Grava no BD o texto do valor de cada item da enum (nesse caso, JURIDICA e FISICA)
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa")
	private TipoPessoa tipoPessoa;
	
	@NotBlank(message = "Número CPF/CNPJ é obrigatório")
	@CPF(groups = CpfGroup.class)  // Só valida CPF se o grupo (interface ou classe de marcação) estiver selecionado
	@CNPJ(groups = CnpjGroup.class) // Só valida CNPJ se o grupo (interface ou classe de marcação) estiver selecionado
	@Column(name = "cpf_cnpj")
	private String cpfCnpj;
	
	private String telefone;
	
	@Email(message = "E-mail inválido")
	private String email;
	
	@Embedded
	private Endereco endereco;
	
	// Callbacks JPA -- Antes de inserir e atualizar a base
	@PrePersist @PreUpdate	
	private void prePersistPreUpdate() {
		this.cpfCnpj = TipoPessoa.removerFormatacaoCpfCnpj(this.cpfCnpj);
	}
	
	// Callback JPA -- Após consultar a base
	@PostLoad
	private void postLoad() {
		this.cpfCnpj = this.tipoPessoa.formatar(this.cpfCnpj);
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public String getCpfCnpjSemFormatacao() {
		return TipoPessoa.removerFormatacaoCpfCnpj(this.cpfCnpj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
