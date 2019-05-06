package com.devs.rest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
//import org.hsqldb.jdbc.JDBCDataSource;

import com.devs.rest.domain.Song;


public class SongJdbcDaoImpl implements SongDao {

	private static SongJdbcDaoImpl INSTANCE;

//	private JDBCDataSource dataSource;
//	private Connection connection;

	String pattern = "yyyy-MM-dd";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	
	static public SongJdbcDaoImpl getInstance() {

		SongJdbcDaoImpl instance;
		if (INSTANCE != null) {
			instance = INSTANCE;
		} else {
			instance = new SongJdbcDaoImpl();
			INSTANCE = instance;
		}

		return instance;
	}

	private SongJdbcDaoImpl() {
		init();
	}

	private void init() {
//		dataSource = new JDBCDataSource();
//		dataSource.setDatabase("jdbc:hsqldb:mem:SONG");
//		dataSource.setUser("username");
//		dataSource.setPassword("password");

//		createSongTable();
//		insertInitSongs();
	}
	
	public Connection getConnection() {
		Connection connection = null;
		String host = "127.0.0.1";
		String port = "3306";
		String dbname = "songs";
		String username = "root";
		String password = "";
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			DriverManager.setLoginTimeout(10);
			connection = DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+dbname, username, password);
			System.out.println("Connection: " + connection);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	

	@Override
	public List<Song> findAll() {

		return findByArtistOrGenre(null, null);
	}

	@Override
	public Song find(Long id) {

		Song song = null;

		if (id != null) {
			String sql = "SELECT * FROM SONGS where id = ?";
			try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

				ps.setInt(1, id.intValue());
				ResultSet results = ps.executeQuery();

				if (results.next()) {
					song = new Song(Long.valueOf(results.getInt("id")), results.getString("title"),
							results.getString("artist"), results.getString("label"), results.getDate("date"), results.getString("genre"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return song;
	}

	@Override
	public List<Song> findByArtistOrGenre(String artist, String genre) {
		List<Song> songs = new ArrayList<>();

		String sql = "SELECT * FROM SONGS WHERE artist LIKE ? OR genre LIKE ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, createSearchValue(artist));
			ps.setString(2, createSearchValue(genre));
			
			ResultSet results = ps.executeQuery();

			while (results.next()) {
				Song song = new Song(Long.valueOf(results.getInt("id")), results.getString("title"),
						results.getString("artist"), results.getString("label"), results.getDate("date"), results.getString("genre"));
				songs.add(song);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return songs;
	}

	private String createSearchValue(String string) {
		
		String value;
		
		if (StringUtils.isBlank(string)) {
			value = "%";
		} else {
			value = string;
		}
		
		return value;
	}
	
	@Override
	public void add(Song song) {
		
		String insertSql = "INSERT INTO SONGS VALUES (null, ?, ?, ?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(insertSql)) {

			ps.setString(1, song.getTitle());
			ps.setString(2, song.getArtist());
			ps.setString(3, song.getLabel());
			ps.setDate(4, new java.sql.Date(song.getDate().getTime()));
			ps.setString(5, song.getGenre());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Song	song) {
		String updateSql = "UPDATE songs SET title = ?, artist = ?, label = ?, date = ?, genre = ? WHERE id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(updateSql)) {

			ps.setString(1, song.getTitle());
			ps.setString(2, song.getArtist());
			ps.setString(3, song.getLabel());
			ps.setDate(4, new java.sql.Date(song.getDate().getTime()));
			ps.setString(5, song.getGenre());
			ps.setLong(6, song.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(Long id) {
		String updateSql = "DELETE FROM songs WHERE id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(updateSql)) {

			ps.setLong(1, id);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
