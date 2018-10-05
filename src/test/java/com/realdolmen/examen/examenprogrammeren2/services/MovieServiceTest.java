package com.realdolmen.examen.examenprogrammeren2.services;

import com.realdolmen.examen.examenprogrammeren2.exceptions.NoQueryPossibleException;
import com.realdolmen.examen.examenprogrammeren2.services.MovieService;
import com.realdolmen.examen.examenprogrammeren2.repositories.MovieRepository;
import com.realdolmen.examen.examenprogrammeren2.domain.Movie;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceTest {

    private MovieService movieService;
    private QueryHelper queryHelper;
    private List<Movie> movies;
    private Movie movieObjectToTest;

    @Mock
    private MovieRepository movieRepository;

    @Before
    public void init() {
        movieService = new MovieService(movieRepository);
        movies = new ArrayList<>();
        movieObjectToTest = new Movie(1, "comedy", "Ace ventura");
    }

    @Test
    public void findAllMoviesTest() throws Exception {
        when(movieRepository.find(QueryHelper.findAll())).thenReturn(movies);

        List<Movie> result = movieService.findAllMovies();

        assertTrue(result.isEmpty());
        assertEquals(result, movies);

        verify(movieRepository, times(1)).find(QueryHelper.findAll());
        verifyNoMoreInteractions(movieRepository);

    }

    @Test(expected = NoQueryPossibleException.class)
    public void findAllMoviesTestNoQueryPossibleExceptionThrow() throws Exception {
        when(movieRepository.find(QueryHelper.findAll())).thenThrow(NoQueryPossibleException.class);

        List<Movie> result = movieService.findAllMovies();

        verify(movieRepository, times(1)).find(QueryHelper.findAll());
        verifyNoMoreInteractions(movieRepository);
    }
    
    @Test
    public void findMovieByIdTest() throws Exception {
        movies.add(movieObjectToTest);
        when(movieRepository.find(QueryHelper.findById(1))).thenReturn(movies);
        
        Movie result = movieService.findMovieById(1);
        
        assertEquals(result, movieObjectToTest);
        
        verify(movieRepository, times(1)).find(QueryHelper.findById(1));
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void getAllPalindromeTitlesTestTitleAddedToList() throws NoQueryPossibleException {
        movies.add(new Movie(2, "saippuakivikauppias", "genre"));
        movies.add(new Movie(3, "title", "genre"));
        movies.add(new Movie(4, "aabbaa", "genre2"));
        when(movieRepository.find(QueryHelper.findAll())).thenReturn(movies);
        
        List<String> result = movieService.getAllPalindromeTitles();
        
         assertNotEquals(movies.size(), result.size());
         assertEquals(result.get(0), "saippuakivikauppias");
         assertEquals(result.get(1), "aabbaa");
         assertFalse(result.contains("title"));
         
         verify(movieRepository, times(1)).find(QueryHelper.findAll());
         verifyNoMoreInteractions(movieRepository);
         
        
        
    }

    //TODO test de methode getAllPalindromeTitles, waarbij de MovieRepository methode die wordt opgeroepen een NoQueryPossibleException gooit
    //kijk goed naar de methodes in MovieService, kijk wat er gebeurd en zorg dat je resultaat net is zoals er verwacht wordt
    //Onthou hierbij dat private methoden niet getest worden, maar de publieke methode 
    @Test
    public void getAllPalindromeTitlesTestNoQueryPossibleExceptionThrownAndCatchedTitleNotAdded() throws NoQueryPossibleException {
        when(movieRepository.find(QueryHelper.findAll())).thenThrow(NoQueryPossibleException.class);
        
        List<String> result = movieService.getAllPalindromeTitles();
        
        assertTrue(result.isEmpty());
        
        verify(movieRepository, times(1)).find(QueryHelper.findAll());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void isAPalinDromeTestTrue() {
        String stringToTest = "saippuakivikauppias";
        
        assertTrue(movieService.isAPalindrome(stringToTest));
    }

}
