package com.devs.rest.dao;

import java.util.List;

import com.devs.rest.domain.Song;


public interface SongDao {
	
	public List<Song> findAll();
	
	public Song find(Long id);
	
	public List<Song> findByArtistOrGenre(String artist, String genre);
	
	public void add(Song song);
	
	public void update(Song song);
	
	public void delete(Long id);

}
