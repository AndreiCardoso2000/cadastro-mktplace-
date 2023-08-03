package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante dao;
	private DaoCategoria daoCategoria;

	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getdaDaoRestaurante();
		this.daoCategoria = FactoryDao.getInstance().getDaoCategoria();
	}

	public void salvar(Restaurante restaurante) {
		validar(restaurante);

		boolean isPersistido = restaurante.getId() > 0;

		if (isPersistido) {
			dao.alterar(restaurante);
		} else {
			dao.inserir(restaurante);
		}
	}

	private void validar(Restaurante restaurante) {
		if (restaurante != null) {
			boolean isNomeInvalido = restaurante.getNome() == null 
					|| restaurante.getNome().isBlank()
					|| restaurante.getNome().length() < 3 
					|| restaurante.getNome().length() > 250;

			boolean isDescricaoInvalida = restaurante.getDescricao() == null 
					|| restaurante.getDescricao().isBlank()
					|| restaurante.getDescricao().length() < 10;

			boolean isCidadeInvalida = restaurante.getEndereco() == null
					|| restaurante.getEndereco().getCidade() == null 
					|| restaurante.getEndereco().getCidade().isBlank()
					|| restaurante.getEndereco().getCidade().length() < 3
					|| restaurante.getEndereco().getCidade().length() > 80;

			boolean isLogradouroInvalido = restaurante.getEndereco() == null
					|| restaurante.getEndereco().getLogradouro() == null
					|| restaurante.getEndereco().getLogradouro().isBlank()
					|| restaurante.getEndereco().getLogradouro().length() < 3
					|| restaurante.getEndereco().getLogradouro().length() > 200;

			boolean isBairroInvalido = restaurante.getEndereco() == null
					|| restaurante.getEndereco().getBairro() == null 
					|| restaurante.getEndereco().getBairro().isBlank()
					|| restaurante.getEndereco().getBairro().length() < 3
					|| restaurante.getEndereco().getBairro().length() > 250;

			boolean isCategoriaInvalida = restaurante.getCategoria() == null
					|| daoCategoria.buscarPor(restaurante.getCategoria().getId()) == null;

			if (isNomeInvalido) {
				throw new IllegalArgumentException("O nome é obrigatório e deve possuir entre 3 e 250 caracteres");
			}

			if (isDescricaoInvalida) {
				throw new IllegalArgumentException("A descrição é obrigatória e deve possuir no mínimo 10 caracteres");
			}

			if (isCidadeInvalida) {
				throw new IllegalArgumentException(
						"O endereço é obrigatório e o campo Cidade deve possuir entre 3 e 80 caracteres");
			}
			
			if (isLogradouroInvalido) {
				throw new IllegalArgumentException(
						"O endereço é obrigatório e o campo Cidade deve possuir entre 3 e 200 caracteres");
			}
			
			if (isBairroInvalido) {
				throw new IllegalArgumentException(
						"O endereço é obrigatório e o campo Cidade deve possuir entre 3 e 250 caracteres");
			}

			if (isCategoriaInvalida) {
				throw new IllegalArgumentException("A categoria informada não é válida");
			}
		} else {
			throw new NullPointerException("O restaurante não pode ser nulo");
		}
	}

	public void removerPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			dao.excluirPor(idDoRestaurante);
		} else {
			throw new IllegalArgumentException("O id para remoção do restaurante deve ser maior que zero");
		}
	}

	public Restaurante buscarPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			Restaurante restauranteEncontrado = dao.buscarPor(idDoRestaurante);
			if (restauranteEncontrado == null) {
				throw new IllegalArgumentException("Não foi encontrado restaurante para o código informado");
			}
			return restauranteEncontrado;
		} else {
			throw new IllegalArgumentException("O id para busca do restaurante deve ser maior que zero");
		}
	}

	public List<Restaurante> listarPor(String nome, Categoria categoria) {
		if ((nome == null || nome.isBlank() || nome.length() < 3) && categoria == null) {
			throw new IllegalArgumentException("Pelo menos um critério de busca deve ser informado");
		}
		return dao.listarPor(nome, categoria);
	}
	
	public List<Restaurante> listarTodos(){
		return dao.listarPor("%%", null);
	}
	
	public void excluirPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			this.dao.excluirPor(idDoRestaurante);	
		} else {
			throw new IllegalArgumentException("O id para exclusão deve ser maior que zero");

		}
	}
}
