package com.devs.rest.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.devs.rest.domain.Song;
import com.devs.rest.service.SongService;
import com.devs.rest.service.SongServiceImpl;


@Path("/songs")
public class SongsController {

	private SongService songService;

	public SongsController() {
		this.songService = new SongServiceImpl();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Song> getSongs(
			@QueryParam("artist") String artist, 
			@QueryParam("genre") String genre) {

		try {
			List<Song> songs;
			
			if (StringUtils.isAllBlank(artist, genre)) {
				songs = songService.findAll();
			} else {
				songs = songService.findByArtistOrGenre(artist, genre);
			}
						
			return songs;
			
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}

	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Song getSong(@PathParam("id") String id) {

		try {
			Long longId = Long.parseLong(id);
			Song song = songService.find(longId);
			return song;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSong(Song song) {

		try {
			songService.add(song);
			String result = "Song saved : " + song.getTitle() + " " + song.getArtist();
			return Response.status(201).entity(result).build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSong(Song song) {

		try {
			songService.upsert(song);
			String result = "Song updated : " + song.getTitle() + " " + song.getArtist();
			return Response.status(200).entity(result).build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}

	}

	@DELETE
	@Path("{id}")
	public Response deleteSong(@PathParam("id") String id) {

		try {
			Long longId = Long.parseLong(id);
			songService.delete(longId);
			String result = "Song deleted";
			return Response.status(200).entity(result).build();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
}
