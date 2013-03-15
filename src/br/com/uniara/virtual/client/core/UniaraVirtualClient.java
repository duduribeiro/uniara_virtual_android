package br.com.uniara.virtual.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.uniara.virtual.client.core.exceptions.NotLoggedException;
import br.com.uniara.virtual.client.core.exceptions.UniaraHTTPException;
import br.com.uniara.virtual.client.models.Aluno;
import br.com.uniara.virtual.client.models.Materia;

public class UniaraVirtualClient {

	private UniaraVirtualHTTPService http = new UniaraVirtualHTTPService();
	
	private static UniaraVirtualClient instance;
	
	public static UniaraVirtualClient getIntance() {
		if(instance == null) instance = new UniaraVirtualClient();
		return instance;
	}
	
	public boolean isLogged() {
		return UniaraVirtualConfig.isLogged();
	}
	public boolean login(String ra, String password) throws UniaraHTTPException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", ra);
		params.put("senha", password);
		logout();
		HttpResponse response = http.multipartPost(params, "/login/");
		Header[] headers = response.getHeaders("Set-Cookie");
		if(headers != null) {
			for(Header header:headers) {
				if(header.getValue().contains("UVXS233E3=S")) {
					UniaraVirtualConfig.setSessionID(response.getFirstHeader("Set-Cookie").getValue());
					return true;
					
				}
			}
		}
		return false;
	}
	
	public void logout() {
		UniaraVirtualConfig.setSessionID(null);
	}
	public void verificaLogin() throws NotLoggedException {
		if (UniaraVirtualConfig.getSessionID() == null) {
			throw new NotLoggedException();
		}
	}
	
	public List<Materia> listarNotas() throws NotLoggedException {
		// "/alunos/consultas/notas/";
		verificaLogin();
		try {
			Document doc = Jsoup.parse(http.responseToSring( http.get("/alunos/consultas/notas/") ));
			Element disciplinas = doc.select("div#conteudo").first().nextElementSibling();
			
			Elements rows = disciplinas.select("tr");
			rows.remove(0);
			List<Materia> materias = new ArrayList<Materia>();
			for(Element element: rows) {
				Materia materia = new Materia();
				Element materiaElement = element.select("td").first();
				materia.setTitulo(StringEscapeUtils.unescapeHtml4(materiaElement.text()));
				
				Element notaElement = materiaElement.nextElementSibling();
				materia.setNota1bim(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setNota2bim(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setNota3bim(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setNota4bim(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setNotaSub(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setNotaMedia(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setNotaCicloRec(notaElement.text());
				notaElement = notaElement.nextElementSibling();
				materia.setSituacao(notaElement.text());
				
				materias.add(materia);
			}
			return materias;
		} catch (UniaraHTTPException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Aluno informacoesAluno() throws NotLoggedException {
		verificaLogin();
		Aluno aluno = null;
		try {
			aluno = new Aluno();
			Document doc = Jsoup.parse(http.responseToSring( http.get("/alunos/servicos/dados/") ));
			Element nameElement = doc.select("div#conteudo").first().nextElementSibling();
			
			aluno.setNome(nameElement.val());
			
			Element tableElement = nameElement.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();

			String email = tableElement.select("tr").eq(2).select("td").eq(1).select("input").val().toLowerCase();
			aluno.setEmail(email);
			
		} catch (UniaraHTTPException e) {
			e.printStackTrace();
		}
		return aluno;
			
	}
}
