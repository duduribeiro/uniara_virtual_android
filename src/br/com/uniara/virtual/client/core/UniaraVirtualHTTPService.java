package br.com.uniara.virtual.client.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import br.com.uniara.virtual.client.core.exceptions.UniaraHTTPException;

class UniaraVirtualHTTPService {
	HttpClient client;
	private final String ENDPOINT = "http://virtual.uniara.com.br";
	
	protected UniaraVirtualHTTPService() {
	}
	
	private void initializeClient() {
		client = new DefaultHttpClient();
	}
	protected HttpResponse multipartPost(HashMap<String, String> params, String path) throws UniaraHTTPException {
		initializeClient();
		HttpPost post = new HttpPost(ENDPOINT + path);
		MultipartEntity multipartForm  = new MultipartEntity();
		if(params!=null) {
			for(String key: params.keySet()) {
				try {
					multipartForm.addPart(key, new StringBody(params.get(key)));
				} catch (UnsupportedEncodingException e) {
					throw new UniaraHTTPException("Encoding não suportado");
				}
			}
		}
		try {
			post.setEntity(multipartForm);
			return client.execute(post);
		} catch (Exception e) {
			throw new UniaraHTTPException("Erro ao fazer conexão");
		} 
	}
	
	protected HttpResponse get(String path) throws UniaraHTTPException {
		initializeClient();
		HttpGet get = new HttpGet(ENDPOINT+path);
		if(UniaraVirtualConfig.isLogged()) {
			get.addHeader("Cookie", UniaraVirtualConfig.getSessionID());
		}
		get.addHeader("Content-Type", "text/html; charset=iso-8859-1");
		try {
			return client.execute(get);
		} catch (ClientProtocolException e) {
			throw new UniaraHTTPException("Erro de protocolo");
		} catch (IOException e) {
			throw new UniaraHTTPException("Erro de IO");
		}
	}
	
	protected String responseToSring(HttpResponse response) {
		if(response == null) return "";
		response.addHeader("Content-Type", "text/html; charset=iso-8859-1");
		HttpEntity entity = response.getEntity();
		try {
			Scanner scan = new Scanner(entity.getContent(), "ISO-8859-1").useDelimiter("\\A");
			return scan.hasNext()? scan.next():"";
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
