package com.freecrats.skyrimnews;

public class Titular {
	private String titulo;
	private String subtitulo;
	
	public Titular(String _titulo, String _subtitulo){
		this.titulo = _titulo;
		this.subtitulo = _subtitulo;
	}
	
	public String getTitulo(){
		return(this.titulo);
	}
	
	public String getSubtitulo(){
		return(this.subtitulo);
	}
	
}
