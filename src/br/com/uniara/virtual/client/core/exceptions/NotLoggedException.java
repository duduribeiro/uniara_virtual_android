package br.com.uniara.virtual.client.core.exceptions;

public class NotLoggedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6972161813958136125L;

	public NotLoggedException() {
		super("Usuário não logado");
	}
}
