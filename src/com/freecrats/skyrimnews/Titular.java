package com.freecrats.skyrimnews;

public class Titular {
	private String titulo;
	private String subtitulo;
	private String url;
	
	public Titular(String _titulo, String _subtitulo,String _url){
		this.titulo = _titulo;
		this.subtitulo = _subtitulo;
		this.url = _url;
	}
	
	public String getTitulo(){
		return(this.titulo);
	}
	
	public String getSubtitulo(){
		return(this.subtitulo);
	}
	
	public String getURL(){
		return(this.url);
	}
	
}
