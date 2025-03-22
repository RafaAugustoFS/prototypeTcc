package com.onAcademy.tcc.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onAcademy.tcc.model.FeedbackForm;
import com.onAcademy.tcc.repository.FeedbackFormRepo;

/**
 * Serviço responsável pela gestão dos formulários de feedback dos alunos.
 * 
 * - Este serviço interage com o repositório de `FeedbackForm` para realizar
 * operações de CRUD (Create, Read, Update, Delete) relacionadas aos formulários
 * de feedback. - Os métodos fornecem a criação, consulta de feedbacks
 * específicos ou todos os feedbacks, e a consulta de feedbacks por aluno.
 * 
 * A classe utiliza a injeção de dependência do Spring para acessar o
 * repositório `FeedbackFormRepo` e realizar as operações de persistência no
 * banco de dados.
 * 
 * @see com.onAcademy.tcc.model.FeedbackForm
 * @see com.onAcademy.tcc.repository.FeedbackFormRepo
 */

@Service
public class FeedbackFormService {
	@Autowired
	FeedbackFormRepo feedbackFormRepo;

	/**
	 * Cria um novo feedback de aluno e o salva no banco de dados.
	 * 
	 * @param feedbackForm O objeto `FeedbackForm` contendo os dados do feedback a
	 *                     ser salvo.
	 * @return O feedback recém-criado e salvo no banco de dados.
	 */
	public FeedbackForm criarFeedbackStudent(FeedbackForm feedbackForm) {
		return feedbackFormRepo.save(feedbackForm);
	}

	/**
	 * Retorna todos os feedbacks de alunos armazenados no banco de dados.
	 * 
	 * @return Uma lista contendo todos os formulários de feedback registrados.
	 */
	public List<FeedbackForm> buscarTodosFeedbacksStudent() {
		List<FeedbackForm> buscarTodosFeedbacks = feedbackFormRepo.findAll();
		return buscarTodosFeedbacks;
	}

	/**
	 * Retorna um feedback específico, dado o seu ID. Se o feedback não for
	 * encontrado, retorna `null`.
	 * 
	 * @param id O ID do feedback que se deseja buscar.
	 * @return O feedback encontrado ou `null` se não houver nenhum feedback com o
	 *         ID fornecido.
	 */
	public FeedbackForm buscarFeedbackUnico(Long id) {
		Optional<FeedbackForm> existFeedback = feedbackFormRepo.findById(id);
		if (existFeedback.isPresent()) {
			return existFeedback.get();
		}
		return null;
	}

	/**
	 * Busca todos os feedbacks associados a um aluno específico.
	 * 
	 * @param id O ID do aluno para o qual se deseja buscar os feedbacks.
	 * @return Uma lista contendo os feedbacks associados ao aluno com o ID
	 *         fornecido.
	 */
	public List<FeedbackForm> buscarFeedbackPorAluno(Long id) {
		return feedbackFormRepo.findByRecipientStudentId(id);
	}
	
	
	 public List<Double> gerarFeedbackGeralDaTurma(Long idTurma) {
	        List<FeedbackForm> feedbacks = feedbackFormRepo.findByRecipientStudent_ClassSt_Id(idTurma);
	        System.out.println("Lista de feedbacks:" + feedbacks);
	        if (feedbacks.isEmpty()) {
	            throw new IllegalArgumentException("Não há feedbacks para esta turma!");
	        }

	        // Variáveis para calcular as médias
	        double somaResposta1 = 0;
	        double somaResposta2 = 0;
	        double somaResposta3 = 0;
	        double somaResposta4 = 0;
	        double somaResposta5 = 0;

	        // Processa cada feedback
	        for (FeedbackForm feedback : feedbacks) {
	            somaResposta1 += feedback.getResposta1();
	            somaResposta2 += feedback.getResposta2();
	            somaResposta3 += feedback.getResposta3();
	            somaResposta4 += feedback.getResposta4();
	            somaResposta5 += feedback.getResposta5();
	        }

	        // Calcula as médias
	        int totalFeedbacks = feedbacks.size();
	        double mediaResposta1 = somaResposta1 / totalFeedbacks;
	        double mediaResposta2 = somaResposta2 / totalFeedbacks;
	        double mediaResposta3 = somaResposta3 / totalFeedbacks;
	        double mediaResposta4 = somaResposta4 / totalFeedbacks;
	        double mediaResposta5 = somaResposta5 / totalFeedbacks;

	        // Retorna as médias
	        List<Double> mediaDeRespostas = new ArrayList<>();
	        mediaDeRespostas.add(mediaResposta1);
	        mediaDeRespostas.add(mediaResposta2);
	        mediaDeRespostas.add(mediaResposta3);
	        mediaDeRespostas.add(mediaResposta4);
	        mediaDeRespostas.add(mediaResposta5);
	        return mediaDeRespostas;
	    }

	

}
