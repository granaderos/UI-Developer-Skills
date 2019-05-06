package com.devs.rest.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.devs.rest.dao.SongDao;
import com.devs.rest.dao.SongJdbcDaoImpl;
import com.devs.rest.domain.Song;



public class SongServiceImpl implements SongService{
	
	SongDao songDao;

	public SongServiceImpl() {
		this.songDao = SongJdbcDaoImpl.getInstance();
		//this.userDao = UserHashMapDaoImpl.getInstance();
	}
	
	@Override
	public List<Song> findAll() {
		return songDao.findAll();
	}

	@Override
	public Song find(Long id) {
		return songDao.find(id);
	}

	@Override
	public List<Song> findByArtistOrGenre(String artist, String genre) {
		return songDao.findByArtistOrGenre(artist, genre);
	}

	@Override
	public void add(Song song) {
		if(validate(song)) {
			songDao.add(song);
		} else {
			throw new IllegalArgumentException("Fields artist and genre cannot be blank.");
		}
	}

	@Override
	public void upsert(Song song) {
		if (validate(song)) {
			if(song.getId() != null && song.getId() >= 0) {
				songDao.update(song);
			} else {
				songDao.add(song);
			}
		} else {
			throw new IllegalArgumentException("Fields artist and genre cannot be blank.");
		}
	}

	@Override
	public void delete(Long id) {
		songDao.delete(id);
	}
	
	private boolean validate(Song song) {
		return !StringUtils.isAnyBlank(song.getTitle(), song.getArtist(), song.getGenre());
	}

}
