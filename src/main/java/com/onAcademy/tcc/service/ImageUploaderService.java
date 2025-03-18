package com.onAcademy.tcc.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Serviço responsável pelo upload de imagens para o serviço Cloudinary.
 * 
 * - Este serviço utiliza a biblioteca `Cloudinary` para realizar o upload de
 * imagens base64 para a plataforma Cloudinary. - O método `uploadBase64Image`
 * recebe uma imagem no formato base64, a decodifica, e faz o upload para o
 * Cloudinary, retornando a URL da imagem armazenada.
 * 
 * O Cloudinary é um serviço de gerenciamento de mídia em nuvem, que oferece
 * funcionalidades como armazenamento e manipulação de imagens e vídeos. Este
 * serviço facilita a integração da aplicação com o Cloudinary para o
 * armazenamento das imagens.
 * 
 * @see com.cloudinary.Cloudinary
 * @see com.cloudinary.utils.ObjectUtils
 */

@Service
public class ImageUploaderService {

	@Autowired
	private Cloudinary cloudinary;

	/**
	 * Realiza o upload de uma imagem codificada em base64 para o serviço
	 * Cloudinary.
	 * 
	 * - O método decodifica a string base64 fornecida para obter os bytes da imagem
	 * e, em seguida, envia esses bytes para o Cloudinary. - Após o upload, o método
	 * retorna a URL da imagem armazenada no Cloudinary. - Caso ocorra algum erro
	 * durante o processo de upload, uma exceção `RuntimeException` é lançada com a
	 * mensagem de erro correspondente.
	 * 
	 * @param base64Image A imagem no formato base64 que será enviada ao Cloudinary.
	 * @return A URL da imagem armazenada no Cloudinary.
	 * @throws RuntimeException Se ocorrer um erro durante o processo de upload da
	 *                          imagem.
	 */
	public String uploadBase64Image(String base64Image) {
		try {

			if (base64Image.contains(",")) {
				base64Image = base64Image.split(",")[1];
			}

			byte[] imageBytes = Base64.getDecoder().decode(base64Image);

			Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap("resource_type", "image"));
			return uploadResult.get("url").toString();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao enviar imagem para o Cloudinary: " + e.getMessage());
		}
	}
}
