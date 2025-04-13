package com.onAcademy.tcc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.onAcademy.tcc.model.Note;
import com.onAcademy.tcc.repository.NoteRepo;

/**
 * Serviço responsável pela gestão das notas dos alunos.
 * 
 * - Este serviço permite realizar operações como criação, busca, atualização e
 * exclusão de notas. - Antes de criar uma nova nota, o serviço verifica se já
 * existe uma nota para o aluno, disciplina e bimestre informados. - Além disso,
 * o serviço permite buscar todas as notas ou uma nota específica, e atualizar
 * ou deletar uma nota existente.
 * 
 * @see com.onAcademy.tcc.model.Note
 * @see com.onAcademy.tcc.repository.NoteRepo
 */

@Service
public class NoteService {
	@Autowired
	private NoteRepo noteRepo;

	/**
	 * Cria uma nova nota para um aluno, disciplina e bimestre específicos.
	 * 
	 * - O método verifica se já existe uma nota registrada para o aluno, disciplina
	 * e bimestre informados. - Se já existir uma nota para essa combinação, uma
	 * exceção é lançada com a mensagem "Já existe uma nota para este aluno,
	 * disciplina e bimestre." - Caso contrário, a nova nota é salva no repositório.
	 * 
	 * @param note O objeto `Note` contendo os dados da nova nota.
	 * @return A nota criada e salva no banco de dados.
	 * @throws RuntimeException Se já existir uma nota para o aluno, disciplina e
	 *                          bimestre.
	 */
	public Note criarNotas(Note note) {
		boolean noteExists = noteRepo.existsByStudentIdAndDisciplineIdAndBimestre(note.getStudentId(),
				note.getDisciplineId(), note.getBimestre());

		if (noteExists) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma nota para este aluno, disciplina e bimestre.");
		}
		return noteRepo.save(note);
	}

	/**
	 * Busca todas as notas registradas no banco de dados.
	 * 
	 * @return Uma lista contendo todas as notas.
	 */
	public List<Note> buscarNotas() {
		List<Note> buscarNotas = noteRepo.findAll();
		return buscarNotas;
	}

	/**
	 * Busca uma nota específica pelo seu ID.
	 * 
	 * - O método verifica se a nota existe no banco de dados. - Se a nota for
	 * encontrada, ela é retornada. Caso contrário, o método retorna `null`.
	 * 
	 * @param id O ID da nota a ser buscada.
	 * @return A nota encontrada, ou `null` se não for encontrada.
	 */
	public Note buscarNotaUnica(Long id) {
		Optional<Note> existNote = noteRepo.findById(id);
		if (existNote.isPresent()) {
			return existNote.get();
		}
		return null;
	}

	/**
	 * Atualiza os dados de uma nota existente.
	 * 
	 * - O método busca a nota pelo ID. Se a nota for encontrada, seus dados são
	 * atualizados com os novos valores fornecidos. - Se a nota não for encontrada,
	 * o método retorna `null`.
	 * 
	 * @param id   O ID da nota a ser atualizada.
	 * @param note O objeto `Note` contendo os novos dados da nota.
	 * @return A nota atualizada, ou `null` se a nota não for encontrada.
	 */
	public Note atualizarNotas(Long id, Note note) {

		Optional<Note> existNote = noteRepo.findById(id);
		if (existNote.isPresent()) {
			Note atualizarNota = existNote.get();
			atualizarNota.setDisciplineId(note.getDisciplineId());
			atualizarNota.setNota(note.getNota());
			atualizarNota.setBimestre(note.getBimestre());
			atualizarNota.setStatus(note.getStatus());
			atualizarNota.setStudentId(note.getStudentId());

			return noteRepo.save(atualizarNota);
		}

		return null;
	}

	/**
	 * Deleta uma nota existente.
	 * 
	 * - O método busca a nota pelo ID. Se a nota for encontrada, ela é deletada do
	 * banco de dados e retornada. - Se a nota não for encontrada, o método retorna
	 * `null`.
	 * 
	 * @param id O ID da nota a ser deletada.
	 * @return A nota deletada, ou `null` se a nota não for encontrada.
	 */
	public Note deletarNota(Long id) {
		Optional<Note> existNote = noteRepo.findById(id);
		if (existNote.isPresent()) {
			Note deletarNote = existNote.get();
			noteRepo.delete(deletarNote);
			return deletarNote;
		}

		return null;
	}

}
