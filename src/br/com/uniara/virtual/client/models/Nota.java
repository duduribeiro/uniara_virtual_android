package br.com.uniara.virtual.client.models;

import java.math.BigDecimal;

public class Nota {
	
	private BigDecimal nota;

	
	public void setNotaFromString(String nota) {
		if(nota == null || nota.trim().equals("--")) {
			this.nota = new BigDecimal(0);
		}else {
			this.nota = new BigDecimal(nota);
		}
		
	}
	public BigDecimal getNota() {
		return nota;
	}

	public void setNota(BigDecimal nota) {
		this.nota = nota;
	}
	
}
