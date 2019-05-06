package com.devs.rest.service;

import java.util.List;

import com.devs.rest.domain.Song;


public interface SongService {

	public List<Song> findAll();
	
	public Song find(Long id);
	
	public List<Song> findByArtistOrGenre(String artist, String genre);
	
	public void add(Song song);
	
	public void upsert(Song song);
	
	public void delete(Long id);

}
