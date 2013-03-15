package br.com.uniara.virtual.client.core.exceptions;

public class LoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -420911055577788191L;
	
	public LoginException() {
		super("Usuário ou senha inválidos");
	}

}
